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
 * @param <K> for the key
 * @param <V> for the value
 */
public interface rbtInterface<K extends Comparable<K>, V> 
{

    /**
     *
     * @param key uses the key to identify the node the value was placed in
     * @param value the value we're placing
     */
    public void add(K key, V value);

    /**
     *
     * @param key uses the key to identify which node to delete
     * @return
     */
    public V remove(K key);

    /**
     *
     * @param key uses key to lookup the node we want in order to get the value without removing
     * @return
     */
    public V lookup(K key);

    /**
     *
     * @return a string representation of the tree
     */
    public String toPrettyString();
}