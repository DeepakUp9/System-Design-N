package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Linux;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Buttons;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.CheckBoxes;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.DropDown;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.GuiFactory;

public class LinuxFactory implements GuiFactory {

    @Override
    public CheckBoxes createCheckbox() {
       return new LinuxCheckBoxes();
    }

    @Override
    public DropDown createDropdown() {
       return new LinuxDropDowns();
    }

    @Override
    public Buttons createButton() {
       return new LinuxButton();
    }

}
