package LLD.DesignPattern.creationalPattern.singleton.firstResource.SIngltoneBreak;

//break singleton pattern using reflection api

import java.lang.reflect.Constructor;

class Singleton {
  private  static Singleton singleton;

  private Singleton(){

  }
  //Lazy way of creatin Singleton object 
  public static Singleton getSingleton(){
    if(singleton == null){
        synchronized(Singleton.class){
            if(singleton == null){
                singleton = new Singleton();
            }
        }
    }
    return singleton;
  }
}


//////====================================================
class client {
    public static void main(String args[]) throws Exception{

        Singleton obj = Singleton.getSingleton();
        System.out.println(obj.hashCode());

        //1. break singleton pattern using reflection api
            Constructor<Singleton> constructor = Singleton.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Singleton obj1 = constructor.newInstance();
            System.out.println(obj1.hashCode());

           /* Solutions 
              1. if object is there => throws Exception from inside constructor
                  private Singleton() {
                    if(singleton != null ) throw new RuntimeException("you are are trying to break");
                  }
              2. use enum 
                  public enum SingleTone{
                    INSTANCE
                  }
                  // SingleTone obj = SingleTone.INSTANCE;
            */
    }
}
