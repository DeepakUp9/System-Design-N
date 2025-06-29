package LLD.DesignPattern.behaviouralPattern.observer.firstResource;


interface Subject{
    void subscribe(Observer ob);
    void unsubscribe(Observer ob);
    void notifyChanges(String titile);

}