public class Queen extends Piece {
    public Queen(boolean white) { super(white); }
    public boolean canMove(Chessboard board, Box start, Box end) {
        int dx = Math.abs(start.getX() - end.getX());
        int dy = Math.abs(start.getY() - end.getY());
        if ((dx == dy || dx == 0 || dy == 0) &&
            (end.getPiece() == null || end.getPiece().isWhite() != this.isWhite())) {
            // check path is not blocked
            return !Chessboard.isPathBlocked(board, start, end);
        }
        return false;
    }
    public String getSymbol() { return isWhite() ? "♕" : "♛"; }
}