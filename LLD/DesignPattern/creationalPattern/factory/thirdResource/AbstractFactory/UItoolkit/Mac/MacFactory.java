package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Mac;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Buttons;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.CheckBoxes;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.DropDown;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.GuiFactory;

public class MacFactory implements GuiFactory {

    @Override
    public Buttons createButton() {
       return new MacButton();
    }

    @Override
    public CheckBoxes createCheckbox() {
      return new MacCheckBoxes();
    }

    @Override
    public DropDown createDropdown() {
       return new MacDropDowns();
    }

   

}
