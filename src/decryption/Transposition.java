package decryption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Transposition {
    static ArrayList<Integer> findCommonDivisors(int size) {
        ArrayList<Integer> commonDivisors = new ArrayList<>();
        for (int i = 1; i < size / 2 + 1; i++) {
            if (size % i == 0) {
                commonDivisors.add(i);
            }
        }
        return commonDivisors;
    }

    public static void crack(String path, int key) throws IllegalArgumentException, IOException {
//        TODOx fix this one
        ArrayList<Character> old_string = Decryption.readFile(path);
        ArrayList<Integer> commonDivisors = findCommonDivisors(old_string.size());
//        TODOx fix cau so 2
        if (key != -1 && !commonDivisors.contains(key)) {
            throw new IllegalArgumentException("Invalid key value!");
        }
        ArrayList<DecodedString.DecodedStringCT> results = new ArrayList<>();
        TreeMap<String, Double> mostCommonWords = Decryption.findMostCommonWords(-1);

        if (key == -1) {
            crackWithNoSpecificKey(old_string, commonDivisors, results, mostCommonWords);
        } else {
            crackWithSpecificKey(key, old_string, results, mostCommonWords);
        }
    }

    private static void crackWithNoSpecificKey(ArrayList<Character> old_string, ArrayList<Integer> commonDivisors, ArrayList<DecodedString.DecodedStringCT> results, TreeMap<String, Double> mostCommonWords) throws IOException {
        for (int i : commonDivisors) {
            transpositionActualCrack(i, old_string, results, mostCommonWords);
        }
        DecodedString.DecodedStringCT[] resultsAsArray = Decryption.convertResultFromAL_A(results);
        Decryption.printTopThreeResult(resultsAsArray);
    }

    private static void crackWithSpecificKey(int key, ArrayList<Character> old_string, ArrayList<DecodedString.DecodedStringCT> results, TreeMap<String, Double> mostCommonWords) throws IOException {
        transpositionActualCrack(key, old_string, results, mostCommonWords);
        System.out.println("RESULT:\n");
        System.out.println(results.get(0));
    }

    static void transpositionActualCrack(int key, ArrayList<Character> old_string, ArrayList<DecodedString.DecodedStringCT> results, TreeMap<String, Double> mostCommonWord) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int k = 0; k < old_string.size() / key; k++) {
            for (int j = 0; j < key; j++) {
                int index = old_string.size() / key * j + k;
                result.append(old_string.get(index));
            }
        }
        String resultAsString = result.toString();
        results.add(new DecodedString.DecodedStringCT(resultAsString, Decryption.scoring(resultAsString, mostCommonWord), key));
    }
}
