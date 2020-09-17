package gui;

import algorithms.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimpleGUI extends Application {

	public static final int MAX_BAR_SIZE = 1000;                                // The maximum height of a bar
	private static final int MAX_ARRAY_SIZE = 5000;                             // The maximum size of the array
	private final Canvas canvas = new Canvas();                                 // The canvas to render the bars to
	private final ComboBox<String> algorithmChoices = new ComboBox<>();         // ComboBox to display algorithm choices in
	private final Button randomize = new Button("Randomize Array");          // Button to allow for randomizing an array at te current size
	private final Button startButton = new Button("Start Sorting");          // Button to start sorting the array
	private final Button stopButton = new Button("Stop Sorting");            // Button to stop sorting the array
	private final Slider arraySizeSlider = new Slider();                        // Slider to choose the size of the array
	private int[] arrayToSort = new int[0];                                     // The array to be sorted by the algorithm
	private Algorithm algorithm = null;                                         // The algorithm currently running
	private ScheduledExecutorService executorService = Executors.               // A scheduler to execute the algorithm at specified intervals
			newSingleThreadScheduledExecutor();

	@Override
	public void start(Stage primaryStage) {
		// Adjust the range of the slider and set it in the middle. Then create a new array given this value
		arraySizeSlider.setMax(MAX_ARRAY_SIZE);
		arraySizeSlider.setValue(MAX_ARRAY_SIZE / 2.0);
		newRandomArray();
		// Create the main layout, VBox, and a sub layout, HBox
		VBox layout = new VBox();
		HBox subLayout = new HBox();
		// Connect the stages width and height properties to the resize children function
		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> resizeChildren(primaryStage));
		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> resizeChildren(primaryStage));
		// Connect the sliders value property to the resizing of the array and rendering the new array in real time
		arraySizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> newRandomArray());
		// Connect the start button and the stop button to their respective functions
		startButton.setOnMouseClicked(EventHandler -> startAlgorithm());
		stopButton.setOnMouseClicked(EventHandler -> stopAlgorithm(false));
		randomize.setOnMouseClicked(EventHandler -> newRandomArray());
		// Add all of the algorithm choices to the combo box and make the default selection the first string
		algorithmChoices.getItems().addAll("Bogo Sort", "Bubble Sort", "Comb Sort", "Gnome Sort", "Cocktail Sort",
				"Selection Sort", "Double Selection Sort", "Insertion Sort", "Radix Sort", "Counting Sort", "Merge Sort",
				"Heap Sort");
		algorithmChoices.getSelectionModel().selectFirst();
		// Add all of the top bar elements to the HBox sub layout and then add the sub-layout and the canvas to the main layout
		subLayout.getChildren().addAll(algorithmChoices, randomize, arraySizeSlider, startButton, stopButton);
		layout.setPadding(new Insets(0, 10, 10, 0));
		layout.getChildren().add(subLayout);
		layout.getChildren().add(canvas);
		// Create a new scene with the base layout and set the stages scene
		Scene scene = new Scene(layout, 500, 500);
		primaryStage.setScene(scene);
		// Set the window title for the stage and show it to the user
		primaryStage.setTitle("Sorting Algorithms Visualized");
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		// If an algorithm was running at the time of close then stop its execution
		if (!executorService.isShutdown())
			executorService.shutdownNow();
	}

	private void resizeChildren(Stage primaryStage) {
		// Determine the minimum width of each of the top bar GUI elements
		startButton.setMinWidth(primaryStage.getWidth() * .1);
		stopButton.setMinWidth(primaryStage.getWidth() * .1);
		randomize.setMinWidth(primaryStage.getWidth() * .1);
		arraySizeSlider.setMinWidth(primaryStage.getWidth() * .6);
		algorithmChoices.setMinWidth(primaryStage.getWidth() * .1);
		// Then scale the canvas to contain all empty space
		canvas.widthProperty().setValue(primaryStage.getWidth());
		canvas.heightProperty().setValue(primaryStage.getHeight() * .98);
		// Then render the frame again as we have resized
		renderFrame();
	}

	public void renderFrame() {
		// Get the graphics context for this canvas, and clear the screen before we start drawing again
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		// Then determine thr width of each of the bars
		double rectWidth = (canvas.getWidth() / arrayToSort.length) * .99;

		for (int i = 0; i < arrayToSort.length; i++) {
			// Get the height of the bars depending on the height of the window, and find the color gradient
			double rectHeight = (canvas.getHeight() / MAX_BAR_SIZE) * arrayToSort[i];
			double gradient = ((double) arrayToSort[i] / MAX_BAR_SIZE) * 255;
			// Draw a rectangle whose color is based on its height, green for shorter bars and blue for taller ones
			gc.setFill(Color.rgb((int) (255 - gradient), (int) (gradient * .5), (int) gradient));
			gc.fillRect(i * rectWidth, canvas.getHeight() - rectHeight, rectWidth, rectHeight);
		}
	}

	public void stopAlgorithm(boolean isCorrect) {
		if (algorithm != null) {
			// If an algorithm was running then cancel the recurring call, set is running to false, dereference the algorithm
			executorService.shutdownNow();
			algorithm = null;
			// Then show the user a notification to let them know if we sorted the array right!
			Alert notification = new Alert(Alert.AlertType.INFORMATION);
			notification.setHeaderText("Algorithm Finished Sorting!");
			notification.setContentText("Algorithm Sorted Array Correctly: " + isCorrect);
			notification.showAndWait();
		}
	}

	public void newRandomArray() {
		if (algorithm == null) {
			// Generate a new array based on the sliders value given that an algorithm is not running
			arrayToSort = new int[(int) (arraySizeSlider.getValue())];
			Random random = new Random();
			// Iterate over the new array and fill it with random values from the random object
			for (int i = 0; i < arrayToSort.length; i++)
				arrayToSort[i] = random.nextInt(MAX_BAR_SIZE);
			// Then render the new array to the screen
			renderFrame();
		}
	}

	private void startAlgorithm() {
		int[] correctArray = arrayToSort.clone();
		// Get the string of the chosen algorithm that the user chose and create a new algorithm object of that class
		switch (algorithmChoices.getValue()) {
			case "Bogo Sort" -> algorithm = new Bogo(this, arrayToSort);
			case "Bubble Sort" -> algorithm = new Bubble(this, arrayToSort);
			case "Comb Sort" -> algorithm = new Comb(this, arrayToSort);
			case "Gnome Sort" -> algorithm = new Gnome(this, arrayToSort);
			case "Cocktail Sort" -> algorithm = new Cocktail(this, arrayToSort);
			case "Selection Sort" -> algorithm = new Selection(this, arrayToSort);
			case "Double Selection Sort" -> algorithm = new DoubleSelection(this, arrayToSort);
			case "Radix Sort" -> algorithm = new Radix(this, arrayToSort);
			case "Counting Sort" -> algorithm = new Counting(this, arrayToSort);
			case "Insertion Sort" -> algorithm = new Insertion(this, arrayToSort);
			case "Heap Sort" -> algorithm = new Heap(this, arrayToSort);
		}
		// Then create a new scheduled executioner and then start executing it
		executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(algorithm, 0, 1, TimeUnit.MILLISECONDS);
	}
}

class Runner {
	public static void main(String[] args) {
		Application.launch(SimpleGUI.class, args);       // Application entry point
	}
}
