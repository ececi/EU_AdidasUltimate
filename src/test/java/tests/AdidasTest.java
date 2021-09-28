package tests;

import com.github.javafaker.Faker;
import enums.EProductCategory;
import io.github.bonigarcia.wdm.WebDriverManager;
import modals.Adidas;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AdidasTest {
    WebDriver driver;

    @BeforeMethod
    void setupBeforeMethod(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @AfterMethod
    void tearDownMethod() throws InterruptedException {
        Thread.sleep(5000);
        driver.quit();
    }

    private void addToChart(Adidas product){
        driver.get("https://www.demoblaze.com/index.html");
        driver.findElement(By.linkText(product.getProductCategory().getLinkText())).click();
        driver.findElement(By.linkText(product.getLinkText())).click();
        driver.findElement(By.linkText(Adidas.addToChartLinkText)).click();

        new WebDriverWait(driver, 4)
                .ignoring(NoAlertPresentException.class)
                .until(ExpectedConditions.alertIsPresent());

        driver.switchTo().alert().accept();

        product.setAmount(getCurrentProductAmount());
    }

    private Double getCurrentProductAmount(){
        return Double.parseDouble(driver.findElement(By.cssSelector(".price-container")).getText().split(" ")[0].substring(1));
    }

    @Test
    void test() throws InterruptedException {

        ArrayList<Adidas> productsInChart = new ArrayList<>();

        productsInChart.add(Adidas.getInstance(EProductCategory.category_Laptop, "Sony vaio i5"));
        addToChart(productsInChart.get(productsInChart.size() - 1));

        productsInChart.add(Adidas.getInstance(EProductCategory.category_Laptop, "Dell i7 8gb"));
        addToChart(productsInChart.get(productsInChart.size() - 1));

        driver.findElement(Adidas.byCart).click();

        String urlCart = driver.getCurrentUrl();

        int productIndexToRemove = 1;//zero based index
        driver.findElement(By.cssSelector("#tbodyid tr:nth-of-type(" + (productIndexToRemove + 1) + ") td:nth-of-type(4) a")).click();
        productsInChart.remove(productIndexToRemove);

        //This sleep's target is to give a time to web page for removing the product
        Thread.sleep(2000);

        driver.get(urlCart);

        driver.findElement(By.xpath("//button[.='Place Order']")).click();

        Faker faker = new Faker();

        WebElement txtName = driver.findElement(By.id("name"));

        txtName.sendKeys(faker.name().fullName());
        driver.findElement((By.id("country"))).sendKeys(faker.country().name());
        driver.findElement(By.id("city")).sendKeys(faker.address().cityName());
        driver.findElement(By.id("card")).sendKeys(faker.finance().creditCard());
        driver.findElement(By.id("month")).sendKeys(String.valueOf(faker.number().numberBetween(1, 12)));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        driver.findElement(By.id("year")).sendKeys(String.valueOf(faker.number().numberBetween(currentYear, currentYear + 10)));

        driver.findElement(By.xpath("//button[.='Purchase']")).click();

        WebElement pPurchaseResult = driver.findElement(By.cssSelector(".lead.text-muted"));

        String[] resultParts = pPurchaseResult.getAttribute("innerHTML").split("<br>");
        int resultID = Integer.parseInt(resultParts[0].split(" ")[1]);
        double resultAmount = Double.parseDouble(resultParts[1].split(" ")[1]);

        Assert.assertEquals(resultAmount, productsInChart.stream().mapToDouble(Adidas::getAmount).sum());

        driver.findElement(By.xpath("//button[.='OK']")).click();
    }
}
