import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Q5 {
    public static void main(String[] args) throws IOException {
        ArrayList<Character> alphabetFile = Q1.processAlphabetFile();
        ArrayList<Character> cipherTextL = Q1.readFile("sourceFile/msg5.enc");
        ArrayList<Integer> cipherTextI = Q1.convertFromASCIIToDenisCode(cipherTextL, alphabetFile);
        ArrayList<Character> key = Q1.readFile("sourceFile/Answer for Q4.txt");
        ArrayList<Integer> keyI = Q1.convertFromASCIIToDenisCode(key, alphabetFile);
        TreeMap<String, Double> mostCommonWords = CommonWordAnalysis.process10000file(-1);

//        HashSet<ArrayList> result = new HashSet<>();
        ArrayList<DecodedString> resultTS = new ArrayList<>();

        for (int i = 0; i < keyI.size() - cipherTextI.size(); i++) {
            ArrayList<Integer> plainTextI = new ArrayList<>();
            for (int j = i; j < cipherTextI.size() + i; j++) {
                Integer cipherChar = cipherTextI.get(j - i);
                Integer keyChar = keyI.get(j);
                Integer plainCharI = ((cipherChar - keyChar + alphabetFile.size()) % alphabetFile.size());
                plainTextI.add(plainCharI);
            }
            String decodedString = Q1.decodeString(plainTextI, alphabetFile);
            int positionOfWrong = decodedString.indexOf("27NOLOGY");
            resultTS.add(new DecodedString(decodedString, Q1.scoring(decodedString, mostCommonWords), i));
//            result.add(plainText);
        }
        resultTS.sort((o1, o2) -> {
            if (o1.getScore() > o2.getScore()) return 1;
            if (o1.getScore() < o2.getScore()) return -1;
            return 0;
        });
        System.out.println();
    }
}
