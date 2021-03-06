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
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;

import static modals.Adidas.Elements;

public class AdidasTest {
    private WebDriver driver;
    private ArrayList<Adidas> productsInChart;

    @BeforeMethod
    void setupBeforeMethod(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        Adidas.setupDriver(driver);
        productsInChart = new ArrayList<>();
    }

    @AfterMethod
    void tearDownMethod() throws InterruptedException {
        Thread.sleep(2200);
        driver.quit();
    }

    private void addToChart(Adidas product){

        driver.get("https://www.demoblaze.com/index.html");
        product.getMenuCategory().click();
        product.getProductLink().click();
        product.getButtonAddToChart().click();

        new WebDriverWait(driver, 4)
                .ignoring(NoAlertPresentException.class)
                .until(ExpectedConditions.alertIsPresent());

        driver.switchTo().alert().accept();

        product.updateAmount();
        productsInChart.add(product);
    }

    /**
     * @param product product name
     */
    private void removeFromChart(Adidas product){
        int productIndexToRemove = productsInChart.indexOf(product);

        Elements.getButtonRemoveProductByProductIndex(product).click();
        productsInChart.remove(product);
    }

    @Test (invocationCount = 4)
    void test() throws InterruptedException {
        addToChart(Adidas.getInstance(EProductCategory.category_Laptop, "Sony vaio i5"));
        Adidas dellToRemove = Adidas.getInstance(EProductCategory.category_Laptop, "Dell i7 8gb");
        addToChart(dellToRemove);

        Adidas.Elements.getButtonCart().click();

        String urlCart = driver.getCurrentUrl();

        removeFromChart(dellToRemove);

        //This sleep's target is to give a time to web page for removing the product
        Thread.sleep(2000);

        driver.get(urlCart);

        Elements.getButtonPlaceOrder().click();

        Faker faker = new Faker();

        Elements.PurchaseForm.getTextBoxName().sendKeys(faker.name().fullName());
        Elements.PurchaseForm.getTextBoxCountry().sendKeys(faker.country().name());
        Elements.PurchaseForm.getTextBoxCity().sendKeys(faker.address().cityName());
        Elements.PurchaseForm.getTextBoxCreditCard().sendKeys(faker.finance().creditCard());
        Elements.PurchaseForm.getTextBoxMonth().sendKeys(String.valueOf(faker.number().numberBetween(1, 12)));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        Elements.PurchaseForm.getTextBoxYear().sendKeys(String.valueOf(faker.number().numberBetween(currentYear, currentYear + 10)));

        Elements.PurchaseForm.getButtonPurchase().click();

        Assert.assertEquals(Elements.PurchaseForm.getResultAmount(), productsInChart.stream().mapToDouble(Adidas::getAmount).sum());

        Elements.PurchaseForm.getButtonOK().click();
    }
}
