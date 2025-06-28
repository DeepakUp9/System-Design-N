package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Mac;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.DropDown;

class MacDropDowns implements DropDown {
    
    @Override
    public void render() {
      System.out.println("render MacDropDowns");
    }

    @Override
    public void expand() {
       System.out.println("expand MacDropDowns");
    }
}