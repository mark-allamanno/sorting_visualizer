package algorithms;

import gui.SimpleGUI;

public class DoubleSelection extends Algorithm {

    private int minValueIndex = 0;          // The index of the minimum value in the array slice
    private int maxValueIndex = 0;          // The index of the maximum value in the array slice
    private int lowerBoundIndex = 0;        // The lower bound of selection sort
    private int upperBoundIndex;            // The upper bound in selection sort

    public DoubleSelection(SimpleGUI graphics, int[] toSort) {
        super(graphics, toSort);
        upperBoundIndex = sorting.length - 1;
    }

    @Override
    public void run() {
        super.run();
        // Check to see if the element we are iterating over is less than the documented minimum value
        if (sorting[pointer] < sorting[minValueIndex])
            minValueIndex = pointer;
        // Check to see if the element we are iterating over is less than the documented maximum value
        if (sorting[maxValueIndex] < sorting[pointer])
            maxValueIndex = pointer;
        // If the pointer is at the upper bound index then we are done checking
        if (pointer == upperBoundIndex) {
            // However if the lower bound held the max value BEFORE swapping then the max value is now where the min value was
            if (maxValueIndex == lowerBoundIndex) {
                super.swapArrayElements(minValueIndex, lowerBoundIndex);
                super.swapArrayElements(minValueIndex, upperBoundIndex);
            }
            // Otherwise swap the indexes normally
            else {
                super.swapArrayElements(minValueIndex, lowerBoundIndex);
                super.swapArrayElements(maxValueIndex, upperBoundIndex);
            }
            // Then update the index pointers accordingly
            pointer = lowerBoundIndex;
            lowerBoundIndex++;
            upperBoundIndex--;
            minValueIndex = lowerBoundIndex;
            maxValueIndex = lowerBoundIndex;
        }

        pointer++;  // We are always moving the pointer forward no matter what
    }

    @Override
    protected boolean isDoneSorting() {
        return lowerBoundIndex >= upperBoundIndex;
    }

    @Override
    protected boolean isArrayAltered() {
        return pointer == upperBoundIndex;
    }
}
