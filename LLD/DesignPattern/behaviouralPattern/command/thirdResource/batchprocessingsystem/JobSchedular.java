package LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import LLD.DesignPattern.behaviouralPattern.command.thirdResource.batchprocessingsystem.Command.Command;

public class JobSchedular {
      private final Queue<Command> jobQueue = new ConcurrentLinkedQueue<>();

    public void schedule(Command command) {
        jobQueue.offer(command);
    }

    public void run() {
        while (!jobQueue.isEmpty()) {
            Command job = jobQueue.poll();
            if (job != null) {
                job.excute();
            }
        }
    }



}
