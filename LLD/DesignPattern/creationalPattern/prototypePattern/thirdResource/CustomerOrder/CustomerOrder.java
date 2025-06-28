package LLD.DesignPattern.creationalPattern.prototypePattern.thirdResource.CustomerOrder;

import java.util.ArrayList;
import java.util.List;

class Address{
    String houseNo;
    String localty;
    String floorNumber;
    int picode;
    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }
    public void setLocalty(String localty) {
        this.localty = localty;
    }
    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }
    public void setPicode(int picode) {
        this.picode = picode;
    }
    public String getHouseNo() {
        return houseNo;
    }
    public String getLocalty() {
        return localty;
    }
    public String getFloorNumber() {
        return floorNumber;
    }
    public int getPicode() {
        return picode;
    }
    @Override
    public String toString() {
        return String.format("Address [houseNo=%s, localty=%s, floorNumber=%s, picode=%s]", houseNo, localty,
                floorNumber, picode);
    }

    

    

}
public class CustomerOrder implements Cloneable{
    private int items;
    private double totalAmount;
    private  List<String>product;
    private List<Address>shippingAddress;
    public void setItems(int items) {
        this.items = items;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public void setProduct(List<String> product) {
        this.product = product;
    }
    public void setShippingAddress(List<Address> shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    public int getItems() {
        return items;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public List<String> getProduct() {
        return product;
    }
    public List<Address> getShippingAddress() {
        return shippingAddress;
    }

    
    public Object clone(){
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setItems(this.items);
        customerOrder.setTotalAmount(this.totalAmount);

        List<String>newproduct = new ArrayList<>();
        for(String prod : product){
            newproduct.add(prod);
        }
        customerOrder.setProduct(newproduct);

        List<Address>newaAddresses = new ArrayList<>();
        for(Address add : shippingAddress){
            newaAddresses.add(add);
        }
        customerOrder.setShippingAddress(newaAddresses);
        return customerOrder;
    }
    @Override
    public String toString() {
        return String.format("CustomerOrder [items=%s, totalAmount=%s, product=%s, shippingAddress=%s]", items, totalAmount, product, shippingAddress);
    }

    

}
