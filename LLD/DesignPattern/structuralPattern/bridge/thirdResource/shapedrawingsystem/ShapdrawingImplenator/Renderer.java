package LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.ShapdrawingImplenator;

public interface Renderer {
    public void renderCircle(float radius);
    public void renderRectangle(float width, float height);
}