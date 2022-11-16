package filesystem.controllers;

import filesystem.entities.ErrorResponse;
import filesystem.entities.Status;
import filesystem.services.StorageService;
import filesystem.entities.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/create")
    public FileResponse createFile(@RequestParam String file) {
        try {
            storageService.createNewFile(file);
            return new FileResponse(file, Status.OK);
        } catch (Exception e) {
            return exceptionResponse(file, e);
        }

    }

    @DeleteMapping("/delete")
    public FileResponse deleteFile(@RequestParam String file) {
        try {
            storageService.deleteFile(file);
            return new FileResponse(file, Status.OK);
        } catch (Exception e) {
            return exceptionResponse(file, e);
        }
    }

    @PostMapping("/copy")
    public FileResponse copyFile(@RequestParam String src,
                                 @RequestParam String dest) {
        try {
            String file = storageService.copyFile(src, dest);
            return new FileResponse(file, Status.OK);
        } catch (Exception e) {
            return exceptionResponse("", e);
        }
    }

    @PostMapping("/move")
    public FileResponse moveFile(@RequestParam String src,
                                 @RequestParam String dest) {
        try {
            String file = storageService.moveFile(src, dest);
            return new FileResponse(file, Status.OK);
        } catch (Exception e) {
            return exceptionResponse("", e);
        }
    }

    @GetMapping("/content")
    public FileResponse getFileContent(@RequestParam String file) throws IOException {
        try {
            FileResponse response = new FileResponse(file, Status.OK);
            String content = storageService.getFileContent(file);
            response.setFileContent(content);
            return response;
        } catch (Exception e) {
            return exceptionResponse("", e);
        }
    }

    private FileResponse exceptionResponse(String file, Exception e) {
        FileResponse response = new FileResponse(file, Status.INTERNAL_ERROR);
        response.setError(new ErrorResponse(e.getMessage(), e.getClass().getCanonicalName()));
        return response;
    }
}
