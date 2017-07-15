import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * Created by Minh V. Tran on 13/07/2017.
 */
public class Q1 {
    /*
    * old_string = convert(nsg1..enc)
    *
*     For j = 0 to j < old_string.length
*       new_string[old_string];
*       For i = 0 to i < 50
    *       new_string[j] = (old_string[j]+i) % 50
 *       decrypt(new_string)
 *       print(new_string)
    * */
    static void answer() throws IOException {
        ArrayList<Character> alphabetFile = processAlphabetFile();
//        TODO Is there anyway to save time, i.e. combine read file and covert from ASCII into 1
        ArrayList<Character> old_string = readFile("sourceFile/msg1.enc");
        ArrayList<Integer> convertedString = convertFromASCIIToDenisCode(old_string, alphabetFile);
        TreeMap<String, Integer> mostCommonWords = process10000file();

        int alphabetFileSize = alphabetFile.size();
        LinkedHashMap<String, Integer> results = new LinkedHashMap<>();

        for (int i = 0; i < alphabetFile.size(); i++) {
            ArrayList<Integer> newString = new ArrayList<>();
            for (Integer elementInConvertedString : convertedString) {
//                System.out.println(j);
                int newCharacter = ((elementInConvertedString + i) % alphabetFileSize);
                newString.add(newCharacter);
            }
            ArrayList<Character> decodedStringAsArrayList = decodeString(newString, alphabetFile);
            Object[] decodedStringAsArrayOfObject = decodedStringAsArrayList.toArray();
            char[] decodedStringAsCharArray = arrayObjectToCharArray(decodedStringAsArrayOfObject);
            String decodedString = new String(decodedStringAsCharArray);
            results.put(decodedString, 0);
            scoring(decodedString, mostCommonWords);
            System.out.println("The key is: " + i);
            printArrayList(decodedStringAsArrayList);
            System.out.println("\n***************************************\n");
        }

//        int i = 0;
//        String stringCorrespondingWithMaxScore = "";
//        int maxScore = 0;
//        for(Map.Entry<String, Integer> entry : results.entrySet()) {
//            String decodedString = entry.getKey();
//            int score = scoring(decodedString, mostCommonWords);
//            if(score < maxScore){
//                score = maxScore;
//            }
//            results.put(decodedString, score);
//            System.out.println("The score of element " + i + " is " + score);
//            i++;
//        }

//        results.values();
//        Set<Map.Entry<String, Integer>> resultsInSetFormat = results.entrySet();
//        Map.Entry<String, Integer>[] test = new Map.Entry<String, Integer>[50];
//        resultsInSetFormat.toArray(test);
//        Object[] resultsInArrayFormat = resultsInSetFormat.toArray();
//        Arrays.sort(resultsInArrayFormat);
//        TreeMap<Integer, String> sortedResult = new TreeMap<>();
//        for(Map.Entry<String, Integer> entry : results.entrySet()){
//            String decodedString = entry.getKey();
//            Integer score = entry.getValue();
//            sortedResult.put(score, decodedString);
//        }

        System.out.println();


    }

    private static int scoring(String decodedString, TreeMap<String, Integer> mostCommonWords) {
        String[] words = decodedString.split(" ");
        int score = 0;
        for (String word : words) {
            if (word.length() < 15) {
                if (mostCommonWords.containsKey(word.toLowerCase())) {
                    score++;
                }
            }
        }
        return score;
    }

    private static char[] arrayObjectToCharArray(Object[] decodedStringAsArrayOfObject) {
        char[] returnResult = new char[decodedStringAsArrayOfObject.length];
        for (int i = 0; i < returnResult.length; i++) {
            returnResult[i] = (Character) decodedStringAsArrayOfObject[i];
        }
        return returnResult;
    }

    private static ArrayList<Character> decodeString(ArrayList<Integer> originalString, ArrayList<Character> alphabetFile) {
        HashMap<Integer, Character> denisCode = new HashMap<>();
        for (int i = 0; i < alphabetFile.size(); i++) {
            denisCode.put(i, alphabetFile.get(i));
        }
        ArrayList<Character> stringAfterConverted = new ArrayList<>();
        for (Integer anOriginalCharacter : originalString) {
            Character correspondingValue = denisCode.get(anOriginalCharacter);
            stringAfterConverted.add(correspondingValue);
        }
        return stringAfterConverted;
    }

    private static TreeMap<String, Integer> process10000file() {
        String line;
        String returnResult = "";
        TreeMap<String, Integer> mostCommonWords = new TreeMap<>();
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader("sourceFile/google-10000-english-no-swears.txt");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            int pos = 0;
            while ((line = bufferedReader.readLine()) != null) {
//                returnResult = returnResult.concat(line + "\n");
                mostCommonWords.put(line, pos);
            }

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

    private static ArrayList<Character> readFile(String fileName) throws IOException {
        FileReader in;
//        String temp = null;
        in = new FileReader(fileName);

//        temp = "";
        ArrayList<Character> readingResult = new ArrayList<>();
        int characterAsInt;
        while ((characterAsInt = in.read()) != -1) {
            char character = (char) characterAsInt;
            if (character != '<' && character != '>' && character != '\r') {
                readingResult.add(character);
            }
        }
        in.close();
        return readingResult;
    }

    //    TODOx can I not use ArrayList? can but the benefit does not justify the cost
    private static <E> void printArrayList(ArrayList<E> arrayList) {
        for (E anArrayList : arrayList) {
            System.out.print(anArrayList);
        }
    }

    private static ArrayList<Character> processAlphabetFile() throws IOException {
        ArrayList<Character> in = readFile("sourceFile/alphabet.txt");
//        TODO anyway to not hard-code?
        in.remove(51);
        in.remove(39);
        in.set(39, '\n');
//        in.remove(50);
        return in;
    }

    private static ArrayList<Integer> convertFromASCIIToDenisCode(ArrayList<Character> originalString, ArrayList<Character> alphabetFile) {
//        TODO if have time think of a more efficient algorithm
        HashMap<Character, Integer> denisCode = new HashMap<>();
        for (int i = 0; i < alphabetFile.size(); i++) {
            denisCode.put(alphabetFile.get(i), i);
        }
        ArrayList<Integer> stringAfterConverted = new ArrayList<>();
        for (Character anOriginalCharacter : originalString) {
            Integer correspondingValue = denisCode.get(anOriginalCharacter);
            stringAfterConverted.add(correspondingValue);
        }
        return stringAfterConverted;
    }
}

class Auxiliary {

}