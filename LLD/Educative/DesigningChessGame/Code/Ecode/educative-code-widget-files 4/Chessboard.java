import java.util.*;

public class Chessboard {
    private Box[][] boxes = new Box[8][8];

    public Chessboard() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                boxes[i][j] = new Box(i, j, null);
    }

    public Box getBox(int x, int y) { return boxes[x][y]; }
    public Box[][] getBoxes() { return boxes; }

    public void resetBoard() {
        // Clear board
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                boxes[i][j].setPiece(null);
        // Place pawns
        for (int j = 0; j < 8; j++) {
            boxes[1][j].setPiece(new Pawn(true));
            boxes[6][j].setPiece(new Pawn(false));
        }
        // Place other pieces
        boxes[0][0].setPiece(new Rook(true));   boxes[0][7].setPiece(new Rook(true));
        boxes[7][0].setPiece(new Rook(false));  boxes[7][7].setPiece(new Rook(false));
        boxes[0][1].setPiece(new Knight(true)); boxes[0][6].setPiece(new Knight(true));
        boxes[7][1].setPiece(new Knight(false));boxes[7][6].setPiece(new Knight(false));
        boxes[0][2].setPiece(new Bishop(true)); boxes[0][5].setPiece(new Bishop(true));
        boxes[7][2].setPiece(new Bishop(false));boxes[7][5].setPiece(new Bishop(false));
        boxes[0][3].setPiece(new Queen(true));
        boxes[7][3].setPiece(new Queen(false));
        boxes[0][4].setPiece(new King(true));
        boxes[7][4].setPiece(new King(false));
    }

    // Helper: checks if path is blocked between start and end for Queen, Rook, Bishop
    public static boolean isPathBlocked(Chessboard board, Box start, Box end) {
        int dx = Integer.compare(end.getX(), start.getX());
        int dy = Integer.compare(end.getY(), start.getY());
        int x = start.getX() + dx, y = start.getY() + dy;
        while (x != end.getX() || y != end.getY()) {
            if (board.getBox(x, y).getPiece() != null)
                return true;
            x += dx; y += dy;
        }
        return false;
    }

    public void printBoard() {
        System.out.println("\n  a b c d e f g h");
        for (int i = 7; i >= 0; i--) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++) {
                Piece p = boxes[i][j].getPiece();
                System.out.print(p != null ? p.getSymbol() : ".");
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}