package decryption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class ComboEncryption {
    public static void transposeThenCaesar(String fileName) throws IOException {
//        EXPLAIN: prepare basic variables
        ArrayList<Character> old_string = Decryption.readFile(fileName);
        ArrayList<Character> alphabetFile = Decryption.processAlphabetFile();
        ArrayList<Integer> commonDivisors = Transposition.findCommonDivisors(old_string.size());
        TreeMap<String, Double> mostCommonWords = Decryption.findMostCommonWords(10000);
        int alphabetFileSize = alphabetFile.size();
        ArrayList<DecodedString.DecodedStringCT> transpositionCrackingResultAL = new ArrayList<>();

//        EXPLAIN: crack using transposition
        for (int i : commonDivisors) {
            Transposition.transpositionActualCrack(i, old_string, transpositionCrackingResultAL, mostCommonWords);
        }

//        EXPLAIN: convert results from AL to A
        DecodedString.DecodedStringCT[] transpositionCrackingResultA = new DecodedString.DecodedStringCT[transpositionCrackingResultAL.size()];
        transpositionCrackingResultAL.toArray(transpositionCrackingResultA);


        ArrayList<DecodedString.DecodedStringCT> finalResultAL = new ArrayList<>();
//        EXPLAIN: cracking using caesar
        for (int i = 0; i < commonDivisors.size(); i++) {
//            EXPLAIN: Covert cipher text to Denis code
            String DecodedStringCT = transpositionCrackingResultA[i].getDecodedString();
            ArrayList<Character> cipherAL_C = Decryption.stringToAL_C(DecodedStringCT);
            ArrayList<Integer> cipherAL_I = Decryption.convertFromASCIIToDenisCode(cipherAL_C, alphabetFile);

//            EXPLAIN: start cracking using Caesar
            DecodedString.DecodedStringCT[] caesarCrackingResultA = new DecodedString.DecodedStringCT[alphabetFileSize];
            for (int j = 0; j < alphabetFileSize; j++) {
                Caesar.crackCaesarCore(alphabetFile, cipherAL_I, mostCommonWords, caesarCrackingResultA, j);
            }
            finalResultAL.addAll(Arrays.asList(caesarCrackingResultA));
        }

//        EXPLAIN: look for the ones with the highest score
        finalResultAL.sort((o1, o2) -> {
            if (o1.getScore() < o2.getScore()) return -1;
            else if (o1.getScore() > o2.getScore()) return 1;
            return 0;
        });

//        EXPLAIN: print top 3 results
        System.out.println("TOP 3 RESULTS\n");
        for (int i = 0; i < 3; i++) {
            System.out.println("Result " + (i + 1));
            DecodedString.DecodedStringCT result = finalResultAL.get(i);
            System.out.println("Key: " + result.getKey());
            System.out.println("Score: " + result.getScore());
            System.out.println("Decoded string: " + result.getDecodedString());
            System.out.println();
        }
    }

    public static void caesarThenTranspose(String fileName) throws IOException {
//        EXPLAIN: basic preparation
        ArrayList<Character> alphabetFile = Decryption.processAlphabetFile();
        int alphabetFileSize = alphabetFile.size();
        ArrayList<Character> cipherAL_C = Decryption.readFile(fileName);
        ArrayList<Integer> cipherAL_I = Decryption.convertFromASCIIToDenisCode(cipherAL_C, alphabetFile);
        DecodedString.DecodedStringCT[] caesarCrackingResult = new DecodedString.DecodedStringCT[alphabetFileSize];
        ArrayList<Integer> commonDivisors = Transposition.findCommonDivisors(cipherAL_C.size());
        TreeMap<String, Double> mostCommonWords = Decryption.findMostCommonWords(10000);
        ArrayList<DecodedString.DecodedStringCT> transpositionCrackingResult = new ArrayList<>();

//        EXPLAIN: crack with Caesar first
        for (int i = 0; i < alphabetFile.size(); i++) {
            Caesar.crackCaesarCore(alphabetFile, cipherAL_I, mostCommonWords, caesarCrackingResult, i);
            String decodedString = caesarCrackingResult[i].getDecodedString();
            ArrayList<Character> readingResult = Decryption.stringToAL_C(decodedString);
            for (int divisor : commonDivisors) {
                // TODOx method actualCrack has been converted to method crackTransposition
                Transposition.transpositionActualCrack(divisor, readingResult, transpositionCrackingResult, mostCommonWords);
            }
        }

        DecodedString.DecodedStringCT[] finalResult = Decryption.convertResultFromAL_A(transpositionCrackingResult);
        Decryption.printTopThreeResult(finalResult);
    }
}
