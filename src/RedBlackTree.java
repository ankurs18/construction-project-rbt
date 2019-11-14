public class RedBlackTree {

    private Node root;

    public RedBlackTree() {
        root = null;
    }

    public void insert(Building buildingData) {
        Node newNode = new Node(buildingData);

        if (root == null) {
            root = newNode;
            newNode.setColor(Constants.BLACK);
            return;
        }
        Node dummyNode = root;
        Node pp = null;
        while (dummyNode != null) {
            pp = dummyNode;
            if (newNode.compareTo(pp) < 0) {
                dummyNode = dummyNode.left;
            } else {
                dummyNode = dummyNode.right;
            }
        }
        newNode.parent = pp;

        if (pp.compareTo(newNode) < 0) {
            pp.right = newNode;
            //System.out.println("toright");
            //System.out.println("p:" + newNode.toString());
            //System.out.println("pp:" + pp.toString());
        } else {
            pp.left = newNode;
            //System.out.println("toleft");
            //System.out.println("p:" + newNode.toString());
            //System.out.println("pp:" + pp.toString());
        }
        rebalance(newNode);
    }

    private void rebalance(Node p) {
        while (p.parent.color == Constants.RED) {
            Node pp = p.parent;
            Node gp = pp.parent;
            if (gp == null)
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
                    rotateLeft(gp);
                }
                break;
                // remaining cases: when gp's other child is red
                default: {
                    gp.left.color = Constants.BLACK;
                    gp.color = Constants.RED;
                    gp.right.color = Constants.BLACK;
                    //pp = gp;
                }
            }
            root.color = Constants.BLACK;

        }
    }


    private void rightRotate1(Node currNode) {
        Node parent = currNode.parent;
        // checking if currNode is the root
        if (parent != null) {
            if (parent.right == currNode) {
                parent.left = currNode.left;
            } else {
                parent.right = currNode.left;
            }
            currNode.left.parent = parent;
            currNode.parent = currNode.left;
            if (currNode.left.right != null) {
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
            left.parent = null;
            root = left;
        }
    }

    private void rightRotate(Node currNode) {
        Node parent = currNode.parent;
        Node left = currNode.left;
        // checking if currNode is the root
        if (parent != null) {
            if (parent.right == currNode) {
                parent.left = left;
            } else {
                parent.right = left;
            }
            left.parent = parent;
        } else {
            left.parent = null;
            root = left;
        }
        currNode.parent = left;
        if (left.right != null) {
            left.right.parent = currNode;
        }
        currNode.left = currNode.left.right;
        currNode.parent.right = currNode;
    }

    void rotateLeft(Node currNode) {
        Node parent = currNode.parent;
        if (parent != null) {
            if (currNode == parent.left) {
                parent.left = currNode.right;
            } else {
                parent.right = currNode.right;
            }
            currNode.right.parent = parent;
            currNode.parent = currNode.right;
            if (currNode.right.left != null) {
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
            right.parent = null;
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
        if (gp.right!=null && gp.right.color == Constants.RED && gp.left!=null && gp.right.color == Constants.RED)
            return "XYr";
        StringBuilder builder = new StringBuilder();
        if (gp.left == pp) {
            builder.append('L');
            if (gp.right !=null && gp.right.color == Constants.RED) builder.append('r');
            else
                builder.append('b');
        } else {
            builder.append('R');
            if (gp.left !=null && gp.left.color == Constants.RED) builder.append('r');
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
    }

    private void printInorder(Node node) {
        if (node == null)
            return;
        printInorder(node.left);
        System.out.print("Building num: " + node.getBuilding().getBuildingNum() + " color: " + node.color + " ");
        printInorder(node.right);
    }
}
