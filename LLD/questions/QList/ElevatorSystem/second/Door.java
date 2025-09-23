package questions.QList.ElevatorSystem.second;

public class Door {
    private boolean isOpen;

    public Door(){
      this.isOpen = false;
    }
    
    public void opened(){
        isOpen = true;
        System.out.println("Door opened");
    }

    public void closed(){
        isOpen = false;
        System.out.println("Door Closed");
    }

    public boolean isopen() {return this.isOpen;}

}
