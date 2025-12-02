public class Pawn extends Piece {
    public Pawn(boolean white) { super(white); }
    public boolean canMove(Chessboard board, Box start, Box end) {
        int dir = isWhite() ? 1 : -1;
        int startRow = isWhite() ? 1 : 6;
        int dx = end.getX() - start.getX();
        int dy = Math.abs(end.getY() - start.getY());
        // Move forward
        if (dy == 0 && start.getPiece() != null && end.getPiece() == null) {
            if (start.getX() + dir == end.getX()) return true;
            if (start.getX() == startRow && start.getX() + 2*dir == end.getX()
                && board.getBox(start.getX() + dir, start.getY()).getPiece() == null)
                return true;
        }
        // Capture diagonally
        if (dx == dir && dy == 1 && end.getPiece() != null && end.getPiece().isWhite() != this.isWhite()) {
            return true;
        }
        return false;
    }
    public String getSymbol() { return isWhite() ? "♙" : "♟"; }
}