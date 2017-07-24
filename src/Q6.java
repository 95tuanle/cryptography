import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Q6 {
    /*
    * gOI HAM SO O CAU SO 2 LEN
    * gOI HAM SO O CAU SO 1 LEN
    * */

    public static void main(String[] args) {
        try {
            workingOnQuestionSix();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void workingOnQuestionSix() throws IOException {
        ArrayList<Character> old_string = Q1.readFile("sourceFile/msg6.enc2");
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
<<<<<<< HEAD
//            if (i < alphabetFileSize) {
                for (int j = 0; j < alphabetFileSize; j++) {
                    ArrayList<Character> tempChars = new ArrayList<>();
                    String decodedString = resultsAsArray[i].getDecodedString();
                    for (char c : decodedString.toCharArray()) {
                        tempChars.add(c);
                    }
                    DecodedString[] resultsAsArrayFinal = new DecodedString[old_string.size()];
                    ArrayList<Integer> convertedString = Q1.convertFromASCIIToDenisCode(tempChars, alphabetFile);
                    Q1.crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, resultsAsArrayFinal, j);
//                    arrayToSaveResultInto=resultsAsArrayFinal.clone();
                    // TODO no hashcode
                    if (resultsAsArrayFinal[j].getScore() > 8) {
                        System.out.println("\n" + resultsAsArrayFinal[j].getKey());
                        System.out.println("Score  " + resultsAsArrayFinal[j].getScore());
                        System.out.println("\n\n" + resultsAsArrayFinal[j].getDecodedString() + "\n");
                    }
//                Arrays.sort(resultsAsArrayFinal, resultsAsArrayFinal[0]);
//                System.out.println("TOP 3 RESULTS\n");
//                for (int e = resultsAsArrayFinal.length - 1; e > resultsAsArrayFinal.length - numberOfPrintResult - 1; e--) {
//                    System.out.println("Key    " + resultsAsArrayFinal[e].getKey());
//                    System.out.println("Score  " + resultsAsArrayFinal[e].getScore());
//                    System.out.println("Decoded string:\n\n" + resultsAsArrayFinal[e].getDecodedString() + "\n");
//                }
=======
            for (int j = 0; j < alphabetFileSize; j++) {
                ArrayList<Character> tempChars = new ArrayList<>();
                String decodedString = resultsAsArray[i].getDecodedString();
                for (char c : decodedString.toCharArray()) {
                    tempChars.add(c);
                }
                DecodedString[] resultsAsArrayFinal = new DecodedString[old_string.size()];
                ArrayList<Integer> convertedString = Q1.convertFromASCIIToDenisCode(tempChars, alphabetFile);
                Q1.crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, resultsAsArrayFinal, j);
                // TODO no hashcode
                if (resultsAsArrayFinal[j].getScore() > 20) {
                    System.out.println("\n" + resultsAsArrayFinal[j].getKey());
                    System.out.println("Score  " + resultsAsArrayFinal[j].getScore());
                    System.out.println("\n\n" + resultsAsArrayFinal[j].getDecodedString() + "\n");
>>>>>>> 2b46e1cc0dec9f53f05133a582e268eb8f3369b8
                }
            }
        }
    }
}
