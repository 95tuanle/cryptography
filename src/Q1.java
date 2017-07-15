import java.io.*;
import java.util.*;


public class Q1 {
    private static boolean printFrequencyFile = false;


    static void answer() throws IOException {
        ArrayList<Character> alphabetFile = processAlphabetFile();
//        TODO Is there anyway to save time, i.e. combine read file and covert from ASCII into 1
        ArrayList<Character> old_string = readFile("sourceFile/msg1.enc");
        ArrayList<Integer> convertedString = convertFromASCIIToDenisCode(old_string, alphabetFile);
        TreeMap<String, Integer> mostCommonWords = process10000file();
        int[] frequency = frequencyAnalysis(mostCommonWords);
        int commonWordSize = calculateCommonWordSize(mostCommonWords, frequency);

        int alphabetFileSize = alphabetFile.size();
//        LinkedHashMap<String, Integer> results = new LinkedHashMap<>();
        DecodedString[] resultsAsArray = new DecodedString[50]; // TODO no hard-coded

        for (int i = 0; i < alphabetFile.size(); i++) {
            ArrayList<Integer> newString = new ArrayList<>();
            for (Integer elementInConvertedString : convertedString) {
//                System.out.println(j);
                int newCharacter = ((elementInConvertedString + i) % alphabetFileSize);
                newString.add(newCharacter);
            }
            String decodedString = decodeString(newString, alphabetFile);
//            results.put(decodedString, 0);
            resultsAsArray[i] = new DecodedString(decodedString, i, scoring(decodedString, mostCommonWords, commonWordSize));
//            System.out.println("The key is: " + i);
//            System.out.println(decodedString);
//            System.out.println("\n***************************************\n");
        }

        Arrays.sort(resultsAsArray, resultsAsArray[0]);
        System.out.println("TOP 3 RESULTS\n");
//        TODO no hard-code
        for (int i = resultsAsArray.length - 1; i > resultsAsArray.length - 4; i--) {
//            System.out.println("Result " + i);
            System.out.println("Key    " + resultsAsArray[i].getKey());
            System.out.println("Score  " + resultsAsArray[i].getScore());
            System.out.println("Decoded string:\n\n" + resultsAsArray[i].getDecodedString() + "\n");
        }
        System.out.println();
    }

    private static int calculateCommonWordSize(TreeMap<String, Integer> mostCommonWords, int[] frequency) {
        int commonWordSize = 0;
        int totalNumberOfWords = frequency[commonWordSize];
        while ((double) totalNumberOfWords / mostCommonWords.size() < 0.99) {
            commonWordSize++;
            totalNumberOfWords += frequency[commonWordSize];
        }
        return commonWordSize;
    }

    private static int scoring(String decodedString, TreeMap<String, Integer> mostCommonWords, int commonWordSize) throws IOException {
//        TODOx better way to score? idk but I dont have time for it
        String[] words = decodedString.split(" ");
        int score = 0;

        for (String word : words) {
//            TODOx do analyze to determine value of 15
            if (word.length() < commonWordSize) {
                if (mostCommonWords.containsKey(word.toLowerCase())) {
                    score++;
                }
            }
        }
        return score;

    }

    private static int[] frequencyAnalysis(TreeMap<String, Integer> mostCommonWords) throws IOException {
//      TODOx can I not hard-code 20? can but what's the point?
        int[] frequency = new int[20];
        for (Map.Entry<String, Integer> entry : mostCommonWords.entrySet()) {
            int stringLength = entry.getKey().length();
            frequency[stringLength - 1]++;
        }
        if (printFrequencyFile) {
            printFrequencyToCSV(frequency);
        }
        return frequency;
    }

    private static void printFrequencyToCSV(int[] frequency) throws IOException {
        PrintWriter outputStream = null;

        try {
            outputStream = new PrintWriter(new FileWriter("Frequency analysis.csv"));
            outputStream.println("Length\tFrequency");
            for (int i = 0; i < frequency.length; i++) {
                outputStream.print(i);
                outputStream.println("\t" + frequency[i]);
            }

        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
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

//    private static char[] arrayObjectToCharArray(Object[] decodedStringAsArrayOfObject) {
//        char[] returnResult = new char[decodedStringAsArrayOfObject.length];
//        for (int i = 0; i < returnResult.length; i++) {
//            returnResult[i] = (Character) decodedStringAsArrayOfObject[i];
//        }
//        return returnResult;
//    }

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
        in = new FileReader(fileName);
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
//    private static <E> void printArrayList(ArrayList<E> arrayList) {
//        for (E anArrayList : arrayList) {
//            System.out.print(anArrayList);
//        }
//    }

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

class DecodedString implements Comparator<DecodedString> {
    private String decodedString;
    private int key;
    private int score;

    //  TODO ngoài cái class này còn cái class nào khac nữa?
    DecodedString() {
        decodedString = "";
        key = 0;
        score = 0;
    }

    DecodedString(String decodedString, int key, int score) {
        this.decodedString = decodedString;
        this.key = key;
        this.score = score;
    }

    String getDecodedString() {
        return decodedString;
    }

    int getKey() {
        return key;
    }

    int getScore() {
        return score;
    }

    @Override
    public int compare(DecodedString o1, DecodedString o2) {
        if (o1.score > o2.score) return 1;
        else if (o1.score < o2.score) return -1;
        else return 0;
    }
}

/*Outline
* Tạo ra class mới
* Thay vì store dưới dạng Map thì store dưới dạng Aray of object
* Implement method comparator
* Dùng Array.sort
* Lọc ra top 3
* */