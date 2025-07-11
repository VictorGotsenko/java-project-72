package hexlet.code.util;

public final class NamedRoutes {
    static final String PATH_MAIN_PAGE = "/";
    static final String PATH_BUILD_PAGE = "/urls/build";
    static final String PATH_SITES_PAGE = "/urls";

    private NamedRoutes() {
        //  for SONAR warning
        throw new IllegalStateException("Utility class");
    }

    public static String mainPage() {
        return PATH_MAIN_PAGE;
    }

    public static String buildPath() {
        return PATH_BUILD_PAGE;
    }

    public static String urlsPath() {
        return PATH_SITES_PAGE;
    }

    public static String urlPath(String id) {
        return PATH_SITES_PAGE + "/" + id;
    }

    public static String urlPath(Long id) {
        return urlPath(String.valueOf(id));
    }

    public static String urlPathForChecks(String id) {
        return PATH_SITES_PAGE + "/" + id + "/checks";
    }

    public static String urlPathForChecks(Long id) {
        return urlPathForChecks(String.valueOf(id));
    }
}
