package LLD.DesignPattern.creationalPattern.factory.firstResource.factoryDesignpattern;

public class AndroidDeveloper implements Employee{

    public int salary(){
        System.out.println("Getting Android Developer salary");
        return 50000;
    }

    public String[] skills(){
        return new String []{"kotline", "ios"};
    }
}