import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private MinHeap heap;
    private RedBlackTree rbTree;
    private int globalTime;
    private PrintWriter writer;
    private ArrayList<Integer> pendingDecreaseKeys;
    private ArrayList<Building> pendingInputs;

    public Main() {
        globalTime = 0;
        this.heap = new MinHeap();
        this.rbTree = new RedBlackTree();
        pendingDecreaseKeys = new ArrayList<>();
        pendingInputs = new ArrayList<>();

        try {
            this.writer = new PrintWriter(new FileWriter("output.txt"));
            //this.fileStream = new PrintStream(new File("file.txt"));
            //
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        Scanner sc = main.readfile("input1.txt");
        main.process2(sc);
        main.writer.close();
    }

    public static void print(ArrayList<Building> heapArray) {
        for (Building b : heapArray) {
            System.out.println(b);
        }
    }

    public void process2(Scanner sc) {
        String currCommand = "";
        Building b=null;
        int workOnLastBuilding = 5;
        int executedTime = 0, totalTime = 0;
        boolean isLastReadCommandExecuted = true;
        boolean isLastPickedBuildingWorked = true;
//        if (sc.hasNextLine())
//            currCommand = sc.nextLine();
//        currCommand = sc.nextLine();
//        processCommand(currCommand, true);
//        globalTime++;
//        b=heap.peek();
        while (sc.hasNextLine() || heap.getHeapSize() > 0 || !isLastReadCommandExecuted || workOnLastBuilding < 5) {
            //globalTime++;
            if (isLastReadCommandExecuted && sc.hasNextLine()) {
                currCommand = sc.nextLine();
                isLastReadCommandExecuted = false;
                //System.out.println("where");
            }

            if(globalTime==97361) {
                System.out.println("here");
                System.out.println(b != null ? b : "null");
            }
            isLastReadCommandExecuted = isLastReadCommandExecuted ? true : processCommand(currCommand, isLastPickedBuildingWorked);
            if (workOnLastBuilding == 5 && heap.getHeapSize() > 0) {
                completePendingDecreaseKeys();
                b = heap.peek();
                totalTime = b.getTotalTime();
                executedTime = b.getExecutedTime() + 1;
                heap.peek().setExecutedTime(executedTime);
                workOnLastBuilding = 1;
                isLastPickedBuildingWorked = false;
                //completePendingDecreaseKeys();
                if (totalTime == executedTime) {
                    b = heap.extractMin();
                    rbTree.delete(b);
                    //System.out.println("(" + b.getBuildingNum() + "," + (globalTime + 1) + ")");
                    printOutput("(" + b.getBuildingNum() + "," + (globalTime + 1) + ")");
                    workOnLastBuilding = 5;
                    isLastPickedBuildingWorked = true;
                    completePendingDecreaseKeys();
                }
            } else if (workOnLastBuilding < 5) {
                workOnLastBuilding++;
                executedTime++;
                heap.peek().setExecutedTime(executedTime);
                if (totalTime == executedTime) {
                    b = heap.extractMin();
                    rbTree.delete(b);
                    //System.out.println("(" + b.getBuildingNum() + "," + (globalTime + 1) + ")");
                    printOutput("(" + b.getBuildingNum() + "," + (globalTime + 1) + ")");
                    workOnLastBuilding = 5;
                    isLastPickedBuildingWorked = true;
                    completePendingDecreaseKeys();
                } else if (workOnLastBuilding == 5) {
//                    heap.increaseKey(0, executedTime);
//                    isLastPickedBuildingWorked = true;
//                    completePendingDecreaseKeys();
                    completePendingDecreaseKeys();
                    //heap.heapify(0);
                    heap.heapifyEntire();
                    isLastPickedBuildingWorked = true;
                }
            }
            globalTime++;
        }
    }

    private void completePendingDecreaseKeys() {
        if (pendingInputs.size() > 0) {
            for (Building b : pendingInputs) {
                heap.insert(b);
            }
            pendingInputs.clear();
        }
//        if (pendingDecreaseKeys.size() > 0) {
//            for (int i : pendingDecreaseKeys) {
//                heap.decreaseKey(i, 0);
//            }
//            pendingDecreaseKeys.clear();
//        }
    }

    public void process(Scanner sc) {
        String currCommand = "";
        boolean isLastReadCommandExecuted = true;
//        if (sc.hasNextLine())
//            currCommand = sc.nextLine();
        while (sc.hasNextLine() || heap.getHeapSize() > 0 || !isLastReadCommandExecuted) {
            if (isLastReadCommandExecuted && sc.hasNextLine()) {
                currCommand = sc.nextLine();
                isLastReadCommandExecuted = false;
                //System.out.println("where");
            }
            //System.out.println("here");
            isLastReadCommandExecuted = isLastReadCommandExecuted ? true : processCommand(currCommand, true);
            if (heap.getHeapSize() > 0)
                pickBuildingToWork();
            else globalTime++;
        }
    }

    private void pickBuildingToWork() {
        Building b = heap.peek();
        //globalTime > 1707
        if (globalTime > 29) {
            System.out.println("here");
        }
        if (b.getBuildingNum() == 13764) {
            System.out.println("here");
        }
        int executedTime = b.getExecutedTime();
        int totalTime = b.getTotalTime();
        if (totalTime - executedTime > 5) {
            heap.increaseKey(0, executedTime + 5);
            globalTime += 5;
        } else {
            b = heap.extractMin();
            //rbTree.deleteNode(node);
//            Node node = new Node(b);
//            rbTree.delete(node);
            rbTree.delete(b);
            globalTime += (totalTime - executedTime);
            System.out.println("(" + b.getBuildingNum() + "," + globalTime + ")");
            printOutput("(" + b.getBuildingNum() + "," + globalTime + ")");
        }

    }

    public boolean processCommand(String command, boolean isLastPickedBuildingWorked) {
        String[] co = command.split(" ");
        int time = Integer.parseInt(co[0].substring(0, co[0].length() - 1));
//        if(time == 910100){
//            System.out.println("last");
//        }
        if (time > globalTime) {
            //System.out.println("false");
            return false;
        }
        int indexOfBracket = co[1].indexOf('(');
        command = co[1].substring(0, indexOfBracket);
        switch (command) {
            case "Insert": {
                int buildingNum = Integer.parseInt(co[1].substring(indexOfBracket + 1, co[1].indexOf(',')));
                int totalTime = Integer.parseInt(co[1].substring(co[1].indexOf(',') + 1, co[1].length() - 1));
                insertBuilding2(buildingNum, totalTime, isLastPickedBuildingWorked);
            }
            break;
            case "Print":
                rbTree.printBuilding(Integer.parseInt(co[1].substring(indexOfBracket + 1, co[1].length() - 1)));
                break;
            case "PrintBuliding": {
                if (co[1].contains(",")) {

                    int b1 = Integer.parseInt(co[1].substring(indexOfBracket + 1, co[1].indexOf(',')));
                    int b2 = Integer.parseInt(co[1].substring(co[1].indexOf(',') + 1, co[1].length() - 1));
                    printOutput(rbTree.printRange(b1, b2));
                } else
                    printOutput(rbTree.printBuilding(Integer.parseInt(co[1].substring(indexOfBracket + 1, co[1].length() - 1))));
            }
            break;
            default:
                System.out.println("default");
        }
        return true;
    }

    private void printOutput(String output) {
        System.out.println(output);
        writer.println(output);
    }

    public void insertBuilding(int buildingNum, int totalTime) {
        Building b = new Building(buildingNum, totalTime);
        heap.insert(b);
        rbTree.insert(b);
    }

    public void insertBuilding2(int buildingNum, int totalTime, boolean isLastPickedBuildingWorked) {
        Building b = new Building(buildingNum, totalTime);
        if (isLastPickedBuildingWorked)
            heap.insert(b);
        else
        {  //pendingDecreaseKeys.add(heap.heapSize-1);
            pendingInputs.add(b);}
        rbTree.insert(b);
    }

    public void testRBTree() {
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
        //RedBlackTree.Node n2 = new RedBlackTree.Node(new Building(52, 20));
        //rbtree.deleteNode(n2);
        rbtree.print();
        //System.out.println("found: " + rbtree.search(n).toString());
    }


    public static void testMinHeap() {
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

    public Scanner readfile(String fileName) {
        URL url = getClass().getResource(fileName);
        File file =
                new File(url.getPath());
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return sc;
    }
}
