package TicTacGame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**********************************************************
 * 
 * @author Jacqueline Lee
 * 
 * Program: a simple game of tic tac toe.  
 * Currently: the AI always goes first. The user is always 'x'.
 * the game is impossible to win( AI should either always win or tie).  
 *
 * TicTacToeFrame is the driver class and general game manager/controller
 * 
 * Does not contain much error checking and is currently fragile. (and messy!)
 * NOTE: will make player class and have AIPlayer extend
 * will have alternating turns and multiple games
 * May later implement difficulty levels.
 * 
 */
public class TicTacToeFrame extends JFrame{
	private static final long serialVersionUID = -3462276804456866674L;
	private static final int FILLED = 9;	//capacity for game board
	private static final int WIDTH = 390;
	private static final int HEIGHT = 390;
	private static final String GAMETITLE = "Tic Tac Toe";
	
	private JPanel tictacPanel;
	private TicTacButton b00;
	private TicTacButton b01;
	private TicTacButton b02;
	private TicTacButton b10;
	private TicTacButton b11;
	private TicTacButton b12;
	private TicTacButton b20;
	private TicTacButton b21;
	private TicTacButton b22;
	
	private int[][] pBoard;	 				//stores moves in game so far player
	private static TicTacButton[][] board;  //stores possible moves
    private static AIplayer AI;
    private int full;		   				//keeps track of used spaces
   
    /******************* inner class TicTacButton **********
     *	the spaces available to make a move on 
     *  for the tic tac toe board are buttons.
     **/
    protected class TicTacButton extends JButton{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 5200264792702882279L;
		private static final String AIimgPath = "src/TicTacGame/imgs/o.png";
    	private static final String pImgPath = "src/TicTacGame/imgs/x.png";
    	private int x, y;	//button coordinates
    	
    	private TicTacButton(){
    		setPreferredSize(new Dimension(WIDTH/3, HEIGHT/3));
    		setBackground(Color.WHITE);
    	}
    	
    	private TicTacButton(int x, int y){
    		this();
    		this.x = x;
    		this.y = y;
    	}
    	
    	
    	public int getbX(){
    		return x;
    	}
    	
    	public int getbY(){
    		return y;
    	}
    	
    	private void drawAIImage(){
    		ImageIcon img = new ImageIcon(AIimgPath);
    		setIcon(img);
    	}
    	
    	private void drawPImage(){
    		ImageIcon img = new ImageIcon(pImgPath);
    		setIcon(img);
    	}
    }
    
    
    /******************* inner class MyMouse **************
     *  is a custom mouse listener, 
     *  listens for the mouseClicked event 
     */
    private class MyListener implements ActionListener{
    	
    	@Override
    	public void actionPerformed(ActionEvent event){
  
    		//take and store player turn
    		((TicTacButton) event.getSource()).drawPImage();
    		int x = ((TicTacButton) event.getSource()).getbX();
    		int y = ((TicTacButton) event.getSource()).getbY(); 
    		pBoard[x][y] = 1;
    		full++;
    		AI.updatePMove(x, y);
    		((Component) event.getSource()).setEnabled(false);
    		
    		//check for 3 in a row, player
    		if(threeInRowP()){
    			JOptionPane.showMessageDialog(tictacPanel, "You have Won!");
    			System.exit(0);
    		}
    		//check if full
    		if(full == FILLED){
    			JOptionPane.showMessageDialog(tictacPanel, "The Game is Tied.");
    			System.exit(0);
    		}
    		
    		//disable GUI
    		//for(Component c: tictacPanel.getComponents())
    		//	c.setEnabled(false);
    		
    		
    		 AI.takeTurn();
    		 performAIMove();
    		
    		//check for 3 in a row, AI
    		if(AI.threeInRowAI()){
    			JOptionPane.showMessageDialog(tictacPanel, "Sorry, you lost the game.");
    			System.exit(0);
    		}
    		//check if full
    		if(full == FILLED){
    			JOptionPane.showMessageDialog(tictacPanel, "The Game is Tied.");
    			System.exit(0);
    		}
    	}
    }
    
    
    //**********default constructor**********//
	public TicTacToeFrame(){
		super(GAMETITLE); // create frame with a title
		board = new TicTacButton[3][3];
		pBoard = new int[3][3];
		AI = new AIplayer();
		full = 0;
	
		// set up frame
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT)); // set size to 390px by 420px
		setResizable(false); // frame size is fixed
		
		// When default close is clicked, display a confirm dialog box
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				confirmExit();
				}
		});

		//set up tic tac toe panel		
		tictacPanel = new JPanel();
		tictacPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		tictacPanel.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		tictacPanel.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		tictacPanel.setLayout(new GridLayout(3,3));
		add(tictacPanel);
		
		//add buttons
		MyListener ml = new MyListener();
		b00 = new TicTacButton(0,0);
		b01 = new TicTacButton(0,1);
		b02 = new TicTacButton(0,2);
		b10 = new TicTacButton(1,0);
		b11 = new TicTacButton(1,1);
		b12 = new TicTacButton(1,2);
		b20 = new TicTacButton(2,0);
		b21 = new TicTacButton(2,1);
		b22 = new TicTacButton(2,2);
		b00.addActionListener(ml);
		b01.addActionListener(ml);
		b02.addActionListener(ml);
		b10.addActionListener(ml);
		b11.addActionListener(ml);
		b12.addActionListener(ml);
		b20.addActionListener(ml);
		b21.addActionListener(ml);
		b22.addActionListener(ml);
		board[0][0] = b00;
		board[1][0] = b10;
		board[2][0] = b20;
		board[0][1] = b01;
		board[1][1] = b11;
		board[2][1] = b21;
		board[0][2] = b02;
		board[1][2] = b12;
		board[2][2] = b22;
		tictacPanel.add(b00);
		tictacPanel.add(b10);
		tictacPanel.add(b20);
		tictacPanel.add(b01);
		tictacPanel.add(b11);
		tictacPanel.add(b21);
		tictacPanel.add(b02);
		tictacPanel.add(b12);
		tictacPanel.add(b22);

		// display frame
		pack();
		setVisible(true);
		setLocationRelativeTo(null); // Centers window on screen		
	}
	
	
	private static TicTacButton getTicTacButton(int bx, int by){
		return board[bx][by];
	}
	
	//************ performAIMove()*****************//
	private Boolean performAIMove(){
		//AI.takeTurn();
		int aix = AI.getAX();
		int aiy = AI.getAY();
		if(pBoard[aix][aiy] == 1)
			return false;
        (getTicTacButton(aix, aiy) ).drawAIImage();
        (getTicTacButton(aix, aiy) ).setEnabled(false);
        full++;
        return true;
	}
	
	
	//************ threeInRowP() *************//
	private boolean threeInRowP(){
		//int[] result = new int[9];
		//int i = 0;
		//for(int pelement : pBoard)
		//	for(int selement : solutions)
		//		result[i] = pelement AND selement;
		//if sum(result) == 3  ->  return true
		 if (pBoard[0][0] == 1 && pBoard[0][1] == 1 && pBoard[0][2] == 1)
			 return true;
		 if (pBoard[1][0] == 1 && pBoard[1][1] == 1 && pBoard[1][2] == 1)
			 return true;
		 if (pBoard[2][0] == 1 && pBoard[2][1] == 1 && pBoard[2][2] == 1)
			 return true;
		 if (pBoard[0][0] == 1 && pBoard[1][0] == 1 && pBoard[2][0] == 1)
			 return true;
		 if (pBoard[0][1] == 1 && pBoard[1][1] == 1 && pBoard[2][1] == 1)
			 return true;
		 if (pBoard[0][2] == 1 && pBoard[1][2] == 1 && pBoard[2][2] == 1)
			 return true;

		// check diagonals
		 if (pBoard[0][0] == 1 && pBoard[1][1] == 1 && pBoard[2][2] == 1)
			 return true;		 
		 if (pBoard[0][2] == 1 && pBoard[1][1] == 1 && pBoard[2][0] == 1)
			 return true;
		return false;
	}
	
	
	//************ confirmExit() *************//
	private void confirmExit() {
		int proceed = JOptionPane.showConfirmDialog(this,
				"Leaving now?","Are you sure?", JOptionPane.OK_CANCEL_OPTION);
			if (proceed == JOptionPane.OK_OPTION)
				System.exit(0);
	}
		
		
	public static void main(String[] args){
		
		EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                TicTacToeFrame game = new TicTacToeFrame();
                game.AI.takeTurn();
                game.performAIMove();
            }
        });
	}



	
	
}
