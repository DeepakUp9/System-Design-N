package DesignPattern.creationalPattern.singleton.firstResource.lazyInitialization;

public class Singletonlazy {
    private static Singletonlazy singleton;

    private Singletonlazy() {}

    //Lazy way of creation Singleton class 
    public static Singletonlazy getSingleton(){
        if(singleton == null){
            synchronized(Singletonlazy.class){ //synchronized block using to prevent the multithreating 
                if(singleton == null){
                    singleton = new Singletonlazy();
                }
            }
        }
       return singleton;
    }
}


class client{
    public static void main(String [] args){
      Singletonlazy obj =  SingletonLaxy.getSingleton();
      System.out.println(obj.hashCode());

      Singletonlazy obj2 =  SingletonLaxy.getSingleton();
      System.out.println(obj2.hashCode());

      //it will give same hashCode i.e same object is creating while calling two times 
        
    }
}

/*
    1. make constructor private
    2. object create with help of the method 
    3. create field to store object is private
*/