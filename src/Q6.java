import java.io.IOException;
import java.util.*;

public class Q6 {

    public static void main(String[] args) {
        try {
            decodeWithTranspositionFirstAndThenCaesarSameKey();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void decodeWithTranspositionFirstAndThenCaesarSameKey() throws IOException {
//        EXPLAIN: prepare basic variables
        ArrayList<Character> old_string = Q1.readFile("sourceFile/msg6.enc2");
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        ArrayList<Integer> commonDivisors = Q2.findCommonDivisors(old_string.size());
        TreeMap<String, Double> mostCommonWords = CommonWordAnalysis.process10000file(10000);
        int alphabetFileSize = alphabetFile.size();
        ArrayList<DecodedString> transpositionCrackingResultAL = new ArrayList<>();

//        EXPLAIN: crack using transposition
        for (int i : commonDivisors) {
            Q2.transpositionActualCrack(i, old_string, transpositionCrackingResultAL, mostCommonWords);
        }

//        EXPLAIN: convert results from AL to A
        DecodedString[] transpositionCrackingResultA = new DecodedString[transpositionCrackingResultAL.size()];
        transpositionCrackingResultAL.toArray(transpositionCrackingResultA);


        ArrayList<DecodedString> finalResultAL = new ArrayList<>();
//        EXPLAIN: cracking using caesar
        for (int i = 0; i < commonDivisors.size(); i++) {
            ArrayList<Character> cipherAL_C = new ArrayList<>();
            String decodedString = transpositionCrackingResultA[i].getDecodedString();
//                EXPLAIN: convert ArrayList of Character to String
            for (char c : decodedString.toCharArray()) {
                cipherAL_C.add(c);
            }
            ArrayList<Integer> convertedString = Q1.convertFromASCIIToDenisCode(cipherAL_C, alphabetFile);
            DecodedString[] caesarCrackingResultA = new DecodedString[alphabetFileSize];
            for (int j = 0; j < alphabetFileSize; j++) {
                Q1.crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, caesarCrackingResultA, j);
            }
            finalResultAL.addAll(Arrays.asList(caesarCrackingResultA));
        }
        finalResultAL.sort(new Comparator<DecodedString>() {
            @Override
            public int compare(DecodedString o1, DecodedString o2) {
                if (o1.getScore() < o2.getScore()) return -1;
                else if (o1.getScore() > o2.getScore()) return 1;
                return 0;
            }
        });
        System.out.println("TOP 3 RESULTS\n");
        for (int i = 0; i < 3; i++) {
            System.out.println("Result " +(i + 1));
            DecodedString result = finalResultAL.get(i);
            System.out.println("Key: " + result.getKey());
            System.out.println("Score: " + result.getScore());
            System.out.println("Decoded string: " + result.getDecodedString());
            System.out.println();
        }
    }
}
