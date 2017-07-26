import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Q7 {
    public static void main(String[] args) throws IOException {
        System.out.println("Check if the message is encoded using transposition");
        Q2.crackTransposition("sourceFile/msg7.enc", -1);
        System.out.println("\n\n");

        System.out.println("Check if the message is encoded using caesar");
        Q1.crackCaesar("sourceFile/msg7.enc", -1);
        System.out.println("\n\n");

//        TODO what does it mean different keys + same key
        System.out.println("Check if the message is encoded using transposition first, then caesar (different keys + same key)");
        decodeWithFirstCaesarAndThenTranspositionDifferentKeysAndSameKey("sourceFile/msg7.enc");
        System.out.println("\n\n");

        System.out.println("Check if the message is encoded using caesar first, then transposition (different keys + same key)");
        Q6.decodeWithTranspositionFirstAndThenCaesarSameKey("sourceFile/msg7.enc");
    }

    private static void decodeWithFirstCaesarAndThenTranspositionDifferentKeysAndSameKey(String fileName) throws IOException {
//        EXPLAIN: basic preparation
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        int alphabetFileSize = alphabetFile.size();
        ArrayList<Character> cipherAL_C = Q1.readFile(fileName);
        ArrayList<Integer> cipherAL_I = Q1.convertFromASCIIToDenisCode(cipherAL_C, alphabetFile);
        DecodedString[] caesarCrackingResult = new DecodedString[alphabetFileSize];
        ArrayList<Integer> commonDivisors = Q2.findCommonDivisors(cipherAL_C.size());
        TreeMap<String, Double> mostCommonWords = CommonWordAnalysis.process10000file(10000);
        ArrayList<DecodedString> transpositionCrackingResult = new ArrayList<>();

//        EXPLAIN: crack with Caesar first
        for (int i = 0; i < alphabetFile.size(); i++) {
            Q1.crackCaesarWithSpecificKey(alphabetFile, cipherAL_I, mostCommonWords, caesarCrackingResult, i);
            String decodedString = caesarCrackingResult[i].getDecodedString();
            ArrayList<Character> readingResult = Q6.stringToArrayListOfCharacter(decodedString);
            for (int divisor : commonDivisors) {
                // TODO method actualCrack has been converted to method crackTransposition
                Q2.transpositionActualCrack(divisor, readingResult, transpositionCrackingResult, mostCommonWords);
            }
        }

        DecodedString[] finalResult = new DecodedString[transpositionCrackingResult.size()];
        transpositionCrackingResult.toArray(finalResult);
        Arrays.sort(finalResult, finalResult[0]);

        printTopThreeResult(finalResult);
    }

    private static void printTopThreeResult(DecodedString[] finalResult) {
        System.out.println("TOP 3 RESULTS\n");
        for (int i = 0; i < 3; i++) {
            System.out.println("Result " + (i + 1));
            DecodedString result = finalResult[i];
            System.out.println("Key: " + result.getKey());
            System.out.println("Score: " + result.getScore());
            System.out.println("Decoded string: " + result.getDecodedString());
            System.out.println();
        }
    }
}