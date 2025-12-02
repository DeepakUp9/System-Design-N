# Getting Ready: The Chess Game

Understand the chess game problem and learn the questions to further simplify this problem.

## Problem definition

Chess is a strategy board game for two players on a checkered board of 64 squares in an 8x8 grid. Each player starts with 16 pieces: one king, one queen, two rooks, two knights, two bishops, and eight pawns. The goal is to checkmate the opponent's king—this occurs when the king is in a position to be captured (check) and there is no way to move the king out of capture (checkmate).

Each piece has its unique movements. The rook can move horizontally or vertically, the knight can move in an L-shape position, the bishop can move diagonally, the queen can move in any direction, and the king can move one square in any direction. Pawns have the most complex movement rules: they can move forward one square, capture diagonally, and are subject to special rules such as a two-square advance on their first move, en passant, and promotion. Most pieces, except for the knight, cannot move through or over others. The game also includes special rules, such as castling for the king and the rook.

A chess game can end in a win, a loss, or a draw. Draws can result from stalemate, threefold repetition, the 50-move rule, insufficient material, mutual agreement, or other scenarios like resignation and forfeiture.

In this LLD interview case study, you'll focus on:

* Digitally modeling the rules, state, and movements of chess pieces on a virtual board, including special rules such as castling, en passant, and pawn promotion.
* Implementing turn-based gameplay, including legal moves, checks, checkmates, and various types of draws.
* Handling edge cases such as stalemate, threefold repetition, insufficient material, resignation, and forfeiture.
* The system will keep a complete record of all moves made during the game.
* Players cannot retract or undo moves once played.

> Note: Numerous variations of the game of chess are played globally. In this design problem, we’ll create a digital version of the standard two-player chess game, faithfully implementing the official rules and recognized situations.

<span style="background-color: yellow; color: blue;">in depth(Getting Ready: The Chess Game), <a href="./deapth/problem-Chess-Game.md">click here</a></span>


# Expectations from the interviewee

There are several components in a chess game, each with specific constraints and requirements. The following provides an overview of some of the main expectations that the interviewer will want to hear you discuss in more detail during the interview.

## Chess pieces

The chess pieces have their own specifications according to the game's rules that need to be clarified by the interviewer. Therefore, you may ask the following questions:

1. How many chess pieces are there in the game?
2. What are the different chess pieces, and what are their respective moves?
3. Which piece is the weakest in chess?
4. Which piece is the strongest in chess?

## Gameplay

In chess, the two opposing teams try to achieve the state of checkmate to win the game, or they face some special cases, like a stalemate, that need to be clarified by the interviewer. You may ask the following questions regarding them:

1. Which player takes the first turn?
2. What are the rules of the game?
3. What is a checkmate?
4. How does a stalemate happen?
5. Can a player forfeit/resign from the game?

<span style="background-color: yellow; color: blue;">in depth(Expectations From the Interviewee), <a href="./deapth/Expectations-Interviewee.md">click here</a></span>

# Design approach

We will design this chess game system using a bottom-up approach. For this purpose, we will follow these steps:

* First, we'll define the core entities: `ChessPiece`, `BoardSquare`, and `Player`.
* Next, we'll model composite components like `ChessBoard`, `Move`, and `Game`, capturing piece movement, board state, and game progression.
* We'll ensure the design supports the enforcement of chess rules, detection of check, checkmate, and draw conditions, and handles move validation and player interactions.
* The system will be scalable for future extensions such as move history, undo/redo, and online multiplayer features.

This design approach will address edge cases and adhere to SOLID principles, making the system easy to extend, maintain, and test. Diagrams and code will illustrate major workflows and class structures.

## Design pattern

During an interview, discussing the design patterns that the chess game falls under is always a good practice. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.


<span style="background-color: yellow; color: blue;">in depth(Design approach, Design pattern), <a href="./deapth/DesignApproch_Pattern.md">click here</a></span>
