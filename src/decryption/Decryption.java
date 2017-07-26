package decryption;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Decryption {

    static double scoring(String decodedString, TreeMap<String, Double> mostCommonWords) throws IOException {
//        TODOx better way to score? idk but I dont have time for it
        String[] words = decodedString.split("\\s+");
        double score = 0;

        for (String word : words) {
//            TODOx do analyze to determine value of 15
            if (word.length() < 12) {
                if (mostCommonWords.containsKey(word)) {
//                    TODOx this may result error
                    score += mostCommonWords.get(word);
                }
            }

        }
        return score;

    }

    static String AL_I_toString(ArrayList<Integer> stringAL_I, ArrayList<Character> alphabetFile) {
        /*
        * 1. Convert orinial to DenisCode
        * 2. Plus 1
        * 3. Convert Denis code to character
        * */

//        EXPLAIN: COVERT ORIGINAL TO DENIS CODE
//        TODOx better way to convert?
        StringBuilder stringSB = new StringBuilder();
        for (Integer anOriginalCharacter : stringAL_I) {
            stringSB.append(alphabetFile.get(anOriginalCharacter));
        }
        return stringSB.toString();
    }

    public static String AL_C_toString(ArrayList<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for (Character ch : list) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public static ArrayList<Character> readFile(String fileName) throws IOException {
        FileReader in;
        in = new FileReader(fileName);
        ArrayList<Character> readingResult = new ArrayList<>();
        int characterAsInt;
        while ((characterAsInt = in.read()) != -1) {
            char character = (char) characterAsInt;
            if (character != '<' && character != '>' && character != '\r') {
                readingResult.add(character);
            }
        }
        if (readingResult.get(readingResult.size() - 1) == '\n') {
            readingResult.remove((readingResult.size() - 1));
        }
        in.close();
        return readingResult;
    }

    public static ArrayList<Character> processAlphabetFile() throws IOException {
        ArrayList<Character> in = readFile("sourceFile/alphabet.txt");
//        TODOx anyway to not hard-code? Idk and I also don't have time
        in.remove(39);
        in.set(39, '\n');
        return in;
    }

    public static ArrayList<Integer> convertFromASCIIToDenisCode(ArrayList<Character> originalString, ArrayList<Character> alphabetFile) {
//        TODOx if have time think of a more efficient algorithm
        ArrayList<Integer> stringAfterConverted = new ArrayList<>();
        for (Character anOriginalCharacter : originalString) {
            stringAfterConverted.add(alphabetFile.indexOf(anOriginalCharacter));
        }
        return stringAfterConverted;
    }

    static DecodedStringCT[] convertResultFromAL_A(ArrayList<DecodedStringCT> results) {
        DecodedStringCT[] finalResult = new DecodedStringCT[results.size()];
        results.toArray(finalResult);
        Arrays.sort(finalResult, finalResult[0]);
        return finalResult;
    }

    public static void sortResult(ArrayList<DecodedStringVN> results) {
        results.sort((o1, o2) -> {
            if (o1.getScore() > o2.getScore()) return 1;
            if (o1.getScore() < o2.getScore()) return -1;
            return 0;
        });
    }

    static void printTopThreeResult(DecodedStringCT[] finalResult) {
        System.out.println("TOP 3 RESULTS\n");
        for (int i = 0; i < 3; i++) {
            System.out.println("Result " + (i + 1));
            DecodedStringCT result = finalResult[i];
            System.out.println("Key: " + result.getKey());
            System.out.println("Score: " + result.getScore());
            System.out.println("Decoded string: " + result.getDecodedString());
            System.out.println();
        }
    }

    public static void printTopThreeResult(ArrayList<DecodedStringVN> results) {
        System.out.println("TOP 3 RESULTS:");
        System.out.println();
        for (int i = 0; i < 3; i++) {
            System.out.println("Result " + (i + 1));
            DecodedStringVN result = results.get(i);
            System.out.println("KEY: " + AL_C_toString(result.getKey()));
            System.out.println("SCORE: " + result.getScore());
            System.out.println("DECODED STRING: " + result.getDecodedString() + "\n");
            System.out.println();
        }
    }

    public static TreeMap<String, Double> findMostCommonWords(int numberOfWords) {
        if (numberOfWords < -1 || numberOfWords > 10000)
            throw new IllegalArgumentException("Number of words must be >= -1 and <= 70000!");

        if (numberOfWords == -1) {
            numberOfWords = 10000;
        }
        TreeMap<String, Double> mostCommonWords = new TreeMap<>();

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader("sourceFile/english_words.txt");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
//            int pos = 0;
            double total_count = 0;
            total_count = getTotalCount(mostCommonWords, bufferedReader, total_count, numberOfWords);
            for (Map.Entry<String, Double> entry : mostCommonWords.entrySet()) {
                entry.setValue(Math.log(entry.getValue() / total_count));
            }
//            TODOx support case where number of words is specified

            // Always close files.
            bufferedReader.close();
            return mostCommonWords;

        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            "sourceFile/google-10000-english-no-swears.txt" + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + "sourceFile/google-10000-english-no-swears.txt" + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return null;
    }

    public static double getTotalCount(TreeMap<String, Double> mostCommonTriagrams, BufferedReader bufferedReader,
                                       double total_count, int numberOfWords) throws IOException {
        String line;
        int numberOrReadLines = 0;
        while ((line = bufferedReader.readLine()) != null && numberOrReadLines <= numberOfWords) {
            String[] lineComponents = line.split(" ");
            Double count = Double.valueOf(lineComponents[1]);
            mostCommonTriagrams.put(lineComponents[0], count);
            total_count += count;
            numberOrReadLines++;
        }
        return total_count;
    }

    static ArrayList<Character> stringToAL_C(String keyS) {
        ArrayList<Character> result = new ArrayList<>();
        for (int i = 0; i < keyS.length(); i++) {
            result.add(keyS.charAt(i));
        }
        return result;
    }
}
