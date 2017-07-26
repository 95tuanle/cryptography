import decryption.DecodedStringVN;
import decryption.Decryption;
import decryption.Vernam;

import java.io.IOException;
import java.util.ArrayList;

public class Q5 {

    public static void main(String[] args) throws IOException {
//        TODOx clean up the code
//        EXPLAIN prepare basic files
        ArrayList<Character> alphabetFile = Decryption.processAlphabetFile();
        ArrayList<Character> cipherTextL = Decryption.readFile("sourceFile/msg5.enc");
        ArrayList<Integer> cipherTextI = Decryption.convertFromASCIIToDenisCode(cipherTextL, alphabetFile);
        ArrayList<Character> key = Decryption.readFile("sourceFile/Answer for Q4.txt");
        ArrayList<Integer> keyI = Decryption.convertFromASCIIToDenisCode(key, alphabetFile);
        ArrayList<DecodedStringVN> resultTS = new ArrayList<>();

//        EXPLAIN generate different keys
        for (int i = 0; i < keyI.size() - cipherTextI.size(); i++) {
            String keyS = getKey(i, cipherTextI, key);
//            EXPLAIN decode using Vernam
            DecodedStringVN decodedString = Vernam.crack("sourceFile/msg5.enc",
                    keyS);
            resultTS.add(decodedString);
        }

        Decryption.sortResult(resultTS);
        Decryption.printTopThreeResult(resultTS);
    }

    private static String getKey(int i, ArrayList<Integer> cipherTextI, ArrayList<Character> key) {
        StringBuilder result = new StringBuilder();
        for (int j = i; j < cipherTextI.size() + i; j++) {
            result.append(key.get(j));
        }
        return result.toString();
    }

}
