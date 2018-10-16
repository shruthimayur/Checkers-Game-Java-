package checkers;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Checkers extends JPanel {
   
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		
      JFrame window = new JFrame("Checkers Game");
      
      Checkers content = new Checkers();
      
      window.setContentPane(content);
      
      window.pack();
      Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
      
      window.setLocation( (screensize.width - window.getWidth())/2,
            (screensize.height - window.getHeight())/2 );
      
      window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
       
      window.setVisible(true);
   }
      
   private JButton hintButton;  // Button for guiding what has to be done next
   
   private JLabel helpMessage;  // A Label that serves as an instruction to the user.
   
   public Checkers() {
      
      setLayout(null); // defining the layout on our own
      setPreferredSize( new Dimension(350,250) );
      
      setBackground(new Color(120,150,150));  
      
      /*We go on creating swing components and add them to the JFrame created */
      
      Board board = new Board();  
      add(board);
      add(hintButton);
      add(helpMessage);
      
      /* The setBounds() method is called to set the size and position of the various components. */
      
      board.setBounds(20,20,164,164); 
      hintButton.setBounds(210, 60, 120, 30);
      helpMessage.setBounds(0, 200, 350, 30);
      helpMessage.setVisible(false);
      
   } 

   /*
    * This class is meant for creating the checker board by defining the various
    * GUI components such as the border for the board, the checkers, etc.
    * Also, this class captures all the user actions through the ActionListener
    * and MouseListener interfaces
    */
   public class Board extends JPanel implements ActionListener, MouseListener {
      
      
      /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CheckersData board;  // Contains the data for the board, 
      					   // also has the legal moves
      
      boolean gameInProgress; //Keeps track whether the game is on or not
      
      /* The following three data fields are valid only when the game is on. */
      
      int currentPlayer;      //this keeps of the player whose turn it is to play
      
      int rowChosen;
      int colChosen;  // when the current player clicks on a piece, 
      								// these variables record the row and column of that piece
      
      CheckersMove[] validMoves;  // An array containing the legal moves for the
                                  //   current player.
      

      /**
       * Constructor.  Create the buttons and label.  Listens for mouse
        * clicks and for clicks on the buttons.  Create the board and
        * start the first game.
       */
      Board() {
         setBackground(Color.BLACK);
         addMouseListener(this);
         hintButton = new JButton("Hint");
         hintButton.addActionListener(this);
         helpMessage = new JLabel("",JLabel.CENTER);
         helpMessage.setFont(new  Font("Arial", Font.BOLD, 12));
         helpMessage.setForeground(Color.black);
         board = new CheckersData();
         doNewGame();
      }
      
      @Override
	  	public void actionPerformed(ActionEvent e) {
	  		Object src = e.getSource();
	          if (src == hintButton)
	             helpMessage.setVisible(true);
	  	}
      
      void doNewGame() {
         board.setUpGame();   // Set up the pieces.
         currentPlayer = CheckersData.RED;   // RED starts the game first.
         validMoves = board.getValidMoves(CheckersData.RED);  // Get RED's legal moves.
         rowChosen = -1;   // RED has not yet selected a piece to move.
         helpMessage.setText("Red:  Make your move.");
         gameInProgress = true;
         hintButton.setEnabled(true);
         
         repaint();
      }
      
      /**
       * This is called by mousePressed() when a player clicks on the
       * square in the specified row and col.  It has already been checked
       * that a game is, in fact, in progress.
       */
      void selectSquare(int row, int col) {
         
         for (int i = 0; i < validMoves.length; i++)
            if (validMoves[i].r1 == row && validMoves[i].c1 == col) {
               rowChosen = row;
               colChosen = col;
               if (currentPlayer == CheckersData.RED)
                  helpMessage.setText("RED:  Make your move.");
               else
                  helpMessage.setText("BLACK:  Make your move.");
               repaint();
               return;
            }
         
         if (rowChosen < 0) {
            helpMessage.setText("Click the piece you want to move.");
            return;
         }
         
         for (int i = 0; i < validMoves.length; i++)
            if (validMoves[i].r1 == rowChosen && validMoves[i].c1 == colChosen
                  && validMoves[i].r2 == row && validMoves[i].c2 == col) {
               doMakeMove(validMoves[i]);
               return;
            }
         
         helpMessage.setText("Click the square you want to move to.");
         
      } 
      
      
      /**
       * This is called when the current player has chosen the specified
       * move.  Make the move, and then either end or continue the game
       * appropriately.
       */
      void doMakeMove(CheckersMove move) {
         
         board.doMakeMove(move);
         
         
         if (move.isJump()) {
            validMoves = board.getValidJumpsFrom(currentPlayer,move.r2,move.c2);
            if (validMoves != null) {
               if (currentPlayer == CheckersData.RED)
                  helpMessage.setText("RED:  You must continue jumping.");
               else
                  helpMessage.setText("BLACK:  You must continue jumping.");
               rowChosen = move.r2;  
               colChosen = move.c2;
               repaint();
               return;
            }
         }
         
         if (currentPlayer == CheckersData.RED) {
            currentPlayer = CheckersData.BLACK;
            validMoves = board.getValidMoves(currentPlayer);
            if (validMoves == null)
               helpMessage.setText("BLACK has no moves.  RED wins.");
            else if (validMoves[0].isJump())
               helpMessage.setText("BLACK:  Make your move.  You must jump.");
            else
               helpMessage.setText("BLACK:  Make your move.");
         }
         else {
            currentPlayer = CheckersData.RED;
            validMoves = board.getValidMoves(currentPlayer);
            if (validMoves == null)
               helpMessage.setText("RED has no moves.  BLACK wins.");
            else if (validMoves[0].isJump())
               helpMessage.setText("RED:  Make your move.  You must jump.");
            else
               helpMessage.setText("RED:  Make your move.");
         }
         
         // if no row is selected then the below variable is initialized with -1
         rowChosen = -1;
         
         if (validMoves != null) {
            boolean sameStartSquare = true;
            for (int i = 1; i < validMoves.length; i++)
               if (validMoves[i].r1 != validMoves[0].r1
                     || validMoves[i].c1 != validMoves[0].c1) {
                  sameStartSquare = false;
                  break;
               }
            if (sameStartSquare) {
               rowChosen = validMoves[0].r1;
               colChosen = validMoves[0].c1;
            }
         }
         repaint();
         
      } 
      
      
      /**
       * Draw a checker board pattern in gray and lightGray.  Draw the
       * checkers.  If a game is in progress, hi-light the legal moves.
       */
      public void paintComponent(Graphics g) {
         
         /* Draw a two-pixel black border around the edges of the canvas. */
         
         g.setColor(Color.black);
         g.drawRect(0,0,getSize().width-1,getSize().height-1);
         g.drawRect(1,1,getSize().width-3,getSize().height-3);
         
         /* Draw the squares of the checker board and the checkers. */
         
         for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
               if ( row % 2 == col % 2 )
                  g.setColor(Color.WHITE);
               else
                  g.setColor(Color.BLACK);
               g.fillRect(2 + col*20, 2 + row*20, 20, 20);
               switch (board.pieceAt(row,col)) {
               case CheckersData.RED:
                  g.setColor(Color.RED);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  break;
               case CheckersData.BLACK:
                  g.setColor(Color.BLACK);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  break;
               case CheckersData.R_KING:
                  g.setColor(Color.RED);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  g.setColor(Color.WHITE);
                  g.drawString("K", 7 + col*20, 16 + row*20);
                  break;
               case CheckersData.B_KING:
                  g.setColor(Color.BLACK);
                  g.fillOval(4 + col*20, 4 + row*20, 15, 15);
                  g.setColor(Color.WHITE);
                  g.drawString("K", 7 + col*20, 16 + row*20);
                  break;
               }
            }
         }
         
         if (gameInProgress) {
               
            g.setColor(Color.cyan);
            // cyan border to indicate the pieces that can be moved
            for (int i = 0; i < validMoves.length; i++) {
               g.drawRect(2 + validMoves[i].c1*20, 2 + validMoves[i].r1*20, 19, 19);
               g.drawRect(3 + validMoves[i].c1*20, 3 + validMoves[i].r1*20, 17, 17);
            }
               
            if (rowChosen >= 0) {
               g.setColor(Color.white);
               g.drawRect(2 + colChosen*20, 2 + rowChosen*20, 19, 19);
               g.drawRect(3 + colChosen*20, 3 + rowChosen*20, 17, 17);
               g.setColor(Color.green);
               for (int i = 0; i < validMoves.length; i++) {
                  if (validMoves[i].c1 == colChosen && validMoves[i].r1 == rowChosen) {
                     g.drawRect(2 + validMoves[i].c2*20, 2 + validMoves[i].r2*20, 19, 19);
                     g.drawRect(3 + validMoves[i].c2*20, 3 + validMoves[i].r2*20, 17, 17);
                  }
               }
            }
         }
      } 
      
      /**
       * Respond to a user click on the board. 
       *  find the row and column that the user 
       * clicked and call selectSqaure() to handle it.
       */
      public void mousePressed(MouseEvent e) {
            int col = (e.getX() - 2) / 20;
            int row = (e.getY() - 2) / 20;
            if (col >= 0 && col < 8 && row >= 0 && row < 8)
               selectSquare(row,col);
      }
      
      
      public void mouseReleased(MouseEvent evt) { }
      public void mouseClicked(MouseEvent evt) { }
      public void mouseEntered(MouseEvent evt) { }
      public void mouseExited(MouseEvent evt) { }
   } 
}
