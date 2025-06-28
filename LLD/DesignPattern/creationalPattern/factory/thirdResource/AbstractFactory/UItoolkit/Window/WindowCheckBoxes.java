package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Window;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.CheckBoxes;

class WindowCheckBoxes implements CheckBoxes {
  
    @Override
    public void render() {
        System.out.println("render WindowCheckBoxes");
    }

    @Override
    public void toggle() {
        System.out.println("toggle WindowCheckBoxes");
    }
}

