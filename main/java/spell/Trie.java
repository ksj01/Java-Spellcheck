package spell;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Kevin on 1/21/2018.
 */

public class Trie implements ITrie {

public Node root = new Node();
public int totalNodes = 1;
public int totalWords;
    public void Trie() {
        totalWords = 0;
    }

    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count
     *
     * @param word The word being added to the trie
     */
    public void add(String word) {
        addLetter(root, word.toLowerCase());
    }

    public void addLetter(Node currentNode, String word) {
        char currentLetter = word.charAt(0);
        if (word.length() > 1) {
            word = word.substring(1);
            if (currentNode.letters[currentLetter - 'a'] == null) {
                currentNode.letters[currentLetter - 'a'] = new Node();
                totalNodes++;
            }
            addLetter(currentNode.letters[currentLetter - 'a'], word);
        }
        else {
            if (currentNode.letters[currentLetter - 'a'] == null) {
                currentNode.letters[currentLetter - 'a'] = new Node();
                totalNodes++;
            }
            currentNode.letters[currentLetter - 'a'].count++;
            if (currentNode.letters[currentLetter - 'a'].count == 1) {
                totalWords++;
            }
        }

    }

    /**
     * Searches the trie for the specified word
     *
     * @param word The word being searched for
     *
     * @return A reference to the trie node that represents the word,
     * 			or null if the word is not in the trie
     */
    public ITrie.INode find(String word) {
        Node found = findHelper(root, word.toLowerCase());
        return found;
    }

    public Node findHelper(Node currentNode, String word) {
        if (word.length() == 0) {
            return null;
        }
        char currentLetter = word.charAt(0);
        if (word.length() > 1) {
            word = word.substring(1);
            if (currentNode.letters[currentLetter - 'a'] == null) {
                return null;
            }
            return findHelper(currentNode.letters[currentLetter - 'a'], word);
        }
        else {
            if (currentNode.letters[currentLetter - 'a'] == null) {
                return null;
            }
            else if (currentNode.letters[currentLetter - 'a'].count > 0) {
                return currentNode.letters[currentLetter - 'a'];
            }
            else {
                return null;
            }
        }
    }

    public ArrayList<String> deletion(ArrayList<String> words) {
        ArrayList<String> newWords = new ArrayList<>();
        if (words.size() > 0) {
            for (int i = 0; i < words.size(); i++) {
                for (int j = 0; j < words.get(i).length(); j++) {
                    StringBuilder attempt = new StringBuilder(words.get(i));
                    attempt.deleteCharAt(j);
                    newWords.add(attempt.toString());
                }
            }
        }
        return newWords;
    }
    public ArrayList<String> transposition(ArrayList<String> words) {
        ArrayList<String> newWords = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            for (int j = 0; j < words.get(i).length() - 1; j ++) {
                char[] wordsChar = words.get(i).toCharArray();
                char temp = wordsChar[j];
                wordsChar[j] = wordsChar[j + 1];
                wordsChar[j + 1] = temp;
                String attempt = new String(wordsChar);
                newWords.add(attempt);
            }
        }
        return newWords;
    }
    public ArrayList<String> alteration(ArrayList<String> words) {
        ArrayList<String> newWords = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            for (int j = 0; j < words.get(i).length(); j ++) {
                for (int x = 0; x < 26; x++) {
                    char[] wordsChar = words.get(i).toCharArray();
                    if (wordsChar[j] != (char)(x + 'a')) {
                        wordsChar[j] = (char) (x + 'a');
                        String attempt = new String(wordsChar);
                        newWords.add(attempt);
                    }
                }
            }
        }
        return newWords;
    }
    public ArrayList<String> insertion(ArrayList<String> words) {
        ArrayList<String> newWords = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            for (int j = 0; j <= words.get(i).length(); j ++) {
                for (int x = 0; x < 26; x++) {
                    StringBuilder attempt = new StringBuilder(words.get(i));
                    attempt.insert(j, ((char)(x + 'a')));
                    newWords.add(attempt.toString());
                }
            }
        }
        return newWords;
    }

    public String findBest(ArrayList<String> words) {
        ArrayList<String> collection = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            if (find(words.get(i)) != null) {
                collection.add(words.get(i));
            }
        }
        ArrayList<Integer> collectionCounts = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++){
            collectionCounts.add(find(collection.get(i)).getValue());
        }
        int highest = Collections.max(collectionCounts);
        String best = null;
        for (int i = 0; i < collection.size(); i++) {
            if (find(collection.get(i)).getValue() == highest) {
                if (best == null) {
                    best = collection.get(i);
                }
                else {
                    int alphabetical = best.compareTo(collection.get(i));
                    if (alphabetical > 0) {
                        best = collection.get(i);
                    }
                }
            }
        }
        return best;
    }
    /**
     * Returns the number of unique words in the trie
     *
     * @return The number of unique words in the trie
     */
    public int getWordCount() {
        return totalWords;
    }


    /**
     * Returns the number of nodes in the trie
     *
     * @return The number of nodes in the trie
     */
    public int getNodeCount() {
        return totalNodes;
    }

    /**
     * The toString specification is as follows:
     * For each word, in alphabetical order:
     * <word>\n
     */

    public String toString() {
        StringBuilder dictionary = new StringBuilder();
        String stack = "";
        dictionary = toStringHelper(dictionary, this.root, stack);
        return dictionary.toString();
    }

    public StringBuilder toStringHelper(StringBuilder dictionary, Node current, String stack) {
        if (current.count > 0) {
            dictionary.append(stack + "\n");
        }
        for (int i = 0; i < 26; i++) {
            if (current.letters[i] != null) {
                stack = stack + (char)(i + 'a');

                toStringHelper(dictionary, current.letters[i], stack);
                if(stack.length() == 1) {
                    stack = "";
                }
                else {
                    stack = stack.substring(0, stack.length() - 1);
                }
            }
            else if (current.letters[i] == null) {
                //do nothing
            }
        }
        return dictionary;
    }


    public int hashCode() {
        int i = 16;
        i = i * ((totalWords * totalWords) + (totalNodes / 3)) / 7;
        return i;
    }


    public boolean equals(Object o) {
        if (o == null || !(o instanceof Trie)) {
            return false;
        }
        else {
            Trie checking = (Trie) o;
            return equalsHelper(this.root, checking.root);
        }
    }
    public boolean equalsHelper(Node main, Node checking) {
        boolean recurse = true;
        if (main.count != checking.count) {
            return false;
        }
        for (int i = 0; i < 26; i++) {
            if ((main.letters[i] != null) && (checking.letters[i] != null)) {
                recurse = equalsHelper(main.letters[i], checking.letters[i]);
                if (recurse == false) {
                    return recurse;
                }
            }
            else if ((main.letters[i] == null) && (checking.letters[i] == null)) {
                //do nothing
            }
            else {
                return false;
            }
        }
        return recurse;
    }

    public class Node implements ITrie.INode {
        public Node letters[] = new Node[26];
        public int count = 0;
        public void Node() {
            for(int i = 0; i < 26; i++) {
                letters[i] = null;
            }
        }
        public int getValue() {
            return this.count;
        }
    }
}


