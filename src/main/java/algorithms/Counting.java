package algorithms;

import gui.SimpleGUI;

import java.util.Collections;
import java.util.HashMap;

public class Counting extends Algorithm {

    private final HashMap<Integer, Integer> occurrences = new HashMap<>();
    private boolean isRebuilding = false;
    private int rebuildPointer = 0;

    public Counting(SimpleGUI graphics, int[] toSort) {
        super(graphics, toSort);
    }

    @Override
    public void run() {
        super.run();

        if (!isRebuilding) {
            // Get the value at the pointer and get the number of times we have seen it before
            int value = sorting[pointer];
            int seenNTimes = occurrences.getOrDefault(value, 0);
            // Then reassign the number to the number of times we've seen it plus one
            occurrences.put(value, seenNTimes + 1);
            // If the pointer is at the end of the array then we need to start rebuilding
            if (pointer == sorting.length - 1)
                isRebuilding = true;
                // Otherwise just move the pointer forward
            else
                pointer++;
        } else {
            // If we are rebuilding then get the minimum key in the dictionary and get its repeats
            int minValue = Collections.min(occurrences.keySet());
            int repeats = occurrences.get(minValue);
            // If the number of repeats is more than 1 then write back repeats - 1
            if (1 < repeats)
                occurrences.put(minValue, repeats - 1);
                // Otherwise this is the last occurrence of the element and we can remove it from the key set
            else
                occurrences.remove(minValue);
            // Place the min value at the pointer and increment the index
            sorting[rebuildPointer] = minValue;
            rebuildPointer++;
        }
    }

    @Override
    protected boolean isDoneSorting() {
        return rebuildPointer == sorting.length;
    }

    @Override
    protected boolean isArrayAltered() {
        return isRebuilding;
    }
}
