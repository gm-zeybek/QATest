package qumu;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.response.Response;
import qumu.model.RootSingleUser;
import qumu.model.RootUsers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static qumu.ApiUtils.create_user_with_name_and_job;


public class StepDefinition extends iniClass {

    private static Response response;

    private RootSingleUser singleUser;
    private Double actualItemTotalPrice;
    private List data;
    private String name;
    private String job;


    @Given("^I am on the home page$")
    public void iAmOnTheHomePage() {
        HomePage.homePage();
    }


    @Given("^I get the default list of users for on (\\d+)st page$")
    public void i_get_the_default_list_of_users_for_on_st_page(int pageNumber) throws Throwable {

        when()
                .get("/unknown").
                then()
                .body("page", equalTo(pageNumber))
                .body("data[0].name", equalTo("cerulean"))
                .body("data[1].name", equalTo("fuchsia rose"))
                .body("data[2].name", equalTo("true red"))
                .body("data[3].name", equalTo("aqua sky"))
                .body("data[4].name", equalTo("tigerlily"))
                .body("data[5].name", equalTo("blue turquoise"));

    }

    @When("^I get the list of all users within every page$")
    public void i_get_the_list_of_all_users_within_every_page() throws Throwable {

        given()
                .queryParam("page", "1").
                when()
                .get("users").
                then()
                .assertThat()
                .body("page", equalTo(1))
                .body("data", hasSize(6))
                .body("data[0].first_name", equalTo("George"))
                .body("data[1].first_name", equalTo("Janet"))
                .body("data[2].first_name", equalTo("Emma"))
                .body("data[3].first_name", equalTo("Eve"))
                .body("data[4].first_name", equalTo("Charles"))
                .body("data[5].first_name", equalTo("Tracey"));


        when()
                .get("users?page=2").
                then()
                .assertThat()
                .body("page", equalTo(2))
                .body("data", hasSize(6))
                .body("data[0].first_name", equalTo("Michael"))
                .body("data[1].first_name", equalTo("Lindsay"))
                .body("data[2].first_name", equalTo("Tobias"))
                .body("data[3].first_name", equalTo("Byron"))
                .body("data[4].first_name", equalTo("George"))
                .body("data[5].first_name", equalTo("Rachel"));

    }

    @Then("^I should see total users count equals the number of user ids$")
    public void i_should_see_total_users_count_equals_the_number_of_user_ids() throws Throwable {
        when()
            .get("users?page=1").
        then()
            .body("data.id",hasSize(6));

        when()
            .get("users?page=2").
        then()
            .body("data.id",hasSize(6))
            .body("total",equalTo(12));      //total users numbers


    }

    @Given("^I make a search for user (\\d+)$")
    public void i_make_a_search_for_user(int userId) throws Throwable {

       response =  when().get("/users/{userId}",userId);
    }

    @Then("^I should see the following user data$")
    public void i_should_see_the_following_user_data(DataTable table) throws Throwable {

        response.then()
                .assertThat()
                .body("data.first_name",equalTo("Emma"))
                .body("data.email",equalTo("emma.wong@reqres.in"));

    }

    @Then("^I receive error code (\\d+) in response$")
    public void i_receive_error_code_in_response(int responseCode) throws Throwable {

        response.then()
                .statusCode(responseCode);


    }


    @Given("^I create a user with following Peter Manager$")
    public void i_create_a_user_with_following_Peter_Manager() throws Throwable {

        name = "Peter";
        job = "Manager";
     response = create_user_with_name_and_job(name,job);

    }

    @Then("^response should contain the following data$")
    public void response_should_contain_the_following_data(DataTable table) throws Throwable {

        response.then()
                .assertThat()
                .body("name",equalTo(name))
                .body("job",equalTo(job))
                .body("id",not(equalTo(null)))
                .body("createdAt",not(equalTo(null)));

    }

    @Given("^I create a user with following Liza Sales$")
    public void i_create_a_user_with_following_Liza_Sales() throws Throwable {

        name = "Liza";
        job = "Sales";

        response = create_user_with_name_and_job(name,job);


    }
    public static Response login_with_credentials(DataTable table){
        List<Map<String, String>> tableList = table.asMaps(String.class,String.class);

        String email = tableList.get(0).get("email").isEmpty()? " ":tableList.get(0).get("email");
        String password = tableList.get(0).get("password").isEmpty()? " ":tableList.get(0).get("password");


       return       given()
                        .headers(email, password)
                        .contentType("application/json; charset=utf-8").
                    when()
                        .get("/login")
                        .thenReturn();
    }

    @Given("^I login unsuccessfully with the following data$")
    public void i_login_unsuccessfully_with_the_following_data(DataTable table) throws Throwable {

        List<Map<String, String>> tableList = table.asMaps(String.class,String.class);

        String email = tableList.get(0).get("email").length()==0? " ":tableList.get(0).get("email");
        String password = tableList.get(0).get("password").length()==0? " ":tableList.get(0).get("password");


        response =
                given()
                    .headers(email, password)
                    .contentType("application/json; charset=utf-8").
                when()
                    .get("/login")
                    .thenReturn();

    }
    @Given("^I login unsuccessfully with the following empty data$")
    public void i_login_unsuccessfully_with_the_following_empty_data(DataTable table) throws Throwable {
        List<Map<String, String>> tableList = table.asMaps(String.class,String.class);

        System.out.println(tableList.size());
        String email = tableList.get(0).get("email").equals("")? " ":tableList.get(0).get("email");
        String password = tableList.get(0).get("password").equals("")? " ":tableList.get(0).get("password");

        response =
                given()
                        .filter(new ErrorLoggingFilter())
                        .headers(email,password)
                        .contentType("application/json; charset=utf-8").
                        when()
                        .get("/login")
                        .thenReturn();
    }

    @Then("^I should get a response success code of (\\d+)$")
    public void i_should_get_a_response_success_code_of(int statusCode) throws Throwable {
       response.then()
               .statusCode(statusCode);

    }

    @Then("^I should get a response error code of (\\d+)$")
    public void i_should_get_a_response_error_code_of(int statusCode) throws Throwable {

       response.then()
                .statusCode(statusCode).log().ifError();

       //"error": "Missing password"
    }


    @Then("^I should see the following response message:$")
    public void i_should_see_the_following_response_message(DataTable message) throws Throwable {

        System.out.println(message.asList(String.class).get(0));


    }

    @Given("^I wait for the user list to load$")
    public void i_wait_for_the_user_list_to_load() throws Throwable {

    }

    @Then("^I should see that every user has a unique id$")
    public void i_should_see_that_every_user_has_a_unique_id() throws Throwable {

    }


    @Given("^I login in with the following details$")
    public void i_login_in_with_the_following_details(DataTable table) throws Throwable {
        List<Map<String, String>> credentialsMap = table.asMaps(String.class, String.class);
        samplePage.username.sendKeys(credentialsMap.get(0).get("userName"));
        samplePage.password.sendKeys(credentialsMap.get(0).get("Password"));
        samplePage.loginButton.click();


    }

    @Given("^I add the following items to the basket$")
    public void i_add_the_following_items_to_the_basket(DataTable table) throws Throwable {
        List<String> itemList = table.asList(String.class);
        itemList.stream().forEach(item -> {
            samplePage.addItemsToCart(item).click();
        });


    }

    @Given("^I  should see (\\d+) items added to the shopping cart$")
    public void i_should_see_items_added_to_the_shopping_cart(int expectedQuantity) throws Throwable {
        assertEquals(Integer.parseInt(samplePage.cartLinkAndQuantity.getText()), expectedQuantity);

    }

    @Given("^I click on the shopping cart$")
    public void i_click_on_the_shopping_cart() throws Throwable {
        samplePage.cartLinkAndQuantity.click();
    }

    @Given("^I verify that the QTY count for each item should be (\\d+)$")
    public void i_verify_that_the_QTY_count_for_each_item_should_be(int each) throws Throwable {
        samplePage.itemQuantitiesList.stream().forEach(element -> {
            assertEquals(Integer.parseInt(element.getText()), each);
        });
    }

    @Given("^I remove the following item:$")
    public void i_remove_the_following_item(DataTable table) throws Throwable {
        List<String> itemList = table.asList(String.class);
        itemList.stream().forEach(item -> {
            samplePage.removeItemFromCart(item).click();
        });

    }

    @Given("^I should see (\\d+) items left in the shopping cart$")
    public void i_should_see_items_left_in_the_shopping_cart(int leftQuantity) throws Throwable {
        assertEquals(Integer.parseInt(samplePage.cartLinkAndQuantity.getText()), leftQuantity);
    }

    @Given("^I click on the CHECKOUT button$")
    public void i_click_on_the_CHECKOUT_button() throws Throwable {
        samplePage.checkoutButton.click();
    }

    @Given("^I type \"([^\"]*)\" for First Name$")
    public void i_type_for_First_Name(String firstName) throws Throwable {
        samplePage.firstname.sendKeys(firstName);
    }

    @Given("^I type \"([^\"]*)\" for Last Name$")
    public void i_type_for_Last_Name(String lastName) throws Throwable {
        samplePage.lastname.sendKeys(lastName);
    }

    @Given("^I type \"([^\"]*)\" for ZIP/Postal Code$")
    public void i_type_for_ZIP_Postal_Code(String postalCode) throws Throwable {
        samplePage.postalcode.sendKeys(postalCode);
    }

    @When("^I click on the CONTINUE button$")
    public void i_click_on_the_CONTINUE_button() throws Throwable {
        samplePage.continueButton.click();
    }

    @Then("^Item total will be equal to the total of items on the list$")
    public void item_total_will_be_equal_to_the_total_of_items_on_the_list() throws Throwable {
        Double expectedTotalPrice = samplePage.itemPricesList.stream().map(element -> {
            return Double.parseDouble(element.getText().substring(1));
        }).reduce(Double::sum).get();
        String text = samplePage.totalPrice.getText();
        actualItemTotalPrice = Arrays.stream(text.split(" "))
                .skip(2).mapToDouble(i -> Double.parseDouble(i.substring(1))).sum();
        assertEquals(actualItemTotalPrice, expectedTotalPrice);

    }

    @Then("^a Tax rate of (\\d+) % is applied to the total$")
    public void a_Tax_rate_of_is_applied_to_the_total(int taxRate) throws Throwable {
        Double actualTax = Arrays.stream(samplePage.taxTotal.getText().split(" "))
                .skip(1).mapToDouble(i -> Double.parseDouble(i.substring(1))).sum();
        Double actualtaxRate = Math.floor(Double.valueOf(actualTax / actualItemTotalPrice * 100));
        Double expectedTaxRate = Double.valueOf(taxRate);
        assertEquals(actualtaxRate, expectedTaxRate);

    }
}
