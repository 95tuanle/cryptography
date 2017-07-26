import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Q5 {

    private static final int NUMBER_OF_WORDS_TO_PROCESS = 10000;

    public static void main(String[] args) throws IOException {
//        TODOx clean up the code
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        ArrayList<Character> cipherTextL = Q1.readFile("sourceFile/msg5.enc");
        ArrayList<Integer> cipherTextI = Q1.convertFromASCIIToDenisCode(cipherTextL, alphabetFile);
        ArrayList<Character> key = Q1.readFile("sourceFile/Answer for Q4.txt");
        ArrayList<Integer> keyI = Q1.convertFromASCIIToDenisCode(key, alphabetFile);

        ArrayList<DecodedStringVernam> resultTS = new ArrayList<>();
        for (int i = 0; i < keyI.size() - cipherTextI.size(); i++) {
            String keyS = getKey(i, cipherTextI, key);
            DecodedStringVernam decodedString = crackVernam("sourceFile/msg5.enc",
                    keyS);
            resultTS.add(decodedString);
        }

        resultTS.sort((o1, o2) -> {
            if (o1.getScore() > o2.getScore()) return 1;
            if (o1.getScore() < o2.getScore()) return -1;
            return 0;
        });

        System.out.println("TOP 3 RESULTS:");
        System.out.println();
        for (int i = 0; i < 3; i++) {
            System.out.println("SCORE: " + resultTS.get(i).getScore());
            System.out.println("DECODED STRING: " + resultTS.get(i).getDecodedString() + "\n");
            System.out.println("KEY: " + Q4.getStringRepresentation(resultTS.get(i).getKey()));
            System.out.println();
        }
    }

    private static String getKey(int i, ArrayList<Integer> cipherTextI, ArrayList<Character> key) {
        StringBuilder result = new StringBuilder();
        for (int j = i; j < cipherTextI.size() + i; j++) {
            result.append(key.get(j));
        }
        return result.toString();
    }

    private static DecodedStringVernam crackVernam(String pathToCipherText, String keyS) throws IOException {
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        TreeMap<String, Double> mostCommonWords = CommonWordAnalysis.process10000file(NUMBER_OF_WORDS_TO_PROCESS);

        ArrayList<Character> cipherTextL = Q1.readFile(pathToCipherText);
//        ArrayList<Character> key = Q1.readFile(pathToKey);
        ArrayList<Character> key = convertStringToALofCharacter(keyS);
        ArrayList<Integer> cipherTextI = Q1.convertFromASCIIToDenisCode(cipherTextL, alphabetFile);
        ArrayList<Integer> keyI = Q1.convertFromASCIIToDenisCode(key, alphabetFile);

        ArrayList<Integer> plainTextI = new ArrayList<>();

        for (int j = 0; j < cipherTextI.size(); j++) {
            Integer cipherChar = cipherTextI.get(j);
            Integer keyChar = keyI.get(j);
            Integer plainCharI = ((cipherChar - keyChar + alphabetFile.size()) % alphabetFile.size());
            plainTextI.add(plainCharI);
        }
        String decodedString = Q1.decodeString(plainTextI, alphabetFile);

        return new DecodedStringVernam(decodedString, Q1.scoring(decodedString, mostCommonWords), key);
    }

    private static ArrayList<Character> convertStringToALofCharacter(String keyS) {
        ArrayList<Character> result = new ArrayList<>();
        for (int i = 0; i < keyS.length(); i++) {
            result.add(keyS.charAt(i));
        }
        return result;
    }
}
