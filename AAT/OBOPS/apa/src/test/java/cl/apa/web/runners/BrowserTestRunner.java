package cl.apa.web.runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by bdumitru on 7/12/2017.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "html:target/cucumber-html-report",
                "json:target/cucumber-report.json",
                "pretty:target/cucumber-pretty.txt",
                "usage:target/cucumber-usage.json",
                "junit:target/cucumber-results.xml"
        },
        features = {"src/test/resources/features"},
        glue = {"cl.apa.web.browser"},
        tags = {"@smoke"}
)
public class BrowserTestRunner {

}
