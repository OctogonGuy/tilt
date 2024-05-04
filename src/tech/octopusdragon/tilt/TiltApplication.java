package tech.octopusdragon.tilt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Allows the user to play a game of Tilt in a GUI.
 * @author Alex Gill
 *
 */
public class TiltApplication extends Application {
	
	private static final Image SPACE_IMAGE = new Image(TiltApplication.class.getClassLoader().getResourceAsStream("space.png"));
	private static final Image HOLE_IMAGE = new Image(TiltApplication.class.getClassLoader().getResourceAsStream("hole.png"));
	private static final Image GREEN_SLIDER_IMAGE = new Image(TiltApplication.class.getClassLoader().getResourceAsStream("green_slider.png"));
	private static final Image BLUE_SLIDER_IMAGE = new Image(TiltApplication.class.getClassLoader().getResourceAsStream("blue_slider.png"));
	private static final Image BLOCKER_IMAGE = new Image(TiltApplication.class.getClassLoader().getResourceAsStream("blocker.png"));
	private static final Image UP_ARROW_IMAGE = new Image(TiltApplication.class.getClassLoader().getResourceAsStream("up_arrow.png"));
	private static final Image DOWN_ARROW_IMAGE = new Image(TiltApplication.class.getClassLoader().getResourceAsStream("down_arrow.png"));
	private static final Image LEFT_ARROW_IMAGE = new Image(TiltApplication.class.getClassLoader().getResourceAsStream("left_arrow.png"));
	private static final Image RIGHT_ARROW_IMAGE = new Image(TiltApplication.class.getClassLoader().getResourceAsStream("right_arrow.png"));
	private static final double ANIMATION_DURATION = 220.0;
	private static final double SPACING = 2.0;
	
	private StackPane root;			// Root element
	private BorderPane borderPane;	// Main screen
	private MenuBar menuBar;		// The menu bar
	private Menu gameMenu;			// The game menu
	private MenuItem newGameItem;	// New game menuItem
	private MenuItem restartItem;	// Restart current level menuItem
	private MenuItem undoItem;		// Undo last move menuItem
	private MenuItem howToPlayItem;	// How to play menuItem
	private GridPane board;			// The whole board including grid and arrows and number of moves label
	private Label numMovesLabel;	// Label that shows number of moves
	private StackPane upArrow;		// Up arrow
	private ImageView upArrowIV;	// Up arrow image view
	private StackPane downArrow;	// Down arrow
	private ImageView downArrowIV;	// Down arrow image view
	private StackPane leftArrow;	// Left arrow
	private ImageView leftArrowIV;	// Left arrow image view
	private StackPane rightArrow;	// Right arrow
	private ImageView rightArrowIV;	// Right arrow image view
	private GridPane gridPane;		// Grid pane for grid
	private StackPane[][] grid;		// A grid of space images and piece images
	private ImageView[][] spaces;	// Space images
	private ImageView[][] pieces;	// Piece images
	
	private Game game;	// The game
	private int lastLevel;	// The last level the user played
	private boolean arrowsDisabled;	// Whether the arrows are disabled
	private boolean animationPlaying;	// Whether an animation is playing
	
	@Override
	public void init() {
		animationPlaying = false;
	}

	@Override
	public void start(Stage primaryStage) {
		
		// Build the GUI
		buildGui();
		
		// Show the stage
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getClassLoader().getResource("styles.css").toExternalForm());
		createKeyBindings(scene);	// Create key bindings
		primaryStage.setScene(scene);
		primaryStage.setTitle("Tilt");
		primaryStage.setResizable(false);
		
		newGameDialog();
		
		primaryStage.show();
	}
	
	/**
	 * Builds the GUI
	 */
	public void buildGui() {
		gridPane = new GridPane();
		grid = new StackPane[Grid.ROWS][Grid.COLUMNS];
		spaces = new ImageView[Grid.ROWS][Grid.COLUMNS];
		pieces = new ImageView[Grid.ROWS][Grid.COLUMNS];
		for (int i = 0; i < Grid.ROWS; i++) {
			for (int j = 0; j < Grid.COLUMNS; j++) {
				grid[i][j] = new StackPane();
				spaces[i][j] = new ImageView(SPACE_IMAGE);
				grid[i][j].getChildren().add(spaces[i][j]);
				pieces[i][j] = new ImageView();
				grid[i][j].getChildren().add(pieces[i][j]);
				gridPane.add(grid[i][j], j, i);
			}
		}
		
		upArrowIV = new ImageView(UP_ARROW_IMAGE);
		upArrow = new StackPane(upArrowIV);
		upArrow.getStyleClass().add("arrow");
		upArrow.setOnMouseClicked(e -> {
			tilt(Direction.UP);
		});
		downArrowIV = new ImageView(DOWN_ARROW_IMAGE);
		downArrow = new StackPane(downArrowIV);
		downArrow.getStyleClass().add("arrow");
		downArrow.setOnMouseClicked(e -> {
			tilt(Direction.DOWN);
		});
		leftArrowIV = new ImageView(LEFT_ARROW_IMAGE);
		leftArrow = new StackPane(leftArrowIV);
		leftArrow.getStyleClass().add("arrow");
		leftArrow.setOnMouseClicked(e -> {
			tilt(Direction.LEFT);
		});
		rightArrowIV = new ImageView(RIGHT_ARROW_IMAGE);
		rightArrow = new StackPane(rightArrowIV);
		rightArrow.getStyleClass().add("arrow");
		rightArrow.setOnMouseClicked(e -> {
			tilt(Direction.RIGHT);
		});
		
		numMovesLabel = new Label();
		
		board = new GridPane();
		board.setHgap(SPACING);
		board.setVgap(SPACING);
		board.add(gridPane, 1, 1);
		board.add(upArrow, 1, 0);
		board.add(rightArrow, 2, 1);
		board.add(downArrow, 1, 2);
		board.add(leftArrow, 0, 1);
		board.add(numMovesLabel, 0, 3);
		GridPane.setColumnSpan(numMovesLabel, 3);
		GridPane.setHalignment(numMovesLabel, HPos.RIGHT);
		
		menuBar = new MenuBar();
		gameMenu = new Menu("Game");
		newGameItem = new MenuItem("New Game");
		newGameItem.setAccelerator(KeyCombination.keyCombination("n"));
		newGameItem.setOnAction(e -> {
			newGameDialog();
		});
		gameMenu.getItems().add(newGameItem);
		restartItem = new MenuItem("Restart");
		restartItem.setAccelerator(KeyCombination.keyCombination("r"));
		restartItem.setOnAction(e -> {
			newGame(lastLevel);
		});
		gameMenu.getItems().add(restartItem);
		undoItem = new MenuItem("Undo");
		undoItem.setAccelerator(KeyCombination.keyCombination("u"));
		undoItem.setOnAction(e -> {
			undo();
		});
		gameMenu.getItems().add(undoItem);
		howToPlayItem = new MenuItem("How to Play");
		howToPlayItem.setOnAction(e -> {
			howToPlayDialog();
		});
		gameMenu.getItems().add(howToPlayItem);
		menuBar.getMenus().add(gameMenu);
		
		borderPane = new BorderPane();
		borderPane.setCenter(board);
		borderPane.setTop(menuBar);
		
		root = new StackPane(borderPane);
	}
	
	/**
	 * Updates the pieces on the grid
	 */
	public void updateGrid() {
		for (int i = 0; i < Grid.ROWS; i++) {
			for (int j = 0; j < Grid.COLUMNS; j++) {
				Image newImage;
				
				switch (game.getSpace(i, j)) {
				case HOLE:
					newImage = HOLE_IMAGE;
					break;
				case GREEN_SLIDER:
					newImage = GREEN_SLIDER_IMAGE;
					break;
				case BLUE_SLIDER:
					newImage = BLUE_SLIDER_IMAGE;
					break;
				case BLOCKER:
					newImage = BLOCKER_IMAGE;
					break;
				default:
					newImage = null;
				}
				
				if (newImage != pieces[i][j].getImage()) {
					pieces[i][j].setImage(newImage);
				}
			}
		}
		
		numMovesLabel.setText("Moves made: " + game.getNumMoves());
	}
	
	/**
	 * Sets arrow key bindings
	 * @param scene The scene
	 */
	public void createKeyBindings(Scene scene) {
		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case UP:
			case W:
				tilt(Direction.UP);
				break;
			case DOWN:
			case S:
				tilt(Direction.DOWN);
				break;
			case LEFT:
			case A:
				tilt(Direction.LEFT);
				break;
			case RIGHT:
			case D:
				tilt(Direction.RIGHT);
				break;
			default:
				break;
			}
		});
	}
	
	/**
	 * Tilts the game grid and updates the board.
	 * @param direction The direction to tilt
	 */
	public void tilt(Direction direction) {
		if (arrowsDisabled)
			return;
		
		FromTo[] movements = game.tilt(direction);
		
		Animation tiltAnimation = tiltAnimation(movements);
		tiltAnimation.play();
	}
	
	/**
	 * Displays an animation showing movements of sliders
	 * @param movements The movements
	 */
	public Animation tiltAnimation(FromTo[] movements) {
		ParallelTransition allMovements = new ParallelTransition();
		
		for (FromTo movement : movements) {
			StackPane fromGridSquare = grid[movement.getFromRow()][movement.getFromColumn()];
			Bounds fromBounds = fromGridSquare.localToScene(fromGridSquare.getBoundsInLocal());
			StackPane toGridSquare = grid[movement.getToRow()][movement.getToColumn()];
			Bounds toBounds = toGridSquare.localToScene(toGridSquare.getBoundsInLocal());
			ImageView fromPiece = pieces[movement.getFromRow()][movement.getFromColumn()];

			ImageView piece = new ImageView(fromPiece.getImage());
			StackPane.setAlignment(piece, Pos.TOP_LEFT);
			root.getChildren().add(piece);
			fromPiece.setImage(null);
			
			// Calculate the duration of the animation based on the number of
			// spaces moved
			int spacesMoved = 0;
			spacesMoved += Math.abs(movement.getToRow() - movement.getFromRow());
			spacesMoved += Math.abs(movement.getToColumn() - movement.getFromColumn());
			
			TranslateTransition moveAnimation = new TranslateTransition(
					Duration.millis(ANIMATION_DURATION * spacesMoved),
					piece);
			moveAnimation.setFromX(fromBounds.getMinX());
			moveAnimation.setFromY(fromBounds.getMinY());
			moveAnimation.setToX(toBounds.getMinX());
			moveAnimation.setToY(toBounds.getMinY());
			allMovements.getChildren().add(moveAnimation);
			
			// If the slider lands in a hole, remove it immediately
			if (game.getSpace(movement.getToRow(), movement.getToColumn()) == Space.HOLE) {
				moveAnimation.setOnFinished(e -> {
					root.getChildren().remove(piece);
				});
			}
		}

		allMovements.setOnFinished(e -> {
			updateGrid();
			animationPlaying = false;
			List<Node> nodesToRemove = new ArrayList<Node>();
			for (Node node : root.getChildren()) {
				if (node != borderPane) {
					nodesToRemove.add(node);
				}
			}
			for (Node node : nodesToRemove) {
				root.getChildren().remove(node);
			}
			
			// End the game if it is over
			if (game.isOver()) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						endGame();
					}
				});
			}
			else {
				enableArrows();
			}
		});
		disableArrows();
		animationPlaying = true;
		
		return allMovements;
	}
	
	/**
	 * Undos the last move
	 */
	public void undo() {
		
		if (animationPlaying || game.gameState() == GameState.WIN)
			return;
		
		game.undo();
		
		updateGrid();
		if (arrowsDisabled)
			enableArrows();
	}
	
	/**
	 * Disables the tilt arrows
	 */
	public void disableArrows() {
		arrowsDisabled = true;
		
		upArrow.getStyleClass().remove("active");
		downArrow.getStyleClass().remove("active");
		leftArrow.getStyleClass().remove("active");
		rightArrow.getStyleClass().remove("active");
	}
	
	/**
	 * Enables the tilt arrows
	 */
	public void enableArrows() {
		arrowsDisabled = false;
		
		upArrow.getStyleClass().add("active");
		downArrow.getStyleClass().add("active");
		leftArrow.getStyleClass().add("active");
		rightArrow.getStyleClass().add("active");
	}
	
	/**
	 * Starts a new game of tilt
	 */
	public void newGame(int level) {
		
		PieceLocation[] gridPieces = readLevel(String.format("level%02d.txt", level));
		game = new Game(gridPieces);
		
		// Turn on arrows
		enableArrows();
		
		updateGrid();
	}
	
	/**
	 * Shows a dialog in which the user can select the level to play
	 */
	public void newGameDialog() {

		// Create the buttons
		ButtonType startButtonType = new ButtonType("Start", ButtonData.OK_DONE);
		ButtonType exitButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		
		// Create the level spinner
		Spinner<Integer> levelSpinner = new Spinner<Integer>(1, 40, lastLevel);
		levelSpinner.setEditable(true);
		
		// Create the dialog
		Alert dialog = new Alert(AlertType.CONFIRMATION, null, startButtonType, exitButtonType);
		dialog.setTitle("Tilt - New Game");
		dialog.setHeaderText("What level do you want to play?");
		dialog.getDialogPane().setContent(levelSpinner);
		
		// Standby and act depending on the user's choice
		dialog.showAndWait().ifPresent(response -> {
			
			// If start, create a new game of the given level
			if (response == startButtonType) {
				lastLevel = levelSpinner.getValue();
				newGame(levelSpinner.getValue());
			}
		});
	}
	
	/**
	 * Shows a dialog with instructions on how to play
	 */
	public void howToPlayDialog() {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("how_to_play.txt");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader in = new BufferedReader(inputStreamReader);
		String howToPlayStr = "";
		try {
			for (String line; (line = in.readLine()) != null;) {
				howToPlayStr += line;
			}
		} catch (IOException e) {
			System.out.println("Error trying to read how to play text file...");
			e.printStackTrace();
		}
		
		TextArea instructions = new TextArea(howToPlayStr);
		instructions.setPrefColumnCount(40);
		instructions.setPrefRowCount(6);
		instructions.setEditable(false);
		instructions.setWrapText(true);
		
		Alert dialog = new Alert(AlertType.INFORMATION);
		dialog.setTitle("Tilt - How to Play");
		dialog.setHeaderText("Instructions");
		dialog.getDialogPane().setContent(instructions);
		dialog.showAndWait();
	}
	
	/**
	 * Ends the current game
	 */
	public void endGame() {
		
		// Print game ending message
		String header = "unspecified game end header";
		String message = "unspecified game end message";
		if (game.gameState() == GameState.WIN) {
			header = "You win!";
			message = "You solved the level in " + game.getNumMoves() + " moves.";
		}
		else if (game.gameState() == GameState.LOSE) {
			header = "You lose...";
			message = "You lost in " + game.getNumMoves() + " moves.";
		}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game Over");
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
		
		// Turn off arrows
		disableArrows();
	}
	
	/**
	 * Reads a level layout from a text file and returns the pieces and their
	 * positions
	 */
	public PieceLocation[] readLevel(String filename) {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader in = new BufferedReader(inputStreamReader);
		
		List<PieceLocation> pieceLocations = new ArrayList<PieceLocation>();
		
		try {
			
			int row = 0;
			for (String line; (line = in.readLine()) != null;) {

				for (int column = 0; column < Grid.COLUMNS; column++) {
					Space spaceType;
					
					switch (line.charAt(column)) {
					case 'g':
						spaceType = Space.GREEN_SLIDER;
						break;
					case 'b':
						spaceType = Space.BLUE_SLIDER;
						break;
					case '#':
						spaceType = Space.BLOCKER;
						break;
					default:
						continue;
					}
					
					pieceLocations.add(new PieceLocation(spaceType, row, column));
				}
				
				row++;
			}
			
		} catch (IOException e) {
			System.out.println("Error trying to read level text file...");
			e.printStackTrace();
		}
		
		return pieceLocations.toArray(new PieceLocation[0]);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
