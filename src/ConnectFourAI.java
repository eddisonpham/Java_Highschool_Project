import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
//Name: Eddison Pham
//Date: 1/7/2022
//Purpose: Connect Four AI
public class ConnectFourAI extends JPanel implements ActionListener
{
	//board dimensions
	int row=4;
	int col=4;
	//pictures data
	JButton pics[][]= new JButton[row][col];
	//board data
	int bo[][] = new int[row][col];
	//hold screens
	Panel screens;
	//the screens
	Panel instructions, play;
	//card layout
	CardLayout layout = new CardLayout();
	//grid to hold squares for the game
	Panel grid;
	//the turn (1 = black, 2 = white)
	int turn = 1;
	//initialized turn label
	JLabel turnLabel;
	//array of possible moves for current player (pm stands for possible moves)
	ConnectFourMoves pm[] = new ConnectFourMoves[col];
	//when there is a winner, this variable is set to the winner's integer ID (1 = black, 2 = white)
	int winner;
	//constructor method
	public ConnectFourAI ()
	{
		screens = new Panel();
		screens.setLayout(layout);
		screens.setBackground(Color.white);//background colour
		createGrid();
		instructions();
		play();
		setLayout(new BorderLayout());
		add("Center",screens);
		refresh();
	}
	//creates the grid for the game
	public void createGrid() {
		//initialize the grid panel
		grid = new Panel(new GridLayout(row,col));
		for (int i =0;i<row;i++) {//creates the individual squares
			for(int j =0;j<col;j++) {
				pics[i][j] = new JButton();
				pics[i][j].setBackground(Color.white);
				pics[i][j].setIcon(createImageIcon("f"+bo[i][j]+".png"));
				pics[i][j].setBorder(null);
				pics[i][j].setPreferredSize(new Dimension(100,100));
				pics[i][j].setActionCommand(i+" "+j);
				pics[i][j].addActionListener(this);
				grid.add(pics[i][j]);
			}
		}
	}
	//instructions screen
	public void instructions() {
		instructions = new Panel();
		//instructions label
		JLabel title = new JLabel("Connect Four AI Instructions");
		title.setFont(new Font(Final.font,Font.BOLD,22));
		title.setPreferredSize(new Dimension(400,60));//y padding
		JLabel tutorial = new JLabel(createImageIcon("cfAITutorial.png"));
		JButton next = new JButton("Play");
		next.setFont(new Font(Final.font,Font.BOLD,20));
		next.setBackground(Color.white);
		next.setPreferredSize(new Dimension(400,30));
		next.setActionCommand("play");
		next.addActionListener(this);
		//home button
		JButton home = new JButton("Return To Home Screen");
		home.setFont(new Font(Final.font,Font.BOLD,20));
		home.setBackground(Color.white);
		home.setPreferredSize(new Dimension(400,30));
		home.setActionCommand(Final.home);
		home.addActionListener(this);
		//add all widgets to screen
		instructions.add(title);
		instructions.add(tutorial);
		instructions.add(next);
		instructions.add(home);
		screens.add("instructions",instructions);
	}
	//playing screen
	public void play() {
		play = new Panel();
		//title
		JLabel title = new JLabel("Connect Four");
		title.setFont(new Font(Final.font,Font.BOLD,Final.size));
		//instructions button
		JButton instructions = new JButton("Instructions");
		instructions.setFont(new Font(Final.font,Font.BOLD,20));
		instructions.setBackground(Color.white);
		instructions.setPreferredSize(new Dimension(400,30));
		instructions.setActionCommand("instructions");
		instructions.addActionListener(this);
		//turn label
		turnLabel = new JLabel("Black's turn.");
		turnLabel.setFont(new Font(Final.font,Font.BOLD,20));
		turnLabel.setBackground(Color.white);
		turnLabel.setPreferredSize(new Dimension(400,30));
		//auto end button
		JButton autoEnd = new JButton("Auto End");
		autoEnd.setFont(new Font(Final.font,Font.BOLD,20));
		autoEnd.setBackground(Color.white);
		autoEnd.setPreferredSize(new Dimension(400,30));
		autoEnd.setActionCommand("Auto End");
		autoEnd.addActionListener(this);
		//reset button
		JButton reset = new JButton("Reset");
		reset.setFont(new Font(Final.font,Font.BOLD,20));
		reset.setBackground(Color.white);
		reset.setPreferredSize(new Dimension(400,30));
		reset.setActionCommand("reset");
		reset.addActionListener(this);
		//add all widgets to screen
		play.add(title);
		play.add(grid);
		play.add(turnLabel);
		play.add(autoEnd);
		play.add(reset);
		play.add(instructions);
		screens.add("play",play);
		createConnectFourMovesHolder();//creates the valid moves array object (ConnectFourMoves class)
		checkValidMoves(turn);//creates the initial valid moves for the first player moving (user)
	}
	//creates the array to hold possible moves
	public void createConnectFourMovesHolder(){
		for (int i =0;i<col;i++) {
			pm[i]=new ConnectFourMoves(0,0,false);
		}
	}
	//dev tool, used to print out the board numbers
	public void print() {
		for (int i =0 ;i<row;i++) {
			for (int j =0 ;j<col;j++) {
				System.out.print(bo[i][j]+" ");
			}
			System.out.println();
		}
	}
	//checks for valid moves
	public void checkValidMoves(int t) {
		clearPlaceableSpots();
		int index = 0;//sometimes an entire column is full so the array's length needs to be cut
		//loops through board
		for (int i =0 ;i<row;i++) {
			for (int j =0 ;j<col;j++) {
				if (((i+1<row&&bo[i+1][j]!=0)||i==row-1)&&bo[i][j]==0) {//if is a valid move, create that position on the pm array
					pm[index]=new ConnectFourMoves(i,j,true);
					index++;
				}
			}	
		}
		//sets the board positions of all of the possible moves (put it in the board's data)
		for (ConnectFourMoves moves:pm) {
			if (moves.open) {
				bo[moves.x][moves.y]=(turn==1)?4:3;
			}
		}
	}
	//refresh screen
	public void refresh() {
		for (int i = 0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				pics[i][j].setIcon(createImageIcon("p"+bo[i][j]+".png"));
			}
		}
	}
	//listen for clicks
	public void actionPerformed (ActionEvent e)
	{
		screensManeuver(e);//move through screens
		//reset button
		if (e.getActionCommand().equals("reset")) {
			reset();	
		//automatically ends game with random, but legal moves
		}else if (e.getActionCommand().equals("Auto End")) {
			autoEnd();
		//go back to menu/main screen
		}else if (e.getActionCommand().equals(Final.home)) {
			Final.mainScreen();
		}
		clickedOnBoard(e);//listen for clicks on the game grid
		refresh();
	}
	//checks for clicks on the board
	public void clickedOnBoard(ActionEvent e) {
		for (int i =0;i<row;i++) {//loops through board
			for (int j =0;j<row;j++) {
				if (e.getActionCommand().equals(i+" "+j)&&isValid(i,j)) {//gets the square clicked
					bo[i][j]=turn;
					if (gameOver()=='n') {//if game is still continuing, play move sound and make it bot's turn
						Final.playSound("moveSound.wav");
						botMove();
					}
					if (gameOver()=='t'){//otherwise if it's a tie, set label to tie
						Final.playSound("gameOver.wav");
						turnLabel.setText("Tie.");
					}else if (gameOver()=='b'||gameOver()=='w'){//otherwise if there is a winner, set text to the winner and clear all placeable spots
						clearPlaceableSpots();
						Final.playSound("gameOver.wav");
						turnLabel.setText(((gameOver()=='b')?"Black ":"AI ")+"is the winner!");
					}
				}
			}
		}
	}
	//maneuver through screens
	public void screensManeuver(ActionEvent e) {
		//play the game
		if (e.getActionCommand().equals("play")) {
			Final.playSound("startGame.wav");
			layout.show(screens, "play");
		//go back to instructions screen
		}else if (e.getActionCommand().equals("instructions")) {
			layout.show(screens, "instructions");
		}
	}
	//resets the entire game 
	public void reset() {
		resetBo();
		Final.playSound("startGame.wav");
		turn=1;
		turnLabel.setText("Black's turn");
		checkValidMoves(turn);
	}
	//prompts bot's move (allowed to be over 30 lines according to previous project)
	public void botMove() {
		int bestscore = Integer.MIN_VALUE;
		int best_x=0;
		int best_y=0;
		//clears the small dots to prompt user's moves
		clearPlaceableSpots();
		//loops through all possible positions
		for (int i =0 ;i<row;i++) {//loops through entire board
			for (int j =0 ;j<col;j++) {
				//checks if it's a valid move
				if (((i+1<row&&bo[i+1][j]!=0)||i==row-1)&&bo[i][j]==0) {
					//places on spot
					turn=2;
					bo[i][j]=turn;
					//recursion
					int score = minimax(true,0,Integer.MIN_VALUE,Integer.MAX_VALUE);
					//undo move
					bo[i][j]=0;
					//compare score evaluation for position
					if (score>bestscore) {//maximize the best score
						bestscore=score;
						best_x=i;
						best_y=j;
					}
				}
			}	
		}
		bo[best_x][best_y]=2;//set position
		turn=1;
		checkValidMoves(1);//prompts user's move
		refresh();//refresh to update screen
	}
	//minimax + alpha beta pruning evaluator  (allowed to be over 30 lines according to previous project)
	public int minimax(boolean maximizing,int d,int alpha,int beta) {
		char result = gameOver();
		if (result=='w') {//maximizing player (bot)
			return 1;
		}else if (result == 'b') {//minimizing player (user)
			return -1;
		}else if (result=='t'){//tie check
			return 0;
		}
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		if(maximizing) {//maximizing player minimax
			for (int i =0 ;i<row;i++) {//loops through entire board
				for (int j =0 ;j<col;j++) {
					if (((i+1<row&&bo[i+1][j]!=0)||i==row-1)&&bo[i][j]==0) {//checks if position is valid 
						turn=1;
						bo[i][j]=turn;//sets position
						int score = minimax(false,d+1,alpha,beta);//position score evaluation
						bo[i][j]=0;//undo position
						min = Math.min(score, min);
						beta = Math.min(beta,min);//compares beta to min
						if (beta<=alpha) {//alpha beta pruning
							break;
						}
					}
				}	
			}
			return min;
		}else {//minimizing player minimax
			for (int i =0 ;i<row;i++) {//loops through entire board
				for (int j =0 ;j<col;j++) {
					if (((i+1<row&&bo[i+1][j]!=0)||i==row-1)&&bo[i][j]==0) {//checks if position is valid
						turn=2;
						bo[i][j]=turn;//sets position
						int score = minimax(true,d+1,alpha,beta);//position score evaluation
						bo[i][j]=0;//undo position
						max = Math.max(score, max);
						alpha = Math.max(alpha,max);//compares alpha to max
						if(beta<=alpha) {//alpha beta pruning
							break;
						}
					}
				}	
			}
			return max;
		}
	}
	//automatically ends the game with valid moves (randomly placed)
	public void autoEnd() {
		clearPlaceableSpots();
		boolean autoEnding = true;
		while (autoEnding) {//while loop to flip the turns until game is over
			int movesArrLength = getMovesArrLength();
			ConnectFourMoves rand = pm[(int)(Math.random()*movesArrLength)];//random possible move position for current player
			int x = rand.x;//set x pos
			int y = rand.y;//set y pos
			bo[x][y]=turn;//place the player's number onto the board data with the given positions
			if (gameOver()=='n') {//if game is still continuing, flip turn and check moves for opponent
				turn = (turn==1)?2:1;
				checkValidMoves(turn);
			}else if (gameOver()=='t'){//otherwise if it's a tie, end game and set the label to tie, break the loop
				turnLabel.setText("Tie");
				autoEnding=false;
			}else {//otherwise there is a winner, set the label to the winner and break the loop
				clearPlaceableSpots();
				turnLabel.setText(((turn==1)?"Black ":"AI ")+"is the winner!");
				autoEnding=false;
			}
		}
		Final.playSound("gameOver.wav");//game over sound
	}
	//gets the length of the PM array
	public int getMovesArrLength() {
		int k = 0;
		for (ConnectFourMoves moves:pm) {
			if (moves.open) {
				k++;
			}
		}
		return k;
	}
	//checks for game over
	public char gameOver() {
		for (int x = 0;x<row;x++) {
			for (int y = 0;y<col;y++) {
				boolean up = checkUp(x,y);//checks for similar pieces upwards
				boolean down = checkDown(x,y);//checks for similar pieces downwards
				boolean left = checkLeft(x,y);//checks for similar pieces left
				boolean right = checkRight(x,y);//checks for similar pieces right
				boolean dl = checkDL(x,y);//checks for similar pieces down left
				boolean dr = checkDR(x,y);//checks for similar pieces down right
				boolean ul = checkUL(x,y);//checks for similar pieces up left
				boolean ur = checkUR(x,y);//checks for similar pieces up right
				if(up||down||left||right||dl||dr||ul||ur) {//checks if any of these booleans above exist, if so, game over
					return (winner==1)?'b':(winner==2)?'w':'n';
				}else if(tie()) {//otherwise return t for tie if the board is filled
					return 't';
				}
			}
		}
		//otherwise continue game
		return 'n';
	}
	//checks upwards (SAME EXACT COMMENT FOR ALL THE OTHER CHECK METHODS BELOW, DONT WANT TO REPEAT COMMENTS 7 MORE TIMES)
	public boolean checkUp(int x, int y) {
		//first boolean is the boundaries
		//the last boolean checks if the alignment is 1 or 2 (players #, not valid moves #)
		//second last boolean checks if the alignment is not an empty space
		//the rest check for similar piece alignment in a row, if there is an alignment, set the winner's value
		if(x-3>=0&&bo[x][y]==bo[x-1][y]&&bo[x][y]==bo[x-2][y]&&bo[x][y]==bo[x-3][y]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];//sets the winner
			return true;
		}
		return false;
	}
	//check downwards
	public boolean checkDown(int x, int y) {
		if(x+3<row&&bo[x][y]==bo[x+1][y]&&bo[x][y]==bo[x+2][y]&&bo[x][y]==bo[x+3][y]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;	
	}
	//check left
	public boolean checkLeft(int x, int y) {
		if(y-3>=0&&bo[x][y]==bo[x][y-1]&&bo[x][y]==bo[x][y-2]&&bo[x][y]==bo[x][y-3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;
	}
	//check right
	public boolean checkRight(int x, int y) {
		if(y+3<col&&bo[x][y]==bo[x][y+1]&&bo[x][y]==bo[x][y+2]&&bo[x][y]==bo[x][y+3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;
	}
	//check down left
	public boolean checkDL(int x, int y) {
		if(x+3<row&&y-3>=0&&bo[x][y]==bo[x+1][y-1]&&bo[x][y]==bo[x+2][y-2]&&bo[x][y]==bo[x+3][y-3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;
	}
	//check down right
	public boolean checkDR(int x, int y) {
		if(x+3<row&&y+3<col&& bo[x][y]==bo[x+1][y+1]&&bo[x][y]==bo[x+2][y+2]&&bo[x][y]==bo[x+3][y+3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;
	}
	//check up left
	public boolean checkUL(int x, int y) {
		if(x-3>=0&&y-3>=0&&bo[x][y]==bo[x-1][y-1]&&bo[x][y]==bo[x-2][y-2]&&bo[x][y]==bo[x-3][y-3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;
	}
	//check up right
	public boolean checkUR(int x, int y) {
		if(x-3>=0&&y+3<col&&bo[x][y]==bo[x-1][y+1]&&bo[x][y]==bo[x-2][y+2]&&bo[x][y]==bo[x-3][y+3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;	
	}
	//checks if the board is filled (with 1s and 2s)
	public boolean tie() {
		for (int i =0;i<row;i++) {
			for (int j =0;j<row;j++) {
				if (bo[i][j]==0||bo[i][j]>2) {//if there is still a possible move or an empty space, continue game
					return false;
				}
			}
		}
		return true;
	}
	//clear all the visible placeable spots in game
	public void clearPlaceableSpots() {
		//set all PM item's open attribute to false 
		for (ConnectFourMoves moves:pm) {
			moves.open=false;
		}
		//clears the possible moves pictures on the board
		for (int i =0;i<row;i++) {
			for (int j =0;j<row;j++) {
				if (bo[i][j]>2) {
					bo[i][j]=0;
				}
			}
		}
	}
	//checks if the move player selected is valid
	public boolean isValid(int x, int y) {
		for (ConnectFourMoves moves:pm) {
			//if the spot is exists in the array, then it's a place-able spot
			if (bo[x][y]>2){
				return true;
			}
		}
		return false;
	}
	//resets board
	public void resetBo() {
		for (int i =0;i<row;i++) {
			for (int j =0;j<col;j++) {
				bo[i][j]=0;
			}
		}
	}
	protected static ImageIcon createImageIcon (String path)
	{
		java.net.URL imgURL = _2048.class.getResource (path);
		if (imgURL != null)
			return new ImageIcon (imgURL);
		else
			return null;
	}
}