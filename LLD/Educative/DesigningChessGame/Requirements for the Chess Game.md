# Requirements for the Chess Game

Learn about all the requirements of the online chess game.

This lesson outlines the functional and operational requirements for an online chess game. Identifying and understanding requirements is essential to defining the system's scope and ensuring a robust, user-friendly design.

We'll use the notational convention to identify each requirement with a unique label "Rn," where "R" is short for requirement and "n" is a natural number.

## Requirement collection

* **R1:** The system must enable two players (human or machine) to play chess online, supporting real-time, turn-based multiplayer gameplay.
* **R2:** The chess game must strictly enforce the official rules of international (standard) chess.
* **R3:** At the beginning of each game, players will be randomly assigned white or black.
* **R4:** Each player starts with 16 pieces: eight pawns, two rooks, two knights, two bishops, one queen, and one king, placed in the official starting positions on the 8x8 chessboard.
* **R5:** The player with the white pieces always makes the first move.
* **R6:** Players may not retract or undo their moves once made. (No "take-back" or undo functionality.)
* **R8:** The game must support all official ways a chess game can end, including:
   * Checkmate (a player's king is under attack and cannot escape)
   * Stalemate (no legal moves, and the king is not in check)
   * Draw (mutual agreement, threefold repetition, 50-move rule, or insufficient material)
   * Resignation (a player concedes defeat)
   * Forfeiture (a player does not show up or abandons the game)
* **R9:** According to international chess rules, the system must support special moves such as castling, en passant, and pawn promotion

## International chess rules

### Rules for pieces

The following table represents the basic rules of chess:

| Piece | Movement Rules |
|-------|----------------|
| King | Moves one square in any direction (horizontally, vertically, or diagonally).<br>May perform castling once per game with a rook, if neither piece has moved, no pieces are between them, and the king does not move through, into, or out of check. |
| Queen | Moves any number of squares vertically, horizontally, or diagonally.<br>Cannot jump over other pieces. |
| Rook | Moves any number of squares vertically or horizontally.<br>Cannot jump over other pieces.<br>Participates in castling with the king. |
| Bishop | Moves any number of squares diagonally.<br>Cannot jump over other pieces. |
| Knight | Moves in an "L-shape": two squares in one direction (horizontal or vertical), then one square perpendicular.<br>Can jump over other pieces. |
| Pawn | Moves forward one square.<br>On its first move, it may move forward two squares.<br>Captures one square diagonally forward.<br>Can perform en passant capture.<br>Promotes to queen, rook, bishop, or knight upon reaching the last rank. |

### Rules for situations

The following table represents certain situations we might face while playing chess:

| Situation | Description / Rule |
|-----------|-------------------|
| Check | The king is under threat of capture.<br>The player must make a move to remove the check. |
| Checkmate | The king is in check and has no legal moves to escape.<br>The game ends, and the opponent wins. |
| Stalemate | The player has no legal moves, and their king is not in check.<br>The game ends in a draw. |
| Draw | Can occur due to stalemate, threefold repetition, the 50-move rule (50 moves by each player with no pawn move or capture), insufficient material, or mutual agreement. |
| Forfeiture | They forfeit if a player does not show up for the game or abandons it. |
| Resignation | A player may resign anytime, conceding the game to the opponent. |
| Castling | Special move involving the king and a rook, performed on either kingside or queenside. Allowed only if:<br>- Neither the king nor the chosen rook has moved.<br>- There are no pieces between the king and the rook.<br>- The king is not in check, does not move through a square under attack, and does not end up in check.<br>- The king has not previously been in check.<br>The king moves two squares toward the rook, and the rook moves to the square the king passed over. |
| En Passant | A pawn that advances two squares from its starting position may be captured "en passant" by an opposing pawn as if it had moved only one square.<br>This must be done immediately on the next move. |
| Pawn Promotion | When a pawn reaches the farthest rank from its starting position, it is promoted to a queen, rook, bishop, or knight, as chosen by the player. |




<span style="background-color: yellow; color: blue;">in depth(Requirements for the Chess Game), <a href="./deapth/Requirements.md">click here</a></span>
