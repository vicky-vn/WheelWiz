import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CarDetails {
    private static List<Map<String, Object>> carDataList = new ArrayList<>();
    private static Map<String, Map<String, Integer>> invertedIndex = new HashMap<>();
    private static Set<String> validModels = new HashSet<>();

    // Method to read multiple CSV files and merge their data into carDataList
    public static void readCSVsToMap() {
        carDataList.clear(); // Clear the list before reading new data
        validModels.clear(); // Clear the valid models set before reading new data

        // List of file paths to read
        List<String> filePaths = Arrays.asList(
                "scraped_mitsubishi.csv",
                "scraped_hyundai.csv",
                "scraped_nissan.csv",
                "scraped_chevrolet.csv");

        for (String filePath : filePaths) {
            try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
                String[] headers = csvReader.readNext(); // Read the header row
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    Map<String, Object> carData = new HashMap<>();
                    for (int i = 0; i < line.length; i++) {
                        if ("Price".equalsIgnoreCase(headers[i])) {
                            carData.put(headers[i], Integer.parseInt(line[i].replaceAll("[^0-9]", "")));
                        } else {
                            carData.put(headers[i], line[i]);
                        }
                    }
                    carDataList.add(carData);

                    // Add model to validModels set
                    String model = (String) carData.get("Model");
                    if (model != null && !model.isEmpty()) {
                        validModels.add(model.toLowerCase());
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading CSV file: " + e.getMessage());
            }
        }
    }

    // Method to set the inverted index
    public static void setInvertedIndex(Map<String, Map<String, Integer>> index) {
        invertedIndex = index;
    }

    // Method to get car details based on brand, category, and price
    public static void getDetails(String brand, String category, int price) {
        boolean found = false;

        for (Map<String, Object> carData : carDataList) {
            String carBrand = (String) carData.get("Brand");
            String carCategory = (String) carData.get("Category");
            Integer carPrice = (Integer) carData.get("Price"); // Use Integer instead of int

            if (carBrand != null && carBrand.equalsIgnoreCase(brand) &&
                    carCategory != null && carCategory.equalsIgnoreCase(category) &&
                    carPrice != null && carPrice < price) {

                String model = (String) carData.get("Model");
                // Normalize the model to handle spaces, hyphens, and case insensitivity
                String normalizedModel = model.split("[\\s-]")[0].toLowerCase();
                // Use the normalized model as the keyword for finding the top URL
                String topUrl = PageRankCalculator.getTopUrl(normalizedModel);

                System.out.println("------------------------------------------------------------------------------");
                System.out.println("Brand: " + carBrand);
                System.out.println("Model: " + model);
                System.out.println("Year: " + carData.get("Year"));
                System.out.println("Price: $" + carPrice);
                System.out.println("Category: " + carCategory);
                System.out.println("Visit: " + topUrl);
                System.out.println("------------------------------------------------------------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No data found");
        }
    }

    // Method to check if the model name is valid
    public static boolean isValidModel(String modelName) {
        return validModels.contains(modelName.toLowerCase());
    }
}
