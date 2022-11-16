package filesystem.entities;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class DirResponse {
    @NonNull
    private String dir;
    private List<String> dirContent;
    private List<FileLine> foundPattern;
    @NonNull
    private Status status;
    private ErrorResponse error;
}
