import decryption.Caesar;

import java.io.IOException;

public class Q1 {
    public static void main(String[] args) throws IOException {
        Caesar.crack("sourceFile/msg1.enc", -1);
    }
}
