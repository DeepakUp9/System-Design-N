package LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit;

import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Linux.LinuxFactory;
import LLD.DesignPattern.creationalPattern.factory.thirdResource.AbstractFactory.UItoolkit.Mac.MacFactory;

public class Solution {


    public static void main(String[] args) {
        //mac
        GuiFactory macFactory = new MacFactory();
        Buttons button = macFactory.createButton();
        CheckBoxes checkBoxes = macFactory.createCheckbox();
        DropDown dropDown = macFactory.createDropdown();

        button.render();
        button.onClick();

        checkBoxes.render();
        checkBoxes.toggle();

        dropDown.render();
        dropDown.expand();


        System.out.println();
        System.out.println();


        //Linux
        GuiFactory lGuiFactory = new LinuxFactory();
        Buttons lButtons = lGuiFactory.createButton();
        CheckBoxes lCheckBoxes = lGuiFactory.createCheckbox();
        DropDown lDropDown = lGuiFactory.createDropdown();

        lButtons.render();
        lButtons.onClick();

        lCheckBoxes.render();
        lCheckBoxes.toggle();

        lDropDown.render();
        lDropDown.expand();


    }
}
