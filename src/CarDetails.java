import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CarDetails {
    private static List<Map<String, Object>> carDataList = new ArrayList<>();

    public static List<Map<String, Object>> readCSVToMap(String filePath) {
        carDataList.clear(); // Clear the list before reading new data

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

        return carDataList;
    }

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
        String filePath = "scraped_ford.csv";
        readCSVToMap(filePath);

        // Example usage of getDetails method
        getDetails("ford", "Sedan", 60000);
    }
}
