package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Linux;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.DropDown;

class LinuxDropDowns implements DropDown {

    @Override
    public void render() {
        System.out.println("render LinuxDropDowns");
    }

    @Override
    public void expand() {
        System.out.println("expand LinuxDropDowns");
    }
   
}