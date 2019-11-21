package de.obopstest.web.runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;


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
        glue = {" de.obopstest.api.steps"},
        tags = {"@api"}
)
public class RestAPITestRunner {

}
