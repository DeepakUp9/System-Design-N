public class HallPanel {
    private final HallButton up;
    private final HallButton down;

    public HallPanel(int floorNumber, int topFloor) {
        this.up = (floorNumber == topFloor) ? null : new HallButton(Direction.UP);
        this.down = (floorNumber == 0) ? null : new HallButton(Direction.DOWN);
    }
    public HallButton getUpButton() { return up; }
    public HallButton getDownButton() { return down; }
}
