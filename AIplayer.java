package TicTacGame;
import java.util.Random;

import javax.swing.JOptionPane;

///////////////////////////////////////////////////////////////////////////////////
//
//
///
/// USES:    0 1 2 x     the board squares are = x+y, held in 2d array
///        0 0 1 2       and enables testing of patterns to be done  
///        1 1 2 3       more quickly(than bit map solutions and/or testing each side) 
///        2 2 3 4       through algorithm and states
///        y
///       
public class AIplayer{
	private int[][] AI;		//AI current moves
	private int[][] p;		//player current moves
	private int turns;		//number of turns already taken by AI
	protected int px, py;	//coordinates for last player turn
	protected int px1, py1; //coordinates player first move
	protected int ax, ay;	////coordinates for AI turn (last turn and for current)
	protected int ax1, ay1; //coordinates AI first move
	
	
	//********* default constructor *****************
	public AIplayer(){
		turns = 0;
		p = new int[3][3];
		AI = new int[3][3];
				
	}
	
	
	//***************** takeTurn() ********************
	// game is mostly decided by first 2 moves
	public boolean takeTurn(){
		
		
		if(turns == 0){
			//randomly choose a corner if going first
			//x and y should be either a 0 or 2
			Random rand = new Random();
			ax = (rand.nextInt(2)) * 2;	//generate random num within range 0-1
												//multiply it by 2
			ay = (rand.nextInt(2)) * 2;
			ax1 = ax;
			ay1 = ay;
			if(makemove())
				return true;
			else{
				JOptionPane.showInternalMessageDialog(null, "Error occured");
				System.exit(1);
			}
		}
		if(fill()) return true;
		if(block()) return true;
		if(turns <= 2 && turns != 0){
			if(assessmentandFill())
				return true;
			
		}
		
		if(!random())
			return false;
				//if at this point game no moves are available
				//finished game
		
		turns++;
		return true;
	}
	
	//************* makemove() **************
	private boolean makemove(){
		if(AI[ax][ay] == 1 || p[ax][ay] == 1)
			return false;
		try{
			AI[ax][ay] = 1;
		}catch(Exception e){
			e.printStackTrace();
		}
		turns++;
		return true;

	}
	
	//**************** assessmentandFill() **************
	private boolean assessmentandFill(){
		//x1 mid -> diag move -> fill block random
		//x1 adjacent -> across move -> ?? ->  fill block random
		//x1 across -> diag (cuts path)  -> fill block random
		//x1 diag -> t1: across move, t2: diagonal
				 //else -> fill block random
		int[] m = new int[2]; 
		if(p[1][1] == 1 && turns == 1){ /* x1 mid */
			m = getDiag(ax1, ay1);
			ax = m[0];
			ay = m[1];
			if(!makemove())
				return false;
					
		}
		else if(adjacent(px1, py1)){
			if(turns == 1){
			m = getAcross(ax1, ay1, px, py, true);
			ax = m[0];
			ay = m[1];
			if(!makemove()) 
				return false;
			}
			else if(turns == 2 ){
			m = getDiag(ax1, ay1);
			ax = m[0];
			ay = m[1];
			if(!makemove()) 
				return false;
			}
		}
		else if(across(px1, py1) && turns == 1){
				m = getDiag(ax1, ay1);
				ax = m[0];
				ay = m[1];
				if(!makemove()) 
					return false;
		}
		else if(diagonal(px1, py1)){
			if(turns == 1){
				m = getAcross(ax1, ay1);
				ax = m[0];
				ay = m[1];
				if(!makemove())
					return false;
			}
			else if(turns == 2){
				m = getDiag(ax1, ay1);
				ax = m[0];
				ay = m[1];
				if(!makemove())
					return false;
			}
		}
		else{
			if(!random())
				return false;
		}
		return true;
	}
	
	////////////////////// getDiag() getAdj() getAcross() ///////////////
	private int[] getDiag(int cx, int cy){
		int[] r = new int[2];
		
		if(cx == 1 || cy == 1){ //no diagonal exist if not corner
			r[0] = -1;
			r[1] = -1;
		}
		
		if(cx == 0)
			r[0] = 2;
		else
			r[0] = 0;
		if(cy == 0)
			r[1] = 2;
		else
			r[1] = 0;
	
		return r;
	}
	
	
	//*************** getAdj()**************
	private int[] getAdj(int cx, int cy){
		int[] r = new int[2];
		
		if(px1 == cx){ //if share x axis
			r[0] = cx;
			r[1] = 1;
		}
		else if(py1 == cy){ //if share y axis
			r[1] = cy;
			r[0] = 1;
		}
		return r;
	}
	
	
	
	//***************** getAcross() ***************
	//gets across first player move
	private int[] getAcross(int cx, int cy){
		int[] r = new int[2];
		
		if(px1 == cx){ //if share x axis
			r[0] = cx;
			if(cy == 0){
				r[1] = 2;
			}
			else if(cy == 2){
				r[1] = 0;
			}
			
		}
		else if(py1 == cy){ //if share y axis
			r[1] = cy;
			if(cx == 0){
				r[0] = 2;
			}
			else if(cx == 2){
				r[0] = 0;
			}
	
		}
		
		return r;
	}
	
	
	//*********** getAcross() *********************
	//gets across along an edge of two parameterized game spaces
	//or that along the other edge
	//edge is true if along the two spaces, false for other edge
	private int[] getAcross(int ix, int iy, int cx, int cy, boolean edge){
		int[] r = new int[2];
		if(ix == cx){ //if share x axis
			r[0] = ix;
			if(iy == 0)
				r[1] = 2;
			
			else if(iy == 2)
				r[1] = 0;
						
		}
		else if(iy == cy){ //if share y axis
			r[1] = iy;
			if(ix == 0)
				r[0] = 2;
			
			else if(ix == 2)
				r[0] = 0;
			
		}	
		if(!edge)
			return r;
	
		
		r = getDiag(r[0], r[1]);
		return r;
		
	}
	
	/////////////////////// fill() block() random() ////////////////////
	private boolean fill(){
		//check 110 011 101 for each axis
				
		//x axis
		if(AI[ax][0] == 1 && AI[ax][1] == 1 && AI[ax][2] == 0){
			ax = ax;
			ay = 2;
			if(makemove()) return true;
		}
		if(AI[ax][0] == 0 && AI[ax][1] == 1 && AI[ax][2] == 1){
			ax = ax;
			ay = 0;
			if(makemove()) return true;
		}
		if(AI[ax][0] == 1 && AI[ax][1] == 0 && AI[ax][2] == 1){
			ax = ax;
			ay = 1;
			if(makemove()) return true;
		}
		//y axis
		if(AI[0][ay] == 1 && AI[1][ay] == 1 && AI[2][ay] == 0){
			ax = 2;
			ay = ay;
			if(makemove()) return true;
		}
		if(AI[0][ay] == 0 && AI[1][ay] == 1 && AI[2][ay] == 1){
			ax = 0;
			ay = ay;
			if(makemove()) return true;
		}
		if(AI[0][ay] == 1 && AI[1][ay] == 0 && AI[2][ay] == 1){
			ax = 1;
			ay = ay;
			if(makemove()) return true;
		}
				
		//check for diagonals
		if(AI[0][0] == 1 && AI[1][1] == 1 && AI[2][2] == 0){
			ax = 2;
			ay = 2;
			if(makemove()) return true;
		}
		if(AI[0][0] == 0 && AI[1][1] == 1 && AI[2][2] == 1){
			ax = 0;
			ay = 0;
			if(makemove()) return true;
		}
		if(AI[0][0] == 1 && AI[1][1] == 0 && AI[2][2] == 1){
			ax = 1;
			ay = 1;
			if(makemove()) return true;
		}
		//
		if(AI[0][2] == 1 && AI[1][1] == 1 && AI[2][0] == 0){
			ax = 2;
			ay = 0;
			if(makemove()) return true;
		}
		if(AI[0][2] == 0 && AI[1][1] == 1 && AI[2][0] == 1){
			ax = 0;
			ay = 2;
			if(makemove()) return true;
		}
		if(AI[0][2] == 1 && AI[1][1] == 0 && AI[2][0] == 1){
			ax = 1;
			ay = 1;
			if(makemove()) return true;
		}
		return false;
	}
	
	//****************** block()**********************
	// currently checks current player move against 
	// player board for pattern
	// NOTE: could use BitSet and XOR
	private boolean block(){
	
		//get current move
		//check 110 011 101 for each axis
		
		//x axis
		if(p[px][0] == 1 && p[px][1] == 1 && p[px][2] == 0){
			ax = px;
			ay = 2;
			if(makemove()) return true;
		}
		if(p[px][0] == 0 && p[px][1] == 1 && p[px][2] == 1){
			ax = px;
			ay = 0;
			if(makemove()) return true;
		}
		if(p[px][0] == 1 && p[px][1] == 0 && p[px][2] == 1){
			ax = px;
			ay = 1;
			if(makemove()) return true;
		}
		//y axis
		if(p[0][py] == 1 && p[1][py] == 1 && p[2][py] == 0){
			ax = 2;
			ay = py;
			if(makemove()) return true;
		}
		if(p[0][py] == 0 && p[1][py] == 1 && p[2][py] == 1){
			ax = 0;
			ay = py;
			if(makemove()) return true;
		}
		if(p[0][py] == 1 && p[1][py] == 0 && p[2][py] == 1){
			ax = 1;
			ay = py;
			if(makemove()) return true;
		}
		
		//check for diagonals
		if(p[0][0] == 1 && p[1][1] == 1 && p[2][2] == 0){
			ax = 2;
			ay = 2;
			if(makemove()) return true;
		}
		if(p[0][0] == 0 && p[1][1] == 1 && p[2][2] == 1){
			ax = 0;
			ay = 0;
			if(makemove()) return true;
		}
		if(p[0][0] == 1 && p[1][1] == 0 && p[2][2] == 1){
			ax = 1;
			ay = 1;
			if(makemove()) return true;
		}
		//
		if(p[0][2] == 1 && p[1][1] == 1 && p[2][0] == 0){
			ax = 2;
			ay = 0;
			if(makemove()) return true;
		}
		if(p[0][2] == 0 && p[1][1] == 1 && p[2][0] == 1){
			ax = 0;
			ay = 2;
			if(makemove()) return true;
		}
		if(p[0][2] == 1 && p[1][1] == 0 && p[2][0] == 1){
			ax = 1;
			ay = 1;
			if(makemove()) return true;
		}
		return false;
	}
	
	private boolean random(){
		
		Random rand = new Random();
		ax = rand.nextInt(2);
		ay = rand.nextInt(2);
		if(AI[ax][ay] == 0 && p[ax][ay] == 0)
			makemove();
		else{ //just go searching for the first spot
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					if(AI[i][j] == 0 && p[i][j] == 0){
						ax = i;
						ay = j;
						makemove();
						return true;
					}
				}	
			}
			return false;
		}//end else
		
 		return true;
	}
	
	//************ threeInRowAI() *************//
		public boolean threeInRowAI(){
			//int[] result = new int[9];
			//int i = 0;
			//for(int pelement : pBoard)
			//	for(int selement : solutions)
			//		result[i] = pelement AND selement;
			//if sum(result) == 3  ->  return true
			 if (AI[0][0] == 1 && AI[0][1] == 1 && AI[0][2] == 1)
				 return true;
			 if (AI[1][0] == 1 && AI[1][1] == 1 && AI[1][2] == 1)
				 return true;
			 if (AI[2][0] == 1 && AI[2][1] == 1 && AI[2][2] == 1)
				 return true;
			 if (AI[0][0] == 1 && AI[1][0] == 1 && AI[2][0] == 1)
				 return true;
			 if (AI[0][1] == 1 && AI[1][1] == 1 && AI[2][1] == 1)
				 return true;
			 if (AI[0][2] == 1 && AI[1][2] == 1 && AI[2][2] == 1)
				 return true;

			// check diagonals
			 if (AI[0][0] == 1 && AI[1][1] == 1 && AI[2][2] == 1)
				 return true;		 
			 if (AI[0][2] == 1 && AI[1][1] == 1 && AI[2][0] == 1)
				 return true;
			return false;
		}
	
	////////////////////////// diagonal() across() adjacent() /////////////////////////   
	//
	//		all relative to first AI move
	//
	//
	private boolean diagonal(int cx, int cy){
		// 0 <-> 2 relationship
		if(ax1 == 0 && cx == 2 || ax1 == 2 && cx == 0)
			if(ay1 == 0 && cy ==2 || ax1 == 2 && ay1 == 0)
				return true;
		return false;
	}
	
	private boolean adjacent(int cx, int cy){
		if(ax1 == cx || ay1 == cy){                //share an axis
			if( Math.abs((ax1+ay1)-(cx+cy)) == 1)  //are +- 1 from each other
												 //accounts for each direction
				return true;
		}
		return false;
	}
	
	private boolean across(int cx, int cy){
		if(ax1 == cx || ay1 == cy){                //share an axis
			if( Math.abs((ax1+ay1)-(cx+cy)) == 2)  //are +- 2 from each other
												 //accounts for each direction
				return true;
		}
		return false;
	}

	/*************** updatePMove() ****************
	* used to keep track of player moves
	*/
	public void updatePMove(int x, int y){
		px = x;
		py = y;
		if(turns == 1){
			px1 = x;
			py1 = y;
		}
		p[x][y] = 1;
	}
	
	///////////////////////// getters and setters /////////////// 
	public int[][] getAI() {
		return AI;
	}

	public int getAX(){
		return ax;
	}
	
	public int getAY(){
		return ay;
	}
	
	public void setAI(int[][] aI) {
		AI = aI;
	}

	public int[][] getP() {
		return p;
	}

	public void setP(int[][] p) {
		this.p = p;
	}

	public int getTurns() {
		return turns;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}



	
	
	
}
