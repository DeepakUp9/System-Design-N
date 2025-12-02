import java.util.*;

public class Driver {
    public static void main(String[] args) {
        Player white = new Player("Alice", true);
        Player black = new Player("Bob", false);
        ChessGame game = new ChessGame(white, black);
        
        // Sample game id
        System.out.println("Game ID: " + game.getGameId());

        game.getBoard().resetBoard();
        game.getView().showBoard(game.getBoard());

        // Scenario 1: White moves pawn e2 to e4
        playMove(game, white, "e2", "e4");

        // Scenario 2: Black moves pawn e7 to e5
        playMove(game, black, "e7", "e5");

        // Scenario 3: White moves Queen d1 to h5
        playMove(game, white, "d1", "h5");

        // Scenario 4: Black moves Knight b8 to c6
        playMove(game, black, "b8", "c6");

        // Scenario 5: White moves Bishop f1 to c4
        playMove(game, white, "f1", "c4");

        // Scenario 6: Black moves Knight g8 to f6
        playMove(game, black, "g8", "f6");

        // Scenario 7: White moves Queen h5 to f7 (Fool's Mate checkmate, if black played g7-g6 earlier)
        playMove(game, white, "h5", "f7");

        // Scenario 8: White tries an illegal move (move pawn e4 to e5 - but e4 is now empty)
        playMove(game, white, "e4", "e5");

        // Scenario 9: Black resigns
        game.setStatus(GameStatus.WHITE_WIN);
        System.out.println("\nBob (Black) resigns! Alice (White) wins!");

        // Print move log
        System.out.println("\nMove log:");
        for (Move m : game.getMoveHistory()) {
            System.out.println("  " + m);
        }
    }

    // Helper to parse and play a move
    static void playMove(ChessGame game, Player player, String from, String to) {
        int sx = from.charAt(1) - '1', sy = from.charAt(0) - 'a';
        int ex = to.charAt(1) - '1', ey = to.charAt(0) - 'a';
        Box start = game.getBoard().getBox(sx, sy);
        Box end = game.getBoard().getBox(ex, ey);
        Piece piece = start.getPiece();
        if (piece == null) {
            System.out.println("\nNo piece at source (" + from + ") for " + player + "!");
            return;
        }
        if (piece.isWhite() != player.isWhiteSide()) {
            System.out.println("\nNot your piece (" + from + ") for " + player + "!");
            return;
        }
        if (!game.getController().validateMove(piece, game.getBoard(), start, end)) {
            System.out.println("\nInvalid move for " + piece.getSymbol() + " from " + from + " to " + to + "!");
            return;
        }
        if (piece instanceof Pawn) {
            int promotionRow = piece.isWhite() ? 7 : 0;
            if (end.getX() == promotionRow) {
                // For demo: auto-promote to Queen
                end.setPiece(new Queen(piece.isWhite()));
                System.out.println(piece.isWhite() ? "White" : "Black" + " pawn promoted to Queen at " +
                                   (char)('a' + end.getY()) + (end.getX() + 1));
            }
        }
        Piece captured = end.getPiece();
        end.setPiece(piece);
        start.setPiece(null);
        Move move = new Move(start, end, piece, captured, player, MoveType.NORMAL);
        game.getMoveHistory().add(move);
        System.out.println("\n" + player + " moves " + piece.getSymbol() + " from " + from + " to " + to +
            (captured != null ? " capturing " + captured.getSymbol() : ""));
        game.getView().showBoard(game.getBoard());
    }
}
