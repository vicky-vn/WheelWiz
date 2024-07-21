import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.util.List;

public class ChevroletScraping {
    public static void chevroletScraper(){
        // Set the path to the chromedriver executable
        //System.setProperty("webdriver.chrome.driver", "/Users/vigneshnatarajan/myData/ACC/chromedriver-mac-arm64/chromedriver");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\madhu\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        // Set ChromeOptions to use Google Chrome for Testing
        ChromeOptions options = new ChromeOptions();
        //options.setBinary("/Applications/Google Chrome for Testing.app/Contents/MacOS/Google Chrome for Testing");

        // Initialize the ChromeDriver with options
        WebDriver driver = new ChromeDriver(options);

        // Open the chev.ca website

        driver.get("https://www.chevrolet.ca");

        // Maximize the window
        driver.manage().window().maximize();

        //cookies
        driver.findElement(By.xpath("/html/body/div[10]/button[1]")).click();

        //popup
        driver.findElement(By.xpath("/html/body/div[10]/img")).click();

        driver.get("https://www.chevrolet.ca/content/chevrolet/na/ca/en/PortableNavigation/main/navigation-flyouts/vehicles.html");


        List<WebElement> chevCars1 = driver.findElements(By.cssSelector(".col-sm-12.col-md-6.col-md-gut-rt-def.col-xl-3.col-xl-gut-rt-def"));

        for (WebElement cars : chevCars1){
            String carName = cars.findElement(By.cssSelector(".gb-headline.gb-none-margin.gb-descriptive1")).getText();
            String carPrice = cars.findElement(By.tagName("gb-regional-price")).getText();
            String year = cars.findElement(By.tagName("img")).getAttribute("alt");
            String category = cars.findElement(By.xpath("/html/body/div[2]/gb-adv-grid/adv-col/div/div/gb-tab-nav/ul/li[1]/button")).getText();

            System.out.println("Model = " + carName.charAt(0));
            System.out.println("Price = " + carPrice);
            System.out.println("Year = " + year.substring(0,4));
            System.out.println("Category = " + category);
            System.out.println();

            if(carName.equals("SUBURBAN")){
                break;
            }
        }
    }

    public static void main(String[] args) {
        chevroletScraper();
    }
}
