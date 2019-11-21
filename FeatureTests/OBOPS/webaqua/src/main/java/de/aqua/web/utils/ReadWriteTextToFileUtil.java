package de.aqua.web.utils;

import org.junit.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class ReadWriteTextToFileUtil {

    public static void writeFile(String text, String fileName) {
        try {
            File statText = new File("Files/Temp/" + fileName + ".txt");
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(text);
            w.close();
        } catch (IOException e) {
            Assert.fail(String.format("Fail writing to file '%s' or file missing!", fileName));
        }
    }

    public static void writeToXmlFile(String text, String fileName) {
        try {
            File statText = new File("Files/Temp/" + fileName + ".txt");
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write(text);
            w.close();
        } catch (IOException e) {
            Assert.fail(String.format("Fail writing to file '%s' or file missing!", fileName));
        }
    }

    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("Files/Temp/" + fileName + ".txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                // sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    public static List<String> readFileAddToList(String fileName) throws IOException {
        List<String> stringList = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader("Files/Temp/" + fileName + ".txt"));
        try {
            String line = br.readLine();

            while (line != null) {
                stringList.add(line);
                line = br.readLine();
            }
            return stringList;
        } finally {
            br.close();
        }
    }

    public static List<Object[]> readFileAddToObject(String fileName) throws IOException {
        List<Object[]> stringList = new ArrayList<Object[]>();
        BufferedReader br = new BufferedReader(new FileReader("Files/Temp/" + fileName + ".txt"));
        try {
            String line = br.readLine();

            while (line != null) {
                stringList.add(new Object[]{line});
                line = br.readLine();
            }
            return stringList;
        } finally {
            br.close();
        }
    }

    public static Map<String, String> readFileAddToMap(String fileName) throws IOException {
        Map<String, String> stringMap = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(new FileReader("Files/Temp/" + fileName + ".txt"));
        try {
            String line = br.readLine();

            while (line != null) {
                String key = line.split(":")[0];
                String value = line.split(":")[1];
                stringMap.put(key, value);
                line = br.readLine();
            }
            return stringMap;
        } finally {
            br.close();
        }
    }
}

