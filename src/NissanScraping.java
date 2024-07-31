import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NissanScraping {
    public static void main(String[] args) {
        WebDriver driver = null;
        CSVWriter writer;

        try {
            // Set the path to the WebDriver executable
            String madhu = "C:/Users/madhu/Downloads/chromedriver-win64-updated/chromedriver-win64/chromedriver.exe";
            String vicky = "/Users/vigneshnatarajan/myData/ACC/chromedriver-mac-arm64/chromedriver";

            System.setProperty("webdriver.chrome.driver", vicky);

            // Initialize WebDriver
            ChromeOptions options = new ChromeOptions();
            // options.addArguments("--headless"); // Run in headless mode (no GUI)
            driver = new ChromeDriver(options);

            String url = "https://www.nissan.ca/shopping-tools/build-price"; // Replace with the actual URL
            driver.get(url);
            driver.findElement(By.id("setLocationModal_setLocation_predictInput_button")).sendKeys("N9C 1J4");
            String brand = "Nissan";
            String year = "2024";

            // Hardcoded categories for each model in a single line
            Map<String, String> modelCategories = new HashMap<>() {{
                put("Versa", "Cars");
                put("KICKS", "Crossovers / SUVs");
                put("Sentra", "Cars");
                put("Altima", "Cars");
                put("Rogue", "Crossovers / SUVs");
                put("LEAF", "Electric Vehicles");
                put("Murano", "Crossovers / SUVs");
                put("Pathfinder", "Crossovers / SUVs");
                put("Frontier", "Crossovers / SUVs");
                put("Z", "Sports Cars");
                put("ARIYA", "Crossovers / SUVs");
                put("Armada", "Crossovers / SUVs");
                put("GT-R", "Sports Cars");
            }};

            // Wait for elements to load (you may need to adjust this or use WebDriverWait)
            Thread.sleep(3000); // 3 seconds delay

            // Lists to store car details and prices
            List<String[]> carData = new ArrayList<>();

            // Add header to CSV
            carData.add(new String[]{"Brand", "Model", "Year", "Price", "Category"});

            // Locate car details and prices
            List<WebElement> modelElements = driver.findElements(By.cssSelector("[data-testid='ModelTile_modelName']"));
            List<WebElement> priceElements = driver.findElements(By.cssSelector("[data-testid='ModelTile_price']"));

            for (int i = 0; i < modelElements.size(); i++) {
                try {
                    // Fetch car model name and price
                    String model = modelElements.get(i).getText();
                    String priceText = priceElements.get(i).getText();

                    // Remove non-numeric characters from the price, ignoring the last character
                    String priceWithoutSymbols = priceText.replace("$", "").replace(",", "").trim();
                    if (priceWithoutSymbols.length() > 1) {
                        priceWithoutSymbols = priceWithoutSymbols.substring(0, priceWithoutSymbols.length() - 1);
                    }
                    int price = Integer.parseInt(priceWithoutSymbols);

                    // Get the category for the current model
                    String category = modelCategories.getOrDefault(model, "Unknown");

                    // Add data to the list in the required order
                    carData.add(new String[]{brand, model, year, Integer.toString(price), category});
                } catch (Exception e) {
                    System.err.println("Failed to retrieve or process details for car at index " + i + ": " + e.getMessage());
                }
            }

            // Write data to CSV
            try (FileWriter fileWriter = new FileWriter("scraped_nissan.csv")) {
                writer = new CSVWriter(fileWriter);
                writer.writeAll(carData);
                System.out.println("Scraped Nissan details successfully");
            } catch (IOException e) {
                System.err.println("Failed to write CSV file: " + e.getMessage());
            }

        } catch (Exception exc) {
            System.err.println("An error occurred: " + exc.getMessage());
        } finally {
            if (driver != null) {
                // Close the browser
                driver.quit();
            }
        }
    }
}