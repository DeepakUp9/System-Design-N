public class ChessMoveController {
    public boolean validateMove(Piece piece, Chessboard board, Box start, Box end) {
        return piece != null && piece.canMove(board, start, end);
    }
}