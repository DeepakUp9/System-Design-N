package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Linux;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.CheckBoxes;

class LinuxCheckBoxes implements CheckBoxes {
    
    @Override
    public void render() {
        System.out.println("render LinuxCheckBoxes");
    }

    @Override
    public void toggle() {
        System.out.println("toggle LinuxCheckBoxes");
    }
}