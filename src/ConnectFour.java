import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
//Name: Eddison Pham
//Date: 1/7/2022
//Purpose: Connect Four
public class ConnectFour extends JPanel implements ActionListener
{
	//dimensions of the board
	int row=8;
	int col=8;
	//hold picture data
	JButton pics[][]= new JButton[row][col];
	//board data
	int bo[][] = new int[row][col];
	//screens holder
	Panel screens;
	//screen 1 (instructions) and screen 2 (playing screen)
	Panel instructions, play;
	//card layout
	CardLayout layout = new CardLayout();
	//grid to hold board squares
	Panel grid;
	//check which turn it is
	int turn = 1;
	//changes and tells who's turn it is and the winner
	JLabel turnLabel;
	//list of ConnectFourMoves objects of all the possible current moves (PM stands for possible moves)
	ConnectFourMoves pm[] = new ConnectFourMoves[col];
	//initialize winner variable
	int winner;
	//constructor
	public ConnectFour (){
		//initialize the screen holder
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
	//creates game grid
	public void createGrid() {
		//grid panel to hold the squares
		grid = new Panel(new GridLayout(row,col));
		//creates the individual squares
		for (int i =0;i<row;i++) {
			for(int j =0;j<col;j++) {
				pics[i][j] = new JButton();
				pics[i][j].setBackground(Color.white);
				pics[i][j].setIcon(createImageIcon("f"+bo[i][j]+".png"));
				pics[i][j].setBorder(null);
				pics[i][j].setPreferredSize(new Dimension(50,50));
				pics[i][j].setActionCommand(i+" "+j);
				pics[i][j].addActionListener(this);
				grid.add(pics[i][j]);
			}
		}
	}
	//instructions of connect four
	public void instructions() {
		instructions = new Panel();
		//instructions label
		JLabel title = new JLabel("Connect Four Instructions");
		title.setFont(new Font(Final.font,Font.BOLD,25));
		title.setPreferredSize(new Dimension(400,60));//y padding
		//tutorial image
		JLabel tutorial = new JLabel(createImageIcon("connectFourTutorial.png"));
		//next button
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
		//place all images on the screen
		instructions.add(title);
		instructions.add(tutorial);
		instructions.add(next);
		instructions.add(home);
		screens.add("instructions",instructions);
	}
	
	public void play() {
		play = new Panel();
		//title of the game
		JLabel title = new JLabel("Connect Four");
		title.setFont(new Font(Final.font,Font.BOLD,Final.size));
		//go back to instructions button
		JButton instructions = new JButton("Instructions");
		instructions.setFont(new Font(Final.font,Font.BOLD,20));
		instructions.setBackground(Color.white);
		instructions.setPreferredSize(new Dimension(400,30));
		instructions.setActionCommand("instructions");
		instructions.addActionListener(this);
		//turns label, checks who's turn it is and the winner
		turnLabel = new JLabel("Black's turn.");
		turnLabel.setFont(new Font(Final.font,Font.BOLD,20));
		turnLabel.setBackground(Color.white);
		turnLabel.setPreferredSize(new Dimension(400,30));
		//automatically ends the game, but with valid moves (basically a random playing AI for both sides)
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
		//places all the widgets on screen
		play.add(title);
		play.add(grid);
		play.add(turnLabel);
		play.add(autoEnd);
		play.add(reset);
		play.add(instructions);
		screens.add("play",play);
		createConnectFourMovesHolder();//creates the array to hold the possible moves
		checkValidMoves(turn);//starts by checking all the valid moves a player can make
	}
	//creates array to hold all the current possible moves the player can make
	public void createConnectFourMovesHolder(){
		for (int i =0;i<col;i++) {//loop through the # of columns
			pm[i]=new ConnectFourMoves(0,0,false);//creates a new possible moves object
		}
	}
	//dev tool, used to see the board
	public void print() {
		for (int i =0 ;i<row;i++) {
			for (int j =0 ;j<col;j++) {
				System.out.print(bo[i][j]+" ");
			}
			System.out.println();
		}
	}
	//checks all of the valid moves and updates the grid to show that
	public void checkValidMoves(int t) {
		clearPlaceableSpots();//clear the previous possible moves
		int index = 0;//index variable for pm array because sometimes a column gets full and the array length shrinks
		for (int i =0 ;i<row;i++) {//checks the whole board for possible moves
			for (int j =0 ;j<col;j++) {
				//checks if there is a piece beneath this position OR it's at the bottom, and the current position is empty
				//then that means it's a possible position
				if (((i+1<row&&bo[i+1][j]!=0)||i==row-1)&&bo[i][j]==0) {
					pm[index]=new ConnectFourMoves(i,j,true);//create new possible moves object
					index++;//increment index
				}
			}	
		}
		//show each possible move-able spot onto the screen
		for (ConnectFourMoves moves:pm) {
			if (moves.open) {
				bo[moves.x][moves.y]=(turn==1)?4:3;
			}
		}
	}
	//updates the screen
	public void refresh() {
		for (int i = 0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				pics[i][j].setIcon(createImageIcon("p"+bo[i][j]+".png"));
			}
		}
	}
	//action performed to check for clicks
	public void actionPerformed (ActionEvent e)
	{
		screensManeuver(e);//maneuver through screens
		//reset button
		if (e.getActionCommand().equals("reset")) {
			reset();
		//automatically ends the game
		}else if (e.getActionCommand().equals("Auto End")) {
			autoEnd();
		}else if (e.getActionCommand().equals(Final.home)) {
			Final.mainScreen();
		}
		clickedOnBoard(e);//like the maneuver method but checks for each grid square on screen
		refresh();
	}
	//checks to see if player clicked a square from the grid
	public void clickedOnBoard(ActionEvent e) {
		//loops through every squares on the grid, checks if a certain square has been clicked
		for (int i =0;i<row;i++) {
			for (int j =0;j<row;j++) {
				//if it's a valid position, place a piece there
				if (e.getActionCommand().equals(i+" "+j)&&isValid(i,j)) {
					bo[i][j]=turn;//places the piece
					if (gameOver()=='n') {//checks if the game isn't over
						Final.playSound("moveSound.wav");
						turn = (turn==1)?2:1;//flip turn
						checkValidMoves(turn);//check the valid moves for the opposition
						turnLabel.setText(((turn==1)?"Black's ":"White's ")+"turn");//changes label
					}else if (gameOver()=='t'){//checks if tie
						Final.playSound("gameOver.wav");
						turnLabel.setText("Tie.");//set text to tie
					}else {//otherwise there is a winner
						Final.playSound("gameOver.wav");
						clearPlaceableSpots();//clear all of the place-able spots on board
						turnLabel.setText(((turn==1)?"Black ":"White ")+"is the winner!");//sets text to the winner
					}
				}
			}
		}
	}
	//maneuver through screens
	public void screensManeuver(ActionEvent e) {
		//play screen
		if (e.getActionCommand().equals("play")) {
			layout.show(screens, "play");
			Final.playSound("startGame.wav");
		//instructions screen
		}else if (e.getActionCommand().equals("instructions")) {
			layout.show(screens, "instructions");
		}
	}
	//resets the entire game 
	public void reset() {
		resetBo();//resets board
		Final.playSound("startGame.wav");
		turn=1;//reset turn
		turnLabel.setText("Black's turn");//reset turn label
		checkValidMoves(turn);//checks move for current starting player (black)
	}
	//automatically ends the game with valid moves (randomly placed)
	public void autoEnd() {
		clearPlaceableSpots();//clears all placeable spots
		boolean autoEnding = true;
		while (autoEnding) {//while loop to simulate 2 AI's randomly playing against each other (randomly)
			int movesArrLength = getMovesArrLength();
			ConnectFourMoves rand = pm[(int)(Math.random()*movesArrLength)];
			int x = rand.x;
			int y = rand.y;
			//for the IF statement below, same functionalities as line 189 and after
			bo[x][y]=turn;
			if (gameOver()=='n') {
				turn = (turn==1)?2:1;
				checkValidMoves(turn);
				turnLabel.setText(((turn==1)?"Black's ":"White's ")+"turn");
			}else if (gameOver()=='t'){
				turnLabel.setText("Tie");
				autoEnding=false;
			}else {
				clearPlaceableSpots();
				turnLabel.setText(((turn==1)?"Black ":"White ")+"is the winner!");
				autoEnding=false;
			}
		}
		Final.playSound("gameOver.wav");
	}
	//gets the length of the pm arr (for auto end method)
	public int getMovesArrLength() {
		int k = 0;
		for (ConnectFourMoves moves:pm) {
			if (moves.open) {
				k++;//length incremented
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
	//check upwards 4 units for winner
	public boolean checkUp(int x, int y) {
		//checks up 4 units, if there is a connection upwards, set winner variable to the colour of the connection it's checking
		//also checks boundaries
		//Same exact thing for all of the check methods below this method, don't want to repeat same comment 7 more itmes
		if(x-3>=0&&bo[x][y]==bo[x-1][y]&&bo[x][y]==bo[x-2][y]&&bo[x][y]==bo[x-3][y]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;
	}
	//checks downwards 4 units for winner
	public boolean checkDown(int x, int y) {
		if(x+3<row&&bo[x][y]==bo[x+1][y]&&bo[x][y]==bo[x+2][y]&&bo[x][y]==bo[x+3][y]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;	
	}
	//checks left 4 units for winner
	public boolean checkLeft(int x, int y) {
		if(y-3>=0&&bo[x][y]==bo[x][y-1]&&bo[x][y]==bo[x][y-2]&&bo[x][y]==bo[x][y-3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;
	}
	//checks right 4 units for winner
	public boolean checkRight(int x, int y) {
		if(y+3<col&&bo[x][y]==bo[x][y+1]&&bo[x][y]==bo[x][y+2]&&bo[x][y]==bo[x][y+3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;
	}
	//checks diagonally down left 4 units for winner
	public boolean checkDL(int x, int y) {
		if(x+3<row&&y-3>=0&&bo[x][y]==bo[x+1][y-1]&&bo[x][y]==bo[x+2][y-2]&&bo[x][y]==bo[x+3][y-3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;
	}
	//checks diagonally down right 4 units for winner
	public boolean checkDR(int x, int y) {
		if(x+3<row&&y+3<col&& bo[x][y]==bo[x+1][y+1]&&bo[x][y]==bo[x+2][y+2]&&bo[x][y]==bo[x+3][y+3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;
	}
	//checks diagonally up left 4 units for winner
	public boolean checkUL(int x, int y) {
		if(x-3>=0&&y-3>=0&&bo[x][y]==bo[x-1][y-1]&&bo[x][y]==bo[x-2][y-2]&&bo[x][y]==bo[x-3][y-3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;
	}
	//checks diagonally up right 4 units for winner
	public boolean checkUR(int x, int y) {
		if(x-3>=0&&y+3<col&&bo[x][y]==bo[x-1][y+1]&&bo[x][y]==bo[x-2][y+2]&&bo[x][y]==bo[x-3][y+3]&&bo[x][y]!=0&&bo[x][y]<=2) {
			winner=bo[x][y];
			return true;
		}
		return false;	
	}
	//checks for tie
	public boolean tie() {
		//iterates entire board
		for (int i =0;i<row;i++) {
			for (int j =0;j<row;j++) {
				//if there is a possible move (bo[i][j] is greater than 2) or 0, there are still spots to move
				if (bo[i][j]==0||bo[i][j]>2) {
					return false;
				}
			}
		}
		return true;
	}
	//clears all the place-able spots for next turn
	public void clearPlaceableSpots() {
		for (ConnectFourMoves moves:pm) {//does the clearing by setting open attribute to false
			moves.open=false;
		}
		for (int i =0;i<row;i++) {
			for (int j =0;j<row;j++) {
				if (bo[i][j]>2) {//clears the board from showing possible moves
					bo[i][j]=0;
				}
			}
		}
	}
	//checks if the spot player clicked on is part of the possible moves (pm) array
	public boolean isValid(int x, int y) {
		for (ConnectFourMoves moves:pm) {
			//checks if it's a place-able spot, if so, return true
			if (bo[x][y]>2){
				return true;
			}
		}
		return false;
	}
	//resets the board
	public void resetBo() {
		//sets all board square data to 0
		for (int i =0;i<row;i++) {
			for (int j =0;j<col;j++) {
				bo[i][j]=0;
			}
		}
	}
	//creates image icon
	protected static ImageIcon createImageIcon (String path)
	{
		java.net.URL imgURL = _2048.class.getResource (path);
		if (imgURL != null)
			return new ImageIcon (imgURL);
		else
			return null;
	}
}