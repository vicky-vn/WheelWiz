import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CarDetails {
    private static List<Map<String, Object>> carDataList = new ArrayList<>();

    // Method to read multiple CSV files and merge their data into carDataList
    public static void readCSVsToMap(List<String> filePaths) {
        carDataList.clear(); // Clear the list before reading new data

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
                }
            } catch (IOException e) {
                System.err.println("Error reading CSV file: " + e.getMessage());
            }
        }
    }

    // Method to get car details based on brand, category, and price
    public static void getDetails(String brand, String category, int price) {
        for (Map<String, Object> carData : carDataList) {
            String carBrand = (String) carData.get("Brand");
            String carCategory = (String) carData.get("Category");
            int carPrice = (int) carData.get("Price");

            if (carBrand.equalsIgnoreCase(brand) && carCategory.equalsIgnoreCase(category) && carPrice < price) {
                System.out.println("Brand: " + carBrand);
                System.out.println("Model: " + carData.get("Model"));
                System.out.println("Year: " + carData.get("Year"));
                System.out.println("Price: $" + carPrice);
                System.out.println("Category: " + carCategory);
                System.out.println("------------------------------------");
            }
        }
    }

    public static void main(String[] args) {
        // List of CSV file paths
        List<String> filePaths = Arrays.asList("scraped_ford1.csv", "scraped_ford2.csv");

        readCSVsToMap(filePaths);

        // Example usage of getDetails method
        getDetails("ford", "Sedan", 60000);
    }
}
