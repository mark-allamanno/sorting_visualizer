package algorithms;

import gui.SimpleGUI;

public class Cocktail extends Algorithm {

    private int upperBound;                 // The upperbound, aka the last known unsorted element
    private int lowerBound;
    private boolean isReversing = false;

    public Cocktail(SimpleGUI graphics, int[] toSort) {
        super(graphics, toSort);
        upperBound = sorting.length - 2;
    }

    @Override
    public void run() {
        super.run();
        // If the element ahead of the pointer is less than the element at the pointer then swap them
        if (sorting[pointer + 1] < sorting[pointer])
            super.swapArrayElements(pointer, pointer + 1);

        if (!isReversing) {
            // If the pointer is less than the upper bound then we increment as normal
            if (pointer < upperBound)
                pointer++;
                // Otherwise we are at the upper bound and we must decrease the upper bound index and reset the pointer
            else {
                upperBound--;
                isReversing = true;
            }
        } else {
            // If the pointer is less than the upper bound then we increment as normal
            if (pointer > lowerBound)
                pointer--;
                // Otherwise we are at the upper bound and we must decrease the upper bound index and reset the pointer
            else {
                lowerBound++;
                isReversing = false;
            }
        }
    }

    @Override
    protected boolean isDoneSorting() {
        return upperBound <= lowerBound;
    }

    @Override
    protected boolean isArrayAltered() {
        return sorting[pointer + 1] < sorting[pointer];
    }
}
