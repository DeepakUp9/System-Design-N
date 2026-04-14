package LLD.DesignPattern.creationalPattern.singleton.firstResource.SIngltoneBreak;

class Singleton implements Cloneable{

    private static Singleton singleton;

    private Singleton() {}

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

  @Override
  public Object clone() throws CloneNotSupportedException{
    return super.clone();
    //solutions return singleton; //same object return 
  }

}

//////=========

class Main {
    public static void main(String args[]) throws Exception, CloneNotSupportedException{

      Singleton obj = Singleton.getSingleton();
      System.out.println(obj.hashCode());
  
      //get different object as above one 
      Singleton  obj2 = (Singleton) obj.clone();
      System.out.println(obj2.hashCode());
    }

}