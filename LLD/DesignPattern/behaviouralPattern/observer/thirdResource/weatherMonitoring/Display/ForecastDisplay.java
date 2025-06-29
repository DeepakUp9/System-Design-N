package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring.Display;

public class ForecastDisplay implements Observer {
    @Override
    public void update(float temp, float humidity, float pressure) {
        System.out.println("ForecastDisplay: Pressure=" + pressure + ", trend is stable.");
    }
}