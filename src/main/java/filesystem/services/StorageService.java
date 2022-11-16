package filesystem.services;

import filesystem.utils.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

@Service
public class StorageService {

    private final String storageDir;

    public StorageService(@Value("${filesystem.storage-dir}") String storageDir) {
        this.storageDir = storageDir;
    }

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
}
