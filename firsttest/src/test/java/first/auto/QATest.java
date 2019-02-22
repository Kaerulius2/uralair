package first.auto;

import jxl.write.WriteException;
import okhttp3.internal.http2.Settings;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import jxl.*;
import jxl.read.biff.BiffException;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
//import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.*;

public class QATest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    String source_path="c:/aData/test.xls";
    String source_cpy="c:/aData/test_cpy.xls";
    String screen_path="c:/aScreens/";

    public boolean isElementPresent(final By locator)
    {
        try
        {
            driver.findElement(locator);
            return true;
        }catch (NoSuchElementException ex){
            return false;
        }
        catch (NullPointerException ex2){
            return false;
        }
    }

    private void getScreenshot(String scn, String time) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filename;
        filename = scn +" " + time + " CHROME";
        filename = filename.replaceAll(":", " ") + ".png"; //////////////////
        FileUtils.copyFile(scrFile, new File(screen_path + filename));
    }

    @BeforeClass
    public static void setup() {

        System.setProperty("webdriver.chrome.driver", "C:\\Chromedriver\\chromedriver.exe");
        ChromeOptions option = new ChromeOptions();
        option.addArguments("start-maximized");
        option.addArguments("incognito");

        driver = new ChromeDriver(option);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,15);

        String url_ua="http://uralairlines.ru";
        driver.get(url_ua);
        //email отвменить
        WebElement button = driver.findElement(By.xpath("//*[contains(@class, 'layer-close js_email_registration_close')]"));
        button.click();
        //СОГЛАСЕН нажать
        button = driver.findElement(By.xpath("//*[contains(@value, 'Я согласен')]"));
        button.click();


    }

    @Test
    public void chromeTest() throws IOException, InterruptedException, WriteException {

        Calendar c = Calendar.getInstance();

        driver.get("https://www.uralairlines.ru/?mmcore.un=qa");
        Thread.sleep(3000);
        driver.switchTo().defaultContent();
        WebElement Qatool=wait.until(presenceOfElementLocated(By.xpath("//*[@id='dptIframe']")));
        driver.switchTo().frame(Qatool); //переключились во фрейм
        //Thread.sleep(1000);
        WebElement settings = wait.until(presenceOfElementLocated(By.id("maxypt-tab-bttn-settings")));

        settings.click();
        Qatool=driver.findElement(By.xpath("//*[contains(@data-label, 'Top - Right')]"));
        Qatool.click();
        Qatool=driver.findElement(By.xpath("//*[contains(@class, 'maxypt-save-settings maxypt-tooltip-right')]"));
        Qatool.click();

        dataSheet ds = new dataSheet();
        ds.readExcel(source_path,source_cpy);

        ReadExcel test = new ReadExcel();
        test.setInputFile(source_path);
        ArrayList<String> listAtrMem = new ArrayList<String>();

        for(int j=0;j<test.getStrokaCount();j++) //по количеству мемберов
        {
            listAtrMem.clear();
            for(int i=0;i<test.getStolbecCount();i++) //по количеству ячеек по горизонтали
            {
                listAtrMem.add(test.readCell(j,i)); //получим атрибуты очередного мембера
            }

            ds.setValueIntoCell("Лист1", 1, j, "PASS"); //имя листа, номер столбца, номер строки

            driver.get("https://www.uralairlines.ru/?mmid=" + listAtrMem.get(0)); //открываем страницу с очередным мембером

            //баннер есть?
            String isBanner = "//*[contains(@class, '" + listAtrMem.get(1) + "')]";
            assertTrue(isElementPresent(By.xpath(isBanner)));
            //промокод есть?
            if(listAtrMem.contains("promo"))
            {
                assertTrue(isElementPresent(By.xpath("//*[contains(@class, 'mx-banner-promo')]")));
            }
            //проверим текст баннера
            for(int k=2; k<listAtrMem.size();k++)
            {
                String isText = "//*[contains(text(), '" + listAtrMem.get(k) + "')]";
                try {
                    assertTrue(isElementPresent(By.xpath(isText)));
                }
                catch (AssertionError asER){
                    ds.setValueIntoCell("Лист1", 1, j, "FAIL - текст баннера!"); //имя листа, номер столбца, номер строки
                    getScreenshot(listAtrMem.get(0),String.valueOf(c.getTime()));
                    continue; //тут можно написать обработчик - флаг, чтобы в конце цикла ронять тест.

                    //throw asER;
                }
            }

            getScreenshot(listAtrMem.get(0),String.valueOf(c.getTime()));

        }//for по количеству мемберов
        ds.closeFile();
        driver.close();
    }



    /*
        @Test
        public void fireTest() throws IOException, InterruptedException {

            System.setProperty("webdriver.gecko.driver", "C:\\Geckodriver\\geckodriver.exe");
            driver = new FirefoxDriver();

            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            wait = new WebDriverWait(driver,15);

            String url_ua="http://uralairlines.ru";
            driver.get(url_ua);
            driver.manage().window().maximize();
            //email отвменить
            WebElement button = driver.findElement(By.xpath("//*[contains(@class, 'layer-close js_email_registration_close')]"));
            button.click();
            //СОГЛАСЕН нажать
            button = driver.findElement(By.xpath("//*[contains(@value, 'Я согласен')]"));
            button.click();

            driver.get("https://www.uralairlines.ru/?mmcore.un=qa");
            Thread.sleep(5000);
            driver.switchTo().defaultContent();
            WebElement Qatool=wait.until(presenceOfElementLocated(By.xpath("//*[@id='dptIframe']")));
            driver.switchTo().frame(Qatool); //переключились во фрейм Change settings
            //Thread.sleep(1000);
            WebElement settings = wait.until(presenceOfElementLocated(By.id("maxypt-tab-bttn-settings")));

            settings.click();
            Qatool=driver.findElement(By.xpath("//*[contains(@data-label, 'Top - Right')]"));
            Qatool.click();
            Qatool=driver.findElement(By.xpath("//*[contains(@class, 'maxypt-save-settings maxypt-tooltip-right')]"));
            Qatool.click();

            //driver.switchTo().defaultContent();

            //получить все атрибуты мембера
            ReadExcel test = new ReadExcel();
            test.setInputFile("c:/aData/test.xls");
            ArrayList<String> listAtrMem = new ArrayList<String>();

            for(int j=0;j<test.getStrokaCount();j++)
            {
                listAtrMem.clear();
                for(int i=0;i<test.getStolbecCount();i++)
                {
                    listAtrMem.add(test.readCell(j,i)); //получим атрибуты очередного мембера
                }
                driver.get("https://www.uralairlines.ru/?mmid=" + listAtrMem.get(0)); //открываем страницу с очередным мембером
                Thread.sleep(3000);
                //баннер есть?

                Qatool = wait.until(presenceOfElementLocated(By.xpath("//*[@name='_ym_native']")));
                driver.switchTo().frame(Qatool).switchTo().defaultContent(); //переключились во фрейм

                String isBanner = "//*[contains(@class, '" + listAtrMem.get(1) + "')]";

                Qatool=null;

                Qatool=wait.until(presenceOfElementLocated(By.xpath(isBanner)));
                Thread.sleep(1500);
                assertTrue(Qatool.isDisplayed());

                Qatool=null;
                //промокод есть?
                if(listAtrMem.contains("promo"))
                {
                    Qatool=wait.until(presenceOfElementLocated(By.xpath("//*[contains(@class, 'mx-banner-promo')]")));
                    assertTrue(Qatool.isDisplayed());
                    Qatool=null;
                }
                //проверим текст баннера
                for(int k=2; k<listAtrMem.size();k++)
                {
                    String isText = "//*[contains(text(), '" + listAtrMem.get(k) + "')]";
                    //Qatool=wait.until(presenceOfElementLocated(By.xpath(isText)));
                    //assertTrue(Qatool.isDisplayed()); //
                    //Qatool=null;
                    assertTrue(isElementPresent(By.xpath(isText)));
                }
                Thread.sleep(1000);
                //сохраняем скриншот
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Calendar c = Calendar.getInstance();
                String filename;
                filename = listAtrMem.get(0) +" " + String.valueOf(c.getTime())+ " FIREFOX";
                filename = filename.replaceAll(":", " ") + ".png";
                FileUtils.copyFile(scrFile, new File("c:\\aScreens\\" + filename));

            }

            driver.close();
        }

    */
/*
    @Test
    public void edgeTest() throws IOException, InterruptedException {
        System.setProperty("webdriver.edge.driver", "C:\\Edgedriver\\MicrosoftWebDriver.exe"); //C:\Edgedriver
        driver = new EdgeDriver();

        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,15);

        String url_ua="http://uralairlines.ru";
        driver.get(url_ua);
        driver.manage().window().maximize();

        WebElement button;
        //email отвменить

      //  if(isElementPresent(By.xpath("//*[contains(@class, 'layer-close js_email_registration_close')]"))) {
      //      button = driver.findElement(By.xpath("//*[contains(@class, 'layer-close js_email_registration_close')]"));
      //      button.click();
      //  }
      //  if(isElementPresent(By.xpath("//*[contains(@value, 'Я согласен')]"))) {
      //      //СОГЛАСЕН нажать
      //      button = driver.findElement(By.xpath("//*[contains(@value, 'Я согласен')]"));
      //      button.click();
      //  }

        driver.get("https://www.uralairlines.ru/?mmcore.un=qa");
        Thread.sleep(5000);
        driver.switchTo().defaultContent();
        WebElement Qatool=wait.until(presenceOfElementLocated(By.xpath("//*[@id='dptIframe']")));
        driver.switchTo().frame(Qatool); //переключились во фрейм Change settings
        //Thread.sleep(1000);
        WebElement settings = wait.until(presenceOfElementLocated(By.id("maxypt-tab-bttn-settings")));

        settings.click();
        Qatool=driver.findElement(By.xpath("//*[contains(@data-label, 'Top - Right')]"));
        Qatool.click();


        //получить все атрибуты мембера
        ReadExcel test = new ReadExcel();
        test.setInputFile("c:/aData/test.xls");
        ArrayList<String> listAtrMem = new ArrayList<String>();

        for(int j=0;j<test.getStrokaCount();j++)
        {
            listAtrMem.clear();
            for(int i=0;i<test.getStolbecCount();i++)
            {
                listAtrMem.add(test.readCell(j,i)); //получим атрибуты очередного мембера
            }
            driver.get("https://www.uralairlines.ru/?mmid=" + listAtrMem.get(0)); //открываем страницу с очередным мембером
            Thread.sleep(3000);
            //баннер есть?

            Qatool = wait.until(presenceOfElementLocated(By.xpath("//*[@name='_ym_native']")));
            driver.switchTo().frame(Qatool).switchTo().defaultContent(); //переключились во фрейм

            String isBanner = "//*[contains(@class, '" + listAtrMem.get(1) + "')]";

            Qatool=null;

            Qatool=wait.until(presenceOfElementLocated(By.xpath(isBanner)));
            Thread.sleep(1500);
            assertTrue(Qatool.isDisplayed());

            Qatool=null;
            //промокод есть?
            if(listAtrMem.contains("promo"))
            {
                Qatool=wait.until(presenceOfElementLocated(By.xpath("//*[contains(@class, 'mx-banner-promo')]")));
                assertTrue(Qatool.isDisplayed());
                Qatool=null;
            }
            //проверим текст баннера
            for(int k=2; k<listAtrMem.size();k++)
            {
                String isText = "//*[contains(text(), '" + listAtrMem.get(k) + "')]";
                Qatool=wait.until(presenceOfElementLocated(By.xpath(isText)));
                assertTrue(Qatool.isDisplayed());
                Qatool=null;
            }
            Thread.sleep(1000);
            //сохраняем скриншот
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Calendar c = Calendar.getInstance();
            String filename;
            filename = listAtrMem.get(0) +" " + String.valueOf(c.getTime())+ " EDGE";
            filename = filename.replaceAll(":", " ") + ".png";
            FileUtils.copyFile(scrFile, new File("c:\\aScreens\\" + filename));

        }


    }
*/
    @AfterClass
    public static void tearDown() {

       driver.quit();
    }
}