import decryption.*;


import java.util.ArrayList;
import java.util.TreeMap;

public class Q7 {
    public static void main(String[] args) throws Exception {
//        System.out.println("Check if the message is encoded using transposition");
//        Transposition.crack("sourceFile/msg7.enc", -1);
//        System.out.println("\n\n");
//
//        System.out.println("Check if the message is encoded using caesar");
//        Caesar.crack("sourceFile/msg7.enc", -1);
//        System.out.println("\n\n");

//        TODO what does it mean different keys + same key
        // EXPLAIN: in question 6 the 2-stage decryption has to use the same key, in this case we have to try use different keys for
//        System.out.println("Check if the message is encoded using transposition first, then caesar (different keys + same key)");
//        ComboEncryption.caesarThenTranspose("sourceFile/msg7.enc");
//        System.out.println("\n\n");
//
//        System.out.println("Check if the message is encoded using caesar first, then transposition (different keys + same key)");
//        ComboEncryption.transposeThenCaesar("sourceFile/msg7.enc");

        System.out.println("Check if the message is encoded using random substitution");
        decryptQuestionSeven();
    }

/*SUM-UP
* 1. Space analysis: analyzeSpacingCharacter
* 2. Monogram analysis: createFrequencyTable
* 3. Most common one letter word analysis: getOneLetterWordFromCipher
* 4. Most common two letter words analysis: getTwoLetterWordFromCipher
* 5. Guessing
* */

    private static void decryptQuestionSeven() throws Exception {
        ArrayList<Character> alphabetFile = Decryption.processAlphabetFile();
        ArrayList<Character> cipherAL_C = Decryption.readFile("sourceFile/msg7.enc");
        String cipherS = Decryption.AL_C_toString(cipherAL_C);
        ArrayList<RandomSubstitution.CharacterFrequency> cipherCharFreq =
                RandomSubstitution.CipherAnalysis.monogramAnalysis(alphabetFile, cipherAL_C);
        ArrayList<RandomSubstitution.SpaceAnalysis> spaceAnalyses =
                RandomSubstitution.CipherAnalysis.analyzeSpacingCharacter(cipherAL_C, cipherCharFreq);
        String[] cipherWordsA = cipherS.split(Character.toString(spaceAnalyses.get(0).getSpace()));
        TreeMap<Character, Character> key = RandomSubstitution.getKeyFromFile("sourceFile/key for Q7.csv");
        DecodedString.DecodedStringRS decodedStringRS = RandomSubstitution.crack(cipherWordsA, key);
        Q4.printResult(cipherS, decodedStringRS);
        Q4.writeResultToFile(decodedStringRS, "sourceFile/Answer for Q7.txt");
    }


}