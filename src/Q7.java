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
        decodeWithFirstCaesarAndThenTranspositionDifferentKeysAndSameKey();
        System.out.println("\n\n");

        System.out.println("Check if the message is encoded using caesar first, then transposition (different keys + same key)");
        decodeWithTranspositionFirstAndThenCaesarDifferentKeysAndSameKey();
    }

    private static void decodeWithTranspositionFirstAndThenCaesarDifferentKeysAndSameKey() throws IOException {
        ArrayList<Character> old_string = Q1.readFile("sourceFile/msg7.enc");
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        ArrayList<Integer> commonDivisors = Q2.findCommonDivisors(old_string.size());
        TreeMap<String, Integer> mostCommonWordsI = CommonWordAnalysis.process10000file();
        TreeMap<String, Double> mostCommonWords = CommonWordAnalysis.process10000file(10000);
        int alphabetFileSize = alphabetFile.size();
        ArrayList<DecodedString> results = new ArrayList<>();
        for (int i : commonDivisors) {
            Q2.transpositionActualCrack(i, old_string, results, mostCommonWords);
        }
        DecodedString[] resultsAsArray = new DecodedString[results.size()];
        results.toArray(resultsAsArray);
        System.out.println("TOP RESULTS\n");
        for (int i = 0; i < commonDivisors.size(); i++) {
            for (int j = 0; j < alphabetFileSize; j++) {
                ArrayList<Character> tempChars = new ArrayList<>();
                String decodedString = resultsAsArray[i].getDecodedString();
                for (char c : decodedString.toCharArray()) {
                    tempChars.add(c);
                }
                DecodedString[] resultsAsArrayFinal = new DecodedString[old_string.size()];
                ArrayList<Integer> convertedString = Q1.convertFromASCIIToDenisCode(tempChars, alphabetFile);
                Q1.crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, resultsAsArrayFinal, j);
                if (resultsAsArrayFinal[j].getScore() > 9) {
                    System.out.println("\n" + resultsAsArrayFinal[j].getKey());
                    System.out.println("Score  " + resultsAsArrayFinal[j].getScore());
                    System.out.println("\n\n" + resultsAsArrayFinal[j].getDecodedString() + "\n");
                }
            }
        }
    }

    private static void decodeWithFirstCaesarAndThenTranspositionDifferentKeysAndSameKey() throws IOException {
//        EXPLAIN: basic preparation
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        int alphabetFileSize = alphabetFile.size();
        ArrayList<Character> cipherAL_C = Q1.readFile("sourceFile/msg7.enc");
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