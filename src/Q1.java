import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;


public class Q1 {
    private static int numberOfPrintResult = 3;

    static void crackCaesar(String path, int key) throws IOException {
        ArrayList<Character> alphabetFile = processAlphabetFile();
        int alphabetFileSize = alphabetFile.size();

        if (key < -1 || key >= alphabetFileSize) {
            throw new IllegalArgumentException("Invalid Key");
        }

        ArrayList<Character> old_string = readFile(path);
        ArrayList<Integer> convertedString = convertFromASCIIToDenisCode(old_string, alphabetFile);
//        TODOx reimplement process10000file
        TreeMap<String, Double> mostCommonWords = CommonWordAnalysis.process10000file(-1);
        DecodedString[] resultsAsArray = new DecodedString[alphabetFileSize];

        if (key == -1) {
            for (int i = 0; i < alphabetFileSize; i++) {
                crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, resultsAsArray, i);
            }
            Arrays.sort(resultsAsArray, resultsAsArray[0]);
            System.out.println("TOP 3 RESULTS\n");
            for (int i = 0; i < numberOfPrintResult; i++) {
                System.out.println("Key    " + resultsAsArray[i].getKey());
                System.out.println("Score  " + resultsAsArray[i].getScore());
                System.out.println("Decoded string:\n\n" + resultsAsArray[i].getDecodedString() + "\n");
            }
        } else {
            crackCaesarWithSpecificKey(alphabetFile, convertedString, mostCommonWords, alphabetFileSize, resultsAsArray, key);
            System.out.println("RESULT\n");
            System.out.println("Key    " + resultsAsArray[key].getKey());
            System.out.println("Score  " + resultsAsArray[key].getScore());
            System.out.println("Decoded string:\n\n" + resultsAsArray[key].getDecodedString() + "\n");
        }
        System.out.println();

    }

    static void crackCaesarWithSpecificKey(ArrayList<Character> alphabetFile, ArrayList<Integer> convertedString, TreeMap<String, Double> mostCommonWords, int alphabetFileSize, DecodedString[] resultsAsArray, int i) throws IOException {
        ArrayList<Integer> newString = new ArrayList<>();
        for (Integer elementInConvertedString : convertedString) {
            int newCharacter = ((elementInConvertedString + i) % alphabetFileSize);
            newString.add(newCharacter);
        }
        String decodedString = decodeString(newString, alphabetFile);
        resultsAsArray[i] = new DecodedString(decodedString, scoring(decodedString, mostCommonWords), i);
    }

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

    private static String decodeString(ArrayList<Integer> originalStringInDenisCode, ArrayList<Character> alphabetFile) {
        /*
        * 1. Convert orinial to DenisCode
        * 2. Plus 1
        * 3. Convert Denis code to character
        * */

//        COVERT ORIGINAL TO DENIS CODE
//        TODOx better way to convert?
//        HashMap<Integer, Character> denisCode = new HashMap<>();
//        for (int i = 0; i < alphabetFile.size(); i++) {
//            denisCode.put(i, alphabetFile.get(i));
//        }
//        ArrayList<Character> stringAfterConverted = new ArrayList<>();
        StringBuilder stringAfterConvertedStringForm = new StringBuilder();

        for (Integer anOriginalCharacter : originalStringInDenisCode) {
//            Character correspondingValue = denisCode.get(anOriginalCharacter);
//            stringAfterConverted.add(correspondingValue);
            stringAfterConvertedStringForm.append(alphabetFile.get(anOriginalCharacter));
        }
//        Object[] decodedStringAsArrayOfObject = stringAfterConverted.toArray();
//        char[] decodedStringAsCharArray = arrayObjectToCharArray(decodedStringAsArrayOfObject);
        return stringAfterConvertedStringForm.toString();
    }

    static ArrayList<Character> readFile(String fileName) throws IOException {
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

    static ArrayList<Character> processAlphabetFile() throws IOException {
        ArrayList<Character> in = readFile("sourceFile/alphabet.txt");
//        TODOx anyway to not hard-code? Idk and I also don't have time
//        in.remove(51);
        in.remove(39);
        in.set(39, '\n');
//        in.remove(50);
        return in;
    }

    public static ArrayList<Integer> convertFromASCIIToDenisCode(ArrayList<Character> originalString, ArrayList<Character> alphabetFile) {
//        TODOx if have time think of a more efficient algorithm
//        HashMap<Character, Integer> denisCode = new HashMap<>();
//        for (int i = 0; i < alphabetFile.size(); i++) {
//            denisCode.put(alphabetFile.get(i), i);
//        }
        ArrayList<Integer> stringAfterConverted = new ArrayList<>();
        for (Character anOriginalCharacter : originalString) {
//            Integer correspondingValue = denisCode.get(anOriginalCharacter);
//            stringAfterConverted.add(correspondingValue);
            stringAfterConverted.add(alphabetFile.indexOf(anOriginalCharacter));
        }
        return stringAfterConverted;
    }
}

class DecodedString extends DecodedStringCore {
    private int key;

    DecodedString(String decodedString, double score, int key) {
        super(decodedString, score);
        this.key = key;
    }

    int getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Key = " + key + "\n" + super.toString();
    }
}

class DecodedStringCore implements Comparator<DecodedStringCore> {
    private String decodedString;
    //    private int key;
    private double score;

    //  TODOx any other classes?
//    DecodedStringCore() {
//        decodedString = "";
////        key = 0;
//        score = 0;
//    }

    DecodedStringCore(String decodedString, double score) {
        this.decodedString = decodedString;
//        this.key = key;
        this.score = score;
    }

    String getDecodedString() {
        return decodedString;
    }

    double getScore() {
        return score;
    }

    @Override
    public int compare(DecodedStringCore o1, DecodedStringCore o2) {
        if (o1.score > o2.score) return 1;
        else if (o1.score < o2.score) return -1;
        else return 0;
    }

    @Override
    public String toString() {
        return "Score = " + score + "\n" +
                "Decoded String = '" + decodedString + '\'' + "\n";
    }
}

/*Outline
* Tạo ra class mới
* Thay vì store dưới dạng Map thì store dưới dạng Aray of object
* Implement method comparator
* Dùng Array.sort
* Lọc ra top 3
* */
