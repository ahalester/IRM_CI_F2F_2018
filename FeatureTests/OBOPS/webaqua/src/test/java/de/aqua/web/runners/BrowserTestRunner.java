package de.aqua.web.runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by bdumitru on 7/12/2017.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "html:target/cucumber-html-report",
                "json:target/cucumber-report.json",
                "pretty:target/cucumber-pretty.txt",
                "usage:target/cucumber-usage.json",
                "junit:target/cucumber-results.xml"
        },
        features = {"src/test/resources/features"},
        tags = {"@smoke"},
        glue = {"de.aqua.web.browser"}
)
public class BrowserTestRunner {

}
