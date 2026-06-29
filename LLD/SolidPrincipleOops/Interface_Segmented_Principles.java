package LLD.SolidPrinciple;

// Interfaces should be such, that client should not implements unnecessary functions they do not need 

interface RestaurantEmployee{
    void washDishes();
    void serverCustomers();
    void cookFood();
}

class waiter implements RestaurantEmployee {

    public void washDishes(){
        //not my job
    }

    public void serverCustomers(){
        //yes and here us my implememtion
        System.out.println("Serving the customer");
    }

    public void cookFood(){
        // not my job
    }
}

//above class does not not follow Interface Segmented Principles beacuse in one interface we wrote all the method 
// so be need divide our interface into segment 

interface WaiterInterface{
    void serverCustomers();
    void takeOrder();
}

interface ChiefInterface{
    void cookFood();
    void decideMenu();
}

class waiter implements WaiterInterface {

    public void serverCustomers(){
        //yes and here us my implememtion
        System.out.println("Serving the customer");
    }

    public void takeOrder(){
       System.out.println("Taking order of the customer");
    }
}