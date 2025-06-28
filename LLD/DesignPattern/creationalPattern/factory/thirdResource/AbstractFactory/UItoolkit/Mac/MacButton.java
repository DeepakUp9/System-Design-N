package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Mac;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Buttons;

class MacButton implements Buttons {
    @Override
    public void render() {
       System.out.println("render MacButton");
    }

    @Override
    public void onClick() {
       System.out.println("onlick MacButton");
    }
}