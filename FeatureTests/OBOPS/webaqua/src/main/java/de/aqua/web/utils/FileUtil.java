package de.aqua.web.utils;

import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.io.IOUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class FileUtil {

//    public static String readStreamToString(String resourceName) {
//        try {
//            InputStream resourceAsStream = FileUtil.class.getClassLoader().getResourceAsStream(resourceName);
//            if (resourceAsStream != null) {
//                return IOUtils.readFully(resourceAsStream);
//            }
//            throw new RuntimeException("Could not find resource " + resourceName);
//        } catch (IOException e) {
//            throw new RuntimeException("Could not read stream " + resourceName);
//        }
//    }
//
//    public static String readStreamToStringLine(String resourceName) {
//        return readStreamToString(resourceName).replaceAll("(\\r|\\n|\\t)", "");
//    }

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
                    .format("The source file '%s' or the destination path '%s' couldn't be fount!", source, dest));
        }
    }

}

