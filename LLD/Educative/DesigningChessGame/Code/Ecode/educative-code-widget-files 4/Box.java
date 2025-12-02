public class Box {
    private int x, y;
    private Piece piece;

    public Box(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) { this.piece = piece; }
}