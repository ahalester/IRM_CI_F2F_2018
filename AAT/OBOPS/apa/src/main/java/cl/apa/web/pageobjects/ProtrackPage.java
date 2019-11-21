package cl.apa.web.pageobjects;

import cl.apa.web.delegates.WebDriverExtended;
import jline.internal.Nullable;
import org.openqa.selenium.WebElement;

import java.util.List;

import static cl.apa.web.utils.RuntimeUtil.bufferMap;

/**
 * Created by bdumitru on 8/16/2017.
 */
@SuppressWarnings("unused")
public class ProtrackPage {

    public static Double sumEF(WebDriverExtended driver, String columnName) {
        Double sum = 0.0;
        List<WebElement> items = GeneralPage.collectGridColumnElements(driver, columnName);
        bufferMap.put("N_EB", String.valueOf(items.size()));
        for (WebElement item : items) {
            sum += Double.parseDouble(item.getAttribute("innerHTML"));
        }
        return sum;
    }

    @Nullable
    public static WebElement parentMOUS(WebDriverExtended driver, String sbName) {
        WebElement we = null;
        List<WebElement> treeCellItems = GeneralPage.visibleElements(driver, "z-treecell-text");
        for (int i = 0; i < treeCellItems.size(); i++) {
            if (treeCellItems.get(i).getAttribute("innerHTML").contains(sbName)) {
                we = treeCellItems.get(i - 1);
                break;
            }
        }
        return we;
    }

    public static WebElement selectMOUSState(WebDriverExtended driver, String stateName) {
        WebElement we = null;
        List<WebElement> stateList = GeneralPage.visibleElements(driver, "z-menuitem");
        for (WebElement state : stateList) {
            if (state.getAttribute("innerHTML").contains(stateName)) {
                we = state;
                break;
            }
        }
        return we;
    }
}
