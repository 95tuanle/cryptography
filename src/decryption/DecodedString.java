package decryption;

import java.util.Comparator;

class DecodedString implements Comparator<DecodedString> {
    private String decodedString;
    private double score;

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
        if (o1.score > o2.score) return 1;
        else if (o1.score < o2.score) return -1;
        else return 0;
    }

    @Override
    public String toString() {
        return "Score = " + score + "\n" +
                "Decoded String = '" + decodedString + '\'' + "\n";
    }
}
