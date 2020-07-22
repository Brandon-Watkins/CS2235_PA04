package edu.isu.cs2235.structures.implementations;

import edu.isu.cs2235.structures.List;

import java.util.Iterator;

/**
 * @author Brandon Watkins
 * @param <E> Element type
 */
public class DoublyLinkedList<E extends Comparable> implements edu.isu.cs2235.structures.List<E>, Iterable<E> {

    private Node<E> tail;
    private Node<E> head;
    private int length;

    public DoublyLinkedList() {
        this.tail = new Node("Tail Sentinel");
        this.head = new Node("Head Sentinel");
        this.tail.setNext(null);
        this.tail.setPrev(head);
        this.head.setNext(tail);
        this.head.setPrev(null);
        this.length = 0;
    }

    /**
     * increases length, aka size
     */
    private void incLength() {
        this.length++;
    }

    /**
     * decreases length, aka size
     */
    private void decLength() {
        this.length--;
    }

    public Node<E> getHead() { return this.head; }
    public Node<E> getTail() { return this.tail; }

    /**
     * @return first element in the list or null if the list is empty.
     */
    @java.lang.Override
    public E first() {
        if (isEmpty()) return null;
        Node<E> next = this.head.getNext();
        E nextVal = next.getValue();
        return nextVal;
    }

    /**
     * @return last element in the list or null if the list is empty.
     */
    @java.lang.Override
    public E last() {
        if (!isEmpty())
            return (E)this.tail.getPrev().getValue();
        else
            return null;
    }

    /**
     * Adds the provided element to the end of the list, only if the element is
     * not null.
     *
     * @param element Element to be added to the end of the list.
     */
    @java.lang.Override
    public void addLast(E element) {
        if (!isNull(element)) {
            Node<E> node = new Node<E>(element);
            node.setNext(this.tail);
            node.setPrev(this.tail.getPrev());
            this.tail.getPrev().setNext(node);
            this.tail.setPrev(node);
            incLength();
        }
    }

    /**
     * Adds the provided element to the front of the list, only if the element
     * is not null.
     *
     * @param element Element to be added to the front of the list.
     */
    @java.lang.Override
    public void addFirst(E element) {
        if (!isNull(element)) {
            Node<E> node = new Node<>(element);
            node.setNext(this.head.getNext());
            node.setPrev(this.head);
            this.head.getNext().setPrev(node);
            this.head.setNext(node);
            incLength();
        }
    }

    /**
     * Removes the element at the front of the list.
     *
     * @return Element at the front of the list(that was just removed), or null if the list is empty.
     */
    @java.lang.Override
    public E removeFirst() {
        if (!isEmpty()) {
            Node<E> firstNode = this.head.getNext();
            E value = firstNode.getValue();
            Node<E> secondNode = firstNode.getNext();
            firstNode.setNext(null);
            firstNode.setPrev(null);
            this.head.setNext(secondNode);
            secondNode.setPrev(this.head);
            decLength();
            return value;
        } else return null;
    }

    /**
     * Removes the element at the end of the list.
     *
     * @return Element at the end of the list(that was just removed), or null if the list is empty.
     */
    @java.lang.Override
    public E removeLast() {
        if (!isEmpty()) {
            Node<E> lastNode = this.tail.getPrev();
            E value = lastNode.getValue();
            this.tail.setPrev(lastNode.getPrev());
            lastNode.getPrev().setNext(this.tail);
            lastNode.setPrev(null);
            lastNode.setNext(null);
            decLength();
            return value;
        } else return null;
    }

    /**
     * Inserts the given element into the list at the provided index. The
     * element will not be inserted if either the element provided is null or if
     * the index provided is less than 0. If the index is greater than or equal
     * to the current size of the list, the element will be added to the end of
     * the list.
     *
     * @param element Element to be added (as long as it is not null).
     * @param index   Index in the list where the element is to be inserted.
     */
    @java.lang.Override
    public void insert(E element, int index) {
        if (!isNull(element) && index >= 0) {
            if (index > this.length) index = this.length;
            Node<E> oldIndexNode;
            Node<E> node = new Node<>(element);
            /** find the index node that will be moved to make room for inserted node. */
            /** if Index is less than half of the list size, search from head, otherwise search bacwards from tail. */
            if (index < this.length / 2){
                oldIndexNode = this.head;
                for (int i = 0; i <= index; i++) {
                    oldIndexNode = oldIndexNode.getNext();
                }
            }
            else {
                oldIndexNode = this.tail;
                for (int i = this.length; i > index; i--) {
                    oldIndexNode = oldIndexNode.getPrev();
                }
            }

            /** insert the node, and scoot over the old index node */
            node.setPrev(oldIndexNode.getPrev());
            oldIndexNode.getPrev().setNext(node);
            oldIndexNode.setPrev(node);
            node.setNext(oldIndexNode);

            incLength();
        }
    }

    /**
     * Removes the element at the given index and returns the value.
     *
     * @param index Index of the element to remove
     * @return The value of the element at the given index, or null if the index
     * is greater than or equal to the size of the list or less than 0.
     */
    @java.lang.Override
    public E remove(int index) {
        if (index <= this.length - 1 && index >= 0) {
            Node<E> nodeToRemove;
            /** find the index node that will be removed */
            /** if Index is less than half of the list size, search from head, otherwise search backwards from tail. */
            if (index < this.length / 2){
                nodeToRemove = this.head;
                for (int i = 0; i <= index; i++) {
                    nodeToRemove = nodeToRemove.getNext();
                }
            }
            else {
                nodeToRemove = this.tail;
                for (int i = this.length; i > index; i--) {
                    nodeToRemove = nodeToRemove.getPrev();
                }
            }
            /** remove the node, adjusting adjacent nodes accordingly. */
            nodeToRemove.getPrev().setNext(nodeToRemove.getNext());
            nodeToRemove.getNext().setPrev(nodeToRemove.getPrev());
            E value = nodeToRemove.getValue();
            nodeToRemove.setPrev(null);
            nodeToRemove.setNext(null);

            decLength();
            return value;
        } else return null;
    }

    /**
     * Retrieves the value at the specified index. Will return null if the index
     * provided is less than 0 or greater than or equal to the current size of
     * the list.
     *
     * @param index Index of the value to be retrieved.
     * @return Element at the given index, or null if the index is less than 0
     * or greater than or equal to the list size.
     */
    @java.lang.Override
    public E get(int index) {
        if (index >= 0 && index < this.length) {
            Node<E> node;
            /** find the index node that will be moved to make room for inserted node. */
            /** if Index is less than half of the list size, search from head, otherwise search bacwards from tail. */
            if (index < this.length / 2){
                node = this.head;
                for (int i = 0; i <= index; i++) {
                    node = node.getNext();
                }
            }
            else {
                node = this.tail;
                for (int i = this.length; i > index; i--) {
                    node = node.getPrev();
                }
            }
            return node.getValue();
        } else return null;
    }

    /**
     * @return The current size of the list. Note that 0 is returned for an
     * empty list.
     */
    @java.lang.Override
    public int size() {
        return this.length;
    }

    /**
     * @return true if there are no items currently stored in the list, false
     * otherwise.
     */
    @java.lang.Override
    public boolean isEmpty() {
        if (size() >= 1) return false;
        else return true;
    }

    @java.lang.Override
    public void printList() {
        E msg;
        Node<E> node = this.head;
        for (int i = 0; i < this.length; i++) {
            node = node.getNext();
            msg = node.getValue();
            System.out.print(msg + "\r\n");
        }
    }

    public boolean isNull(E element) {
        if (element == null || element == "") return true;
        else return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new DoublyLinkedListIterator<E>(this);
    }

    private class DoublyLinkedListIterator<E extends Comparable> implements Iterator<E>{

        Node<E> pointer;

        public DoublyLinkedListIterator(DoublyLinkedList<E> list){ this.pointer = list.head.getNext(); }

        @Override
        public boolean hasNext() { return pointer != null; }

        @Override
        public E next() {
            E value = pointer.getValue();
            pointer = pointer.getNext();
            return value;
        }
    }
}
