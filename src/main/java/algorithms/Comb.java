package algorithms;

import gui.SimpleGUI;

public class Comb extends Algorithm {

    private int elementGap;                     // The gap between elements in the sort
    private boolean iterationSwap = false;      // Tells us if we have swapped ANY elements when iterating over the array once

    public Comb(SimpleGUI graphics, int[] toSort) {
        super(graphics, toSort);
        elementGap = sorting.length;
    }

    @Override
    public void run() {
        super.run();
        // If we are still within the bounds of the array on the upper bound then compare the pointers
        if (pointer + elementGap < sorting.length) {
            // If the upper bounds of the sort is less than the lower bounds then swap them
            if (sorting[pointer + elementGap] < sorting[pointer]) {
                swapArrayElements(pointer, pointer + elementGap);
                iterationSwap = true;
            }
            // Always increment the pointer
            pointer++;
        }
        // Otherwise we need to restart the iteration and that means dividing the gap by 1.3, and resetting the swap boolean and pointer
        else {
            elementGap = (1 <= elementGap / 1.3) ? (int) (elementGap / 1.3) : 1;
            iterationSwap = false;
            pointer = 0;
        }
    }

    @Override
    protected boolean isDoneSorting() {
        // Comb sort is done when we are at the end of an iteration with a gap of 1, and we have made no swaps
        return elementGap == 1 && pointer == sorting.length - 1 && !iterationSwap;
    }

    @Override
    protected boolean isArrayAltered() {
        // If the upper bound pointer is within the bounds of the array return the normal comparison
        if (pointer + elementGap < sorting.length)
            return sorting[pointer + elementGap] < sorting[pointer];
            // Otherwise return false as we will not do anything this iteration anyways
        else
            return false;
    }
}
