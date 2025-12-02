import java.util.*;

public class ChessGame {
    private Player[] players;
    private Chessboard board;
    private Player currentTurn;
    private GameStatus status;
    private List<Move> moveHistory;
    private ChessMoveController controller;
    private ChessGameView view;
    private final String gameId;

    public ChessGame(Player white, Player black) {
        this.gameId = UUID.randomUUID().toString();
        this.players = new Player[] { white, black };
        this.board = new Chessboard();
        this.currentTurn = white;
        this.status = GameStatus.ACTIVE;
        this.moveHistory = new ArrayList<>();
        this.controller = new ChessMoveController();
        this.view = new ChessGameView();
        board.resetBoard();
    }
    
    public String getGameId() { return gameId; }
    public Chessboard getBoard() { return board; }
    public ChessGameView getView() { return view; }
    public List<Move> getMoveHistory() { return moveHistory; }
    public ChessMoveController getController() { return controller; }
    public void setStatus(GameStatus status) { this.status = status; }
    public Player getCurrentTurn() { return currentTurn; }
    public void setCurrentTurn(Player player) { this.currentTurn = player; }

    public void play() {
        Scanner sc = new Scanner(System.in);
        view.showBoard(board);
        while (status == GameStatus.ACTIVE) {
            System.out.println(currentTurn + " to move (format: e2 e4, or 'resign'):");
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("resign")) {
                status = currentTurn.isWhiteSide() ? GameStatus.BLACK_WIN : GameStatus.WHITE_WIN;
                System.out.println(currentTurn + " resigns! " + (status == GameStatus.WHITE_WIN ? "White" : "Black") + " wins.");
                break;
            }
            if (!input.matches("[a-h][1-8] [a-h][1-8]")) {
                System.out.println("Invalid input format!");
                continue;
            }
            int sx = input.charAt(1) - '1', sy = input.charAt(0) - 'a';
            int ex = input.charAt(4) - '1', ey = input.charAt(3) - 'a';
            Box start = board.getBox(sx, sy), end = board.getBox(ex, ey);
            Piece piece = start.getPiece();
            if (piece == null) {
                System.out.println("No piece at source!");
                continue;
            }
            if (piece.isWhite() != currentTurn.isWhiteSide()) {
                System.out.println("Not your piece!");
                continue;
            }
            if (!controller.validateMove(piece, board, start, end)) {
                System.out.println("Invalid move for " + piece.getSymbol());
                continue;
            }
            Piece captured = end.getPiece();
            end.setPiece(piece);
            start.setPiece(null);
            Move move = new Move(start, end, piece, captured, currentTurn, MoveType.NORMAL);
            moveHistory.add(move);
            view.showMove(move);
            view.showBoard(board);

            // TODO: Add checkmate, stalemate, and draw logic
            // For demo: end after 50 moves or resignation
            if (moveHistory.size() > 50) { status = GameStatus.DRAW; System.out.println("Draw by 50 moves."); break; }
            // Switch turn
            currentTurn = (currentTurn == players[0]) ? players[1] : players[0];
        }
    }
}