import java.io.IOException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        try {
            Q1.answer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Q2.answer();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String a= "";
//        a += "213213";
//        System.out.println(a);
//        ArrayList<Integer> commonDivisors = new ArrayList<>();
//        commonDivisors.add(0, 2);
//        commonDivisors.add(1, 3);
//        for (int i:commonDivisors) {
//            for (int k = 0; k < 30/i; k++) {
//                for (int j = 0; j < i; j++) {
//                    int index = 30/i*j + k;
//                    System.out.print(index + " ");
//                }
//            }
//            System.out.println();
//        }
    }
}
