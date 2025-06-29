package LLD.DesignPattern.structuralPattern.facade.thirdResource.hotelbookingsystem;

public class FacadeHotelBooking {
    private RoomInventory roomInventory;
    private PaymentGatway paymentGatway;
    private NotificationService notificationService;
    
    public FacadeHotelBooking(RoomInventory roomInventory, PaymentGatway paymentGatway,NotificationService notificationService) {
        this.roomInventory = roomInventory;
        this.paymentGatway = paymentGatway;
        this.notificationService = notificationService;
    }

    

    public FacadeHotelBooking() {
        roomInventory = new RoomInventory();
        paymentGatway = new PaymentGatway();
        notificationService = new NotificationService();
    }



    public boolean roomBook(){
        roomInventory.roomAvaibility();
        paymentGatway.makePayment();
        notificationService.sendNotification();
        return true;
    }

}
