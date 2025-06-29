package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring.Display;

public class Statistics implements Observer {

    @Override
    public void update(float temperature, float humidity, float pressure) {
        System.out.println("ForecastDisplay: Pressure=" + pressure + ", trend is stable.");
    }
    
}
