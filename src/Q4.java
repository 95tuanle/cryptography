import java.io.*;
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
    private static final int SAMPLE_ENDING = 635;
    private static final int THRESHOLD = -450;
    private static final int englishMonogram = 5;

    public static void main(String[] args) throws Exception {
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        ArrayList<Character> old_string = Q1.readFile("sourceFile/msg4.enc");

        ArrayList<CharacterFrequency> mostCommonMonogram = processEnglishMonogram();
        TreeMap<String, Double> mostCommonTrigram = CommonWordAnalysis.processTriagramFile(-1);

//        TreeMap<String, Double> mostCommonDouble = CommonWordAnalysis.processDoubleFile();

//        TreeMap<String, Double> mostCommonInitialLetter = CommonWordAnalysis.processInitialLetterFile();

        TreeMap<String, Double> mostCommonOneLetterWord = CommonWordAnalysis.processOneLetterWordFile();
//        TreeMap<String, Double> mostCommonTwoLetterWord = CommonWordAnalysis.processTwoLetterWordFile();
//        TreeMap<String, Double> mostCommonThreeLetterWord = CommonWordAnalysis.processThreeLetterWordFile();
//        TreeMap<String, Double> mostCommonFourLetterWord = CommonWordAnalysis.processFourLetterWordFile();

        ArrayList<CharacterFrequency> cipherFreqTable = createFrequencyTable(alphabetFile, old_string);

        ArrayList<SpaceAnalysis> spaceAnalyses = analyzeSpacingCharacter(old_string, cipherFreqTable);


        /*ASSUMPTION
        * I am assuming that sample of size SAMPLE_ENDING is good enough!*/
        ArrayList<Character> sampleList = getSubList(old_string, 0, SAMPLE_ENDING);
//        TODOx analyze based on words
        String sampleString = getStringRepresentation(sampleList);
        String[] sampleWordsList = sampleString.split(Character.toString(spaceAnalyses.get(0).getSpace()));

        ArrayList<CharacterFrequency> letterUseInSample = getLetterUseInSample(sampleList, cipherFreqTable);

        HashSet<String> oneLetterWordFromCipher = getOneLetterWordFromCipher(sampleWordsList);
        TreeMap<String, Integer> twoLetterWordFromCipher = getTwoLetterWordFromCipher(sampleWordsList);

        preparePossibleCharacter(cipherFreqTable, mostCommonMonogram, spaceAnalyses, oneLetterWordFromCipher, mostCommonOneLetterWord);

        HashSet<DecodedStringOfRandomSub> results = new HashSet<>();
        TreeMap<Character, Character> key = new TreeMap<>();

        getKeyFromFile(key);
        key.put('$', '\'');
        DecodedStringOfRandomSub decodedStringOfRandomSub = crackRandomSubstitution(key, mostCommonTrigram, sampleWordsList);

        sampleString = sampleString.replaceAll("\n", "\\n");
        System.out.println(sampleString);
        System.out.println(decodedStringOfRandomSub.getDecodedString());
        FileWriter fileWriter = new FileWriter("sourceFile/Answer for Q4.txt");
        fileWriter.write(decodedStringOfRandomSub.getDecodedString());
        fileWriter.close();
        System.out.println();
//        noName(letterUseInSample, key, 0, mostCommonTrigram, sampleList, results, sampleWordsList);
//        DecodedStringOfRandomSub[] resultsAsArray = new DecodedStringOfRandomSub[results.size()];
//        results.toArray(resultsAsArray);
//        Arrays.sort(resultsAsArray, (o1, o2) -> {
//            if (o1.getScore() > o2.getScore()) return 1;
//            if (o1.getScore() < o2.getScore()) return -1;
//            return 0;
//        });
    }

    private static TreeMap<String, Integer> getTwoLetterWordFromCipher(String[] sampleWordsList) {
        TreeMap<String, Integer> result = new TreeMap<>();
        for (String word : sampleWordsList) {
            if (word.length() == 2) {
                if (result.containsKey(word)) {
                    result.put(word, result.get(word) + 1);
                } else {
                    result.put(word, 1);
                }
            }
        }
        return result;
    }

    private static HashSet<String> getOneLetterWordFromCipher(String[] sampleWordsList) {
        HashSet<String> oneLetterWordFromCipher = new HashSet<>();
        for (String word : sampleWordsList) {
            if (word.length() == 1) {
                oneLetterWordFromCipher.add(word);
            }
        }
        return oneLetterWordFromCipher;
    }

    private static void getKeyFromFile(TreeMap<Character, Character> key) throws Exception {
        FileReader fileReader = new FileReader("sourceFile/Cipher Letter Frequency.csv");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        boolean isLineContainNewlineChar = false;

        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals("")) {
                line = bufferedReader.readLine();
                isLineContainNewlineChar = true;
//                String plainCharacterAsString = lineComponents[2];
//                Character plainCharacter = plainCharacterAsString.charAt(0);
            }
            String[] lineComponents = line.split("\\t+");
            Character cipherCharacter;
            if (isLineContainNewlineChar) {
                cipherCharacter = '\n';
                isLineContainNewlineChar = false;
            } else {
                cipherCharacter = lineComponents[0].charAt(0);
            }
            Character plainCharacter;
            if (lineComponents.length == 3) {
                String plainCharacterAsString = lineComponents[2];
                plainCharacter = plainCharacterAsString.charAt(0);
                if (plainCharacter == 's') {
                    key.put(cipherCharacter, ' ');
                } else {
                    key.put(cipherCharacter, plainCharacter);
                }
            } else if (lineComponents.length == 2) {
                plainCharacter = '*';
                key.put(cipherCharacter, plainCharacter);
            } else throw new Exception("Strange thing happens!");
        }

    }

    private static void saveCipherLetterFrequencyToFile(ArrayList<CharacterFrequency> cipherFreqTable) throws IOException {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(new FileWriter("sourceFile/Cipher Letter Frequency.csv"));
            outputStream.println("Character\tFrequency\tKey");
            for (CharacterFrequency characterFrequency : cipherFreqTable) {
                if (characterFrequency.getFrequency() == 0) break;
                outputStream.print(characterFrequency.getCharacter() + "\t\t\t");
                outputStream.print(characterFrequency.getFrequency() + "\t\t\t\n");
            }

        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    private static ArrayList<SpaceAnalysis> analyzeSpacingCharacter(ArrayList<Character> old_string,
                                                                    ArrayList<CharacterFrequency> cipherFreqTable) {
        ArrayList<SpaceAnalysis> result = new ArrayList<>();

//        EXPLAIN test what if each character is a space
        for (CharacterFrequency cipherChar : cipherFreqTable) {
            if (cipherChar.getFrequency() == 0) break;
//            TODO solve unchecked casting if have time
            ArrayList<Character> newStringL = (ArrayList<Character>) old_string.clone();
//            EXPLAIN replace cipherChar with space
            if (cipherChar.getCharacter() == ' ') {
                for (int i = 0; i < newStringL.size(); i++) {
                    if (newStringL.get(i) == cipherChar.getCharacter()) {
                        newStringL.set(i, ' ');
                    }
                }
            } else {
                for (int i = 0; i < newStringL.size(); i++) {
                    if (newStringL.get(i) == ' ') {
                        newStringL.set(i, 'z');
                    }
                    if (newStringL.get(i) == cipherChar.getCharacter()) {
                        newStringL.set(i, ' ');
                    }
                }
            }
            String newStringS = getStringRepresentation(newStringL);
            String[] words = newStringS.split(" ");

//            EXPLAIN identify max word length
            int maxWordLength = 0;
            TreeMap<Integer, Integer> wordLengthAnalysis = new TreeMap<>();
            for (String word : words) {
                int wordLength = word.length();
                if (wordLength > maxWordLength) maxWordLength = wordLength;
                if (wordLengthAnalysis.containsKey(wordLength))
                    wordLengthAnalysis.put(wordLength, wordLengthAnalysis.get(wordLength) + 1);
                else wordLengthAnalysis.put(wordLength, 1);
            }
            result.add(new SpaceAnalysis(newStringS, maxWordLength, cipherChar.getCharacter(), wordLengthAnalysis));
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
                               int index, TreeMap<String, Double> mostCommonTriagram, HashSet<DecodedStringOfRandomSub> results, String[] sampleWordsList) throws IOException {
        if (index == cipherFreqTable.size()) {
            DecodedStringOfRandomSub decodedString = crackRandomSubstitution(key, mostCommonTriagram, sampleWordsList);
            if (decodedString.getScore() < THRESHOLD)
                results.add(decodedString);
//            if (key.values().contains(' ')) {
//            }
            return;
        }
        CharacterFrequency character = cipherFreqTable.get(index);

        TreeSet<Character> possibleCharacters = character.getPossibleCharacter();
        for (Character aPossibleCharacter : possibleCharacters) {
//            Character possibleCharacter = ((TreeSet<Character>) possibleCharacter).get(i);
            Collection<Character> values = key.values();
            if (values.contains(aPossibleCharacter)) continue;
//            TODOx be aware, what if in the end the character matches with nothing
            key.put(character.getCharacter(), aPossibleCharacter);
            noName(cipherFreqTable, key, index + 1, mostCommonTriagram, results, sampleWordsList);
            key.remove(character.getCharacter());
        }
//        if (key.containsKey(character.getCharacter()))

//        if (!key.containsKey(character.getCharacter()))
//            throw new RuntimeException("All possible characters for character " + character.getCharacter() + " has been used up");
    }

    private static void preparePossibleCharacter(ArrayList<CharacterFrequency> cipherFreqTable,
                                                 ArrayList<CharacterFrequency> mostCommonMonogram,
                                                 ArrayList<SpaceAnalysis> spaceAnalyses,
                                                 HashSet<String> oneLetterWordFromCipher,
                                                 TreeMap<String, Double> mostCommonOneLetterWord) {
//        EXPLAIN: create an array to keep track which cipher letter has been processed
        boolean[] usedIndexArray = new boolean[cipherFreqTable.size()];
        for (int i = 0; i < usedIndexArray.length; i++) {
            usedIndexArray[i] = false;
        }

//        EXPLAIN: HANDLE SPACE
//        ASSUMPTION spaceAnalyse[1/2/3/...] cannot be the space
        char cipherLetterForSpace = spaceAnalyses.get(0).getSpace();
        int indexOfSpace = indexOfLetter(cipherFreqTable, cipherLetterForSpace);
        cipherFreqTable.get(indexOfSpace).getPossibleCharacter().add(' ');
        usedIndexArray[indexOfSpace] = true;

//        EXPLAIN use most common one letter word result
        for (String oneLetterWord : oneLetterWordFromCipher) {
            int indexOfOneLetterWord = indexOfLetter(cipherFreqTable, oneLetterWord.charAt(0));
            for (Map.Entry<String, Double> entry : mostCommonOneLetterWord.entrySet()) {
                cipherFreqTable.get(indexOfOneLetterWord).getPossibleCharacter().add(entry.getKey().charAt(0));
                usedIndexArray[indexOfOneLetterWord] = true;
            }
        }
//        TODOx this needs to be fixed
//        EXPLAIN use English most common monogram result
        int numberOfLetterAdded = 0;
        for (int i = 0; i < mostCommonMonogram.size() && numberOfLetterAdded < englishMonogram; i++) {
            if (usedIndexArray[i]) continue;
            TreeSet<Character> possibleCharacter = cipherFreqTable.get(i).getPossibleCharacter();
            /*ASSUMPTION
            1. the distribution of cipher letter is the same as English letter
            2. Top 5 letters of the ciphertext mataches top 5 letter of English most common monogram
            */
            for (int j = 0; j < englishMonogram; j++) {
                possibleCharacter.add(mostCommonMonogram.get(j).getCharacter());
            }
//            usedIndex.add(i);
            usedIndexArray[i] = true;
            numberOfLetterAdded++;
            /*ASS-UMPTIONS:
            possible character is in the range of [starting index; st. index +5]
            possible character is limited in the 26 alphabet letters*/
//            int startingIndex = (int) ((float) 22 / 25 * i);
//            for (int j = startingIndex; j < startingIndex + 5; j++) {
//                possibleCharacter.add(englishMonogram.get(j).getCharacter());
//            }
//            possibleCharacter.add(' ');
        }
        for (int i = mostCommonMonogram.size() - 1; i < cipherFreqTable.size(); i++) {
            /*ASSUMPTIONS:
            possible character of uncommon character is " " > this is false for sure*/
            if (cipherFreqTable.get(i).getFrequency() != 0) cipherFreqTable.get(i).getPossibleCharacter().add(' ');
            else break;
        }
    }

    private static int indexOfLetter(ArrayList<CharacterFrequency> cipherFreqTable, char searchCharacter) {
        for (int i = 0; i < cipherFreqTable.size(); i++) {
            CharacterFrequency aCipherLetter = cipherFreqTable.get(i);
            if (aCipherLetter.getCharacter() == searchCharacter) {
                return i;
            }
        }
        return -1;
    }

    private static DecodedStringOfRandomSub crackRandomSubstitution(TreeMap<Character, Character> key, TreeMap<String, Double> mostCommonTriagram, String[] sampleWordsList) throws IOException {
//        String[] plaintextWordList = new String[sampleWordsList.length];
        StringBuilder plainTextWordListSB = new StringBuilder();
        for (String sampleWord : sampleWordsList) {
            StringBuilder plainWord = new StringBuilder("");
            for (int j = 0; j < sampleWord.length(); j++) {
                plainWord.append(key.get(sampleWord.charAt(j)));
                plainTextWordListSB.append(key.get(sampleWord.charAt(j)));
            }
//            plaintextWordList[i] = plainWord.toString();
            plainTextWordListSB.append(' ');
        }

//        new String()
        String decodedString = plainTextWordListSB.toString();
//        ArrayList<Character> newString = new ArrayList<>();
//        for (Character character : old_string) {
//            Character newCharacter = key.get(character);
//            newString.add(newCharacter);
//        }
//        TODOx need to check latter
//        String decodedString = getStringRepresentation(newString);
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
            /*ASSUMPTION: not exist word with length greater than 20*/
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
        private TreeMap<Integer, Integer> wordLengthAnalysis;

        SpaceAnalysis(String string, int max_word_size, char space) {
            this.string = string;
            this.max_word_size = max_word_size;
            this.space = space;
        }

        SpaceAnalysis(String string, int max_word_size, char space, TreeMap<Integer, Integer> wordLengthAnalysis) {
            this.string = string;
            this.max_word_size = max_word_size;
            this.space = space;
            this.wordLengthAnalysis = wordLengthAnalysis;
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
        TreeSet<Character> possibleCharacter;

        CharacterFrequency(char character, int frequency) {
            possibleCharacter = new TreeSet<>();
            this.character = character;
            this.frequency = frequency;
        }

        TreeSet<Character> getPossibleCharacter() {
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