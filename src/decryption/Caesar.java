package decryption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class Caesar {
    public static void crack(String path, int key) throws IOException {
//        EXPLAIN basic preparation
        ArrayList<Character> alphabetFile = Decryption.processAlphabetFile();
        int alphabetFileSize = alphabetFile.size();
        if (key < -1 || key >= alphabetFileSize) {
            throw new IllegalArgumentException("Invalid Key");
        }
        ArrayList<Character> cipherAL_C = Decryption.readFile(path);
        ArrayList<Integer> cipherAL_I = Decryption.convertFromASCIIToDenisCode(cipherAL_C, alphabetFile);
        TreeMap<String, Double> mostCommonWords = Decryption.findMostCommonWords(-1);
        DecodedString.DecodedStringCT[] resultsA = new DecodedString.DecodedStringCT[alphabetFileSize];

//        TODOx re-implement process10000file
//        EXPLAIN actual cracking
        if (key == -1) {
            crackCaesarWithNoSpecificKey(alphabetFile, alphabetFileSize, cipherAL_I, mostCommonWords, resultsA);
        } else {
            crackCaesarWithSpecificKey(key, alphabetFile, cipherAL_I, mostCommonWords, resultsA);
        }
    }

    private static void crackCaesarWithSpecificKey(int key, ArrayList<Character> alphabetFile, ArrayList<Integer> cipherAL_I, TreeMap<String, Double> mostCommonWords, DecodedString.DecodedStringCT[] resultsA) {
        crackCaesarCore(alphabetFile, cipherAL_I, mostCommonWords, resultsA, key);
        System.out.println("RESULT\n");
        System.out.println("Key    " + resultsA[key].getKey());
        System.out.println("Score  " + resultsA[key].getScore());
        System.out.println("Decoded string:\n\n" + resultsA[key].getDecodedString() + "\n");
    }

    private static void crackCaesarWithNoSpecificKey(ArrayList<Character> alphabetFile, int alphabetFileSize, ArrayList<Integer> cipherAL_I, TreeMap<String, Double> mostCommonWords, DecodedString.DecodedStringCT[] resultsA) {
        for (int i = 0; i < alphabetFileSize; i++) {
            crackCaesarCore(alphabetFile, cipherAL_I, mostCommonWords, resultsA, i);
        }
        Arrays.sort(resultsA, resultsA[0]);
        Decryption.printTopThreeResult(resultsA);
    }

    static void crackCaesarCore(ArrayList<Character> alphabetFile, ArrayList<Integer> convertedString, TreeMap<String, Double> mostCommonWords, DecodedString.DecodedStringCT[] resultsAsArray, int i) {
        int alphabetFileSize = alphabetFile.size();
        ArrayList<Integer> newString = new ArrayList<>();
        for (Integer elementInConvertedString : convertedString) {
            int newCharacter = ((elementInConvertedString + i) % alphabetFileSize);
            newString.add(newCharacter);
        }
        String decodedString = Decryption.AL_I_toString(newString, alphabetFile);
        resultsAsArray[i] = new DecodedString.DecodedStringCT(decodedString, Decryption.scoring(decodedString, mostCommonWords), i);
    }
}
