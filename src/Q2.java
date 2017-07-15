import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;


public class Q2 {
    /*
    * array1 = Tim tat ca cac soo chia het cho old_string.length
    * foreach i in array1:
    *   for k = 0; k < OLD_STRING.LENGTH/I; k++
    *       for j = 0; j < I; j++
    *           index = OLD_STRING.LENGTH/I*j+k
    *
    * */

    static void answer() throws IOException {
        ArrayList<Character> old_string = Q1.readFile("sourceFile/msg2.enc");
        ArrayList<Integer> commonDivisors = findCommonDivisors(old_string.size());
//        TreeMap<String, Integer> mostCommonWords = Q1.process10000file();
//        DecodedString[] resultsAsArray = new DecodedString[commonDivisors.size()];
        for (int i : commonDivisors) {
            String result = "";
            for (int k = 0; k < old_string.size() / i; k++) {
                for (int j = 0; j < i; j++) {
                    int index = old_string.size() / i * j + k;
                    result += old_string.get(index);
                }
            }
            //print result from here
            System.out.println("Key    " + i);
            System.out.println("Decoded string:\n\n" + result + "\n");
        }
        // TODO implement scoring method
//            resultsAsArray[i] = new DecodedString(result, i, Q1.scoring(result, mostCommonWords));
//        }
//        Arrays.sort(resultsAsArray, resultsAsArray[0]);
//        System.out.println("TOP 3 RESULTS OF QUESTION 2\n");
//        for (int i = resultsAsArray.length - 1; i > resultsAsArray.length - 4; i--) {
//            System.out.println("Key    " + resultsAsArray[i].getKey());
//            System.out.println("Score  " + resultsAsArray[i].getScore());
//            System.out.println("Decoded string:\n\n" + resultsAsArray[i].getDecodedString() + "\n");
//        }
//        System.out.println();
    }

    private static ArrayList<Integer> findCommonDivisors(int size) {
        ArrayList<Integer> commonDivisors = new ArrayList<>();
        for (int i = 1; i < size; i++) {
            if (size%i ==0) {
                commonDivisors.add(i);
            }
        }
        return commonDivisors;
    }

    public static void crackTransposition(String path, int key) throws IllegalArgumentException {
        if (key == -1) {
            System.out.println("There is no key provided");
        } else if (key > -1) {
            System.out.println("There is at least a key provided");
        }
    }
}