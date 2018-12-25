package com.old.code.generate;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import com.bit.code.util.Contants;
import com.old.code.generate.domain.FileConfig;
import com.old.code.generate.domain.GlobalConfig;
import com.old.code.generate.util.Utils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class GeneratorCode {

    /**存放要生成文件的配置，如输出目录、扩展名，这里是每张表映射过来，要生成list里所有的文件**/
    static List<FileConfig> table2FileParaConfigList = new ArrayList<FileConfig>();

    /**生成一个指定的文件，里面的参数可能包含所有表相关的属性**/
    static List<FileConfig> singleFileParaConfigList = new ArrayList<FileConfig>();

    static Map<String,String> configMap = new HashMap<String, String>();

    /**不用添加的属性**/
    static Map<String,String> excludeFieldsMap = new HashMap<String, String>();

    /**不用添加的表**/
    static Map<String,String> excludeTablesMap = new HashMap<String, String>();
    static GlobalConfig globalConfig = new GlobalConfig();
	public static void main(String[] args) {
		try {
            init();
			generate();
        } catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static void init() throws Exception {
        Utils.readProperties("config/SystemConfig.properties",configMap);
        try{
            globalConfig.setOutputPath(configMap.get("outputPath"));
            globalConfig.setSlqDriver(configMap.get("slq.driver"));
            globalConfig.setSqlType(configMap.get("sql.type"));
            globalConfig.setSqlPass(configMap.get("sql.pass"));
            globalConfig.setSqlUrl(configMap.get("sql.url"));
            globalConfig.setSqlUser(configMap.get("sql.user"));

            String excludeFields = configMap.get("excludeFields");
            String[] excludeFieldArr = excludeFields.split(",");
            for (String excludeField:excludeFieldArr){
                excludeFieldsMap.put(excludeField,excludeField);
            }
            String excludeTables  = configMap.get("excludeTables");
            String[] excludeTablesArr = excludeTables.split(",");
            for (String excludeField:excludeTablesArr){
                excludeTablesMap.put(excludeField,excludeField);
            }
        }catch (Exception e){
          System.out.println("没有配置要过滤的字段"+e);
        }

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("Table2FileParaConfig");
        if (url != null) {
            String type = url.getProtocol();
            if(type.equals("file")){
                File file = new File(url.getPath());
                File[] childFiles = file.listFiles();
                for (File childFile:childFiles){
					Boolean isFile = childFile.isFile();
					if(isFile){
						if(!childFile.getName().endsWith(".properties")){
							continue;
						}
					}
                    FileConfig fileConfig = new FileConfig();
                    Utils.readProperties(childFile,fileConfig);
                    table2FileParaConfigList.add(fileConfig);
                }
            }

            url = loader.getResource("SingleFileParaConfig");
            if (url != null) {
                type = url.getProtocol();
                if(type.equals("file")){
                    File file = new File(url.getPath());
                    File[] childFiles = file.listFiles();
                    for (File childFile:childFiles){
                        FileConfig fileConfig = new FileConfig();
                        Utils.readProperties(childFile,fileConfig);
                        singleFileParaConfigList.add(fileConfig);
                    }
                }
            }
        }else{
            throw new Exception("需要生成的文件，没有目录去配置参数");
        }
    }

    /*
     * 得到connection
     */
	public static Connection getSQLConnection() throws Exception {
		Connection con = null;
		try {
			Class.forName(globalConfig.getSlqDriver());

			con = DriverManager.getConnection(globalConfig.getSqlUrl(), globalConfig.getSqlUser(), globalConfig.getSqlPass());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return con;
	}



	public static void generate() throws Exception {
		Connection conn = getSQLConnection();
        String sqlType = globalConfig.getSqlType();
        List<String> alltables = null;
        if(sqlType.equals(Contants.MYSQL)){
            alltables = getMYSQLAllTables(conn);
        } else  if(sqlType.equals(Contants.MSSQL)){
            alltables = getMSSQLAllTables(conn);
        }

		List<Map<String, Object>> velocityParameterMapList = getAllTable2VelocityParameterList(sqlType, alltables, conn);
		conn.close();
		for (Map<String, Object> velocityParameterMap : velocityParameterMapList) {
            for(FileConfig fileConfig:table2FileParaConfigList){
                velocityParameterMap.put("arr0", "[0]");
                velocityParameterMap.put("parameterMap", "parameterMap");
                velocityParameterMap.put("excludeFieldsMap",excludeFieldsMap);
                velocityParameterMap.put("package", fileConfig.getPackagePath());
                velocityParameterMap.put("importBean","com.old.jr.fin.peerlending.account.export.vo." + (String) velocityParameterMap.get("className"));
                velocityParameterMap.put("importService","com.old.jr.fin.peerlending.account.service." + (String) velocityParameterMap.get("className")+"Service");
                velocityParameterMap.put("importMapper","com.old.jr.fin.peerlending.account.dao." + (String) velocityParameterMap.get("className")+"Mapper");
                String str = renderVelocityTemplate(velocityParameterMap, fileConfig.getVelocityName());
                String childPath = fileConfig.getSrcTargetChildPath();
                if(fileConfig.getFileType().equals("java")){
                    childPath = childPath +"/"+ fileConfig.getPackagePath().replace(".", "/");
                }
                createFile(childPath, (String) velocityParameterMap.get("className") + fileConfig.getFileExtendName(), fileConfig.getFileType(), str);
            }
        }
        for(FileConfig fileConfig:singleFileParaConfigList){
            String childPath = fileConfig.getSrcTargetChildPath();
            if(fileConfig.getFileType().equals("java")){
                childPath = childPath + fileConfig.getPackagePath().replace(".", "/");
            }
            String str = renderComplexVelocityTemplate(velocityParameterMapList, fileConfig.getVelocityName());
            createFile( childPath, fileConfig.getFileExtendName(), fileConfig.getFileType(), str);
        }
	}





	public static void createFile(String childPath,String fileName, String fileType, String content) throws IOException {
		File f = new File(globalConfig.getOutputPath()  + childPath+"/"+fileName + "." + fileType);
		// WebUtil.writeToFile(outputPath+"src/"+ beanName.replace(".","/" )
		// +".java", sb.toString());
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		f.createNewFile();
		// if(f.exists()){
		// FileWriter factory=new FileWriter(f);

		OutputStreamWriter factory = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
		factory.write(content);
		factory.flush();
		factory.close();
		// }
		getFileContent(f.getAbsolutePath(), "UTF-8");
	}

	public static String getFileContent(String fullpath, String fileCoding) {
		try {
			File f = new File(fullpath);
			// //System.out.println(f.getAbsolutePath());
			if (!f.exists()) {
				return "";
			}
			byte[] buf = getFileContentB(f);
			return new String(buf, fileCoding);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static byte[] getFileContentB(File f) {
		try {
			if (!f.exists()) {
				return null;
			}
			FileInputStream in = new FileInputStream(f);
			int length = in.available();
			byte[] buf = new byte[length];
			in.read(buf);
			in.close();
			return buf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 得到connection
	 */
	public static Connection getMSSQLConnection() {
		Connection con = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection("jdbc:sqlserver://192.168.229.56:1433;databaseName=moneyMonitor2", "yangtao", "!q2w3e4r5t");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}


	/*
	 * 得到库里的所用表�?
	 */
	public static List<String> getMSSQLAllTables(Connection conn) throws Exception {
		List<String> alltables = new ArrayList<String>();
		PreparedStatement pstmt;
		ResultSet rs;
		pstmt = conn.prepareStatement("select [name] from sysobjects where xtype='U'");
		rs = pstmt.executeQuery();
		while (rs.next()) {
			String tablename = rs.getString(1);
			alltables.add(tablename);
		}
		return alltables;
	}

	/*
	 * 得到库里的所用表�?
	 */
	public static List<String> getMYSQLAllTables(Connection conn) throws Exception {
		List<String> alltables = new ArrayList<String>();
		PreparedStatement pstmt;
		ResultSet rs;
		pstmt = conn.prepareStatement("show tables");
        //pstmt = conn.prepareStatement("select * from TABLES where  TABLE_NAME='item'");
		rs = pstmt.executeQuery();
		while (rs.next()) {
			String tablename = rs.getString(1);
			alltables.add(tablename);
		}
		return alltables;
	}

	/*
	 * �?��表生成的velocity模版参数放入list。一张表对应�?��map
	 */
	public static List<Map<String, Object>> getAllTable2VelocityParameterList(String sqlserverType, List<String> alltables, Connection conn) throws Exception {
        List<Map<String, Object>> velocityParameterMapList = new ArrayList<Map<String, Object>>();
        try{

        Map<String,String> tableMap = new HashMap<String, String>();
		for (int j = 0; j < alltables.size(); j++) {
			String tablename = alltables.get(j);
            Set<String> keySet = excludeTablesMap.keySet();
            Iterator<String> keys =  keySet.iterator();
            Boolean continueFlag = false;
            while (keys.hasNext()) {
                String key = (String)keys.next();
                if(tablename.contains(key)){
                    continueFlag = true;
                    break;
                }
            }
            if(continueFlag){
                continue;
            }
            tableMap.put(tablename,"");
            VelocityParaContext context = new VelocityParaContext();
			Map<String, Object> velocityParameterMap = context.generateTable2VelocityParameter(sqlserverType, conn, tablename);
			velocityParameterMapList.add(velocityParameterMap);
		}
            System.out.println("数据库表名如下:"+tableMap);
            Set<String> keySet = VelocityParaContext.paraMap.keySet();
            Iterator<String> keys =  keySet.iterator();
            while (keys.hasNext()) {
                String key = (String)keys.next();
                System.out.println(key+"="+VelocityParaContext.paraMap.get(key));
            }
    }catch (Exception e)
    {
        System.out.println("getAllTable2VelocityParameterList异常::"+e.getMessage());
    }

        return velocityParameterMapList;
    }



	/*
	 * 更具模板生成文件的内容
	 */
	public static String renderVelocityTemplate(Map<String, Object> params, String velocityName) {
		BufferedReader reader = null;
		try {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(velocityName);
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			return evaluate(reader, params);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

		}
		return null;
	}

    /**
     * 更具模板，将参数带入生成综合的文件
     * @param params
     * @param velocityName
     * @return
     */
    public static String renderComplexVelocityTemplate(List<Map<String, Object>> params, String velocityName) {
        BufferedReader reader = null;
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(velocityName);
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("param", params);
            StringWriter writer = new StringWriter();
             Velocity.evaluate(velocityContext, writer, "", reader);
             return  writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        }
        return null;
    }

	public static String evaluate(Reader reader, Map<String, Object> variables) {
		// VelocityEngine velocityEngine = new VelocityEngine();
		// velocityEngine.init();
		VelocityContext velocityContext = new VelocityContext();

		if (variables != null) {
			for (Map.Entry<String, Object> entry : variables.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key != null && key.trim().length() > 1) {
					velocityContext.put(key, value);
				}
			}
		}

		StringWriter writer = new StringWriter();
        try {
            Velocity.evaluate(velocityContext, writer, "", reader);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return writer.toString();
	}
}
