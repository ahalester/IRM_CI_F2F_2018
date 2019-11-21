package de.project.web.utils;

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
            File statText = new File("src/test/resources/tmp/" + fileName + ".txt");
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer wr = new BufferedWriter(osw);
            wr.write(text);
            wr.close();
        } catch (IOException e) {
            Assert.fail(String.format("Fail writing to file '%s' or file missing!", fileName));
        }
    }

    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/test/resources/tmp/"
                + fileName + ".txt"));
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

    public static void writeToXmlFile(String text, String fileName) {
        try {
            File statText = new File("Files/Temp/" + fileName + ".txt");
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer writer = new BufferedWriter(osw);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            Assert.fail(String.format("Fail writing to file '%s' or file missing!", fileName));
        }
    }

    public static List<String> readFileAddToList(String fileName) throws IOException {
        List<String> stringList = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader("Files/Temp/"
                + fileName + ".txt"))) {
            String line = br.readLine();

            while (line != null) {
                stringList.add(line);
                line = br.readLine();
            }
            return stringList;
        }
    }

    public static List<Object[]> readFileAddToObject(String fileName) throws IOException {
        List<Object[]> stringList = new ArrayList<Object[]>();
        try (BufferedReader br = new BufferedReader(new FileReader("Files/Temp/"
                + fileName + ".txt"))) {
            String line = br.readLine();

            while (line != null) {
                stringList.add(new Object[]{line});
                line = br.readLine();
            }
            return stringList;
        }
    }

    public static HashMap readFileAddToMap(String fileName) throws IOException {
        HashMap stringMap = new HashMap();
        try (BufferedReader br = new BufferedReader(new FileReader("Files/Temp/"
                + fileName + ".txt"))) {
            String line = br.readLine();

            while (line != null) {
                String key = line.split(":")[0];
                String value = line.split(":")[1];
                stringMap.put(key, value);
                line = br.readLine();
            }
            return stringMap;
        }
    }
}

