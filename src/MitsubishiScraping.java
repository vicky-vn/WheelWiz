import com.opencsv.CSVWriter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MitsubishiScraping {
    public static void main(String[] args) {
        WebDriver driver = null;
        CSVWriter writer;

        try {
            // Set the path to the WebDriver executable
            String vicky = "/Users/vigneshnatarajan/myData/ACC/chromedriver-mac-arm64/chromedriver";
            System.setProperty("webdriver.chrome.driver", vicky);

            // Initialize WebDriver
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // Run in headless mode (no GUI)
            driver = new ChromeDriver(options);

            String url = "https://www.mitsubishi-motors.ca/en/buy/configure-your-mitsubishi";
            driver.get(url);
            String brand = "Mitsubishi";

            // Wait for elements to load (you may need to adjust this or use WebDriverWait)
            Thread.sleep(3000); // 3 seconds delay

            // Lists to store car details and prices
            List<String[]> carData = new ArrayList<>();

            // Add header to CSV
            carData.add(new String[]{"Brand", "Model", "Year", "Price", "Category"});

            // Locate car details and prices
            for (int i = 1; i <= 5; i++) {
                try {
                    String detailXpath = String.format("/html/body/div[2]/main/div[2]/div/div[3]/a[%d]/div[2]/p[1]", i);
                    String priceXpath = String.format("/html/body/div[2]/main/div[2]/div/div[3]/a[%d]/div[2]/p[2]", i);

                    // Fetch car details and prices
                    WebElement detailElement = driver.findElement(By.xpath(detailXpath));
                    WebElement priceElement = driver.findElement(By.xpath(priceXpath));

                    String detail = detailElement.getText();
                    String priceText = priceElement.getText();

                    // Extract year and name
                    String year = detail.substring(0, 4);
                    String name = detail.substring(5);

                    // Remove non-numeric characters from the price, ignoring the last character
                    String priceWithoutSymbols = priceText.replace("$", "").replace(",", "").trim();
                    if (priceWithoutSymbols.length() > 1) {
                        priceWithoutSymbols = priceWithoutSymbols.substring(0, priceWithoutSymbols.length() - 1);
                    }
                    int price = Integer.parseInt(priceWithoutSymbols);

                    // Determine category
                    String category = name.equalsIgnoreCase("Mirage") ? "Hatchback" : "SUV & Crossover";

                    // Add data to the list in the required order
                    carData.add(new String[]{brand, name, year, Integer.toString(price), category});
                } catch (Exception e) {
                    System.err.println("Failed to retrieve or process details for car at index " + i + ": " + e.getMessage());
                }
            }

            // Write data to CSV
            try (FileWriter fileWriter = new FileWriter("scraped_mitsubishi.csv")) {
                writer = new CSVWriter(fileWriter);
                writer.writeAll(carData);
                System.out.println("Scraped mitsubishi details successfully");
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