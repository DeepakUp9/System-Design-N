package LLD.DesignPattern.creationalPattern.prototypePattern.thirdResource.CustomerOrder;

import java.util.List;

public class Solution {
    public static void main(String[] args) {
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setItems(10);
        customerOrder.setTotalAmount(200);
        customerOrder.setProduct(List.of("A","B","C","D"));
       
        Address address = new Address();
        address.setHouseNo("1298");
        address.setFloorNumber("3");
        address.setLocalty("gurgaon");
        address.setPicode(212121);
        customerOrder.setShippingAddress(List.of(address));

        System.out.println(customerOrder);

        System.out.println();
        System.out.println();

        CustomerOrder customerOrder2 = (CustomerOrder) customerOrder.clone();
        customerOrder2.setTotalAmount(500);
        List<Address> shippingAddress = customerOrder2.getShippingAddress();
        shippingAddress.get(0).setLocalty("Delhi");
        System.out.println(customerOrder2);


    }
}
