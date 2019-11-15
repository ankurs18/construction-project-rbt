import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {   Main main = new Main();
        main.readfile("input.txt");
    }

    public static void testRBTree(){
        //Building one = new Building(50, 20);
        RedBlackTree rbtree = new RedBlackTree();
        rbtree.insert(new Building(50, 20));
        //rbtree.print();
        //rbtree.insert(new Building(20, 20));
        //rbtree.insert(new Building(25, 20));
        //rbtree.print();

        //rbtree.insert(new Building(15, 20));
        //rbtree.insert(new Building(70, 20));
        rbtree.insert(new Building(55, 20));
        //rbtree.print();
        rbtree.insert(new Building(52, 20));
        rbtree.print();
        rbtree.insert(new Building(60, 20));
        rbtree.insert(new Building(40, 20));
        rbtree.insert(new Building(51, 20));
        rbtree.print();
//        Node n = new Node(new Building(50, 20));
//        rbtree.deleteNode(n);
//        rbtree.print();
        Node n2 = new Node(new Building(52, 20));
        rbtree.deleteNode(n2);
        rbtree.print();
        //System.out.println("found: " + rbtree.search(n).toString());
    }

    public static void testMinHeap(){
        MinHeap heap = new MinHeap();
        heap.insert(new Building(30, 20));
        heap.insert(new Building(20, 20));
        heap.insert(new Building(15, 20));
        heap.insert(new Building(5, 20));
        heap.insert(new Building(25, 20));
        heap.insert(new Building(35, 20));
        heap.insert(new Building(1, 20));
        heap.increaseKey(2, 5);
        print(heap.getHeapArray());
    }

    public static void print(ArrayList<Building> heapArray){
        for(Building b: heapArray){
            System.out.println(b);
        }
    }

    public void readfile(String fileName){

        URL url = getClass().getResource(fileName);
        File file =
                new File(url.getPath());
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine())
            System.out.println(sc.nextLine());
    }
}
