package LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem.Command;

import LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem.Job;

public class GenerateReport implements Command{
    private Job job;

    public GenerateReport(Job job){
        this.job = job;
    }
    
    @Override
    public void excute() {
       job.generateReport();
    }
    
}
