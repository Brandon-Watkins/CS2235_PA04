package edu.isu.cs2235.structures.implementations;

import jdk.jshell.spi.ExecutionControl;

public class Node<E extends Comparable> implements Comparable {

    private Node<E> next;
    private Node<E> prev;
    private E value;

    public Node(E value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value.toString();
    }

    /**
     * Move "pointer" to previous node.
     * @return The Node previous to current node.
     */
    public Node getPrev() {
        return this.prev;
    }

    /**
     * Set the current node's previous node.
     * @param previousNode The node that you want to be placed before current node.
     */
    public void setPrev(Node<E> previousNode) {
        this.prev = previousNode;
    }

    /**
     * Set the current node's next node.
     * @param nextNode The node that you want to be placed after current node.
     */
    public void setNext(Node<E> nextNode) {
        this.next = nextNode;
    }

    /**
     * Move "pointer" to next node.
     * @return The node after the current node.
     */
    public Node getNext() {
        return this.next;
    }

    /**
     * Get The value stored in the current node.
     * @return The value stored in the current node.
     */
    public E getValue() {
        return this.value;
    }

    /**
     * Set the value stored in the current node.
     * @param value The value you want stored in the current node.
     */
    public void setValue(E value) {
        this.value = value;
    }

    @Override
    public int compareTo(Object o) {
        return this.value.compareTo(((Node<E>)o).getValue());
    }
}
