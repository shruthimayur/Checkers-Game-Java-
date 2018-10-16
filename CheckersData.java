package checkers;

import java.util.ArrayList;

/**
 * This class holds the data associated with the checkers board
 * the various variables specified below represents the data on the board at any given time
 * Also, it has the logic for the various legal moves possible for the current player
 */

public class CheckersData {
    	static final int EMPTY = 0;
    	static final int RED = 1;
    	static final int R_KING = 2;
    	static final int BLACK = 3;
    	static final int B_KING = 4;
    	
    int[][] checkerBoard;  
    
    
    /**
     * This constructor creates the board and sets all the pieces initially
     */
    CheckersData() {
       checkerBoard = new int[8][8];
       setUpGame();
    }
    
    
    /**
     * This method sets up the game initially by placing the relevant pieces in its position
     * the pieces are only placed at position where row % 2 == column % 2
     * The first three rows would be filled with the black pieces
     * and the last three rows with the red pieces
     */
    void setUpGame() {
       for (int row = 0; row < 8; row++) {
          for (int col = 0; col < 8; col++) {
             if ( row % 2 == col % 2 ) {
                if (row < 3)
                   checkerBoard[row][col] = BLACK;
                else if (row > 4)
                   checkerBoard[row][col] = RED;
                else
                   checkerBoard[row][col] = EMPTY;
             }
             else {
                checkerBoard[row][col] = EMPTY;
             }
          }
       }
    }  
    
    
    /**
     * This method returns the data on a particular square
     * and this is specified by the row and the column
     */
    int pieceAt(int row, int col) {
       return checkerBoard[row][col];
    }
          
    
    /**
     * Make the specified move.  It is assumed that move
     * is non-null and that the move it represents is legal.
     */
    void doMakeMove(CheckersMove move) {
       doMove(move.r1, move.c1, move.r2, move.c2);
    }
    
    
    /**
     * If the move is a jump, the
     * jumped piece is removed from the board.  If a piece moves to
     * the last row on the opponent's side of the board, the 
     * piece becomes a king.
     */
    void doMove(int r1, int c1, int r2, int c2) {
       checkerBoard[r2][c2] = checkerBoard[r1][c1];
       checkerBoard[r1][c1] = EMPTY;
       if (r1 - r2 == 2 || r1 - r2 == -2) {
          
          int jumpRow = (r1 + r2) / 2;  
          int jumpCol = (c1 + c2) / 2;  
          checkerBoard[jumpRow][jumpCol] = EMPTY;
       }
       if (r2 == 0 && checkerBoard[r2][c2] == RED)
          checkerBoard[r2][c2] = R_KING;
       if (r2 == 7 && checkerBoard[r2][c2] == BLACK)
          checkerBoard[r2][c2] = B_KING;
    }
    
    /**
     * Return an array containing all the legal CheckersMoves
     * if there are no moves possible, null is returned
     */
    CheckersMove[] getValidMoves(int player) {
       
       if (player != RED && player != BLACK)
          return null;
       
       int playerKing;  
       if (player == RED)
          playerKing = R_KING;
       else
          playerKing = B_KING;
       
       ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();  // all possible moves will be stored in this array list.
       
       for (int row = 0; row < 8; row++) {
          for (int col = 0; col < 8; col++) {
             if (checkerBoard[row][col] == player || checkerBoard[row][col] == playerKing) {
                if (jumpPossible(player, row, col, row+1, col+1, row+2, col+2))
                   moves.add(new CheckersMove(row, col, row+2, col+2));
                if (jumpPossible(player, row, col, row-1, col+1, row-2, col+2))
                   moves.add(new CheckersMove(row, col, row-2, col+2));
                if (jumpPossible(player, row, col, row+1, col-1, row+2, col-2))
                   moves.add(new CheckersMove(row, col, row+2, col-2));
                if (jumpPossible(player, row, col, row-1, col-1, row-2, col-2))
                   moves.add(new CheckersMove(row, col, row-2, col-2));
             }
          }
       }
       
       
       if (moves.size() == 0) {
          for (int row = 0; row < 8; row++) {
             for (int col = 0; col < 8; col++) {
                if (checkerBoard[row][col] == player || checkerBoard[row][col] == playerKing) {
                   if (movePossible(player,row,col,row+1,col+1))
                      moves.add(new CheckersMove(row,col,row+1,col+1));
                   if (movePossible(player,row,col,row-1,col+1))
                      moves.add(new CheckersMove(row,col,row-1,col+1));
                   if (movePossible(player,row,col,row+1,col-1))
                      moves.add(new CheckersMove(row,col,row+1,col-1));
                   if (movePossible(player,row,col,row-1,col-1))
                      moves.add(new CheckersMove(row,col,row-1,col-1));
                }
             }
          }
       }
       
       if (moves.size() == 0)
          return null;
       else {
          CheckersMove[] moveArray = new CheckersMove[moves.size()];
          for (int i = 0; i < moves.size(); i++)
             moveArray[i] = moves.get(i);
          return moveArray;
       }
    } 
    
    
    /**
     * Return a list of the valid jumps that the specified player can
     * make starting from the specified row and column.  If no such
     * jumps are possible, null is returned.  The logic is similar
     * to the logic of the getValidMoves() method.
     */
    CheckersMove[] getValidJumpsFrom(int player, int row, int col) {
       if (player != RED && player != BLACK)
          return null;
       int playerKing; 
       if (player == RED)
          playerKing = R_KING;
       else
          playerKing = B_KING;
       ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();  // The legal jumps will be stored in this list.
       if (checkerBoard[row][col] == player || checkerBoard[row][col] == playerKing) {
          if (jumpPossible(player, row, col, row+1, col+1, row+2, col+2))
             moves.add(new CheckersMove(row, col, row+2, col+2));
          if (jumpPossible(player, row, col, row-1, col+1, row-2, col+2))
             moves.add(new CheckersMove(row, col, row-2, col+2));
          if (jumpPossible(player, row, col, row+1, col-1, row+2, col-2))
             moves.add(new CheckersMove(row, col, row+2, col-2));
          if (jumpPossible(player, row, col, row-1, col-1, row-2, col-2))
             moves.add(new CheckersMove(row, col, row-2, col-2));
       }
       if (moves.size() == 0)
          return null;
       else {
          CheckersMove[] moveArray = new CheckersMove[moves.size()];
          for (int i = 0; i < moves.size(); i++)
             moveArray[i] = moves.get(i);
          return moveArray;
       }
    }  
    
    public boolean jumpPossible(int player, int r1, int c1, int r2, int c2, int r3, int c3) {
       
       if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
          return false;  
       
       if (checkerBoard[r3][c3] != EMPTY)
          return false;  
       
       if (player == RED) {
          if (checkerBoard[r1][c1] == RED && r3 > r1)
             return false;  
          if (checkerBoard[r2][c2] != BLACK && checkerBoard[r2][c2] != B_KING)
             return false; 
          return true;  
       }
       else {
          if (checkerBoard[r1][c1] == BLACK && r3 < r1)
             return false;  
          if (checkerBoard[r2][c2] != RED && checkerBoard[r2][c2] != R_KING)
             return false;  
          return true;  
       }
       
    } 
    
    public boolean movePossible(int player, int r1, int c1, int r2, int c2) {
       
       if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
          return false;  
       
       if (checkerBoard[r2][c2] != EMPTY)
          return false;  
       
       if (player == RED) {
          if (checkerBoard[r1][c1] == RED && r2 > r1)
             return false;  
          return true;  
       }
       else {
          if (checkerBoard[r1][c1] == BLACK && r2 < r1)
             return false;  
          return true;  
       }
       
    } 
 } 
