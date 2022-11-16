import filesystem.FilesystemApp;
import filesystem.entities.FileLine;
import filesystem.services.StorageService;
import filesystem.utils.FileUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FilesystemApp.class)
public class StorageServiceDirectoryImplIntegrationTest {

    @Autowired
    private StorageService storageService;
    @Value("${filesystem.storage-dir}")
    private String storageDir;

    @Test
    public void createNewDirectory() throws IOException {
        String dirName = "/newDir";
        File dir = new File(FileUtil.buildPath(storageDir, dirName));

        if (dir.exists()) {
            FileUtils.forceDelete(dir);
        }

        storageService.createNewDir(dirName);
        assertThat(dir.exists(), equalTo(true));
    }

    @Test
    public void deleteExistingDirectory() throws IOException {
        String dirName = "/dir";
        File dir = new File(FileUtil.buildPath(storageDir, dirName));

        if (!dir.exists()) {
            FileUtils.forceMkdir(dir);
        }

        storageService.deleteDir(dirName);
        assertThat(dir.exists(), equalTo(false));
    }

    @Test
    public void getContentOfExistingDir() throws IOException {
        String dirName = "/dir";
        File dir = new File(FileUtil.buildPath(storageDir, dirName));
        List<String> expectedContent = Arrays.asList(
                Paths.get(FileUtil.buildPath(storageDir, dirName, "/dir1")).toString(),
                Paths.get(FileUtil.buildPath(storageDir, dirName, "file1.txt")).toString(),
                Paths.get(FileUtil.buildPath(storageDir, dirName, "file2.txt")).toString()
        );

        if (!dir.exists()) {
            FileUtils.forceMkdir(dir);
        } else {
            FileUtils.forceDelete(dir);
            FileUtils.forceMkdir(dir);
        }

        FileUtils.touch(new File(FileUtil.buildPath(storageDir, dirName, "file1.txt")));
        FileUtils.touch(new File(FileUtil.buildPath(storageDir, dirName, "file2.txt")));
        FileUtils.forceMkdir(new File(FileUtil.buildPath(storageDir, dirName, "/dir1")));

        Stream<Path> dirContent = storageService.getDirContent(dirName);
        assertThat(dirContent.map(Path::toString).collect(Collectors.toList()), equalTo(expectedContent));
    }

    @Test
    public void getExistingPatternFromExistingFile() throws IOException {
        String dirName = "/dir";
        File dir = new File(FileUtil.buildPath(storageDir, dirName));
        String pattern = "hello";

        if (!dir.exists()) {
            FileUtils.forceMkdir(dir);
        } else {
            FileUtils.forceDelete(dir);
            FileUtils.forceMkdir(dir);
        }

        File file = new File(FileUtil.buildPath(storageDir, dirName, "file.txt"));
        FileUtils.touch(file);
        FileUtils.writeStringToFile(file, "hello world\nUpper Case Hello\nWord hello is there.", StandardCharsets.UTF_8);

        List<FileLine> expected = new ArrayList<>();
        String path = Paths.get(file.toString()).toString();
        expected.add(new FileLine(path, 1));
        expected.add(new FileLine(path, 3));

        List<FileLine> found = storageService.getPattern(dirName, pattern);
        assertThat(found, equalTo(expected));
    }
}
