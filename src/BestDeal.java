import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BestDeal {

    private static final Logger varForLogger = Logger.getLogger(BestDeal.class.getName());
    private static final List<String> dataset_cars = Arrays.asList(
            "scraped_mitsubishi.csv",
            "scraped_hyundai.csv",
            "scraped_chevrolet.csv",
            "scraped_nissan.csv");

    // Method to find and print the best deal across all files
    public static void findAndPrintBestDeals() {
        for (String sourceFile : dataset_cars) {
            Map<String, Object> cheapestCar = null;

            try (CSVReader csvReader = new CSVReader(new FileReader(sourceFile))) {
                String[] headers = csvReader.readNext(); // Read the header row
                if (headers == null) {
                    varForLogger.warning("CSV file " + sourceFile + " is empty or has no header row.");
                    continue;
                }

                int priceIndex = -1;
                for (int i = 0; i < headers.length; i++) {
                    if ("Price".equalsIgnoreCase(headers[i])) {
                        priceIndex = i;
                        break;
                    }
                }

                if (priceIndex == -1) {
                    varForLogger.severe("CSV file " + sourceFile + " does not contain a 'Price' column.");
                    continue;
                }

                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    Map<String, Object> carData = new HashMap<>();
                    try {
                        for (int i = 0; i < line.length; i++) {
                            if (i == priceIndex) {
                                carData.put(headers[i], Integer.parseInt(line[i].replaceAll("[^0-9]", "")));
                            } else {
                                carData.put(headers[i], line[i]);
                            }
                        }
                    } catch (NumberFormatException e) {
                        varForLogger.log(Level.WARNING, "Invalid number format in file " + sourceFile + " for line: " + Arrays.toString(line), e);
                        continue; // Skip this line and continue processing
                    }

                    if (cheapestCar == null || (Integer) carData.get("Price") < (Integer) cheapestCar.get("Price")) {
                        cheapestCar = carData;
                    }
                }
            } catch (IOException e) {
                varForLogger.log(Level.SEVERE, "Error reading CSV file: " + sourceFile, e);
            }

            if (cheapestCar != null) {
                String model = (String) cheapestCar.get("Model");
                String topUrl = null;
                if (model != null) {
                    // Normalize the model to handle spaces, hyphens, and case insensitivity
                    String normalizedModel = model.split("[\\s-]")[0].toLowerCase();
                    topUrl = PageRankCalculator.getTopUrl(normalizedModel);
                }

                System.out.println("-------------------------------------------------------------------------------------------");
                System.out.println("Brand: " + cheapestCar.get("Brand"));
                System.out.println("Model: " + ((String)cheapestCar.get("Model")).toUpperCase());
                System.out.println("Year: " + cheapestCar.get("Year"));
                System.out.println("Price: $" + cheapestCar.get("Price"));
                System.out.println("Category: " + cheapestCar.get("Category"));
                System.out.println("URL: " + (topUrl != null ? topUrl : "No URL found"));
                System.out.println("-------------------------------------------------------------------------------------------");
            } else {
                System.out.println("No cars found in the file: " + sourceFile);
            }
        }
    }

    public static void main(String[] args) {
        findAndPrintBestDeals();
    }
}