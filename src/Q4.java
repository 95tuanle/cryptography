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
    if score > than certain THRESHOLD > print
* */

public class Q4 {
    private static final int SAMPLE_ENDING = 60;
    private static final int THRESHOLD = -450;

    public static void main(String[] args) throws IOException {
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        ArrayList<Character> old_string = Q1.readFile("sourceFile/msg4.enc");
        TreeMap<String, Double> mostCommonTrigram = CommonWordAnalysis.processTriagramFile(-1);

        ArrayList<CharacterFrequency> cipherFreqTable = createFrequencyTable(alphabetFile, old_string);
        ArrayList<CharacterFrequency> englishMonogram = processEnglishMonogram();
        ArrayList<SpaceAnalysis> spaceAnalyses = analyzeSpacingCharacter(old_string, cipherFreqTable);
        preparePossibleCharacter(cipherFreqTable, englishMonogram, spaceAnalyses);


        /*TODO ASSUMPTION
        * I am assuming that sample of size SAMPLE_ENDING is good enough!*/
        ArrayList<Character> sample = getSubList(old_string, 0, SAMPLE_ENDING);
        ArrayList<CharacterFrequency> letterUseInSample = getLetterUseInSample(sample, cipherFreqTable);

        HashSet<DecodedStringOfRandomSub> results = new HashSet<>();
        TreeMap<Character, Character> key = new TreeMap<>();

        noName(letterUseInSample, key, 0, mostCommonTrigram, sample, results);
        DecodedStringOfRandomSub[] resultsAsArray = new DecodedStringOfRandomSub[results.size()];
        results.toArray(resultsAsArray);
        Arrays.sort(resultsAsArray, (o1, o2) -> {
            if (o1.getScore() > o2.getScore()) return 1;
            if (o1.getScore() < o2.getScore()) return -1;
            return 0;
        });
    }

    private static ArrayList<SpaceAnalysis> analyzeSpacingCharacter(ArrayList<Character> old_string,
                                                                    ArrayList<CharacterFrequency> cipherFreqTable) {
        ArrayList<SpaceAnalysis> result = new ArrayList<>();
        for (CharacterFrequency characterFrequency : cipherFreqTable) {
            if (characterFrequency.getFrequency() == 0) break;
//            TODO solve unchecked casting if have time
            ArrayList<Character> new_string = (ArrayList<Character>) old_string.clone();
            for (int i = 0; i < new_string.size(); i++) {
                if (new_string.get(i) == characterFrequency.getCharacter()) {
                    new_string.set(i, ' ');
                }
            }
            String new_string_as_String = getStringRepresentation(new_string);
            String[] words = new_string_as_String.split(" ");
            int maxWordLength = 0;
            for (String word : words) {
                if (word.length() > maxWordLength) maxWordLength = word.length();
            }
            result.add(new SpaceAnalysis(new_string_as_String, maxWordLength, characterFrequency.getCharacter()));
        }
        result.sort((o1, o2) -> {
            if (o1.getMax_word_size() > o2.getMax_word_size()) return 1;
            else if (o1.getMax_word_size() < o2.getMax_word_size()) return -1;
            return 0;
        });
        return result;
    }

    private static ArrayList<Character> getSubList(ArrayList<Character> list, int starting, int ending) {
        ArrayList<Character> result = new ArrayList<>();
        for (int i = starting; i < ending; i++) {
            result.add(list.get(i));
        }
        return result;
    }

    private static ArrayList<CharacterFrequency> getLetterUseInSample(ArrayList<Character> sample, ArrayList<CharacterFrequency> cipherFreqTable) {
        ArrayList<CharacterFrequency> returnResult = new ArrayList<>();
        for (CharacterFrequency characterFrequency : cipherFreqTable) {
            if (sample.contains(characterFrequency.getCharacter()) && !returnResult.contains(characterFrequency))
                returnResult.add(characterFrequency);
        }
        return returnResult;
    }

    private static void noName(ArrayList<CharacterFrequency> cipherFreqTable, TreeMap<Character, Character> key,
                               int index, TreeMap<String, Double> mostCommonTriagram, ArrayList<Character> cipherText, HashSet<DecodedStringOfRandomSub> results) throws IOException {
        if (index == cipherFreqTable.size()) {
            if (key.values().contains(' ')) {
                DecodedStringOfRandomSub decodedString = crackRandomSubstitution(key, cipherText, mostCommonTriagram);
                if (decodedString.getScore() < THRESHOLD)
                    results.add(decodedString);
            }
            return;
        }
        CharacterFrequency character = cipherFreqTable.get(index);
        for (int i = 0; i < character.getPossibleCharacter().size(); i++) {
            Character possibleCharacter = character.getPossibleCharacter().get(i);
            Collection<Character> values = key.values();
            if (values.contains(possibleCharacter)) continue;
//            TODOx be aware, what if in the end the character matches with nothing
            key.put(character.getCharacter(), possibleCharacter);
            noName(cipherFreqTable, key, index + 1, mostCommonTriagram, cipherText, results);
            key.remove(character.getCharacter());
        }
//        if (key.containsKey(character.getCharacter()))

//        if (!key.containsKey(character.getCharacter()))
//            throw new RuntimeException("All possible characters for character " + character.getCharacter() + " has been used up");
    }

    private static void preparePossibleCharacter(ArrayList<CharacterFrequency> cipherFreqTable, ArrayList<CharacterFrequency> englishMonogram, ArrayList<SpaceAnalysis> spaceAnalyses) {
        char cipherLetterForSpace = spaceAnalyses.get(0).getSpace();
        int indexOfCipherLetterForSpace = 0;
        for (int i = 0; i < cipherFreqTable.size(); i++) {
            CharacterFrequency aCipherFreqTable = cipherFreqTable.get(i);
            if (aCipherFreqTable.getCharacter() == cipherLetterForSpace) {
                indexOfCipherLetterForSpace = i;
                aCipherFreqTable.getPossibleCharacter().add(' ');
                break;
            }
        }

//        TODO this needs to be fixed
        for (int i = 0; i < englishMonogram.size() - 1; i++) {
            if (i == indexOfCipherLetterForSpace) continue;
            ArrayList<Character> possibleCharacter = cipherFreqTable.get(i).getPossibleCharacter();
            /*TODO ASSUMPTIONS:
            possible character is in the range of [starting index; st. index +5]
            possible character is limited in the 26 alphabet letters*/
            int startingIndex = (int) ((float) 22 / 25 * i);
            for (int j = startingIndex; j < startingIndex + 5; j++) {
                possibleCharacter.add(englishMonogram.get(j).getCharacter());
            }
//            possibleCharacter.add(' ');
        }
        for (int i = englishMonogram.size() - 1; i < cipherFreqTable.size(); i++) {
            /*TODO ASSUMPTIONS:
            possible character of uncommon character is " "*/
            if (cipherFreqTable.get(i).getFrequency() != 0) cipherFreqTable.get(i).getPossibleCharacter().add(' ');
            else break;
        }
    }

    private static DecodedStringOfRandomSub crackRandomSubstitution(TreeMap<Character, Character> key, ArrayList<Character> old_string, TreeMap<String, Double> mostCommonTriagram) throws IOException {
//        ArrayList<Character> old_string = Q1.readFile(path);
//        TreeMap<String, Integer> mostCommonWords = CommonWordAnalysis.process10000file();

        ArrayList<Character> newString = new ArrayList<>();
        for (Character character : old_string) {
            Character newCharacter = key.get(character);
//            if (newCharacter == null) {
//                newString.add(' ');
//            } else {
//            }
            newString.add(newCharacter);
        }
//        TODOx need to check latter
        String decodedString = getStringRepresentation(newString);
//        String decodedString = newString.stream().map(e->e.toString()).collect(Collectors.joining());
        DecodedStringOfRandomSub decodedStringOfRandomSub = new DecodedStringOfRandomSub(decodedString, key, scoringUsingTrigram(decodedString, mostCommonTriagram));
        if (decodedStringOfRandomSub.getScore() < THRESHOLD) {
//            System.out.println("RESULT\n");
            System.out.println("Key    " + decodedStringOfRandomSub.getKey());
            System.out.println("Score  " + decodedStringOfRandomSub.getScore());
            System.out.println("Decoded string: " + decodedStringOfRandomSub.getDecodedString() + "\n");
        }
        return decodedStringOfRandomSub;
    }

    private static double scoringUsingTrigram(String decodedString, TreeMap<String, Double> mostCommonTrigram) {
        String[] words = decodedString.split("\\s+");
        double score = 0;

        for (String word : words) {
            /*TODO ASSUMPTION: not exist word with length greater than 20*/
            if (word.length() > 20) return 0;
            if (word.length() < 12 && word.length() >= 3) {
                for (int i = 0; i < word.length() - 2; i++) {
                    if (mostCommonTrigram.containsKey(word.substring(i, i + 3))) {
                        score += mostCommonTrigram.get(word.substring(i, i + 3));
                    }
                }
            }
        }
        return score;
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

    static class SpaceAnalysis {
        private String string;
        private int max_word_size;
        private char space;

        SpaceAnalysis(String string, int max_word_size) {
            this.string = string;
            this.max_word_size = max_word_size;
        }

        SpaceAnalysis(String string, int max_word_size, char space) {
            this.string = string;
            this.max_word_size = max_word_size;
            this.space = space;
        }

        int getMax_word_size() {
            return max_word_size;
        }

        char getSpace() {
            return space;
        }
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

        @Override
        public String toString() {
            return "CharacterFrequency{" +
                    "character=" + character +
                    ", possibleCharacter=" + possibleCharacter +
                    '}';
        }
    }
}

class DecodedStringOfRandomSub extends DecodedStringCore {
    private TreeMap<Character, Character> key;

    DecodedStringOfRandomSub(String decodedString, TreeMap<Character, Character> key, double score) {
        super(decodedString, score);
        this.key = key;
    }

    TreeMap<Character, Character> getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Key = " + key + "\n" + super.toString();
    }
}