import decryption.Caesar;
import decryption.Transposition;

import java.io.IOException;

public class Q3 {
    /*
     * gOI HAM SO UNG VOI CAU SO 1 VA CAU SO 2 RA
     * */
    public static void main(String[] args) throws IOException {
        System.out.println("Check if the message is encoded using transposition");
        Transposition.crack("sourceFile/msg3.enc", 20);
        System.out.println("Check if the message is encoded using caesar");
        Caesar.crack("sourceFile/msg3.enc", 20);
    }
}