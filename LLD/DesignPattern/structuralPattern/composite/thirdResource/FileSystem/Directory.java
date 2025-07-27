package DesignPattern.structuralPattern.composite.thirdResource.FileSystem;

import java.util.ArrayList;
import java.util.List;

public class Directory implements FileSystem{
    private List<FileSystem>fileList;
    private String dName;
    private int dsize; 

    Directory(String dName, int dsize ){
        this.dName = dName;
        this.dsize = dsize;
        fileList = new ArrayList<>();
    }

    public void add(FileSystem fileSystem){
        fileList.add(fileSystem);
    }


    @Override
    public int sizeCalculation() {
       int totalSize = 0;
       for(FileSystem list : fileList){
           totalSize += list.sizeCalculation();
       }
       return totalSize + dsize;
    }

    public boolean deletion(FileSystem fileSystem) {
       fileList.remove(fileSystem);
       return true;
    }

    @Override
    public void ls() {
       System.out.println("dictory name :" + dName);
       for(FileSystem list : fileList){
           list.ls();
       }
    }
    
}
