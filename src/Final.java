import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
//Name: Eddison Pham
//Date: 1/7/2022
//Purpose: Final Board Game Project
public class Final extends JPanel implements ActionListener{
	//global constants (font)
	static final String font = "snap itc";
	//global constants (title font size)
	static final int size = 40;
	//global action command key
	static final String home = "homekey";
	//screen dimensions constants
	static final int screen_x=420;
	static final int screen_y=700;
	//navigator buttons constants
	static final int button_x = 400;
	static final int button_y = 100;
	//screen holder so that I can change background
	Panel screen;
	//main screen
	Panel mainLayout;
	//card layout
	CardLayout layout = new CardLayout();
	//starts the program
	public static void main(String args[]) {
		mainScreen();
	}
	//static play sound method to play sounds across all the programs (credit to Samyam)
	public static void playSound(String path) {
		soundStuff soundObject = new soundStuff();
		soundObject.playSound(System.getProperty("user.dir") + "\\src\\" + path);
	}
	//starts the main screen to select game
	public static void mainScreen() {
		JFrame.setDefaultLookAndFeelDecorated (true);
		//Create and set up the window.
		JFrame frame = new JFrame ("Final");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		//Create and set up the content pane.
		JComponent newContentPane = new Final ();
		newContentPane.setOpaque (true);
		frame.setContentPane (newContentPane);
		frame.setSize (screen_x, screen_y);
		frame.setVisible (true);
	}
	//constructor method
	public Final() {
		screen = new Panel();
		screen = new Panel();
		screen.setLayout(layout);
		screen.setBackground(Color.white);
		mainLayout();
		setLayout(new BorderLayout());
		add("Center",screen);
	}
	//the main layout of the main screen (home screen)
	public void mainLayout() {
		mainLayout = new Panel();
		//go to checkers
		JLabel choose = new JLabel("Choose A Game:");
		choose.setFont(new Font(font,Font.BOLD,18));
		JButton checkers = new JButton("Checkers");
		checkers.setActionCommand("checkers");
		checkers.addActionListener(this);
		checkers.setIcon(createImageIcon("checkersButton.png"));
		checkers.setPreferredSize(new Dimension(button_x,button_y));
		checkers.setFont(new Font(font,Font.BOLD,20));
		//go to maze builder
		JButton maze = new JButton("Maze Builder");
		maze.setActionCommand("maze");
		maze.addActionListener(this);
		maze.setIcon(createImageIcon("mazeBuilderButton.png"));
		maze.setPreferredSize(new Dimension(button_x,button_y));
		maze.setFont(new Font(font,Font.BOLD,20));
		//go to 2048
		JButton _2048 = new JButton("2048");
		_2048.setActionCommand("2048");
		_2048.addActionListener(this);
		_2048.setIcon(createImageIcon("2048Button.png"));
		_2048.setPreferredSize(new Dimension(button_x,button_y));
		_2048.setFont(new Font(font,Font.BOLD,20));
		//go to regular connect four
		JButton cf = new JButton("Connect Four");
		cf.setActionCommand("connect four");
		cf.addActionListener(this);
		cf.setIcon(createImageIcon("connectFourButton.png"));
		cf.setPreferredSize(new Dimension(button_x,button_y));
		cf.setFont(new Font(font,Font.BOLD,20));
		//go to connect four AI
		JButton cfAI = new JButton("Connect Four AI");
		cfAI.setActionCommand("connect four AI");
		cfAI.addActionListener(this);
		cfAI.setIcon(createImageIcon("cfAIButton.png"));
		cfAI.setPreferredSize(new Dimension(button_x,button_y));
		cfAI.setFont(new Font(font,Font.BOLD,20));
		//go to othello
		JButton othello = new JButton("Othello");
		othello.setActionCommand("othello");
		othello.addActionListener(this);
		othello.setIcon(createImageIcon("othelloButton.png"));
		othello.setPreferredSize(new Dimension(button_x,button_y));
		othello.setFont(new Font(font,Font.BOLD,20));
		//add widgets to main screen
		mainLayout.add(choose);
		mainLayout.add(checkers);
		mainLayout.add(maze);
		mainLayout.add(_2048);
		mainLayout.add(cf);
		mainLayout.add(cfAI);
		mainLayout.add(othello);
		screen.add(mainLayout);
	}
	//listen for clicks
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("checkers")) {//go to checks
			checkers();
		}else if (e.getActionCommand().equals("othello")) {//go to othello
			othello();
		}else if (e.getActionCommand().equals("connect four AI")) {//go to connect four AI
			connectfourAI();
		}else if (e.getActionCommand().equals("connect four")) {//go to connect four (player v player)
			connectfour();
		}else if (e.getActionCommand().equals("2048")) {//go to 2048
			_2048();
		}else if (e.getActionCommand().equals("maze")) {// go to maze builder
			maze();
		}
	}
	//starts maze builder program
	public static void maze() {
		JFrame.setDefaultLookAndFeelDecorated (true);
		//Create and set up the window.
		JFrame frame = new JFrame ("Maze");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		//Create and set up the content pane.
		JComponent newContentPane = new Maze ();
		newContentPane.setOpaque (true);
		frame.setContentPane (newContentPane);
		frame.setSize (screen_x, screen_y);
		frame.setVisible (true);
	}
	//starts connect four AI program
	public static void connectfourAI() {
		JFrame.setDefaultLookAndFeelDecorated (true);
		//Create and set up the window.
		JFrame frame = new JFrame ("Connect Four A.I.");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		//Create and set up the content pane.
		JComponent newContentPane = new ConnectFourAI ();
		newContentPane.setOpaque (true);
		frame.setContentPane (newContentPane);
		frame.setSize (screen_x, screen_y);
		frame.setVisible (true);
	}
	//starts connect four player v player program
	public static void connectfour() {
		JFrame.setDefaultLookAndFeelDecorated (true);
		//Create and set up the window.
		JFrame frame = new JFrame ("Connect Four");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		//Create and set up the content pane.
		JComponent newContentPane = new ConnectFour ();
		newContentPane.setOpaque (true);
		frame.setContentPane (newContentPane);
		frame.setSize (screen_x, screen_y);
		frame.setVisible (true);
	}
	//starts checkers program
	public static void checkers() {
		JFrame.setDefaultLookAndFeelDecorated (true);
		//Create and set up the window.
		JFrame frame = new JFrame ("Checkers");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		//Create and set up the content pane.
		JComponent newContentPane = new Checkers ();
		newContentPane.setOpaque (true);
		frame.setContentPane (newContentPane);
		frame.setSize (screen_x, screen_y);
		frame.setVisible (true);
	}
	//starts 2048 program
	public static void _2048() {
		JFrame.setDefaultLookAndFeelDecorated (true);
		//Create and set up the window.
		JFrame frame = new JFrame ("2048");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		//Create and set up the content pane.
		JComponent newContentPane = new _2048 ();
		newContentPane.setOpaque (true);
		frame.setContentPane (newContentPane);
		frame.setSize (screen_x, screen_y);
		frame.setVisible (true);
	}
	//starts othello program
	public static void othello() {
		JFrame.setDefaultLookAndFeelDecorated (true);
		//Create and set up the window.
		JFrame frame = new JFrame ("Othello");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		//Create and set up the content pane.
		JComponent newContentPane = new Othello ();
		newContentPane.setOpaque (true);
		frame.setContentPane (newContentPane);
		frame.setSize (screen_x, screen_y);
		frame.setVisible (true);
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
