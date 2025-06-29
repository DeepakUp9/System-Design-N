package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring;

import LLD.DesignPattern.behaviouralPattern.observer.thirdResource.weatherMonitoring.Display.Observer;

public interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}