public class ChessGameView {
    public void showBoard(Chessboard board) {
        board.printBoard();
    }
    public void showMove(Move move) {
        System.out.println(move);
    }
}