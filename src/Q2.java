import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;


public class Q2 {
    /*
        * array1 = Tim tat ca cac soo chia het cho old_string.length
        * foreach i in array1:
        *   for k = 0; k < OLD_STRING.LENGTH/I; k++
        *       for j = 0; j < I; j++
        *           index = OLD_STRING.LENGTH/I*j+k
        *
        * */

    private static int numberOfPrintResult = 3;

    public static void main(String[] args) {
        try {
            crackTransposition("sourceFile/msg2.enc", -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> findCommonDivisors(int size) {
        ArrayList<Integer> commonDivisors = new ArrayList<>();
        for (int i = 1; i < size / 2 + 1; i++) {
            if (size%i ==0) {
                commonDivisors.add(i);
            }
        }
        return commonDivisors;
    }

    static void crackTransposition(String path, int key) throws IllegalArgumentException, IOException {
//        TODOx fix this one
        ArrayList<Character> old_string = Q1.readFile(path);
        ArrayList<Integer> commonDivisors = findCommonDivisors(old_string.size());
//        TODOx fix cau so 2
        if (key != -1 && !commonDivisors.contains(key)) {
            throw new IllegalArgumentException("Invalid key value!");
        }

        ArrayList<DecodedString> results = new ArrayList<>();
        TreeMap<String, Double> mostCommonWords = CommonWordAnalysis.process10000file(-1);

        if (key == -1) {
            for (int i : commonDivisors) {
                actualCrack(i, old_string, results, mostCommonWords);
            }

//            TODO can it be more efficient
            DecodedString[] resultsAsArray = new DecodedString[results.size()];
            results.toArray(resultsAsArray);
            Arrays.sort(resultsAsArray, resultsAsArray[0]);

            System.out.println("TOP 3 RESULTS\n");
            for (int i = 0; i < numberOfPrintResult; i++) {
                System.out.println(resultsAsArray[i]);
            }
        } else {
            actualCrack(key, old_string, results, mostCommonWords);
            System.out.println("RESULT:\n");
            System.out.println(results.get(0));
        }
    }

    static void actualCrack(int key, ArrayList<Character> old_string, ArrayList<DecodedString> results, TreeMap<String, Double> mostCommonWord) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int k = 0; k < old_string.size() / key; k++) {
            for (int j = 0; j < key; j++) {
                int index = old_string.size() / key * j + k;
                result.append(old_string.get(index));
            }
        }
        String resultAsString = result.toString();
        results.add(new DecodedString(resultAsString, Q1.scoring(resultAsString, mostCommonWord), key));
    }

//    static void actualCrack(int key, ArrayList<Character> old_string, ArrayList<DecodedString> results, TreeMap<String, Integer> mostCommonWord) throws IOException {
//        StringBuilder result = new StringBuilder();
//        for (int k = 0; k < old_string.size() / key; k++) {
//            for (int j = 0; j < key; j++) {
//                int index = old_string.size() / key * j + k;
//                result.append(old_string.get(index));
//            }
//        }
//        String resultAsString = result.toString();
//        results.add(new DecodedString(resultAsString, key, Q1.scoring(resultAsString, mostCommonWord)));
//    }

}