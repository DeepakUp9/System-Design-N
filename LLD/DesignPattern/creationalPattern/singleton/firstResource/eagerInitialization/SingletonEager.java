package LLD.DesignPattern.creationalPattern.singleton.firstResource.eagerInitialization;

public class SingletonEager {
    private static SingletonEager singleton = new SingletonEager();

    private SingletonEager() {}
  
    //Eager way of creation Singleton object 
    public static SingletonEager getSingleton(){
        return singleton;
    }
}

class client{
  public static void main(String [] args){
    SingletonEager obj =  SingletonEager.getSingleton();
    System.out.println(obj.hashCode());

    SingletonEager obj2 =  SingletonEager.getSingleton();
    System.out.println(obj2.hashCode());

    //it will give same hashCode i.e same object is creating while calling two times 
  }
}

