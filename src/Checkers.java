import java.awt.*;
import java.text.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
//Name: Eddison Pham
//Date: 1/7/2022
//Purpose: Checkers
public class Checkers extends JPanel implements ActionListener
{
	//Variable starts with b = black, starts with w = white (seconds). Variable ends with 1 = starting time, ends with 2 = end
	int b_sec1,b_sec2,w_sec1,w_sec2;
	//Same as above but in minutes
	int b_min1,b_min2,w_min1,w_min2;
	//black timer
	JProgressBar blackTimer;
	//black HP according to their timer
	int black_hp=100;
	//white timer
	JProgressBar whiteTimer;
	//white HP according to their timer
	int white_hp=100;
	//black DT for timer
	Date b_now,b_end;
	//white DT for timer
	Date w_now,w_end;
	//keep track of possible moves for the selected piece
	int moves[] = new int[4];
	//keeps track of previous X and Y position
	int previous_x;
	int previous_y;
	//checks if is selecting or jumping
	boolean selected = false;
	//used for multi-jumps, check if is currently jumping
	boolean jumping = false;
	//checks if previously moved 1 or 2 square
	int previous = 0;
	//squares horizontal
	int row = 8;
	//squares vertical
	int col = 8;
	//integer grid of checkers, 1 is black, 2 is white, 0 is empty
	int bo[][] = new int[row][col];
	//holds this pictures of the game
	JButton pics[][] = new JButton[row][col];
	//checks who's turn it is, 1 or 2
	int turn = 1;
	//label for the turns, changes depending on who wins/lose or the current player's turn
	JLabel turnLabel;
	//can be "black" or "white", used for setting winner text
	String winner;
	//grid panel for screen
	Panel grid;
	//screens holder
	Panel screens;
	//instructions screen and play screen
	Panel instructions,instructions2,play;
	//card layout
	CardLayout layout = new CardLayout();
	public Checkers (){
		//screen setup
		screens = new Panel();
		screens.setLayout(layout);
		screens.setBackground(Color.white);//background colour
		createGrid();
		placeCheckers();
		instructions();
		instructions2();
		play();
		setLayout(new BorderLayout());
		add("Center",screens);
	}
	//creates the game grid
	public void createGrid() {
		//game grid
		grid = new Panel(new GridLayout(row,col));
		//creates the board
		for (int i =0;i<row;i++) {
			for (int j =0;j<col;j++) {
				pics[i][j]=new JButton();
				pics[i][j].setBorder(null);
				pics[i][j].setBorder(BorderFactory.createEmptyBorder());
				pics[i][j].setPreferredSize(new Dimension(50,50));
				pics[i][j].setActionCommand(i+" "+j);
				pics[i][j].addActionListener(this);
				grid.add(pics[i][j]);
			}
		}
	}
	//instructions screen (screens are allowed to be 30+ lines -Ms Gorski)
	public void instructions() {
		instructions = new Panel();
		//instructions label
		JLabel title = new JLabel("Checkers Instructions");
		title.setFont(new Font(Final.font,Font.BOLD,30));
		title.setPreferredSize(new Dimension(400,60));//y padding
		//tutorial image
		JLabel tutorial = new JLabel(createImageIcon("checkersTutorial.png"));
		//next button
		JButton next = new JButton("Next");
		next.setFont(new Font(Final.font,Font.BOLD,20));
		next.setBackground(Color.white);
		next.setPreferredSize(new Dimension(400,30));
		next.setActionCommand("instructions2");
		next.addActionListener(this);
		//home button
		JButton home = new JButton("Return To Home Screen");
		home.setFont(new Font(Final.font,Font.BOLD,20));
		home.setBackground(Color.white);
		home.setPreferredSize(new Dimension(400,30));
		home.setActionCommand(Final.home);
		home.addActionListener(this);
		//add all the widgets in this given order
		instructions.add(title);
		instructions.add(tutorial);
		instructions.add(next);
		instructions.add(home);
		screens.add("instructions",instructions);
	}
	//second instructions screen (talks about the extra abilities added)
	public void instructions2() {
		instructions2 = new Panel();
		//instructions label 2
		JLabel title = new JLabel("Checkers Instructions");
		title.setFont(new Font(Final.font,Font.BOLD,30));
		title.setPreferredSize(new Dimension(400,60));//y padding
		//second tutorial image (talks about the 3 new abilities)
		JLabel tutorial = new JLabel(createImageIcon("checkersTutorial2.png"));
		//play the game
		JButton play = new JButton("Play");
		play.setFont(new Font(Final.font,Font.BOLD,20));
		play.setBackground(Color.white);
		play.setPreferredSize(new Dimension(400,30));
		play.setActionCommand("play");
		play.addActionListener(this);
		//back to previous instructions page
		JButton back = new JButton("Back");
		back.setFont(new Font(Final.font,Font.BOLD,20));
		back.setBackground(Color.white);
		back.setPreferredSize(new Dimension(400,30));
		back.setActionCommand("instructions");
		back.addActionListener(this);
		//add all the widgets in this given order
		instructions2.add(title);
		instructions2.add(tutorial);
		instructions2.add(play);
		instructions2.add(back);
		screens.add("instructions2",instructions2);
	}
	//game screen
	public void play() {
		play = new Panel();
		//title
		JLabel title = new JLabel("Checkers");
		title.setFont(new Font(Final.font,Font.BOLD,Final.size));
		//create reset button
		JButton instructions = new JButton("Instructions");
		instructions.setFont(new Font(Final.font,Font.BOLD,20));
		instructions.setBackground(Color.white);
		instructions.setActionCommand("instructions");
		instructions.addActionListener(this);
		instructions.setPreferredSize(new Dimension(400,30));
		//resets the game
		JButton reset = new JButton("Reset");
		reset.setFont(new Font(Final.font,Font.BOLD,20));
		reset.setBackground(Color.white);
		reset.setActionCommand("reset");
		reset.addActionListener(this);
		reset.setPreferredSize(new Dimension(400,30));
		//black timer
		blackTimer = new JProgressBar(0,0,100);
		blackTimer.setValue(100);
		blackTimer.setStringPainted(true);
		blackTimer.setForeground(Color.black);
		blackTimer.setBorder(null);
		blackTimer.setPreferredSize(new Dimension(390,20));
		blackTimer.setFont(new Font(Final.font,Font.BOLD,12));
		//white timer
		whiteTimer = new JProgressBar(0,0,100);
		whiteTimer.setValue(100);
		whiteTimer.setStringPainted(true);
		whiteTimer.setForeground(Color.lightGray);
		whiteTimer.setPreferredSize(new Dimension(390,20));
		whiteTimer.setFont(new Font(Final.font,Font.BOLD,12));
		//create turn label
		turnLabel = new JLabel("Black's turn.");
		turnLabel.setFont(new Font(Final.font,Font.BOLD,20));
		//adding all the widgets in this given order
		play.add(title);
		play.add(whiteTimer);
		play.add(grid);
		play.add(blackTimer);
		play.add(turnLabel);
		play.add(instructions);
		play.add(reset);
		screens.add("play",play);
	}
	//initially places the checkers on the grid
	public void placeCheckers() {
		for(int i =0;i<row;i++) {
			for (int j =0;j<col;j++) {
				//uses XOR to create checker pattern when placing checkers
				if (i%2!=0^j%2!=1) {
					bo[i][j]=(i<(row-1)/2)?2:(i>(row+1)/2)?1:0;
				}
			}
		}
		//images
		for (int i =0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				if (bo[i][j]!=-1) {
					pics[i][j].setIcon(createImageIcon("p"+bo[i][j]+".png"));
				}
			}
		}
	}
	//click functionality
	public void actionPerformed (ActionEvent e)
	{
		screenManeuver(e);//change screens
		//reset button action command
		if(e.getActionCommand().equals("reset")) {
			reset();
		//home screen
		}else if (e.getActionCommand().equals(Final.home)) {
			Final.mainScreen();
		}
		clickedOnBoard(e);
	}
	//checks which squares on the board is clicked on
	public void clickedOnBoard(ActionEvent e) {
		for (int i =0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				if (e.getActionCommand().equals(i+" "+j)) {
					//selects piece
					if (bo[i][j]!=0&&bo[i][j]%2==turn%2&&!jumping) {
						resetRed();
						checkSpots(i,j,turn);
						getPreviousMove(i,j);
					//moves piece
					}else if (bo[i][j]==0&&isValid(i,j)&&selected){
						move(i,j);//move piece
						checkKing(i,j,turn);//checks if piece reaches other side
						checkWinner();//check if game over
					}
				}
			}
		}
	}
	//checks winner and game over
	public void checkWinner() {
			if (!jumping) {//when not jumping
				if(TimeOver()!='n') {//I want TimeOver to be checked before gameOver so that player can't win with 0 time
					turnLabel.setText(winner+" wins!");
				}else if (gameOver()) {//checks if game over
					turnLabel.setText(winner+" wins!");
				}else {//otherwise flip the turn label
					turnLabel.setText((turn==1)?"Black's turn.":"White's turn.");
				}
			}
	}
	//changes screens
	public void screenManeuver(ActionEvent e) {
		//instructions
		if(e.getActionCommand().equals("instructions")) {
			layout.show(screens, "instructions");
		//instructions part 2
		}else if (e.getActionCommand().equals("instructions2")){
			layout.show(screens, "instructions2");
		//play game
		}else if (e.getActionCommand().equals("play")) {
			reset();
			layout.show(screens, "play");
		}
	}
	//resets the game
	public void reset() {
		Final.playSound("startGame.wav");//start game sound
		bo=new int[row][col];
		placeCheckers();
		turnLabel.setText("Black's turn.");
		turn = 1;
		white_hp=100;
		black_hp=100;
		whiteTimer.setValue(white_hp);
		blackTimer.setValue(black_hp);
		b_now=new Date();
		b_sec1=b_now.getSeconds();
		b_min1=b_now.getMinutes();
	}
	//checks if game is over
	public boolean gameOver() {
		for (int i =0;i<row;i++) {
			for (int j =0;j<row;j++) {
				if (bo[i][j]%2==turn%2) {
					checkSpots(i,j,turn);
					//checks if moves array has no possible moves
					for (int m=0;m<moves.length;m++) {
						//if a move exists for the current player, continue game
						if (moves[m]!=0) {
							refresh();
							return false;
						}
					}
				}
			}
		}
		refresh();
		Final.playSound("gameOver.wav");//game over sound
		winner = (turn==2)?"Black":"White";
		return true;
	}
	//check if the is over (game over) and returns the first letter of the loser, returns N if none
	public char TimeOver() {
		char timeOver = 'n';
		if(black_hp<=0) {//checks if black's health is empty
			timeOver='b';
			winner="White";
			blackTimer.setValue(0);//if the health is negative, set it to 0
		}else if(white_hp<=0) {//checks if white's health is empty
			timeOver='w';
			winner="Black";
			whiteTimer.setValue(0);//if the health is negative, set it to 0
		}
		return timeOver;
	}
	//prevents pieces being places on an invalid empty space
	public boolean isValid(int i,int j) {
		//checks if the current move is valid according to the previous position
		for (int m =0;m<moves.length;m++) {
			if (m==0&&i==previous_x-moves[m]&&j==previous_y-moves[m]) {//checks upwards left
				return true;
			}
			if (m==1&&i==previous_x-moves[m]&&j==previous_y+moves[m]) {//checks upwards right
				return true;
			}
			if (m==2&&i== previous_x+moves[m]&&j==previous_y-moves[m]) {//checks downwards left
				return true;
			}
			if (m==3&&i==previous_x+moves[m]&&j==previous_y+moves[m]){//checks downwards right
				return true;
			}
		}
		return false;
	}
	//creates king if piece reaches other side
	public void checkKing(int i ,int j, int turn) {
		for (int m=0;m<row;m++) {
			//black king
			if (bo[0][m]==1) {
				bo[0][m]=5;
				refresh();
			//white king
			}else if (bo[col-1][m]==2) {
				bo[col-1][m]=6;
				refresh();
			}
		}
	}
	//gets the previous move
	public void getPreviousMove(int i, int j) {
		previous_x = i;
		previous_y = j;
		selected = true;
		
	}
	//moves the piece
	public void move (int i,int j) {
		int temp = bo[previous_x][previous_y];
		bo[previous_x][previous_y]=0;
		capture(i,j,previous_x,previous_y);
		bo[i][j]=temp;
		refresh();
		//if previous move was not 0 (the only case should be 2), then check for jumpable pieces
		if (previous !=0) {
			jumping = true;
			checkSpots(i,j,turn);
			if (hasMoves()) {//multi jumps
				getPreviousMove(i,j);
			}else {//break out of multi jumps
				turn=(turn==1)?2:1;
				runTimer(turn);
				selected = false;
				jumping = false;
			}
		//otherwise change turns
		}else{
			turn=(turn==1)?2:1;
			runTimer(turn);
			selected = false;	
		}
	}
	//Flips the timer, drains the other person's timer when turn flips
	public void runTimer(int _turn) {
		if (_turn==1) {//if it's black's turn
			w_end=new Date();//stop draining white's timer
			w_sec2=w_end.getSeconds();
			w_min2=w_end.getMinutes();
			int elapsed_sec = (w_min2*60+w_sec2)-(w_min1*60+w_sec1);//get the time transpired
			white_hp-=elapsed_sec;//subtract from white's timer
			whiteTimer.setValue(white_hp);
			b_now=new Date();//start black's timer
			b_sec1= b_now.getSeconds();
			b_min1= b_now.getMinutes();
		}else if (_turn==2){//if it's white's turn
			b_end=new Date();//stop draining black's timer
			b_sec2=b_end.getSeconds();
			b_min2=b_end.getMinutes();
			int elapsed_sec = (b_min2*60+b_sec2)-(b_min1*60+b_sec1);//get the time transpired
			black_hp-=elapsed_sec;//subtract from black's timer
			blackTimer.setValue(black_hp);
			w_now=new Date();//starts white's timer
			w_sec1 = w_now.getSeconds();
			w_min1 = w_now.getMinutes();
		}
	}
	//check if has anymore pieces can jump
	public boolean hasMoves() {
		for (int m =0;m<moves.length;m++) {
			if (moves[m]==2) {
				return true;
			}
		}
		return false;
	}
	//captures using piece
	public void capture(int i, int j, int i2,int j2) {
		//gets the distance between current and before, divide it by 2 to get the piece in between
		int di = (int)(i2-i)/2;
		int dj = (int)(j2-j)/2;
		//abs value to prevent negative distance
		if (Math.abs(di)>=1||Math.abs(dj)>=1) {
			Final.playSound("capturingSound.wav");//plays capturing sound
			bo[i+di][j+dj]=0;
			previous=2;
		}else {
			Final.playSound("moveSound.wav");//plays moving sound
			previous = 0;
		}
	}
	//refreshes the board to place image
	public void refresh() {
		for (int i = 0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				pics[i][j].setIcon(createImageIcon("p"+bo[i][j]+".png"));
			}
		}
	}
	//resets the red marking that shows possible moves
	public void resetRed() {
		for (int i =0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				if (pics[i][j].getBackground().equals(Color.red)) {
					pics[i][j].setBackground(Color.white);
					pics[i][j].setIcon(createImageIcon("p"+bo[i][j]+".png"));
				}
			}
		}
	}
	//check all possible spots, place the possible move inside the moves array
	public void checkSpots(int i,int j,int turn) {
		int opp = (turn==1)?2:1;
		//resets moves array
		for (int m =0;m<moves.length;m++){
			moves[m]=0;
		}
		TL(i,j,turn,opp);
		TR(i,j,turn,opp);
		BL(i,j,turn,opp);
		BR(i,j,turn,opp);
		//marks the valid moves as red
		mark(i,j);
	}
	//does the marking for method above
	public void mark (int i ,int j) {
		for (int m=0;m<moves.length;m++) {
			if (moves[m]!=0) {//if moves[m] is 0, then that means the entire array is empty of moves
				if (m==0) {//mark upwards left
					pics[i-moves[m]][j-moves[m]].setBackground(Color.red);//background colour to red
					pics[i-moves[m]][j-moves[m]].setIcon(null);//deletes the image to see background colour
				}
				if (m==1) {//mark upwards right
					pics[i-moves[m]][j+moves[m]].setBackground(Color.red);
					pics[i-moves[m]][j+moves[m]].setIcon(null);
				}
				if (m==2) {//mark downwards left
					pics[i+moves[m]][j-moves[m]].setBackground(Color.red);
					pics[i+moves[m]][j-moves[m]].setIcon(null);
				}
				if (m==3){//mark downwards right
					pics[i+moves[m]][j+moves[m]].setBackground(Color.red);
					pics[i+moves[m]][j+moves[m]].setIcon(null);
				}
			}
			
		}
	}
	//check top left (next 3 methods are the exact same functionality but with different positions)
	public void TL(int i, int j,int turn,int opp) {
		//the first bracket of boolean checks whether if the piece can move up or down according to the colour
		//the next checks if its in the board's range
		//the last boolean checks if the position is not equal to the same piece you are currently selecting
		if ((bo[i][j]==1||bo[i][j]>2)&&i-1>=0&&j-1>=0&&bo[i-1][j-1]!=turn) { 
			//modulo because all odd numbered pieces are black and all even are white
			if (bo[i-1][j-1]!=0&&bo[i-1][j-1]%2==opp%2) {
				//if so, check if space after that is open for piece capture
				if (i-2>=0&&j-2>=0&&bo[i-2][j-2]==0) {
					moves[0]=2;
				}
			//otherwise if we are currently not jumping and there is open space, mark as red
			//!jumping prevents red mark showing for an non-capturing move after jumping an enemy piece
			}else if (!jumping&&bo[i-1][j-1]==0){
				moves[0]=1;
			}
		}else {
			moves[0]=0;
		}
	}
	//check top right
	public void TR(int i, int j,int turn,int opp) {
		if ((bo[i][j]>2||bo[i][j]==1)&&i-1>=0&&j+1<col&&bo[i-1][j+1]!=turn) {
			if (bo[i-1][j+1]!=0&&bo[i-1][j+1]%2==opp%2) {
				if (i-2>=0&&j+2<col&&bo[i-2][j+2]==0) {
					moves[1]=2;
				}
			}else if (!jumping&&bo[i-1][j+1]==0){
				moves[1]=1;
			}	
		}else {
			moves[1]=0;
		}
	}
	//check bottom left
	public void BL(int i, int j,int turn, int opp) {
		if ((bo[i][j]==2||bo[i][j]>2)&&i+1<col&&j-1>=0&&bo[i+1][j-1]!=turn) {
			if (bo[i+1][j-1]!=0&&bo[i+1][j-1]%2==opp%2) {
				if (i+2<col&&j-2>=0&&bo[i+2][j-2]==0) {
					moves[2]=2;
				}
			}else if (!jumping&&bo[i+1][j-1]==0){
				moves[2]=1;
			}
			
		}else {
			moves[2]=0;
		}
	}
	//checks bottom right
	public void BR(int i, int j,int turn,int opp) {
		if ((bo[i][j]>2||bo[i][j]==2)&&i+1<row&&j+1<col&&bo[i+1][j+1]!=turn) {
			if (bo[i+1][j+1]!=0&&bo[i+1][j+1]%2==opp%2) {
				if (i+2<row&&j+2<col&&bo[i+2][j+2]==0) {
					moves[3]=2;
				}
			}else if (!jumping&&bo[i+1][j+1]==0){
				moves[3]=1;
			}
		}else {
			moves[3]=0;
		}
	}
	//creates image icon
	protected static ImageIcon createImageIcon (String path)
	{
		java.net.URL imgURL = Checkers.class.getResource (path);
		if (imgURL != null)
			return new ImageIcon (imgURL);
		else
			return null;
	}
}
