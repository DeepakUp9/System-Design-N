package LLD.DesignPattern.behaviouralPattern.templateMethod.thirdResource.dataprocessingsystem;

public abstract class DataProcessing {
   
   public abstract void readData(); 
   public abstract void parseData();
   public abstract void  savingData(); 
  
   public final void processData(){
        //step1
        readData();

        //step 2
        parseData();

        //step3 
        savingData();
   }
}
