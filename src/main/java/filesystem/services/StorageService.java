package filesystem.services;

import filesystem.entities.FileLine;
import filesystem.utils.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class StorageService {

    private final String storageDir;

    public StorageService(@Value("${filesystem.storage-dir}") String storageDir) {
        this.storageDir = storageDir;
    }

    //####################### FILE #######################

    public void createNewFile(String file) throws IOException {
        Path filePath = Paths.get(FileUtil.buildPath(storageDir, file));

        Files.createFile(filePath);
    }

    public void deleteFile(String file) throws IOException {
        Path filePath = Paths.get(FileUtil.buildPath(storageDir, file));

        if (Files.isDirectory(filePath)) {
            throw new IOException(filePath + " is a directory");
        }
        Files.delete(filePath);
    }

    public String copyFile(String src, String dest) throws IOException {
        Path srcPath = Paths.get(FileUtil.buildPath(storageDir, src));
        Path destPath = Paths.get(FileUtil.buildPath(storageDir, dest));

        destPath = destPath.resolve(srcPath.getFileName());
        Files.copy(srcPath, destPath);

        return Paths.get(storageDir).relativize(destPath).toString();
    }

    public String moveFile(String src, String dest) throws IOException {
        Path srcPath = Paths.get(FileUtil.buildPath(storageDir, src));
        Path destPath = Paths.get(FileUtil.buildPath(storageDir, dest));

        destPath = destPath.resolve(srcPath.getFileName());
        Files.move(srcPath, destPath);

        return Paths.get(storageDir).relativize(destPath).toString();
    }

    public String getFileContent(String file) throws IOException {
        Path filePath = Paths.get(FileUtil.buildPath(storageDir, file));

        return Files.readString(filePath);
    }

    //####################### DIRECTORY #######################

    public void createNewDir(String dir) throws IOException {
        Path dirPath = Paths.get(FileUtil.buildPath(storageDir, dir));

        Files.createDirectory(dirPath);
    }

    public void deleteDir(String dir) throws IOException {
        Path dirPath = Paths.get(FileUtil.buildPath(storageDir, dir));

        if (Files.isRegularFile(dirPath)) {
            throw new IOException(dirPath + " is a file");
        }
        Files.delete(dirPath);
    }

    public Stream<Path> getDirContent(String dir) throws IOException {
        Path dirPath = Paths.get(FileUtil.buildPath(storageDir, dir));

        return Files.list(dirPath);
    }

    public List<FileLine> getPattern(String dir, String patternString) throws IOException {
        Path dirPath = Paths.get(FileUtil.buildPath(storageDir, dir));
        List<FileLine> found = new ArrayList<>();
        List<String> content;
        Pattern pattern = Pattern.compile(patternString);
        int lineNum;

        for (Path filePath : Files.list(dirPath).toList()) {
            lineNum = 1;
            if (Files.isRegularFile(filePath)) {
                content = Files.readAllLines(filePath);
                for (String line : content) {
                    if (pattern.matcher(line).find()) {
                        found.add(new FileLine(filePath.toString(), lineNum));
                    }
                    lineNum++;
                }
            }
        }

        return found;
    }
}
