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

        String vicky = "/Users/vigneshnatarajan/myData/ACC/chromedriver-mac-arm64/chromedriver"; // file location
        String madhu = "C:/Users/madhu/Downloads/chromedriver-win64-updated/chromedriver-win64/chromedriver.exe";

        System.setProperty("webdriver.chrome.varForDriver", vicky);
        ChromeOptions varForOptions = new ChromeOptions();
        WebDriver varForDriver = new ChromeDriver(varForOptions);
        WebDriverWait varForWait = new WebDriverWait(varForDriver, Duration.ofSeconds(10));

        try (CSVWriter varForCSV = new CSVWriter(new FileWriter("scraped_hyundai.csv"))) { // try catch
            // Writing the varForHeader to CSV
            String[] varForHeader = {"Brand", "Model", "Year", "Price", "Category"};
            varForCSV.writeNext(varForHeader);

            varForDriver.get("https://www.hyundaicanada.com/en");
            varForDriver.manage().window().maximize();

            try {
                WebElement varForCookieButton = varForDriver.findElement(By.xpath("//button[contains(text(), 'Accept')]"));
                if (varForCookieButton.isDisplayed()) {
                    varForCookieButton.click();
                }
                WebElement varForSubmitButton = varForDriver.findElement(By.xpath("/html/body/div[1]/varForHeader/section[1]/div[2]/form/div[3]/button"));
                if (varForSubmitButton.isDisplayed()) {
                    varForSubmitButton.click();
                }
            } catch (Exception e) {
                System.out.println("Cookie consent button not found or already accepted.");
            }

            WebElement varForBnp = varForDriver.findElement(By.xpath("/html/body/div[1]/varForHeader/div[2]/div/div[2]/nav/div[2]/div/div[2]/ul/li[2]/a/span[2]"));
            varForBnp.click();
            varForWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[6]/div/div[2]/div/div/section/div[3]/div/div/div[3]/div")));

            WebElement varForParent = varForDriver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div/section/div[3]/div/div/div[3]/div"));
            List<WebElement> varForModelCards = varForParent.findElements(By.xpath("./child::*"));

            for (WebElement varForModelCard : varForModelCards) { //webelement
                try {
                    String modelName = varForModelCard.findElement(By.cssSelector(".bp-card__vehicle-name")).getText().replaceAll("[^a-zA-Z0-9 ]", "");
                    String modelPriceStr = varForModelCard.findElement(By.cssSelector(".bp-card__base-price__value.bp-card__base-price__wrapper__value")).getText().replaceAll("[^0-9]", "");
                    int modelPrice = Integer.parseInt(modelPriceStr);
                    String category = varForDriver.findElement(By.cssSelector("#\\{E6A294E5-5FD1-4FF0-892F-069E9774BA9B\\} > span:nth-child(1)")).getText();
                    String yearString = varForDriver.findElement(By.id("selected-2024-Venue")).getText();
                    int year = Integer.parseInt(yearString);
                    String brand = "Hyundai";

                    varForCSV.writeNext(new String[]{
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
                WebElement compactSedanSection = varForDriver.findElement(By.id("{F6B3E6B3-9684-4B92-9864-561F505B10ED}")); //Sedan
                compactSedanSection.click();
                varForWait.until(ExpectedConditions.presenceOfElementLocated(By.id("{F6B3E6B3-9684-4B92-9864-561F505B10ED}")));

                List<WebElement> modelCards2 = varForParent.findElements(By.xpath("./child::*"));

                for (WebElement modelCard : modelCards2) {
                    try {
                        String modelName = modelCard.findElement(By.cssSelector(".bp-card__vehicle-name")).getText().replaceAll("[^a-zA-Z0-9 ]", "");
                        String modelPriceStr = modelCard.findElement(By.cssSelector(".bp-card__base-price__value.bp-card__base-price__wrapper__value")).getText().replaceAll("[^0-9]", "");
                        int modelPrice = Integer.parseInt(modelPriceStr);
                        String category = varForDriver.findElement(By.cssSelector("#\\{F6B3E6B3-9684-4B92-9864-561F505B10ED\\}")).getText();
                        String yearString = varForDriver.findElement(By.id("selected-2024-IONIQ-6")).getText();
                        int year = Integer.parseInt(yearString);
                        String varForBrand = "Hyundai";

                        varForCSV.writeNext(new String[]{
                                varForBrand,
                                modelName,
                                String.valueOf(year),
                                String.valueOf(modelPrice),
                                category
                        });

                        System.out.println("Brand: " + varForBrand);
                        System.out.println("Model Name: " + modelName);
                        System.out.println("Category: " + category);
                        System.out.println("Price: " + modelPrice);
                        System.out.println("Model Year: " + year);
                    } catch (Exception exce) {
                        System.out.println("Error processing model card: " + exce.getMessage());
                    }
                }
            } catch (Exception exce) {
                System.out.println("Error processing compact sedan section: " + exce.getMessage());
            }

            try {
                WebElement hybridSection = varForDriver.findElement(By.id("{2E463193-EE6E-42BF-98AD-8630E0C6D7C9}")); //HybridElectric
                hybridSection.click();
                varForWait.until(ExpectedConditions.presenceOfElementLocated(By.id("{2E463193-EE6E-42BF-98AD-8630E0C6D7C9}")));

                List<WebElement> varForModelCards3 = varForParent.findElements(By.xpath("./child::*"));

                for (WebElement varForModelCard : varForModelCards3) {
                    try {
                        String modelName = varForModelCard.findElement(By.cssSelector(".bp-card__vehicle-name")).getText().replaceAll("[^a-zA-Z0-9 ]", "");
                        String modelPriceStr = varForModelCard.findElement(By.cssSelector(".bp-card__base-price__value.bp-card__base-price__wrapper__value")).getText().replaceAll("[^0-9]", "");
                        int modelPrice = Integer.parseInt(modelPriceStr);
                        String category = varForDriver.findElement(By.id("{2E463193-EE6E-42BF-98AD-8630E0C6D7C9}")).getText();
                        String yearString = varForDriver.findElement(By.id("selected-2025-IONIQ-5-N")).getText();
                        int year = Integer.parseInt(yearString);
                        String brand = "Hyundai";

                        varForCSV.writeNext(new String[]{
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
                    } catch (Exception exc) {
                        System.out.println("Error processing model card: " + exc.getMessage());
                    }
                }
            } catch (Exception exc) {
                System.out.println("Error processing hybrid section: " + exc.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            varForDriver.quit();
        }
    }
}