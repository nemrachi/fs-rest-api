package filesystem.controllers;

import filesystem.entities.DirResponse;
import filesystem.entities.ErrorResponse;
import filesystem.entities.FileLine;
import filesystem.entities.Status;
import filesystem.services.StorageService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("directory")
public class DirectoryController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/create")
    public DirResponse createDirectory(@RequestParam String dir) {
        try {
            storageService.createNewDir(dir);
            return new DirResponse(dir, Status.OK);
        } catch (Exception e) {
            return exceptionResponse(dir, e);
        }
    }

    @DeleteMapping("/delete")
    public DirResponse deleteFile(@RequestParam String dir) {
        try {
            storageService.deleteDir(dir);
            return new DirResponse(dir, Status.OK);
        } catch (Exception e) {
            return exceptionResponse(dir, e);
        }
    }

    @GetMapping("/content")
    public DirResponse getDirContent(@RequestParam String dir) {
        try {
            DirResponse response = new DirResponse(dir, Status.OK);
            Stream<Path> dirContent = storageService.getDirContent(dir);

            dirContent = dirContent.sorted((p1, p2) -> {
                File f1 = p1.toFile();
                File f2 = p2.toFile();
                if (FileUtils.isDirectory(f1)) {
                    if (FileUtils.isDirectory(f2)) {
                        return Long.compare(FileUtils.sizeOfDirectory(f2), FileUtils.sizeOfDirectory(f1));
                    } else {
                        return Long.compare(FileUtils.sizeOf(f2), FileUtils.sizeOfDirectory(f1));
                    }
                } else {
                    if (FileUtils.isDirectory(f2)) {
                        return Long.compare(FileUtils.sizeOfDirectory(f2), FileUtils.sizeOf(f1));
                    } else {
                        return Long.compare(FileUtils.sizeOf(f2), FileUtils.sizeOf(f1));
                    }
                }
            });

            response.setDirContent(dirContent.map(Path::toString).collect(Collectors.toList()));

            return response;
        } catch (Exception e) {
            return exceptionResponse(dir, e);
        }
    }

    @GetMapping("/pattern")
    public DirResponse getPattern(@RequestParam String dir,
                                  @RequestParam String pattern) {
        try {
            DirResponse response = new DirResponse(dir, Status.OK);
            List<FileLine> found = storageService.getPattern(dir, pattern);
            response.setFoundPattern(found);
            return response;
        } catch (Exception e) {
            return exceptionResponse(dir, e);
        }
    }

    private DirResponse exceptionResponse(String file, Exception e) {
        DirResponse response = new DirResponse(file, Status.INTERNAL_ERROR);
        response.setError(new ErrorResponse(e.getMessage(), e.getClass().getCanonicalName()));
        return response;
    }
}
