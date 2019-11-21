package alma.aat.archive.web.pageobjects;

import alma.aat.archive.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class RHRequestPage {

    public static WebElement headerTitle(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath(".//*[@headertitle='ALMA Request Handler']"));
    }

    public static WebElement downloadSelectedButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath(".//*[@id='downloadButton']"));
    }

    public static WebElement downloadScriptButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath(".//*[@id='downloadScriptButton']"));
    }

    public static WebElement fileListButton(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath(".//*[@id='downloadFileListButton']"));
    }


    public static WebElement requestNumber(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//*[@id=\"content\"]/h1/span[1]"));
    }

    public static WebElement requestCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//*[@id=\"tree\"]/tbody/tr[1]/td[1]/span/span[2]"));
    }

    public static WebElement requestNumberTreeNode(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("//*[@id=\"tree\"]/tbody/tr[1]/td[1]/span/span[4]/span[2]"));
    }

    public static List<WebElement> SBRows(WebDriverExtended driver) {
        return driver.findElements(By.tagName("tr")).stream().filter((WebElement row) -> {
                    List<WebElement> spans = row.findElements(By.tagName("span"));
                    return !spans.stream().filter(span -> span.getText().contains("SB") || span.getText().contains("Source")).collect(Collectors.toList()).isEmpty();
                }
        ).collect(Collectors.toList());
    }


    public static List<Float> sizes(WebDriverExtended driver) {
        // Get all td containing a size (inner text containing MB, GM, TB) and then convert the values to long
        List<WebElement> sizeCells = driver.findElements(By.xpath(".//td[(contains(.,'GB') or contains(.,'MB') or contains(.,'TB')) and not(contains(.,'Total:'))]"));
        return sizeCells.stream().map(element -> toMB(element.getText()))
                .collect(Collectors.toList());
    }

    private static Float toMB(String size) {
        return size.contains("TB") ? Float.parseFloat(size.replace("TB", "").replace("Total:", "")) * 1024 * 1024 :
                size.contains("GB") ? Float.parseFloat(size.replace("GB", "").replace("Total:", "")) * 1024 :
                        Float.parseFloat(size.replace("MB", "").replace("Total:", ""));

    }

    public static Float totalSize(WebDriverExtended driver) {
        List<WebElement> sizeCells = driver.findElements(By.xpath(".//td[contains(.,'Total:')]"));
        return sizeCells.stream().map(element -> toMB(element.getText()))
                .collect(Collectors.toList()).get(0);
    }

    public static WebElement requestRows(WebDriverExtended driver) {
        return driver.findElements(By.tagName("tr")).get(0);
    }

    public static List<WebElement> productRows(WebDriverExtended driver) {
        return getRowByType(driver, "product");
    }

    public static WebElement firstProductMemberCheckbox(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("/html/body/div[7]/div[2]/form/table/tbody/tr[9]/td[1]/span/span[2]"));
    }

    public static WebElement firstProductExpander(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath("/html/body/div[7]/div[2]/form/table/tbody/tr[8]/td[1]/span/span[1]"));
    }

     public static List<WebElement> auxiliaryRows(WebDriverExtended driver) {
        return getRowByType(driver, "auxiliary");
    }

    public static List<WebElement> readmeRows(WebDriverExtended driver) {
        return getRowByType(driver, "readme");
    }

    private static List<WebElement> getRowByType(WebDriverExtended driver, String type) {
        List<WebElement> rows = driver.findElements(By.tagName("tr"));
        return rows.stream().filter((WebElement row) -> {
                    List<WebElement> spans = row.findElements(By.tagName("span"));
                    return (spans != null && spans.size() == 6 && spans.get(5).getText().equals(type));
                }
        ).collect(Collectors.toList());
    }



    public static List<WebElement> availableSBs(WebDriverExtended driver) {
        return driver.findElementsAndWait(
                By.xpath(".//*[@class='fancytree-folder fancytree-has-children fancytree-unselectable"
                        + " info fancytree-exp-c fancytree-ico-cf']"));
    }

    public static List<WebElement> unselectableSBs(WebDriverExtended driver) {
        return driver.findElementsAndWait(
                By.xpath(".//*[@class='fancytree-folder fancytree-has-children fancytree-unselectable"
                        + " info fancytree-exp-c fancytree-ico-cf']"));
    }

    public static List<WebElement> auxiliaries(WebDriverExtended driver) {
        return driver.findElementsAndWait(
                By.xpath(".//*[@class='fancytree-folder fancytree-has-children fancytree-unselectable"
                        + " info fancytree-exp-c fancytree-ico-cf']"));
    }


    public static List<WebElement> expanders(WebDriverExtended driver) {
        return driver.findElementsAndWait(By.className("fancytree-expander"));
    }

    public static List<WebElement> checkboxes(WebDriverExtended driver) {
        return driver.findElementsAndWait(By.className("fancytree-checkbox"));
    }

    public static WebElement expander(WebElement we) {
        return we.findElement(By.className("fancytree-expander"));
    }


    public static WebElement uid(WebElement we) {
        return we.findElement(By.className("uid"));
    }

    public static WebElement type(WebElement we) {
        return we.findElement(By.className("type"));
    }

    public static WebElement link(WebElement we) {
        return we.findElement(By.xpath("//*[starts-with(@id,'linkId')]"));
    }

    public static List<WebElement> sources(WebDriverExtended driver) {
        return driver.findElementsAndWait(
                By.xpath(".//*[@class='fancytree-lastsib fancytree-unselectable info fancytree-exp-nl"
                        + " fancytree-ico-c']"), 50);
    }

    public static List<WebElement> productList(WebDriverExtended driver) {
        return driver.findElementsAndWait(
                By.xpath(".//*[@class='fancytree-partsel fancytree-selected PIPELINE_PRODUCT fancytree-exp-n fancytree-ico-c']"));
    }


    public static List<WebElement> linkList(WebDriverExtended driver) {
        return driver.findElementsAndWait(
                By.xpath("//*[starts-with(@id,'linkId')]"));
    }

    public static List<WebElement> memberLinkList(WebDriverExtended driver) {
        return driver.findElementsAndWait(
                By.xpath("//*[starts-with(@id,'linkId_member')]"));
    }

    public static List<WebElement> memberSpanList(WebDriverExtended driver) {
        return driver.findElementsAndWait(By.xpath(".//span[contains(.,'member')]"));
    }

    public static List<WebElement> MOUSList(WebDriverExtended driver) {
        return driver.findElementsAndWait(
                By.xpath(".//*[@class='fancytree-expanded fancytree-folder fancytree-has-children fancytree-partsel fancytree-exp-e fancytree-ico-ef']"));
    }

    public static List<WebElement> linkMemberList(WebDriverExtended driver, String prefix) {
        return driver.findElementsAndWait(
                By.xpath(".//a[contains(@id,    'linkId_member.uid___" + prefix + "')]"), 90);
    }

    public static List<WebElement> rawList(WebDriverExtended driver) {
        return driver.findElementsAndWait(
                By.xpath(".//*[@class='fancytree-lastsib ASDM fancytree-exp-nl fancytree-ico-c']"));
    }







    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(Float.parseFloat("76.0"));
    }

}
