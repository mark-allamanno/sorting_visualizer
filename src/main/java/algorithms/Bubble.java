package algorithms;


import gui.SimpleGUI;

public class Bubble extends Algorithm {

    private int upperBound;     // The upperbound, aka the last known unsorted element

    public Bubble(SimpleGUI graphics, int[] toSort) {
        super(graphics, toSort);
        upperBound = sorting.length - 2;
    }

    @Override
    public void run() {
        super.run();
        // If the element ahead of the pointer is less than the element at the pointer then swap them
        if (sorting[pointer + 1] < sorting[pointer])
            super.swapArrayElements(pointer, pointer + 1);
        // If the pointer is less than the upper bound then we increment as normal
        if (pointer < upperBound)
            pointer++;
            // Otherwise we are at the upper bound and we must decrease the upper bound index and reset the pointer
        else {
            upperBound--;
            pointer = 0;
        }
    }

    @Override
    protected boolean isArrayAltered() {
        return sorting[pointer + 1] < sorting[pointer];
    }

    @Override
    protected boolean isDoneSorting() {
        return upperBound <= 0;
    }
}
