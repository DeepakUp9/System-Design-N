package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit;

public interface GuiFactory {
    Buttons createButton();
    CheckBoxes createCheckbox();  // Fixed method name casing
    DropDown createDropdown();    // Fixed method name
}
