package linyuzai.plugin.xml.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    public static File createDir(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            if (file.mkdirs())
                return file;
        }
        return null;
    }

    public static File createFile(File dir, String name) throws IOException {
        File file = new File(dir, name);
        if (file.exists()) {
            return file;
        } else {
            if (file.createNewFile())
                return file;
        }
        return null;
    }

    public static void writeFile(File file, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        writer.write(content);
        writer.close();
    }
}
