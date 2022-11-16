import filesystem.FilesystemApp;
import filesystem.services.StorageService;
import filesystem.utils.FileUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FilesystemApp.class)
public class StorageServiceFileImplIntegrationTest {

    @Autowired
    private StorageService storageService;
    @Value("${filesystem.storage-dir}")
    private String storageDir;

    @Test
    public void createNewFile() throws IOException {
        String fileName = "newFile.txt";
        File file = new File(FileUtil.buildPath(storageDir, fileName));

        if (file.exists()) {
            FileUtils.delete(file);
        }

        storageService.createNewFile(fileName);
        assertThat(file.exists(), equalTo(true));
    }

    @Test
    public void deleteExistingFile() throws IOException {
        String fileName = "file.txt";
        File file = new File(FileUtil.buildPath(storageDir, fileName));

        if (!file.exists()) {
            FileUtils.touch(file);
        }

        storageService.deleteFile(fileName);
        assertThat(file.exists(), equalTo(false));
    }

    @Test
    public void copyExistingFileToExistingDir() throws IOException {
        String fileName = "file.txt";
        String dirName = "/dir";
        File file = new File(FileUtil.buildPath(storageDir, fileName));
        File dir = new File(FileUtil.buildPath(storageDir, dirName));

        if (!dir.exists()) {
            FileUtils.forceMkdir(dir);
        } else {
            FileUtils.forceDelete(dir);
            FileUtils.forceMkdir(dir);
        }
        if (!file.exists()) {
            FileUtils.touch(file);
        }

        storageService.copyFile(fileName, dirName);
        file = new File(FileUtil.buildPath(storageDir, dirName, fileName));
        assertThat(file.exists(), equalTo(true));
    }

    @Test
    public void moveExistingFileToExistingDir() throws IOException {
        String fileName = "file.txt";
        String dirName = "/dir";
        File file = new File(FileUtil.buildPath(storageDir, fileName));
        File dir = new File(FileUtil.buildPath(storageDir, dirName));

        if (!dir.exists()) {
            FileUtils.forceMkdir(dir);
        } else {
            FileUtils.forceDelete(dir);
            FileUtils.forceMkdir(dir);
        }
        if (!file.exists()) {
            FileUtils.touch(file);
        }

        storageService.moveFile(fileName, dirName);
        file = new File(FileUtil.buildPath(storageDir, dirName, fileName));
        assertThat(file.exists(), equalTo(true));
    }

    @Test
    public void getExistingFileContent() throws IOException {
        String content = "hello Test from Spring Boot.";
        String fileName = "file.txt";
        File file = new File(FileUtil.buildPath(storageDir, fileName));

        if (!file.exists()) {
            FileUtils.touch(file);
        }
        FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);

        assertThat(storageService.getFileContent(fileName), equalTo(content));

    }
}
