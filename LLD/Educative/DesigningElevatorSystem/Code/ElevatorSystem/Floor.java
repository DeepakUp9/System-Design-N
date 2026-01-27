import java.util.ArrayList;
import java.util.List;

public class Floor {
    private final int floorNumber;
    private final List<HallPanel> panels;
    private final List<Display> displays;

    public Floor(int floorNumber, int numPanels, int numDisplays, int topFloor) {
        this.floorNumber = floorNumber;
        this.panels = new ArrayList<>();
        for (int i = 0; i < numPanels; i++) {
            panels.add(new HallPanel(floorNumber, topFloor));
        }
        this.displays = new ArrayList<>();
        for (int i = 0; i < numDisplays; i++) {
            displays.add(new Display());
        }
    }

    public int getFloorNumber() { return floorNumber; }
    public List<HallPanel> getPanels() { return panels; }
    public HallPanel getPanel(int index) { return panels.get(index); }
    public List<Display> getDisplays() { return displays; }
    public Display getDisplay(int index) { return displays.get(index); }
}
