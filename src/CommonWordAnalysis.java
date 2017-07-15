import java.io.*;
import java.util.Map;
import java.util.TreeMap;

class CommonWordAnalysis {
    private static boolean printFrequencyFile = false;

    static int determineTheSizeOfMostCommonWords(TreeMap<String, Integer> mostCommonWords) throws IOException {
        int[] frequency = frequencyAnalysis(mostCommonWords);
        return calculateCommonWordSize(mostCommonWords, frequency);
    }

    private static TreeMap<String, Integer> saveMostCommonWordToFIle() throws IOException {
        TreeMap<String, Integer> mostCommonWords = process10000file();
        FileOutputStream fos = new FileOutputStream("sourceFile/mostCommonWords.o");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(mostCommonWords);
        oos.close();
        return mostCommonWords;
    }

    private static int calculateCommonWordSize(TreeMap<String, Integer> mostCommonWords, int[] frequency) {
        int commonWordSize = 0;
        int totalNumberOfWords = frequency[commonWordSize];
        while ((double) totalNumberOfWords / mostCommonWords.size() < 0.99) {
            commonWordSize++;
            totalNumberOfWords += frequency[commonWordSize];
        }
        return commonWordSize;
    }

    private static int[] frequencyAnalysis(TreeMap<String, Integer> mostCommonWords) throws IOException {
//      TODOx can I not hard-code 20? can but what's the point?
        int[] frequency = new int[20];
        for (Map.Entry<String, Integer> entry : mostCommonWords.entrySet()) {
            int stringLength = entry.getKey().length();
            frequency[stringLength - 1]++;
        }
        if (printFrequencyFile) {
            printFrequencyToCSV(frequency);
        }
        return frequency;
    }

    private static void printFrequencyToCSV(int[] frequency) throws IOException {
        PrintWriter outputStream = null;

        try {
            outputStream = new PrintWriter(new FileWriter("Frequency analysis.csv"));
            outputStream.println("Length\tFrequency");
            for (int i = 0; i < frequency.length; i++) {
                outputStream.print(i);
                outputStream.println("\t" + frequency[i]);
            }

        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    static TreeMap<String, Integer> process10000file() {
        String line;
        TreeMap<String, Integer> mostCommonWords = new TreeMap<>();
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader("sourceFile/google-10000-english-no-swears.txt");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            int pos = 0;
            while ((line = bufferedReader.readLine()) != null) {
//                returnResult = returnResult.concat(line + "\n");
                mostCommonWords.put(line, pos);
            }

            // Always close files.
            bufferedReader.close();


            return mostCommonWords;
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            "sourceFile/google-10000-english-no-swears.txt" + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + "sourceFile/google-10000-english-no-swears.txt" + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
        return null;
    }
}
