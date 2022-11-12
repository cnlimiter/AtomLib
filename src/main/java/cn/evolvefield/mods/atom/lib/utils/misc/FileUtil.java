package cn.evolvefield.mods.atom.lib.utils.misc;


import cn.evolvefield.mods.atom.lib.AtomLib;
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



}
