import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class Q5 {
    public static void main(String[] args) throws IOException {
        ArrayList<Character> cipherTextList = Q1.readFile("sourceFile/msg5.enc");
        ArrayList<Character> key = Q1.readFile("sourceFile/Answer for Q4.txt");
        TreeMap<String, Double> mostCommonWords = CommonWordAnalysis.process10000file(-1);

//        HashSet<ArrayList> result = new HashSet<>();
        ArrayList<DecodedString> resultTS = new ArrayList<>();

        for (int i = 0; i < key.size() - cipherTextList.size() - 1; i++) {
            ArrayList<Character> plainText = new ArrayList<>();
            for (int j = i; j < cipherTextList.size() + i; j++) {
                Character cipherChar = cipherTextList.get(j - i);
                Character keyChar = key.get(j);
                Character plainChar = (char) (cipherChar ^ keyChar);
                plainText.add(plainChar);
            }
            String decodedString = Q4.getStringRepresentation(plainText);
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
