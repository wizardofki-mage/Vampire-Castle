// Programmer: Jason Ash
// Creation date: 07-09 May, 2026
// Description: VampireCastle.java and Warrior.java are based on the Pac-Man tutorial by Kenny Yip on YouTube at https://www.youtube.com/watch?v=lB_J-VNMVpE&t=17s
// It is programmed in Java using Notepad and JDK 21 using the Windows Command Prompt for compiling and running it.
// Inputs: Type the left, right, up, and down arrow keys for movement, p to pause, and any key to restart the game after game over.
// Output: The Vampire Castle game where the objective is to collect all of the coins to advance to the next level and not to collide with monsters or else you will lose a life in the game each time this occurs.
// Run: Compile the game by typing javac Warrior.java and javac VampireCastle.java in the command line and then java VampireCastle to run it. Alternatively, you can use your IDE of choice.

import javax.swing.JFrame;
import java.awt.event.*;

public class VampireCastle
{
	public static void main(String[] args) throws Exception
	{
		int rowCount = 21, columnCount = 19, tileSize = 32;
		int boardWidth = columnCount * tileSize;
		int boardHeight = rowCount * tileSize;

		JFrame frame = new JFrame("Vampire Castle");
		frame.setSize(boardWidth, boardHeight);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Warrior warriorGame = new Warrior();
		frame.add(warriorGame);
		frame.pack();
		warriorGame.requestFocus();
		frame.setVisible(true);
	}

}