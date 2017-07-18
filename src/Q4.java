import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/*General Strategy
* Read alpha bet file
* Find the frequency of each letter in the cipher text
* Print result to file
* Use excel to do analysis
* Replace
* Think
* */

/*Strategy 2
    Create a group of character that x could be
    create different threads for the most common characters
    for each possibility, generate a new string > score
    if score > than certain threshold > print
* */

public class Q4 {
    public static void main(String[] args) throws IOException {
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        ArrayList<Character> old_string = Q1.readFile("sourceFile/msg4.enc");
        TreeMap<String, Integer> mostCommonWords = CommonWordAnalysis.process10000file();

        ArrayList<CharacterFrequency> cipherFreqTable = createFrequencyTable(alphabetFile, old_string);
        ArrayList<CharacterFrequency> englishMonogram = processEnglishMonogram();
        preparePossibleCharacter(cipherFreqTable, englishMonogram);

        HashSet<DecodedStringOfRandomSub> results = new HashSet<>();
        TreeMap<Character, Character> key = new TreeMap<>();
//        for (int i = 0; i < englishMonogram.size(); i++) {
//            key.put(cipherFreqTable.get(i).getCharacter(),englishMonogram.get(i).getCharacter());
//        }


//        DecodedStringOfRandomSub decodedString = crackRandomSubstitution(key, old_string, mostCommonWords);
//        results.add(decodedString);

        noName(cipherFreqTable, key, 0, mostCommonWords, old_string, results);
        DecodedStringOfRandomSub[] resultsAsArray = new DecodedStringOfRandomSub[results.size()];
        results.toArray(resultsAsArray);
        Arrays.sort(resultsAsArray, (o1, o2) -> {
            if (o1.getScore() > o2.getScore()) return 1;
            if (o1.getScore() < o2.getScore()) return -1;
            return 0;
        });

        System.out.println("Hello");
    }

    private static void noName(ArrayList<CharacterFrequency> cipherFreqTable, TreeMap<Character, Character> key,
                               int index, TreeMap<String, Integer> mostCommonWords, ArrayList<Character> old_string, HashSet<DecodedStringOfRandomSub> results) throws IOException {
        if (index >= cipherFreqTable.size()) return;
        CharacterFrequency character = cipherFreqTable.get(index);
        for (int i = 0; i < character.getPossibleCharacter().size(); i++) {
            Character possibleCharacter = character.getPossibleCharacter().get(i);
            Collection<Character> values = key.values();
            if (values.contains(possibleCharacter) && possibleCharacter != ' ') continue;
            if (key.containsKey(character.getCharacter())) key.remove(character.getCharacter());
//            TODOx be aware, what if in the end the character matches with nothing
            key.put(character.getCharacter(), possibleCharacter);
            noName(cipherFreqTable, key, ++index, mostCommonWords, old_string, results);
            DecodedStringOfRandomSub decodedString = crackRandomSubstitution(key, old_string, mostCommonWords);
            results.add(decodedString);
        }
        if (!key.containsKey(character.getCharacter()))
            throw new RuntimeException("All possible characters for character " + character.getCharacter() + " has been used up");
    }

    private static void preparePossibleCharacter(ArrayList<CharacterFrequency> cipherFreqTable, ArrayList<CharacterFrequency> englishMonogram) {
        for (int i = 0; i < englishMonogram.size() - 1; i++) {
            ArrayList<Character> possibleCharacter = cipherFreqTable.get(i).getPossibleCharacter();
            int startingIndex = (int) ((float) 22 / 25 * i);
            for (int j = startingIndex; j < startingIndex + 5; j++) {
                possibleCharacter.add(englishMonogram.get(j).getCharacter());
            }
            possibleCharacter.add(' ');
        }
        for (int i = englishMonogram.size() - 1; i < cipherFreqTable.size(); i++) {
            cipherFreqTable.get(i).getPossibleCharacter().add(' ');
        }
    }

    private static DecodedStringOfRandomSub crackRandomSubstitution(TreeMap<Character, Character> key, ArrayList<Character> old_string, TreeMap<String, Integer> mostCommonWords) throws IOException {
//        ArrayList<Character> old_string = Q1.readFile(path);
//        TreeMap<String, Integer> mostCommonWords = CommonWordAnalysis.process10000file();

        ArrayList<Character> newString = new ArrayList<>();
        for (Character character : old_string) {
            Character newCharacter = key.get(character);
            if (newCharacter == null) {
                newString.add(' ');
            } else {
                newString.add(newCharacter);
            }
        }
//        TODOx need to check latter
        String decodedString = getStringRepresentation(newString);
//        String decodedString = newString.stream().map(e->e.toString()).collect(Collectors.joining());
        return new DecodedStringOfRandomSub(decodedString, key, Q1.scoring(decodedString, mostCommonWords));

//        System.out.println("RESULT\n");
//        System.out.println("Key    " + decodedStringOfRandomSub.getKey());
//        System.out.println("Score  " + decodedStringOfRandomSub.getScore());
//        System.out.println("Decoded string: " + decodedStringOfRandomSub.getDecodedString() + "\n");
    }

    private static String getStringRepresentation(ArrayList<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for (Character ch : list) {
            builder.append(ch);
        }
        return builder.toString();
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

    private static ArrayList<CharacterFrequency> createFrequencyTable(ArrayList<Character> alphabetFile, ArrayList<Character> old_string) throws IOException {
//        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
//        String cipherText = getCipherText();
        String cipherText = getStringRepresentation(old_string);
//        int alphabetFileSize = alphabetFile.size();
        int frequency;
//        int[] charFreq = new int[alphabetFileSize];
        ArrayList<CharacterFrequency> charFreq = new ArrayList<>();
//        PrintWriter printWriter = new PrintWriter("sourceFile/frequency_table.csv");
        for (Character letter : alphabetFile) {
            frequency = findFrequency(letter, cipherText);
//            charFreq[i] = frequency;
            charFreq.add(new CharacterFrequency(letter, frequency));
//            printWriter.println(alphabetFile.get(i) + "\t" + charFreq[i]);
        }
//        printWriter.close();
        charFreq.sort(charFreq.get(0));
        return charFreq;
    }

    private static int findFrequency(Character character, String cipherText) {
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

//    private static String getCipherText() throws IOException {
//        ArrayList<Character> alphabetFile = Q1.readFile("sourceFile/msg3.enc");
//        StringBuilder alphabetFileAsString = new StringBuilder();
//        for (Character anAlphabetFile : alphabetFile) {
//            alphabetFileAsString.append(anAlphabetFile);
//        }
////        Character[] alphabetFileAsCharacterArray = new Character[alphabetFile.size()];
////        alphabetFile.toArray(alphabetFileAsCharacterArray);
////        alphabetFileAsString.replace(39, 41, "\n");
//        return alphabetFileAsString.toString();
//    }

//    static class ReturnResultFromCreateFrequencyTableMethod {
//        ArrayList<Character> alphabetFile;
//        ArrayList<CharacterFrequency> charFreq;
//
//        ReturnResultFromCreateFrequencyTableMethod(ArrayList<Character> alphabetFile, ArrayList<CharacterFrequency> frequencyTable) {
//            this.alphabetFile = alphabetFile;
//            this.charFreq = frequencyTable;
//        }
//
//        ArrayList<Character> getAlphabetFile() {
//            return alphabetFile;
//        }
//
//        ArrayList<CharacterFrequency> getCharFreq() {
//            return charFreq;
//        }
//    }

    static class CharacterFrequency implements Comparator<CharacterFrequency> {
        char character;
        int frequency;
        ArrayList<Character> possibleCharacter;

        CharacterFrequency(char character, int frequency) {
            possibleCharacter = new ArrayList<>();
            this.character = character;
            this.frequency = frequency;
        }

        ArrayList<Character> getPossibleCharacter() {
            return possibleCharacter;
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

class DecodedStringOfRandomSub extends DecodedStringCore {
    private TreeMap<Character, Character> key;

//    public TreeMap<Character, Character> getKey() {
//        return key;
//    }

    DecodedStringOfRandomSub(String decodedString, TreeMap<Character, Character> key, int score) {
        super(decodedString, score);
        this.key = key;
    }

    @Override
    public String toString() {
        return "Key = " + key + "\n" + super.toString();
    }
}