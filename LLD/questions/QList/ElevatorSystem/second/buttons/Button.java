package questions.QList.ElevatorSystem.second.buttons;

import questions.QList.ElevatorSystem.first.ButtonType;

public abstract class Button {
    private ButtonType buttonType;
    private boolean ispressed;

    public Button(ButtonType buttonType){
        this.buttonType = buttonType;
    }

    public abstract void press();

    public boolean ispressed(){ return ispressed;}

    public ButtonType getButtonType(){ return this.buttonType;}
    
}
