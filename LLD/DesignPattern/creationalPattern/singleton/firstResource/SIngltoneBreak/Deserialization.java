package LLD.DesignPattern.creationalPattern.singleton.firstResource.SIngltoneBreak;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class Singleton implements Serializable{

    private  static Singleton singleton;

    Singleton() {}

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

    public Object readResolve(){
        return singleton;
    }
}

//////=========

class Main {
    public static void main(String args[]) throws Exception{
    //1. Serilization
      Singleton obj = Singleton.getSingleton();
      System.out.println(obj.hashCode());
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("abc.ob"));
      oos.writeObject(obj);
      System.out.println("Serilization is done ");

      
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream("abc.ob"));
      Singleton obj2 = (Singleton)ois.readObject();
      System.out.println(obj2.hashCode());
      System.out.println("Deserilization  is done ");

      /* Solution
        1. implementing readResolve method inside the Singletone class
      */

    }
}
