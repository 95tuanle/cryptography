import java.io.IOException;
import java.util.ArrayList;


public class Q2 {
    /*
    * array1 = Tim tat ca cac soo chia het cho old_string.length
    * foreach i in array1:
    *   for k = 0; k < OLD_STRING.LENGTH/I; k++
    *       for j = 0; j < I; j++
    *           index = OLD_STRING.LENGTH/I*j+k
    *
    * */
    public static void main(String[] args) {
        try {
            crackTransposition("sourceFile/msg2.enc", -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Integer> findCommonDivisors(int size) {
        ArrayList<Integer> commonDivisors = new ArrayList<>();
        for (int i = 1; i < size / 2 + 1; i++) {
            if (size%i ==0) {
                commonDivisors.add(i);
            }
        }
        return commonDivisors;
    }

    static void crackTransposition(String path, int key) throws IllegalArgumentException, IOException {
//        TODOx fix this one
        ArrayList<Character> old_string = Q1.readFile(path);
        ArrayList<Integer> commonDivisors = findCommonDivisors(old_string.size());
        if (key != -1 && !commonDivisors.contains(key)) {
            throw new IllegalArgumentException("Invalid key value!");
        }
        if (key == -1) {
            for (int i : commonDivisors) {
                actualCrack(i, old_string);
            }
        } else if (key > -1) {
            actualCrack(key, old_string);
        }
    }

    private static void actualCrack(int key, ArrayList<Character> old_string) {
        StringBuilder result = new StringBuilder();
        for (int k = 0; k < old_string.size() / key; k++) {
            for (int j = 0; j < key; j++) {
                int index = old_string.size() / key * j + k;
                result.append(old_string.get(index));
            }
        }
        System.out.println("Key    " + key);
        System.out.println("Decoded string:\n\n" + result + "\n");
    }
}