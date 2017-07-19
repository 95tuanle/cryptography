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
        TreeMap<String, Double> mostCommonWords = process10000file(-1);
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

    static TreeMap<String, Double> process10000file(int numberOfWords) {
        if (numberOfWords < -1 || numberOfWords > 10000)
            throw new IllegalArgumentException("Number of words must be >= -1 and <= 70000!");
//        String line;
        TreeMap<String, Double> mostCommonWords = new TreeMap<>();

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader("sourceFile/english_words.txt");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
//            int pos = 0;
            double total_count = 0;
            int numberOrReadLines = 0;
            total_count = getTotalCount(mostCommonWords, bufferedReader, total_count, numberOrReadLines);
            for (Map.Entry<String, Double> entry : mostCommonWords.entrySet()) {
                entry.setValue(Math.log(entry.getValue() / total_count));
            }
//            TODO support case where number of words is specified
//            if (numberOfWords != -1) {
//                Set<Map.Entry<String, Double>> entries = mostCommonWords.entrySet();
//
////                Arrays.sort(mostCommonWordsAsArray, new Comparator<Object>() {
////                    @Override
////                    public int compare(Object o1, Object o2) {
////                        if(((Map.Entry<String, Double>) o1).getValue() > ((Map.Entry<String, Double>) o2).getValue()) return 1;
////                        else if(((Map.Entry<String, Double>) o1).getValue() < ((Map.Entry<String, Double>) o2).getValue()) return -1;
////                        return 0;
//////                        if(o1 instanceof Map.Entry && o2 instanceof Map.Entry){
//////                            if(((Map.Entry) o1).getKey() instanceof String && ((Map.Entry) o2).getKey()
//////                                    instanceof String && ((Map.Entry) o1).getValue() instanceof Double &&
//////                                    ((Map.Entry) o2).getValue() instanceof Double){
//////
//////                            }
//////                        }
////                    }
////                });
//            }

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
            total_count = getTotalCount(mostCommonTriagrams, bufferedReader, total_count, numberOrReadLines);
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

    private static double getTotalCount(TreeMap<String, Double> mostCommonTriagrams, BufferedReader bufferedReader, double total_count, int numberOrReadLines) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] lineComponents = line.split(" ");
            Double count = Double.valueOf(lineComponents[1]);
            mostCommonTriagrams.put(lineComponents[0], count);
            total_count += count;
            numberOrReadLines++;
        }
        return total_count;
    }
}
