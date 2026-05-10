// Programmer: Jason Ash
// Creation date: 09 May, 2026
// Description: AsciiMapGenerator.java is a utility program for generating random maps to be displayed on the console for use in the Vampire Castle java game.
// It was obtained from a Google AI search summary, and I editted it to meet the needs of making level maps for Vampire Castle.
// It is programmed in Java using Notepad and JDK 21 using the Windows Command Prompt for compiling and running it.
// Inputs: none
// Output: A map that you can use for a level in Vampire Castle. Just remember to add one each the characters 'v', 'b', 'm', 'z', and 'W' for the vampire, bat, mummy, zombie, and warrior, respectively, or else a run-time error will occur.
// I copied the map from the console by highlighting it with the mouse and copying it into Notepad before editting it to make sure all the coins (blank spaces) are reachable for the level completion scenario.
// Caution: Pressing ctrl+C in the console can have unpredicatable effects including terminating running processes. Use this operation with care.
// Run: Compile this utility program by typing javac AsciiMapGenerator.java in the console, and then run it by typing AsciiMapGenerator in the console.

import java.util.Random;

public class AsciiMapGenerator
{
    private static final int WIDTH = 19;
    private static final int HEIGHT = 21;
    private static char[][] map = new char[HEIGHT][WIDTH];
    public static void main(String[] args) {
        generateMap();
        printMap();
    }

    // Generates a random map with walls and paths
    private static void generateMap() {
        Random rand = new Random();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (y == 0 || y == HEIGHT - 1 || x == 0 || x == WIDTH - 1 || rand.nextInt(5) == 0) {
                    map[y][x] = 'X'; // Wall
                } else {
                    map[y][x] = ' '; // Coin/Floor
                }
            }
        }
     }

    // Prints the map to console
    private static void printMap()
    {
        for (int y = 0; y < HEIGHT; y++)
        {
            for (int x = 0; x < WIDTH; x++)
            {
                System.out.print(map[y][x]);
            }
            System.out.println();
        }
    }
}
