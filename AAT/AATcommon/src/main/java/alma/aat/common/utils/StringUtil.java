package alma.aat.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StringUtil {

    private static Logger LOG = LoggerFactory.getLogger(StringUtil.class);

    public static String format(String msg, Object... objs) {
        return MessageFormatter.arrayFormat(msg, objs).getMessage();
    }

    public static String listOfStringMapsToString(List<Map<String, String>> myListOfMaps) {
        StringBuffer str = new StringBuffer();
        for (Map<String, String> map : myListOfMaps) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append("[" + entry.getKey() + " , " + entry.getValue() + "] ");
            }
            str.append("\n");
            str.append(sb);
        }
        return str.toString();
    }

    public static List<String> getListOfValuesFromMapForSpecificKeys(List<Map<String, String>> myListOfMaps, String key) {
        List<String> list = new ArrayList<String>();
        for (Map<String, String> map : myListOfMaps) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(key))
                    list.add(entry.getValue());
            }
        }
        return list;
    }

    public static List<String> getListOfValuesFromMapList(List<Map<String, String>> myListOfMaps) {
        List<String> list = new ArrayList<String>();
        for (Map<String, String> map : myListOfMaps) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

    public static String getRandomStringFromList(List<String> list) {
        String str = null;
        Random rand = new Random();
        if (!list.isEmpty()) {
            str = list.get(rand.nextInt(list.size()));
        }
        return str;
    }
}