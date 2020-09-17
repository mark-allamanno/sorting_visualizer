package algorithms;

import gui.SimpleGUI;
import javafx.application.Platform;

import java.util.Arrays;

public abstract class Algorithm implements Runnable {

    protected SimpleGUI display;                // The JavaFX Application
    protected int[] sorting;                    // The array we are currently sorting
    private final int[] correctArray;           // The correct array we should get after sorting
    protected int pointer;                      // Pointer to iterate over the array with

    public Algorithm(SimpleGUI graphics, int[] toSort) {
        // Assign all of the instance attributes
        sorting = toSort;
        correctArray = toSort.clone();
        display = graphics;
        pointer = 0;
    }

    public void run() {
        // If the algorithm is done then stop the timer that calls it
        if (isDoneSorting()) {
            Arrays.sort(correctArray);
            Platform.runLater(() -> {
                display.stopAlgorithm(Arrays.equals(sorting, correctArray));
            });
        }
        // If we have altered the array on the last iteration then update the screen
        if (isArrayAltered()) {
            Platform.runLater(display::renderFrame);
        }
    }

    protected void swapArrayElements(int index1, int index2) {
        // Standard way to swap two array values
        int tmp = sorting[index1];
        sorting[index1] = sorting[index2];
        sorting[index2] = tmp;
    }

    protected abstract boolean isDoneSorting();       // Boolean to let us know when to tell the GUI to stop calling us

    protected abstract boolean isArrayAltered();      // Boolean to let use know if the array has been altered on the last iteration
}
