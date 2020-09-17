package algorithms;

import gui.SimpleGUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Bogo extends Algorithm {

    private final int[] answer;
    private final ArrayList<Integer> permutation = new ArrayList<>();

    public Bogo(SimpleGUI graphics, int[] toSort) {
        super(graphics, toSort);
        // Add all of the numbers from the array to the list
        for (int num : sorting)
            permutation.add(num);
        // Get the correct array to check against
        answer = toSort.clone();
        Arrays.sort(answer);
    }

    @Override
    public void run() {
        super.run();
		// Shuffle the list to get a random permutation
        Collections.shuffle(permutation, new Random());
		// THen insert that permutation into the original array
        for (int i = 0; i < sorting.length; i++)
            sorting[i] = permutation.get(i);
    }

    @Override
    protected boolean isArrayAltered() {
        return true;
    }

    @Override
    protected boolean isDoneSorting() {
        return Arrays.equals(sorting, answer);
    }
}
