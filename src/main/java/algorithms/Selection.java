package algorithms;

import gui.SimpleGUI;

public class Selection extends Algorithm {

    private int minValueIndex = 0;          // The index of the minimum value in the array slice
    private int lowerBoundIndex = 0;        // The lower bound of selection sort

    public Selection(SimpleGUI graphics, int[] toSort) {
        super(graphics, toSort);
    }

    @Override
    public void run() {
        super.run();
        // Check to see if the element we are iterating over is less than the documented minimum value
        if (sorting[pointer] < sorting[minValueIndex])
            minValueIndex = pointer;

        if (pointer == sorting.length - 1) {
            // If we are at the last element then switch the min value found with the lower bound
            super.swapArrayElements(minValueIndex, lowerBoundIndex);
            // Then update the index pointers accordingly
            pointer = lowerBoundIndex;
            lowerBoundIndex++;
            minValueIndex = lowerBoundIndex;
        }

        pointer++;      // Otherwise we move the pointer forward
    }

    @Override
    protected boolean isDoneSorting() {
        return lowerBoundIndex == sorting.length - 1;
    }

    @Override
    protected boolean isArrayAltered() {
        return pointer == sorting.length - 1;
    }
}
