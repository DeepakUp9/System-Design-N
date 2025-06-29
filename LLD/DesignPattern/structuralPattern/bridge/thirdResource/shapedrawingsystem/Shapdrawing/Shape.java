package LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.Shapdrawing;

import LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.ShapdrawingImplenator.Renderer;

public abstract class Shape {
    protected Renderer renderer;

    public Shape(Renderer renderer) {
        this.renderer = renderer;
    }

    public abstract void draw();
}