package algorithms;

import gui.SimpleGUI;

public class Gnome extends Algorithm {

    private boolean swapOccurred = false;

    public Gnome(SimpleGUI graphics, int[] toSort) {
        super(graphics, toSort);
    }

    @Override
    public void run() {
        super.run();
        // If the Gnome is at the beginning of his pots then move him forward
        if (pointer == 0) {
            swapOccurred = false;
            pointer++;
        }
        // Otherwise if the element to his left is greater then the element to his right then swap them and move back
        else if (sorting[pointer] < sorting[pointer - 1]) {
            super.swapArrayElements(pointer, pointer - 1);
            swapOccurred = true;
            pointer--;
        }
        // Otherwise they are as they should be amd he can move forward
        else {
            swapOccurred = false;
            pointer++;
        }
    }

    @Override
    protected boolean isArrayAltered() {
        return swapOccurred;
    }

    @Override
    protected boolean isDoneSorting() {
        return pointer == sorting.length;
    }
}
