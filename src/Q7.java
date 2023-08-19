import decryption.Caesar;
import decryption.ComboEncryption;
import decryption.RandomSubstitution;
import decryption.Transposition;

import java.util.TreeMap;

public class Q7 {
    public static void main(String[] args) throws Exception {
        System.out.println("Check if the message is encoded using transposition");
        Transposition.crack("sourceFile/msg7.enc", -1);
        System.out.println("\n\n");

        System.out.println("Check if the message is encoded using caesar");
        Caesar.crack("sourceFile/msg7.enc", -1);
        System.out.println("\n\n");

//        TODO what does it mean different keys + same key
//         EXPLAIN: in question 6 the 2-stage decryption has to use the same key, in this case we have to try use different keys for
        System.out.println("Check if the message is encoded using transposition first, then caesar (different keys + same key)");
        ComboEncryption.caesarThenTranspose("sourceFile/msg7.enc");
        System.out.println("\n\n");

        System.out.println("Check if the message is encoded using caesar first, then transposition (different keys + same key)");
        ComboEncryption.transposeThenCaesar("sourceFile/msg7.enc");

        System.out.println("Check if the message is encoded using random substitution");
        TreeMap<Character, Character> key = RandomSubstitution.getKeyFromFile("sourceFile/key for Q7.csv");
        RandomSubstitution.crack("sourceFile/msg7.enc", key);
//        decryptQuestionSeven();
    }

    /*SUM-UP
     * 1. Space analysis: analyzeSpacingCharacter
     * 2. Monogram analysis: createFrequencyTable
     * 3. Most common one-letter word analysis: getOneLetterWordFromCipher
     * 4. Most common two-letter words analysis: getTwoLetterWordFromCipher
     * 5. Guessing
     * */
}