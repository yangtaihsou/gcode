package com.bit.code.database;

import com.bit.code.util.FileReadUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.stream.Collectors.toList;

public class FileTest {

    String filename="D:\\jd\\CodeGenerate-pay\\Template\\pay-database\\";
    @Test
    public void testseparator(){
        System.out.println(File.separator);
    }
    @Test
    public void testlistFile() throws IOException {
        String fileTypeReg = ".*(.pro|.properties)$";
       List<File>  files =  FileReadUtils.listFile(filename,fileTypeReg);
        Assert.assertEquals(files.size(),9);
        Charset charset = Charset.forName("GBK");

       String path = files.stream().filter(x->x.getName().equals("tableConfig.properties")).findFirst().get().getAbsolutePath();
        for(File file:files){
            System.out.println(file);
            Files.lines(Paths.get(file.getAbsolutePath()),charset).forEach(System.out::print);

        }
    }
    @Test
    public void testlistDirectory() throws IOException {
        List<File>  files =  FileReadUtils.listDirectory(filename);
        Assert.assertEquals(files.size(),4);
        for(File file:files){
            System.out.println(file.getName());
        }
    }
    @Test
    public void testReadProperties(){
        Charset charset = Charset.forName("GBK");
        Map<String,String>  map = FileReadUtils.readProperties("D:\\jd\\CodeGenerate-pay\\src\\main\\resources\\Table2FileParaConfig\\bean.properties",charset);
        Assert.assertEquals(map.size(),5);
    }
    @Test
    public void testreadContext(){
        Charset charset = Charset.forName("GBK");
        filename = filename +"TempFile\\pojo.vm";
        String sb =  FileReadUtils.readContext(filename,charset);
        System.out.println(sb.toString());
    }
    @Test
    public void testJDK8FileList() throws IOException {
        Charset charset = Charset.forName("GBK");

       List list = Arrays.stream(new File(filename).listFiles()).filter(x->x.isDirectory()).collect(Collectors.toList());

        Files.lines(Paths.get(filename +"TempFile\\pojo.vm"), charset).forEach(System.out::print);

        Files.newDirectoryStream(
                Paths.get(filename),
                path -> path.toFile().isDirectory())
                .forEach(System.out::print);

        String[] files = new File(filename).list();

       // System.out.println(files);

        List<File> fileList = Stream.of(new File(filename).listFiles())
                .flatMap(file -> file.isFile()  ?
                        Stream.of(file) : Stream.of(file.listFiles()))
                .filter(File::isFile)
                .collect(toList());

        //System.out.println(fileList);

        Files.newDirectoryStream(
                Paths.get(filename),
                path -> path.toString().endsWith(".properties"))
                .forEach(System.out::println);

        Files.walkFileTree(Paths.get(filename), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // TODO Auto-generated method stub
                // return super.preVisitDirectory(dir, attrs);
               // System.out.println("正在访问：" + dir + "目录");
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // TODO Auto-generated method stub
                // return super.visitFile(file, attrs);
                if (file.endsWith(".properties")) {
                    System.out.println(file.toString());
                }
                return FileVisitResult.CONTINUE; // 没找到继续找
            }

        });
    }
    @Test
    public void testZip() throws IOException {
        String sourceDirectoryPath = "D:\\yangkuan\\zip";
        String zipPath = "D:\\yangkuan\\zip\\source.zip";
        Path zipFilePath = Files.createFile(Paths.get(zipPath));
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            Path sourceDirPath = Paths.get(sourceDirectoryPath);
            Files.walk(sourceDirPath).filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry("test\\1\\"+sourceDirPath.relativize(path).toString());
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            zipOutputStream.write(Files.readAllBytes(path));
                            zipOutputStream.closeEntry();
                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    });
        }
    }
}
