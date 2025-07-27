package LLD.DesignPattern.creationalPattern.factory.secondResource.simpleFactory.factoryWithDependencyInjection.solution.JAVA.logger;

public class LoggerFactory {
    public static ILogger createLogger(LogLevel logLevel) {
        switch(logLevel) {
            case DEBUG:
                return new DebugLogger();
            case INFO:
                return new InfoLogger();
            case ERROR:
                return new ErrorLogger();
            default:
                return null;
        }
    }
}
