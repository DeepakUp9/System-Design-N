package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.notifications;

import Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.models.Address;

public class PostalNotification extends Notification {
    private Address address;
    
    public PostalNotification(String notificationId, String content, Address address) {
        super(notificationId, content);
        this.address = address;
    }
    
    @Override
    public boolean sendNotification() {
        System.out.println("Postal notification to " + address + ": " + getContent());
        return true;
    }
}
