package decryption;

import java.util.ArrayList;

// VN stands for Vernam
public class DecodedStringVN extends DecodedString {
    private ArrayList<Character> key;

    DecodedStringVN(String decodedString, double score, ArrayList<Character> key) {
        super(decodedString, score);
        this.key = key;
    }

    public ArrayList<Character> getKey() {
        return key;
    }

    public String toString() {
        return "Key = " + key + "\n" + super.toString();
    }

}
