import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class API {

    private static final long SLEEP5 = 5000;
    private static final long SLEEP2 = 2000;
    private static final long SLEEP1 = 1000;
    private static final String settingsXPath = "//*[@id=\"side\"]/header/div[2]/div/span/div[3]/div/span";
    private static final String logOutXPath = "//*[@id=\"side\"]/header/div[2]/div/span/div[3]/span/div[1]/ul/li[7]";

    public static void main(String[] args) {
        File file = new File("data/"+"Whatsapp Automation Data.xlsx");
        String fileName = file.toString(), extension;
        int index = fileName.lastIndexOf('.');
        extension = fileName.substring(index + 1);
        System.out.println("File extension is " + extension);

        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver.manage().window().maximize();
        driver.get("https://web.whatsapp.com/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"side\"]/div[1]/div/label/div")));

        try {
            FileReader fileReader = new FileReader(file);
            FileInputStream fileInputStream = new FileInputStream(file);

            switch (extension) {

                case "xlsx":
                    System.out.println(".xlsx");
                    XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
                    XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
                    for (Row cells : xssfSheet) {
                        XSSFRow row = (XSSFRow) cells;
                        XSSFCell contact = row.getCell(0), message = row.getCell(1);

                        sendMessage(driver, new DataFormatter().formatCellValue(contact), String.valueOf(message));
                    }
                    break;

                case "xls":
                    System.out.println(".xls");
                    HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream);
                    HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
                    for (Row cells : hssfSheet) {
                        HSSFRow row = (HSSFRow) cells;
                        HSSFCell contact = row.getCell(0), message = row.getCell(1);

                        sendMessage(driver, new DataFormatter().formatCellValue(contact), String.valueOf(message));
                    }
                    break;

                case "csv":
                    System.out.println(".csv");
                    CSVReader csvReader = new CSVReader(fileReader);
                    String[] row;
                    String message, contact;
                    while ((row = csvReader.readNext()) != null) {
                        contact = row[0];
                        message = row[1];

                        sendMessage(driver, contact, message);
                    }
                    break;
            }

            driver.findElement(By.xpath(settingsXPath)).click();
            Thread.sleep(SLEEP1);
            driver.findElement(By.xpath(logOutXPath)).click();
            Thread.sleep(SLEEP1);

        } catch (FileNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (CsvValidationException e) {
            System.out.println(e.getLocalizedMessage());
        }

        driver.quit();
    }

    private static void sendMessage(WebDriver driver, String contact, String message) throws InterruptedException, UnsupportedEncodingException {
        Thread.sleep(SLEEP5);
        String link = "https://web.whatsapp.com/send?phone=91"+contact+"&text="+ URLEncoder.encode(message, StandardCharsets.UTF_8.toString())+"&source=&data=";
        driver.get(link);
        Thread.sleep(SLEEP5);
        driver.findElement(By.xpath("//*[@id=\"main\"]/footer/div[1]/div[3]/button")).click();
        Thread.sleep(SLEEP2);
    }
}
