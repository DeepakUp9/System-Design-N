package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Mac;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.CheckBoxes;

class MacCheckBoxes implements CheckBoxes {
    
    @Override
    public void render() {
      System.out.println("render MacCheckBoxes");
    }

    @Override
    public void toggle() {
        System.out.println("toggle MacCheckBoxes");
    }
}