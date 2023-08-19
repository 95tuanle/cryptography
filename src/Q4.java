import decryption.DecodedString;
import decryption.Decryption;
import decryption.RandomSubstitution;
import decryption.RandomSubstitution.CharacterFrequency;

import java.util.ArrayList;
import java.util.TreeMap;

/*General Strategy
 * Read alpha bet file
 * Find the frequency of each letter in the cipher text
 * Print result to file
 * Use Excel to do analysis
 * Replace
 * Think
 * */

/*Strategy 2
    Create a group of characters that x could be
    created different threads for the most common characters
    for each possibility, generate a new string > score
    if score > than certain THRESHOLD > print
* */

/*SUM-UP
 * 1. Space analysis: analyzeSpacingCharacter
 * 2. Monogram analysis: createFrequencyTable
 * 3. Most common one-letter word analysis: getOneLetterWordFromCipher
 * 4. Most common two-letter words analysis: getTwoLetterWordFromCipher
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
    public static void main(String[] args) throws Exception {
//        TODOx design an algorithm to automate the process > shouldn't do it

//        EXPLAIN basic preparation
        ArrayList<Character> alphabetFile = Decryption.processAlphabetFile();
        ArrayList<Character> cipherAL_C = Decryption.readFile("sourceFile/msg4.enc");
        String cipherS = Decryption.AL_C_toString(cipherAL_C);
//        ArrayList<CharacterFrequency> mostCommonMonogram = RandomSubstitution.processEnglishMonogram();
//        TreeMap<String, Double> mostCommonTrigram = Decryption.processTrigramFile(-1);
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
//        RandomSubstitution.printResult(cipherS, decodedStringRS);
        RandomSubstitution.writeResultToFile(decodedStringRS, "sourceFile/Answer for Q4.txt");
    }
}

