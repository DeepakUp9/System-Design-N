package LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.Shapdrawing;

import LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.ShapdrawingImplenator.Renderer;

public class Circle extends Shape {
    private final float radius;

    public Circle(Renderer renderer, float radius) {
        super(renderer);
        this.radius = radius;
    }

    @Override
    public void draw() {
        renderer.renderCircle(radius);
    }
}