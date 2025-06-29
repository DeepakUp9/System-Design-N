package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring;

import LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring.Display.CurrentConditionsDisplay;
import LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring.Display.ForecastDisplay;
import LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring.Display.Observer;
import LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring.Display.Statistics;

public class Solution {
    public static void main(String[] args) {
    
         WeatherData weatherData = new WeatherData();

        Observer current = new CurrentConditionsDisplay();
        Observer forecast = new ForecastDisplay();
        Observer sta = new Statistics();

        weatherData.registerObserver(current);
        weatherData.registerObserver(forecast);
        weatherData.registerObserver(sta);

        weatherData.setMeasurements(30.4f, 60f, 1012f);
        System.out.println();
        System.out.println();

        weatherData.removeObserver(current);

        weatherData.setMeasurements(28.2f, 55f, 1010f);

    }
}
