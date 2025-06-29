package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring.Display;

public interface Observer {
    void update(float temperature, float humidity, float pressure);
}
