package LLD.DesignPattern.structuralPattern.proxy.thirdResource.VirtualProxy.ImageLoader;

public class RealImage implements Image {
    private final String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("Loading image from disk: " + fileName);
        // Simulate expensive loading logic
        try {
            Thread.sleep(2000); // simulate delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void display() {
        System.out.println("Displaying image: " + fileName);
    }
}
