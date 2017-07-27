import decryption.DecodedString;
import decryption.Decryption;
import decryption.RandomSubstitution;
import decryption.RandomSubstitution.CharacterFrequency;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/*General Strategy
* Read alpha bet file
* Find the frequency of each letter in the cipher text
* Print result to file
* Use excel to do analysis
* Replace
* Think
* */

/*Strategy 2
    Create a group of character that x could be
    create different threads for the most common characters
    for each possibility, generate a new string > score
    if score > than certain THRESHOLD > print
* */

/*SUM-UP
* 1. Space analysis: analyzeSpacingCharacter
* 2. Monogram analysis: createFrequencyTable
* 3. Most common one letter word analysis: getOneLetterWordFromCipher
* 4. Most common two letter words analysis: getTwoLetterWordFromCipher
* 5. Guessing
* */

/*How to guess?
* 1. Assuming that a cipher letter is something
* 2. Look for words that satisfy any of these criteria
*   - Most of the letters have been identified
*   - The first syllable has been identified
*   - Contain different types of characters
* 3. Check in dictionary possible matching words
* 4. Use your intuition to determine whether your assumption is correct*/

public class Q4 {
    private static final int THRESHOLD = -450;
    private static final int englishMonogram = 5;

    public static void main(String[] args) throws Exception {
//        TODOx design an algorithm to automate the process > shouldn't do it

//        EXPLAIN basic preparation
        ArrayList<Character> alphabetFile = Decryption.processAlphabetFile();
        ArrayList<Character> cipherAL_C = Decryption.readFile("sourceFile/msg4.enc");
        String cipherS = Decryption.AL_C_toString(cipherAL_C);
//        ArrayList<CharacterFrequency> mostCommonMonogram = RandomSubstitution.processEnglishMonogram();
//        TreeMap<String, Double> mostCommonTrigram = Decryption.processTriagramFile(-1);
//        TreeMap<String, Double> mostCommonOneLetterWord = CommonWordAnalysis.processOneLetterWordFile();

//        EXPLAIN basic analysis
        ArrayList<CharacterFrequency> cipherCharFreq = RandomSubstitution.CipherAnalysis.monogramAnalysis(alphabetFile, cipherAL_C);
        ArrayList<RandomSubstitution.SpaceAnalysis> spaceAnalyses = RandomSubstitution.CipherAnalysis.analyzeSpacingCharacter(cipherAL_C, cipherCharFreq);
        String[] cipherWordsA = cipherS.split(Character.toString(spaceAnalyses.get(0).getSpace()));
//        TODOx analyze based on words
//        ArrayList<CharacterFrequency> letterUseInSample = RandomSubstitution.getLetterUseInCipher(cipherAL_C, cipherCharFreq);
//        TreeMap<String, Integer> oneLetterWordFromCipher = RandomSubstitution.getOneLetterWordFromCipher(cipherWordsA);
//        TreeMap<String, Integer> twoLetterWordFromCipher = RandomSubstitution.getTwoLetterWordFromCipher(cipherWordsA);

        TreeMap<Character, Character> key = RandomSubstitution.getKeyFromFile("sourceFile/key for Q4.csv");
        DecodedString.DecodedStringRS decodedStringRS = RandomSubstitution.crack(cipherWordsA, key);
        RandomSubstitution.printResult(cipherS, decodedStringRS);
        RandomSubstitution.writeResultToFile(decodedStringRS, "sourceFile/Answer for Q4.txt");
    }

    private static void saveCipherCharFrequencyToFile(ArrayList<CharacterFrequency> cipherCharFreq) throws IOException {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileWriter("sourceFile/key for Q4.csv"));
            outputStream.println("Character\tFrequency\tKey");
            for (CharacterFrequency characterFrequency : cipherCharFreq) {
                if (characterFrequency.getFrequency() == 0) break;
                if (characterFrequency.getCharacter() == 'n') {
                    outputStream.print("n" + "\t\t\t");
                } else {
                    outputStream.print(characterFrequency.getCharacter() + "\t\t\t");
                }
                outputStream.print(characterFrequency.getFrequency() + "\t\t\t\n");
            }

        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    //    TODO fix this if have time
    private static void noName(ArrayList<CharacterFrequency> cipherFreqTable, TreeMap<Character, Character> key,
                               int index, TreeMap<String, Double> mostCommonTriagram, HashSet<DecodedString.DecodedStringRS> results, String[] sampleWordsList) throws IOException {
        if (index == cipherFreqTable.size()) {
            DecodedString.DecodedStringRS decodedString = RandomSubstitution.crack(key, mostCommonTriagram, sampleWordsList);
            if (decodedString.getScore() < THRESHOLD)
                results.add(decodedString);
//            if (key.values().contains(' ')) {
//            }
            return;
        }
        CharacterFrequency character = cipherFreqTable.get(index);

        TreeSet<Character> possibleCharacters = character.getPossibleCharacter();
        for (Character aPossibleCharacter : possibleCharacters) {
//            Character possibleCharacter = ((TreeSet<Character>) possibleCharacter).get(i);
            Collection<Character> values = key.values();
            if (values.contains(aPossibleCharacter)) continue;
//            TODOx be aware, what if in the end the character matches with nothing
            key.put(character.getCharacter(), aPossibleCharacter);
            noName(cipherFreqTable, key, index + 1, mostCommonTriagram, results, sampleWordsList);
            key.remove(character.getCharacter());
        }
//        if (key.containsKey(character.getCharacter()))

//        if (!key.containsKey(character.getCharacter()))
//            throw new RuntimeException("All possible characters for character " + character.getCharacter() + " has been used up");
    }

    //    TODO fix this if have time
    private static void preparePossibleCharacter(ArrayList<CharacterFrequency> cipherCharFreq,
                                                 ArrayList<CharacterFrequency> mostCommonMonogram,
                                                 ArrayList<RandomSubstitution.SpaceAnalysis> spaceAnalyses,
                                                 TreeMap<String, Integer> oneLetterWordFromCipher,
                                                 TreeMap<String, Double> mostCommonOneLetterWord) {
//        EXPLAIN: create an array to keep track which cipher letter has been processed
        boolean[] usedIndexArray = new boolean[cipherCharFreq.size()];
        for (int i = 0; i < usedIndexArray.length; i++) {
            usedIndexArray[i] = false;
        }

//        EXPLAIN: HANDLE SPACE
//        ASSUMPTION spaceAnalyse[1/2/3/...] cannot be the space
        char cipherLetterForSpace = spaceAnalyses.get(0).getSpace();
        int indexOfSpace = indexOfLetter(cipherCharFreq, cipherLetterForSpace);
        cipherCharFreq.get(indexOfSpace).getPossibleCharacter().add(' ');
        usedIndexArray[indexOfSpace] = true;

//        EXPLAIN use most common one letter word result
        for (String oneLetterWord : oneLetterWordFromCipher.keySet()) {
            int indexOfOneLetterWord = indexOfLetter(cipherCharFreq, oneLetterWord.charAt(0));
            for (Map.Entry<String, Double> entry : mostCommonOneLetterWord.entrySet()) {
                cipherCharFreq.get(indexOfOneLetterWord).getPossibleCharacter().add(entry.getKey().charAt(0));
                usedIndexArray[indexOfOneLetterWord] = true;
            }
        }
//        TODOx this needs to be fixed
//        EXPLAIN use English most common monogram result
        int numberOfLetterAdded = 0;
        for (int i = 0; i < mostCommonMonogram.size() && numberOfLetterAdded < englishMonogram; i++) {
            if (usedIndexArray[i]) continue;
            TreeSet<Character> possibleCharacter = cipherCharFreq.get(i).getPossibleCharacter();
            /*ASSUMPTION
            1. the distribution of cipher letter is the same as English letter
            2. Top 5 letters of the ciphertext mataches top 5 letter of English most common monogram
            */
            for (int j = 0; j < englishMonogram; j++) {
                possibleCharacter.add(mostCommonMonogram.get(j).getCharacter());
            }
//            usedIndex.add(i);
            usedIndexArray[i] = true;
            numberOfLetterAdded++;
            /*ASS-UMPTIONS:
            possible character is in the range of [starting index; st. index +5]
            possible character is limited in the 26 alphabet letters*/
//            int startingIndex = (int) ((float) 22 / 25 * i);
//            for (int j = startingIndex; j < startingIndex + 5; j++) {
//                possibleCharacter.add(englishMonogram.get(j).getCharacter());
//            }
//            possibleCharacter.add(' ');
        }
        for (int i = mostCommonMonogram.size() - 1; i < cipherCharFreq.size(); i++) {
            /*ASSUMPTIONS:
            possible character of uncommon character is " " > this is false for sure*/
            if (cipherCharFreq.get(i).getFrequency() != 0) cipherCharFreq.get(i).getPossibleCharacter().add(' ');
            else break;
        }
    }

    private static int indexOfLetter(ArrayList<CharacterFrequency> cipherCharFreq, char searchCharacter) {
        for (int i = 0; i < cipherCharFreq.size(); i++) {
            CharacterFrequency aCipherLetter = cipherCharFreq.get(i);
            if (aCipherLetter.getCharacter() == searchCharacter) {
                return i;
            }
        }
        return -1;
    }

}

