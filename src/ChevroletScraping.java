import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ChevroletScraping {
    public static void chevroletScraper() {
        // Set the path to the chromedriver executable
        System.setProperty("webdriver.chrome.driver", "/Users/vigneshnatarajan/myData/ACC/chromedriver-mac-arm64/chromedriver");

        // Set ChromeOptions to use Google Chrome for Testing
        ChromeOptions options = new ChromeOptions();

        // Initialize the ChromeDriver with options
        WebDriver driver = new ChromeDriver(options);

        FileWriter csvWriter = null;

        try {
            // Open the chev.ca website
            driver.get("https://www.chevrolet.ca");
            driver.manage().window().maximize();

            // Accept cookies
            driver.findElement(By.xpath("/html/body/div[10]/button[1]")).click();

            // Close popup
            driver.findElement(By.xpath("/html/body/div[10]/img")).click();

            driver.get("https://www.chevrolet.ca/content/chevrolet/na/ca/en/PortableNavigation/main/navigation-flyouts/vehicles.html");

            List<WebElement> chevCars1 = driver.findElements(By.cssSelector(".col-sm-12.col-md-6.col-md-gut-rt-def.col-xl-3.col-xl-gut-rt-def"));

            csvWriter = new FileWriter("scraped_chevrolet.csv");
            csvWriter.append("Brand,Model,Year,Price,Category\n");

            for (WebElement cars : chevCars1) {
                try {
                    String carName = cars.findElement(By.cssSelector(".gb-headline.gb-none-margin.gb-descriptive1")).getText();
                    String carPrice = cars.findElement(By.tagName("gb-regional-price")).getText().replaceAll("[^\\d]", "");
                    String year = cars.findElement(By.tagName("img")).getAttribute("alt").substring(0, 4);
                    String category = cars.findElement(By.xpath("/html/body/div[2]/gb-adv-grid/adv-col/div/div/gb-tab-nav/ul/li[1]/button")).getText();

                    System.out.println("Model = " + carName);
                    System.out.println("Price = " + carPrice);
                    System.out.println("Year = " + year);
                    System.out.println("Category = " + category);
                    System.out.println();

                    csvWriter.append("Chevrolet,");
                    csvWriter.append(carName).append(",");
                    csvWriter.append(year).append(",");
                    csvWriter.append(carPrice).append(",");
                    csvWriter.append(category).append("\n");

                    if (carName.equals("SUBURBAN")) {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Error while processing car: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error in web scraping: " + e.getMessage());
        } finally {
            if (csvWriter != null) {
                try {
                    csvWriter.flush();
                    csvWriter.close();
                } catch (IOException e) {
                    System.out.println("Error while closing CSV writer: " + e.getMessage());
                }
            }
            driver.quit();
        }
    }

    public static void main(String[] args) {
        chevroletScraper();
    }
}
