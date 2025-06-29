package LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem;

import LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.ShapdrawingImplenator.RasterRenderer;
import LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.ShapdrawingImplenator.VectorRenderer;
import LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.ShapdrawingImplenator.Renderer;
import LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.Shapdrawing.Shape;
import LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.Shapdrawing.Circle;
import LLD.DesignPattern.structuralPattern.bridge.thirdResource.shapedrawingsystem.Shapdrawing.Rectangle;

public class Solution {
    public static void main(String[] args) {
        Renderer raster = new RasterRenderer();
        Renderer vector = new VectorRenderer();

        Shape circle1 = new Circle(raster, 5);
        Shape circle2 = new Circle(vector, 10);

        Shape rect1 = new Rectangle(raster, 4, 3);
        Shape rect2 = new Rectangle(vector, 6, 2);

        circle1.draw();
        circle2.draw();
        rect1.draw();
        rect2.draw();

        
        
    }
}
