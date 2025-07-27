package LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem;

public class Job {
    public void generateReport(){
        System.out.println("generating the report");
    }

    public void sendNotification(){
         System.out.println("sending the notification");
    }
    
    public void backupDB(){
        System.out.println("backup the db");
    }
}
