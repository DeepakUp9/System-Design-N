package LLD.DesignPattern.structuralPattern.adapter.firstResource;

/* 
   The iterator pattern provide a way to access the element of an object without exposing it's underlying implementation.

*/

//link for diagram 
   // https://lucid.app/lucidspark/97842f05-757e-4a44-9ebc-ecf14c5a0d88/edit?viewport_loc=-16%2C-311%2C1920%2C931%2C0_0&invitationId=inv_ea1f55e1-9105-4f41-a32e-67af521e56b7


public class Client{

   public static void main(String args[]){
    //   AppleCharger charger = new ChargerXYZ();
    //   Iphone13 iphone13 = new Iphone13(charger);
    //   iphone13.chargePhone();


    AppleCharger charger = new AdapterCharger(new DkCharger());
    Iphone13 iphone13 = new Iphone13(charger);
    iphone13.chargerIphone();

   }
}