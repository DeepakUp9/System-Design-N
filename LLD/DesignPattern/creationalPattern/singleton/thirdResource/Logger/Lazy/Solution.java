package LLD.DesignPattern.creationalPattern.singleton.thirdResource.Logger.Lazy;


class Logger {

    private static Logger logger = new Logger();

    private Logger(){
        if(logger != null) throw new RuntimeException("Trying to break the singleton class");
    }

    public static Logger getInstance(){
      return logger;
    }

    public void log(String message){
        System.out.println("logger message :"+ message);
    }


}


class Solution {
    public static void main(String [] args){
        Logger obj = Logger.getInstance();
        Logger obj1 = Logger.getInstance();
        System.out.println(obj.hashCode());
        System.out.println(obj1.hashCode());
        obj.log("Network error");

    }
}