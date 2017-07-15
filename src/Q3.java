import java.io.IOException;

public class Q3 {
    /*
    * gOI HAM SO UNG VOI CAU SO 1 VA CAU SO 2 RA
    * */
    public static void main(String[] args) throws IOException {
        System.out.println("Check if the meesage is encoded using transposition");
        Q2.crackTransposition("sourceFile/msg3.enc", 20);
        System.out.println("Check if the meesage is encoded using caesar");
        Q1.crackCaesar("sourceFile/msg3.enc", 20);
    }
}
