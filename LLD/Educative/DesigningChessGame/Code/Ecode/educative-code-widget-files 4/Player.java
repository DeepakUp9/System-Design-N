public class Player {
    private String name;
    private boolean whiteSide;

    public Player(String name, boolean whiteSide) {
        this.name = name;
        this.whiteSide = whiteSide;
    }

    public String getName() { return name; }
    public boolean isWhiteSide() { return whiteSide; }
    @Override
    public String toString() { return name + (whiteSide ? " (White)" : " (Black)"); }
}