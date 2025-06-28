package LLD.DesignPattern.creationalPattern.singleton.thirdResource.Logger.Eager;


class Logger {
    private static Logger logger;

    private Logger(){
      if(logger != null) throw new RuntimeException("Trying to break the singleton class");
    }

    public static Logger getInstance(){
        if(logger == null){
            synchronized(Logger.class){
              if(logger == null){
                return logger = new Logger();
              }
            }
        }
        return logger;
    }

    public void log(String message){
        System.out.println("logger message :"+ message);
    }
}

public class Solution{
    public static void main(String [] args){
        Logger obj1 = Logger.getInstance();
        Logger obj2 = Logger.getInstance();
        System.out.println(obj1.hashCode());
        System.out.println(obj2.hashCode());
        obj1.log("database connection...");

        //there are 3way to break this
        //1. reflection api
        //2. serialization/deserialization
        //3. ENUM
    }
}