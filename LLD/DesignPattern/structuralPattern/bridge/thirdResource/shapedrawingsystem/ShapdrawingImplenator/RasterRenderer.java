package LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.ShapdrawingImplenator;

public class RasterRenderer implements Renderer {
    public void renderCircle(float radius) {
        System.out.println("Drawing CIRCLE in Raster with radius: " + radius);
    }

    public void renderRectangle(float width, float height) {
        System.out.println("Drawing RECTANGLE in Raster of size: " + width + " x " + height);
    }
}
