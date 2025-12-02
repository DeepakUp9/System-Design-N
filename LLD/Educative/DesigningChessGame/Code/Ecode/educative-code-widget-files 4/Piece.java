public abstract class Piece {
    private boolean white;
    private boolean killed = false;

    public Piece(boolean white) { this.white = white; }
    public boolean isWhite() { return white; }
    public boolean isKilled() { return killed; }
    public void setKilled(boolean killed) { this.killed = killed; }
    public abstract boolean canMove(Chessboard board, Box start, Box end);
    public abstract String getSymbol();
}