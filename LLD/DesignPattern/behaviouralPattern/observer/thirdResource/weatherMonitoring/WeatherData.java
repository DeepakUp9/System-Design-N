package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring;

import java.util.ArrayList;
import java.util.List;

import LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring.Display.Observer;


public class WeatherData implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private float temperature;
    private float humidity;
    private float pressure;

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(temperature, humidity, pressure);
        }
    }

    public void setMeasurements(float temp, float hum, float pres) {
        this.temperature = temp;
        this.humidity = hum;
        this.pressure = pres;
        notifyObservers();
    }

    
}
