package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Window;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Buttons;

class WindowButton implements Buttons {

    @Override
    public void render() {
       System.out.println("render WindowButton");
    }

    @Override
    public void onClick() {
        System.out.println("onClick WindowButton");
    }
    
}