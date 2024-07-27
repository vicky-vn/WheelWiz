import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BestDeal {

    private static final List<String> FILE_PATHS = Arrays.asList(
            "scraped_mitsubishi.csv",
            "scraped_hyundai.csv",
            "scraped_toyota.csv",
            "scraped_chevrolet.csv");

    // Method to find the cheapest car in a CSV file
    private static Map<String, Object> findCheapestCar(String filePath) {
        Map<String, Object> cheapestCar = null;

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
                if (cheapestCar == null || (Integer) carData.get("Price") < (Integer) cheapestCar.get("Price")) {
                    cheapestCar = carData;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return cheapestCar;
    }

    // Method to get the top URL for a given car model
    private static String getTopUrlForCar(Map<String, Object> carData) {
        String model = (String) carData.get("Model");
        if (model != null) {
            // Normalize the model to handle spaces, hyphens, and case insensitivity
            String normalizedModel = model.split("[\\s-]")[0].toLowerCase();
            return PageRankCalculator.getTopUrl(normalizedModel);
        }
        return null;
    }

    // Method to find and print the best deal across all files
    public static void findAndPrintBestDeals() {
        FILE_PATHS.stream()
                .map(BestDeal::findCheapestCar)
                .forEach(cheapestCar -> {
                    if (cheapestCar != null) {
                        String topUrl = getTopUrlForCar(cheapestCar);
                        System.out.println("----------------------------------------------");
                        System.out.println("Brand: " + cheapestCar.get("Brand"));
                        System.out.println("Model: " + cheapestCar.get("Model"));
                        System.out.println("Year: " + cheapestCar.get("Year"));
                        System.out.println("Price: $" + cheapestCar.get("Price"));
                        System.out.println("Category: " + cheapestCar.get("Category"));
                        System.out.println("URL: " + (topUrl != null ? topUrl : "No URL found"));
                        System.out.println("----------------------------------------------");
                    } else {
                        System.out.println("No cars found in the current file");
                    }
                });
    }

    public static void main(String[] args) {
        findAndPrintBestDeals();
    }
}
