package decryption;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static decryption.Decryption.processTriagramFile;

public class RandomSubstitution {
    private static final int THRESHOLD = -450;

    private static ArrayList<Character> cloneAL_C(ArrayList<Character> characterArrayList) {
        ArrayList<Character> result = new ArrayList<>();
        result.addAll(characterArrayList);
        return result;
    }

    //    TODO fix if have time
    public static DecodedString.DecodedStringRS crackRandomSubstitution(TreeMap<Character, Character> key, TreeMap<String, Double> mostCommonTriagram, String[] cipher) throws IOException {
//        String[] plaintextWordList = new String[sampleWordsList.length];
        StringBuilder plainTextWordListSB = new StringBuilder();
        for (String sampleWord : cipher) {
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
        DecodedString.DecodedStringRS decodedStringRS = new DecodedString.DecodedStringRS(decodedString, key, scoringUsingTrigram(decodedString, mostCommonTriagram));
        if (decodedStringRS.getScore() < THRESHOLD) {
//            System.out.println("RESULT\n");
            System.out.println("Key    " + decodedStringRS.getKey());
            System.out.println("Score  " + decodedStringRS.getScore());
            System.out.println("Decoded string: " + decodedStringRS.getDecodedString() + "\n");
        }
        return decodedStringRS;
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

    public static DecodedString.DecodedStringRS crack(String path, TreeMap<Character, Character> key) throws IOException, KeyNotContainSpaceException {
        ArrayList<Character> cipherAL_C = Decryption.readFile(path);
        String cipherS = Decryption.AL_C_toString(cipherAL_C);
        String[] cipherWordsA = cipherS.split(findCipherCharCorrespondingToSpace(key));
        TreeMap<String, Double> mostCommonTrigram = processTriagramFile(-1);
        return crackRandomSubstitution(key, mostCommonTrigram, cipherWordsA);
    }

    public static DecodedString.DecodedStringRS crack(String[] cipherWordsA, TreeMap<Character, Character> key) throws IOException {
        TreeMap<String, Double> mostCommonTrigram = processTriagramFile(-1);
        return crackRandomSubstitution(key, mostCommonTrigram, cipherWordsA);
    }

    private static String findCipherCharCorrespondingToSpace(TreeMap<Character, Character> key) throws KeyNotContainSpaceException {
        for (Map.Entry<Character, Character> entry : key.entrySet()) {
            if (entry.getValue() == ' ') return Character.toString(entry.getKey());
        }
        throw new KeyNotContainSpaceException();
//        return null;
    }

//    INNER CLASS

    private static class KeyNotContainSpaceException extends Exception {

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

        public static TreeMap<String, Integer> getTwoLetterWordFromCipher(String[] cipher) {
            TreeMap<String, Integer> result = new TreeMap<>();
            for (String word : cipher) {
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
