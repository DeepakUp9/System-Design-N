package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Window;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Buttons;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.CheckBoxes;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.DropDown;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.GuiFactory;

public class WindowFactory implements GuiFactory {

    @Override
    public Buttons createButton() {
       return new WindowButton();
    }

    @Override
    public CheckBoxes createCheckbox() {
        return new WindowCheckBoxes();
    }

    @Override
    public DropDown createDropdown() {
      return new WindowDropDowns();
    }

}
