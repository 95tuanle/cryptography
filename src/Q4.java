import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class Q4 {
    public static void main(String[] args) throws IOException {
        /*General Strategy
        * Read alpha bet file
        * Find the frequency of each letter in the cipher text
        * Print result to file
        * Use excel to do analysis
        * Replace
        * Think
        * */
        ReturnResultFromCreateFrequencyTableMethod result = createFrequencyTable();
        ArrayList<Character> alphabetFile = result.getAlphabetFile();
        ArrayList<CharacterFrequency> cipherFreqTable = result.getCharFreq();
        cipherFreqTable.sort(cipherFreqTable.get(0));

        ArrayList<CharacterFrequency> englishMonogram = processEnglishMonogram();
//        ArrayList<Character> key = (ArrayList<Character>) alphabetFile.clone();
        TreeMap<Character, Character> key = new TreeMap<>();
        for (int i = 0; i < englishMonogram.size(); i++) {
            key.put(englishMonogram.get(i).getCharacter(), cipherFreqTable.get(i).getCharacter());
        }
        System.out.println("Hello");
    }

    private static ArrayList<CharacterFrequency> processEnglishMonogram() throws IOException {
//        FileReader fileReader = new FileReader("sourceFile/english_monograms.txt");
        List<String> lines = Files.readAllLines(Paths.get("sourceFile/english_monograms.txt"));
//        LinkedHashMap<Character, Integer> monographFrequency = new LinkedHashMap<>();

        ArrayList<CharacterFrequency> monoFreq = new ArrayList<>();
        for (String next : lines) {
            String[] splittingResult = next.split(" ");
//            monographFrequency.put(splittingResult[0].charAt(0), Integer.valueOf(splittingResult[1]));
            char character = splittingResult[0].charAt(0);
            int frequency = Integer.valueOf(splittingResult[1]);
            monoFreq.add(new CharacterFrequency(character, frequency));
        }
        return monoFreq;

    }

    private static ReturnResultFromCreateFrequencyTableMethod createFrequencyTable() throws IOException {
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        String cipherText = getCipherText();
        int alphabetFileSize = alphabetFile.size();
        int frequency;
//        int[] charFreq = new int[alphabetFileSize];
        ArrayList<CharacterFrequency> charFreq = new ArrayList<>();
//        PrintWriter printWriter = new PrintWriter("sourceFile/frequency_table.csv");
        for (Character anAlphabetFile : alphabetFile) {
            frequency = findFrequency(anAlphabetFile, cipherText, alphabetFileSize);
//            charFreq[i] = frequency;
            charFreq.add(new CharacterFrequency(anAlphabetFile, frequency));
//            printWriter.println(alphabetFile.get(i) + "\t" + charFreq[i]);
        }
//        printWriter.close();

        return new ReturnResultFromCreateFrequencyTableMethod(alphabetFile, charFreq);
    }

    private static int findFrequency(Character character, String cipherText, int alphabetFileSize) {
        int frequency = 0;
        int startingIndex = 0;
        int index;
        do {
            index = cipherText.indexOf(character, startingIndex);
            if (index != -1) {
                frequency++;
                startingIndex = index + 1;
            } else {
                break;
            }
        } while (true);

        return frequency;
    }

    private static String getCipherText() throws IOException {
        ArrayList<Character> alphabetFile = Q1.readFile("sourceFile/msg3.enc");
        StringBuilder alphabetFileAsString = new StringBuilder();
        for (Character anAlphabetFile : alphabetFile) {
            alphabetFileAsString.append(anAlphabetFile);
        }
//        Character[] alphabetFileAsCharacterArray = new Character[alphabetFile.size()];
//        alphabetFile.toArray(alphabetFileAsCharacterArray);
//        alphabetFileAsString.replace(39, 41, "\n");
        return alphabetFileAsString.toString();
    }

    static class ReturnResultFromCreateFrequencyTableMethod {
        ArrayList<Character> alphabetFile;
        ArrayList<CharacterFrequency> charFreq;

        ReturnResultFromCreateFrequencyTableMethod(ArrayList<Character> alphabetFile, ArrayList<CharacterFrequency> frequencyTable) {
            this.alphabetFile = alphabetFile;
            this.charFreq = frequencyTable;
        }

        ArrayList<Character> getAlphabetFile() {
            return alphabetFile;
        }

        ArrayList<CharacterFrequency> getCharFreq() {
            return charFreq;
        }
    }

    static class CharacterFrequency implements Comparator<CharacterFrequency> {
        char character;
        int frequency;

        CharacterFrequency(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }

        int getFrequency() {
            return frequency;
        }

        char getCharacter() {
            return character;
        }

        @Override
        public int compare(CharacterFrequency o1, CharacterFrequency o2) {
            if (o1.getFrequency() > o2.getFrequency()) return -1;
            else if (o1.getFrequency() < o2.getFrequency()) return 1;
            return 0;
        }
    }
}
