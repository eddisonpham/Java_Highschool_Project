import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Stack;
//Name: Eddison Pham
//Date: 1/7/2022
//Purpose: Othello
public class Othello extends JPanel implements ActionListener
{
	//board dimensions
	int row = 8;
	int col = 8;
	//board data
	int bo[][] = new int[row][col];
	//pictures data
	JButton pics[][] = new JButton[row][col];
	//turn holder
	int turn = 1;
	//screens holder
	Panel screens;
	//screen 1 (instructions), screen 2 (play game)
	Panel instructions,play;
	//card layout
	CardLayout layout = new CardLayout();
	//turn label initialized
	JLabel turnLabel;
	//board game grid panel
	Panel grid;
	//(x,y) position stacks used for flipping pieces (go to line 261)
	Stack <String> x_stack = new Stack<String>();
	Stack <String> y_stack = new Stack<String>();
	//constructor method
	public Othello ()
	{
		screens = new Panel();
		screens.setLayout(layout);
		screens.setBackground(Color.white);
		//spawn starting pieces
		bo[(row/2)-1][(col/2)-1]=1;
		bo[(row/2)-1][col/2]=2;
		bo[(row/2)][col/2]=1;
		bo[row/2][(col/2)-1]=2;
		createGrid();
		instructions();
		play();
		setLayout(new BorderLayout());
		add("Center",screens);
	}
	//creates the game grid
	public void createGrid() {
		//grid panel to hold the squares
		grid = new Panel(new GridLayout(8,8));
		//creates individual pictures for the squares
		for (int i =0;i<row;i++) {
			for(int j =0;j<col;j++) {
				pics[i][j]=new JButton(createImageIcon("p"+bo[i][j]+".png"));
				pics[i][j].setPreferredSize(new Dimension(50,50));
				pics[i][j].setBorder(null);
				if (bo[i][j]==0) {
					pics[i][j].setActionCommand(i+" "+j);
					pics[i][j].addActionListener(this);
				}
				grid.add(pics[i][j]);
			}
		}
	}
	public void instructions() {
		instructions = new Panel();
		//welcoming title
		//instructions label
		JLabel title = new JLabel("Othello Instructions");
		title.setFont(new Font(Final.font,Font.BOLD,32));
		title.setPreferredSize(new Dimension(400,70));//y padding
		//tutorial image
		JLabel tutorial = new JLabel(createImageIcon("othelloTutorial.png"));
		//play button
		JButton play = new JButton("Play");
		play.setActionCommand("play");
		play.addActionListener(this);
		play.setPreferredSize(new Dimension(400,30));
		play.setFont(new Font(Final.font,Font.BOLD,20));
		play.setBackground(Color.white);
		//home button
		JButton home = new JButton("Return To Home Screen");
		home.setFont(new Font(Final.font,Font.BOLD,20));
		home.setBackground(Color.white);
		home.setPreferredSize(new Dimension(400,30));
		home.setActionCommand(Final.home);
		home.addActionListener(this);
		//put all widgets on screen
		instructions.add(title);
		instructions.add(tutorial);
		instructions.add(play);
		instructions.add(home);
		screens.add("instructions",instructions);
	}
	public void play() {
		play = new Panel();
		//title label
		JLabel title = new JLabel("Othello");
		title.setFont(new Font(Final.font,Font.BOLD,Final.size));
		//reset game button
		JButton reset = new JButton("Reset");
		reset.setActionCommand("reset");
		reset.addActionListener(this);
		reset.setPreferredSize(new Dimension(400,30));
		reset.setFont(new Font(Final.font,Font.BOLD,20));
		reset.setBackground(Color.white);
		//end game button
		JButton end = new JButton("Auto End");
		end.setActionCommand("end");
		end.addActionListener(this);
		end.setPreferredSize(new Dimension(400,30));
		end.setFont(new Font(Final.font,Font.BOLD,20));
		end.setBackground(Color.white);
		//instructions button
		JButton tutorial = new JButton("Instructions");
		tutorial.setActionCommand("instructions");
		tutorial.addActionListener(this);
		tutorial.setPreferredSize(new Dimension(400,30));
		tutorial.setFont(new Font(Final.font,Font.BOLD,20));
		tutorial.setBackground(Color.white);
		//shows which turn it is, or whether game is over or not
		turnLabel = new JLabel("Black's turn.");
		turnLabel.setFont(new Font(Final.font,Font.BOLD,20));
		//put all widgets on screen
		play.add(title);
		play.add(grid);
		play.add(turnLabel);
		play.add(reset);
		play.add(end);
		play.add(tutorial);
		mark("p"+((turn==1)?4:3)+".png");
		screens.add("play",play);
	}
	//look for any widgets clicked
	public void actionPerformed (ActionEvent e)
	{
		screensManeuver(e);//maneuver through screens
		for (int i =0;i<row;i++) {//loops through board to check which square was clicked
			for (int j = 0;j<col;j++) {
				if (e.getActionCommand().equals(i+" "+j)) {
					Final.playSound("moveSound.wav");
					int opp = (turn==1)?2:1;//change turn
					flip(turn,i,j,opp);//flips the opponent's pieces					
				}
			}
		}
		//automatically ends the game
		if (e.getActionCommand().equals("end")) {
			autoEnd();
		//resets the board
		}else if (e.getActionCommand().equals("reset")) {
			Final.playSound("startGame.wav");
			reset();
		}else if (e.getActionCommand().equals(Final.home)) {
			Final.mainScreen();
		}
	}
	//maneuver through screens
	public void screensManeuver(ActionEvent e) {
		//playing screen
		if (e.getActionCommand().equals("play")) {
			Final.playSound("startGame.wav");
			layout.show(screens,"play");
		//instructions screen
		}else if (e.getActionCommand().equals("instructions")) {
			layout.show(screens,"instructions");
		}
	}
	//automatically ends the game with legal moves (randomly
	public void autoEnd() {
		while (!pass(1)||!pass(2)) {//while loop until neither player can make a valid move
			int i = (int)(Math.random()*8);
			int j = (int)(Math.random()*8);
			int opp = (turn==1)?2:1;
			flip(turn,i,j,opp);
		}
		Final.playSound("gameOver.wav");
	}
	//resets the board
	public void reset() {
		//clears the board
		for (int i =0;i<row;i++) {
			for (int j =0;j<col;j++) {
				bo[i][j]=0;
			}
		}
		turn = 1;
		//places starting pieces on the board
		bo[(row/2)-1][(col/2)-1]=1;
		bo[(row/2)-1][col/2]=2;
		bo[(row/2)][col/2]=1;
		bo[row/2][(col/2)-1]=2;
		//reset pictures
		for (int i =0;i<row;i++) {
			for (int j =0;j<col;j++) {
				pics[i][j].setIcon(createImageIcon("p"+bo[i][j]+".png"));
			}
		}
		turnLabel.setText("Black's turn.");//reset turn label
		mark("p"+((turn==1)?4:3)+".png");//mark the possible positions the first player can move 
	}
	//checks if the entire board is filled
	public boolean boardFilled() {
		for (int i =0;i<row;i++) {
			for (int j = 0;j<col;j++) {
				if (bo[i][j]!=0) {//if its not filled, return false
					return false;
				}
			}
		}
		return true;
	}
	//mark the possible spots that the current player can go to
	public void mark(String m) {
		for(int i =0;i<row;i++) {
			for(int j = 0;j<col;j++) {
				//if empty spot and a move that flips the opponent's pieces, then mark it
				if (bo[i][j]==0&&isValid(turn,i,j,((turn==1)?2:1))) {
					pics[i][j].setIcon(createImageIcon(m));
				}
			}
		}
	}
	//updates the turn label
	public void updateLabel() {
		turn =(turn==1)?2:1;//flips turn
		//checks game over
		if (pass(1)&&pass(2)) {
			int empty = count(0);
			int b = count(1);
			int w = count(2);
			int total = w+b+empty;
			Final.playSound("gameOver.wav");
			turnLabel.setText((w<b)?"Black wins with "+b+"/"+total:(b<w)?"White wins with "+w+"/"+total:"Tie");
		//skips current player's turn if no possible moves
		}else if (pass(turn)) {
			turnLabel.setText(((turn==1)?"Black":"White")+"'s turn skipped. Move again.");
			turn=(turn==1)?2:1;
		//otherwise change the label normally
		}else{
			turnLabel.setText(((turn==1)?"Black":"White")+"'s turn.");
		}
	}
	//counts number of pieces for method above
	public int count(int n) {
		int am = 0;
		for(int i =0;i<row;i++) {
			for(int j = 0;j<col;j++) {
				if (bo[i][j]==n) {
					am++;
				}
			}
		}
		return am;
	}
	//checks if t (player) has no valid moves
	public boolean pass(int t) {
		for(int i =0;i<row;i++) {
			for(int j = 0;j<col;j++) {
				if (bo[i][j]==0&&isValid(t,i,j,(t==1)?2:1)) {
					return false;
				}
			}
		}
		return true;
	}
	//flips 
	public void flip(int t,int i,int j,int opp) {
		if (bo[i][j]==0&&isValid(t,i,j,opp)) {
			mark("p0.png");
			bo[i][j]=turn;
			pics[i][j].setIcon(createImageIcon("p"+t+".png"));
			turn(t,i,j);
			updateLabel();
			mark("p"+((turn==1)?4:3)+".png");
		}
	}
	//flips the enemy pieces, horizontal, vertical, diagonally down, diagonally up
	public void turn(int t,int i, int j) {
		int opp = (t==1)?2:1;//opponent
		verticalFlip(t,i,j,opp);
		hoirzontalFlip(t,i,j,opp);
		DDFlip(t,i,j,opp);
		DUFlip(t,i,j,opp);
	}
	//flips vertically (up and down)
	public void verticalFlip(int t,int i,int j,int opp) {
		int temp_i=i;
		int curr = 1;
		for (int m=0;m<2;m++) {
			boolean bool = (curr>0)?i+curr<row:i+curr>=0;
			while (bool&&bo[i+curr][j]!=0) {
				i+=curr;
				x_stack.add(i+"");
				bool = (curr>0)?i+curr<row:i+curr>=0;
				if (bool&&bo[i+curr][j]==t) {
					while(x_stack.size()!=0){
						int x = Integer.parseInt(x_stack.peek());
						bo[x][j]=t;
						pics[x][j].setIcon(createImageIcon("p"+t+".png"));
						x_stack.pop();
					}
					break;
				}
			}
			x_stack.clear();
			curr*=-1;
			i=temp_i;
		}	
	}
	//flips horizontally (left and right)
	public void hoirzontalFlip(int t,int i,int j,int opp) {
		int temp_j=j;
		int curr = 1;
		for (int m=0;m<2;m++) {
			boolean bool = (curr>0)?j+curr<row:j+curr>=0;
			while (bool&&bo[i][j+curr]==opp) {
				j+=curr;
				y_stack.add(j+"");
				bool = (curr>0)?j+curr<row:j+curr>=0;
				if (bool&&bo[i][j+curr]==t) {
					while(y_stack.size()!=0){
						int y = Integer.parseInt(y_stack.peek());
						bo[i][y]=t;
						pics[i][y].setIcon(createImageIcon("p"+t+".png"));
						y_stack.pop();
					}
					break;
				}
			}
			y_stack.clear();
			curr*=-1;
			j=temp_j;
		}	
	}
	//diagonally down left AND right
	public void DDFlip(int t,int i,int j,int opp) {
		int temp_j=j;
		int temp_i=i;
		int curr = 1;
		for (int m=0;m<2;m++) {
			DDFwhileloop(curr,opp,i,j,t);
			x_stack.clear();
			y_stack.clear();
			curr*=-1;
			j=temp_j;
			i=temp_i;
		}	
	}
	//while loop to for method above (checks the similar consecutive pieces)
	public void DDFwhileloop(int curr,int opp,int i,int j, int t) {
		boolean bool = (curr>0)?j+curr<row:j+curr>=0;
		boolean bool2 = (curr>0)?i+curr<col:i+curr>=0;
		while (bool&&bool2&&bo[i+curr][j+curr]==opp) {
			j+=curr;
			i+=curr;
			x_stack.add(i+"");
			y_stack.add(j+"");
			bool = (curr>0)?j+curr<row:j+curr>=0;
			bool2 = (curr>0)?i+curr<col:i+curr>=0;
			if (bool&&bool2&&bo[i+curr][j+curr]==t) {
					while(x_stack.size()!=0) {
						int y = Integer.parseInt(y_stack.peek());
						int x = Integer.parseInt(x_stack.peek());
						bo[x][y]=t;
						pics[x][y].setIcon(createImageIcon("p"+t+".png"));
						y_stack.pop();
						x_stack.pop();
					}
					break;
			}
		}
	}
	//diagonally up left AND right
	public void DUFlip(int t,int i,int j,int opp) {
		int temp_j=j;
		int temp_i=i;
		int curr = 1;
		int curr2 = -1;
		for (int m=0;m<2;m++) {
			DUFwhileloop(curr,curr2,opp,i,j,t);
			x_stack.clear();
			y_stack.clear();
			curr*=-1;
			curr2*=-1;
			j=temp_j;
			i=temp_i;
		}
	}
	//while loop for method above (checks the similar consecutive pieces)
	public void DUFwhileloop(int curr,int curr2, int opp, int i, int j,int t) {
		boolean bool = (curr2>0)?j+curr2<row:j+curr2>=0;
		boolean bool2 = (curr>0)?i+curr<col:i+curr>=0;
		while ((bool&&bool2)&&bo[i+curr][j+curr2]==opp) {
			j+=curr2;
			i+=curr;
			x_stack.add(i+"");
			y_stack.add(j+"");
			bool = (curr2>0)?j+curr2<row:j+curr2>=0;
			bool2 = (curr>0)?i+curr<col:i+curr>=0;
			if (bool&&bool2&&bo[i+curr][j+curr2]==t) {
				while(x_stack.size()!=0) {
					int y = Integer.parseInt(y_stack.peek());
					int x = Integer.parseInt(x_stack.peek());
					bo[x][y]=t;
					pics[x][y].setIcon(createImageIcon("p"+t+".png"));
					y_stack.pop();
					x_stack.pop();
				}
				break;
			}
		}
	}
	//checks if the move is valid (flips any enemy pieces)
	public boolean isValid(int t,int i,int j,int opp) {
		//north
		boolean n = n(t,i,j,opp);
		//south
		boolean s = s(t,i,j,opp);
		//east
		boolean e = e(t,i,j,opp);
		//west
		boolean w = w(t,i,j,opp);
		//up left
		boolean ul = ul(t,i,j,opp);
		//down left
		boolean dl = dl(t,i,j,opp);
		//up right
		boolean ur = ur(t,i,j,opp);
		//down right
		boolean dr = dr(t,i,j,opp);
		return n||s||e||w||ul||dl||ur||dr;
	}
	//check north for isValid method (same EXACT comments for all the methods similar to this until line 518)
	public boolean n(int t,int i,int j,int opp) {
		int num = 1;
		//checks leading piece if it's the same, if so, return true otherwise keep looking
		//first boolean before the && are boundaries
		//same EXACT code for all the other similar methods (s,e,w,dl,dr,ul,ur)
		while (i-num>=0&&bo[i-num][j]==opp) {
			//if statement to break out of loop
			//checks if the leading piece if your piece (meaning your pieces are trapping the enemy pieces)
			if (i-num-1>=0&&bo[i-num-1][j]==t) {
				return true;
			//otherwise keep looking
			}else {
				num++;
			}
		}
		return false;
	}
	//checks south for isValid method
	public boolean s(int t,int i,int j, int opp) {
		int num = 1;
		while (i+num<row&&bo[i+num][j]==opp) {
			if (i+num+1<8&&bo[i+num+1][j]==t) {
				return true;
			}else {
				num++;
			}
		}
		return false;
	}
	//checks east for isValid method
	public boolean e(int t,int i,int j,int opp) {
		int num = 1;
		while (j+num<row&&bo[i][j+num]==opp) {
			if (j+num+1<8&&bo[i][j+num+1]==t) {
				return true;
			}else {
				num++;
			}
		}
		return false;
	}
	//checks west for isValid method
	public boolean w(int t,int i,int j,int opp) {
		int num = 1;
		while (j-num>=0&&bo[i][j-num]==opp) {
			if (j-num-1>=0&&bo[i][j-num-1]==t) {
				return true;
			}else {
				num++;
			}
		}
		return false;
	}
	//checks diagonally up left for isValid method
	public boolean ul(int t,int i,int j,int opp) {
		int num = 1;
		while (i-num>=0&&j-num>=0&&bo[i-num][j-num]==opp) {
			if (i-num-1>=0 &&j-num-1>=0&&bo[i-num-1][j-num-1]==t) {
				return true;
			}else {
				num++;
			}
		}
		return false;
	}
	//checks diagonally down left for isValid method
	public boolean dl(int t,int i,int j,int opp) {
		int num = 1;
		while (i+num<row&&j-num>=0&&bo[i+num][j-num]==opp) {
			if (i+num+1<row &&j-num-1>=0&&bo[i+num+1][j-num-1]==t) {
				return true;
			}else {
				num++;
			}
		}
		return false;
	}
	//checks diagonally up right for isValid method
	public boolean ur(int t,int i,int j,int opp) {
		int num = 1;
		while (i-num>=0&&j+num<col&&bo[i-num][j+num]==opp) {
			if (i-num-1>=0 &&j+num+1<col&&bo[i-num-1][j+num+1]==t) {
				return true;
			}else {
				num++;
			}
		}
		return false;
	}
	//checks diagonally down right for isValid method
	public boolean dr(int t,int i,int j,int opp) {
		int num = 1;
		while (i+num<row&&j+num<col&&bo[i+num][j+num]==opp) {
			if (i+num+1<row &&j+num+1<col&&bo[i+num+1][j+num+1]==t) {
				return true;
			}else {
				num++;
			}
		}
		return false;
	}
	//creates image icon
	protected static ImageIcon createImageIcon (String path)
	{
		java.net.URL imgURL = Othello.class.getResource (path);
		if (imgURL != null)
			return new ImageIcon (imgURL);
		else
			return null;
	}
}

