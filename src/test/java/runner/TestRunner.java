package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features =".//Features//SignUpPage.feature",
        glue={"StepDefinitions"},
        dryRun=false,
        monochrome=true,
        plugin = {"pretty","json:target/Cucumber.json"}


)
public class TestRunner {


}