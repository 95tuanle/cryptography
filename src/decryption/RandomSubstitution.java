package decryption;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static decryption.Decryption.processTriagramFile;

public class RandomSubstitution {

    public static final double BIET_BAO_NHIEU_PHAN_TRAM_CHU = 0.40;

    public static void main(String[] args) throws IOException, CannotFindCipherLetter {
        crack("sourceFile/msg4.enc");
    }
    private static final int THRESHOLD = -450;

    private static ArrayList<Character> cloneAL_C(ArrayList<Character> characterArrayList) {
        ArrayList<Character> result = new ArrayList<>();
        result.addAll(characterArrayList);
        return result;
    }

    private static double scoringUsingTrigram(String decodedString, TreeMap<String, Double> mostCommonTrigram) {
        String[] words = decodedString.split("\\s+");
        return actualTrigramScoring(words, mostCommonTrigram);
    }

    private static double scoringUsingTrigram(String[] words, TreeMap<String, Double> mostCommonTrigram) {
        return actualTrigramScoring(words, mostCommonTrigram);
    }

    private static double actualTrigramScoring(String[] words, TreeMap<String, Double> mostCommonTrigram) {
        double score = 0;
        for (String word : words) {
            /*ASSUMPTION: not exist word with length greater than 20*/
//            if (word.length() > 20) return 0;
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

    public static TreeMap<Character, Character> getKeyFromFile(String fileName) throws Exception {
        TreeMap<Character, Character> key = new TreeMap<>();
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        boolean isLineContainNewlineChar = false;

        while ((line = bufferedReader.readLine()) != null) {
//            if (line.equals("")) {
//                line = bufferedReader.readLine();
//                isLineContainNewlineChar = true;
//            }
            String[] lineComponents = line.split("\\t+");
            Character cipherCharacter;
            cipherCharacter = lineComponents[0].charAt(0);
            if (cipherCharacter == 'n') cipherCharacter = '\n';
            Character plainCharC;
            if (lineComponents.length == 3) {
                String plainCharS = lineComponents[2];
                plainCharC = plainCharS.charAt(0);
                if (plainCharC == 's') {
                    key.put(cipherCharacter, ' ');
                } else if (plainCharC == 'n') {
                    key.put(cipherCharacter, '\n');
                } else if (plainCharC == 'q') {
                    key.put(cipherCharacter, '\'');
                } else {
                    key.put(cipherCharacter, plainCharC);
                }
            } else if (lineComponents.length == 2) {
                plainCharC = '*';
                key.put(cipherCharacter, plainCharC);
            } else throw new Exception("Strange thing happens!");
        }
        return key;
    }

    //    TODOx fix if have time
    public static DecodedString.DecodedStringRS crack(TreeMap<Character, Character> key, TreeMap<String, Double> mostCommonTriagram, String[] cipher) throws IOException {
        StringBuilder plainTextSB = new StringBuilder();
        for (String word : cipher) {
//            StringBuilder plainWord = new StringBuilder("");
            for (int j = 0; j < word.length(); j++) {
//                plainWord.append(key.get(word.charAt(j)));
                plainTextSB.append(key.get(word.charAt(j)));
            }
            plainTextSB.append(' ');
        }

//        new String()
        String decodedString = plainTextSB.toString();
//        ArrayList<Character> newString = new ArrayList<>();
//        for (Character character : old_string) {
//            Character newCharacter = key.get(character);
//            newString.add(newCharacter);
//        }
//        TODOx need to check latter
//        String decodedString = getStringRepresentation(newString);
//        String decodedString = newString.stream().map(e->e.toString()).collect(Collectors.joining());
        DecodedString.DecodedStringRS decodedStringRS = new DecodedString.DecodedStringRS(decodedString, key, scoringUsingTrigram(decodedString, mostCommonTriagram));
        if (decodedStringRS.getScore() < THRESHOLD) {
//            System.out.println("RESULT\n");
            System.out.println("Key    " + decodedStringRS.getKey());
            System.out.println("Score  " + decodedStringRS.getScore());
            System.out.println("Decoded string: " + decodedStringRS.getDecodedString() + "\n");
        }
        return decodedStringRS;
    }

    public static DecodedString.DecodedStringRS crack(String path, TreeMap<Character, Character> key) throws IOException, KeyNotContainSpaceException {
        ArrayList<Character> cipherAL_C = Decryption.readFile(path);
        String cipherS = Decryption.AL_C_toString(cipherAL_C);
        String[] cipherWordsA = cipherS.split(findCipherCharCorrespondingToSpace(key));
        TreeMap<String, Double> mostCommonTrigram = processTriagramFile(-1);
        return crack(key, mostCommonTrigram, cipherWordsA);
    }

    public static DecodedString.DecodedStringRS crack(String[] cipherWordsA, TreeMap<Character, Character> key) throws IOException {
        TreeMap<String, Double> mostCommonTrigram = processTriagramFile(-1);
        return crack(key, mostCommonTrigram, cipherWordsA);
    }

    private static String findCipherCharCorrespondingToSpace(TreeMap<Character, Character> key) throws KeyNotContainSpaceException {
        for (Map.Entry<Character, Character> entry : key.entrySet()) {
            if (entry.getValue() == ' ') return Character.toString(entry.getKey());
        }
        throw new KeyNotContainSpaceException();
//        return null;
    }

    private static DecodedString.DecodedStringRS crack(String path) throws IOException, CannotFindCipherLetter {
//        EXPLAIN: basic preparation
        ArrayList<Character> cipherAL_C = Decryption.readFile(path);
        String cipherS = Decryption.AL_C_toString(cipherAL_C);
        ArrayList<Character> alphabetFile = Decryption.processAlphabetFile();

//        EXPLAIN: Do some basic analysis
        ArrayList<CharacterFrequency> cipherCharFreq = CipherAnalysis.monogramAnalysis(alphabetFile, cipherAL_C);
        ArrayList<SpaceAnalysis> spaceAnalyses = CipherAnalysis.analyzeSpacingCharacter(cipherAL_C, cipherCharFreq);

//        EXPLAIN: Gia dinh space
        char cipherLetterCorrespondingWithSpace = spaceAnalyses.get(0).getSpace();

//        EXPLAIN: Tach cipher thanh cac words
        String[] cipherWordsA = cipherS.split(Character.toString(cipherLetterCorrespondingWithSpace));

//        EXPLAIN: Word analysis
        TreeMap<String, Integer> oneLetterWordAnalysis = CipherAnalysis.getOneLetterWordFromCipher(cipherWordsA);
        Map<String, Integer> twoLetterWordAnalysis = CipherAnalysis.getTwoLetterWordFromCipher(cipherWordsA);

//        EXPLAIN: identify letters used in cipher
        ArrayList<CharacterFrequency> letterUsedIncipher = CipherAnalysis.getLetterUseInCipher(cipherAL_C, cipherCharFreq);
        Map<Character, Character> key = createKey(letterUsedIncipher);
        key.put(cipherLetterCorrespondingWithSpace, ' ');


//        EXPLAIN: Gia dinh chu a va chu e
        char letterCorrespondingWithA = getLetterCorrespondingWithA(oneLetterWordAnalysis, key);
//                oneLetterWordAnalysis.firstKey().charAt(0);
        key.put(letterCorrespondingWithA, 'A');
        char letterCorrespondingWithE = getLetterCorrespondingWithE(cipherCharFreq, key);
        key.put(letterCorrespondingWithE, 'E');

//        EXPLAIN Dua tren two-letter analys de gia dinh tiep
        DecodedString.DecodedStringRS decodedStringRS = crack(cipherWordsA, (TreeMap<Character, Character>) key);
        List guessableWords = findGuessableWords(decodedStringRS.getDecodedString().split(" "));

        printResult(cipherS, decodedStringRS);

        return null;
    }

    private static List findGuessableWords(String[] cipherWordsA) {
        List guessableWords = new ArrayList();
        for (String word: cipherWordsA){
            double percentage = daBietBaoNhieuPhanTram(word);
            if(percentage > BIET_BAO_NHIEU_PHAN_TRAM_CHU && percentage < 1) guessableWords.add(word);
        }

        return guessableWords;
    }

    private static double daBietBaoNhieuPhanTram(String word) {
        int length = word.length();
        int numberOfAsterisk = findNumberOfAsterisk(word);
        return (double) (length - numberOfAsterisk)/length;
    }

    private static int findNumberOfAsterisk(String word) {

        int numberOfAsterisk = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == '*') numberOfAsterisk++;
        }
        return numberOfAsterisk;
    }

    private static char getLetterCorrespondingWithE(ArrayList<CharacterFrequency> cipherCharFreq, Map<Character, Character> key) throws CannotFindCipherLetter {
        for (CharacterFrequency entry: cipherCharFreq){
            Character cipherLetter = entry.getCharacter();
            Character plainLetter = key.get(cipherLetter);
            if(plainLetter == '*'){
                return cipherLetter;
            }
        }
        throw new CannotFindCipherLetter("E");
    }

    private static char getLetterCorrespondingWithA(TreeMap<String, Integer> oneLetterWordAnalysis, Map<Character, Character> key) throws CannotFindCipherLetter {
        for (Map.Entry<String, Integer> entry: oneLetterWordAnalysis.entrySet()){
            Character cipherLetter = entry.getKey().charAt(0);
            Character plainLetter = key.get(cipherLetter);
            if(plainLetter == '*'){
                return cipherLetter;
            }
        }
        throw new CannotFindCipherLetter("A");
    }

    private static Map<Character, Character> createKey(ArrayList<CharacterFrequency> letterUsedIncipher) {
        Map<Character, Character> key = new TreeMap<>();
        for (CharacterFrequency aLetterUsedIncipher : letterUsedIncipher) {
            key.put(aLetterUsedIncipher.getCharacter(), '*');
        }
        return key;
    }

    public static void writeResultToFile(DecodedString.DecodedStringRS decodedStringRS, String path) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(decodedStringRS.getDecodedString());
        fileWriter.close();
    }

    public static void printResult(String cipherS, DecodedString.DecodedStringRS decodedStringRS) {
        cipherS = cipherS.replaceAll("\n", "\\n");
        System.out.println(cipherS);
        String decodedString = decodedStringRS.getDecodedString();
        decodedString = decodedString.replaceAll("\n", "\\n");
        System.out.println(decodedString);
    }

//    INNER CLASS

    private static class KeyNotContainSpaceException extends Exception {
    }

    private static class CannotFindCipherLetter extends Exception{
        public CannotFindCipherLetter(String message) {
            super(message);
        }
    }

    public static class SpaceAnalysis {
        private String string;
        private int max_word_size;
        private char space;
        private TreeMap<Integer, Integer> wordLengthAnalysis;

        SpaceAnalysis(String string, int max_word_size, char space, TreeMap<Integer, Integer> wordLengthAnalysis) {
            this.string = string;
            this.max_word_size = max_word_size;
            this.space = space;
            this.wordLengthAnalysis = wordLengthAnalysis;
        }

        int getMax_word_size() {
            return max_word_size;
        }

        public char getSpace() {
            return space;
        }
    }

    public static class CharacterFrequency implements Comparator<CharacterFrequency> {
        char character;
        int frequency;
        TreeSet<Character> possibleCharacter;

        CharacterFrequency(char character, int frequency) {
            possibleCharacter = new TreeSet<>();
            this.character = character;
            this.frequency = frequency;
        }

        public TreeSet<Character> getPossibleCharacter() {
            return possibleCharacter;
        }

        public int getFrequency() {
            return frequency;
        }

        public char getCharacter() {
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

    public static class CipherAnalysis {

        public static Map<String, Integer> getTwoLetterWordFromCipher(String[] cipher) {
            Map<String, Integer> result = new TreeMap<>();
            for (String word : cipher) {
                if (word.length() == 2) {
                    if (result.containsKey(word)) {
                        result.put(word, result.get(word) + 1);
                    } else {
                        result.put(word, 1);
                    }
                }
            }
            result = sortByValue(result);
            return result;
        }

        public static TreeMap<String, Integer> getOneLetterWordFromCipher(String[] cipher) {
            //        HashSet<String> oneLetterWordFromCipher = new HashSet<>();
            TreeMap<String, Integer> result = new TreeMap<>();
            for (String word : cipher) {
                if (word.length() == 1) {
                    //                oneLetterWordFromCipher.add(word);
                    if (result.containsKey(word)) {
                        result.put(word, result.get(word) + 1);
                    } else {
                        result.put(word, 1);
                    }
                }
            }
            return result;
        }

        public static ArrayList<SpaceAnalysis> analyzeSpacingCharacter(ArrayList<Character> cipher,
                                                                       ArrayList<CharacterFrequency> cipherCharFreq) {
            ArrayList<SpaceAnalysis> result = new ArrayList<>();

            //        EXPLAIN test what if each character is a space
            for (CharacterFrequency cipherCharCF : cipherCharFreq) {
                if (cipherCharCF.getFrequency() == 0) break;
                //            TODOx solve unchecked casting if have time
                ArrayList<Character> cipherClone = cloneAL_C(cipher);
                //            EXPLAIN replace cipherChar with space
                //            TODOx not sure
                char cipherCharC = cipherCharCF.getCharacter();
                if (cipherCharC != ' ') {
                    for (int i = 0; i < cipherClone.size(); i++) {
                        if (cipherClone.get(i) == ' ') {
                            //                        TODOx should not be 'z'
                            //                        TODO test
                            cipherClone.set(i, '\254');
                        }
                        if (cipherClone.get(i) == cipherCharC) {
                            cipherClone.set(i, ' ');
                        }
                    }
                }
                String stringAfterReplaceSpace = Decryption.AL_C_toString(cipherClone);
                String[] words = stringAfterReplaceSpace.split(" ");

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
                result.add(new SpaceAnalysis(stringAfterReplaceSpace, maxWordLength, cipherCharC, wordLengthAnalysis));
            }
            result.sort((o1, o2) -> {
                if (o1.getMax_word_size() > o2.getMax_word_size()) return 1;
                else if (o1.getMax_word_size() < o2.getMax_word_size()) return -1;
                return 0;
            });
            return result;
        }

        public static ArrayList<CharacterFrequency> getLetterUseInCipher(ArrayList<Character> cipher, ArrayList<CharacterFrequency> cipherFreqTable) {
            ArrayList<CharacterFrequency> returnResult = new ArrayList<>();
            for (CharacterFrequency characterFrequency : cipherFreqTable) {
                if (cipher.contains(characterFrequency.getCharacter()) && !returnResult.contains(characterFrequency))
                    returnResult.add(characterFrequency);
            }
            return returnResult;
        }

        public static ArrayList<CharacterFrequency> monogramAnalysis(ArrayList<Character> alphabetFile, ArrayList<Character> cipher) throws IOException {
            String cipherText = Decryption.AL_C_toString(cipher);
            int frequency;
            ArrayList<CharacterFrequency> charFreq = new ArrayList<>();
            for (Character letter : alphabetFile) {
                frequency = findCharFreq(letter, cipherText);
                charFreq.add(new CharacterFrequency(letter, frequency));
            }
            charFreq.sort(charFreq.get(0));
            return charFreq;
        }

        private static int findCharFreq(Character character, String cipherText) {
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

        static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map){
//            TODO read up on Generic, Interface, Anoynomous class
            List<Map.Entry<K, V>> list =
                    new LinkedList<>( map.entrySet() );
            Collections.sort( list, new Comparator<Map.Entry<K, V>>()
            {
                @Override
                public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
                {
                    return ( o2.getValue() ).compareTo( o1.getValue() );
                }
            } );

            Map<K, V> result = new LinkedHashMap<>();
            for (Map.Entry<K, V> entry : list)
            {
                result.put( entry.getKey(), entry.getValue() );
            }
            return result;
        }
    }

    public static class EnglishAnalysis {
        private static TreeMap<Integer, Integer> commonEnglishWord_LengthAnalysis(TreeMap<String, Double> mostCommonWords) throws IOException {
            //      TODOx can I not hard-code 20? can but what's the point?
            TreeMap<Integer, Integer> frequencyTM = new TreeMap<>();
            for (Map.Entry<String, Double> entry : mostCommonWords.entrySet()) {
                int stringLength = entry.getKey().length();
                if (frequencyTM.containsKey(stringLength)) {
                    frequencyTM.put(stringLength, frequencyTM.get(stringLength) + 1);
                } else {
                    frequencyTM.put(stringLength, 1);
                }
            }
            return frequencyTM;
        }

        public static int getLengthOfMostEnglishWord(TreeMap<String, Double> mostCommonWords) throws IOException {
            TreeMap<Integer, Integer> frequency = commonEnglishWord_LengthAnalysis(mostCommonWords);
            int commonWordSize = 0;
            int totalNumberOfWords = frequency.get(commonWordSize);
            while ((double) totalNumberOfWords / mostCommonWords.size() < 0.99) {
                commonWordSize++;
                totalNumberOfWords += frequency.get(commonWordSize);
            }
            return commonWordSize;
        }

        public static ArrayList<CharacterFrequency> processEnglishMonogram() throws IOException {
            List<String> lines = Files.readAllLines(Paths.get("sourceFile/english_monograms.txt"));

            ArrayList<CharacterFrequency> monoFreq = new ArrayList<>();
            for (String next : lines) {
                String[] splittingResult = next.split(" ");
                char character = splittingResult[0].charAt(0);
                int frequency = Integer.valueOf(splittingResult[1]);
                monoFreq.add(new CharacterFrequency(character, frequency));
            }
            return monoFreq;

        }
    }
}

/*Strategy
* Goal: Input: Ciphertext Output: plaintext
* 1. Do some basic analysis
*   - Space analysis
*   - Monogram analysis*
* 2. Gia dinh space
* 3. Tach cipher thanh cac words
* 4. Do these analysis
*   - Single letter word analysis
*   - Two letter word analysis
* 5. Gia dinh chu a
* 6. Gia dinh chu e
* 7. Dua tren two-letter analys de gia dinh tiep
*   - Tim ra nhung chu co the doan duoc
*   - Nhin trong tu dien de xem day co the la chu gi
*   - Cap nhat key
*   - Lap lai
* 8.
* */