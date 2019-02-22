package first.auto;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

import java.lang.reflect.Array;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;



public class AJAX {

    private static WebDriver driver;



    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Chromedriver\\chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //wait = new WebDriverWait(driver,10);



        //driver.get(url_ua);
        //driver.wait();

    }
    @Test
    public void userLogin() {

      // WebElement string = driver.findElement(By.xpath("//*[contains(@pre, 'error')]"));
        //banner.click();
        String[] Ap = {"LED","PEK","IKT"};


        for(int i=0; i<3; i++) {
            String url_ua = "https://www.uralairlines.ru/ajax/special_ajax.php?from=SVX&to=" + Ap[i];

            driver.get(url_ua);
            WebElement string = driver.findElement(By.xpath("//*[text()[contains(.,'{\"error\":\"Not found\",\"error_code\":\"1\"}')]]"));
            String st = string.getText();
            Assert.assertEquals("{\"error\":\"Not found\",\"error_code\":\"1\"}", st);
        }



    }
    @AfterClass
    public static void tearDown() {

        driver.quit();
    }

}
