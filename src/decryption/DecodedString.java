package decryption;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

public class DecodedString implements Comparator<DecodedString> {
    private final String decodedString;
    private final double score;

    //  TODOx any other classes?

    DecodedString(String decodedString, double score) {
        this.decodedString = decodedString;
        //        this.key = key;
        this.score = score;
    }

    public String getDecodedString() {
        return decodedString;
    }

    public double getScore() {
        return score;
    }

    @Override
    public int compare(DecodedString o1, DecodedString o2) {
        return Double.compare(o1.score, o2.score);
    }

    @Override
    public String toString() {
        return "Score = " + score + "\n" +
                "Decoded String = '" + decodedString + '\'' + "\n";
    }

    // CT stands for Caesar and Transposition
    public static class DecodedStringCT extends DecodedString {
        private final int key;

        DecodedStringCT(String decodedString, double score, int key) {
            super(decodedString, score);
            this.key = key;

        }

        public int getKey() {
            return key;
        }

        @Override
        public String toString() {
            return "Key = " + key + "\n" + super.toString();
        }
    }

    public static class DecodedStringRS extends DecodedString {
        private final TreeMap<Character, Character> key;

        DecodedStringRS(String decodedString, TreeMap<Character, Character> key, double score) {
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

    // VN stands for Vernam
    public static class DecodedStringVN extends DecodedString {
        private final ArrayList<Character> key;

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
}
