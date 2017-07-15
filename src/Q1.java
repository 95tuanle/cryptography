import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Q1 {
    static ArrayList<Character> readFile(String fileName) throws IOException {
        FileReader in;
        in = new FileReader(fileName);
        ArrayList<Character> readingResult = new ArrayList<>();
        int characterAsInt;
        while ((characterAsInt = in.read()) != -1) {
            char character = (char) characterAsInt;
            if (character != '<' && character != '>' && character != '\r') {
                readingResult.add(character);
            }
        }
        in.close();
        return readingResult;
    }
}