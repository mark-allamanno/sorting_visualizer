package algorithms;

import gui.SimpleGUI;

public class Insertion extends Algorithm {

    private int currentIndex = 0;               // The furthest pointer that represents the frontier of sorted values
    private int backTrackIndex = 0;             // The index backwards into the sorted portion
    private int elementIndex = 0;               // The index of the element we are trying to insert
    private int replacementIndex = 0;           // The current index of the replacement method
    private boolean isBackTracking = false;     // Boolean to let us know if we are currently backtracking over the array
    private boolean isReplacing = false;        // Boolean to let us know if we are currently replacing a slice of the array

    public Insertion(SimpleGUI graphics, int[] toSort) {
        super(graphics, toSort);
    }

    @Override
    public void run() {
        super.run();
        // If the algorithm is currently backtracking to find the right index then continue doing that
        if (isBackTracking)
            backTrackArray();
            // If the algorithm is currently replacing elements after it found the correct index then continue doing that
        else if (isReplacing)
            replaceArraySlice();
            // If the algorithm is currently idling then check the next element
        else
            incrementPointer();
    }

    private void backTrackArray() {
        // If the back track index has found an element less than the reference then the correct start index is back track + 1
        if (sorting[backTrackIndex] < sorting[elementIndex])
            replacementIndex = backTrackIndex + 1;
            // Or if we are at the 0th index then it is the smallest element in the array
        else if (backTrackIndex == 0)
            replacementIndex = 0;
        // Let the algorithm know if we are still back tracking or not
        isReplacing = (sorting[backTrackIndex] < sorting[elementIndex]) || backTrackIndex == 0;
        isBackTracking = !isReplacing;
        // If we are still back tracking then decrease the back track index
        if (isBackTracking)
            backTrackIndex--;
    }

    private void replaceArraySlice() {
        // If we are still working our way back to the start position then swap the replacement index with the current
        // one and increment the replacement
        if (replacementIndex < elementIndex) {
            super.swapArrayElements(replacementIndex, elementIndex);
            replacementIndex++;
        }
        // Check if we are still replacing elements in the array
        isReplacing = replacementIndex < elementIndex;
    }

    private void incrementPointer() {
        // Check to see if the next element needs to be inserted into the back of the array
        if (sorting[currentIndex + 1] < sorting[currentIndex]) {
            elementIndex = currentIndex + 1;
            backTrackIndex = currentIndex;
            isBackTracking = true;
        } else {
            currentIndex++;     // Otherwise move forward as usual
        }
    }

    @Override
    protected boolean isArrayAltered() {
        return isReplacing;
    }

    @Override
    protected boolean isDoneSorting() {
        return currentIndex == sorting.length - 1;
    }
}
