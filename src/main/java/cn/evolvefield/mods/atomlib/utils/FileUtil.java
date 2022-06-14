package cn.evolvefield.mods.atomlib.utils;


import cn.evolvefield.mods.atomlib.AtomLib;
import cn.evolvefield.mods.atomlib.Static;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/1 15:30
 * Version: 1.0
 */
public class FileUtil {

    public static Path checkFolder(Path folder) {
        if (!folder.toFile().isDirectory()) {
            try {
                return Files.createDirectories(folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return folder;
    }
    public static File checkFolder(File folder) {
        if (!folder.isDirectory()) {
            try {
                return Files.createDirectories(folder.toPath()).toFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return folder;
    }

    public static void streamResourceToDisk(URL inputUrl, File filePath) throws IOException {
        if (inputUrl == null) {
            Static.LOGGER.error("源文件夹是空的: " + filePath.toString());
        } else {
            FileUtils.copyURLToFile(inputUrl, filePath);
        }
    }


    public static File createSubFile(String fileName, File parrentFolder) {
        return new File(parrentFolder.getAbsolutePath() + "/" + fileName);
    }


}
