package alma.aat.archive.web.utils;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class FileUtil {

    public static Logger LOG = LoggerFactory.getLogger(FileUtil.class);


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


    public static String readFileToString(String filename) {
        try {
            return FileUtils.readFileToString(new File(filename), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Could not find file " + filename);
        }
    }

    public static List<String> readFileAsList(String filename) {
        try {
            return FileUtils.readLines(new File(filename), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Could not find file " + filename);
        }
    }

    public static void writeToFile(String filename, String content) {
        try {
            FileUtils.writeStringToFile(new File(filename), content, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Could not find file " + filename);
        }
    }

    public static void writeToFile(String filename, List<String> contentAsLines) {
        try {
            FileUtils.writeLines(new File(filename), "UTF-8", contentAsLines);
        } catch (IOException e) {
            throw new RuntimeException("Could not find file " + filename);
        }
    }

    public static void copyFile(File source, File dest) {
        try {
            Files.copy(source.toPath(), dest.toPath());
        } catch (IOException e) {
            throw new RuntimeException(String
                    .format("The source file '%s' or the destination path '%s' couldn't be fount!",
                            source, dest));
        }
    }


    public static long getFileSize(String fileName) {
        File file = new File(fileName);
        return file.exists() ? file.length() : 0;

    }

    public static String getContent(String file) {
        String content = "";

        try {
            content = new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            LOG.error("Unable to read file '" + file + "'");
            e.printStackTrace();
        }
        return content;

    }
}

