import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FrequencyCount {

    public static void getFrequencyCount() {  // Initializing a list with dataset file names
        List<String> sourceFiles = Arrays.asList(
                "scraped_toyota.csv",
                "scraped_ford.csv",
                "scraped_chevrolet.csv",
                "scraped_hyundai.csv");

        //Initializing a variable to display the most frequently used words from the dataset
        int topNWords = 5;

        //Declaring wordCountMap as a map interface to hold the words and their frequency
        Map<String, Integer> wordCountMap = new HashMap<>();

        // Process each file to get the word count
        for (String sourceFile : sourceFiles) {
            processFile(sourceFile, wordCountMap);
        }

        // Get the entry set from the wordCountMap which contains all key-value pairs from the map
        List<Map.Entry<String, Integer>> wordCountList = new ArrayList<>(wordCountMap.entrySet());

        // Sort using QuickSort
        quickSort(wordCountList, 0, wordCountList.size() - 1);

        //Helper method to check if the words from dataset are present
        //printAll(wordCountList);

        // Display the top N most frequent words
        int SNo=1;
        for (int i = 0; i < Math.min(topNWords, wordCountList.size()); i++) {
            Map.Entry<String, Integer> entry = wordCountList.get(i);
            //System.out.println(SNo + ") " + entry.getKey() + " : " + entry.getValue());
            System.out.print("| " + entry.getKey() + " | ");
            SNo++;
        }
        System.out.println();
    }
    // Method to process each file and update the word count map
    private static void processFile(String sourceFile, Map<String, Integer> wordCountMap) {
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split line into words using regex to match alphabetic characters and apostrophes
                String[] words = line.split("[^a-zA-Z']+");

                for (String word : words) {
                    if (!word.isEmpty() && word.matches("[a-zA-Z]+")) {
                        word = word.toLowerCase(); // Normalize to lower case if needed
                        wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    // QuickSort implementation
    private static void quickSort(List<Map.Entry<String, Integer>> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }
    private static int partition(List<Map.Entry<String, Integer>> list, int low, int high) {
        //Initializing the pivot value
        int pivot = list.get(high).getValue();
        //To track the small element index
        int i = (low - 1);
        //Iterating from low->high-1
        for (int j = low; j < high; j++) {
            if (list.get(j).getValue() > pivot) { //Sort in descending order
                i++;
                Collections.swap(list, i, j);  //Swapping the element at indices
            }
        }
        //Swapping the pivot element
        Collections.swap(list, i + 1, high);
        return i + 1;
    }
    //Helper method to print all the words
    @SuppressWarnings("unused")
    private static void printAll(List<Map.Entry<String, Integer>> wordCountList) {
        // Listing all the words
        int SNo = 1;
        for (Map.Entry<String, Integer> entry : wordCountList) {
            System.out.println(SNo + ") " + entry.getKey() + " : " + entry.getValue());
            SNo++;
        }
        System.out.println("**********************************");
    }
}