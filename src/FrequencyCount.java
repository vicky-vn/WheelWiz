import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FrequencyCount {

    public static void getFrequencyCount() {
        List<String> sourceFiles = Arrays.asList(
                "scraped_nissan.csv",
                "scraped_mitsubishi.csv",
                "scraped_chevrolet.csv",
                "scraped_hyundai.csv");

        int topNWords = 5;

        Map<String, Integer> wordCountMap = new HashMap<>();

        for (String sourceFile : sourceFiles) {
            processFile(sourceFile, wordCountMap);
        }

        List<Map.Entry<String, Integer>> wordCountList = new ArrayList<>(wordCountMap.entrySet());

        quickSort(wordCountList, 0, wordCountList.size() - 1);

        int SNo = 1;
        for (int i = 0; i < Math.min(topNWords, wordCountList.size()); i++) {
            Map.Entry<String, Integer> entry = wordCountList.get(i);
            System.out.print("| " + entry.getKey() + " " +entry.getValue() +" | ");
            SNo++;
        }
        System.out.println();
    }

    private static void processFile(String sourceFile, Map<String, Integer> wordCountMap) {
        try (BufferedReader br = new BufferedReader(new FileReader(sourceFile))) {
            String line = br.readLine(); // Read the header line
            if (line != null) {
                String[] headers = parseCSVLine(line);
                int categoryIndex = -1;

                // Find the index of the 'category' column
                for (int i = 0; i < headers.length; i++) {
                    if (headers[i].equalsIgnoreCase("category")) {
                        categoryIndex = i;
                        break;
                    }
                }

                if (categoryIndex == -1) {
                    System.err.println("No 'category' column found in " + sourceFile);
                    return;
                }

                // Process the remaining lines
                while ((line = br.readLine()) != null) {
                    String[] columns = parseCSVLine(line);
                    if (columns.length > categoryIndex) {
                        String word = columns[categoryIndex].trim().toLowerCase();
                        if (!word.isEmpty()) {
                            wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        values.add(current.toString()); // add last value
        return values.toArray(new String[0]);
    }

    private static void quickSort(List<Map.Entry<String, Integer>> list, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, high);
        }
    }

    private static int partition(List<Map.Entry<String, Integer>> list, int low, int high) {
        int pivot = list.get(high).getValue();
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (list.get(j).getValue() > pivot) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    public static void printAll(List<Map.Entry<String, Integer>> wordCountList) {
        int SNo = 1;
        for (Map.Entry<String, Integer> entry : wordCountList) {
            System.out.println(SNo + ") " + entry.getKey() + " : " + entry.getValue());
            SNo++;
        }
        System.out.println("**********************************");
    }

    public static void main(String[] args) {
        getFrequencyCount();
    }
}
