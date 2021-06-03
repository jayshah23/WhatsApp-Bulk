import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class xlsData {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver.manage().window().maximize();
        driver.get("https://web.whatsapp.com/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"side\"]/div[1]/div/label/div")));

        File file = new File("data/Whatsapp Automation Data.xls");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet sheet = workbook.getSheetAt(0);
            for (Row cells : sheet) {
                HSSFRow row = (HSSFRow) cells;
                HSSFCell contact = row.getCell(0), message = row.getCell(1);

                WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"side\"]/div[1]/div/label/div/div[2]"));
                searchBox.sendKeys(new DataFormatter().formatCellValue(contact));
                searchBox.sendKeys(Keys.ENTER);
                Thread.sleep(5000);

                WebElement msgBox = driver.findElement(By.xpath("//*[@id=\"main\"]/footer/div[1]/div[2]/div[1]"));
                msgBox.sendKeys(message.toString());
                Thread.sleep(2000);
                driver.findElement(By.xpath("//*[@id=\"main\"]/footer/div[1]/div[3]/button")).click();
                Thread.sleep(5000);
            }
            driver.findElement(By.xpath("//*[@id=\"side\"]/header/div[2]/div/span/div[3]/div/span")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[@id=\"side\"]/header/div[2]/div/span/div[3]/span/div[1]/ul/li[7]")).click();
        } catch (FileNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        driver.quit();
    }
}
