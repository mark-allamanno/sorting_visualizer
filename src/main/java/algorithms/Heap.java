package algorithms;

import gui.SimpleGUI;

public class Heap extends Algorithm {

    private final MaxBinaryHeap heap;       // The heap object that we use to sort the array

    public Heap(SimpleGUI graphics, int[] toSort) {
        super(graphics, toSort);
        heap = new MaxBinaryHeap(toSort);
    }

    @Override
    public void run() {
        super.run();
        // If we have not formed a complete binary heap yet continue to do so
        if (!heap.isCompleteHeap())
            heap.createHeap();
            // If we have moved an element in the heap and need to restructure it then do so
        else if (!heap.isMaintainedHeap())
            heap.maintainHeap();
            // Otherwise swap the root of the max heap to the last open index
        else
            heap.swapRootAndPointer();
    }

    @Override
    protected boolean isDoneSorting() {
        return heap.isEmpty();                  // We are done once the max heap is empty
    }

    @Override
    protected boolean isArrayAltered() {
        return heap.isAlteredArray();           // The heap will let us know when it has altered the array
    }
}

class MaxBinaryHeap {

    private final int[] sorting;                    // The binary heap array and the array to be sorted
    private int placementIndex;                     // The index at which we will place the root of the tree once it is formed
    private int heapPointer = 0;                    // The pointer for the heap data structure
    private boolean completeHeap = false;           // Boolean to let us know once the heap is done initializing itself
    private boolean maintainedHeap = false;         // Boolean to let us know if the heap needs to be altered
    private boolean alteredArray = false;           // Boolean to let the sorter know if the array has been altered

    public MaxBinaryHeap(int[] toSort) {
        // Initialize the sorting array and the last index of it
        sorting = toSort;
        placementIndex = sorting.length - 1;
    }

    public boolean isCompleteHeap() {
        return completeHeap;                            // Expose whether the heap is complete or not
    }

    public boolean isMaintainedHeap() {
        return maintainedHeap;                          // Expose where the heap has been maintained or not
    }

    public boolean isAlteredArray() {
        return alteredArray;                            // Expose where the array has been altered or not
    }

    public boolean isEmpty() {
        return placementIndex == 1;                     // Expose where the heap is empty or not
    }

    private int leftChild(int parent) {
        return sorting[(parent * 2) + 1];               // Get the left child to a parent in the heap
    }

    private int rightChild(int parent) {
        return sorting[(parent * 2) + 2];               // Get the right child to a parent in the heap
    }

    private boolean hasLeftChild(int parent) {
        return (parent * 2) + 1 <= placementIndex;      // Get whether a left child exists for a given parent
    }

    private boolean hasRightChild(int parent) {
        return (parent * 2) + 2 <= placementIndex;      // Get whether a right child exists for a given parent
    }

    public void createHeap() {
        if (heapPointer <= sorting.length - 1) {
            // While the heap pointer is working to the end get the current node
            int parent = sorting[heapPointer];
            // If it has a left child and that child is larger than the parent then move the left child up
            if (hasLeftChild(heapPointer) && parent < leftChild(heapPointer)) {
                bubbleUp((heapPointer * 2) + 1);
                alteredArray = true;
            }
            // If it has a right child and that child is larger than the parent then move the left child up
            else if (hasRightChild(heapPointer) && parent < rightChild(heapPointer)) {
                bubbleUp((heapPointer * 2) + 2);
                alteredArray = true;
            }
            // Otherwise the property holds and we can move to the next node in the tree
            else {
                heapPointer++;
                alteredArray = false;
            }
        }
        // If the heap pointer is at the end of the array then we are done creating the heap
        else {
            completeHeap = true;
            maintainedHeap = true;
        }
    }

    public void maintainHeap() {
        // If the current node has a left and right child then compare both to the parent
        if (hasLeftChild(heapPointer) && hasRightChild(heapPointer)) {
            // Get all 3 values for comparison
            int parent = sorting[heapPointer];
            int leftChild = leftChild(heapPointer);
            int rightChild = rightChild(heapPointer);
            // If the parent is less than either of the two children then we need to do a swap
            if (parent < leftChild || parent < rightChild) {
                // If the left child is greatest then we need to swap it with the parent node
                if (rightChild < leftChild) {
                    swapArrayElements(heapPointer, (heapPointer * 2) + 1);
                    heapPointer = (heapPointer * 2) + 1;
                }
                // If the right child is greatest then we need to swap it with the parent node
                else {
                    swapArrayElements(heapPointer, (heapPointer * 2) + 2);
                    heapPointer = (heapPointer * 2) + 2;
                }
                // We must have altered the array so this is always true in this block
                alteredArray = true;
            }
            // Otherwise the tree obeys the property at this level and thus we can break out of the loop
            else {
                maintainedHeap = true;
                alteredArray = false;
            }
        }
        // We might also be towards the end of the binary tree and onl have a left node
        else if (hasLeftChild(heapPointer)) {
            // Get the parent and the left child for comparison
            int parent = sorting[heapPointer];
            int leftChild = leftChild(heapPointer);
            // If the child is greater than the parent then swap them and move down
            if (parent < leftChild) {
                swapArrayElements(heapPointer, (heapPointer * 2) + 1);
                heapPointer = (heapPointer * 2) + 1;
                alteredArray = true;
            }
            // Either way we are at the end of the tree and can break out of the loop
            maintainedHeap = true;
            alteredArray = false;
        }
        // Otherwise we are at the very last node and can break
        else {
            // Either way we are at the end of the tree and can break out of the loop
            maintainedHeap = true;
            alteredArray = false;
        }
    }

    private void swapArrayElements(int index1, int index2) {
        // Standard way to swap two array values
        int tmp = sorting[index1];
        sorting[index1] = sorting[index2];
        sorting[index2] = tmp;
    }

    private void bubbleUp(int index) {
        // Start at the index of the value-to-place's parent
        int parentIndex = (index - 1) / 2;
        // While the parent index is smaller than the value to place and we are not at the root then look at the next parent
        while (sorting[parentIndex] < sorting[index] && 0 < parentIndex) {
            // If the parent of the parent is larger than the value-to-place then the value should replace the current parent
            if (sorting[index] <= sorting[(parentIndex - 1) / 2])
                break;
            // Otherwise continue and look at the next parent
            parentIndex = (parentIndex - 1) / 2;
        }
        // Swap the value-to-place and the parent index that we determined
        swapArrayElements(parentIndex, index);
    }

    public void swapRootAndPointer() {
        // Swap the root of the array and the placement pointer, then decrement the pointer and let the tree know it needs
        // to be maintained again
        swapArrayElements(0, placementIndex);
        placementIndex--;
        maintainedHeap = false;
        heapPointer = 0;
    }
}
