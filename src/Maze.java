import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Stack;
//Name: Eddison Pham
//Date: 1/7/2022
//Purpose: Maze Builder
public class Maze extends JPanel implements ActionListener{
	//the # of rows and columns on the board
	int row = 11;
	int col = 11;
	//board array to hold data for board setup
	int bo[][] = new int[row][col];
	//pictures
	JButton pics[][] = new JButton[row][col];
	//board array to hold data for playing board
	int bo2[][]=new int[row][col];
	//hold pictures when player is playing the maze 
	JButton pics2[][] = new JButton[row][col];
	//starts at key mode so that the first thing player runs into is a key and not a gate
	//key opens gate
	char GateKeyMode = 'k';
	//checks if is currently traversing, prevents the target spots (6) from spawning all over the grid and only in deadends
	boolean traversing = false;
	//current X and Y value for DFS
	int curr_x;
	int curr_y;
	//initial and current end point location
	int end_x=row-1;
	int end_y=col-1;
	//initial and current starting point location
	int start_x=0;
	int start_y=0;
	//mode to change block placement mode
	int mode=1;
	//stacks used for backtracking in DFS
	Stack <String> x_stack = new Stack<String>();
	Stack <String> y_stack = new Stack<String>();
	//mode button initialized
	JButton modeButton;
	//screens
	Panel screens;
	//screen 1-2 (instructions), screen 3 (setup), screen 4 (end)
	Panel instructions,instructions2, setup, play;
	CardLayout layout = new CardLayout();
	//setup board grid
	Panel grid;
	//playing board grid
	Panel playingBo;
	JButton endButton;
	//number of keys attained when playing
	int keys=0;
	//number of targets left in maze
	int targets=0;
	//total targets
	int total_targets=0;
	//current position of player when they are playing the maze
	int move_x=curr_x;
	int move_y=curr_y;
	//initialized key label
	JLabel keysLabel;
	//initialized target label
	JLabel targetsLabel;
	//initialize target progress bar
	JProgressBar progress;
	//constructor, sets up everything
	public Maze (){
		screens = new Panel();
		screens.setLayout(layout);
		screens.setBackground(Color.white);//background colour
		createGrid();
		//initially places the start point
		bo[start_x][start_y]=1;
		//initially places the end point
		bo[end_x][end_y]=2;
		instructions();
		instructions2();
		setup();
		end();
		setLayout(new BorderLayout());
		add("Center",screens);
	}
	//creates the game grid
	public void createGrid() {
		//game grid panel to hold the squares
		grid = new Panel(new GridLayout(row,col));
		//loop to create the initial board
		for (int i =0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				pics[i][j] = new JButton();
				pics[i][j].setBackground(Color.white);
				pics[i][j].setIcon(createImageIcon("e"+bo[i][j]+".png"));
				pics[i][j].setBorder(null);
				pics[i][j].setPreferredSize(new Dimension(36,36));
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
		JLabel title = new JLabel("Maze Builder Instructions");
		title.setFont(new Font(Final.font,Font.BOLD,25));
		title.setPreferredSize(new Dimension(400,60));//y padding
		//image for the instructions
		JLabel instructionsImage=new JLabel(createImageIcon("mazeTutorial.png"));
		//transition to instructions part 2
		JButton next = new JButton ("Next");
		next.addActionListener(this);
		next.setActionCommand("instructions2");
		next.setPreferredSize(new Dimension(400,30));
		next.setFont(new Font(Final.font,Font.BOLD,20));
		next.setBackground(Color.white);
		//home button
		JButton home = new JButton("Return To Home Screen");
		home.setFont(new Font(Final.font,Font.BOLD,20));
		home.setBackground(Color.white);
		home.setPreferredSize(new Dimension(400,30));
		home.setActionCommand(Final.home);
		home.addActionListener(this);
		//add every widget to the instructions screen
		instructions.add(title);
		instructions.add(instructionsImage);
		instructions.add(next);
		instructions.add(home);
		screens.add("instructions",instructions);
	}
	//instructions screen part 2 
	public void instructions2() {
		instructions2 = new Panel();
		//instructions label
		JLabel title = new JLabel("Maze Builder Instructions");
		title.setFont(new Font(Final.font,Font.BOLD,25));
		title.setPreferredSize(new Dimension(400,60));//y padding
		//image for the instructions
		JLabel instructionsImage=new JLabel(createImageIcon("mazeTutorial2.png"));
		//transition to instructions part 2
		JButton setup = new JButton ("Setup");
		setup.addActionListener(this);
		setup.setActionCommand("end");
		setup.setPreferredSize(new Dimension(400,30));
		setup.setFont(new Font(Final.font,Font.BOLD,20));
		setup.setBackground(Color.white);
		//back to instructions part 1
		JButton back = new JButton ("Back");
		back.addActionListener(this);
		back.setActionCommand("instructions");
		back.setPreferredSize(new Dimension(400,30));
		back.setFont(new Font(Final.font,Font.BOLD,20));
		back.setBackground(Color.white);
		//add every widget to the instructions screen
		instructions2.add(title);
		instructions2.add(instructionsImage);
		instructions2.add(setup);
		instructions2.add(back);
		screens.add("instructions2",instructions2);
	}
	//board setup screen
	public void setup() {
		//create mode button
		setup = new Panel();
		//title of the game
		JLabel title = new JLabel("Maze Builder");
		title.setFont(new Font(Final.font,Font.BOLD,Final.size));
		//set up the mode changer button
		modeButton = new JButton("Mode: Start");
		modeButton.addActionListener(this);
		modeButton.setActionCommand("mode");
		modeButton.setPreferredSize(new Dimension(400,30));
		modeButton.setFont(new Font(Final.font,Font.BOLD,20));
		modeButton.setBackground(Color.white);
		//create maze generator button
		JButton mazeGen = new JButton("Generate Maze");
		mazeGen.addActionListener(this);
		mazeGen.setActionCommand("generate");
		mazeGen.setPreferredSize(new Dimension(400,30));
		mazeGen.setFont(new Font(Final.font,Font.BOLD,20));
		mazeGen.setBackground(Color.white);
		//create clear maze button
		JButton clearMaze = new JButton("Clear Maze");
		clearMaze.addActionListener(this);
		clearMaze.setActionCommand("clear");
		clearMaze.setPreferredSize(new Dimension(400,30));
		clearMaze.setFont(new Font(Final.font,Font.BOLD,20));
		clearMaze.setBackground(Color.white);
		//play button 
		JButton playButton = new JButton ("Play Level");
		playButton.addActionListener(this);
		playButton.setActionCommand("play");
		playButton.setPreferredSize(new Dimension(400,30));
		playButton.setFont(new Font(Final.font,Font.BOLD,20));
		playButton.setBackground(Color.white);
		//go back to instructions
		JButton instructions = new JButton ("Instructions");
		instructions.addActionListener(this);
		instructions.setActionCommand("instructions");
		instructions.setPreferredSize(new Dimension(400,30));
		instructions.setFont(new Font(Final.font,Font.BOLD,20));
		instructions.setBackground(Color.white);
		//add every widget to the setup screen
		setup.add(title);
		setup.add(grid);
		setup.add(modeButton);
		setup.add(clearMaze);
		setup.add(mazeGen);
		setup.add(playButton);
		setup.add(instructions);
		screens.add("end",setup);
		refresh();
	}
	//playing screen
	public void end() {
		//create mode button
		play = new Panel();
		//title
		JLabel title = new JLabel("Maze Builder");
		title.setFont(new Font(Final.font,Font.BOLD,Final.size));
		playingBo = new Panel(new GridLayout(row,col));
		//loop to create the initial board
		for (int i =0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				pics2[i][j] = new JButton();
				pics2[i][j].setBackground(Color.white);
				pics2[i][j].setIcon(createImageIcon("e"+bo2[i][j]+".png"));
				pics2[i][j].setBorder(null);
				pics2[i][j].setPreferredSize(new Dimension(36,36));
				pics2[i][j].setActionCommand(i+" "+j+"p");
				pics2[i][j].addActionListener(this);
				playingBo.add(pics2[i][j]);
			}
		}
		//layout for key and target label
		Panel labels_panel = new Panel(new GridLayout(2,1)); 
		//keys label (how many keys player has)
		keysLabel=new JLabel("Keys: "+keys);
		keysLabel.setFont(new Font(Final.font,Font.BOLD,20));
		labels_panel.add(keysLabel);
		//how many targets left
		targetsLabel=new JLabel("Targets Captured: ");
		targetsLabel.setFont(new Font(Final.font,Font.BOLD,20));
		labels_panel.add(targetsLabel);
		//progress bar keeping track of how many targets left
		progress = new JProgressBar(0,0,100);
		progress.setValue(0);
		progress.setStringPainted(true);
		progress.setBackground(Color.white);
		progress.setBorder(null);
		progress.setForeground(Color.black);
		progress.setFont(new Font(Final.font,Font.BOLD,12));
		//stop playing
		endButton = new JButton ("End");
		endButton.addActionListener(this);
		endButton.setActionCommand("end");
		endButton.setPreferredSize(new Dimension(400,30));
		endButton.setFont(new Font(Final.font,Font.BOLD,20));
		endButton.setBackground(Color.white);
		//add every widget to the playing screen
		play.add(title);
		play.add(playingBo);
		play.add(labels_panel);
		play.add(progress);
		play.add(endButton);
		screens.add("play",play);
		refresh();
	}
	//percentage calculator for target, returns 100 if no targets
	public int targetPercCalculator() {
		return (total_targets==0)?100:(100-(int)(targets*100/total_targets));
	}
	//used for testing
	public void print() {
		System.out.println();
		for (int i =0;i<row;i++) {
			for (int j = 0 ;j<col;j++) {
				System.out.print(bo2[i][j]);
			}
			System.out.println();
		}
	}
	//creates the maze
	public void generateMaze() {
		wallifyMaze();//turns whole maze into walls for DFS to work
		//DFS starts at the starting node
		curr_x=start_x;
		curr_y=start_y;
		//add first elements to stack
		x_stack.push(""+curr_x);
		y_stack.push(""+curr_y);
		//start maze generation
		bo[start_x][start_y]=0;
		DFS();
		//places back both the end and start nodes into the board when it was previously removed
		bo[end_x][end_y]=2;
		bo[start_x][start_y]=1;
		//special case I found while debugging where white node has no possible path towards it when black's x and y are both even and white is in a corner
		specialCaseEndNode(end_x,end_y);
		//updates screen
		refresh();
	}
	//makes the whole board into walls for DFS
	public void wallifyMaze() {
		clearMaze();//clears current maze from obstacles (not start or end point)
		for (int i =0 ;i<row;i++) {
			for (int j =0 ;j<col;j++) {
				if(bo[i][j]==0) {//if current position is empty, turn it into a wall
					bo[i][j]=3;
				}
				
			}
		}
	}
	//checks for special case where white has no possible path towards it
	public void specialCaseEndNode(int x, int y) {
		int temp_x=x;
		int temp_y=y;
		if (noSolutions(x,y)) {
			//used a do while loop because i want to set the value before checking
			do {
				//random number to choose N,E,S or W to open
				int rand = (int)(Math.random()*4);
				temp_x=x;
				temp_y=y;
				switch(rand) {
				case 0:temp_x=x-1;
				break;
				case 1:temp_x=x+1;
				break;
				case 2:temp_y=y-1;
				break;
				case 3:temp_y=y+1;
				break;
				}
			//if the current temporary x or y is out of range, keep searching
			}while (temp_x<0||temp_x>=row||temp_y<0||temp_y>=col);
			//opens up the space
			bo[temp_x][temp_y]=0;
		}
	}
	//checks if end point cannot be reached for method above ^^^^
	public boolean noSolutions(int x,int y) {
		if (x-1>=0&&bo[x-1][y]!=3) {
			return false;
		}
		if (x+1<row&&bo[x+1][y]!=3) {
			return false;
		}
		if (y-1>=0&&bo[x][y-1]!=3) {
			return false;
		}
		if (y+1<col&&bo[x][y+1]!=3) {
			return false;
		}
		return true;
	}
	//Depth first search maze generator
	public void DFS() {
		//holds all valid moves from current position
		int moves[] = dirCheckInt();
		//boolean version of moves, used to find dead ends
		boolean movesValid[] = dirCheckBoolean();
		//if all the movesValid elements are false (dead end), then backtrack by popping x,y stacks to the previous node
		if (isEmpty(movesValid)) {
			//removes node from stack
			x_stack.pop();
			y_stack.pop();
			//places down the target
			placeTargets();
			//if finished DFS (node stack empty), end program and set GateKeyMode back to 'k' for the next time player clicks generate
			if (x_stack.empty()&&y_stack.empty()) {
				GateKeyMode='k';
				return;
			}
			//backtracks, sets the current position to the previous
			curr_x=Integer.parseInt(x_stack.peek());
			curr_y=Integer.parseInt(y_stack.peek());
			DFS();
		//otherwise continue randomly opening 2 unopened squares
		}else {
			//choose random, valid spot to open up (up, down, left, right)
			ChooseOpenRand(moves, movesValid);
			DFS();
		}
	}
	//choose random square to open up and progress to for DFS. Also places Gates and Keys
	public void ChooseOpenRand(int moves[], boolean movesValid[]) {
		//temporarily stores current x and y location before it gets changed in randChoice method
		int x = curr_x;
		int y = curr_y;
		//adds current node to the stack
		x_stack.push(""+curr_x);
		y_stack.push(""+curr_y);
		//opens up 2 units to a random, valid direction and also changes curr_x and curr_y (current positions)
		randChoice(moves, movesValid);
		if(bo[x][y]==0) {//if there's an intersection, place either a gate block or key block
			if (hasIntersection(x,y)==2) {//place gate if 2> intersections
				if (GateKeyMode=='g') {
					GateKeyMode='k';
					bo[x][y]=5;
				}
			}else if (hasIntersection(x,y)>2){//place key if 2 intersections
				if (GateKeyMode=='k'){
					GateKeyMode='g';
					bo[x][y]=4;
				}
			}
		}
		//sets traversing to true so that when it reaches dead end, it will spawn in target (6)
		traversing=true;
	}
	//places the target on dead end (DFS)
	public void placeTargets() {
		//if DFS was originally opening up squares (traversing), then place a target at current spot and turn off traversing so that it spawns only once
		//however if there is a gate or key, move it aside to an open space beside target
		if (traversing) {
			//case if empty space
			if (bo[curr_x][curr_y]==0) {
				bo[curr_x][curr_y]=6;
			//case if space is a gate or key
			}else if (bo[curr_x][curr_y]==4||bo[curr_x][curr_y]==5){
				KeyGateCase();
			}
			//turn off traversing to prevent repeated red spot placement
			traversing=false;
		}
	}
	//in the case if there is a gate or key on the dead end. Moves it aside to place target (DFS)
	public void KeyGateCase() {
		//swap variables
		int temp = bo[curr_x][curr_y];
		bo[curr_x][curr_y]=0;
		//used if and else if statements instead of separate if statements because mathematically there can only be 1 open spot around a dead end.
		//check up for open space
		if (curr_x-1>=0&&bo[curr_x-1][curr_y]==0) {
			bo[curr_x-1][curr_y]=temp;
		//check down for open space
		}else if (curr_x+1>row&&bo[curr_x+1][curr_y]==0) {
			bo[curr_x+1][curr_y]=temp;
		//check left for open space
		}else if (curr_y-1>=0&&bo[curr_x][curr_y-1]==0) {
			bo[curr_x][curr_y-1]=temp;
		//check down for open space
		}else if (curr_y+1<col&&bo[curr_x][curr_y+1]==0) {
			bo[curr_x][curr_y+1]=temp;
		}
		bo[curr_x][curr_y]=6;
	}
	//returns an integer value for the valid spots you can traverse to (DFS)
	public int[] dirCheckInt() {
		int moves[]= new int[4];
		//checks if 2 units up is not already opened and in range
		if (curr_x-2>=0&&bo[curr_x-2][curr_y]!=0&&bo[curr_x-2][curr_y]<=3) {
			moves[0]=curr_x-2;
		}
		//checks if 2 units down is not already opened and in range
		if (curr_x+2<row && bo[curr_x+2][curr_y]!=0&& bo[curr_x+2][curr_y]<=3) {
			moves[1]=curr_x+2;
		}
		//checks if 2 units left is not already opened and in range
		if (curr_y-2>=0 && bo[curr_x][curr_y-2]!=0&& bo[curr_x][curr_y-2]<=3) {
			moves[2]=curr_y-2;
		}
		//checks if 2 units right is not already opened and in range
		if (curr_y+2<col && bo[curr_x][curr_y+2]!=0&& bo[curr_x][curr_y+2]<=3) {
			moves[3]=curr_y+2;
		}
		return moves;
	}
	//returns boolean vallue for the valid spots you can traverse to (DFS)
	public boolean[] dirCheckBoolean() {
		boolean movesValid[] = new boolean[4];
		if (curr_x-2>=0&&bo[curr_x-2][curr_y]!=0&&bo[curr_x-2][curr_y]<=3) {
			movesValid[0]=true;
		}
		if (curr_x+2<row && bo[curr_x+2][curr_y]!=0&& bo[curr_x+2][curr_y]<=3) {
			movesValid[1]=true;
		}
		if (curr_y-2>=0 && bo[curr_x][curr_y-2]!=0&& bo[curr_x][curr_y-2]<=3) {
			movesValid[2]=true;
		}
		//checks if 2 units right is not already opened and in range
		if (curr_y+2<col && bo[curr_x][curr_y+2]!=0&& bo[curr_x][curr_y+2]<=3) {
			movesValid[3]=true;
		}
		return movesValid;
	}
	//count the intersections, used for keys and gates (DFS)
	public int hasIntersection(int x, int y) {
		int intersectionCount=0;
		//up
		if ((x-1>=0&&bo[x-1][y]==0&&bo[x-1][y]<4)) {
			intersectionCount++;
		}
		//down
		if((x+1<row && bo[x+1][y]==0&& bo[x+1][y]<4)) {
			intersectionCount++;
		}
		//left
		if((y-1>=0 && bo[x][y-1]==0&& bo[x][y-1]<4)) {
			intersectionCount++;
		}
		//right
		if((y+1<col && bo[x][y+1]==0&& bo[x][y+1]<4)) {
			intersectionCount++;
		}
		return intersectionCount;
	}
	//chooses a random, valid spot and moves there (DFS)
	public void randChoice(int moves[], boolean movesValid[]) {
		//while loop to choose random possible moves in the moves array
		int randInt = (int)(Math.random()*4);
		while (!movesValid[randInt]) {
			randInt = (int)(Math.random()*4);
		}
		//if <2, move in X axis
		if (randInt<2) {
			int dx = moves[randInt]-curr_x;
			place(dx,dx/2,0,0);
		//otherwise move in Y axis
		}else {
			int dy = moves[randInt]-curr_y;
			place(0,0,dy,dy/2);
		}
	}
	//does the square placing for method above
	public void place(int x1, int x2, int y1, int y2) {
		//move by 2 units
		bo[curr_x+x1][curr_y+y1]=0;
		bo[curr_x+x2][curr_y+y2]=0;
		//sets the current value to the new value
		curr_x+=x1;
		curr_y+=y1;
	}
	//check if there are no moves valid (dead end), used for DFS backtracking
	public boolean isEmpty(boolean movesValid[]) {
		for (boolean i : movesValid) {
			if (i) return false;
		}
		return true;
	}
	//listen for clicks
	public void actionPerformed (ActionEvent e){
		//move through screens
		screensManeuver(e);
		//changes the mode
		if(e.getActionCommand().equals("mode")) {
			modeChanger();
		//generates maze
		}else if (e.getActionCommand().equals("generate")) {
			generateMaze();
			Final.playSound("mazeGenerator.wav");
		//clears the maze
		}else if(e.getActionCommand().equals("clear")) {
			clearMaze();
			Final.playSound("erasingSound.wav");
		//home button
		}else if (e.getActionCommand().equals(Final.home)) {
			Final.mainScreen();
		}
		clickedOnBoard(e);
		refresh();
	}
	//check to see if clicked on board
	public void clickedOnBoard(ActionEvent e) {
		//loops to check which square player clicked
		for (int i =0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				//clickable board when setting up the board
				if (e.getActionCommand().equals(i+" "+j)) {
					modePrompt(i,j);
					Final.playSound("moveSound.wav");
				//clickable board when playing
				}else if (e.getActionCommand().equals(i+" "+j+"p")&&isValidMove(i,j)) {
					movePiece(i,j);
				}
			}
		}
		
	}
	//transition through screens
	public void screensManeuver(ActionEvent e) {
		//instructions screen
		if(e.getActionCommand().equals("instructions")){
			layout.show(screens,"instructions");
		//instructions 2 screen
		}else if (e.getActionCommand().equals("instructions2")){
			layout.show(screens,"instructions2");
		//playing screen	
		}else if(e.getActionCommand().equals("play")) {
			keys=0;
			endButton.setText("End");
			createMovementBoard();
			Final.playSound("startGame.wav");
			layout.show(screens, "play");
		//ends the playing screen (setup)
		}else if(e.getActionCommand().equals("end")) {
			Final.playSound("startGame.wav");
			layout.show(screens, "end");
		}
	}
	//changes the squares mode for maze building
	public void modeChanger() {
		//6 modes, if the mode int reaches 7, reset to 1
		if (mode==6) {
			mode=1;
		}else {
			mode++;
		}
		//changes mode button label
		modeButton.setText("Mode: "+((mode==1)?"Start":(mode==2)?"End":(mode==3)?"Wall":(mode==4)?"Key":(mode==5)?"Gate":"Target"));
	}
	//checks which is the current mode when player clicks on a square
	public void modePrompt(int i, int j) {
		switch(mode) {
		case 1:placeStart(i,j);//place starting node
		break;
		case 2:placeEnd(i,j);//place end node
		break;
		case 3: wall(i,j);//place wall
		break;
		case 4: key(i, j);//place key
		break;
		case 5: gate(i, j);//place gate
		break;
		case 6: target(i, j);//place target
		break;
		}
	}
	public void createMovementBoard() {
		//sets the current position when playing
		move_x=start_x;
		move_y=start_y;
		//creates the second board to be shown while playing
		createSecondBoard();
		for (int i =0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				pics2[i][j].setIcon(createImageIcon("e"+bo2[i][j]+".png"));
			}
		}
		//counts targets
		countTargets();
		//sets the keys and target labels
		keysLabel.setText("Keys: "+keys);
	}
	//Moves the piece to the spot the player clicks on
	public void movePiece(int i,int j) {
		//increments and decrements target and key values
		keysAndTargetsIncrements(i,j);
		if (bo2[i][j]==2&&targets==0) {//win condition
			Final.playSound("winSound.wav");
			endButton.setText("You Win! Click to return.");
		}
		pics2[i][j].setIcon(createImageIcon("e"+1+".png"));
		bo2[i][j]=1;
		if (bo[move_x][move_y]==2) {//so that player can overlap end point without deleting it
			pics2[move_x][move_y].setIcon(createImageIcon("e"+2+".png"));
			bo2[move_x][move_y]=2;
			Final.playSound("moveSound.wav");
		}else {//move on empty square
			pics2[move_x][move_y].setIcon(createImageIcon("e"+0+".png"));
			bo2[i][j]=0;
			Final.playSound("moveSound.wav");
		}
		move_x=i;
		move_y=j;
		keysLabel.setText("Keys: "+keys);
	}
	//increases/decreases keys and target values when player touches them
	public void keysAndTargetsIncrements(int i, int j) {
		//++ key if player touches key
		if(bo2[i][j]==4) {
			keys++;
			Final.playSound("keysSound.wav");
		}
		//-- key if player touches gate
		if(bo2[i][j]==5) {
			keys--;
			Final.playSound("keysSound.wav");
		}
		//-- target if player touches target
		if (bo2[i][j]==6) {
			targets--;
			progress.setValue(targetPercCalculator());
			Final.playSound("collectSound.wav");
		}
	}
	//creates the second board so that the original board's data gets saved
	public void createSecondBoard() {
		for (int i =0;i<row;i++) {
			for (int j =0;j<row;j++) {
				bo2[i][j]=bo[i][j];
			}
		}
	}
	//counts the # of targets on the playing board
	public void countTargets() {
		int count=0;
		for (int i =0;i<row;i++) {
			for (int j =0;j<row;j++) {
				if (bo[i][j]==6) {
					count++;
				}
			}
		}
		targets=count;
		total_targets=targets;
		progress.setValue(targetPercCalculator());
	}
	//checks if there is anything blocking the way (gate with not enough keys or walls in the way)
	public boolean isValidMove(int x, int y) {
		if(keys==0&&bo2[x][y]==5||(x==move_x&&y==move_y)) {
			return false;
		}
		if (bo2[x][y]!=3&&(Math.abs(move_x-x)+Math.abs(move_y-y)==1)) {
			return true;
		}
		return false;
	}
	//clears all walls
	public void clearMaze() {
		for (int i =0 ;i<row;i++) {
			for (int j =0 ;j<col;j++) {
				if (bo[i][j]>=3) {
					bo[i][j]=0;
				}
			}
		}
		refresh();
	}
	//updates the board
	public void refresh() {
		for (int i = 0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				pics[i][j].setIcon(createImageIcon("e"+bo[i][j]+".png"));
			}
		}
	}
	//places target so that players have to get these before reaching the end
	public void target(int x,int y) {
		if (bo[x][y]!=1&&bo[x][y]!=2) {
			if (bo[x][y]==6) {
				bo[x][y]=0;
			}else {
				bo[x][y]=6;
			}
		}
	}
	//places gate to block player
	public void gate(int x,int y) {
		if (bo[x][y]!=1&&bo[x][y]!=2) {
			if (bo[x][y]==5) {
				bo[x][y]=0;
			}else {
				bo[x][y]=5;
			}
		}
	}
	//places key to open the gates
	public void key(int x,int y) {
		if (bo[x][y]!=1&&bo[x][y]!=2) {
			if (bo[x][y]==4) {
				bo[x][y]=0;
			}else {
				bo[x][y]=4;
			}
		}
	}
	//places wall to block trail
	public void wall(int x, int y) {
		if (bo[x][y]!=1&&bo[x][y]!=2) {
			if (bo[x][y]==3) {
				bo[x][y]=0;
			}else {
				bo[x][y]=3;
			}
		}
	}
	//places final destination or end node
	public void placeEnd(int x,int y) {
		for (int i =0;i<row;i++) {
			for (int j =0;j<row;j++) {
				//resets start position
				if (bo[i][j]==2){
					bo[i][j]=0;
				}
			}
		}
		if (bo[x][y]!=1) {
			end_x=x;
			end_y=y;
			bo[x][y]=2;
		}
	}
	//places the starting node
	public void placeStart(int x,int y) {
		for (int i =0;i<row;i++) {
			for (int j =0;j<row;j++) {
				//resets start position
				if (bo[i][j]==1){
					bo[i][j]=0;
				}
			}
		}
		if (bo[x][y]!=2) {
			start_x=x;
			start_y=y;
			bo[x][y]=1;
		}
	}
	//creates image/icons
	protected static ImageIcon createImageIcon (String path)
	{
		java.net.URL imgURL = Checkers.class.getResource (path);
		if (imgURL != null)
			return new ImageIcon (imgURL);
		else
			return null;
	}
}
