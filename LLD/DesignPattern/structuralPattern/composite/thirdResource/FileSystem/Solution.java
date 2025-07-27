package DesignPattern.structuralPattern.composite.thirdResource.FileSystem;

import java.util.Dictionary;

public class Solution {
    public static void main(String[] args) {
        Directory dictoryMovie = new Directory("Movie", 100);
        FileSystem movie1 = new File("Hera Pheri", 5);
        FileSystem movie2 = new File("Halchal", 3);
        FileSystem movie3 = new File("idiot", 4);
        dictoryMovie.add(movie1);
        dictoryMovie.add(movie2);
        dictoryMovie.add(movie3);

        Directory dictoryDoc = new Directory("Document", 50);
        FileSystem graductionCerti = new File("Marksheet", 3);
        FileSystem timepass = new File("time", 9);
        FileSystem phtos = new File("Photo", 9);
        dictoryDoc.add(graductionCerti);
        dictoryDoc.add(phtos);
        dictoryDoc.add(timepass);
        
        dictoryDoc.add(dictoryMovie);


        System.out.println(dictoryMovie.sizeCalculation());
        System.out.println(dictoryDoc.sizeCalculation());

        System.out.println(phtos.sizeCalculation());

       //dictoryMovie.ls();

       dictoryDoc.ls();

       dictoryDoc.deletion(timepass);

       System.out.println();
       System.out.println();

       dictoryDoc.ls();




    }
}
