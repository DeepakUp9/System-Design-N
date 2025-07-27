package DesignPattern.structuralPattern.composite.thirdResource.FileSystem;

public class File implements FileSystem{
    private String fName;
    private int fSize;

    public File(String fName, int fSize) {
        this.fName = fName;
        this.fSize = fSize;
    }

    public int sizeCalculation(){
        return this.fSize;
    }

    public void ls(){
      System.out.println("file name :" + fName);
    }


}
