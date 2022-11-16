import filesystem.FilesystemApp;
import filesystem.services.CypherService;
import filesystem.utils.FileUtil;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FilesystemApp.class)
public class CypherServiceImplIntegrationTest {

    @Autowired
    private CypherService cypherService;
    @Value("${filesystem.storage-dir}")
    private String storageDir;

    @Test
    public void encryptAndDecryptExistingFile() throws Exception {
        String fileName = "/file.txt";
        File file = new File(FileUtil.buildPath(storageDir, fileName));
        String content = "very secret text\npassword";

        FileUtils.touch(file);
        FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);

        cypherService.encryptFile(fileName);
        cypherService.decryptFile(fileName);

        assertThat(FileUtils.readFileToString(file, StandardCharsets.UTF_8), equalTo(content));
    }
}
