package edu.isu.cs2235.structures.implementations;

import java.io.*;
import java.util.ArrayList;
import edu.isu.cs2235.structures.implementations.TrieNode;
import edu.isu.cs2235.structures.implementations.Trie;

/**
 * A class meant to read in the word list from a text file, and insert the characters into corresponding nodes in a trie.
 * @implNote The reader assumes that each line of text in the file equates to a word. Will not take a CSV file.
 * @implNote When adding a new word, I decided to backtrack through the ancestors of the last added letter until I reach
 * a shared node between the previous word and the next word to add to the tree, instead of starting at root, resulting
 * in filling the trie 10% faster.
 * @author Brandon Watkins
 */
public class FillTree {
    Trie tree;
    String prevWord;
    String currentWord;
    boolean out = false;//To show how long it takes to fill the tree.

    public FillTree(){
        this.tree = new Trie();
        this.prevWord = null;
        this.currentWord = null;
    }

    public Trie readInFile(String filePath) throws IOException {
        try {
            long time = System.currentTimeMillis();
            System.out.println("\r\nLoading word list...");
            //Open word list file
            File file = new File(filePath);
            BufferedReader fileReader = new BufferedReader(new FileReader(file), 50000);
            String[] currentLine;
            this.tree = new Trie();
            while (fileReader.ready() && (currentLine = fileReader.readLine().split(",")) != null){
                //For each word in the word list file...
                for (int w = 0; w < currentLine.length; w++) {

                    if (incorrectlyFormattedWord(currentLine, w, filePath)) continue;

                    int numberOfGenerationsToMoveDownTrie = determineGenerationsToLastSharedNode();

                     pointToSharedNode(numberOfGenerationsToMoveDownTrie);

                    addWordToTrie();
                }
            }
            fileReader.close();
            if (out) System.out.println("\r\nWord list loaded, in " + (System.currentTimeMillis() - time) + "ms.");
            else System.out.println("\r\nWord list loaded.");
            return this.tree;
        }
        catch (Exception e){
            System.out.println("Error trying to read file " + filePath + ".");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Formats the current word, determining if using json wordlist and removing quotes, and trims white space from
     * currentWord. If currentWord ends in "-", it is just a prefix marker for the wordlist. If the word is a single
     * character, only 'a' and 'i' are considered words, unsure why the word lists all consider 'b' a word, etc.
     * @param currentLine The current line being read from the file.
     * @param currentLineIndex The index of currentWord in currentLine.
     * @param filePath The file path for the word list being read in.
     * @return True if word doesn't meet requirements for a word, go to next word.
     */
    public boolean incorrectlyFormattedWord(String[] currentLine, int currentLineIndex, String filePath){
        this.currentWord = currentLine[currentLineIndex].trim();
        if(filePath.contains("words_dictionary.json")){
            int firstQuotes = this.currentWord.indexOf('"') + 1;
            int secondQuotes = this.currentWord.indexOf('"', firstQuotes);
            if (firstQuotes == -1 || secondQuotes == -1) return true;
            this.currentWord = this.currentWord.substring(firstQuotes, secondQuotes);
        }
        this.currentWord = this.currentWord.trim();
        //If word ends with "-", move onto next word. This is just marking prefixes in my word list file.
        if (this.currentWord.endsWith("-")) return true;
        this.currentWord = this.currentWord.toLowerCase();
        if (this.currentWord.length() == 1){
            if (this.currentWord.compareTo("a") != 0 && this.currentWord.compareTo("i") != 0) return true;
        }
        return false;
    }

    /**
     * Determine the number of parents we need to cycle through to get to the last shared node.
     * @return The number of ancestors we need to backtrack through to get to the shared node.
     */
    public int determineGenerationsToLastSharedNode() {
        int numberOfMatchedLetters = 0;
        int numberOfGenerationsToMoveDownTrie = this.currentWord.length();
        // if there's a previous word, try to use pointer to continue building the tree.
        if (this.prevWord != null) {
            // See how many letters are already inside our pointer
            for (int i = 0; i < this.prevWord.length() && i < this.currentWord.length() && this.prevWord.charAt(i) == this.currentWord.charAt(i); i++) {
                numberOfMatchedLetters++;
            }
            if (numberOfMatchedLetters >= 0) {
                return this.prevWord.length() - numberOfMatchedLetters;
            }
        }
        return 0;
    }

    /**
     * Set the pointer to the last shared node between prevWord and currentWord.
     * @param generationsToBackTrack The number of parents to back track through to get to the shared node.
     */
    public void pointToSharedNode(int generationsToBackTrack){
        //if the words had no common nodes, set pointer to root.
        if (this.prevWord == null || generationsToBackTrack == this.prevWord.length()){
            this.tree.pointer(this.tree.root());
        }
        else {
            // for each letter/node/generation that we need to backtrack...
            for (int j = 0; j < generationsToBackTrack && this.tree.pointer() != this.tree.root(); j++) {
                this.tree.pointer(this.tree.pointer().parent());
            }
        }
    }

    /**
     * Add the current word to the trie(Should have already set the pointer to the last shared node between
     * prevWord and currentWord).
     */
    public void addWordToTrie(){
        // for each new letter, from the last shared node...
        for (int currentIndex = this.tree.pointer().length(); currentIndex < this.currentWord.length(); currentIndex++) {
            boolean endOfWord = false;
            if (currentIndex == this.currentWord.length() - 1) endOfWord = true;
            // Create a new child TrieNode with the new letter
            TrieNode newNode = this.tree.pointer().addChild(this.currentWord.charAt(currentIndex), endOfWord);
            this.tree.incNodeCount();
            //mark the letter if it completes the word, increase the word count.
            if (endOfWord) {
                this.prevWord = this.currentWord;
                this.tree.incWordCount();
            }
            // set pointer to the new node
            this.tree.pointer(newNode);
        }
    }

}
