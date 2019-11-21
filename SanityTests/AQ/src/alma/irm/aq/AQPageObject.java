//This is not a complete page object
//page objects with following functionality
//* Add text input any text input fields
//* Select from selection input
//* Select from radio buttons
//* <todo>
package alma.irm.aq;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.TimeoutException;

public class AQPageObject {
    private boolean current_status;
    private WebDriver driver;
    private int num_of_result;
    private String url;

    public AQPageObject() {
         //implement here
        this.current_status = true;
    }

    public AQPageObject(WebDriver d, String target_url) {
        this.driver = d;
        this.num_of_result = 0;
        this.current_status = true;
        this.url = target_url;
    }

    public String getTitle()
    {
        return this.driver.getTitle();
    }

    public boolean goHome(){
		this.driver.get(this.url);
        //todo: check if there are js errors from console
        return true;
    }


    public boolean submit_search() throws Exception {
        this.driver.findElement(By.id("queryFormSubmit")).click();
        WebDriverWait wait = new WebDriverWait(driver, 60);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("queryFormSubmit")));
        }
        catch (TimeoutException e){
            return false;
        }
        WebElement element = driver.findElement(By.id("results-grid-header"));
//        System.out.println("Before Num of result: "+element.findElement(By.xpath(".//div/span[2]")).getText());
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(
                    element.findElement(By.xpath(".//div/span[2]")), "Fetching data")));
        }
        catch (TimeoutException e){
            try{
                //Support production
                wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(
                        element.findElement(By.xpath("//*[@id=\"results-grid-header\"]/div/span[8]")), "Fetching data")));
            }
            catch (TimeoutException ee) {
                return false;
            }
        }
        //Set number of result found
        this.num_of_result = Integer.parseInt(element.findElement(By.xpath(".//div/span[2]")).getText().split(" ")[1]);
        return true;
	}

	public int getNum_of_result(){
        return this.num_of_result;
    }

    public boolean reset_page() throws Exception {
        //todo: click reset button and make sure that every input selection is cleared out
        //return false; if time out
        return true;
    }

    public boolean enter_target_list(String file_path) throws Exception {
        //todo: Implement, file input
        return false;
    }

    //view_type should be a type
    public boolean select_view(String view_type) throws Exception {
        //todo: implement, radio input
        return false;
    }

    public boolean select_dropdown_with_id(String view_type) throws Exception {
        //todo: implement
        return false;
    }

    public boolean select_public_data_only() throws Exception {
        //todo: implement
        //If already selected, it will be unselected
        return false;
    }

    public boolean select_science_observations_only() throws Exception {
        //todo: implement
        //If already selected, it will be unselected
        return false;
    }

    public boolean enter_observation_date(String search_str) throws Exception {
        return this.enter_search_string_to_input_with_id("start_date", search_str);
    }

    public boolean enter_frequency(String search_str) throws Exception {
        return this.enter_search_string_to_input_with_id("frequency", search_str);
    }

    public boolean enter_publication_year(String search_str) throws Exception {
        return this.enter_search_string_to_input_with_id("publication_year", search_str);
    }

    public boolean enter_pi_name(String search_str) throws Exception {
        return this.enter_search_string_to_input_with_id("pi_name", search_str);
    }

    public boolean enter_spectral_resolution(String search_str) throws Exception {
        return this.enter_search_string_to_input_with_id("spectral_resolution", search_str);
    }

    public boolean enter_search_string_to_input_with_id(String id, String search_str) throws Exception {
        WebElement element = this.driver.findElement(By.id(id));

        Actions actions = new Actions(driver);
        actions.moveToElement(element.findElement(By.xpath("./../..")));
        actions.click();
        actions.pause(500); //<todo> find way around this
        actions.moveToElement(element);
        actions.sendKeys(search_str);
        try {
            actions.build().perform();
        } catch (Exception e) {
            System.err.println(
                    "Caught an exception while trying to build actions for enter_search_string_to_input_with_label_id(): "
                            + e.getMessage());
            throw (e);
        }
        Thread.sleep(1000);
        try{
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.attributeContains(element, "value", search_str));
        }
        catch (TimeoutException e){
            return false;
        }
        catch (Exception e){
            System.err.println(
                    "Caught an exception while trying to search for enter_search_string_to_input_with_label_id(): "
                            + e.getMessage());
            throw (e);
        }
        return true;
    }

    public boolean enter_search_string_to_input_with_label_id(String id, String search_str) throws Exception {
        WebElement element = this.driver.findElement(By.id(id));

        Actions actions = new Actions(driver);
        actions.moveToElement(element.findElement(By.xpath("./..")));
        actions.click();
        actions.pause(500); //<todo> find way around this
        actions.moveToElement(element);
        actions.sendKeys(search_str);
        try {
            actions.build().perform();
        } catch (Exception e) {
            System.err.println(
                    "Caught an exception while trying to build actions for enter_search_string_to_input_with_label_id(): "
                            + e.getMessage());
            throw (e);
        }
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try{
            wait.until(ExpectedConditions.attributeContains(
                    this.driver.findElement(By.id(element.findElement(By.xpath(".//input")).
                            getAttribute("id"))), "value", search_str));
        }
        catch (TimeoutException e){
            return false;
        }
        catch (Exception e){
            System.err.println(
                    "Caught an exception while trying to search for enter_search_string_to_input_with_label_id(): "
                            + e.getMessage());
            throw (e);
        }
        return true;
    }
}

//todo: make UI Element is part of page obj.
//Map<String, String> uiElements = new HashMap<String, <String, String>>();
//(id, section, type, secondary_id)
//private void initUiElements(){
//    //todo: implement
//
//}
//
//private class AQUiElement {
//    public String id;
//    public String section;
//    public String type;
//    public String secondary_id;
//
//    public AQUiElement(String id, String section, String type, String sec_id){
//        this.id = id;
//        this.section = section;
//        this.type = type;
//        this.secondary_id = sec_id;
//    }
//
//}
//		uiElements.put("sourcenameresolver", "Position", "text", "source_name_resolver");
//		uiElements.put("source_name_alma", "Position", "text", "");
//		uiElements.put("ra_dec", "Position", "text", "");
//		uiElements.put("galactic", "Position", "text", "");
//		uiElements.put("targetList", "Position", "file", "");
//		uiElements.put("spatial_resolution", "Position", "text", "");
//		uiElements.put("spatial_scale_max", "Position", "text", "");
//		uiElements.put("fov", "Position", "text", "");

//		uiElements.put("frequency", "Energy", "text");
//		uiElements.put("bandwidth", "Energy", "text");
//		uiElements.put("spectral_resolution", "Energy", "text");
//		uiElements.put("band_list", "Energy", "select-multiple");

//		uiElements.put("start_date", "Time", "text");
//		uiElements.put("integration_time", "Time", "text");

//		uiElements.put("polarisation_type", "Polarisation", "select-multiple");

//		uiElements.put("line_sensitivity", "Observation", "text");
//		uiElements.put("continuum_sensitivity", "Observation", "text");
//		uiElements.put("water_vapour", "Observation", "text");

//		uiElements.put("project_code", "Project", "text");
//		uiElements.put("project_title", "Project", "text");
//		uiElements.put("pi_name", "Project", "text");
//		uiElements.put("proposal_authors", "Project", "text");
//		uiElements.put("project_abstract", "Project", "text");
//		uiElements.put("publication_count", "Project", "text");
//		uiElements.put("science_keyword", "Project", "select-multiple");

//		uiElements.put("bibcode", "Publication", "text");
//		uiElements.put("pub_title", "Publication", "text");
//		uiElements.put("first_author", "Publication", "text");
//		uiElements.put("authors", "Publication", "text");
//		uiElements.put("pub_abstract", "Publication", "text");
//		uiElements.put("publication_year", "Publication", "text");

//		uiElements.put("public_data", "Options", "checkbox");
//		uiElements.put("observationView", "Options", "radio");
//		uiElements.put("projectView", "Options", "radio");
//		uiElements.put("biblioView", "Options", "radio");
//		uiElements.put("science_observations", "Options", "checkbox");




