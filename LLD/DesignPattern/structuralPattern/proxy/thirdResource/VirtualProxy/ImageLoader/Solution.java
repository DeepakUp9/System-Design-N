package LLD.DesignPattern.structuralPattern.proxy.thirdResource.VirtualProxy.ImageLoader;

public class Solution {
    public static void main(String[] args) {
        Image image = new ProxyImage("nature.jpg");

        System.out.println("Image created...");

        // Image will be loaded only when display is called
        System.out.println("Calling display first time:");
        image.display(); // triggers real image load

        System.out.println("Calling display second time:");
        image.display(); // now uses cached RealImage
    }
}
