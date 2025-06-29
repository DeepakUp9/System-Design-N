package LLD.DesignPattern.structuralPattern.proxy.thirdResource.VirtualProxy.ImageLoader;
public class ProxyImage implements Image {
    private RealImage realImage;
    private final String fileName;

    public ProxyImage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName); // lazy loading
        }
        realImage.display();
    }
}