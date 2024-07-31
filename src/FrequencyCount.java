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

        Map<String, Integer> varForWCMap = new HashMap<>();

        for (String sourceFile : sourceFiles) {
            processFile(sourceFile, varForWCMap);
        }

        List<Map.Entry<String, Integer>> varForWCList = new ArrayList<>(varForWCMap.entrySet());

        methodForQuickSort(varForWCList, 0, varForWCList.size() - 1);

        int varForSNo = 1;
        for (int i = 0; i < Math.min(topNWords, varForWCList.size()); i++) {
            Map.Entry<String, Integer> entry = varForWCList.get(i);
            System.out.print("| " + entry.getKey() + " => " +entry.getValue() +" | ");
            varForSNo++;
        }
        System.out.println();
    }

    private static void processFile(String sourceFile, Map<String, Integer> wordCountMap) {
        try (BufferedReader VNbr = new BufferedReader(new FileReader(sourceFile))) {
            String varForLine = VNbr.readLine(); // Read the header varForLine
            if (varForLine != null) {
                String[] headers = parseCSVLine(varForLine);
                int categoryIndex = -1;

                // Find the index of the 'category' column
                for (int varFori = 0; varFori < headers.length; varFori++) {
                    if (headers[varFori].equalsIgnoreCase("category")) {
                        categoryIndex = varFori;
                        break;
                    }
                }

                if (categoryIndex == -1) {
                    System.err.println("No 'category' column found in " + sourceFile);
                    return;
                }

                // Process remaining lines
                while ((varForLine = VNbr.readLine()) != null) {
                    String[] columns = parseCSVLine(varForLine);
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
        for (char varForc : line.toCharArray()) {
            if (varForc == '\"') {
                inQuotes = !inQuotes;
            } else if (varForc == ',' && !inQuotes) {
                values.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(varForc);
            }
        }
        values.add(current.toString()); // add last value
        return values.toArray(new String[0]);
    }

    //Q_u_i_c_k_S_o_r_t
    private static void methodForQuickSort(List<Map.Entry<String, Integer>> list, int paramForLow, int paramForHigh) {
        if (paramForLow < paramForHigh) {
            int pivotIndex = methodForPartition(list, paramForLow, paramForHigh);
            methodForQuickSort(list, paramForLow, pivotIndex - 1);
            methodForQuickSort(list, pivotIndex + 1, paramForHigh);
        }
    }
    //P_a_r_t_i_t_i_o_n
    private static int methodForPartition(List<Map.Entry<String, Integer>> list, int paramForLow, int paramForHigh) {
        int varForPivot = list.get(paramForHigh).getValue();
        int varForI = (paramForLow - 1);
        for (int varForJ = paramForLow; varForJ < paramForHigh; varForJ++) {
            if (list.get(varForJ).getValue() > varForPivot) {
                varForI++;
                Collections.swap(list, varForI, varForJ);
            }
        }
        Collections.swap(list, varForI + 1, paramForHigh);
        return varForI + 1;
    }

    public static void main(String[] args) {
        getFrequencyCount();
    }
}