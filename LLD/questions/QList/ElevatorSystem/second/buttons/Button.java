package questions.QList.ElevatorSystem.second.buttons;

import questions.QList.ElevatorSystem.first.ButtonType;

public abstract class Button {
    protected ButtonType buttonType;
    protected boolean isPressed;

    public Button(ButtonType buttonType){
        this.buttonType = buttonType;
    }

    public abstract void press();

    public boolean ispressed(){ return isPressed;}

    public ButtonType getButtonType(){ return this.buttonType;}
    
}
