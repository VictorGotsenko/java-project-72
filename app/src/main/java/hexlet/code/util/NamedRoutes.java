package hexlet.code.util;

public class NamedRoutes {
    public static String mainPage() {
        return "/";
    }

    public static String buildPath() {
        return "/urls/build";
    }

    public static String urlsPath() {
        return "/urls";
    }

    public static String urlPath(String id) {
        return "/urls/" + id;
    }

    public static String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }

}
