package LLD.SolidPrinciple;

//If class B is subtype of class A, then we should be able to replae object of A with B with B without breaking the behaviour of the program.
// Subclass should extends the capability of parent class not narrow it down 

interface Bike{
    void turnOnEngine();
    void accelerate();
}

class MotorCycle implements Bike{
    boolean isEngineOn;
    int speed;

    public void turnOnEngine(){
        //turn on the engine
        isEngineOn = true;
    }

    public void accelerate(){
        //increase the speed
        speed = speed + 10;
    }
}

class BiCycle implements Bike{
   
    public void turnOnEngine(){
       throw new AssertionError("there is no engine");
    }

    public void accelerate(){
       //do something 
    }
}