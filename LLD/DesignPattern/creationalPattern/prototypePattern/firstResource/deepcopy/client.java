package LLD.DesignPattern.creationalPattern.prototypePattern.firstResource.deepcopy;


/* Deep copy 
    coping each propties one by one 
*/

public class client{

   public static void main(String args[]){
       System.out.println("Creating object using prototype design");

       NetworkConnection networkConnection = new NetworkConnection();

       networkConnection.setIp("192.168.4.4");
       networkConnection.loadVeryImportantData();

       System.out.println(networkConnection);

       //now we want new object of network connections but by copy of old object 
       // or  By Default it shallow copy
       try{
          NetworkConnection  networkConnection2 = (NetworkConnection)networkConnection.clone();
          System.out.println(networkConnection2);
       }catch( CloneNotSupportedException e){
          e.printStackTrace();
       }

   }
}