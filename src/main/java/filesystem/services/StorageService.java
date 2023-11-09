package filesystem.services;

import filesystem.aosd.PatternPointcut;
import filesystem.entities.FileLine;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class StorageService {

    public StorageService() { }

    //####################### FILE #######################

    public void createNewFile(String file) throws IOException {
        Files.createFile(Path.of(file));
    }

    public void deleteFile(String file) throws IOException {
        Path path = Path.of(file);
        if (Files.isDirectory(path)) {
            throw new IOException(file + " is a directory");
        }
        Files.delete(path);
    }

    public String copyFile(String src, String dest) throws IOException {
        Path srcPath = Path.of(src);
        Path destPath = Path.of(dest);

        destPath = destPath.resolve(srcPath.getFileName());
        Files.copy(srcPath, destPath);

        return dest;
    }

    public String moveFile(String src, String dest) throws IOException {
        Path srcPath = Path.of(src);
        Path destPath = Path.of(dest);

        destPath = destPath.resolve(srcPath.getFileName());
        Files.move(srcPath, destPath);

        return dest;
    }

    public String getFileContent(String file) throws IOException {
        return Files.readString(Path.of(file));
    }

    //####################### DIRECTORY #######################

    public void createNewDir(String dir) throws IOException {
        Files.createDirectory(Path.of(dir));
    }

    public void deleteDir(String dir) throws IOException {
        Path dirPath = Path.of(dir);

        if (Files.isRegularFile(dirPath)) {
            throw new IOException(dirPath + " is a file");
        }
        Files.walk(dirPath)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public Stream<Path> getDirContent(String dir) throws IOException {
        return Files.list(Path.of(dir));
    }

    @PatternPointcut
    public List<FileLine> getPattern(String dir, String patternString) throws IOException {
        Path dirPath = Path.of(dir);
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
