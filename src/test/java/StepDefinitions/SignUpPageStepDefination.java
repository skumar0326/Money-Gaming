package StepDefinitions;

import Base.BaseTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SignUpPageStepDefination {

    public static String pageTitle;
    public String before = null;
    public String after = null;
    BaseTest baseTest;

    public SignUpPageStepDefination(BaseTest baseTest) {
        this.baseTest = baseTest;
    }

    @Given("user is on Sign-Up page")
    public void user_is_on_sign_up_page() {
        baseTest.openBrowser();
        baseTest.navigate("appUrl");
        baseTest.waitForPageToLoad();

    }


    @When("user gets the title of the page")
    public void user_gets_the_title_of_the_page() {
         pageTitle = baseTest.getPageTitle();
    }

    @Then("page title should be {string}")
    public void page_title_should_be(String expectedTitle) {
        if (pageTitle.equals(expectedTitle)){
            baseTest.infoLog("Title Matches");
        } else
            baseTest.infoLog("Title does not match");
    }

    @And("Clicks on JOIN NOW! button")
    public void clicks_on_join_now_button(){
        baseTest.click("joinInButton_xpath");

    }
    @Then("user selects {string} from dropdown for Title field")
    public void user_selects_from_dropdown_for_title_field(String title) {
        baseTest.doSelectByVisibleText("title_id",title);
        //baseTest.takeScreenShot();

    }
    @And("user enters {string}  in FirstName field")
    public void user_enters_in_first_name_field(String firstName) {
        baseTest.type("firstName_id" ,firstName);

    }
    @And("user enters {string} in SurName field")
    public void user_enters_in_sur_name_field(String surName) {
       baseTest.type("surName_name" ,surName);
    }

    @Then("user checks the terms and conditions")
    public void user_checks_the_terms_and_conditions() {
      baseTest.click("checkBox_name");
    }

    @Then("user submits the form by clicking on JOIN NOW! button below")
    public void user_submits_the_form_by_clicking_on_join_now_button_below() {
        baseTest.click("regJoinInButton_xpath");

    }
    @And("message appears  {string} under the date of  birth box field")
    public void message_appears_under_the_date_of_birth_box_field(String validationMessage) {
        boolean text = baseTest.verifyText("requiredMsg_xpath", validationMessage);
         if (text){
             baseTest.passLog("Message displayed");
         }else
             baseTest.reportFailure("not displayed");


    }
}
