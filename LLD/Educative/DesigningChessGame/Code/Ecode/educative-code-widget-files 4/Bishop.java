public class Bishop extends Piece {
    public Bishop(boolean white) { super(white); }
    public boolean canMove(Chessboard board, Box start, Box end) {
        int dx = Math.abs(start.getX() - end.getX());
        int dy = Math.abs(start.getY() - end.getY());
        if (dx == dy &&
            (end.getPiece() == null || end.getPiece().isWhite() != this.isWhite())) {
            return !Chessboard.isPathBlocked(board, start, end);
        }
        return false;
    }
    public String getSymbol() { return isWhite() ? "♗" : "♝"; }
}