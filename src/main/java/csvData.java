import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class csvData {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver.manage().window().maximize();
        driver.get("https://web.whatsapp.com/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"side\"]/div[1]/div/label/div")));

        File file = new File("data/Whatsapp Automation Data.csv");
        try {
            FileReader fileReader = new FileReader(file);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] row;
            String message, contact;
            while ((row = csvReader.readNext()) != null) {
                contact = row[0];
                message = row[1];

//                System.out.println(contact+" | "+message);

                WebElement searchBox = driver.findElement(By.xpath("//*[@id=\"side\"]/div[1]/div/label/div/div[2]"));
                searchBox.sendKeys(contact);
                searchBox.sendKeys(Keys.ENTER);
                Thread.sleep(5000);

                WebElement msgBox = driver.findElement(By.xpath("//*[@id=\"main\"]/footer/div[1]/div[2]/div[1]"));
                msgBox.sendKeys(message);
                Thread.sleep(2000);
                driver.findElement(By.xpath("//*[@id=\"main\"]/footer/div[1]/div[3]/button")).click();
                Thread.sleep(5000);
            }
            driver.findElement(By.xpath("//*[@id=\"side\"]/header/div[2]/div/span/div[3]/div/span")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//*[@id=\"side\"]/header/div[2]/div/span/div[3]/span/div[1]/ul/li[7]")).click();
        } catch (FileNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (CsvValidationException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
        driver.quit();
    }
}
