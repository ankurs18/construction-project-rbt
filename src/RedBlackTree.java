import java.util.zip.CheckedOutputStream;

public class RedBlackTree {

    private Node root;

    public RedBlackTree() {
        root = nil;
    }

    public void insert(Building buildingData) {
        Node newNode = new Node(buildingData);

        if (root == nil) {
            root = newNode;
            root.parent = nil;
            root.setColor(Constants.BLACK);
            return;
        }
        Node dummyNode = root;
        Node pp = nil;
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
//        newNode.parent = pp;
//
//        if (pp.compareTo(newNode) < 0) {
//            pp.right = newNode;
//
//        } else {
//            pp.left = newNode;
//        }
        fixTree(newNode);
    }
    private void fixTree(Node node) {
        while (node.parent.color == Constants.RED) {
            Node uncle = nil;
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;

                if (uncle != nil && uncle.color == Constants.RED) {
                    node.parent.color = Constants.BLACK;
                    uncle.color = Constants.BLACK;
                    node.parent.parent.color = Constants.RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.right) {
                    //Double rotation needed
                    node = node.parent;
                    rotateLeft(node);
                }
                node.parent.color = Constants.BLACK;
                node.parent.parent.color = Constants.RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation
                rightRotate(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                if (uncle != nil && uncle.color == Constants.RED) {
                    node.parent.color = Constants.BLACK;
                    uncle.color = Constants.BLACK;
                    node.parent.parent.color = Constants.RED;
                    node = node.parent.parent;
                    continue;
                }
                if (node == node.parent.left) {
                    //Double rotation needed
                    node = node.parent;
                    rightRotate(node);
                }
                node.parent.color = Constants.BLACK;
                node.parent.parent.color = Constants.RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation
                rotateLeft(node.parent.parent);
            }
        }
        root.color = Constants.BLACK;
    }
    private void rebalance(Node p) {
        //p.parent!=null &&
        while (p.parent.color == Constants.RED) {
            Node pp = p.parent;
            Node gp = pp.parent;
            if (gp == nil)
                return;
            switch (getInsertImbalanceType(p, pp, gp)) {
                // Case 1: if p is left child of pp, pp is left child of gp and gp's other child is black
                case Constants.IMBALANCE_LLb: {
                    pp.color = Constants.BLACK;
                    gp.color = Constants.RED;
                    rightRotate(gp);
                }
                break;
                case Constants.IMBALANCE_LRb: {
                    p = pp;
                    rotateLeft(p);
                    //pp.color = Constants.BLACK;
                    p.parent.color = Constants.BLACK;
                    p.parent.parent.color = Constants.RED;
                    rightRotate(gp);
                }
                break;
                case Constants.IMBALANCE_RRb: {
                    pp.color = Constants.BLACK;
                    gp.color = Constants.RED;
                    rotateLeft(gp);
                }
                break;
                case Constants.IMBALANCE_RLb: {
                    p = pp;
                    rightRotate(p);
                    //pp.color = Constants.BLACK;
                    p.parent.color = Constants.BLACK;
                    p.parent.parent.color = Constants.RED;
                    //gp.color = Constants.RED;
                    rotateLeft(p.parent.parent);
                }
                break;
                // remaining cases: when gp's other child is red
                default: {
                    gp.left.color = Constants.BLACK;
                    gp.color = Constants.RED;
                    gp.right.color = Constants.BLACK;
                    p = p.parent;
                    //pp = gp;
                }
            }
            root.color = Constants.BLACK;

        }
    }

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

    private void rightRotatemod(Node currNode) {
        Node parent = currNode.parent;
        // checking if currNode is the root
        if (parent != nil) {
            if (parent.right == currNode) {
                parent.left = currNode.left;
            } else {
                parent.right = currNode.left;
            }
            currNode.left.parent = parent;
            currNode.parent = currNode.left;
            if (currNode.left.right != nil) {
                currNode.left.right.parent = currNode;
            }
            currNode.left = currNode.left.right;
            currNode.parent.right = currNode;
        } else { // when currNode is the root
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }

    private void rightRotate1(Node currNode) {
        Node parent = currNode.parent;
        Node left = currNode.left;
        // checking if currNode is the root
        if (parent != nil) {
            if (parent.right == currNode) {
                parent.left = left;
            } else {
                parent.right = left;
            }
            left.parent = parent;
        } else {
            left.parent = nil;
            root = left;
        }
        currNode.parent = left;
        if (left.right != nil) {
            left.right.parent = currNode;
        }
        currNode.left = currNode.left.right;
        currNode.parent.right = currNode;
    }

    void rotateLeftOwn(Node currNode) {
        Node parent = currNode.parent;
        Node right = currNode.right;
        if (parent != nil) {
            if (currNode == parent.left) {
                parent.left = right;
            } else {
                parent.right = right;
            }
            right.parent = parent;
        } else {
            right.parent = nil;
            root = right;
        }
        currNode.parent = right;
        if (right.left != nil) {
            right.left.parent = currNode;
        }
        currNode.right = currNode.right.left;
        currNode.parent.left = currNode;
    }
    void rotateLeft(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {//Need to rotate root
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }

    void rotateLeft1(Node currNode) {
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

    /*private void rightRotate(Node gp, Node pp) {
        Node ggp = gp.parent;
        if (ggp != null) {
            if (ggp.left == gp) {
                ggp.left = pp;
            } else ggp.right = pp;
        } else
            // if gpp == null, gp is the root; thus, pp will be the new root
            root = pp;
        Node temp = pp.right;
        pp.right = gp;
        gp.left = temp;
        gp.left = pp.right;
    }*/

    private String getInsertImbalanceType(Node p, Node pp, Node gp) {
        if (gp.right != nil && gp.right.color == Constants.RED && gp.left != nil && gp.right.color == Constants.RED)
            return "XYr";
        StringBuilder builder = new StringBuilder();
        if (gp.left == pp) {
            builder.append('L');
            if (gp.right != nil && gp.right.color == Constants.RED) builder.append('r');
            else
                builder.append('b');
        } else {
            builder.append('R');
            if (gp.left != nil && gp.left.color == Constants.RED) builder.append('r');
            else
                builder.append('b');
        }
        if (pp.left == p) builder.insert(1, 'L');
        else builder.insert(1, 'R');
        ;
        return builder.toString();
    }

    public void print() {
        printInorder(root);
        System.out.println("");
    }

    private void printInorder(Node node) {
        if (node == nil)
            return;
        printInorder(node.left);
        System.out.print("Building num: " + node.getBuilding().getBuildingNum() + " color: " + node.color + " ");
        printInorder(node.right);
    }
    boolean delete(Building building){
        Node n = new Node(building);
        return delete(n);
    }
    boolean delete(Node z){
        if((z = search(z))==null)return false;
        Node x;
        Node y = z; // temporary reference y
        int y_original_color = y.color;

        if(z.left == nil){
            x = z.right;
            transplant(z, z.right);
        }else if(z.right == nil){
            x = z.left;
            transplant(z, z.left);
        }else{
            if(z.right==null){
                System.out.println("null");
            }
            y = findMinimumInSubtree(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if(y_original_color==Constants.BLACK)
            deleteFixup(x);
        return true;
    }

    public boolean deleteNode(Node nodeToDelete) {
        nodeToDelete = search(nodeToDelete);
        if (nodeToDelete == nil)
            return false;

        Node y = nodeToDelete, x;
        int originalColorN = nodeToDelete.color;

        if (nodeToDelete.left == nil && nodeToDelete.right == nil) {
            if (nodeToDelete.parent.left == nodeToDelete) {
                nodeToDelete.parent.left = nil;
            } else
                nodeToDelete.parent.right = nil;
            x = nil;
        } else if (nodeToDelete.left == nil) {
            x = nodeToDelete.right;
            transplant(nodeToDelete, nodeToDelete.right);
        } else if (nodeToDelete.right == nil) {
            x = nodeToDelete.left;
            transplant(nodeToDelete, nodeToDelete.left);
        } else {
            // finding the minimum in the right subtree to replace nodeToDelete
            y = findMinimumInSubtree(nodeToDelete.right);
            originalColorN = y.color;
            x = y.right;
            if (y.parent == nodeToDelete) {
                if (x != nil) x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = nodeToDelete.right;
                y.right.parent = y;
            }
            transplant(nodeToDelete, y);
            y.left = nodeToDelete.left;
            y.left.parent = y;
            y.color = nodeToDelete.color;
        }
        if (originalColorN == Constants.BLACK)
            deleteFixup(x);
        return true;
    }
    void deleteFixup(Node x) {
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

    private void deleteRebalance(Node node) {
        while (node != root && node.color == Constants.BLACK) {
            if (node == node.parent.left) {
                Node sibling = node.parent.right;
                if (sibling.color == Constants.RED) {
                    sibling.color = Constants.BLACK;
                    node.parent.color = Constants.RED;
                    rotateLeft(node.parent);
                }
                if (sibling.left!=null && sibling.left.color == Constants.BLACK && sibling.right!=null && sibling.right.color == Constants.BLACK) {
                    sibling.color = Constants.RED;
                    node = node.parent;
                    continue;
                } else if (sibling.right==null || sibling.right.color == Constants.BLACK) {
                    sibling.left.color = Constants.BLACK;
                    sibling.color = Constants.RED;
                    rightRotate(sibling);
                    sibling = node.parent.right;
                }
                if (sibling.right!=null && sibling.right.color == Constants.RED) {
                    sibling.color = node.parent.color;
                    node.parent.color = Constants.BLACK;
                    sibling.right.color = Constants.BLACK;
                    rotateLeft(node.parent);
                    node = root;
                }
            } else {
                Node sibling = node.parent.left;
                if (sibling.color == Constants.RED) {
                    sibling.color = Constants.BLACK;
                    node.parent.color = Constants.RED;
                    rightRotate(node.parent);
                    sibling = node.parent.left;
                }
                if (sibling.right.color == Constants.BLACK && sibling.left!=null  && sibling.left.color == Constants.BLACK) {
                    sibling.color = Constants.RED;
                    node = node.parent;
                    continue;
                } else if (sibling.left!=null  && sibling.left.color == Constants.BLACK) {
                    sibling.right.color = Constants.BLACK;
                    sibling.color = Constants.RED;
                    rotateLeft(sibling);
                    sibling = node.parent.left;
                }
                if (sibling.left.color == Constants.RED) {
                    sibling.color = node.parent.color;
                    node.parent.color = Constants.BLACK;
                    sibling.left.color = Constants.BLACK;
                    rightRotate(node.parent);
                    node = root;
                }
            }
        }
        node.color = Constants.BLACK;
    }

    public void transplantown(Node u, Node v) {
        if (u.parent == nil)
            root = v;
        else if (u == u.parent.left)
            u.parent.left = v;
        else
            u.parent.right = v;
        v.parent = u.parent;
    }
    void transplant(Node target, Node with){
        if(target.parent == nil){
            root = with;
        }else if(target == target.parent.left){
            target.parent.left = with;
        }else
            target.parent.right = with;
        with.parent = target.parent;
    }

    Node findMinimumInSubtree(Node subTreeRoot) {
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
                return dummyNode.getBuilding().toStringForOutput();
        }
        return "(0,0,0)";
    }

    // wrapper function to print buildings in the given range
    public String printRange(int b1, int b2) {
        StringBuilder b = printRange(root, b1, b2, new StringBuilder());
        //b = (b.length()>1) ? b: new StringBuilder("(0,0,0)");
        //System.out.println(b.substring(0, b.length() - 1));
        return (b.length()>1) ? b.substring(0, b.length() - 1) : "(0,0,0)";
    }

    // recursive function to print buildings in the given range
    // initially called from root node
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
            builder.append(b.toStringForOutput());
            builder.append(',');
            //System.out.print(b.toStringForOutput());
        }

        if (b2 > currBuildingNum) {
            printRange(node.right, b1, b2, builder);
        }
        return builder;
    }
    private final Node nil = new Node(new Building(-1, -1), Constants.BLACK);
    public class Node {
        //public static final Node nil = new Node(new Building(-1, -1));
        Node left = nil, right = nil, parent = nil;
        private Building building;
        int color;
        //private final Node nil = new Node(new Building(-1, -1));

//        @Override
//        public String toString() {
//            return "Node{" +
//                    //"left=" + left +
//                    //", right=" + right +
//                    ", parent=" + parent +
//                    ", building=" + building.getBuildingNum() +
//                    ", color=" + color +
//                    '}';
//        }

        public Node(Building building) {
            this.building = building;
            color = Constants.RED;
//            left = nil;
//            right = nil;
//            parent = nil;
        }

        public Node(Building building, int color) {
            this.building = building;
            this.color = color;
//            this.left = nil;
//            this.right = nil;
//            this.parent = nil;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public Building getBuilding() {
            return this.building;
        }

        public int compareTo(Node o) {
            return this.getBuilding().getBuildingNum() - o.getBuilding().getBuildingNum();
        }
    }
}
