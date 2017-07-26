package decryption;

import java.util.TreeMap;

public class DecodedStringRS extends DecodedString {
    private TreeMap<Character, Character> key;

    public DecodedStringRS(String decodedString, TreeMap<Character, Character> key, double score) {
        super(decodedString, score);
        this.key = key;
    }

    public TreeMap<Character, Character> getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Key = " + key + "\n" + super.toString();
    }
}
