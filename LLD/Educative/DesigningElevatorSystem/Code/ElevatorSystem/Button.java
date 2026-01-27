public abstract class Button {
    protected boolean pressed = false;
    public void pressDown() { pressed = true; }
    public void reset() { pressed = false; }
    public abstract boolean isPressed();
}
