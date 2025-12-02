public class Move {
    private Box start;
    private Box end;
    private Piece pieceMoved;
    private Piece pieceKilled;
    private Player player;
    private MoveType moveType;

    public Move(Box start, Box end, Piece pieceMoved, Piece pieceKilled, Player player, MoveType moveType) {
        this.start = start;
        this.end = end;
        this.pieceMoved = pieceMoved;
        this.pieceKilled = pieceKilled;
        this.player = player;
        this.moveType = moveType;
    }

    public Box getStart() { return start; }
    public Box getEnd() { return end; }
    public Piece getPieceMoved() { return pieceMoved; }
    public Piece getPieceKilled() { return pieceKilled; }
    public Player getPlayer() { return player; }
    public MoveType getMoveType() { return moveType; }

    @Override
    public String toString() {
        char col1 = (char)('a' + start.getY());
        char col2 = (char)('a' + end.getY());
        return player + ": " + pieceMoved.getSymbol() + " " +
            col1 + (start.getX()+1) + " to " + col2 + (end.getX()+1) +
            (pieceKilled != null ? " x " + pieceKilled.getSymbol() : "");
    }
}