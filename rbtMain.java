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

/**
 *
 * @author Y-Uyen
 */
public class  rbtMain{

    /**
     * @param args the command line arguments
     * This is the main method which is just here to test the RBT
     */
    public static void main(String[] args) {
       myRBT<Integer,String> test = new myRBT<Integer , String>();
       
       test.add(9, "hello");
       test.add(8, "chicken"); 
       test.add(7, "watermelon");
       test.add(7, "what");
       test.add(3, "hellur");
       test.add(5, "hur");
       test.add(2, "hur");

      System.out.println("looking up 5's value, it is: " + test.lookup(5));
       

//
//
//       System.out.println(test.remove(2));
//       System.out.println(test.remove(5));
//      System.out.println(test.remove(3));
//       System.out.println(test.remove(7));
//       System.out.println(test.remove(8));
//       System.out.println(test.remove(9));
//       System.out.println(test.remove(15));

//       test.add(44, "hapr");
//       test.add(48, "hapr");
//       test.add(43, "hapr");

//       System.out.println(test.remove(70)+"p");
//       System.out.println(test.remove(85)+"p");
//       System.out.println(test.remove(36)+"p");
//       System.out.println(test.remove(100)+"p");
//       System.out.println(test.remove(14)+"p");
//       System.out.println(test.remove(56)+"p");
//        test.add(24, "hapr");
//       test.add(17, "hapr");
//       test.add(10, "hapr");
//       test.remove(43);
//       test.remove(10);
//       test.remove(17);
    
       
       
       System.out.print(test.toPrettyString());
       System.out.println();
    }
    
}
