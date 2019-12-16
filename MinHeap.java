import java.util.ArrayList;

public class MinHeap {

    private ArrayList<Building> heapArray;
    int heapSize;

    MinHeap() {
        this.heapArray = new ArrayList<>();
        this.heapSize = 0;
    }

    // adds a building to the heap's last position
    // and then decreases the heap's element's main key, i.e., the building's executed time to 0
    void insert(Building building) {
        heapArray.add(building);
        decreaseKey(heapSize++, 0);
    }

    // decreases the key of the element at position i and makes relevant updates, moving the element up the heap;
    // this is done by exchanging the building element with its parent element in the heap
    // while the parent is larger than the current element; key here is the current executed time of the building
    public void decreaseKey(int i, int key) {
        Building curr = heapArray.get(i);
        curr.setExecutedTime(key);
        while (i > 0 && heapArray.get(parentOf(i)).compareTo(curr) > 0) {
            swap(parentOf(i), i);
            i = parentOf(i);
        }
    }

    // Sets the ith element’s key, i.e., the building’s execution time to the value provided as parameter.
    // Calls the heapify(i) method to ensure that the subtree rooted at position i satisfies the min-heap property
    // and restores the same if it has been violated by increasing the element’s key.
    public void increaseKey(int i, int key){
        heapArray.get(i).setExecutedTime(key);
        heapify(i);
    }

    // builds the entire array into a min heap.
    // this method is invoked when the min-heap may be violated at more than one positions
    void heapifyEntire(){
        for(int i = (int)Math.floor(heapSize/2); i>=0;i--){
            heapify(i);
        }
    }

    // Restores the min-heap property at index pos.
    // Its assumes that the subtrees rooter at left(pos) and right(pos) are min-heaps.
    // When array[pos] is smaller than its children the method makes it float down such that the min-heap property is maintained.
    private void heapify(int pos){
        int leftIndex = leftChildOf(pos);
        Building curr = heapArray.get(pos);
        Building left =  leftIndex < heapSize ? heapArray.get(leftIndex) : null;
        int rightIndex = rightChildOf(pos);
        Building right =  rightIndex < heapSize ? heapArray.get(rightIndex) : null;
        int minimum;
        minimum = (left !=null && left.compareTo(curr)<0) ? leftIndex : pos;
        Building minBuilding = heapArray.get(minimum);
        minimum = (right !=null && right.compareTo(minBuilding) < 0)? rightIndex : minimum;
        if(minimum !=pos){
            swap(pos, minimum);
            heapify(minimum);
        }
    }

    //returns the minimum element in the heap, without removing it
    Building peek(){
        if(heapSize<0)
            return null;
        return heapArray.get(0);
    }

    //removes and returns the minimum in the heap; calls heapify to maintain the heap property
    Building extractMin(){
        if(heapSize<0)
            return null;
        Building min = heapArray.get(0);
        heapArray.set(0, heapArray.get(heapSize-1));
        heapArray.remove(heapArray.get(heapSize-1));
        heapSize--;
        if(heapSize>0)
            //heapify(0);
            heapifyEntire();
        return min;
    }

    // helper function to swap two heap elements
    private void swap(int i1, int i2){
        Building temp = heapArray.get(i1);
        heapArray.set(i1, heapArray.get(i2));
        heapArray.set(i2, temp);
    }

    // helper function to calculate index of the parent of ith element
    private int parentOf(int i) {
        return (int) (Math.floor(i-1) / 2);
    }

    // helper function to calculate index of the left child of ith element
    private int leftChildOf(int i) {
        return (2 * i) + 1;
    }

    // helper function to calculate index of the right child of ith element
    private int rightChildOf(int i) {
        return (2 * i) + 2;
    }

    int getHeapSize() {
        return heapSize;
    }

}
