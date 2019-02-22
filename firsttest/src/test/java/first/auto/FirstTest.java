package first.auto;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.JavascriptExecutor;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.File;
import org.openqa.selenium.OutputType;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import java.util.Calendar;

public class FirstTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    public boolean isElementPresent(final By locator)
    {
        try
        {
            driver.findElement(locator);
            return true;
        }catch (NoSuchElementException ex){
            return false;
        }
    }

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Chromedriver\\chromedriver.exe");
        ChromeOptions option = new ChromeOptions();
        option.addArguments("start-maximized");
        option.addArguments("incognito");

        driver = new ChromeDriver(option);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,10);

        String url_ua="https://my.uralairlines.ru/personal/auth/?rlink=personal/cabinet";

        driver.get(url_ua);


    }
    @Test
    public void userLogin() throws IOException {

//Скриншот!!!!
// File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
   //    FileUtils.copyFile(scrFile, new File("c:\\Screens\\scr.png"));
       // JavascriptExecutor js = (JavascriptExecutor) driver;
       // js.executeScript("console.('mmsystem.enableUtility(\"qa\")');");

              //ткнуть на кнопку предупреждения
        WebElement button = driver.findElement(By.xpath("//*[contains(@value, 'Я согласен')]"));
        button.click();

       // JavascriptExecutor js = (JavascriptExecutor) driver;
       // js.executeScript("var element = document.getElementsByClassName("auth-form_input")[0]; element.value = '1000843011';");

        List<WebElement> loginFields = driver.findElements(By.className("auth-form_input"));

        loginFields.get(0).sendKeys("1000894319");
        loginFields.get(1).sendKeys("Aa1234567890");

        button = driver.findElement(By.xpath("//*[contains(@class, 'lk-btn js_wings_auth_submit')]"));
        button.click();

        assertTrue(isElementPresent(By.className("num")));

        driver.get("https://www.uralairlines.ru/");

        //mx-banner __banner-four-two __active
       assertTrue(isElementPresent(By.xpath("//*[contains(@class, 'mx-banner __banner-four-two __active')]")));

        //*[contains(text(), 'Ваш текст')]
        assertTrue(isElementPresent(By.xpath("//*[contains(text(), 'Порекомендуйте нас')]")));
        assertTrue(isElementPresent(By.xpath("//*[contains(text(), 'Друзьям')]")));
   //     assertTrue(isElementPresent(By.xpath("//*[contains(text(), 'и близким')]")));
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

        Calendar c=Calendar.getInstance();
        String filename;
        filename = String.valueOf(c.getTime());
        filename = filename.replaceAll(":"," ")+".png";
        FileUtils.copyFile(scrFile, new File("c:\\aScreens\\"+filename));
/*

        WebElement otkuda = driver.findElement(By.name("origin-city-name"));
        WebElement kuda = driver.findElement(By.name("destination-city-name"));


        otkuda.clear();
        otkuda.sendKeys(" ");
        otkuda.sendKeys("Екатеринбург");

        kuda.clear();
        kuda.sendKeys(" ");
        kuda.sendKeys("Петропавловск-Камчатский");
        otkuda.click();

        driver.navigate().refresh();

        //String err_text=kuda.getAttribute("error");


        //Assert.assertEquals("Укажите город прибытия", err_text);
        */

    }
    @AfterClass
    public static void tearDown() {

        driver.quit();
    }
}