package filesystem.utils;

public class FileUtil {

    public static String buildPath(String... paths) {
        return String.join("/", paths);
    };
}
