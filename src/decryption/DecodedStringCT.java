package decryption;

// CT stands for Caesar and Transposition
public class DecodedStringCT extends DecodedString {
    private int key;

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
