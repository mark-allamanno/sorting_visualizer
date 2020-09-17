package algorithms;

import gui.SimpleGUI;

import java.util.ArrayList;
import java.util.HashMap;


/*
	Methodology for getting the digit in a number. Lets say we are trying to get the 10's digit of 321.

	To do this I devised that you can first use modulus to strip away the '3' to solely leave 21. To do this we use our
	digits place variable to get a power of 10. In this case the digits place would be 2 (as the 10's place is the
	second digit) so we take 10 ^ 2 = 100 -> 321 % 100 = 21.

	Alright! So know we just have 21 left, were almost there!

	Then I devised that you can then take 10 ^ 1 = 10 to then isolate the 2 from the 1 to get our answer. We do this through simple
	integer division. So if we take 21 / 10 then we are left with just 2! The correct digit for our equation!

	I propose we can generalize this to saying we can take: (number) % 10 ^ digitsPlace to strip the leading digits and with the
	result of that modulus take (result) / (int) 10 ^ (digitsPlace - 1) to get our digit.
*/
public class Radix extends Algorithm {

    private final HashMap<Integer, ArrayList<Integer>> radixDigits;     // A hashmap to link digits to the sorted ints on each iteration
    private boolean isRebuilding = false;                               // A boolean to let us know if we are rebuilding the array from the counting sort
    private int digitsPlace = 1;                                        // An int to let us know what digit place we are currently comparing

    public Radix(SimpleGUI graphics, int[] toSort) {
        super(graphics, toSort);
        // Initialize the Hashmap of digit places and then fill it with 0-9 as the keys and empty lists as the values
        radixDigits = new HashMap<>();
        for (int i = 0; i < 10; i++)
            radixDigits.put(i, new ArrayList<>());
    }

    @Override
    public void run() {
        super.run();
        // If we arent rebuilding the array then fill then hashmaps with values
        if (!isRebuilding)
            radixFill();
            // Else take the values from the hashmaps and insert them back into the array by digit
        else
            radixRebuild();
    }

    private void radixFill() {
        if (pointer <= sorting.length - 1) {
            // Get the value from the original array if we arent done yet
            int value = sorting[pointer];
            // Then use modulus to reduce us so that the largest non zero digit is the place we are looking at
            int remainder = value % (int) Math.pow(10, digitsPlace);
            // Then use integer division to get the value of that digit. Check above class definition for an example
            int digit = remainder / (int) Math.pow(10, digitsPlace - 1);
            // Then place that value from the array with its digit and increment the pointer
            radixDigits.get(digit).add(value);
            pointer++;
        } else {
            // Else we are done filling and can now start rebuilding the array from the hashmaps
            isRebuilding = true;
            pointer = 0;
        }
    }

    private void radixRebuild() {
        // Iterate over all of the hashmaps until we find one that has elements to rebuild
        ArrayList<Integer> radixNums = null;
        for (ArrayList<Integer> list : radixDigits.values()) {
            if (!list.isEmpty()) {
                radixNums = list;
                break;
            }
        }

        if (radixNums == null) {
            // If the radix nums list is non existent then all array lists are empty and thus we are done rebuilding
            // and can move on to the next digit
            isRebuilding = false;
            digitsPlace++;
            pointer = 0;
        } else {
            // Otherwise we take the first array element and pop it out
            int toInsert = radixNums.get(0);
            radixNums.remove(0);
            // We then insert it at the pointer and increment the pointer to the next index
            sorting[pointer] = toInsert;
            pointer++;
        }
    }

    @Override
    protected boolean isArrayAltered() {
        return isRebuilding;                // We only alter the array during a rebuild
    }

    @Override
    protected boolean isDoneSorting() {
        return SimpleGUI.MAX_BAR_SIZE == Math.pow(10, digitsPlace - 1);            // Place Holder
    }
}
