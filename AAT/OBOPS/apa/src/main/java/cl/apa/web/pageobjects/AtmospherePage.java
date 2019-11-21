package cl.apa.web.pageobjects;

import cl.apa.web.delegates.WebDriverExtended;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class AtmospherePage {

    private static List<WebElement> legendItems(WebDriverExtended driver) {
        return GeneralPage
                .visibleElements(driver, "xpath", "tr", "jqplot-table-legend");
    }

    public static WebElement legendLabel(WebDriverExtended driver, String labelName) {
        WebElement element = null;
        for (WebElement elem : legendItems(driver)) {
            if (elem.findElement(By.className("jqplot-table-legend-label"))
                    .getText().equals(labelName)) {
                element = elem;
                break;
            }
        }
        return element;
    }

    public static WebElement legendSwatch(WebDriverExtended driver, String labelName) {
        WebElement element = null;
        for (WebElement elem : legendItems(driver)) {
            if (elem.findElement(By.className("jqplot-table-legend-label")).getText()
                    .equals(labelName)) {
                element = elem.findElement(By.xpath(".//div[@class='jqplot-table-legend-swatch']"));
                break;
            }
        }
        return element;
    }

    private static List<WebElement> plotTableList(WebDriverExtended driver) {
        List<WebElement> plotTables = null;
        List<WebElement> allTables = driver.findElements(By.tagName("table"));
        for (WebElement table : allTables) {
            if (table.getAttribute("class").equals("jqplot-table-legend")) {
                plotTables.add(table);
            }
        }
        return plotTables;
    }

    public static List<String> legendColorList(WebDriverExtended driver, String legendIndex) {
        List<String> colorList = new ArrayList<>();
        List<WebElement> swatchElements = plotTableList(driver).get(Integer.parseInt(legendIndex))
                .findElements(By.className("jqplot-table-legend-swatch"));
        for (WebElement swatch : swatchElements) {
            colorList.add(swatch.getAttribute("style"));
        }
        return colorList;
    }

    public static <T> boolean hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<>();
        // Set#add returns false if the set does not change, which
        // indicates that a duplicate element has been added.
        for (T each : all) if (!set.add(each)) return true;
        return false;
    }

    public static <T> boolean areUnique(final Stream<T> stream) {
        final Set<T> seen = new HashSet<>();
        return stream.allMatch(seen::add);
    }
}
