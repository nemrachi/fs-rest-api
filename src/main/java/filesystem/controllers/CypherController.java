package filesystem.controllers;

import filesystem.entities.ErrorResponse;
import filesystem.entities.FileResponse;
import filesystem.entities.Status;
import filesystem.services.CypherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cypher")
public class CypherController {

    @Autowired
    private CypherService cypherService;

    @PostMapping("/encrypt")
    public FileResponse encryptFile(@RequestParam String file){
        try {
            cypherService.encryptFile(file);
            return new FileResponse(file, Status.OK);
        } catch (Exception e) {
            return exceptionResponse(file, e);
        }
    }

    @PostMapping("/decrypt")
    public FileResponse decryptFile(@RequestParam String file) {
        try {
            cypherService.decryptFile(file);
            return new FileResponse(file, Status.OK);
        } catch (Exception e) {
            return exceptionResponse(file, e);
        }
    }

    private FileResponse exceptionResponse(String file, Exception e) {
        FileResponse response = new FileResponse(file, Status.INTERNAL_ERROR);
        response.setError(new ErrorResponse(e.getMessage(), e.getClass().getCanonicalName()));
        return response;
    }
}
