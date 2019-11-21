package de.aqua.web.utils;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static de.aqua.web.utils.ReadWriteTextToFileUtil.readFile;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class StringUtil {

    private static Logger LOG = LoggerFactory.getLogger(StringUtil.class);

    public static String truncateToLength(String input, int size) {
        String ret = input;
        if (size != -1 && ret != null && ret.length() > size) {
            ret = ret.substring(0, size);
        }
        return ret;
    }

    public static List<String> split(String text) {
        return split(text, ",");
    }

    public static List<String> split(String text, String separator) {
        List<String> tokens = new ArrayList<String>();
        if (isNotBlank(text)) {
            String[] splitedTokens = text.split(separator);
            for (String token : splitedTokens) {
                if (isNotBlank(token)) {
                    tokens.add(token.trim());
                }
            }
        }
        if (tokens.isEmpty()) {
            tokens.add("");
        }
        return tokens;
    }

    public static double parseDouble(String value, double defaultValue) {
        double ret;
        if (value != null) {
            try {
                ret = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                ret = defaultValue;
            }
        } else {
            ret = defaultValue;
        }
        return ret;
    }

    public static int generateRandomNo(int max) {
        return new Random().nextInt(max);
    }

    public static String generateTimeStamp() {
        return String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public static String extractStringBeforeSlash(String mainString) {
        return mainString.substring(0, mainString.indexOf("/"));
    }

    public static String extractStringAfterSlash(String mainString) {
        return extractStringAfterSeparator(mainString, "/");
    }

    public static String extractStringBeforeSeparator(String mainString, String separator) {
        return mainString.substring(0, mainString.indexOf(separator));
    }

    public static String extractStringAfterSeparator(String mainString, String separator) {
        return mainString.substring(mainString.indexOf(separator) + 1, mainString.length());
    }

    public static String manipulateNumber(String initialNo, String additionalNo, String mathOperation) {
        String result = "";
        switch (mathOperation.toLowerCase()) {
            case "/":
                result = String.valueOf(Integer.parseInt(initialNo) / Integer.parseInt(additionalNo));
                break;
            case "%":
                result = String.valueOf(Integer.parseInt(initialNo) % Integer.parseInt(additionalNo));
                break;
            case "*":
                result = String.valueOf(Integer.parseInt(initialNo) * Integer.parseInt(additionalNo));
                break;
            case "+":
                result = String.valueOf(Integer.parseInt(initialNo) + Integer.parseInt(additionalNo));
                break;
            default:
                break;
        }
        return result;
    }

    public static String removeEmptySpaces(String actualString) {
        return actualString.replaceAll(" ", "").replaceAll("[\\s\\u00A0]+$", "").replace("\n", "").replace("\r", "")
                .replace(" Peninsula", "Peninsula");
    }

    public static String replaceStrings(String mainString, String toBeReplaced, String replaceWith) {
        return mainString.replace(toBeReplaced, replaceWith);
    }

    public static String ipChange(String mainString) {
        return mainString.replace(".", "_");
    }

    public static List<String> getValueFromMapByKey(Map<String, String> response, String[] mapKey) {
        List<String> mapValues = new ArrayList<String>();
        for (Map.Entry<String, String> entry : response.entrySet()) {
            for (int i = 0; i < mapKey.length; i++) {
                if (entry.getKey().equals(mapKey[i])) {
                    mapValues.add(entry.getValue());
                    break;
                }
            }
        }
        return mapValues;
    }

    public static String extractMapValue(Map<String, String> response, String mapKey) {
        String value = null;
        for (Map.Entry<String, String> entry : response.entrySet()) {
            if (entry.getKey().equals(mapKey)) {
                value = entry.getValue();
                break;
            }
        }
        return value;
    }

    public static Integer extractIntMapValue(Map<String, Integer> response, String mapKey) {
        Integer value = 0;
        for (Map.Entry<String, Integer> entry : response.entrySet()) {
            if (entry.getKey().equals(mapKey)) {
                value = entry.getValue();
                break;
            }
        }
        return value;
    }

    public static String removeHttpAndPort(String url) {
        url = url.replace("http://", "");
        url = url.substring(0, url.indexOf(":"));
        return url;
    }

    public static String absoluteSshCmd(String sshCmd, String relativeCmd) {
        String absoluteCmd = "";
        try {
            if (readFile(relativeCmd).contains("http")) {
                absoluteCmd = sshCmd + removeHttpAndPort(readFile(relativeCmd));
            } else
                absoluteCmd = sshCmd + readFile(relativeCmd);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return absoluteCmd;
    }

    public static String removeGenericsFromSshCmd(String SshCmd, String gatewayMachine, String innerMachine,
                                                  String innerCmd) {
        try {
            innerMachine = removeHttpAndPort(readFile(innerMachine));
            SshCmd = SshCmd.replace("<gateway_machine>", readFile(gatewayMachine));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SshCmd = SshCmd.replace("<inner_machine>", innerMachine);
        SshCmd = SshCmd.replace("<inner_interactive_command>", innerCmd);
        return SshCmd;
    }

    public static String fillSystemRegisterCmdWithParams(String SshCmd, String endpointIp, String endpointPass,
                                                         String serverIp, String apiKey) {
        SshCmd = SshCmd.replace("<provide_endpoint_ip>", endpointIp);
        SshCmd = SshCmd.replace("<provide_password>", endpointPass);
        try {
            SshCmd = SshCmd.replace("<provide_SC_server_ip>", readFile(serverIp));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            SshCmd = SshCmd.replace("<provide_SC_api_key>", readFile(apiKey));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SshCmd;
    }

    public static String fillFileDownloadCmdWithParams(String SshCmd, String fileName, String serverIp, String systemId,
                                                       String apiKey) {
        SshCmd = SshCmd.replace("<provide_file_name>", fileName);

        try {
            SshCmd = SshCmd.replace("<provide_SC_server_ip>", readFile(serverIp));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            SshCmd = SshCmd.replace("<generated_system_id>", readFile(systemId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            SshCmd = SshCmd.replace("<provide_SC_api_key>", readFile(apiKey));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SshCmd;
    }

    /**
     * Returns a String of ,custom delimiter, separated values form a List
     * <String> collection
     *
     * @param list  the list of String elements (List<String>) to be joined in a
     *              String
     * @param delim the delimiter
     * @return String containing joined elements
     */
    public static String stringListToString(List<String> list, String delim) {
        StringBuilder sb = new StringBuilder();

        String loopDelim = "";

        for (String s : list) {

            sb.append(loopDelim);
            sb.append(s);

            loopDelim = delim;
        }
        return sb.toString();
    }

    public static String customLoggerTimestamp(long timeInMillis) {
        return new SimpleDateFormat("ddMMyyHHmmss").format(new Date(timeInMillis));
    }

    public static String getLastTestResult(String fileName) {
        String fileContent = null;
        try {
            fileContent = readFile(fileName);
        } catch (IOException e) {
            Assert.fail("Missing file !");
        }
        return split(fileContent, "#").get(1);
    }

    public static boolean equalMaps(Map<String, String> firstMap, Map<String, String> secondMap,
                                    Map<String, String> err) {
        int counter = 0;
        if (firstMap.size() != secondMap.size()) {
            return false;
        }
        for (String key : firstMap.keySet()) {
            if (!firstMap.get(key).equals(secondMap.get(key))) {
                err.put("baseline", key + ":" + firstMap.get(key));
                err.put("actual", key + ":" + secondMap.get(key));
                if (counter == 0) {
                    LOG.info("Inconsistencies:");
                }
                LOG.info("expected: " + key + "=" + firstMap.get(key));
                LOG.info("actual: " + key + "=" + secondMap.get(key));
                counter++;
                return false;
            }
        }
        return true;
    }

    public static int occurrencesCount(String mainString, String subString) {
        int count = 0;
        Pattern pattern = Pattern.compile(subString);
        Matcher matcher = pattern.matcher(mainString);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static String splitStringBySeparator(String mainString, String separator) {
        String[] strArray = mainString.split(separator);
        String buffer = mainString;
        buffer = strArray[strArray.length - 1];
        return buffer;
    }

    public static int lastIndexOfSubstring(String stringBuilder) {
        String str = stringBuilder;
        String findStr = "processed: ";
        int index = 0;
        int lastIndex = 0;

        while (index != -1) {

            index = str.indexOf(findStr, index);
            if (index != -1) {
                lastIndex = index;
                LOG.info("Last index of the current timezone within the string builder is: " + lastIndex);
            }

            if (index != -1) {
                index += findStr.length();
                if (index != -1) {
                    // lastIndex = index;
                    // LOG.info(lastIndex);
                }
            }
        }
        return lastIndex;
    }

    public static String RgbToHex(WebElement we, String rgbValues) {
        String[] numbers = rgbValues.replace("rgba(", "").replace(")", "").split(",");
        int r = Integer.parseInt(numbers[0].trim());
        int g = Integer.parseInt(numbers[1].trim());
        int b = Integer.parseInt(numbers[2].trim());
        LOG.info("The RGB color code for the web element " + we + " is r: " + r + "g: " + g + "b: " + b);
        String hex = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
        LOG.info("The HEX color code for the web element " + we + " is: " + hex);
        return hex;
    }

    public static String currentMonth() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(timestamp.getTime());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM/dd/yyy");

        return extractStringBeforeSlash(simpleDateFormat.format(date));
    }

    public static String extractMonthOrYearFromCurrentDate(String monthOrYear) {
        String value = "";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(timestamp.getTime());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
        if (monthOrYear.equalsIgnoreCase("MM"))
            value = extractStringBeforeSlash(simpleDateFormat.format(date));
        if (monthOrYear.equalsIgnoreCase("YYYY") || monthOrYear.equalsIgnoreCase("YYY")
                || monthOrYear.equalsIgnoreCase("YY"))
            value = extractStringAfterSlash(simpleDateFormat.format(date));
        return value;
    }

    public static String updateDate(String date) {
        String currMonth = StringUtil.extractMonthOrYearFromCurrentDate("MM");
        String currYear = StringUtil.extractMonthOrYearFromCurrentDate("YYYY");
        String nextMonth;
        String year;
        int intMonth = Integer.parseInt(currMonth);
        int intYear = Integer.parseInt(currYear);
        if (intMonth < 10) {
            nextMonth = "0" + String.valueOf(intMonth + 1);
            date = date.replace("MM", nextMonth).replace("YYYY", currYear);
        }
        if (intMonth >= 10 && intMonth < 12) {
            nextMonth = String.valueOf(intMonth + 1);
            date = date.replace("MM", nextMonth).replace("YYYY", currYear);
        }
        if (intMonth == 12) {
            year = String.valueOf(intYear + 1);
            date = date.replace("MM", "01").replace("YYYY", year);
        }
        return date;
    }

    public static String extractSubstringUntilIndexOf(String mainString, String indexOf) {
        return mainString.substring(0, mainString.indexOf(indexOf));
    }

    public static String extractLanguageFromUrl(String url) {
        return url.substring(url.indexOf("language="), url.indexOf("language=") + "language=".length() + 2)
                .replace("language=", "");
    }

    public static String convertDateFormat(String dateToConvert) {
        return dateToConvert.replaceAll("(\\d+)/(\\d+)/(\\d+)", "$3/$2/$1").replace("/", "-");
    }

    public static String updateDatesInUrl(String url) {
        if (url.contains("arrival_date") && url.contains("departure_date")) {
            String arrivalDate = url.substring(url.indexOf("arrival_date="), url.indexOf("arrival_date=")
                    + "arrival_date=".length() + 10);
            url = url.replace(arrivalDate, updateDate(arrivalDate));
            String departureDate = url.substring(url.indexOf("departure_date="), url.indexOf("departure_date=")
                    + "departure_date=".length() + 10);
            url = url.replace(departureDate, updateDate(departureDate));
        }
        return url;
    }

}

