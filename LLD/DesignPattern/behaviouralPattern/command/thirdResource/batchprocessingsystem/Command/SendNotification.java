package LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem.Command;

import LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem.Job;

public class SendNotification implements Command{

    private Job job;
    public SendNotification(Job job){
        this.job = job;
    }

    @Override
    public void excute() {
       job.sendNotification();
    }
    
}
