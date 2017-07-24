import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Q7 {
    /*
    * gOI HAM SO UNG VOI CAU SO 1 VA CAU SO 2 RA
    * */
    public static void main(String[] args) throws IOException {
        System.out.println("Check if the message is encoded using transposition");
        Q2.crackTransposition("sourceFile/msg7.enc", -1);
        System.out.println("Check if the message is encoded using caesar");
        Q1.crackCaesar("sourceFile/msg7.enc", -1);
//        System.out.println("Check if the message is encoded using caesar first, then transposition (same key)");
//        Q6.workingOnQuestionSix("sourceFile/msg7.enc");
        System.out.println("Check if the message is encoded using transposition first, then caesar (different keys + same key)");
        workingOnQuestionSeven();
        System.out.println("Check if the message is encoded using caesar first, then transposition (different keys + same key)");
        workingOnQuestionSevenPart2();

    }

    private static void workingOnQuestionSevenPart2() throws IOException {
        ArrayList<Character> old_string = Q1.readFile("sourceFile/msg7.enc");
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        ArrayList<Integer> commonDivisors = Q2.findCommonDivisors(old_string.size());
        TreeMap<String, Integer> mostCommonWords = CommonWordAnalysis.process10000file();
        int alphabetFileSize = alphabetFile.size();
        ArrayList<DecodedString> results = new ArrayList<>();
        for (int i : commonDivisors) {
            Q2.actualCrack(i, old_string, results, mostCommonWords);
        }
        DecodedString[] resultsAsArray = new DecodedString[results.size()];
        results.toArray(resultsAsArray);
        for (int i = 0; i < commonDivisors.size(); i++) {
//            int x = 0;
            for (int j = 0; j < alphabetFileSize; j++) {
                ArrayList<Character> tempChars = new ArrayList<>();
                String decodedString = resultsAsArray[i].getDecodedString();
                for (char c : decodedString.toCharArray()) {
                    tempChars.add(c);
                }
                DecodedString[] resultsAsArrayFinal = new DecodedString[old_string.size()];
                ArrayList<Integer> convertedString = Q1.convertFromASCIIToDenisCode(tempChars, alphabetFile);
                Q1.crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, resultsAsArrayFinal, j);
                System.out.println("\n" + resultsAsArrayFinal[j].getKey());
                System.out.println("Score  " + resultsAsArrayFinal[j].getScore());
                System.out.println("\n\n" + resultsAsArrayFinal[j].getDecodedString() + "\n");
            }
//            x++;
        }
    }

    private static void workingOnQuestionSeven() throws IOException {
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        int alphabetFileSize = alphabetFile.size();
        ArrayList<Character> old_string = Q1.readFile("sourceFile/msg7.enc");
        ArrayList<Integer> convertedString = Q1.convertFromASCIIToDenisCode(old_string, alphabetFile);
        DecodedString[] resultsAsArray = new DecodedString[alphabetFileSize];
        ArrayList<Integer> commonDivisors = Q2.findCommonDivisors(old_string.size());
        TreeMap<String, Integer> mostCommonWords = CommonWordAnalysis.process10000file();
        ArrayList<DecodedString> results = new ArrayList<>();

        for (int i = 0; i < alphabetFile.size(); i++) {
            Q1.crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, resultsAsArray, i);
            for (int x : commonDivisors) {
                    String tempString = resultsAsArray[i].getDecodedString();
                    ArrayList<Character> readingResult = new ArrayList<>();
                    for (int j = 0; j < tempString.length(); j++) {
                        readingResult.add(tempString.charAt(j));
                    }
//                    TODO method actualCrack has been converted to method crackTransposition
                    Q2.actualCrack(x, readingResult, results, mostCommonWords);
                    DecodedString[] resultsAsArrayX = new DecodedString[results.size()];
                    results.toArray(resultsAsArrayX);
                    System.out.println("RESULT:\n");
                    // TODO get the string value from results array which have key = x and print it out in the next line
                    System.out.println("Decoded string:\n\n" + " *TODO* " + "\n\n");
            }
        }

    }
}