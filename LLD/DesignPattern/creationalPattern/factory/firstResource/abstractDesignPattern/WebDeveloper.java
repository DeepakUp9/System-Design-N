package LLD.DesignPattern.creationalPattern.factory.firstResource.abstractDesignPattern;


class WebDeveloper implements Employee{

    public int salary(){
        System.out.println("Getting Web Developer salary");
        return 40000;
    }

    public String[] skills(){
        return new String []{"java", "python"};
    }

    @Override
    public String name() {
       return "I'm WebDeveloper";
    }
}