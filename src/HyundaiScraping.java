// Importing Packages
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

// Main Class File
public class HyundaiScraping {
    public static void main(String[] args) {

        // Setting up Web Driver
        String vicky = "/Users/vigneshnatarajan/myData/ACC/chromedriver-mac-arm64/chromedriver";
        String madhu ="";

        System.setProperty("webdriver.chrome.driver", vicky);
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter("scraped_hyundai.csv"))) {
            // Writing the header to CSV
            String[] header = {"Brand", "Model", "Year", "Price", "Category"};
            csvWriter.writeNext(header);

            driver.get("https://www.hyundaicanada.com/en");
            driver.manage().window().maximize();

            try {
                WebElement cookieButton = driver.findElement(By.xpath("//button[contains(text(), 'Accept')]"));
                if (cookieButton.isDisplayed()) {
                    cookieButton.click();
                }
                WebElement submitButton = driver.findElement(By.xpath("/html/body/div[1]/header/section[1]/div[2]/form/div[3]/button"));
                if (submitButton.isDisplayed()) {
                    submitButton.click();
                }
            } catch (Exception e) {
                System.out.println("Cookie consent button not found or already accepted.");
            }

            WebElement bnp = driver.findElement(By.xpath("/html/body/div[1]/header/div[2]/div/div[2]/nav/div[2]/div/div[2]/ul/li[2]/a/span[2]"));
            bnp.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div/div[2]/div/div/section/div[3]/div/div/div[3]/div")));

            WebElement parent = driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div/section/div[3]/div/div/div[3]/div"));
            List<WebElement> modelCards = parent.findElements(By.xpath("./child::*"));

            for (WebElement modelCard : modelCards) {
                try {
                    String modelName = modelCard.findElement(By.cssSelector(".bp-card__vehicle-name")).getText().replaceAll("[^a-zA-Z0-9 ]", "");
                    String modelPriceStr = modelCard.findElement(By.cssSelector(".bp-card__base-price__value.bp-card__base-price__wrapper__value")).getText().replaceAll("[^0-9]", "");
                    int modelPrice = Integer.parseInt(modelPriceStr);
                    String category = driver.findElement(By.cssSelector("#\\{E6A294E5-5FD1-4FF0-892F-069E9774BA9B\\} > span:nth-child(1)")).getText();
                    String yearString = driver.findElement(By.id("selected-2024-Venue")).getText();
                    int year = Integer.parseInt(yearString);
                    String brand = "Hyundai";

                    csvWriter.writeNext(new String[]{
                            brand,
                            modelName,
                            String.valueOf(year),
                            String.valueOf(modelPrice),
                            category
                    });

                    System.out.println("Brand: " + brand);
                    System.out.println("Model Name: " + modelName);
                    System.out.println("Category: " + category);
                    System.out.println("Price: " + modelPrice);
                    System.out.println("Model Year: " + year);
                } catch (Exception e) {
                    System.out.println("Error processing model card: " + e.getMessage());
                }
            }

            try {
                WebElement compactSedanSection = driver.findElement(By.id("{F6B3E6B3-9684-4B92-9864-561F505B10ED}")); //Sedan
                compactSedanSection.click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("{F6B3E6B3-9684-4B92-9864-561F505B10ED}")));

                List<WebElement> modelCards2 = parent.findElements(By.xpath("./child::*"));

                for (WebElement modelCard : modelCards2) {
                    try {
                        String modelName = modelCard.findElement(By.cssSelector(".bp-card__vehicle-name")).getText().replaceAll("[^a-zA-Z0-9 ]", "");
                        String modelPriceStr = modelCard.findElement(By.cssSelector(".bp-card__base-price__value.bp-card__base-price__wrapper__value")).getText().replaceAll("[^0-9]", "");
                        int modelPrice = Integer.parseInt(modelPriceStr);
                        String category = driver.findElement(By.cssSelector("#\\{F6B3E6B3-9684-4B92-9864-561F505B10ED\\}")).getText();
                        String yearString = driver.findElement(By.id("selected-2024-IONIQ-6")).getText();
                        int year = Integer.parseInt(yearString);
                        String brand = "Hyundai";

                        csvWriter.writeNext(new String[]{
                                brand,
                                modelName,
                                String.valueOf(year),
                                String.valueOf(modelPrice),
                                category
                        });

                        System.out.println("Brand: " + brand);
                        System.out.println("Model Name: " + modelName);
                        System.out.println("Category: " + category);
                        System.out.println("Price: " + modelPrice);
                        System.out.println("Model Year: " + year);
                    } catch (Exception e) {
                        System.out.println("Error processing model card: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error processing compact sedan section: " + e.getMessage());
            }

            try {
                WebElement hybridSection = driver.findElement(By.id("{2E463193-EE6E-42BF-98AD-8630E0C6D7C9}")); //HybridElectric
                hybridSection.click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("{2E463193-EE6E-42BF-98AD-8630E0C6D7C9}")));

                List<WebElement> modelCards3 = parent.findElements(By.xpath("./child::*"));

                for (WebElement modelCard : modelCards3) {
                    try {
                        String modelName = modelCard.findElement(By.cssSelector(".bp-card__vehicle-name")).getText().replaceAll("[^a-zA-Z0-9 ]", "");
                        String modelPriceStr = modelCard.findElement(By.cssSelector(".bp-card__base-price__value.bp-card__base-price__wrapper__value")).getText().replaceAll("[^0-9]", "");
                        int modelPrice = Integer.parseInt(modelPriceStr);
                        String category = driver.findElement(By.id("{2E463193-EE6E-42BF-98AD-8630E0C6D7C9}")).getText();
                        String yearString = driver.findElement(By.id("selected-2025-IONIQ-5-N")).getText();
                        int year = Integer.parseInt(yearString);
                        String brand = "Hyundai";

                        csvWriter.writeNext(new String[]{
                                brand,
                                modelName,
                                String.valueOf(year),
                                String.valueOf(modelPrice),
                                category
                        });

                        System.out.println("Brand: " + brand);
                        System.out.println("Model Name: " + modelName);
                        System.out.println("Category: " + category);
                        System.out.println("Price: " + modelPrice);
                        System.out.println("Model Year: " + year);
                    } catch (Exception e) {
                        System.out.println("Error processing model card: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println("Error processing hybrid section: " + e.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
