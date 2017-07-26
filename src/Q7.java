import decryption.Caesar;
import decryption.ComboEncryption;
import decryption.Transposition;

import java.io.IOException;

public class Q7 {
    public static void main(String[] args) throws IOException {
        System.out.println("Check if the message is encoded using transposition");
        Transposition.crack("sourceFile/msg7.enc", -1);
        System.out.println("\n\n");

        System.out.println("Check if the message is encoded using caesar");
        Caesar.crack("sourceFile/msg7.enc", -1);
        System.out.println("\n\n");

//        TODO what does it mean different keys + same key
        System.out.println("Check if the message is encoded using transposition first, then caesar (different keys + same key)");
        ComboEncryption.caesarThenTranspose("sourceFile/msg7.enc");
        System.out.println("\n\n");

        System.out.println("Check if the message is encoded using caesar first, then transposition (different keys + same key)");
        ComboEncryption.transposeThenCaesar("sourceFile/msg7.enc");
    }

}