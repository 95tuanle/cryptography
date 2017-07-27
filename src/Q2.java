import decryption.Transposition;

import java.io.IOException;


public class Q2 {
    public static void main(String[] args) throws IOException {
        Transposition.crack("sourceFile/msg2.enc", -1);
    }
}