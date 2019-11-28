import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class risingCity {
    private MinHeap heap;
    private RedBlackTree rbTree;
    private int globalTime;
    private PrintWriter writer;
    private ArrayList<Building> pendingHeapInserts;

    public risingCity() {
        globalTime = 0;
        this.heap = new MinHeap();
        this.rbTree = new RedBlackTree();
        pendingHeapInserts = new ArrayList<>();
        try {
            this.writer = new PrintWriter(new FileWriter("output.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        risingCity main = new risingCity();
        Scanner sc = main.readFile("input3.txt");
        System.out.println("Execution started..");
        main.process(sc);
        main.writer.close();
        System.out.println("Execution completed. Output file: output.txt");
    }

    // the next command to process is read from the input file but not processed until its time matches the global timer
    private void process(Scanner sc) {
        String currCommand = "";
        Building b = null;
        int workOnLastBuilding = 5;
        int executedTime = 0, totalTime = 0;
        boolean isLastReadCommandExecuted = true;
        boolean isLastPickedBuildingWorked = true;
        boolean isGlobalTimeIncreased;
        while (sc.hasNextLine() || heap.getHeapSize() > 0 || !isLastReadCommandExecuted || workOnLastBuilding < 5) {
            isGlobalTimeIncreased = false;
            if (isLastReadCommandExecuted && sc.hasNextLine()) {
                currCommand = sc.nextLine();
                isLastReadCommandExecuted = false;
            }
            isLastReadCommandExecuted = isLastReadCommandExecuted || processCommand(currCommand, isLastPickedBuildingWorked);
            if (workOnLastBuilding == 5 && heap.getHeapSize() > 0) {
                completePendingHeapInserts();
                b = heap.peek();
                totalTime = b.getTotalTime();
                executedTime = b.getExecutedTime() + 1;
                heap.peek().setExecutedTime(executedTime);
                workOnLastBuilding = 1;
                isLastPickedBuildingWorked = false;
                if (totalTime == executedTime) {
                    b = heap.extractMin();
                    globalTime++;
                    isGlobalTimeIncreased = true;
                    isLastPickedBuildingWorked = true;
                    isLastReadCommandExecuted = isLastReadCommandExecuted || processCommand(currCommand, isLastPickedBuildingWorked && currCommand.contains("Print"));
                    rbTree.delete(b);
                    printOutput("(" + b.getBuildingNum() + "," + globalTime + ")");
                    workOnLastBuilding = 5;
                    completePendingHeapInserts();
                }
            } else if (workOnLastBuilding < 5) {
                workOnLastBuilding++;
                executedTime++;
                heap.peek().setExecutedTime(executedTime);
                if (totalTime == executedTime) {
                    b = heap.extractMin();
                    globalTime++;
                    isGlobalTimeIncreased = true;
                    isLastPickedBuildingWorked = true;
                    isLastReadCommandExecuted = isLastReadCommandExecuted || processCommand(currCommand, isLastPickedBuildingWorked && currCommand.contains("Print"));
                    rbTree.delete(b);
                    printOutput("(" + b.getBuildingNum() + "," + globalTime + ")");
                    workOnLastBuilding = 5;
                    completePendingHeapInserts();
                } else if (workOnLastBuilding == 5) {
                    completePendingHeapInserts();
                    heap.heapifyEntire();
                    isLastPickedBuildingWorked = true;
                }
            }
            if (!isGlobalTimeIncreased) {
                globalTime++;
            }
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
                printOutput("Error:Invalid command encountered - " + command);
                System.out.println("Error:Invalid command encountered - " + command);
                System.exit(-1);
        }
        return true;
    }

    // inserts the building element into the red-black tree; but its added to the min heap if a building not being worked upon
    // if the construction of a building is in process new buildings to be inserted are not immediately added to the heap;
    // this would mess the heap balance, and thus we add those buildings to a list of pending buildings
    // which is processed only once the current building being worked upon is done or 5 days are up (whichever is earlier))
    private void insertBuilding(int buildingNum, int totalTime, boolean isLastPickedBuildingWorked) {
        Building b = new Building(buildingNum, totalTime);

        rbTree.insert(b);
        if (isLastPickedBuildingWorked)
            heap.insert(b);
        else {
            pendingHeapInserts.add(b);
        }
    }

    // Helper function to insert (if any) pending building inputs to the min heap.
    // Called when the construction of the previous building is completed.
    private void completePendingHeapInserts() {
        if (pendingHeapInserts.size() > 0) {
            for (Building b : pendingHeapInserts) {
                heap.insert(b);
            }
            pendingHeapInserts.clear();
        }
    }

    // Writes the string given to it as a parameter to the specified output text file.
    // It uses the PrintWriter object initialized in the constructor.
    private void printOutput(String output) {
        writer.println(output);
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
            System.out.println("Error: Input file not found");
        }
        return sc;
    }
}
