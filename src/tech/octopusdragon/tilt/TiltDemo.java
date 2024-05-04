package tech.octopusdragon.tilt;

import java.util.Scanner;

/**
 * Tests out the tilt game by allowing the user to play a game in the console.
 * @author Alex Gill
 *
 */
public class TiltDemo {
	
	public static void main(String[] args) {
		
		PieceLocation[] gridPieces = {
				new PieceLocation(Space.BLUE_SLIDER, 0, 0),
				new PieceLocation(Space.GREEN_SLIDER, 2, 0),
				new PieceLocation(Space.BLUE_SLIDER, 2, 1),
				new PieceLocation(Space.BLOCKER, 1, 4)
		};
		Game game = new Game(gridPieces);
		
		Scanner keyboard = new Scanner(System.in);
		String userInput;
		boolean keepGoing = true;
		
		do {
			// Print the grid
			printGrid(game);
			System.out.println("\n-----\n");
			
			// If the player won or lost, print a message and stop the game.
			if (game.gameState() == GameState.WIN) {
				System.out.println("You win!");
				keepGoing = false;
				break;
			}
			else if (game.gameState() == GameState.LOSE) {
				System.out.println("You lose...");
				keepGoing = false;
				break;
			}
			
			// Get user input
			boolean validInput = true;
			do {
				System.out.print("Enter your move: ");
				userInput = keyboard.nextLine();
				System.out.println();
				switch (userInput.toLowerCase().charAt(0)) {
				case 'u':
					game.tilt(Direction.UP);
					break;
				case 'd':
					game.tilt(Direction.DOWN);
					break;
				case 'l':
					game.tilt(Direction.LEFT);
					break;
				case 'r':
					game.tilt(Direction.RIGHT);
					break;
				case 'q':
					keepGoing = false;
					break;
				default:
					validInput = false;
				}
				
			} while (!validInput);
		} while (keepGoing);
		
		keyboard.close();
		
		System.out.println("Goodbye");
	}

	
	/**
	 * Prints a grid to the console
	 * @param grid The grid
	 */
	public static void printGrid(Game grid) {
		for (int i = 0; i < Grid.ROWS; i++) {
			for (int j = 0; j < Grid.COLUMNS; j++) {
				printSpace(grid.getSpace(i, j));
				if (j < Grid.COLUMNS - 1)
					System.out.print(" - ");
			}
			System.out.println();
			if (i < Grid.ROWS - 1) {
				for (int j = 0; j < Grid.COLUMNS - 1; j++)
					System.out.print("|   ");
				System.out.print(" | ");
				System.out.println();
			}
		}
	}
	
	
	
	/**
	 * Prints a space to the console
	 * @param space The space
	 */
	public static void printSpace(Space space) {
		char spaceChar;
		
		switch (space) {
		case GREEN_SLIDER:
			spaceChar = 'g';
			break;
		case BLUE_SLIDER:
			spaceChar = 'b';
			break;
		case BLOCKER:
			spaceChar = '□';
			break;
		case HOLE:
			spaceChar = '●';
			break;
		case EMPTY:
		default:
			spaceChar = ' ';
		}
		
		System.out.print(spaceChar);
	}
}
