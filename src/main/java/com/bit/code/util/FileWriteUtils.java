package com.bit.code.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileWriteUtils {
    private static final Logger log = LoggerFactory.getLogger(FileWriteUtils.class);
    ZipOutputStream zipOutputStream = null;
    String targetSrcDir = null;

    /**
     * 建立目录
     * @param localPath
     * @return
     */
    public synchronized FileWriteUtils mkTargetSrcDir(String localPath){
        targetSrcDir = localPath;
        try {
            Files.createDirectories(Paths.get(localPath));
        } catch (Exception e) {
            log.info("创建多级目录异常:"+e.getMessage());
        }
        return this;
    }

    /**
     * 创建zip压缩包流
     * @param fileName
     * @return
     * @throws IOException
     */
    public synchronized FileWriteUtils buildZipOs(String fileName) throws IOException {
        Path filePath = Paths.get(targetSrcDir + File.separator + fileName+".zip");
        Files.deleteIfExists(filePath);
        this.zipOutputStream = new ZipOutputStream(Files.newOutputStream(Files.createFile(filePath)));
        return this;
    }

    /**
     * 将文本内容放进压缩文件
     * @param zipFilePath
     * @param text
     * @return
     */
    public FileWriteUtils zip(String zipFilePath,String text) {
        try {
            ZipEntry zipEntry = new ZipEntry(zipFilePath);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(text.getBytes("UTF-8"));
        } catch (Exception e) {
            log.info("生成压缩包异常:" + e.getMessage());
        }
        return this;
    }



    /**
     * 将文本生成生成本地文件
     * @param fileSrcPath
     * @param fileName
     * @param text
     */
    public void writeSrcFile(String fileSrcPath,String fileName,String text){
        try {
            String path = targetSrcDir  + fileSrcPath;
            Files.createDirectories(Paths.get(path));
            Files.write(Paths.get(path+ File.separator + fileName), text.getBytes());
        } catch (IOException e) {
            log.info("生成文件，发生异常:" + e.getMessage());
        }
    }

    /**
     * 关闭压缩文件的输出流
     */
    public  void closeZipOs() {
        try {
            if (zipOutputStream != null) {
                zipOutputStream.close();
            }
        } catch (IOException e) {
            log.info("关闭压缩包的输出流，发生异常:" + e.getMessage());
        }
    }
}
