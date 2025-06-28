package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Linux;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Buttons;

public class LinuxButton implements Buttons {
    @Override
    public void render() {
        System.out.println("render LinuxButton");
    }

    @Override
    public void onClick() {
        System.out.println("onClick LinuxButton");
    }
}