package com.silvia.blogwebsite.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.ZoneId;

public class FileManager {
    private String root;

    public String getRoot() {
        return root;
    }
    public FileManager(){}

    public boolean createDirectory(String fileRoot, String fileName){
        File root = new File(fileRoot);
        if(root.isDirectory()){
            File file = new File(fileRoot+"/"+fileName);
            if(file.mkdirs()||file.isDirectory()){
                System.out.println(file.getAbsolutePath()+" is directory.");
                this.root = fileRoot+"/"+fileName;
                return true;
            }
            else {
                System.out.println("[IO Error]: make directory failed!" + fileRoot+"/"+fileName);
            }
        }
        else {
            System.out.println("[IO Error]: "+fileRoot+" is not a directory!");
        }
        return false;
    }

    public static String getFileCreatedTime(String filePath){
        String createTime = "";
        try{
            Path path = Paths.get(filePath);
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

            FileTime creationTime = attributes.creationTime();
            LocalDate creationDate = creationTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            createTime = creationDate.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return createTime;
    }

    public static String getFileLatestUpdate(String filePath){
        String lastUpdate = "";
        try{
            Path path = Paths.get(filePath);
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);

            FileTime lastModifiedTime = attributes.lastModifiedTime();
            LocalDate lastModifiedDate = lastModifiedTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            lastUpdate =lastModifiedDate.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return lastUpdate;
    }
}
