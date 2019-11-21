package alma.aat.archive.web.pageobjects;

import alma.aat.archive.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Optional;

public class RHAnonymousUserPage {

    public static WebElement headerTitle(WebDriverExtended driver) {
        return driver.findElementAndWait(By.xpath(".//*[@headertitle='ALMA Request Handler']"));
    }

    public static WebElement downloadSelected(WebDriverExtended driver) {
        return driver.findElementAndWait(By.id("downloadButton"));
    }

    public static List<WebElement> availableSBs(WebDriverExtended driver) {
        return driver.findElementsAndWait(
                By.xpath(".//*[@class='fancytree-folder fancytree-has-children fancytree-unselectable"
                        + " info fancytree-exp-c fancytree-ico-cf']"));
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
        /* Sample linkId:
        <a id="linkId_2017.1.00984.S_uid___A001_X1296_Xa62_001_of_001.tar"
        href="http://phase-a.hq.eso.org/dataportal-ARCHIVE-2018MAY/requests/anonymous/248254223/ALMA/2017.1.00984.S_uid___A001_X1296_Xa62_001_of_001.tar/2017.1.00984.S_uid___A001_X1296_Xa62_001_of_001.tar">
        2017.1.00984.S_uid___A001_X1296_Xa62_001_of_001.tar
        </a>
         */
        return driver.findElementsAndWait(
                By.xpath("//*[starts-with(@id,'linkId')]"));
    }

    public static List<WebElement> MOUSList(WebDriverExtended driver) {
        //fancytree-active fancytree-folder fancytree-has-children fancytree-lastsib fancytree-partsel fancytree-exp-cl fancytree-ico-cf
        return driver.findElementsAndWait(
                By.xpath(".//*[@class='fancytree-expanded fancytree-folder fancytree-has-children fancytree-partsel fancytree-exp-e fancytree-ico-ef']"));
    }

    public static List<WebElement> linkMemberList(WebDriverExtended driver, String prefix) {
        /* Sample linkId_member
        <a id="linkId_member.uid___A001_X1296_Xa62.NGC1808_sci.spw17.cube.I.manual.fits.tar"
        href="http://phase-a.hq.eso.org/dataportal-ARCHIVE-2018MAY/requests/anonymous/248254223/ALMA/member.uid___A001_X1296_Xa62.NGC1808_sci.spw17.cube.I.manual.fits/member.uid___A001_X1296_Xa62.NGC1808_sci.spw17.cube.I.manual.fits.tar">
        member.uid___A001_X1296_Xa62.NGC1808_sci.spw17.cube.I.manual.fits.tar
        </a>
         */
        return driver.findElementsAndWait(
                By.xpath(".//a[contains(@id,    'linkId_member.uid___" + prefix + "')]"), 90);

    }


    public static WebElement productLink(WebDriverExtended driver, String id) {

        List<WebElement> linkMembers = driver.findElementsAndWait(
                By.xpath(".//a[contains(@id,    '" + id + "')]"), 90);

        return linkMembers != null ? linkMembers.get(0) : null;
    }

    public static List<WebElement> rawList(WebDriverExtended driver) {
        return driver.findElementsAndWait(
                By.xpath(".//*[@class='fancytree-lastsib ASDM fancytree-exp-nl fancytree-ico-c']"));
    }
}
