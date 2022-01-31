import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.*;
import javax.swing.border.*;
public class _2048 extends JPanel implements ActionListener{
	//board dimensions
	int row=6;
	int col=6;
	//pictures
	JButton pics[][]= new JButton[row][col];
	//board data
	int bo[][] = new int[row][col];
	//hold the screens
	Panel screens;
	//screens (instructions, ability instructions and playing screen)
	Panel instructions,instructions2, play;
	//screen layout
	CardLayout layout = new CardLayout();
	//score label
	JLabel scoreLabel;
	//high score integer value
	int hs;
	//high score label
	JLabel highscore = new JLabel();
	//current score 
	int score = getScore();
	//global game grid panel
	Panel grid;
	//the 3 global variables below are used for the Shifter ability to swap places
	//checks if currently selecting Shifter ability
	boolean selecting = false;
	//gets the current position of the Shifter
	int selectionX;
	int selectionY;
	public _2048 (){
		screens = new Panel();
		screens.setLayout(layout);
		screens.setBackground(Color.white);//background colour
		createGrid();
		bo[(int)(Math.random()*row)][(int)(Math.random()*col)]=2;
		instructions();
		instructions2();
		play();
		setLayout(new BorderLayout());
		add("Center",screens);
		refresh();
	}
	//creates the game grid
	public void createGrid() {
		//grid to hold everything
		grid = new Panel(new GridLayout(row,col));
		//create board
		for (int i =0;i<row;i++) {
			for(int j =0;j<col;j++) {
				pics[i][j] = new JButton();
				pics[i][j].setBackground(Color.white);
				pics[i][j].setIcon(createImageIcon("f"+bo[i][j]+".png"));
				pics[i][j].setBorder(null);
				pics[i][j].setPreferredSize(new Dimension(67,67));
				pics[i][j].setFont(new Font(Final.font,Font.BOLD,15));
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
		JLabel title = new JLabel("2048 Instructions");
		title.setFont(new Font(Final.font,Font.BOLD,35));
		title.setPreferredSize(new Dimension(400,70));//y padding
		//tutorial image
		JLabel tutorial=new JLabel(createImageIcon("_2048tutorial.png"));
		//next (abilities instructions)
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
		//add all widgets onto screen
		instructions.add(title);
		instructions.add(tutorial);
		instructions.add(next);
		instructions.add(home);
		screens.add("instructions",instructions);
	}
	//instructions screen with abilities
	public void instructions2() {
		instructions2 = new Panel();
		//instructions label 2
		JLabel title = new JLabel("2048 Instructions");	
		title.setFont(new Font(Final.font,Font.BOLD,35));
		title.setPreferredSize(new Dimension(400,70));//y padding
		//second tutorial image
		JLabel tutorial=new JLabel(createImageIcon("_2048tutorial2.png"));
		//play button
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
		instructions2.add(title);
		instructions2.add(tutorial);
		instructions2.add(play);
		instructions2.add(back);
		screens.add("instructions2",instructions2);
	}
	//playing screen
	public void play() {
		play = new Panel();
		//title
		JLabel title = new JLabel("2048");
		title.setFont(new Font(Final.font,Font.BOLD,Final.size));
		//score label
		scoreLabel = new JLabel("Score: "+score);
		scoreLabel.setFont(new Font(Final.font,Font.BOLD,15));
		scoreLabel.setPreferredSize(new Dimension(400,20));
		//high score label
		openHighScoreFile();
		highscore.setFont(new Font(Final.font,Font.BOLD,15));
		highscore.setPreferredSize(new Dimension(400,20));
		//add score and highscore label to score format grid
		//controls buttons grid (up, down, left, right)
		Panel OuterGrid = new Panel(new GridLayout(2,1));
		//up button
		JButton u = new JButton("Up");
		u.setActionCommand("u");
		u.addActionListener(this);
		u.setBackground(Color.white);
		u.setFont(new Font(Final.font,Font.BOLD,20));
		//separately added to the outer grid because I want this to be at the top
		OuterGrid.add(u);
		Panel InnerGrid2 = new Panel(new GridLayout(1,3));
		//down button
		JButton d = new JButton("Down");
		d.setActionCommand("d");
		d.addActionListener(this);
		d.setBackground(Color.white);
		d.setFont(new Font(Final.font,Font.BOLD,20));
		//left button
		JButton l = new JButton("Left");
		l.setActionCommand("l");
		l.addActionListener(this);
		l.setBackground(Color.white);
		l.setFont(new Font(Final.font,Font.BOLD,20));
		//right button
		JButton r = new JButton("Right");
		r.setActionCommand("r");
		r.addActionListener(this);
		r.setBackground(Color.white);
		r.setFont(new Font(Final.font,Font.BOLD,20));
		//add the controls (d,l,r) to a sub-grid
		InnerGrid2.add(l);
		InnerGrid2.add(d);
		InnerGrid2.add(r);
		OuterGrid.add(InnerGrid2);
		//reset the game
		JButton reset = new JButton("Reset");
		reset.setFont(new Font(Final.font,Font.BOLD,20));
		reset.setBackground(Color.white);
		reset.setPreferredSize(new Dimension(400,30));
		reset.setActionCommand("reset");
		reset.addActionListener(this);
		//go back to instructions screen
		JButton Instructions = new JButton("Instructions");
		Instructions.setFont(new Font(Final.font,Font.BOLD,20));
		Instructions.setBackground(Color.white);
		Instructions.setPreferredSize(new Dimension(400,30));
		Instructions.setActionCommand("instructions");
		Instructions.addActionListener(this);
		play.add(title);
		play.add(scoreLabel);
		play.add(highscore);
		play.add(grid);
		play.add(OuterGrid);
		play.add(Instructions);
		play.add(reset);
		screens.add("play",play);
	}
	//gets the current score of the board 
	public int getScore() {
		int s=0;
		for (int i =0;i<row;i++) {
			for (int j =0;j<row;j++) {
				//used an if statement because I used negative numbers to represent abilities (which don't count to score)
				if (bo[i][j]>0) {
					s+=bo[i][j];	
				}
			}
		}
		return s;
	}
	//updates the board
	public void refresh() {
		for (int i = 0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				if (bo[i][j]<=0) {//checks if it's an ability or empty space
					pics[i][j].setText("");
					pics[i][j].setIcon(createImageIcon("f"+Math.abs(bo[i][j])+".png"));//ability image
				}else {//otherwise set it to have button text
					pics[i][j].setIcon(null);
					pics[i][j].setText(""+bo[i][j]);
				}
				//RGB colours to increase green intensity as the number grows exponentially bigger
				int r=(int)(Math.abs(255-(log(2,bo[i][j]+1,0))*20));
				int g=(int)(255-(log(2,bo[i][j]+1,0)));
				int b=r;
				pics[i][j].setBackground(new Color(r,g,b));
			}
		}
		score=getScore();
		if(gameOver()) {
			scoreLabel.setText("Game Over! Final Score: "+score);
		}else {
			scoreLabel.setText("Score: "+score);
		}
	}
	//recursive base 2 log function 
	public int log(int base, int x, int depth) {
		if(x<=1) {
			return depth;
		}
		depth=log(base,x/base,depth+1);
		return depth;
	}
	//click functionality
	public void actionPerformed (ActionEvent e){
		screensManeuver(e);
		keyPad(e);
		//reset button
		if (e.getActionCommand().equals("reset")) {
			selecting=false;
			reset();
		}else if (e.getActionCommand().equals(Final.home)) {
			Final.mainScreen();
		}
		clickedOnAbilities(e);
		ShifterBorderChanger();
		refresh();
	}
	//checks if player has clicked on an ability or a square on the board
	public void clickedOnAbilities(ActionEvent e) {
		//clicked on squares
		for (int i =0;i<row;i++) {
			for (int j =0;j<col;j++) {
				if (e.getActionCommand().equals(i+" "+j)) {
					specialAbility(i,j);//checks ability player clicked on
				}
			}
		}
	}
	public void keyPad(ActionEvent e) {
		//Up, Down, Left, Right keypad functionality
		if (!selecting) {
			if(e.getActionCommand().equals("u")) {//up button
				moveUp();
				Final.playSound("moveSound.wav");
			}else if(e.getActionCommand().equals("d")) {//down button
				moveDown();
				Final.playSound("moveSound.wav");
			}else if(e.getActionCommand().equals("l")) {//left button
				moveLeft();
				Final.playSound("moveSound.wav");
			}else if(e.getActionCommand().equals("r")) {//right button
				moveRight();
				Final.playSound("moveSound.wav");
			} 
		}
	}
	//Maneuvering around screens, ran out of space in action performed
	public void screensManeuver(ActionEvent e) {
		if(e.getActionCommand().equals("instructions")) {//instructions screen
			layout.show(screens, "instructions");
		}else if (e.getActionCommand().equals("instructions2")){//second instructions screen
			layout.show(screens, "instructions2");
		}else if (e.getActionCommand().equals("play")) {//starts game
			Final.playSound("startGame.wav");
			layout.show(screens, "play");
		}
	}
	//changes the border of the selected Shifter ability
	public void ShifterBorderChanger(){
		//if selecting a Shifter, set the boarder to be black, otherwise none
		if (selecting) {
			pics[selectionX][selectionY].setBorder(BorderFactory.createLineBorder(Color.black,2,true));
		}else {
			pics[selectionX][selectionY].setBorder(null);
		}
	}
	
	//when player clicks on special ability, this method does the abilities
	public void specialAbility(int i, int j) {
		//checks if player has selected on the Shifter ability. Player cannot do any other moves until unselected (second if statement)
		if (selecting&&inRange(i,j)&&bo[i][j]!=0&&bo[i][j]!=-3) {//swaps selected square position with shifter's position
			int temp = bo[i][j];
			bo[i][j]=0;
			bo[selectionX][selectionY]=temp;
			selecting = false;
			Final.playSound("capturingSound.wav");
		}else if (selecting&&selectionX==i&&selectionY==j) {//unselect the shifter
			selecting = false;
			Final.playSound("moveSound.wav");
		}else if (!selecting&&bo[i][j]<=-1) {//otherwise player has clicked on a special ability
			abilityPrompt(i,j);
		}
	}
	//prompts ability
	public void abilityPrompt(int i,int j) {
		if (!selecting&&bo[i][j]==-1) {//black lightning
			bo[i][j]=0;
			lowestSurrounding(i,j);
			//for loop to check for all of the targets and apply the same function as white lightning to them
			for (int n=0;n<row;n++) {
				for (int m=0;m<col;m++) {
					if (bo[n][m]==-3) {
						bo[n][m]=0;
						lowestSurrounding(n,m);
					}
				}
			}
			Final.playSound("lightningZap.wav");
		}else if (!selecting&&bo[i][j]==-2){//white lightning
			bo[i][j]=0;
			lowestSurrounding(i,j);
			Final.playSound("lightningZap.wav");
		}else if (!selecting&&bo[i][j]==-3) {//Shifter
			selectionX=i;
			selectionY=j;
			selecting=true;
			Final.playSound("moveSound.wav");
		}
	}
	//checks the lowest value around the White/Black Lightning
	public void lowestSurrounding(int x, int y) {
		//positive infinite
		int min=Integer.MAX_VALUE;
		boolean foundMin = false;
		for (int i =x-1;i<=x+1;i++) {
			for (int j =y-1;j<=y+1;j++) {
				//if the current position is in range of the board, is greater than 0 (not ability), and is less than min, set it to be the min
				if (i>=0&&i<row&&j>=0&&j<col&&bo[i][j]>0&&min>bo[i][j]) {
					min=bo[i][j];
					foundMin=true;
				}
			}
		}
		//if there is a min (which means not all of the squares surrounding it are abilities), call the cell multiplier
		if (foundMin) {
			multiplyCells(x,y, min);
		}
	}
	//checks if the position is above, below, to the left or to the right of the selected shifter.
	public boolean inRange(int x, int y) {
		//this type of math restricts it to only being able to check up, down, left or right
		if (Math.abs(selectionX-x)+Math.abs(selectionY-y)==1) {
			return true;
		}
		return false;
	}
	//Checks around the current cell and multiply all the lowest numbers by 2. 
	//Then recursively checks each one of those numbers for any more similar numbers that are touching it .
	public void multiplyCells(int x, int y, int lowest) {
		bo[x][y]*=2;
		for (int i =x-1;i<=x+1;i++) {
			for (int j =y-1;j<=y+1;j++) {
				if (i>=0&&i<row&&j>=0&&j<col&&bo[i][j]==lowest) {
					//recursion
					multiplyCells(i,j,lowest);
				}
			}
		}
	}
	//resets the game
	public void reset() {
		for (int i = 0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				bo[i][j]=0;//clears board
			}
		}
		Final.playSound("startGame.wav");
		bo[(int)(Math.random()*row)][(int)(Math.random()*col)]=2;//places number 2 at a random position
		//resets high score label back to normal (in case if it was changed to "New High Score")
		highscore.setText("High Score: "+hs);
	}
	//moves everything to the left
	public void moveLeft() {
		for (int i =0;i<row;i++) {
			for (int j =0;j<col;j++) {
				moveSquareLeft(i,j);//moves the individual squares to the left
			}
		}
		if(checkL()) {//checks if there is an open space on the side opposite to the left (right)
			int rand = (int)(Math.random()*col);
			while(bo[rand][col-1]!=0) {//while loop to find open square
				rand = (int)(Math.random()*col);
			}
			bo[rand][col-1]=randomSquareSpawn();//places a random value (2 or 4) or an ability on the open square
		}
	}
	//separate method from method above because >30 lines, individually moves the pieces left
	public void moveSquareLeft(int i, int j) {
		if(bo[i][j]!=0) {//if the current square is not a 0, don't want it to move empty spaces around and cause errors
			int n = 1;
			while(j-n>=0) {
				if(bo[i][j-n]==bo[i][j]&&bo[i][j]>0) {//if the leading number is the same as the moving square, add them
					bo[i][j-n]+=bo[i][j];
					bo[i][j]=0;
					break;
				}else if(bo[i][j-n]!=0) {// if the leading number is not equal to 0 and not the same as the current, stay one block behind
					if(n==1)break;
					bo[i][j-n+1]=bo[i][j];
					bo[i][j]=0;
					break;
				}else if(j-n==0) {//otherwise if it's on the left-most edge, stay there
					bo[i][j-n]=bo[i][j];
					bo[i][j]=0;
				}
				n++;
			}
		}
	}
	//moves all the squares to the right 
	public void moveRight() {
		for (int i =0;i<row;i++) {
			for (int j =col-1;j>=0;j--) {
				moveSquareRight(i,j);//moves the individual squares to the right
			}
		}
		if(checkR()) {//checks if there is an open space on the side opposite to the right (left)
			int rand = (int)(Math.random()*col);
			while(bo[rand][0]!=0) {//while loop to find open square
				rand = (int)(Math.random()*col);
			}
			bo[rand][0]=randomSquareSpawn();//places a random value (2 or 4) or an ability on the open square
		}
	}
	//separate method from method above because >30 lines, individually moves the square to the right
	public void moveSquareRight(int i ,int j) {
		if(bo[i][j]!=0) {//if the current square is not a 0, don't want it to move empty spaces around and cause errors
			int n = 1;
			while(j+n<col) {
				if(bo[i][j+n]==bo[i][j]&&bo[i][j]>0) {//if the leading number is the same as the moving square, add them
					bo[i][j+n]+=bo[i][j];
					bo[i][j]=0;
					break;
				}else if(bo[i][j+n]!=0) {// if the leading number is not equal to 0 and not the same as the current, stay one block behind
					if(n==1)break;
					bo[i][j+n-1]=bo[i][j];
					bo[i][j]=0;
					break;
				}else if(j+n==col-1) {//otherwise if it's on the right-most edge, stay there
					bo[i][j+n]=bo[i][j];
					bo[i][j]=0;
				}
				n++;
			}
		}
	}
	//moves all squares upwards (literally the same functionality as moveLeft and moveRight but upwards)
	public void moveUp() {
		for (int i =0;i<row;i++) {
			for (int j =0;j<col;j++) {
				moveSquareUp(i,j);//moves the individual squares upwards
			}
		}
		if(checkU()) {//checks if there is an open space on the side opposite to up (down)
			int rand = (int)(Math.random()*row);
			while(bo[row-1][rand]!=0) {//while loop to find open square
				rand = (int)(Math.random()*row);
			}
			bo[row-1][rand]=randomSquareSpawn();//places a random value (2 or 4) or an ability on the open square
		}
	}
	//separate method from method above because >30 lines, individually moves the square to the upwards
	public void moveSquareUp(int i ,int j) {
		if(bo[i][j]!=0) {//if the current square is not a 0, don't want it to move empty spaces around and cause errors
			int n = 1;
			while(i-n>=0) {
				if(bo[i-n][j]==bo[i][j]&&bo[i][j]>0) {//if the leading number is the same as the moving square, add them
					bo[i-n][j]+=bo[i][j];
					bo[i][j]=0;
					break;
				}else if(bo[i-n][j]!=0) {// if the leading number is not equal to 0 and not the same as the current, stay one block behind
					if(n==1)break;
					bo[i-n+1][j]=bo[i][j];
					bo[i][j]=0;
					break;
				}else if(i-n==0) {//otherwise if it's on the right-most edge, stay there
					bo[i-n][j]=bo[i][j];
					bo[i][j]=0;
				}
				n++;
			}
		}
	}
	//moves all squares downwards 
	public void moveDown() {
		for (int i =row-1;i>=0;i--) {
			for (int j =0;j<col;j++) {
				moveSquareDown(i,j);//moves the individual squares downwards
			}
		}
		if(checkD()) {//checks if there is an open space on the side opposite to down (up)
			int rand = (int)(Math.random()*row);
			while(bo[0][rand]!=0) {//while loop to find open square
				rand = (int)(Math.random()*row);
			}
			bo[0][rand]=randomSquareSpawn();//places a random value (2 or 4) or an ability on the open square
		}
	}
	//separate method from method above because >30 lines, individually moves the square to the downwards
	public void moveSquareDown(int i,int j) {
		if(bo[i][j]!=0) {//if the current square is not a 0, don't want it to move empty spaces around and cause errors
			int n = 1;
			while(i+n<row) {
				 if(bo[i+n][j]==bo[i][j]&&bo[i][j]>0) {//if the leading number is the same as the moving square, add them
					bo[i+n][j]+=bo[i][j];
					bo[i][j]=0;
					break;
				}else if(bo[i+n][j]!=0) {// if the leading number is not equal to 0 and not the same as the current, stay one block behind
					if(n==1)break;
					bo[i+n-1][j]=bo[i][j];
					bo[i][j]=0;
					break;
				}else if(i+n==row-1) {//otherwise if it's on the right-most edge, stay there
					bo[i+n][j]=bo[i][j];
					bo[i][j]=0;
				}
				n++;
			}
		}
	}
	//spawns in a random number (2 or 4) or one of the 3 random abilities
	public int randomSquareSpawn() {
		int rand = (int)(Math.random()*301);
		int twoOrFour=(int)(Math.random()*10);
		//black lightning
		if (rand<=2) {
			return -1;
		//white lightning
		}else if (rand<=10) {
			return -2;
		//shifter
		}else if(rand<=30) {
			return -3;
		}
		//2 or 4
		return (twoOrFour<8)?2:4;
	}
	//check the side opposite to left for open space
	public boolean checkL() {
		for (int i =0;i<row;i++) {
			if (bo[i][col-1]==0) {
				return true;
			}
		}
		return false;
	}
	//check the side opposite to right for open space
	public boolean checkR() {
		for (int i =0;i<row;i++) {
			if (bo[i][0]==0) {
				return true;
			}
		}
		return false;
	}
	//check the side opposite to up for open space
	public boolean checkU() {
		for (int i =0;i<row;i++) {
			if (bo[row-1][i]==0) {
				return true;
			}
		}
		return false;
	}
	//check the side opposite to down for open space
	public boolean checkD() {
		for (int i =0;i<row;i++) {
			if (bo[0][i]==0) {
				return true;
			}
		}
		return false;
	}
	//check if game is over
	public boolean gameOver() {
		//checks whole board
		for (int i =0;i<row;i++) {
			for (int j=0;j<col;j++) {
				//if an ability exists, game is not over
				if (bo[i][j]<=0) {
					return false;
				}
				//all if statements below check if the squares around it (up down left right) are still merge-able
				if (i-1>=0&&bo[i-1][j]==bo[i][j]) {//up
					return false;
				}
				if (i+1<row&&bo[i+1][j]==bo[i][j]) {//down
					return false;
				}
				if (j-1>=0&&bo[i][j-1]==bo[i][j]) {//left
					return false;
				}
				if (j+1<col&&bo[i][j+1]==bo[i][j]) {//right
					return false;
				}
			}
		}
		Final.playSound("gameOver.wav");
		setHighScore();//save high score
		return true;
	}
	//changes the high score label and saves current new high score into file
	public void setHighScore() {
		if(score>hs) {//if score is higher than high score when game ends
			saveHighScoreFile();//save the high score
			openHighScoreFile();//set the high score to the new high score
		}
	}
	//opens the high score file
	public void openHighScoreFile(){
		try {
			BufferedReader br = new BufferedReader(new FileReader("_2048HighScoreFile"));//file opener
			hs = Integer.parseInt(br.readLine());//reads the first line of the file
			br.close();//close reader
		}
		catch (Exception e) {
		}
		highscore.setText("New High Score: "+hs);//sets high score
	}
	//save the high score into a file 
	public void saveHighScoreFile() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("_2048HighScoreFile"));//file saver
			bw.write(score+"");//overwrites the first line to the new high score
			bw.close();//closes file writer
		}
		catch(Exception e) {
			
		}
	}
	//create images/icons
	protected static ImageIcon createImageIcon (String path)
	{
		java.net.URL imgURL = _2048.class.getResource (path);
		if (imgURL != null)
			return new ImageIcon (imgURL);
		else
			return null;
	}
}

