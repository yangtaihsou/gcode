package com.old.code.generate.util;

import com.old.code.generate.domain.FileConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

public class Utils {
    private static  final Logger log = LoggerFactory.getLogger(Utils.class);
    /**
     *  读取properties的全部信息
     */
    public static void readProperties(String filePath,Map<String,String> map)   {
        try {

            Properties props = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream in = loader.getResourceAsStream(filePath);
            props.load(in);
            Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                map.put(key, (String)props.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
           // throw new Exception("读取配置文件失败");
        }
    }

    public static  void readProperties(File file,FileConfig fileConfig) throws Exception {
        InputStream in  = new FileInputStream(file);
        Properties props = new Properties();
        props.load(in);
        fileConfig.setPackagePath(props.getProperty("packagePath"));
        fileConfig.setFileExtendName(props.getProperty("fileExtendName"));
        fileConfig.setFileType(props.getProperty("fileType"));
        fileConfig.setVelocityName(props.getProperty("velocityName"));
        fileConfig.setSrcTargetChildPath(props.getProperty("srcTargetChildPath"));
    }

    public static Properties readProperties(String filePath){
        try {
            Properties props = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream in = loader.getResourceAsStream(filePath);
            props.load(in);
            return props;
        }catch (Exception e){
            log.info("读取配置文件失败:"+e.getMessage());
        }
        return null;
    }
    public static void main(String[] agrs){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("Table2FileParaConfig");
        if (url != null) {
            String type = url.getProtocol();
            if (type.equals("file")) {
                File file = new File(url.getPath());
                File[] childFiles = file.listFiles();
                for (File childFile : childFiles) {
                    Boolean isFile = childFile.isFile();
                    if (isFile) {
                        if (!childFile.getName().endsWith(".properties")) {
                            continue;
                        }

                        Properties props = Utils.readProperties(childFile.getName());
                        props.getProperty("");
                    }
                }
            }
        }
    }
}
