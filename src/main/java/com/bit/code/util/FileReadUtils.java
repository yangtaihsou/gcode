package com.bit.code.util;

import com.old.code.generate.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileReadUtils {
    private static  final Logger log = LoggerFactory.getLogger(Utils.class);

    /**
     * 根据文件类型，匹配目录下所有的文件
     * @param filepath
     * @param fileTypeRex
     * @return
     */
    public static List<File> listFile(String filepath,String fileTypeRex){
        List<File> filePath = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get(filepath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

                    if (file.toFile().getAbsolutePath().matches(fileTypeRex)) {
                        filePath.add(file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }catch (Exception e){
            log.info("遍历目录下的文件异常:"+e.getMessage());
        }
        return filePath;
    }

    /**
     * 读取Propertie的配置
     * @param filePath
     * @param charset
     * @return
     */
    public static Map<String,String> readProperties(String filePath,Charset charset){
        Map<String,String> configMap = new HashMap<>(8);
        try {
            Files.lines(Paths.get(filePath), charset).forEach(x->{
                if(x.contains("=")){
                    String key = x.substring(0,x.indexOf("="));
                    String value = x.substring(x.indexOf("=")+1,x.length());
                    configMap.put(key.trim(),value.trim());
                }else{
                    configMap.put(x,null);
                }
            });
        } catch (Exception e) {
            log.info("解析文件配置异常:"+e.getMessage());
        }
        return configMap;
    }

    /**
     * 读取文件内容
     * @param fileName
     * @param charset
     * @return
     */
    public static String readContext(String fileName,Charset charset){
        StringBuffer stringBuffer = new StringBuffer();
        try {
            Files.lines(Paths.get(fileName), charset).forEach(x->{
                stringBuffer.append(x).append("\r\n");
            });
        } catch (Exception e) {
            log.info("读取文件内容异常:"+e.getMessage());
        }
        return stringBuffer.toString();
    }

    /**
     * 列出路径下的一级目录集合
     * @param filepath
     * @return
     */
    public static List<File> listDirectory(String filepath){
        List<File> fileList = new ArrayList<>();
        try {
            Files.newDirectoryStream(
                    Paths.get(filepath),
                    path -> path.toFile().isDirectory())
                    .forEach( x->fileList.add(x.toFile()));
        } catch (Exception e) {
            log.info("遍历目录异常:"+e.getMessage());
        }
        return fileList;
    }

    public static List<File> listFile(String filepath){
        List<File> fileList = new ArrayList<>();
        try {
            Files.newDirectoryStream(
                    Paths.get(filepath))
                    .forEach( x->fileList.add(x.toFile()));
        } catch (Exception e) {
            log.info("遍历目录异常:"+e.getMessage());
        }
        return fileList;
    }
}
