package DesignPattern.creationalPattern.singleton.firstResource.lazyInitialization;

public class SingletonLaxy {
    private static SingletonLaxy singleton;

    private SingletonLaxy() {}

    //Lazy way of creation Singleton class 
    public static SingletonLaxy getSingleton(){
        if(singleton == null){
            synchronized(SingletonLaxy.class){ //synchronized block using to prevent the multithreating 
                if(singleton == null){
                    singleton = new SingletonLaxy();
                }
            }
        }
       return singleton;
    }
}


class client{
    public static void main(String [] args){
      SingletonLaxy obj =  SingletonLaxy.getSingleton();
      System.out.println(obj.hashCode());

      SingletonLaxy obj2 =  SingletonLaxy.getSingleton();
      System.out.println(obj2.hashCode());

      //it will give same hashCode i.e same object is creating while calling two times 
        
    }
}

/*
    1. make constructor private
    2. object create with help of the method 
    3. create field to store object is private
*/