public abstract class Service {
    private int serviceId;
    private int price;

    public void setServiceId(int id) { serviceId = id; }
    public int getServiceId() { return serviceId; }
    public void setPrice(int p) { price = p; }
    public int getPrice() { return price; }
}
