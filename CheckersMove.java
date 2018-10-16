package checkers;

/**
 * This class takes care of the moves, by keeping track of rows and columns
 * involved from and to in the move
 * Also, checks whether there is a jump involved
 */

public class CheckersMove {
	
    int r1;
    int c1;  // the position whether the piece is originally a
    int r2; 
    int c2;      // the new position where the piece moves 
    
    CheckersMove(int row1, int col1, int row2, int col2) {
            // Constructor.  Just set the values of the instance variables.
       r1 = row1;
       c1 = col1;
       r2 = row2;
       c2 = col2;
    }
    
    // The isJump() method checks whether there is a jump involved
    // Normally the piece moves one row, and when it is a jump 2 rows are involved
    boolean isJump() {
       return (r1 - r2 == 2 || r1 - r2 == -2);
    }
 } 
