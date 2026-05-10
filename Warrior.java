// Programmer: Jason Ash
// Creation date: 07-09 May, 2026
// Description: VampireCastle.java and Warrior.java are based on the Pac-Man tutorial by Kenny Yip on YouTube at https://www.youtube.com/watch?v=lB_J-VNMVpE&t=17s
// It is programmed in Java using Notepad and JDK 21 using the Windows Command Prompt for compiling and running it.
// Inputs: Type the left, right, up, and down arrow keys for movement, p to pause, and any key to restart the game after game over.
// Output: The Vampire Castle game where the objective is to collect all of the coins to advance to the next level and not to collide with monsters or else you will lose a life in the game each time this occurs.
// Run: Compile the game by typing javac Warrior.java and javac VampireCastle.java in the command line and then java VampireCastle to run it. Alternatively, you can use your IDE of choice.

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;


public class Warrior extends JPanel implements ActionListener, KeyListener
{
	JFrame frame = new JFrame("");
	class Block
	{
		int x, y, width, height;
		Image image;

		// for restarting the game, save the original x and y positions
		int startX, startY;

		char direction = 'U'; // U, D, L, or R
		int velocityX = 0;
		int velocityY = 0;
		
		// Constructor
		Block(Image image, int x, int y, int width, int height)
		{
			this.image = image;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.startX = x;
			this.startY = y;
		}

		void updateDirection(char direction)
		{
			char prevDirection = this.direction;
			this.direction = direction;
			updateVelocity();
			this.x += this.velocityX;
			this.y += this.velocityY;
			for(Block wall : walls)
			{
				if(collision(this, wall))
				{
					this.x -= this.velocityX;
					this.y -= this.velocityY;
					this.direction = prevDirection;
					updateVelocity();
				}
			}

		}

		void updateVelocity()
		{
			if(this.direction == 'U')
			{
				this.velocityX = 0;
				this.velocityY = -tileSize / 4;
			}
			else if(this.direction == 'D')
			{
				this.velocityX = 0;
				this.velocityY = tileSize / 4;
			}
			else if(this.direction == 'L')
			{
				this.velocityX = -tileSize / 4;
				this.velocityY = 0;
			}
			else if(this.direction == 'R')
			{
				this.velocityX = tileSize / 4;
				this.velocityY = 0;

			}
		}

		// Resets direction when the player dies
		void reset()
		{
			this.x = this.startX;
			this.y = this.startY;
		} 
	}

	private int rowCount = 21;
	private int columnCount = 19;
	private int tileSize = 32;
	private int boardWidth = columnCount * tileSize;
	private int boardHeight = rowCount * tileSize;

	// int numTimes is used so that the enemy movement is not so spastic
	private int numTimes = 0;

	// int highScore loads and stores high score in the file by calling loadHighScore
	private int highScore = loadHighScore();

	//member variables to store images
	private Image wallImage;
	private Image batImage;
	private Image vampireImage;
	private Image mummyImage;
	private Image zombieImage;
	private Image coinImage;
	
	//Image variables for the player
	private Image warriorUpImage;
	private Image warriorDownImage;
	private Image warriorLeftImage;
	private Image warriorRightImage;

	// creating HashSets (which are similar to arrays, but more efficient to search)
	HashSet<Block> walls;
	HashSet<Block> coins;
	HashSet<Block> enemies;
	
	// Only one block is need to represent the warrior (player)
	Block warrior;

	Timer gameLoop;
	char[] directions = {'U', 'D', 'L', 'R'};
	Random random = new Random();
	int score = 0;
	int lives = 3;
	boolean gameOver = false;
	boolean paused = false;
	int level = 0;
	int extraLifeScore = 0;

	// for storing the highscore in a file
	private final String FILE_NAME = "highscore.txt";

	// Tile map: X = wall, O = skip, W = Warrior, ' ' = coin
	// Enemies: v = vampire, b = bat, m = mummy, z = zombie;

	private String[][] tileMap = { {
	"XXXXXXXXXXXXXXXXXXX",
	"X        X        X",
	"X XX XXX X XXX XX X",
	"X                 X",
	"X XX X XXXXX X XX X",
	"X    X       X    X",
	"XXXX XXXX XXXX XXXX",
	"000X X       X X000",
	"XXXX X XXmXX X XXXX",
	"O       bvz       O",
	"XXXX X XXXXX X XXXX",
	"000X X       X XXXX",
	"XXXX X XXXXX X XXXX",
	"X        X        X",
	"X XX XXX X XXX XX X",
	"X  X     W     X  X",
	"XX X X XXXXX X X XX",
	"X    X   X   X    X",
	"X XXXXXX X XXXXXX X",
	"X                 X",
	"XXXXXXXXXXXXXXXXXXX"},
 
	{
	"XXXXXXXXXXXXXXXXXXX",
	"X  XX  X      XX  X",
	"X        XX       X",
	"X   X X  X    X  XX",
	"X   X         X   X",
	"XvX X   X X X   X X",
	"XXXX    X  X    X X",
	"XX  X           X X",
	"XX   X         X bX",
	"X      X W X  X   X",
	"X X           X   X",
	"Xz   X X  X  X    X",
	"X    X       X    X",
	"0    X X     X    O",
	"XXX X  X     X    X",
	"XXXX    X   X    XX",
	"X  XXX  X   X    XX",
	"X           X     X",
	"X   X          X  X",
	"Xm  X             X",
	"XXXXXXXXXXXXXXXXXXX"},

	{
	"XXXXXXXXXXXXXXXXXXX",
	"X         X X     X",
	"X       X   bXXX  X",
	"XX   X X    X     X",
	"X                 X",
	"X       XX  X  XXXX",
	"X      X  XX     XX",
	"X            XXXX X",
	"X         W       X",
	"XX         XXXXX  X",
	"X     X        X XX",
	"X        X  XX    X",
	"X X   X           X",
	"X  X              X",
	"Xm  X      XX     X",
	"X  X  X    X   X  X",
	"X X               X",
	"X       X  X      X",
	"X XX   Xv   X     X",
	"Xz  X X  X  X X   X",
	"XXXXXXXXXXXXXXXXXXX"}
	};


	// Constructor
	Warrior()
	{
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setBackground(Color.BLACK);

		addKeyListener(this);
		setFocusable(true);

		// load enemy images
		wallImage 		= new ImageIcon(getClass().getResource("./wall.png")).getImage();
		batImage 		= new ImageIcon(getClass().getResource("./bat.png")).getImage();
		vampireImage 		= new ImageIcon(getClass().getResource("./Dracula2.png")).getImage();
		mummyImage 		= new ImageIcon(getClass().getResource("./mummy2.png")).getImage();
		zombieImage 		= new ImageIcon(getClass().getResource("./zombie.png")).getImage();
		coinImage		= new ImageIcon(getClass().getResource("./coin.png")).getImage();

		// load player images
		warriorUpImage 		= new ImageIcon(getClass().getResource("./WarriorUp.png")).getImage();
		warriorDownImage 	= new ImageIcon(getClass().getResource("./WarriorDown.png")).getImage();
		warriorLeftImage	= new ImageIcon(getClass().getResource("./WarriorLeft.png")).getImage();
		warriorRightImage	= new ImageIcon(getClass().getResource("./WarriorRight.png")).getImage();

		loadMap();
		
		for(Block enemy : enemies)
		{
			char newDirection = directions[random.nextInt(4)];
			enemy.updateDirection(newDirection);
		}

		gameLoop		= new Timer(50, this); // 20fps (1000/50)
		gameLoop.start();

		/*frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
                		if(score > highScore)
				{
					saveHighScore(score);
				}
				frame.dispose();            
                       		System.exit(0); // Use this to terminate the entire app
            		}
		});*/
	} // end constructor

	public void loadMap()
	{
		//initialize the new HashSets
		walls = new HashSet<Block>();
		coins = new HashSet<Block>();
		enemies = new HashSet<Block>();

		// Iterate through the map. I'm using row and column instead of r and c as in the video tutorial.		
		for(int row = 0; row < rowCount; row++)
		{
			for(int column = 0; column < columnCount; column++)
			{
				String rowPosition = tileMap[level][row];
				char tileMapChar = rowPosition.charAt(column);
				
				int x = column * tileSize;
				int y = row * tileSize;

				if(tileMapChar == 'X')
				{
					Block wall = new Block(wallImage, x, y, tileSize, tileSize);
					walls.add(wall);
				}
				else if(tileMapChar == 'b')
				{
					Block bat = new Block(batImage, x, y, tileSize, tileSize);
					enemies.add(bat);
				}
				else if(tileMapChar == 'm')
				{
					Block mummy = new Block(mummyImage, x, y, tileSize, tileSize);
					enemies.add(mummy);
				}
				else if(tileMapChar == 'v')
				{
					Block vampire = new Block(vampireImage, x, y, tileSize, tileSize);
					enemies.add(vampire);
				}
				else if(tileMapChar == 'z')
				{
					Block zombie = new Block(zombieImage, x, y, tileSize, tileSize);
					enemies.add(zombie);
				}
				else if(tileMapChar == 'W')
				{
					warrior = new Block(warriorRightImage, x, y, tileSize, tileSize);
				}
				else if(tileMapChar == ' ')
				{
					Block coin = new Block(coinImage, x + 14, y + 14, 4, 4);
					coins.add(coin);
				}

			} // end inner for loop
		} // end outer for loop

	} // end method loadMap()

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g)
	{
		g.drawImage(warrior.image, warrior.x, warrior.y, warrior.width, warrior.height, null);

		for(Block enemies : enemies)
		{
			g.drawImage(enemies.image, enemies.x, enemies.y, enemies.width, enemies.height, null);
		}

		for(Block walls : walls)
		{
			g.drawImage(walls.image, walls.x, walls.y, walls.width, walls.height, null);
		}

		for(Block coins : coins)
		{
			g.drawImage(coins.image, coins.x, coins.y, coins.width, coins.height, null);

		}

		// Display score
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		if(gameOver)
		{
			g.drawString("Game Over: " + String.valueOf(score), tileSize / 2, tileSize / 2);
			if(score > highScore)
			{
				saveHighScore(score);
				highScore = score;
				g.drawString("New high score: " + String.valueOf(score), 200, tileSize / 2);
			}
			score = 0;
			extraLifeScore = 0;	
		}
		else
		{
			g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize / 2, tileSize / 2);
			if(score > highScore)
			{
				highScore = score;
			}
			g.drawString("High Score: " + String.valueOf(highScore), 150, tileSize / 2);
		}
	}

	public void move()
	{
		warrior.x += warrior.velocityX;
		warrior.y += warrior.velocityY;

		for(Block wall : walls)
		{
			if(collision(warrior, wall))
			{
				// undo the movement if the warrior collides with a wall
				warrior.x -= warrior.velocityX;
				warrior.y -= warrior.velocityY;

				// break terminates the loop early if a collision was found so that the for loop doesn't need to continue iterating over the walls HashSet
				break;
			}
		}

		for(Block enemy : enemies)
		{
			// Check if the enemies have collided with the warrior
			if(collision(enemy, warrior))
			{
				lives -= 1;
				resetPositions();
				if(lives == 0)
				{
					gameOver = true;
					return;
				}
			}
			char newDirection;
			if(numTimes == 3)
			{
				newDirection = directions[random.nextInt(4)];
				enemy.updateDirection(newDirection);
				numTimes = 0;
			}
			numTimes += 1;
			enemy.x += enemy.velocityX;
			enemy.y += enemy.velocityY;
			
			for(Block wall : walls)
			{
				if(collision(enemy, wall))
				{
					enemy.x -= enemy.velocityX;
					enemy.y -= enemy.velocityY;
					newDirection = directions[random.nextInt(4)];
					enemy.updateDirection(newDirection);
				}
			}
			if(enemy.x < 0)
			{
				enemy.x = boardWidth;

			}
			else if(enemy.x > boardWidth)
			{
				enemy.x = 0;
			}
		}

		if(warrior.x < 0)
		{
			warrior.x = boardWidth;
		}
		else if(warrior.x > boardWidth)
		{
			warrior.x = 0;
		}

		// Check for player collision with coins
		Block coinCollected = null;
		for(Block coin : coins)
		{
			if(collision(warrior, coin))
			{
				coinCollected = coin;
				score += 10;
				extraLifeScore += 10;
				if(extraLifeScore >= 2000)
				{
					lives += 1;
					extraLifeScore = 0;
				}
			}
		}
		coins.remove(coinCollected);

		// Check to see if all the coins have been collected
		if(coins.isEmpty())
		{
			// For now, just reload the same map and reset the positions
			
			level += 1;
			if(level == tileMap.length)
			{
				level = 0;
			}
			loadMap();
			resetPositions();
		}
	}

	public boolean collision(Block a, Block b)
	{
		return	a.x < b.x + b.width &&
			a.x + a.width > b.x &&
			a.y < b.y + b.height &&
			a.y + a.height > b.y;
		
	}
	
	public void resetPositions()
	{
		warrior.reset();
		// After dying, prevent the player from moving until an arrow key is struck
		warrior.velocityX = 0;
		warrior.velocityY = 0;
		
		// Do the same for the enemies
		for(Block enemy : enemies)
		{
			enemy.reset();
			// Just give the enemies new directions
			char newDirection = directions[random.nextInt(4)];
			enemy.updateDirection(newDirection);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		move();
		repaint();
		// If game over, stop drawing and moving the warrior
		if(gameOver)
		{
			gameLoop.stop();
		}
	}

	// KeyTyped and KeyPressed are not needed and not used.
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	// The method we will be using is keyReleased
	@Override
	public void keyReleased(KeyEvent e)
	{
		// Any key should restart a new game after game over
		if(gameOver)
		{
			level = 0;
			loadMap(); // adds the coins back to the HashSet
			resetPositions(); // This gives each enemy a new direciton
			lives = 3;
			gameOver = false;
			gameLoop.start();
		}
		if(e.getKeyCode() == KeyEvent.VK_UP)
		{
			warrior.updateDirection('U');
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			warrior.updateDirection('D');
		}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			warrior.updateDirection('L');
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			warrior.updateDirection('R');
		}
		else if(e.getKeyCode() == KeyEvent.VK_P)
		{
			if(!paused)
			{
				gameLoop.stop();
				paused = true;
			}
			else
			{
				gameLoop.start();
				paused = false;
			}
		}

		if(warrior.direction == 'U')
		{
			warrior.image = warriorUpImage;
		}
		else if(warrior.direction == 'D')
		{
			warrior.image = warriorDownImage;
		}
		else if(warrior.direction == 'L')
		{
			warrior.image = warriorLeftImage;
		}
		else if(warrior.direction == 'R')
		{
			warrior.image = warriorRightImage;
		}


	} // end keyReleased function

	// method loadHighScore reads the highScore.txt file and returns it to the caller.
	public int loadHighScore()
	{
        	File file = new File(FILE_NAME);
        	if(!file.exists())
		{
 			return 0; // Return 0 if no file exists yet
		}

        	try (Scanner scanner = new Scanner(file))
		{
            		if (scanner.hasNextInt())
 			{
                		return scanner.nextInt();
            		}
        	}
		catch (FileNotFoundException e)
		{
           		System.err.println("High score file not found.");
        	}
        	return 0;
    	}

	// saveHighScore saves the high score to a file
	public void saveHighScore(int currentScore)
	{
        	try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME)))
		{
            		writer.print(currentScore);
            		System.out.println("New high score saved: " + score);
        	}
 		catch (IOException e)
		{
            		System.err.println("Error saving high score: " + e.getMessage());
        	}
    	}

} // end Warrior class