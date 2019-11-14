
public class Main {
    public static void main(String[] args)
    {
        //Building one = new Building(50, 20);
        RedBlackTree rbtree = new RedBlackTree();
        rbtree.insert(new Building(50, 20));
        //rbtree.print();
        //rbtree.insert(new Building(20, 20));
        //rbtree.insert(new Building(25, 20));
        //rbtree.print();
        rbtree.insert(new Building(60, 20));
        //rbtree.insert(new Building(15, 20));
        rbtree.insert(new Building(70, 20));
        //rbtree.insert(new Building(50, 20));
        rbtree.print();
    }
}
