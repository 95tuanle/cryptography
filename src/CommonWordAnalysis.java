import decryption.Decryption;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

class CommonWordAnalysis {
    private static boolean printFrequencyFile = false;

    static int determineTheSizeOfMostCommonWords(TreeMap<String, Integer> mostCommonWords) throws IOException {
        int[] frequency = frequencyAnalysis(mostCommonWords);
        return calculateCommonWordSize(mostCommonWords, frequency);
    }

    private static TreeMap<String, Double> saveMostCommonWordToFIle() throws IOException {
        TreeMap<String, Double> mostCommonWords = Decryption.findMostCommonWords(-1);
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
            outputStream = new PrintWriter(new FileWriter("Word length analysis.csv"));
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

    static TreeMap<String, Double> processTriagramFile(int numberOfWords) {
//        TODO support limit number of words
        if (numberOfWords < -1 || numberOfWords > 10000)
            throw new IllegalArgumentException("Number of words must be >= -1 and <= 70000!");
//        String line;
        TreeMap<String, Double> mostCommonTriagrams = new TreeMap<>();

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader("sourceFile/english_trigrams.txt");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
//            int pos = 0;
            double total_count = 0;
            int numberOrReadLines = 0;
            total_count = Decryption.getTotalCount(mostCommonTriagrams, bufferedReader, total_count, numberOfWords);
            for (Map.Entry<String, Double> entry : mostCommonTriagrams.entrySet()) {
                entry.setValue(Math.log(entry.getValue() / total_count));
            }
            bufferedReader.close();
            return mostCommonTriagrams;

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

    static TreeMap<String, Double> processDoubleFile() {
        return null;
    }

    static TreeMap<String, Double> processInitialLetterFile() {
        return null;
    }

    static TreeMap<String, Double> processOneLetterWordFile() {
        TreeMap<String, Double> oneLetterWord = new TreeMap<>();
        oneLetterWord.put("A", 0.0);
        oneLetterWord.put("I", 0.0);
        return oneLetterWord;
    }

    static TreeMap<String, Double> processTwoLetterWordFile() {
        return null;
    }

    static TreeMap<String, Double> processThreeLetterWordFile() {
        return null;
    }

    static TreeMap<String, Double> processFourLetterWordFile() {
        return null;
    }
}
