//Name: Eddison Pham
//Date: 1/7/2022
//Purpose: Connect Four Moves Object
public class ConnectFourMoves {
	//the position coordinates
	int x;
	int y;
	//if it's open, then it's place-able
	boolean open;
	//creates a valid place-able position on connect four grid
	public ConnectFourMoves(int x2, int y2, boolean open2) {
		x=x2;
		y=y2;
		open=open2;
	}
}
