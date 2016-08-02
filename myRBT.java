/**
 * CS 241: Data Structures and Algorithms II
 * Professor: Edwin Rodríguez
 *
 * Programming Assignment #4
 *
 * <Implement a simple Red Black Tree>
 *
 * Y-Uyen La
 *   
 */
package rbt;
import java.util.ArrayList;

/**
 *
 * @author Y-Uyen
 * @param <K> is the key
 * @param <V> is the value
 */
public class myRBT<K extends Comparable<K>, V> implements Tree<K,V> 
{
    
    /**
     * this creates a global root so that we may access the tree easily
     */
    public Node root;

    
    /**
     *
     * @param key is the key we want to use to identify the node
     * @param value is the value stored inside of the node
     * 
     * This method finds the appropriate placement for the newly added node. Then, it sends the node in to check to see if it falls into any of the test cases
     */
    public void add(K key, V value)
    {
        if(root == null)
        {
            root = new Node(key, value, null, "black");
            root.leftChild = new Node("black");
            root.rightChild = new Node("black");
        }
        else
        {
            Node addToMe = traverse(key);
            Node newNode = new Node(key, value, addToMe, "red");
            if(key.compareTo((K)addToMe.key) < 0)
            {
                addToMe.leftChild = newNode;
                newNode.leftChild = new Node("black");
                newNode.rightChild = new Node("black");
            }
            else
            {
                addToMe.rightChild = newNode;
                newNode.leftChild = new Node("black");
                newNode.rightChild = new Node("black");
            }
            addBalanceTree(newNode);
            
        }
        
    }
    
    /**
     * This method checks to see if the newly added node makes the tree violate the 5 invariants. 
     * If it does, it will check to see what case it caused and sends to to be fixed.
     */
    
    private void addBalanceTree(Node x)
    {
        if(root.rightChild != null && root.leftChild != null ) //need to fix after you add in the first 3 nodes
        {
            if(x.parent.color.equals("black")) //just finishes up the baseCase
            {
                addcase0(x);
            }       
            else if(x.uncle().color.equals("red") && x.parent.color.equals("red")) // case1
            {
                addcase1(x);
            }
            else if(x.uncle().color.equals("black") && external(x) == true) //case3
            {
                addcase3(x);
            }
            else if(x.uncle().color.equals("black") && external(x) == false) //case2
            {
                addcase2(x);
            } 
        }
    }
    
    /**
     * This method doesn't fix anything, it's just another check, and to let the tree know that everything is fine.
     */
    
    private void addcase0(Node x) 
    {
        if(x.parent.color.equals("black") && x.color.equals("red"))
            return;
        if(x.grandParent() != null)
            return;
    }
    
    /**
     * This method fixes the case for when x's uncle is red. 
     * It fixes it by coloring both parent and uncle black and then the grandparent red and then back to black. After this has been done,
     * the grandparent of x is sent recursively back to see if the tree needs additional fixing.
     */
    
    private void addcase1(Node x) // x's uncle is red
    {
        x.parent.color = "black"; // color parent black
        x.uncle().color = "black"; // color uncle black
        x.parent.parent.color = "red"; //color grandparent red and make grandparent x
        
        if(x.parent.parent == root) //if gparent is root, color black
            x.parent.parent.color = "black";
        else
            addBalanceTree(x.parent.parent); //call recursively on grandparent (all the way up the tree)
    }
    
    /**
     * This method is for when x's uncle is black and x is an internal node.
     * This method fixes it by rotating either left/right in order to get it on the outside and then it immediately falls into addcase3
     */
    
    private void addcase2(Node x)
    {
        if(left(x) == true)
        {
            rotateRight(x.parent);
            addcase3(x.rightChild);
        }
        else
        {
            rotateLeft(x.parent);
            addcase3(x.leftChild);
        }
    }
    
    /**
     * This method is for when x's uncle is black and x is an external node.
     * This method fixes it by rotating either left/right on x's parent (or in this case, x's grandparent because we sent in the child of x's parent) 
     * and then coloring it black and quitting the program.
     */
    
    private void addcase3(Node x)
    {
        x.parent.color = "black";
        x.grandParent().color = "red";
        if(left(x) == true)
        {
            rotateRight(x.grandParent());
        }
        else
        {
            rotateLeft(x.grandParent());
        }
    }
    
    /**
     *
     * @param x is sent in to check if the node is an external node to its parent
     * @return true/false
     */
    public boolean external(Node x)
    {
        boolean external = true;
       if(x.key.compareTo(root.key) < 0 || x.key.compareTo(root.key) == 0) //if x is less/equal to the root, it's on the left side of the tree
       {
            if(x.parent.leftChild == x && x.grandParent().leftChild == x.parent)
                external = true;
            else
                external = false;
       }
       else if(x.key.compareTo(root.key) > 0 ) //if x is greater than the root, it's on the right side
       {
           if(x.parent.rightChild == x && x.grandParent().rightChild == x.parent)
               external = true;
           else
               external = false;
       }
       return external;
           
    }
    
    /**
     * This method just checks to see if x is a left node or not
     */
    
    private boolean left(Node x)
    {
        boolean left = true;
        if(!x.equals(root))
        {
            if(x.parent.leftChild.equals(x))
                left = true;
            else
                left = false;
        }
        return left;
                   
            
    }
   
    /**
     * This method rotates the tree to the right, it needs to keep track of the old left node because when you rotate right, the right side gets an extra node.
     * This is achieved by taking the rightChild of the root's left child.
     * It requires x's grandparent node to be the point of rotation.
     */
    
    private void rotateRight(Node x) //working on gparent
    {
        Node oldLeft = x.leftChild;
        nodeSwap(x, oldLeft);
        x.leftChild = oldLeft.rightChild; // moving one node to the right side
        if(oldLeft.rightChild != null)
            oldLeft.rightChild.parent = x;
        oldLeft.rightChild = x;
        x.parent = oldLeft;
    }
    
    
    /**
     * This method rotates the tree to the left, it needs to keep track of the old right node because when you rotate left, the left side gets an extra node.
     * This is achieved by taking the leftChild of the root's left child.
     * It requires x's parent node to be the point of rotation.
     */
    private void rotateLeft(Node x) //working on x's parent
    {
        Node oldRight = x.rightChild;
        nodeSwap(x, oldRight);
        x.rightChild = oldRight.leftChild;
        if(oldRight.leftChild != null)
            oldRight.leftChild.parent = x;
        oldRight.leftChild = x;
        x.parent = oldRight;      
    }
    
    /**
     * This method is needed to help rotate happen correctly. It swaps the old right/left node with the point of rotation and colors the point of rotation red
     */
    private void nodeSwap(Node current, Node old)
    {
        if(current.equals(root))
        {
            root = old;
            root.color = "black";
        }
        else
        {
            if(current == current.parent.leftChild)
                current.parent.leftChild = old;
            else
                current.parent.rightChild = old;
        }
        if(old != null)
            old.parent = current.parent;
        current.color = "red";
    }
    
    /**
     * This method helps the add method to find out where to put the new node
     */
    private Node traverse(K key)
    {
        Node current = root;
        Node prev = null;
       
        while(current.value != null)
        {
            prev = current;
            if(key.compareTo((K)current.key) < 0)
                current = current.leftChild;
            else
                current = current.rightChild;
        }
        return prev;
    }
    
 
    
    /**
     *
     * @param key sends in the key to identify the value to be removed
     * @return the value inside of the node
     * 
     * This method then sends the node with the matching key to another method, bstRemove() to be deleted.
     */
        
    public V remove(K key)
    {
        Node current=root;
        boolean found=false;
        V value=null;
        System.out.println("Trying to remove: " + key);
        while(current.value != null && !found)
        {
            
            if(key.compareTo((K)current.key) < 0)
            {
                if(current.leftChild != null)
                    current = current.leftChild;
            }
            else if(key.compareTo((K)current.key) > 0)
            {
                if(current.rightChild != null)
                    current = current.rightChild;
            }
          
            else if(current.key.equals(key))
            {
                found=true;
                value = (V)current.value;
                bstRemove(current);
            }	
        }
        if(current.value == null && !found)
        {
            System.out.print("Node was not found...");
            return null;
        }
        else
            return value;
    }
    
    
    /**
     * This method follows the BStree's rules for removing nodes. After the node has been removed, it needs to be fixed in order to follow the 5 invariants.
     */
    private void bstRemove(Node x)
    {
        Node child = null; //used in cases2
        if(x == root && x.leftChild.isSentinel() && x.rightChild.isSentinel())
        {
            x.key = null;
            x.value = null;
            x.leftChild = null;
            x.rightChild = null;
            x = null;
        }
        else if(x.isSentinel()) //case 2 , tree isn't empty but has one element. and that one element has to be a sentinel node because every nonempty node in rbt must have sentinels attached
        {
            x = null;
        }
        else if(x.isLeaf() && x.rightChild.value == null && x.leftChild.value == null) //case 3a, x is a leaf, just pluck it but leave a sentinel node behind.
        {
            if(x.parent != null)
            {
                if(left(x) == true)
                {
                    x = new Node(null, null, x.parent, "black"); //made into a leaf (or "double black")
                    x.leftChild = null;
                    x.rightChild = null;
                    x.parent.leftChild = x;
                    rbtRemoveFix(x);
                }
                else
                {
                    x = new Node(null, null, x.parent, "black"); //made into a leaf (or "double black")
                    x.leftChild = null;
                    x.rightChild = null;
                    x.parent.rightChild = x;
                    rbtRemoveFix(x); 
                }
            }
            else if(x == root)//trying to remove root
            {
                x.key = null;
                x.value = null;
                x.leftChild = null;
                x.rightChild = null;
                x = null;
            }
        }
        else if(x.rightChild.isSentinel() || x.leftChild.isSentinel()) //case3b x has only one non sentinel child
        {
            if(x.rightChild.isSentinel())
            {
                child = x.leftChild;
            }
            else
            {
                child = x.rightChild;
            }
            
            if(child.color.equals("red"))
                child.color = x.color();
            nodeSwap(x, child);
            x.key = null;
            x.value = null;
            x.color = "";
            
        }
        else if(x.leftChild != null && x.rightChild != null)
        {
            Node predecessor = predecessor(x);
            x.key = predecessor.key;
            x.value = predecessor.value;
            bstRemove(predecessor);
        }
    }
    
    
    
    /**
     * This method determines what needs to be done in order to fix the tree after a node has been removed.
     */
    private void rbtRemoveFix(Node x)
    {
        if(x.equals(root) && x.rightChild.isLeaf() && x.leftChild.isSentinel() )
            root.color = "black";
        else if(x.equals(root) && x.leftChild.isLeaf() && x.rightChild.isSentinel() )
            root.color = "black";
        else if(x.equals(root) && x.rightChild.isLeaf() && x.leftChild.isLeaf())
        {
            x.rightChild.color = "black";
            x.leftChild.color = "black";
        }
        else if(x.equals(root) && !x.rightChild.isLeaf() && !x.leftChild.isLeaf())
        {
            root.color = "black";
        }
        else if(x.sibling().isSentinel())
            return;
        else if(x.sibling().color.equals("red"))
            remCase1(x);
        else if(x.parent.color.equals("black") && x.sibling().color.equals("black") && x.sibling().leftChild.color.equals("black") && x.sibling().rightChild.color.equals("black")) 
            remCase2(x);
        else if(x.parent.color.equals("red") && x.sibling().color.equals("black") && x.sibling().leftChild.color.equals("black") && x.sibling().rightChild.color.equals("black"))
            remCase2(x);
        else if(x.sibling().color.equals("black") && internalChild(x.sibling()).color.equals("red") && externalChild(x.sibling()).color.equals("black"))
            remCase3(x);
        else if(x.sibling().color.equals("black") && externalChild(x.sibling()).color.equals("red") )
            remCase4(x);  
    }
    
    /**
     * This method is for when the node you removed which we will call x, has a sibling that is red
     * it fixes it by rotating it appropriately and recoloring its parent and sibling.
     */
    private void remCase1(Node x)
    {
        //recolor b and c
        x.parent.color = "red";
        x.sibling().color = "black";
        if(left(x))
            rotateLeft(x.parent);
        else
            rotateRight(x.parent);
        rbtRemoveFix(x);
    }
    
    /**
     * This method is for when x's sibling w is black and w's children are black.
     * It also checks for the parent's color as well.
     * it fixes it by recoloring the parent and sibling and then calls on x's parent recursively
     */
    private void remCase2(Node x)
    {
        if(x.parent.color.equals("black") && x.sibling().color.equals("black") && x.sibling().leftChild.color.equals("black") && x.sibling().rightChild.color.equals("black"))
        {
            x.sibling().color = "red";
            x.parent.color = "black";
            
        }
        else if (x.parent.color.equals("red") && x.sibling().color.equals("black") && x.sibling().leftChild.color.equals("black") && x.sibling().rightChild.color.equals("black"))
        {
            x.sibling().color = "red";
            x.parent.color = "black";
        }
        else
            rbtRemoveFix(x.parent);

    }
    
    /**
     * This method is for when x's sibling w is black and w's internal child is red and w's external child is black
     * it fixes it by rotating it appropriately and recoloring its parent and sibling and then falling into case 4.
     */
    private void remCase3(Node x)
    {
        if(left(x))
        {
            x.parent.rightChild.leftChild.color = "black"; //color d black
            x.parent.rightChild.color = "red"; //color c red
            rotateRight(x.parent.rightChild);
        }
        else
        {
            x.parent.leftChild.rightChild.color = "black";
            x.parent.leftChild.color = "red";
            rotateLeft(x.parent.leftChild);
        }
        remCase4(x);
    }
    
    /**
     * This method is for when x's sibling w is black and w's external child is red
     * It fixes it by rotating it appropriately and recoloring its parent and sibling.
     * Case 4 is terminal, but just to be sure, we send in the root again to make sure everything is okay.
     */
    private void remCase4(Node x)
    {
        if(left(x) == true)
        {
            String temp = x.parent.rightChild.color();
            x.parent.rightChild.color = x.parent.color(); //c becomes b's color
            x.parent.color = temp;
            externalChild(x.sibling()).color = "black"; //color d black
            rotateLeft(x.parent);
        }
        else
        {
            colorSwap(x.parent, x.sibling());
            externalChild(x.sibling()).color = "black";
            rotateRight(x.parent);
            colorSwap(x.parent, x.sibling());
        }
        rbtRemoveFix(root);
            
    }
    
    /**
     * This method swaps the colors of two nodes
     */
    private void colorSwap(Node n1, Node n2)
    {
        String temp = n1.color();
        n1.color = n2.color();
        n2.color = temp;
    }

     /**
     * This method returns the internal child of x in order to check the color (in rbtRemoveFix)
     */
    private Node internalChild(Node x)
    {
        Node current = x;
        if(left(current))
            current = current.rightChild;
        else
            current = current.leftChild;
        return current;
    }
    
    /**
     * This method returns the external child of x in order to check the color (in rbtRemoveFix)
     */
    private Node externalChild(Node x)
    {
        Node current = x;
        if(left(current))
            current = current.leftChild;
        else
            current = current.rightChild;
        return current;
    }
    
    
    
    private void keyValSwap(Node n1, Node n2) 
    {
        K tempK = (K) n1.key;
        V tempV = (V) n1.value;
        n1.key = n2.key;
        n2.key = tempK;
        n1.value = n2.value;
        n2.value = tempV;
    }
    
    private Node predecessor(Node x) //its in-order predecessor
    {
        Node current = null;
        if(left(x))
        {
           current = x.leftChild;
            if(current != null)
            {
                while(!current.rightChild.isSentinel())
                {
                    current = current.rightChild;
                }
            }
        }
        else
        {
            current = x.rightChild;
            if(current != null)
            {
                while(!current.leftChild.isSentinel())
                {
                    current = current.leftChild;
                }
            }
        }
        
        return current;
    }
    
    /**
     *
     * @param key sends in the key to identify the node
     * @return the value inside of the key
     */
    public V lookup(K key)
    {
        Node current=root;
        boolean found=false;
        V value=null;
        System.out.println("Trying to find the value for: " + key);
        while(!current.isSentinel() && !found)
        {
            if(key.compareTo((K)current.key) < 0)
            {
                if(current.leftChild != null)
                    current = current.leftChild;
            }
            else if(key.compareTo((K)current.key) > 0)
            {
                if(current.rightChild != null)
                    current = current.rightChild;
            }
            
            else if(current.key.equals(key))
            {
                found=true;
                value = (V)current.value;
            }	
        }
        if(found == false)
        {
            return (V)"not in rbt.";
        }
        else
            return value;
    }
    
    /**
     *
     * @param x find out the depth of the tree
     * @return the depth of the tree
     */
    public int depth(Node x) 
    {
        if (x == null) 
           return 0;
        else 
        {
            int leftSide = depth(x.leftChild);
            int rightSide = depth(x.rightChild);
            if (leftSide > rightSide) 
            {
                return leftSide + 1;
            } 
            else 
            {
                return rightSide + 1;
            }
        }
    }

    /**
     *
     * @return a string representation of the tree
     * 
     * This method uses a 2d array to first store the information and then uses a string to hold all of the values
     */
    public String toPrettyString() 
    {
        int depth = depth(root);
        String result = "";
        Node[][] tree = new Node[depth][(int) Math.pow(2, depth)];
        tree[0][0] = root;
        //stores info into the 2d array
        for (int m = 1; m < depth; m++) 
        {
            for (int n = 0; n < Math.pow(2, m - 1); n++) 
            {
                if (tree[m - 1][n] != null) 
                {
                    tree[m][2 * n] = tree[m - 1][n].leftChild;
                    tree[m][(2 * n) + 1] = tree[m - 1][n].rightChild;
                }
            }
        }

        String gap = "    ";
        System.out.println();
        int levels = depth - 1;
        for (int i = 0; i < depth; i++) 
        {
            result = result + "\n"; //for the levels of the tree
            for (int x = 0; x < (Math.pow(2, (levels)) - Math.pow(2, i)) / 2; x++) 
            {
                result = result + gap;
            }

            for (int j = 0; j < Math.pow(2, i); j++) 
            {
                if (tree[i][j] != null)
                {
                    result = result +"" + "(" + tree[i][j].key().charAt(0) + "," + tree[i][j].color().charAt(0) + ")" + "  ";   
                }
            }
        }

        return result;
    }
    
    /**
     *
     * @param <K> for the key
     * @param <V> for the value
     */
    public class Node<K extends Comparable<K>,V>
    {
        protected Node parent,

        /**
         * keeps track of left Child
         */
        leftChild,

        /**
         * keeps track of right child
         */
        rightChild, dummyChild;

        /**
         * keeps track of the value
         */
        protected V value;

        /**
         * keeps track of the key
         */
        public K key;

        /**
         * keeps track of the color
         */
        protected String color;
        
        
        Node(K key, V value, Node parent, String color)
        {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.color = color;
            this.leftChild = new Node("black");
            this.rightChild = new Node("black");
        }
        
        Node(String color) //to make empty leaves//sentinels
        {
            this.color = color;
            this.key = null;
            this.value = null;
        }
        
        /**
         *
         * @param key stores the key
         * @param value stores the value
         * @param color stores the color
         */
        public void dummy(K key, V value, String color) //creates dummy value for toPrettyString
        {
            this.key = key;
            this.value = value;
            this.color = color;
        }

        /**
         *
         * @return the color of the node
         */
        public String color()
        {
            return this.color;
        }

        /**
         *
         * @return the key of the node
         */
        public String key()
        {
            return "" + this.key;
        }
        
        /**
         *
         * @return the node's parent
         */
        public Node parent()
        {
            if(this.parent != null)
                return this.parent;
            else
                return null;
        }
       
        /**
         *
         * @return the node's sibling
         */
        public Node sibling()
        {
            if(this.parent != null)
            {
                if(this == parent.leftChild)
                    return parent.rightChild;
                else
                    return parent.leftChild;
            }
            else
                return null;
        }
    
        /**
         *
         * @return the node's grandparent
         */
        public Node grandParent()
        {
            if(this.parent.parent != null)
                return this.parent.parent;
            else
                return null;
        }
        
        /**
         *
         * @return the node's uncle
         */
        public Node uncle()
        {
            if(this.parent.sibling() != null)
                return this.parent.sibling();
            else
                return null;
        }

        /**
         *
         * @return if the node is a leaf or not
         * it is a leaf when it's children are null
         */
        public boolean isLeaf()
        {
            if(this.leftChild.key == null && this.leftChild.value == null && this.rightChild.key == null && this.rightChild.value == null)
                    return true;
            else 
                    return false;
        }

        /**
         *
         * @return if it is a sentinel or not
         * it is a sentinel if it is null 
         */
        public boolean isSentinel()
        {
            if(this.color.equals("black") && this.value == null && this.key == null && this.leftChild == null && this.rightChild == null)
                return true;
            else
                return false;
        }
        
    }
}



