public class Knight extends Piece {
    public Knight(boolean white) { super(white); }
    public boolean canMove(Chessboard board, Box start, Box end) {
        int dx = Math.abs(start.getX() - end.getX());
        int dy = Math.abs(start.getY() - end.getY());
        if (((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) &&
            (end.getPiece() == null || end.getPiece().isWhite() != this.isWhite()))
            return true;
        return false;
    }
    public String getSymbol() { return isWhite() ? "♘" : "♞"; }
}