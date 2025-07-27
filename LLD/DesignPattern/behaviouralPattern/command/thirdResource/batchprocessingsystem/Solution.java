package LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem;

import LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem.Command.BackupDB;
import LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem.Command.Command;
import LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem.Command.GenerateReport;
import LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem.Command.SendNotification;

public class Solution {
    public static void main(String[] args) {
        Job job = new Job();
       
        JobSchedular jobSchedular = new JobSchedular();

        Command report = new GenerateReport(job);
        Command sendNotification = new SendNotification(job);
        Command backupDb = new BackupDB(job);

        jobSchedular.schedule(report);
        jobSchedular.schedule(sendNotification);
        jobSchedular.schedule(backupDb);

        jobSchedular.run();



       
    }
}
