public class RedBlackTree {

    private Node root;

    public RedBlackTree() {
        root = nil;
    }

    // creates a new node with the building data and inserts into the red-black tree
    // the insertion may mess the red-black tree property of no two parent-child nodes being red
    // and the rebalance method is called to fix that
    public void insert(Building buildingData) {
        Node newNode = new Node(buildingData);
        if(search(newNode)!=null){
            System.out.println( "Error: Duplicate building number encountered - " + buildingData.getBuildingNum());
            System.exit(-1);
        }
        if (root == nil) {
            root = newNode;
            root.parent = nil;
            root.setColor(Constants.BLACK);
            return;
        }
        Node dummyNode = root;
        Node pp;
        while (dummyNode != nil) {
            pp = dummyNode;
            if (newNode.compareTo(pp) < 0) {
                if(pp.left == nil){
                    pp.left = newNode;
                    newNode.parent = pp;
                    break;
                }
                else
                    dummyNode = dummyNode.left;
            } else {
                if(pp.right == nil){
                    pp.right = newNode;
                    newNode.parent = pp;
                    break;
                }
                else
                dummyNode = dummyNode.right;
            }
        }
        rebalance(newNode);
    }

    // Called on the node where the red-black tree is being violated.
    // Fixes the tree in either of the cases by performing left and/or right rotations.
    private void rebalance(Node node) {
        while (node.parent.color == Constants.RED) {
            Node otherChild;
            if (node.parent == node.parent.parent.left) {
                otherChild = node.parent.parent.right;
                // Case LXr where X can be L or R
                if (otherChild != nil && otherChild.color == Constants.RED) {
                    node.parent.color = Constants.BLACK;
                    otherChild.color = Constants.BLACK;
                    node.parent.parent.color = Constants.RED;
                    node = node.parent.parent;
                    continue;
                }
                // Case LRb: we need to perform two rotates
                // 1. left rotate pp and 2. right rotate gp
                if (node == node.parent.right) {
                    node = node.parent;
                    rotateLeft(node);
                }
                node.parent.color = Constants.BLACK;
                node.parent.parent.color = Constants.RED;
                // Case LLb or LRb if the previous if evaluates to true
                // we need to rotate the node gp to right
                rightRotate(node.parent.parent);
            } else {
                otherChild = node.parent.parent.left;
                if (otherChild != nil && otherChild.color == Constants.RED) {
                    node.parent.color = Constants.BLACK;
                    otherChild.color = Constants.BLACK;
                    node.parent.parent.color = Constants.RED;
                    node = node.parent.parent;
                    continue;
                }
                // Case LRb: we need to perform two rotates
                // 1. right rotate pp and 2. left rotate gp
                if (node == node.parent.left) {
                    node = node.parent;
                    rightRotate(node);
                }
                node.parent.color = Constants.BLACK;
                node.parent.parent.color = Constants.RED;
                // Case RRb or RLb if the previous if evaluates to true
                // we need to rotate the node gp to left
                rotateLeft(node.parent.parent);
            }
        }
        root.color = Constants.BLACK;
    }

    // Makes the left child of Node the root of the subtree rooted at node
    // and makes other relevant pointer and color changes along the process.
    void rightRotate(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {//Need to rotate root
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }

    // Makes the right child of Node the root of the subtree rooted at node
    // and makes other relevant pointer and color changes along the process.
    void rotateLeft(Node currNode) {
        Node parent = currNode.parent;
        if (parent != nil) {
            if (currNode == parent.left) {
                parent.left = currNode.right;
            } else {
                parent.right = currNode.right;
            }
            currNode.right.parent = parent;
            currNode.parent = currNode.right;
            if (currNode.right.left != nil) {
                currNode.right.left.parent = currNode;
            }
            currNode.right = currNode.right.left;
            currNode.parent.left = currNode;
        } else {// when currNode is the root
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }

    // wrapper function to delete the node with the building element
    boolean delete(Building building){
        Node n = new Node(building);
        return delete(n);
    }

    // actual function to perform deletion in a red black tree;
    // since it is a time of BST, for the deletion of a node with one or more children,
    // we find an element to replace the node to be deleted;
    // this is the child of the node to be deleted if has a single child or the minimum in its right subtree
    // returns true on successful deletion or false when the element is not found in the tree
    boolean delete(Node nodeToBeDeleted){
        if((nodeToBeDeleted = search(nodeToBeDeleted))==null)return false;
        Node x;
        Node y = nodeToBeDeleted;
        int y_original_color = y.color;
        if(nodeToBeDeleted.left == nil){
            x = nodeToBeDeleted.right;
            transplant(nodeToBeDeleted, nodeToBeDeleted.right);
        }else if(nodeToBeDeleted.right == nil){
            x = nodeToBeDeleted.left;
            transplant(nodeToBeDeleted, nodeToBeDeleted.left);
        }else{
            y = findMinimumInSubtree(nodeToBeDeleted.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == nodeToBeDeleted)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = nodeToBeDeleted.right;
                y.right.parent = y;
            }
            transplant(nodeToBeDeleted, y);
            y.left = nodeToBeDeleted.left;
            y.left.parent = y;
            y.color = nodeToBeDeleted.color;
        }
        if(y_original_color==Constants.BLACK)
            deleteRebalance(x);
        return true;
    }

    // Called on the node where the red-black tree property is being violated.
    // Fixes the tree in either of the cases by invoking the method(s) rightRotate and/or leftRotate.
    void deleteRebalance(Node x) {
        while(x!=root && x.color == Constants.BLACK){
            if(x == x.parent.left){
                Node w = x.parent.right;
                if(w.color == Constants.RED){
                    w.color = Constants.BLACK;
                    x.parent.color = Constants.RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left==null){
                    System.out.println("null");
                }
                if(w.left.color == Constants.BLACK && w.right.color == Constants.BLACK){
                    w.color = Constants.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == Constants.BLACK){
                    w.left.color = Constants.BLACK;
                    w.color = Constants.RED;
                    rightRotate(w);
                    w = x.parent.right;
                }
                if(w.right.color == Constants.RED){
                    w.color = x.parent.color;
                    x.parent.color = Constants.BLACK;
                    w.right.color = Constants.BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
                Node w = x.parent.left;
                if(w.color == Constants.RED){
                    w.color = Constants.BLACK;
                    x.parent.color = Constants.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == Constants.BLACK && w.left.color == Constants.BLACK){
                    w.color = Constants.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == Constants.BLACK){
                    w.right.color = Constants.BLACK;
                    w.color = Constants.RED;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == Constants.RED){
                    w.color = x.parent.color;
                    x.parent.color = Constants.BLACK;
                    w.left.color = Constants.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = Constants.BLACK;
    }
    
    // places the subtree rooted at node u with the subtree rooted at node v
    // node u’s parent becomes node v’s parent,
    // and u’s parent ends up having v as its appropriate child
    public void transplant(Node u, Node v) {
        if (u.parent == nil)
            root = v;
        else if (u == u.parent.left)
            u.parent.left = v;
        else
            u.parent.right = v;
        v.parent = u.parent;
    }

    // helper function to find the minimum in a subtree by following left child pointers
    private Node findMinimumInSubtree(Node subTreeRoot) {
        if(subTreeRoot==null){
            return subTreeRoot.parent;
        }
        while (subTreeRoot.left != nil) {
            if(subTreeRoot.left==null){
                break;
            }
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }

    // search method works as in a BST: going to the left child if the nodeToSearch is smaller than the current node
    // or going to the right child if larger or else if return current node if both are equal
    public Node search(Node nodeToSearch) {
        if (root == nil)
            return null;
        Node dummyNode = root;
        while (dummyNode != nil) {
            if (nodeToSearch.compareTo(dummyNode) < 0) {
                dummyNode = dummyNode.left;
            } else if (nodeToSearch.compareTo(dummyNode) > 0) {
                dummyNode = dummyNode.right;
            } else
                return dummyNode;
        }
        return null;
    }

    // Given the buildingNum, this function to search and print the building data from the corresponding node element
    // or returns (0,0,0) if the building is not found
    public String printBuilding(int buildingNum) {
        Node nodeToSearch = new Node(new Building(buildingNum, -1));
        if (root == nil)
            return "(0,0,0)";
        Node dummyNode = root;
        while (dummyNode != nil) {
            if (nodeToSearch.compareTo(dummyNode) < 0) {
                dummyNode = dummyNode.left;
            } else if (nodeToSearch.compareTo(dummyNode) > 0) {
                dummyNode = dummyNode.right;
            } else
                return dummyNode.getBuilding().toString();
        }
        return "(0,0,0)";
    }

    // wrapper function to print buildings in the given range or return (0,0,0) if the building is not found in the given range.
    public String printRange(int b1, int b2) {
        StringBuilder b = printRange(root, b1, b2, new StringBuilder());
        return (b.length()>1) ? b.substring(0, b.length() - 1) : "(0,0,0)";
    }

    // recursive function to build output of printing the buildings in the given range
    // initially called from root node and an empty Stringbuilder. Searches the range,
    // going into the left and right subtree only when their value is less than b1 and greater than b2 respectively.
    private StringBuilder printRange(Node node, int b1, int b2, StringBuilder builder) {
        if (node == null) {
            return builder;
        }
        Building b = node.getBuilding();
        int currBuildingNum = b.getBuildingNum();
        if (b1 < currBuildingNum) {
            printRange(node.left, b1, b2, builder);
        }

        if (b1 <= currBuildingNum && b2 >= currBuildingNum) {
            builder.append(b.toString());
            builder.append(',');
        }

        if (b2 > currBuildingNum) {
            printRange(node.right, b1, b2, builder);
        }
        return builder;
    }
    private final Node nil = new Node(new Building(-1, -1), Constants.BLACK);
    public class Node {
        Node left = nil, right = nil, parent = nil;
        private Building building;
        int color;

        public Node(Building building) {
            this.building = building;
            color = Constants.RED;
        }

        public Node(Building building, int color) {
            this.building = building;
            this.color = color;
        }

        // helper method to compare two nodes with respect to their building element,
        // i.e., the building number of those elements
        public int compareTo(Node o) {
            return this.getBuilding().getBuildingNum() - o.getBuilding().getBuildingNum();
        }

        public void setColor(int color) {
            this.color = color;
        }

        public Building getBuilding() {
            return this.building;
        }
    }
}
