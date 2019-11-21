package cl.apa.web.utils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;

/**
 * .
 * Class able to handle "Authentication required" pop-up by key press simulations
 */
public class SmartRobotUtil extends Robot {

    public SmartRobotUtil() throws AWTException {
        super();
    }

    public void pasteClipboard() {
        keyPress(KeyEvent.VK_CONTROL);
        keyPress(KeyEvent.VK_V);
        delay(50);
        keyRelease(KeyEvent.VK_V);
        keyRelease(KeyEvent.VK_CONTROL);
    }

    public void type(String text) {
        writeToClipboard(text);
        pasteClipboard();
    }

    private void writeToClipboard(String stringToCopy) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(stringToCopy);
        clipboard.setContents(transferable, null);
    }
}
