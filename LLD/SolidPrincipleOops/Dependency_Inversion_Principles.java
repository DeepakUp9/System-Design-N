package LLD.SolidPrinciple;

//Class should  depend on interfaces rather than concrete classes
class MackBook{
    private final WiredKeyboard keyboard;
    private final WiredMouse mouse;

    public MackBook(){
        keyboard = new WiredKeyboard();
        mouse = new WiredMouse();
    }

}

// let fix this so no it depenet on interface rather than concrete classes
class MackBook{
    private final Keyboard keyboard;
    private final Mouse mouse;

    public MackBook( Keyboard keyboard, Mouse mouse){
        this.keyboard = keyboard;
        this.mouse = mouse;
    }

}
