package com.old.code.generate;

import com.old.code.generate.domain.FileConfig;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * User: yangkuan@old.com
 * Date: 16-2-22
 * Time: 下午7:09
 */
public class GenerateCarCode {
    public static void main(String[] args) {

        try {
            init();
            for (FileConfig fileConfig:fileList){
            Map<String, Object> velocityParameterMap = new HashMap<String, Object>();
            Properties prop = new Properties();
                BufferedReader br =new BufferedReader( new InputStreamReader(new FileInputStream(fileConfig.getPackagePath()),"GB2312"));
                System.out.println(fileConfig.getPackagePath());
                StringBuffer sb=new StringBuffer();
               String  temp=br.readLine();
                while(temp!=null){

                    System.out.println(temp);
                    temp=br.readLine();
                }
                System.out.println(fileConfig.getPackagePath());
          //  GeneratorCode.renderVelocityTemplate(velocityParameterMap, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static List<FileConfig> fileList = new ArrayList<FileConfig>();

    public static void init() throws Exception {


        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("carbx");
        if (url != null) {
            String type = url.getProtocol();
            if(type.equals("file")){
                File file = new File(url.getPath());
                File[] childFiles = file.listFiles();
                for (File childFile:childFiles){

                    FileConfig fileConfig = new FileConfig();
                    fileConfig.setVelocityName(childFile.getName());
                    fileConfig.setPackagePath(childFile.getAbsolutePath());
                    fileList.add(fileConfig);
                }
            }

    }
}
}
