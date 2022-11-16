package filesystem.entities;

import lombok.*;

@Data
public class FileResponse {
    @NonNull
    private String file;
    private String fileContent;
    @NonNull
    private Status status;
    private ErrorResponse error;
}
