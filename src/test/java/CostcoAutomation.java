import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.util.Asserts;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CostcoAutomation {
    private static void testGoToCoffeeSweeteners (WebDriver driver){
        //performance meassure
        //StopWatch watch = new StopWatch();
        //watch.start();
        driver.get("https://www.costco.com/");
        //ARRANGE
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        //hover over grocery tab and click on coffee link
        Actions action = new Actions(driver);
        WebElement groceryTab = (new WebDriverWait(driver,10)).until(ExpectedConditions.presenceOfElementLocated(By.id("Home_Ancillary_0")));
        action.moveToElement(groceryTab).perform();
        WebElement coffeeLink = driver.findElement(By.xpath("//a[@href = '/coffee-sweeteners.html']"));
        //coffeeLink.sendKeys(Keys.ENTER);
        //ACT
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();", coffeeLink);
        //ASSERT
        String URL = driver.getCurrentUrl();
        Asserts.check(URL.equalsIgnoreCase("https://www.costco.com/coffee-sweeteners.html"), "You did not go to the correct coffee link");
        //watch.stop();
        //System.out.println(("Time Elapsed: " + watch.getTime()));
    }
    private static void checkIfSwitchedToRatingsHighLow (WebDriver driver){
        driver.get("https://www.costco.com/instant-coffee.html");
        //Arrange
        WebElement zipCode = driver.findElement(By.id("postal-code-input"));
        zipCode.sendKeys("98055");
        WebElement sendButton = driver.findElement(By.id("postal-code-submit"));
        sendButton.click();
        WebElement dropDown = (new WebDriverWait(driver,10)).until(ExpectedConditions.presenceOfElementLocated(By.id("sort_by")));
        //WebElement dropDown = driver.findElement(By.id("sort_by"));
        Select select = new Select(dropDown);
        //ACT - rating to highToLow
        select.selectByIndex(3);
        //ASSERT
        String URL = driver.getCurrentUrl();
        Asserts.check(URL.equalsIgnoreCase("https://www.costco.com/instant-coffee.html?sortBy=item_ratings+desc"), "You did not switch to HighToLow");
    }
    private static void AddToCart (WebDriver driver) throws InterruptedException {
        WebElement highestRatedCoffee = driver.findElement(By.id("addbutton-0"));
        highestRatedCoffee.click();
        Thread.sleep(10000);
        WebElement cart = driver.findElement(By.id("cart-d"));
        cart.click();
        WebElement cartInventory = (new WebDriverWait(driver,10)).until(ExpectedConditions.presenceOfElementLocated(By.id("quantity_1")));
        //WebElement cartInventory = driver.findElement(By.id("quantatity_1"));
        Asserts.check(cartInventory.getAttribute("value").equalsIgnoreCase("1"), "You didn't succesfully add the item");
    }
    private static void disableImageChrome(ChromeOptions options){
        HashMap<String, Object> images = new HashMap<String, Object>();
        images.put("images", 2);
        HashMap<String, Object> prefs = new HashMap<String,Object>();
        prefs.put("profile.default_content_setting_values", images);

        options.setExperimentalOption("prefs", prefs);
    }
    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "c:/Homework/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        disableImageChrome(options);
        WebDriver driver = new ChromeDriver(options);
        //testGoToCoffeeSweeteners(driver);
        checkIfSwitchedToRatingsHighLow(driver);
        AddToCart(driver);
        driver.quit();
    }
}

