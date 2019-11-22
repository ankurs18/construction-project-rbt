import java.io.*;
import java.net.URL;
import java.util.ArrayList;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        Scanner sc = main.readFile("input3.txt");
        main.process(sc);
        main.writer.close();
    }

    // the next command to process is read from the input file but not processed until its time matches the global timer
    private void process(Scanner sc) {
        String currCommand = "";
        Building b=null;
        int workOnLastBuilding = 5;
        int executedTime = 0, totalTime = 0;
        boolean isLastReadCommandExecuted = true;
        boolean isLastPickedBuildingWorked = true;
        while (sc.hasNextLine() || heap.getHeapSize() > 0 || !isLastReadCommandExecuted || workOnLastBuilding < 5) {

            if (isLastReadCommandExecuted && sc.hasNextLine()) {
                currCommand = sc.nextLine();
                isLastReadCommandExecuted = false;
            }

            if(globalTime==60) {
                System.out.print("");
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

    // This function processes the input command.
    // The command given to it as an argument represents a single line of the input,
    // which is parsed and executed based on the type of command
    private boolean processCommand(String command, boolean isLastPickedBuildingWorked) {
        String[] co = command.split(" ");
        int time = Integer.parseInt(co[0].substring(0, co[0].length() - 1));
        if (time > globalTime) {
            return false;
        }
        int indexOfBracket = co[1].indexOf('(');
        command = co[1].substring(0, indexOfBracket);
        switch (command) {
            case "Insert": {
                int buildingNum = Integer.parseInt(co[1].substring(indexOfBracket + 1, co[1].indexOf(',')));
                int totalTime = Integer.parseInt(co[1].substring(co[1].indexOf(',') + 1, co[1].length() - 1));
                insertBuilding(buildingNum, totalTime, isLastPickedBuildingWorked);
            }
            break;
            case "PrintBuliding":
            case "PrintBuilding": {
                if (co[1].contains(",")) {
                    int b1 = Integer.parseInt(co[1].substring(indexOfBracket + 1, co[1].indexOf(',')));
                    int b2 = Integer.parseInt(co[1].substring(co[1].indexOf(',') + 1, co[1].length() - 1));
                    printOutput(rbTree.printRange(b1, b2));
                } else
                    printOutput(rbTree.printBuilding(Integer.parseInt(co[1].substring(indexOfBracket + 1, co[1].length() - 1))));
            }
            break;
            default:
                System.out.println( "Error:Invalid command encountered - " + command);
                System.exit(-1);
        }
        return true;
    }

    // Writes the string given to it as a parameter to the specified output text file.
    // It uses the PrintWriter object initialized in the constructor.
    private void printOutput(String output) {
        System.out.println(output);
        writer.println(output);
    }

    // inserts the building element into the red-black tree; but its added to the min heap if a building not being worked upon
    // if the construction of a building is in process new buildings to be inserted are not immediately added to the heap;
    // this would mess the heap balance, and thus we add those buildings to a list of pending buildings
    // which is processed the heap only once the current building being worked upon is done or 5 days are up (whichever is earlier))
    private void insertBuilding(int buildingNum, int totalTime, boolean isLastPickedBuildingWorked) {
        Building b = new Building(buildingNum, totalTime);

        rbTree.insert(b);
        if (isLastPickedBuildingWorked)
            heap.insert(b);
        else
        {  //pendingDecreaseKeys.add(heap.heapSize-1);
            pendingInputs.add(b);}
    }

    //helper function to process (if any) pending building inputs to the min heap
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

    // helper function to create an Scanner object for the input file
    private Scanner readFile(String fileName) {
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
