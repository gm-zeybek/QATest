package qumu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class SamplePage extends BasePage {
    public SamplePage() {
        PageFactory.initElements(driver, this);
    }


    /**
     * Login page locators
     *
     */

    @FindBy(css = "input[placeholder=\"Username\"]")
    public WebElement username;

    @FindBy(css = "input[placeholder=\"Password\"]")
    public WebElement password;

    @FindBy(css = "input[id=\"login-button\"]")
    public WebElement loginButton;

    /**
     * Inventory page locators
     *
     */

    @FindBy(xpath = "//div[contains(@id,\"shopping_cart\")]")
    public WebElement cartLinkAndQuantity;


    /**
     * Cart page locators
     *
     */

    @FindBy(xpath = "//div[contains(@class,\"cart_item\")]/div[contains(@class,\"cart_quantity\")]")
    public List<WebElement> itemQuantitiesList;

    @FindBy(css = "button#checkout")
    public WebElement checkoutButton;

    /**
     * Checkout page locators
     *
     */

    @FindBy(css = "input#first-name")
    public WebElement firstname;

    @FindBy(css = "input#last-name")
    public WebElement lastname;

    @FindBy(css = "input#postal-code")
    public WebElement postalcode;

    @FindBy(css = "input#continue")
    public WebElement continueButton;

    @FindBy(css = "button#finish")
    public WebElement finishButton;


    @FindBy(xpath = "//div[@class='inventory_item_price']")
    public List<WebElement> itemPricesList;

    @FindBy(xpath = "//div[@class='summary_subtotal_label']")
    public WebElement totalPrice;

    @FindBy(xpath = "//div[@class='summary_tax_label']")
    public WebElement taxTotal;

    @FindBy(xpath = "//div[@class='summary_total_label']")
    public WebElement totalPriceInclusiveTax;



    public WebElement addItemsToCart(String itemName){
        String locator ="//div[contains(text(),'"+itemName+"')]/../../../div[2]/button";

        return driver.findElement(By.xpath(locator));
    }
    public WebElement removeItemFromCart(String itemName){
        String locator ="(//div[contains(text(),'"+itemName+"')]/../../.)/div/button";
        return driver.findElement(By.xpath(locator));
    }







}
