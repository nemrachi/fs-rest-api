package filesystem.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileLine {
    private String file;
    private int line;
}
