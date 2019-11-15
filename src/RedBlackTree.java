import java.util.zip.CheckedOutputStream;

public class RedBlackTree {


    private Node root;
    public RedBlackTree() {
        root = Node.nil;
    }

    public void insert(Building buildingData) {
        Node newNode = new Node(buildingData);

        if (root == Node.nil) {
            root = newNode;
            newNode.setColor(Constants.BLACK);
            return;
        }
        Node dummyNode = root;
        Node pp = Node.nil;
        while (dummyNode != Node.nil) {
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
        //p.parent!=null &&
        while (p.parent.color == Constants.RED) {
            Node pp = p.parent;
            Node gp = pp.parent;
            if (gp == Node.nil)
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
        if (node.parent != Node.nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != Node.nil) {
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
            left.parent = Node.nil;
            root = left;
        }
    }

    private void rightRotatemod(Node currNode) {
        Node parent = currNode.parent;
        // checking if currNode is the root
        if (parent != Node.nil) {
            if (parent.right == currNode) {
                parent.left = currNode.left;
            } else {
                parent.right = currNode.left;
            }
            currNode.left.parent = parent;
            currNode.parent = currNode.left;
            if (currNode.left.right != Node.nil) {
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
            left.parent = Node.nil;
            root = left;
        }
    }

    private void rightRotate1(Node currNode) {
        Node parent = currNode.parent;
        Node left = currNode.left;
        // checking if currNode is the root
        if (parent != Node.nil) {
            if (parent.right == currNode) {
                parent.left = left;
            } else {
                parent.right = left;
            }
            left.parent = parent;
        } else {
            left.parent = Node.nil;
            root = left;
        }
        currNode.parent = left;
        if (left.right != Node.nil) {
            left.right.parent = currNode;
        }
        currNode.left = currNode.left.right;
        currNode.parent.right = currNode;
    }

    void rotateLeft(Node currNode) {
        Node parent = currNode.parent;
        Node right = currNode.right;
        if (parent != Node.nil) {
            if (currNode == parent.left) {
                parent.left = right;
            } else {
                parent.right = right;
            }
            right.parent = parent;
        } else {
            right.parent = Node.nil;
            root = right;
        }
        currNode.parent = right;
        if (right.left != Node.nil) {
            right.left.parent = currNode;
        }
        currNode.right = currNode.right.left;
        currNode.parent.left = currNode;
    }

    void rotateLeft1(Node currNode) {
        Node parent = currNode.parent;
        if (parent != Node.nil) {
            if (currNode == parent.left) {
                parent.left = currNode.right;
            } else {
                parent.right = currNode.right;
            }
            currNode.right.parent = parent;
            currNode.parent = currNode.right;
            if (currNode.right.left != Node.nil) {
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
            right.parent = Node.nil;
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
        if (gp.right != Node.nil && gp.right.color == Constants.RED && gp.left != Node.nil && gp.right.color == Constants.RED)
            return "XYr";
        StringBuilder builder = new StringBuilder();
        if (gp.left == pp) {
            builder.append('L');
            if (gp.right != Node.nil && gp.right.color == Constants.RED) builder.append('r');
            else
                builder.append('b');
        } else {
            builder.append('R');
            if (gp.left != Node.nil && gp.left.color == Constants.RED) builder.append('r');
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
        if (node == Node.nil)
            return;
        printInorder(node.left);
        System.out.print("Building num: " + node.getBuilding().getBuildingNum() + " color: " + node.color + " ");
        printInorder(node.right);
    }

    public boolean deleteNode(Node nodeToDelete) {
        nodeToDelete = search(nodeToDelete);
        if (nodeToDelete == Node.nil)
            return false;

        Node y = nodeToDelete, x;
        int originalColorN = nodeToDelete.color;

        if (nodeToDelete.left == Node.nil && nodeToDelete.right == Node.nil) {
            if (nodeToDelete.parent.left == nodeToDelete){
                nodeToDelete.parent.left=Node.nil;
            }
            else
                nodeToDelete.parent.right=Node.nil;
            x=Node.nil;
        }

        else if (nodeToDelete.left == Node.nil) {
            x = nodeToDelete.right;
            transplant(nodeToDelete, nodeToDelete.right);
        } else if (nodeToDelete.right == Node.nil) {
            x = nodeToDelete.left;
            transplant(nodeToDelete, nodeToDelete.left);
        } else {
            // finding the minimum in the right subtree to replace nodeToDelete
            y = findMinimumInSubtree(nodeToDelete.right);
            originalColorN = y.color;
            x = y.right;
            if (y.parent == nodeToDelete) {
                if(x!=Node.nil) x.parent = y ;
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
            deleteRebalance(x);
        return true;
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
                if (sibling.left.color == Constants.BLACK && sibling.right.color == Constants.BLACK) {
                    sibling.color = Constants.RED;
                    node = node.parent;
                    continue;
                } else if (sibling.right.color == Constants.BLACK) {
                    sibling.left.color = Constants.BLACK;
                    sibling.color = Constants.RED;
                    rightRotate(sibling);
                    sibling = node.parent.right;
                }
                if (sibling.right.color == Constants.RED) {
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
                if (sibling.right.color == Constants.BLACK && sibling.left.color == Constants.BLACK) {
                    sibling.color = Constants.RED;
                    node = node.parent;
                    continue;
                } else if (sibling.left.color == Constants.BLACK) {
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

    public void transplant(Node u, Node v) {
        if (u.parent == Node.nil)
            root = v;
        else if (u == u.parent.left)
            u.parent.left = v;
        else
            u.parent.right = v;
        v.parent = u.parent;
    }

    Node findMinimumInSubtree(Node subTreeRoot) {
        while (subTreeRoot.left != Node.nil) {
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }

    public Node search(Node nodeToSearch) {
        if (root == Node.nil)
            return Node.nil;
        Node dummyNode = root;
        while (dummyNode != Node.nil) {
            if (nodeToSearch.compareTo(dummyNode) < 0) {
                dummyNode = dummyNode.left;
            } else if (nodeToSearch.compareTo(dummyNode) > 0) {
                dummyNode = dummyNode.right;
            } else
                return dummyNode;
        }
        return Node.nil;
    }
}
