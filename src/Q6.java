import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * Created by s3574983 on 13/07/2017.
 */
public class Q6 {
    /*
    * gOI HAM SO O CAU SO 2 LEN
    * gOI HAM SO O CAU SO 1 LEN
    * */
//    private static int numberOfPrintResult = 3;

    public static void main(String[] args) {
        try {
            workingOnQuestionSix("sourceFile/msg6.enc2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void workingOnQuestionSix(String path) throws IOException {
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        int alphabetFileSize = alphabetFile.size();
        ArrayList<Character> old_string = Q1.readFile(path);
        ArrayList<Integer> convertedString = Q1.convertFromASCIIToDenisCode(old_string, alphabetFile);
        DecodedString[] resultsAsArray = new DecodedString[alphabetFileSize];
        ArrayList<Integer> commonDivisors = Q2.findCommonDivisors(old_string.size());
        TreeMap<String, Integer> mostCommonWords = CommonWordAnalysis.process10000file();
        ArrayList<DecodedString> results = new ArrayList<>();
        for (int i = 0; i < alphabetFile.size(); i++) {
            Q1.crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, resultsAsArray, i);
            for (int x : commonDivisors) {
                if (i == x) {
                    String tempString = resultsAsArray[i].getDecodedString();
                    ArrayList<Character> readingResult = new ArrayList<>();
                    for (int j = 0; j < tempString.length(); j++) {
                        readingResult.add(tempString.charAt(j));
                    }
//                    for (char c : tempString.charAt()) {
//                        readingResult.add(c);
//                    }
                    Q2.actualCrack(x, readingResult, results, mostCommonWords);
                    System.out.println("RESULT:\n");
//                    System.out.println("Key    " + results.get(x).getKey());
//                    System.out.println("Score  " + results.get(x).getScore());
                    // TODO get the string value from results array which have key = x and print it out in the next line
                    System.out.println("Decoded string:\n\n" + " *TODO* " + "\n\n");
                }
            }
        }
//        Arrays.sort(resultsAsArray, resultsAsArray[0]);
//        System.out.println("TOP 3 RESULTS\n");
//        for (int i = resultsAsArray.length - 1; i > resultsAsArray.length - numberOfPrintResult - 1; i--) {
//            System.out.println("Key    " + resultsAsArray[i].getKey());
//            System.out.println("Score  " + resultsAsArray[i].getScore());
//            System.out.println("Decoded string:\n\n" + resultsAsArray[i].getDecodedString() + "\n");
//        }
    }
}
