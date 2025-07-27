package LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem.Command;

import LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem.Job;

public class BackupDB implements Command{
    private Job job;

    public BackupDB(Job job){
        this.job = job;
    }

    
    @Override
    public void excute() {
        job.backupDB();
    }
    
}
