import java.util.ArrayList;

public class MinHeap {

    private ArrayList<Building> heapArray;
    int heapSize;

    MinHeap() {
        this.heapArray = new ArrayList<>();
        this.heapSize = 0;
    }

    // adds a building (with the initial executed time set to Max Integer) to the heap's last position
    // and then decreases the heap's element's main key, i.e., the building's executed time to 0
    void insert(Building building) {
        heapArray.add(building);
        decreaseKey(heapSize++, 0);
    }

    // decreases the key of the element at position i and makes relevant updates, moving the element up the heap;
    // this is done by exchanging the building element with its parent element in the heap
    // while the parent is larger than the current element
    public void decreaseKey(int i, int key) {
        Building curr = heapArray.get(i);
        curr.setExecutedTime(key);
        while (i > 0 && heapArray.get(parentOf(i)).compareTo(curr) > 0) {
            swap(parentOf(i), i);
            i = parentOf(i);

        }
    }

    public void increaseKey(int i, int key){
        heapArray.get(i).setExecutedTime(key);
        heapify(i);
    }

    public void heapifyEntire(){
        for(int i = (int)Math.floor(heapSize/2); i>=0;i--){
            heapify(i);
        }
    }
    public void heapify(int pos){
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

    public void print(){
        for(Building b: heapArray){
            System.out.println(b);
        }
    }

    public Building peek(){
        if(heapSize<0)
            return null;
        return heapArray.get(0);
    }

    public Building extractMin(){
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

    private void swap(int i1, int i2){
        Building temp = heapArray.get(i1);
        heapArray.set(i1, heapArray.get(i2));
        heapArray.set(i2, temp);
    }

    private void swap(Building b1, Building b2){
        Building temp = b1;
        b1 = b2;
        b2 = temp;
    }

    private int parentOf(int i) {
        return (int) (Math.floor(i-1) / 2);
    }

    private int leftChildOf(int i) {
        return (2 * i) + 1;
    }

    private int rightChildOf(int i) {
        return (2 * i) + 2;
    }

    public ArrayList<Building> getHeapArray() {
        return heapArray;
    }

    public int getHeapSize() {
        return heapSize;
    }

}
