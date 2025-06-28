package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Window;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.DropDown;

class WindowDropDowns implements DropDown {

    @Override
    public void render() {
       System.out.println("render WindowDropDowns");
    }

    @Override
    public void expand() {
        System.out.println("expand WindowDropDowns");
    }
    
}