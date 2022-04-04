package qumu;

import io.restassured.RestAssured;

public class HomePage extends BasePage {

    public static void homePage() {
        driver.get(LoadProp.getproperty("url"));

    }
}
