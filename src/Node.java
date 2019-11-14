public class Node
{

    Node left, right, parent;
    private Building building;
    int color;

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
        left = null;
        right = null;
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
}