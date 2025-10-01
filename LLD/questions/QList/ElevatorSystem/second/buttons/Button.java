package questions.QList.ElevatorSystem.second.buttons;

import questions.QList.ElevatorSystem.first.ButtonType;

/**
 * Abstract base class for all elevator buttons
 * Provides common functionality for both internal and external buttons
 */
public abstract class Button {
    protected ButtonType buttonType;
    protected boolean isPressed;

    public Button(ButtonType buttonType) {
        this.buttonType = buttonType;
        this.isPressed = false; // Initialize as not pressed
    }

    /**
     * Abstract method to handle button press action
     * Must be implemented by concrete button classes
     */
    public abstract void press();

    /**
     * Checks if button is currently pressed
     * @return true if button is pressed, false otherwise
     */
    public boolean isPressed() {
        return isPressed;
    }

    /**
     * Gets the type of button
     * @return ButtonType enum value
     */
    public ButtonType getButtonType() {
        return this.buttonType;
    }
}