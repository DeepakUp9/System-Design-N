public class TableConfiguration {
    private int tableConfigID;
    private byte[] tableConfigImage;

    public TableConfiguration(int tableConfigID, byte[] tableConfigImage) {
        this.tableConfigID = tableConfigID;
        this.tableConfigImage = tableConfigImage;
    }

    public int getTableConfigID() {
        return tableConfigID;
    }

    public void setTableConfigID(int tableConfigID) {
        this.tableConfigID = tableConfigID;
    }

    public byte[] getTableConfigImage() {
        return tableConfigImage;
    }

    public void setTableConfigImage(byte[] tableConfigImage) {
        this.tableConfigImage = tableConfigImage;
    }
}
