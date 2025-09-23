package questions.QList.ElevatorSystem.first;

public abstract class Button {
    protected ButtonType buttonType;
    protected boolean isPressed;
    
    public Button(ButtonType type) {
        this.buttonType = type;
        this.isPressed = false;
    }
    
    public abstract void press();
    
    public boolean isPressed() { return isPressed; }
    public ButtonType getButtonType() { return buttonType; }
}
