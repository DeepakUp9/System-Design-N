package LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.ShapdrawingImplenator;

public class VectorRenderer implements Renderer {
    public void renderCircle(float radius) {
        System.out.println("Drawing CIRCLE in Vector with radius: " + radius);
    }

    public void renderRectangle(float width, float height) {
        System.out.println("Drawing RECTANGLE in Vector of size: " + width + " x " + height);
    }
}