package edu.isu.cs2235.structures.implementations;

import java.lang.Character;
import java.util.Iterator;

/**
 * A class for a node, specific to a Trie<String> data structure.
 * I decided to do sorting and whatnot in this class, versus the tree, because I noticed to have the tree handle things,
 * I'd be sending over extra variables that seemed more relevant to the node class.
 * @implNote The newly made child nodes are auto sorted, in a manner that puts punctuation(./'-) "before" the alphabet.
 * @author Brandon Watkins
 */

public class TrieNode {

    private Character value;
    private TrieNode next;
    private TrieNode prev;
    private TrieNode parent;
    private TrieNode firstChild;
    private TrieNode lastChild;
    private Integer numChildren;
    private boolean endOfWord;

    public TrieNode(Character value, TrieNode parent, boolean endOfWord) {
        this.value = value;
        this.parent = parent;
        this.endOfWord = endOfWord;
        this.numChildren = 0;
    }

    public TrieNode(Character value) {
        this(value, null, false);
    }

    /**
     * Get the string containing the values along the path from root to current node.
     * @return The word spelled by the nodes along the path from root to this node.
     */
    public String toString(){
        if (this == null || this.value() == null) return null;
        TrieNode pathPointer = this;
        String word = "";
        while (pathPointer.parent() != null){
            word = pathPointer.value() + word;
            pathPointer = pathPointer.parent();
        }
        if (word.equals("")) return null;
        return word;
    }

    /**
     * Get the number of letters in the word spelled by the nodes from root to current node.
     * @return Number of letters in the word spelled by the nodes from root to current node.
     */
    public int length(){
        String word =  this.toString();
        if (word != null) return word.length();
        return 0;
    }

    public boolean isEndOfWord(){
        return this.endOfWord;
    }

    /**
     * Get or Set the current node's previous node. No param = get.
     * @return The node before the current node.
     */
    public TrieNode prev() {
        return this.prev;
    }

    /**
     * Get or Set the current node's previous node. No param = get.
     * @param previousNode The node that will be placed before the current node.
     * @return The node before the current node.
     */
    public TrieNode prev(TrieNode previousNode) {
        if (previousNode != null) this.prev = previousNode;
        return this.prev;
    }

    /**
     * Get or Set the current node's next node. No param = get.
     * @param nextNode The node that will be placed after the current node.
     * @return The node after the current node.
     */
    public TrieNode next(TrieNode nextNode) {
        if (nextNode != null) this.next = nextNode;
        return this.next;
    }

    /**
     * Get or Set the current node's next node. No param = get.
     * @return The node after the current node.
     */
    public TrieNode next() {
        return this.next(null);
    }

    /**
     * Get The value stored in the current node.
     * @return The value stored in the current node.
     */
    public Character value() {
        return this.value;
    }

    /**
     * Set The value stored in the current node.
     * @param value The value stored in the current node.
     * @return The value stored in the current node.
     */
    private Character value(Character value) {
        this.value = value;
        return value;
    }

    /**
     * Compares the path's of 2 nodes (their value, in addition to parent's values, from oldest to youngest).
     * Compares the 2 nodes letter by letter, until the end of one of the words.
     * If equal up to that point, the longer word is deemed greater.
     * @param node the 2nd node being compared to the caller node.
     * @return If node 1 is > 2, returns a value greater than 1 (>1). If 1 < 2, returns < 1. If equal, =.
     */
    public int compareTo(TrieNode node) {
        String str1 = this.toString();
        String str2 = node.toString();
        if (str1 == null){
            if (str2 == null ) return 0;
            else return str2.length();
        }
        else if (str2 == null) return str1.length();

        for (int i = 0; i < str1.length() && i < str2.length(); i++){
            int val = ((Character)str1.charAt(i)).compareTo(str2.charAt(i));
            if (val != 0) return val;
        }
        int len = str1.length() - str2.length();
        return len;
    }

    /**
     * Get or Set the parent of this node. no param = get.
     * @param parent The node to make a parent of this node.
     * @return The parent of this node. Null if none (root).
     */
    public TrieNode parent(TrieNode parent) {
        if (parent != null) this.parent = parent;
        return this.parent; // possibly null
    }

    /**
     * Get or Set the parent of this node. no param = get.
     * @return The parent of this node. Null if none (root).
     */
    public TrieNode parent(){
        return this.parent;
    }

    /**
     * Get or Set the first child of the node. no param = get.
     * @param newFirstChild The node to become the first(left-most, smallest) child of this node.
     * @return The first child of the node. Null if no children.
     */
    public TrieNode firstChild(TrieNode newFirstChild) {
        if (newFirstChild != null) this.firstChild = newFirstChild;
        return this.firstChild; // possibly null
    }

    /**
     * Get or Set the first child of the node. no param = get.
     * @return The first child of the node. Null if no children.
     */
    public TrieNode firstChild(){
        return this.firstChild(null);
    }

    /**
     * Get or Set the last child of the node. no param = get.
     * @param newLastChild The node to become the last(right-most, largest) child of this node.
     * @return The first child of the node. Null if no children.
     */
    public TrieNode lastChild(TrieNode newLastChild) {
        if (newLastChild != null) {
            if (this.firstChild != null) {
                this.lastChild = newLastChild;
                return this.lastChild;
            } else {
                this.firstChild = newLastChild;
                return this.firstChild;
            }
        } else {
            if (this.lastChild == null) return this.firstChild; // possibly null
            else return this.lastChild;
        }
    }

    /**
     * Get or Set the last child of the node. no param = get.
     * @return The last child of the node. Null if no children.
     */
    public TrieNode lastChild(){
        return this.lastChild(null);
    }

    /**
     * Determines if the new value is already in the trie.
     * @param value The new value we want to add to the trie.
     * @param parent The parent of the new node we want to add.
     * @param endOfWord Whether or not this value marks the end of the word.
     * @return The existing node if the node already exists(Don't make a new one!). Null if no duplicates found.
     */
    private TrieNode foundDuplicates(Character value, TrieNode parent, boolean endOfWord){
        TrieNode pointer = parent.lastChild();
        for(int i = parent.numChildren; i > 0 && pointer != null && value.compareTo(pointer.value()) <= 0; i--){
            if(pointer.value().compareTo(value) == 0){
                if (endOfWord) pointer.endOfWord = true; //whether it was or wasnt already
                return pointer;
            }
            pointer = pointer.prev();
        }
        return null;
    }

    /**
     * Find the correct pointer to place the new node, so new node > pointer.
     * @param newNode The new node we're trying to place.
     * @param parent The parent of the new node.
     * @return The node that should be directly before the new node, or null if new node should be first child.
     */
    private TrieNode findCorrectPosition(TrieNode newNode, TrieNode parent){
        TrieNode pointer = parent.lastChild();
        // if/while new child is lower than last existing child, move pointer left
        for(int i = parent.numChildren; i > 0 && pointer != null && newNode.value().compareTo(pointer.value()) < 0; i--){
            pointer = pointer.prev();
        }
        return pointer;//could be null
    }

    /**
     * Adds a child node to current node, auto sorting itself if necessary.
     * @param value The character being stored in the new child node.
     * @param endOfWord True if this character(value) completes a word.
     * @return The newly created/added child node.
     */
    public TrieNode addChild(Character value, boolean endOfWord) {
        // making sure the new child isnt already in the tree...
        TrieNode dupe = foundDuplicates(value, this, endOfWord);
        // If we found an existing node with same value/path, just return the existing node, instead of new node.
        if (dupe != null) return dupe;

        // No dupes found, making the newChild node.
        TrieNode newChild = new TrieNode(value, this, endOfWord);
        // if this is the first child added to parent node...
        if (this.firstChild() == null) {
            this.firstChild(newChild);
        }
        // if there's existing children...
        else {
            TrieNode pointer = findCorrectPosition(newChild, this);
            if (pointer == null) {
                newChild.next(this.firstChild());
                this.firstChild().prev(newChild);
                this.firstChild(newChild);
            }
            else if (pointer == newChild.parent.lastChild()){
                TrieNode temp = newChild.prev(this.lastChild());
                temp.next(newChild);
                this.lastChild(newChild);
            } else {
                TrieNode temp = newChild.next(pointer.next());
                temp.prev(newChild);
                newChild.prev(pointer);
                pointer.next(newChild);
            }
        }

        // increase number of children for parent node
        numChildren("++");
        return newChild;
    }

    /**
     * Get or Set the number of this node's direct children. No param = get.
     * @param operator The math operator(In String format) to adjust the numChildren value.
     * @return The number of children to current node.
     */
    public Integer numChildren(String operator){
        if (operator == null) return this.numChildren;
        else if (operator.equals("+") || operator.equals("++")) return ++this.numChildren;
        else if (operator.equals("-") || operator.equals("--")) {
            if (this.numChildren > 0) return --this.numChildren;
            return this.numChildren;
        }
        else return null;
    }

    /**
     * Get or Set the number of this node's direct children. No param = get.
     * @return The number of children to current node.
     */
    public Integer numChildren(){ return this.numChildren(null); }


}

