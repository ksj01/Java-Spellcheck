package spell;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpellCheck implements ISpellCorrector {
    public Trie mainTrie = new Trie();
    public int x = 0;
    /**
     * Tells this <code>SpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     * @param dictionaryFileName File containing the words to be used
     * @throws IOException If the file cannot be read
     */
    public void useDictionary(String dictionaryFileName) throws IOException {
        String alphaReg = "(?:\\s|^)([A-Za-z]+)(?=\\s|$)";
        FileReader loadedFile = new FileReader(dictionaryFileName);
        Scanner sc = new Scanner(loadedFile);
        Pattern alpha = Pattern.compile(alphaReg);
        while(sc.hasNext()) {
            String current = sc.next();
            Matcher match = alpha.matcher(current);
                if (match.find()) {
                    mainTrie.add(current);
                } else {
                    sc.nextLine();
                }
            }
        sc.close();
        }


    /**
     * Suggest a word from the dictionary that most closely matches
     * <code>inputWord</code>
     * @param inputWord
     * @return The suggestion or null if there is no similar word in the dictionary
     */
    public String suggestSimilarWord(String inputWord) {
        if (mainTrie.find(inputWord) != null) {
            return inputWord.toLowerCase();
        }
        else {
            ArrayList<String> input = new ArrayList<>();
            input.add(inputWord);
            ArrayList<String> step1 = new ArrayList<>();
            step1.addAll(mainTrie.deletion(input));
            step1.addAll(mainTrie.transposition(input));
            step1.addAll(mainTrie.alteration(input));
            step1.addAll(mainTrie.insertion(input));
            ArrayList<String> foundStep1 = new ArrayList<>();
            for (int i = 0; i < step1.size(); i++) {

                if (mainTrie.find(step1.get(i)) != null) {
                    foundStep1.add(step1.get(i));
                }
            }
            if (foundStep1.size() > 0) {
                return mainTrie.findBest(foundStep1);
            }
            else {
                ArrayList<String> step2 = new ArrayList<>();
                step2.addAll(mainTrie.deletion(step1));
                step2.addAll(mainTrie.transposition(step1));
                step2.addAll(mainTrie.alteration(step1));
                step2.addAll(mainTrie.insertion(step1));
                ArrayList<String> foundStep2 = new ArrayList<>();
                for (int i = 0; i < step2.size(); i++) {
                    if (mainTrie.find(step2.get(i)) != null) {
                        foundStep2.add(step2.get(i));
                    }
                }
                if (foundStep2.size() > 0) {
                    return mainTrie.findBest(foundStep2);
                }
                else {
                    return null;
                }
            }

        }
    }
}
