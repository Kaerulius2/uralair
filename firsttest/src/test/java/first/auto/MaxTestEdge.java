package first.auto;

import jxl.write.WriteException;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

//import static junit.framework.Assert.assertTrue;


public class MaxTestEdge {

    //блок глобальных переменных-------------------------------------------блок глобальных переменных
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String browser_type = "EDGE";
    //пути для файлов первого сценария
    String source_path1="c:/aData/1_scn/test_1scn.xls";
    String source_cpy1="c:/aData/1_scn/test_1scn_"+browser_type;
    String screen_path1="c:/aScreens/1_scn/";

    String source_path2="c:/aData/2_scn/test_2scn.xls";
    String source_cpy2="c:/aData/2_scn/test_2scn_"+browser_type;
    String screen_path2="c:/aScreens/2_scn/";

    String source_path3="c:/aData/3_scn/test_3scn.xls";
    String source_cpy3="c:/aData/3_scn/test_3scn_"+browser_type;
    String screen_path3="c:/aScreens/3_scn/";

    String source_path4="c:/aData/4_scn/test_4scn.xls";
    String source_cpy4="c:/aData/4_scn/test_4scn_"+browser_type;
    String screen_path4="c:/aScreens/4_scn/";

    //блок глобальных переменных-------------------------------------------блок глобальных переменных

    public String getcurrdatetime()
    {
        Calendar c = Calendar.getInstance();
        String time = c.getTime().toString();
        int x = time.length();
        int f = time.indexOf(" ");
        time=time.substring(f+1,x-9);

        time = time.replaceAll(":"," ");
        return time;
    }

    public static boolean isElementPresent(final By locator) //метод проверки наличия элемента на странице
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

    private void getScreenshot(String scn, String path, boolean isError) throws IOException {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filename;

        filename = scn +" " + getcurrdatetime() + " " + browser_type+".png";
        //filename = filename +".png"; //////////////////
        if(isError){
            filename="FAIL "+ filename;
        }

        FileUtils.copyFile(scrFile, new File(path + filename));
    }


    public ExpectedCondition<String> thereIsWindowOtherThan(final Set<String> oldW) //на вход получим множество окон до клика
    {
        return new ExpectedCondition<String>() {
            public String apply(WebDriver input) {
                Set<String> newW = driver.getWindowHandles();   //получим множество окон после клика
                newW.removeAll(oldW);                           //вычтем из нового множества старое
                if(newW.size()>0) {                             //если множества различны (т.е. после клика появились новые окна), вернем первое, иначе - NULL
                    return newW.iterator().next();
                }else
                {
                    return null;
                }
            }
        };

    }



    @BeforeClass
    public void setup() throws InterruptedException {

        System.setProperty("webdriver.edge.driver", "C:\\Edgedriver\\MicrosoftWebDriver.exe"); //C:\Edgedriver
        driver = new EdgeDriver();

        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,15);

                //Подготовка к работе - закрытие приглашения подписаться на рассылку и предупреждения о cookies.
                String url_ua="http://uralairlines.ru";
                driver.get(url_ua);
                driver.manage().window().maximize();
                //email отвменить

                if(isElementPresent(By.xpath("//*[contains(@class, 'layer-close js_email_registration_close')]")))
                {
                    WebElement button = driver.findElement(By.xpath("//*[contains(@class, 'layer-close js_email_registration_close')]"));
                    button.click();
                }

                if(isElementPresent(By.xpath("//*[contains(@class, 'layer-close js_email_registration_close')]")))
                {
                    WebElement button2 = driver.findElement(By.xpath("//*[contains(@value, 'Я согласен')]"));
                    button2.click();
                }

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
                        /*
                        Qatool=driver.findElement(By.xpath("//*[contains(@class, 'maxypt-save-settings maxypt-tooltip-right')]"));
                        Qatool.click();
                           */


    }

    //@Before


    @Test //Тест сценария 1
            public void edge_scn_1() throws IOException, InterruptedException, WriteException {

                boolean wasErrScreen=false; //флаг того, что был сделан скриншот с ошибкой

                dataSheet ds = new dataSheet();

                String log_path = source_cpy1+"_"+getcurrdatetime()+"_log.xls";
                ds.readExcel(source_path1, log_path);

                ReadExcel test = new ReadExcel();
                test.setInputFile(source_path1);
                ArrayList<String> listAtrMem = new ArrayList<String>();

                for(int j=0;j<test.getStrokaCount();j++) //по количеству мемберов
                {
                    listAtrMem.clear();
                    wasErrScreen=false;
                        for(int i=0;i<test.getStolbecCount();i++) //по количеству ячеек по горизонтали
                        {
                                listAtrMem.add(test.readCell(j,i)); //получим атрибуты очередного мембера
                        }
                            //удалим пустые блоки
                            while(listAtrMem.contains(""))
                            {
                                listAtrMem.remove("");
                            }

                    ds.setValueIntoCell("Лист1", 10, j, "PASS"); //установим в лог-файле отметку о прохождении и затрём её, если что-то сломается - имя листа, номер столбца, номер строки

                    String scenario = listAtrMem.get(0); //возьмем номер сценария

                    String memberUrl = "https://www.uralairlines.ru/?mmid=" + listAtrMem.get(1) + "&logs=on";

                    driver.get(memberUrl); //открываем страницу с очередным мембером

                    //Проверяем наличие необходимых элементов
                    //1. Баннер есть?

                    String isBanner = "//*[contains(@class, '" + listAtrMem.get(2) + "')]"; //загрузим локатор баннера из файла              //assertTrue(isElementPresent(By.xpath(isBanner)));
                                if(!isElementPresent(By.xpath(isBanner))){ //если нужного баннера нет - сменить сессию и проверить заново
                                    String parentWin = driver.getWindowHandle();
                                    Set<String> oldWins = driver.getWindowHandles();
                                    //((JavascriptExecutor)driver).executeScript("window.open();");
                                    //driver.findElement(By.xpath("//*[contains(@class,'mx-banner-btn')]")).click();
                                    driver.get("https://checkin.uralairlines.ru/");
                                    Thread.sleep(15000);
                                    //driver.get(memberUrl);

                                    //String newWin = wait.until(thereIsWindowOtherThan(oldWins)); // ожидаем, когда появится хендл нового окна (или окон)
                                    //driver.switchTo().window(parentWin);

                                    //driver.close();

                                    //driver.switchTo().window(newWin);

                                    driver.get(memberUrl);
                                            if(!isElementPresent(By.xpath(isBanner))){
                                                ds.setValueIntoCell("Лист1", 10, j, "FAIL - отсутствует баннер!"); //имя листа, номер столбца, номер строки
                                                getScreenshot(scenario,screen_path1,true);
                                                wasErrScreen = true;
                                                continue;
                                            }
                                    }

                    //промокод есть?
                    if(listAtrMem.contains("promo"))
                    {
                        listAtrMem.remove("promo");
                        //assertTrue(isElementPresent(By.xpath("//*[contains(@class, 'mx-banner-promo')]")));
                        if(!isElementPresent(By.xpath("//*[contains(@class, 'mx-banner-promo')]"))){
                            ds.setValueIntoCell("Лист1", 10, j, "FAIL - отсутствует промокод!"); //имя листа, номер столбца, номер строки
                            getScreenshot(scenario,screen_path1,true);
                            wasErrScreen = true;
                            continue;
                        }
                    }

                    //проверим текст баннера
                    for(int k=3; k<listAtrMem.size();k++) //по количеству текстовых блоков
                    {
                        String isText = "//*[contains(text(), '" + listAtrMem.get(k) + "')]";

                                if(!isElementPresent(By.xpath(isText))){
                                    ds.setValueIntoCell("Лист1", 10, j, "FAIL - текст баннера!"); //имя листа, номер столбца, номер строки
                                    getScreenshot(scenario,screen_path1,true);
                                    wasErrScreen = true;
                                }


                    }

                    if(!wasErrScreen){
                        Thread.sleep(3000); //не успевает отрисовываться!!!
                        getScreenshot(scenario, screen_path1,false); //если скриншотов не было, сделать!

                    }



                }//for по количеству мемберов
                ds.closeFile();

            }//тест первого сценария

    @Test //Тест сценария 2
    public void edge_scn_2() throws IOException, InterruptedException, WriteException {

        boolean wasErrScreen=false; //флаг того, что был сделан скриншот с ошибкой

        //создаем лог
        dataSheet ds = new dataSheet();

        String log_path = source_cpy2+"_"+getcurrdatetime()+"_log.xls";
        ds.readExcel(source_path2, log_path);

        ReadExcel test = new ReadExcel();
        test.setInputFile(source_path2);
        ArrayList<String> listAtrMem = new ArrayList<String>();

        for(int j=0;j<test.getStrokaCount();j++) //по количеству мемберов
        {
            listAtrMem.clear();
            wasErrScreen=false;
            for(int i=0;i<test.getStolbecCount();i++) //по количеству ячеек по горизонтали
            {
                listAtrMem.add(test.readCell(j,i)); //получим атрибуты очередного мембера
            }
            //удалим пустые блоки
            while(listAtrMem.contains(""))
            {
                listAtrMem.remove("");
            }

            ds.setValueIntoCell("Лист1", 10, j, "PASS"); //установим в лог-файле отметку о прохождении и затрём её, если что-то сломается - имя листа, номер столбца, номер строки

            String scenario = listAtrMem.get(0); //возьмем номер сценария

            String memberUrl = "https://www.uralairlines.ru/?mmid=" + listAtrMem.get(1) + "&logs=on";

            driver.get(memberUrl); //открываем страницу с очередным мембером

            //если нет баннера, создать брошенный поиск
            String isBanner = "//*[contains(@class, '" + listAtrMem.get(2) + "')]"; //загрузим локатор баннера из файла              //assertTrue(isElementPresent(By.xpath(isBanner)));
            if(!isElementPresent(By.xpath(isBanner))){ //если нужного баннера нет - создть брошенный поиск

                WebElement from = driver.findElement(By.id("avia_from"));

                if(isElementPresent(By.xpath("//*[contains(@class, 'mx-banner-close')]")))
                {
                    driver.findElement((By.xpath("//*[contains(@class, 'mx-banner-close')]"))).click();
                }

                from.clear();
                from.click();
                from.sendKeys("Екатеринбург" + Keys.TAB);



                WebElement to = driver.findElement(By.id("avia_to"));
                to.clear();
                to.click();
                to.sendKeys("Петропавловск-Камчатский"+ Keys.TAB);

                driver.findElement(By.id("calendar-text-container")).click();

                driver.get(memberUrl);
                if(!isElementPresent(By.xpath(isBanner))){
                    ds.setValueIntoCell("Лист1", 10, j, "FAIL - отсутствует баннер!"); //имя листа, номер столбца, номер строки
                    getScreenshot(scenario,screen_path2,true);
                    wasErrScreen = true;
                    continue;
                }
            }//if !isElementPresent

            //промокод есть?
            if(listAtrMem.contains("promo"))
            {
                listAtrMem.remove("promo");

                if(!isElementPresent(By.xpath("//*[contains(@class, 'mx-banner-promo')]"))){
                    ds.setValueIntoCell("Лист1", 10, j, "FAIL - отсутствует промокод!"); //имя листа, номер столбца, номер строки
                    getScreenshot(scenario,screen_path2,true);
                    wasErrScreen = true;
                    continue;
                }
            }

            //проверим текст баннера
            for(int k=3; k<listAtrMem.size();k++) //по количеству текстовых блоков
            {
                String isText = "//*[contains(text(), '" + listAtrMem.get(k) + "')]";

                if(!isElementPresent(By.xpath(isText))){
                    ds.setValueIntoCell("Лист1", 10, j, "FAIL - текст баннера!"); //имя листа, номер столбца, номер строки
                    getScreenshot(scenario,screen_path2,true);
                    wasErrScreen = true;
                }
            }

            if(!wasErrScreen){
                Thread.sleep(3000); //не успевает отрисовываться!!!
                getScreenshot(scenario, screen_path2,false); //если скриншотов не было, сделать!

            }

        }//for по количеству мемберов
        ds.closeFile();
        driver.findElement(By.xpath("//*[contains(@class, 'mx-banner-btn')]")).click(); //кликнуть на кнопку для сброса сценария
    }

    @Test //Тест сценария 3
    public void edge_scn_3() throws IOException, InterruptedException, WriteException {

        boolean wasErrScreen=false; //флаг того, что был сделан скриншот с ошибкой

        //создаем лог
        dataSheet ds = new dataSheet();
        String log_path = source_cpy3+"_"+getcurrdatetime()+"_log.xls";
        ds.readExcel(source_path3, log_path);

        ReadExcel test = new ReadExcel();
        test.setInputFile(source_path3);
        ArrayList<String> listAtrMem = new ArrayList<String>();

        for(int j=0;j<test.getStrokaCount();j++) //по количеству мемберов
        {
            listAtrMem.clear();
            wasErrScreen=false;
            for(int i=0;i<test.getStolbecCount();i++) //по количеству ячеек по горизонтали
            {
                listAtrMem.add(test.readCell(j,i)); //получим атрибуты очередного мембера
            }
            //удалим пустые блоки
            while(listAtrMem.contains(""))
            {
                listAtrMem.remove("");
            }

            ds.setValueIntoCell("Лист1", 10, j, "PASS"); //установим в лог-файле отметку о прохождении и затрём её, если что-то сломается - имя листа, номер столбца, номер строки

            String scenario = listAtrMem.get(0); //возьмем номер сценария

            String memberUrl = "https://www.uralairlines.ru/?mmid=" + listAtrMem.get(1) + "&logs=on";

            driver.get(memberUrl); //открываем страницу с очередным мембером

            //если нет баннера
            String isBanner = "//*[contains(@class, '" + listAtrMem.get(2) + "')]"; //загрузим локатор баннера из файла              //assertTrue(isElementPresent(By.xpath(isBanner)));
            if(!isElementPresent(By.xpath(isBanner))){ //если нужного баннера нет, зафейлим проход
                    ds.setValueIntoCell("Лист1", 10, j, "FAIL - отсутствует баннер!"); //имя листа, номер столбца, номер строки
                    getScreenshot(scenario,screen_path3,true);
                    wasErrScreen = true;
                    continue;
            }//if !isElementPresent

            //промокод есть?
            if(listAtrMem.contains("promo"))
            {
                listAtrMem.remove("promo");

                if(!isElementPresent(By.xpath("//*[contains(@class, 'mx-banner-promo')]"))){
                    ds.setValueIntoCell("Лист1", 10, j, "FAIL - отсутствует промокод!"); //имя листа, номер столбца, номер строки
                    getScreenshot(scenario,screen_path3,true);
                    wasErrScreen = true;
                    continue;
                }
            }

            //проверим текст баннера
            for(int k=3; k<listAtrMem.size();k++) //по количеству текстовых блоков
            {
                String isText = "//*[contains(text(), '" + listAtrMem.get(k) + "')]";

                if(!isElementPresent(By.xpath(isText))){
                    ds.setValueIntoCell("Лист1", 10, j, "FAIL - текст баннера!"); //имя листа, номер столбца, номер строки
                    getScreenshot(scenario,screen_path3,true);
                    wasErrScreen = true;
                }
            }

            if(!wasErrScreen){
                Thread.sleep(3000); //не успевает отрисовываться!!!
                getScreenshot(scenario, screen_path3,false); //если скриншотов не было, сделать!

            }

        }//for по количеству мемберов
        ds.closeFile();

    }

    @Test //Тест сценария 4 -------------------------------------------------------------------------отдельно проверить https://www.uralairlines.ru/?mmid=1-3LV6AWB&logs=on&utm_content=MOW-IKT!!!!!!
    public void edge_scn_4() throws IOException, InterruptedException, WriteException {

        boolean wasErrScreen=false; //флаг того, что был сделан скриншот с ошибкой

        //создаем лог
        dataSheet ds = new dataSheet();

        String log_path = source_cpy4+"_"+getcurrdatetime()+"_log.xls";
        ds.readExcel(source_path4, log_path);

        ReadExcel test = new ReadExcel();
        test.setInputFile(source_path4);
        ArrayList<String> listAtrMem = new ArrayList<String>();

        for(int j=0;j<test.getStrokaCount();j++) //по количеству мемберов
        {
            listAtrMem.clear();
            wasErrScreen=false;
            for(int i=0;i<test.getStolbecCount();i++) //по количеству ячеек по горизонтали
            {
                listAtrMem.add(test.readCell(j,i)); //получим атрибуты очередного мембера
            }
            //удалим пустые блоки
            while(listAtrMem.contains(""))
            {
                listAtrMem.remove("");
            }

            ds.setValueIntoCell("Лист1", 10, j, "PASS"); //установим в лог-файле отметку о прохождении и затрём её, если что-то сломается - имя листа, номер столбца, номер строки

            String scenario = listAtrMem.get(0); //возьмем номер сценария

            String memberUrl = "https://www.uralairlines.ru/?mmid=" + listAtrMem.get(1) + "&logs=on";

            driver.get(memberUrl); //открываем страницу с очередным мембером

            if(listAtrMem.contains("utm"))
            {
                int numUTM = listAtrMem.indexOf("utm");
                String url=listAtrMem.get(numUTM+1);
                listAtrMem.remove(numUTM+1);
                listAtrMem.remove("utm");

                driver.get(url);

            }
            //если нет баннера
            String isBanner = "//*[contains(@class, '" + listAtrMem.get(2) + "')]"; //загрузим локатор баннера из файла              //assertTrue(isElementPresent(By.xpath(isBanner)));

            if(!isElementPresent(By.xpath(isBanner))){
                    ds.setValueIntoCell("Лист1", 10, j, "FAIL - отсутствует баннер!"); //имя листа, номер столбца, номер строки
                    getScreenshot(scenario,screen_path4,true);
                    wasErrScreen = true;
                    continue;
            }//if !isElementPresent

            //промокод есть?
            if(listAtrMem.contains("promo"))
            {
                listAtrMem.remove("promo");

                if(!isElementPresent(By.xpath("//*[contains(@class, 'mx-banner-promo')]"))){
                    ds.setValueIntoCell("Лист1", 10, j, "FAIL - отсутствует промокод!"); //имя листа, номер столбца, номер строки
                    getScreenshot(scenario,screen_path4,true);
                    wasErrScreen = true;
                    continue;
                }
            }

            //проверим текст баннера
            for(int k=3; k<listAtrMem.size();k++) //по количеству текстовых блоков
            {
                String isText = "//*[contains(text(), '" + listAtrMem.get(k) + "')]";

                if(!isElementPresent(By.xpath(isText))){
                    ds.setValueIntoCell("Лист1", 10, j, "FAIL - текст баннера!"); //имя листа, номер столбца, номер строки
                    getScreenshot(scenario,screen_path4,true);
                    wasErrScreen = true;
                }
            }

            if(!wasErrScreen){
                Thread.sleep(5000); //не успевает отрисовываться!!!
                getScreenshot(scenario, screen_path4,false); //если скриншотов не было, сделать!

            }

        }//for по количеству мемберов
        ds.closeFile();

    }

    @AfterClass
    public static void tearDown() {

        driver.quit();
    }
}// class MaxTestChrome
