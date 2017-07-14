import java.io.*;
import java.util.ArrayList;

/**
 * Created by s3574983 on 13/07/2017.
 */
public class Q1 {
    /*
    * old_string = convert(nsg1..enc)
    *
*     For j = 0 to j < old_string.length
*       new_string[old_string];
*       For i = 0 to i < 50
    *       new_string[j] = (old_string[j]+i) % 50
 *       decrypt(new_string)
 *       print(new_string)
    * */
    static void answer() throws IOException {
        ArrayList old_string = readFile("sourceFile/msg1.enc");
        for (int i = 0; i < old_string.size(); i++) {
            ArrayList<Character> newString = new ArrayList();
            int stringSize = stringSize();
            for (int j = 0; j < stringSize; j++) {
                Character character = (Character) old_string.get(i);
                char newCharacter = (char) (((char) character + j)%50);
                newString.add(newCharacter);
            }
            printArrayList(newString);
            System.out.println();
        }
    }
    static ArrayList<Character> readFile(String fileName) throws IOException {
        FileReader in = null;

        String temp = null;
        in = new FileReader(fileName);

//        temp = "";
        ArrayList<Character> result= new ArrayList();
        int integer;
        ;
        while ((integer = in.read()) != -1) {
            if((char) integer != '<' && (char) integer != '>'){
                result.add((char) integer);
            }
        }
        in.close();
        return result;
    }

    static int stringSize() throws IOException {
        return processAlphabetFile().size();

    }

    static void printArrayList(ArrayList<Character> arrayList){
        for (Character anArrayList : arrayList) {
            System.out.print(anArrayList);
        }
    }

    static  ArrayList<Character> processAlphabetFile() throws IOException {
        ArrayList<Character> in = readFile("sourceFile/alphabet.txt");
        in.remove(51);
        in.remove(39);
        in.set(39, '\n');
        return in;
    }

    static void convertFromASCIIToDenis(ArrayList<Character> original){
                
    }
}
