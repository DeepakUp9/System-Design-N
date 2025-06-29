package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring.Display;

public class CurrentConditionsDisplay implements Observer {
    @Override
    public void update(float temp, float humidity, float pressure) {
        System.out.println("CurrentConditionsDisplay: Temp=" + temp + ", Humidity=" + humidity);
    }
}