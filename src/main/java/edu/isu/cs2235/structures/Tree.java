package edu.isu.cs2235.structures;

import edu.isu.cs2235.structures.implementations.Node;

import java.util.Iterator;

/**
 * @author Brandon Watkins
 * @param <E> Element type
 */
public interface Tree<E extends Comparable> extends Iterable<E>  {

    /**
     * Pointer to the root of the tree.
     * @return The node that is the root of the tree.
     */
    Node<E> root();

    /**
     * Determines whether this node is the root of the tree.
     * @param node The node in question.
     * @return True if node is the root of its tree.
     */
    boolean isRoot(Node<E> node) throws IllegalArgumentException;

    /**
     * Pointer to this node's parent.
     * @param node The node in question.
     * @return The node that is this node's parent.
     */
    Node<E> parent(Node<E> node) throws IllegalArgumentException;

    /**
     * The number of children to this node.
     * @param node The node in question.
     * @return The number of this node's direct children.
     */
    int numChildren(Node<E> node) throws IllegalArgumentException;

    /**
     * Determines if this node has children.
     * @param node The node in question.
     * @return True if this node has children nodes.
     */
    boolean isInternal(Node<E> node) throws IllegalArgumentException;

    /**
     * Determines if this node is a leaf(has no children nodes).
     * @param node Thew node in question.
     * @return True if this node has no children nodes.
     */
    boolean isExternal(Node<E> node) throws IllegalArgumentException;

    /**
     * Determine the number of nodes in the tree.
     * @return The number of nodes in the tree
     */
    int size();

    /**
     * Determine if the tree is empty(no root and/or nodes).
     * @return True if the tree is empty.
     */
    boolean isEmpty();

    /**
     * Create an iterable list of the specified node's children nodes.
     * @param node The node in question.
     * @return Iterable list of children nodes.
     */
    Iterable<Node<E>> children(Node<E> node) throws IllegalArgumentException;

    /**
     * Create an iterable list of all nodes in the tree.
     * @return An iterable list of all nodes in the tree.
     */
    Iterable<Node<E>> nodeList();

    /**
     * Create an iterator to navigate a n iterable list of nodes.
     * @return An iterator to navigate an iterable list of nodes.
     */
    Iterator<E> iterator();

}
