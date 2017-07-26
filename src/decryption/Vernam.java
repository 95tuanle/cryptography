package decryption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Vernam {
    public static DecodedStringVN crack(String pathToCipherText, String keyS) throws IOException {
        ArrayList<Character> alphabetFile = Decryption.processAlphabetFile();
        TreeMap<String, Double> mostCommonWords = Decryption.findMostCommonWords(-1);

        ArrayList<Character> cipherTextL = Decryption.readFile(pathToCipherText);
//        ArrayList<Character> key = Q1.readFile(pathToKey);
        ArrayList<Character> key = Decryption.stringToAL_C(keyS);
        ArrayList<Integer> cipherTextI = Decryption.convertFromASCIIToDenisCode(cipherTextL, alphabetFile);
        ArrayList<Integer> keyI = Decryption.convertFromASCIIToDenisCode(key, alphabetFile);

        ArrayList<Integer> plainTextI = new ArrayList<>();

        for (int j = 0; j < cipherTextI.size(); j++) {
            Integer cipherChar = cipherTextI.get(j);
            Integer keyChar = keyI.get(j);
            Integer plainCharI = ((cipherChar - keyChar + alphabetFile.size()) % alphabetFile.size());
            plainTextI.add(plainCharI);
        }
        String decodedString = Decryption.AL_I_toString(plainTextI, alphabetFile);

        return new DecodedStringVN(decodedString, Decryption.scoring(decodedString, mostCommonWords), key);
    }
}
