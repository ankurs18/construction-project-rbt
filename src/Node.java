public class Node
{
    public static final Node nil = new Node(new Building(-1, -1));
    Node left= nil, right = nil, parent = nil;
    private Building building;
    int color;
    //private final Node nil = new Node(new Building(-1, -1));

    @Override
    public String toString() {
        return "Node{" +
                //"left=" + left +
                //", right=" + right +
                ", parent=" + parent +
                ", building=" + building.getBuildingNum() +
                ", color=" + color +
                '}';
    }

    public Node(Building building)
    {
        this.building = building;
        color = Constants.RED;
    }

    public void setColor(int color){
        this.color = color;
    }

    public Building getBuilding(){
        return this.building;
    }

    public int compareTo(Node o) {
        return this.getBuilding().getBuildingNum() - o.getBuilding().getBuildingNum();
    }

//    public int heapCompareTo(Building o) {
//        Building b1 = this.getBuilding();
//        Building b2 = o.getBuilding();
//        if(b1.getExecutedTime() < b2.)
//    }
}