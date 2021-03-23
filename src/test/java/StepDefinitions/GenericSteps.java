package StepDefinitions;

import Base.BaseTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;


public class GenericSteps {

    BaseTest baseTest;

    public GenericSteps(BaseTest baseTest) {

        this.baseTest = baseTest;
    }

    @Before
    public void before(Scenario s) {
       // System.out.println("***Bef*** "+s.getName());
        baseTest.initReports(s.getName());
    }

 @After
    public void after() {
        //System.out.println("***Aft***");
        baseTest.quit();
    }

}
