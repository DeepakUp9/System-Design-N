package LLD.DesignPattern.creationalPattern.factory.firstResource.factoryDesignpattern;

class WebDeveloper implements Employee{

    public int salary(){
        System.out.println("Getting Web Developer salary");
        return 40000;
    }

    public String[] skills(){
        return new String []{"java","python"};
    }
}