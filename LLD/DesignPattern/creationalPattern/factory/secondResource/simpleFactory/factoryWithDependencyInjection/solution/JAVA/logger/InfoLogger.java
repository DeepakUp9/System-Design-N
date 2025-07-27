package LLD.DesignPattern.creationalPattern.factory.secondResource.simpleFactory.factoryWithDependencyInjection.solution.JAVA.logger;

public class InfoLogger implements ILogger {
    public void log(String msg) {
        System.out.println("INFO: " + msg);
    }
}
