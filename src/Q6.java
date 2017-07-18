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
    private static int numberOfPrintResult = 3;

    public static void main(String[] args) {
        try {
            workingOnQuestionSix("sourceFile/msg6.enc2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void workingOnQuestionSix(String path) throws IOException {
        ArrayList<Character> old_string = Q1.readFile(path);
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        ArrayList<Integer> commonDivisors = Q2.findCommonDivisors(old_string.size());
        TreeMap<String, Integer> mostCommonWords = CommonWordAnalysis.process10000file();
        int alphabetFileSize = alphabetFile.size();
//        ArrayList<Integer> convertedString = Q1.convertFromASCIIToDenisCode(old_string, alphabetFile);
//        ArrayList<DecodedString> results = new ArrayList<>();
        ArrayList<DecodedString> results = new ArrayList<>();
        for (int i : commonDivisors) {
            Q2.actualCrack(i, old_string, results, mostCommonWords);
        }
        DecodedString[] resultsAsArray = new DecodedString[results.size()];
        results.toArray(resultsAsArray);
//        DecodedString[] arrayToSaveResultInto = new DecodedString[commonDivisors.size()*alphabetFileSize];
        for (int i : commonDivisors) {
            if (i < alphabetFileSize) {
                for (int j = 0; j < commonDivisors.size(); j++) {
                    ArrayList<Character> tempChars = new ArrayList<>();
                    String decodedString = resultsAsArray[j].getDecodedString();
                    for (char c : decodedString.toCharArray()) {
                        tempChars.add(c);
                    }
                    DecodedString[] resultsAsArrayFinal = new DecodedString[old_string.size()];
                    ArrayList<Integer> convertedString = Q1.convertFromASCIIToDenisCode(tempChars, alphabetFile);
                    Q1.crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, resultsAsArrayFinal, i);
//                    arrayToSaveResultInto=resultsAsArrayFinal.clone();
                    // TODO no hashcode
                    if (resultsAsArrayFinal[i].getScore() > 20) {
                        System.out.println("\n" + resultsAsArrayFinal[i].getKey());
                        System.out.println("Score  " + resultsAsArrayFinal[i].getScore());
                        System.out.println("\n\n" + resultsAsArrayFinal[i].getDecodedString() + "\n");
                    }
//                Arrays.sort(resultsAsArrayFinal, resultsAsArrayFinal[0]);
//                System.out.println("TOP 3 RESULTS\n");
//                for (int e = resultsAsArrayFinal.length - 1; e > resultsAsArrayFinal.length - numberOfPrintResult - 1; e--) {
//                    System.out.println("Key    " + resultsAsArrayFinal[e].getKey());
//                    System.out.println("Score  " + resultsAsArrayFinal[e].getScore());
//                    System.out.println("Decoded string:\n\n" + resultsAsArrayFinal[e].getDecodedString() + "\n");
//                }
                }
            }

        }
//        Arrays.sort(arrayToSaveResultInto, arrayToSaveResultInto[0]);
//        System.out.println("TOP 3 RESULTS\n");
//        for (int i = arrayToSaveResultInto.length - 1; i > arrayToSaveResultInto.length - numberOfPrintResult - 1; i--) {
//            System.out.println("Key    " + arrayToSaveResultInto[i].getKey());
//            System.out.println("Score  " + arrayToSaveResultInto[i].getScore());
//            System.out.println("Decoded string:\n\n" + arrayToSaveResultInto[i].getDecodedString() + "\n");
//        }

//        System.out.println("\n\nbam\n\n");
//        for (int i : commonDivisors) {
////            Q2.actualCrack(i, old_string, results, mostCommonWords);
//            StringBuilder tempString = new StringBuilder();
//            for (int k = 0; k < old_string.size() / i; k++) {
//                for (int j = 0; j < i; j++) {
//                    int index = old_string.size() / i * j + k;
//                    tempString.append(old_string.get(index));
//                }
//            }
//            String resultAsString = tempString.toString();
//            ArrayList<Character> tempChars = new ArrayList<>();
//            for (char c : resultAsString.toCharArray()) {
//                tempChars.add(c);
//            }
//            ArrayList<Integer> convertedString = Q1.convertFromASCIIToDenisCode(tempChars, alphabetFile);
//            DecodedString[] resultsAsArray = new DecodedString[resultAsString.length()];
//            Q1.crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, resultsAsArray, i);
//            System.out.println("Key    " + resultsAsArray[i].getKey());
//            System.out.println("Score  " + resultsAsArray[i].getScore());
//            System.out.println("Decoded string:\n\n" + resultsAsArray[i].getDecodedString() + "\n");

//            DecodedString[] resultsAsArray = new DecodedString[resultAsString.length()];
//            System.out.println(resultsAsArray);
//            results.add(new DecodedString(resultAsString, i, Q1.scoring(resultAsString, mostCommonWords)));



//        for (int i : commonDivisors) {
//            Q1.crackCaesarWithSpecificKey(alphabetFile, results, mostCommonWords, alphabetFileSize, resultsAsArray, i);
//
//        }

//        DecodedString[] resultsAsArray = new DecodedString[results.size()];
//        results.toArray(resultsAsArray);
//        for (int i : commonDivisors) {
//            Q1.crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, resultsAsArray[i], i);
//        }



//        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
//        int alphabetFileSize = alphabetFile.size();
//        ArrayList<Character> old_string = Q1.readFile(path);
//        ArrayList<Integer> convertedString = Q1.convertFromASCIIToDenisCode(old_string, alphabetFile);
//        DecodedString[] resultsAsArray = new DecodedString[alphabetFileSize];
//        ArrayList<Integer> commonDivisors = Q2.findCommonDivisors(old_string.size());
//        TreeMap<String, Integer> mostCommonWords = CommonWordAnalysis.process10000file();
//        ArrayList<DecodedString> results = new ArrayList<>();
//        for (int i = 0; i < alphabetFile.size(); i++) {
//            Q1.crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, resultsAsArray, i);
//            for (int x : commonDivisors) {
//                if (i == x) {
//                    String tempString = resultsAsArray[i].getDecodedString();
//                    ArrayList<Character> readingResult = new ArrayList<>();
//                    for (int j = 0; j < tempString.length(); j++) {
//                        readingResult.add(tempString.charAt(j));
//                    }
////                    for (char c : tempString.charAt()) {
////                        readingResult.add(c);
////                    }
//                    Q2.actualCrack(x, readingResult, results, mostCommonWords);
//                    System.out.println("RESULT:\n");
////                    System.out.println("Key    " + results.get(x).getKey());
////                    System.out.println("Score  " + results.get(x).getScore());
//                    // TODO get the string value from results array which have key = x and print it out in the next line
//                    System.out.println("Decoded string:\n\n" + " *TODO* " + "\n\n");
//                }
//            }
//        }
////        Arrays.sort(resultsAsArray, resultsAsArray[0]);
////        System.out.println("TOP 3 RESULTS\n");
////        for (int i = resultsAsArray.length - 1; i > resultsAsArray.length - numberOfPrintResult - 1; i--) {
////            System.out.println("Key    " + resultsAsArray[i].getKey());
////            System.out.println("Score  " + resultsAsArray[i].getScore());
////            System.out.println("Decoded string:\n\n" + resultsAsArray[i].getDecodedString() + "\n");
////        }
    }
}
