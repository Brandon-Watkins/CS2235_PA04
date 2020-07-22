package edu.isu.cs2235.structures.implementations;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Brandon Watkins
 */
public class Trie {

    private Integer numberOfNodes;
    private Integer numberOfLeaves;
    private TrieNode root;
    private TrieNode pointer;

    public Trie(){
        this.root = new TrieNode(' ', null, false);
        this.numberOfNodes = 0;
        this.numberOfLeaves = 0;
        this.pointer = this.root;
    }

    public Trie(TrieNode node){
        this.root = node;
        TrieNode pointer = node.firstChild();
        for(int i = 0; i < node.numChildren() && pointer != null; i++){
            if (pointer.isEndOfWord()) this.numberOfLeaves++;
            this.numberOfNodes++;
            pointer = pointer.next();
        }
        this.pointer = this.root;
    }

    public void incWordCount(){
        this.numberOfLeaves++;
    }

    public void incNodeCount() { this.numberOfNodes++; }

    public int numberOfNodes(){ return this.numberOfNodes; }

    public int numberOfWords() { return this.numberOfLeaves; }

    /**
     * Current node being searched through(looking for matching children)
     * @return Current node being searched through(looking for matching children)
     */
    public TrieNode pointer() {
        if (this.pointer == null) return this.root;
        return pointer;
    }

    /**
     * Current node being searched through(looking for matching children)
     * @param node The node you want to set as the trie's pointer.
     * @return Current node being searched through(looking for matching children)
     */
    public TrieNode pointer(TrieNode node) { return this.pointer = node; }

    public TrieNode root() {
        return this.root;
    }

    /**
     * Determine if current node is the root node
     * @param node The node in question.
     * @return True if the node in question is the Trie's root node.
     * @throws IllegalArgumentException Ex if node parameter is null.
     */
    public boolean isRoot(TrieNode node) throws IllegalArgumentException {
        if (node == null) {
            throw new IllegalArgumentException("Given node is null.");
        }
        else return this.root() == node;
    }

    /**
     * The number of nodes contained in the entire tree.
     * @return The number of nodes contained in the entire tree(excluding root).
     */
    public int size() {
        return this.numberOfNodes;
    }


    /**
     * Determine whether the trie is empty.
     * @return True if the trie is empty.
     */
    public boolean isEmpty() {
        return this.numberOfNodes == 0;
    }


    /** Tree traversal */

    public boolean hasNext(){
        return this.pointer().next() != null;
    }

    /**
     * Gets the next sibling, and assigns the pointer to it.
     * @return The next sibling.
     */
    public TrieNode next(){
        return this.pointer(this.pointer.next());
    }

    public boolean hasPrev(){
        return this.pointer().prev() != null;
    }

    /**
     * Gets the previous sibling, and assigns the pointer to it.
     * @return The previous sibling.
     */
    public TrieNode prev(){
        return this.pointer(this.pointer().prev());
    }

    /**
     * Add a word to the trie. Intentionally does not add the word to the word file.
     * @param wordToAdd The word you want to add to the trie.
     * @return The last letter's node in the trie.
     */
    public TrieNode add(String wordToAdd){
        this.pointer(this.root());
        // for each new letter...
        for (int currentIndex = 0; currentIndex < wordToAdd.length(); currentIndex++) {
            boolean endOfWord = false;
            if (currentIndex == wordToAdd.length() - 1) endOfWord = true;
            // Create a new child TrieNode with the new letter
            TrieNode newNode = this.pointer().addChild(wordToAdd.charAt(currentIndex), endOfWord);
            this.incNodeCount();
            //mark the letter completes the word, increase the word count.
            if (endOfWord) this.incWordCount();
            // set pointer to the new node
            this.pointer(newNode);
        }
        return this.pointer();
    }

    /**
     * Finds the specified character, within current node's children, or null if not found.
     * @param character The character to search for.
     * @return The node containing the searched character, or null if not found.
     */
    public TrieNode find(Character character, TrieNode pointer){
        if (character == null) return null;
        character = Character.toLowerCase(character);
        int numChildren = pointer.numChildren();
        pointer = pointer.firstChild();
        for (int i = 0; i < numChildren && pointer != null; i++){
            if (character == pointer.value()) return pointer;
            else if (character < pointer.value()) return null;
            else pointer = pointer.next();
        }
        return null;
    }

    /**
     * Finds the specified character, within current pointer node's children, or null if not found.
     * @param character The character to search for.
     * @return The node containing the searched character, or null if not found.
     */
    public TrieNode find(Character character){ return find(character, this.pointer()); }

    /**
     * Finds the specified string, within current node's children, or null if not found.
     * @param string The string to search for, from root.
     * @return The node containing the last character, or null if not found.
     */
    public TrieNode find(String string) { return find(string, this.root()); }

    /**
     * Finds the specified string, within current node's children, or null if not found.
     * @param string The string to search for.
     * @return The node containing the last character, or null if not found.
     */
    public TrieNode find(String string, TrieNode pointer){
        if (string == null) return null;
        string = string.trim().toLowerCase();
        if (string.length() == 0) return null;
        if (string.length() == 1){
            return find(string.charAt(0), pointer);
        }
        String temp = string.substring(1);
        TrieNode locationOfNextLetter = find(string.charAt(0), pointer);
        if (locationOfNextLetter == null) return null;
        return find(temp, locationOfNextLetter);
    }

    /**
     * Tries to find the specified string
     * @param string The string you want to find.
     * @return True if the word is found, and it's last letter completes a word.
     */
    public boolean findWord(String string){
        TrieNode n = find(string);
        if (n != null && n.isEndOfWord()) return true;
        return false;
    }

    /**
     * Searches for words matching a 1-3 letter prefix + search string.
     * @param string the string you want to find word suggestions for.
     * @return A string with 1-3 letters prepended to the search term.
     */
    public String findMissingPrefix(String string){
        if(string.length() == 0) return null;
        //For up to 1 missing letter...
        for(int i = '"'; i <= 'z'; i ++){
            if (i == 'A') i = 'a';
            String temp = Character.toString(i) + string;
            TrieNode n;
            if ((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
        }
        //For up to 2 missing letters...
        for(int i = '"'; i <= 'z'; i ++){
            if (i == 'A') i = 'a';
            for(int j = '"'; j <= 'z'; j++){
                if (j == 'A') j = 'a';
                String temp = Character.toString(i) + Character.toString(j) + string;
                TrieNode n;
                if ((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
            }
        }
        //For up to 3 missing letters...
        for(int i = '"'; i <= 'z'; i ++){
            if (i == 'A') i = 'a';
            for(int j = '"'; j <= 'z'; j++){
                if (j == 'A') j = 'a';
                for(int k = '"'; k <= 'z'; k++){
                    if (k == 'A') k = 'a';
                    String temp = Character.toString(i) + Character.toString(j) + Character.toString(k) + string;
                    TrieNode n;
                    if ((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
                }
            }
        }
        return null;
    }

    /**
     * Searches for words matching the search string + 1-3 letter suffix.
     * @param string the string you want to find word suggestions for.
     * @return A string with 1-3 letters appended to the search term.
     */
    public String findMissingSuffix(String string){
        if(string.length() == 0) return null;
        //For up to 1 missing letter...
        for(int i = '"'; i <= 'z'; i++){
            if (i == 'A') i = 'a';
            String temp = string + Character.toString(i);
            TrieNode n;
            if ((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
        }
        //For up to 2 missing letters...
        for(int i = '"'; i <= 'z'; i++){
            if (i == 'A') i = 'a';
            for(int j = '"'; j <= 'z'; j++){
                if (j == 'A') j = 'a';
                String temp = string + Character.toString(i) + Character.toString(j);
                TrieNode n;
                if ((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
            }
        }
        //For up to 3 missing letters...
        for(int i = '"'; i <= 'z'; i++){
            if (i == 'A') i = 'a';
            for(int j = '"'; j <= 'z'; j++){
                if (j == 'A') j = 'a';
                for(int k = '"'; k <= 'z'; k++){
                    if (k == 'A') k = 'a';
                    String temp = string + Character.toString(i) + Character.toString(j) + Character.toString(k);
                    TrieNode n;
                    if ((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
                }
            }
        }
        return null;
    }

    /**
     * Searches for words matching the search string, minus 1-3 letters(in a row) inside of the word.
     * @param string the string you want to find word suggestions for.
     * @return A string with 1-3 letters removed from the interior of the search term.
     */
    public String findMissingMiddle(String string){
        int stringLength = string.length();
        if(stringLength == 0) return null;
        //for each letter in the given string...
        for(int u = 1; u < stringLength; u++) {
            String leftString = string.substring(0, u);
            String rightString = string.substring(u, string.length());
            //For up to 1 missing letter...
            for (int i = '"'; i < 'z'; i++) {
                if (i == 'A') i = 'a';
                String temp = leftString + Character.toString(i) + rightString;
                TrieNode n;
                if ((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
            }
            //For up to 2 missing letters...
            for (int i = '"'; i < 'z'; i++) {
                if (i == 'A') i = 'a';
                for (int j = '"'; j < 'z'; j++) {
                    if (j == 'A') j = 'a';
                    String temp = leftString + Character.toString(i) + Character.toString(j) + rightString;
                    TrieNode n;
                    if ((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
                }
            }
            //For up to 3 missing letters...
            for (int i = '"'; i < 'z'; i++) {
                if (i == 'A') i = 'a';
                for (int j = '"'; j < 'z'; j++) {
                    if (j == 'A') j = 'a';
                    for (int k = '"'; k < 'z'; k++) {
                        if (k == 'A') k = 'a';
                        String temp = leftString + Character.toString(i) + Character.toString(j) + Character.toString(k) + rightString;
                        TrieNode n;
                        if ((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Searches for words matching the string, minus a 1-3 letter prefix.
     * @param string the string you want to find word suggestions for.
     * @return A string with 1-3 letters removed from the front of the search term.
     */
    public String findExtraPrefix(String string){
        int stringLength = string.length();
        if(stringLength == 0) return null;
        //For up to 3 additional letters...
        for (int o = 1; o < 4 && o < stringLength; o++){
            String temp = string.substring(o);
            TrieNode n;
            if((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
        }
        return null;
    }

    /**
     * Searches for words matching the string, minus a 1-3 letter suffix.
     * @param string the string you want to find word suggestions for.
     * @return A string with 1-3 letters removed from the end of the search term.
     */
    public String findExtraSuffix(String string){
        int stringLength = string.length();
        if(stringLength == 0) return null;
        //For up to 3 additional letters...
        for (int o = 1; o < 4 && o < stringLength; o++){
            String temp = string.substring(0, string.length() - o);
            TrieNode n;
            if((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
        }
        return null;
    }

    /**
     * Searches for words matching the string, minus 1-3 letters(in a row) on the interior of the string.
     * @param string the string you want to find word suggestions for.
     * @return A string with 1-3 letters removed from the interior of the search term.
     */
    public String findExtraMiddle(String string){
        int stringLength = string.length();
        if(stringLength == 0) return null;
        //for each letter in the given string...
        for(int h = 1; h < stringLength - 1; h++){
            String leftString = string.substring(0, h);
            String rightString = string.substring(h, stringLength);
            //For up to 3 additional letters...
            for (int o = 1; o < 4 && h + o < stringLength; o++){
                String leftString2 = string.substring(0, h);
                String rightString2 = string.substring(h + o, stringLength);
                String temp = leftString2 + rightString2;
                if(leftString2.compareTo("Bran") == 0)
                    System.out.print("");
                TrieNode n;
                n = find((temp), this.root());
                if (n != null)
                //if((n = find((temp), this.root())) != null && n.isEndOfWord())
                    return temp;
            }
        }
        return null;
    }

    /**
     * Looks for a possible miss typing of a single character. preference given to last char>first char>middle.
     * @param string The string you want to find a typing suggestion for.
     * @return The adjusted string, or null if none found.
     */
    public String findMissTypedCharacter(String string){
        int stringLength = string.length();
        if(stringLength == 0) return null;
        TrieNode n;
        //check for last character, first, as is probably most common.
        for(int i = 'a'; i <= 'z'; i++){
            String temp = string.substring(0, stringLength - 1) + (char)i;
            if((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
        }
        //check for first character
        for(int i = 'a'; i <= 'z'; i++){
            String temp = (char)i + string.substring(1, stringLength);
            if((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
        }
        //check for miss typed middle char
        for (int j = 1; j < stringLength - 1; j++){
            for(int i = 'a'; i <= 'z'; i++){
                String temp = string.substring(0, j) + (char)i + string.substring(j + 1);
                if((n = find((temp), this.root())) != null && n.isEndOfWord()) return temp;
            }
        }
        return null;
    }

    /**
     * Get an array list of suggestions(up to 5 words), using the different word searches.
     * @param word the word you want to find spelling suggestions for.
     * @return ArrayList of strings containing the spelling suggestions.
     */
    public ArrayList<String> wordSuggestions(String word){
        ArrayList<String> suggestions = new ArrayList<>();
        String suggestion;
        if((suggestion = this.findMissTypedCharacter(word)) != null) suggestions.add(suggestion);
        if((suggestion = this.findExtraPrefix(word)) != null) suggestions.add(suggestion);
        if((suggestion = this.findMissingPrefix(word)) != null) suggestions.add(suggestion);
        if((suggestion = this.findExtraSuffix(word)) != null) suggestions.add(suggestion);
        if((suggestion = this.findMissingMiddle(word)) != null) suggestions.add(suggestion);
        if(suggestions.size() < 5 && (suggestion = this.findMissingSuffix(word)) != null) suggestions.add(suggestion);
        if(suggestions.size() < 5 && (suggestion = this.findExtraMiddle(word)) != null) suggestions.add(suggestion);

        suggestions.add("Manual Entry");
        suggestions.add("Ignore");
        return suggestions;
    }




}
