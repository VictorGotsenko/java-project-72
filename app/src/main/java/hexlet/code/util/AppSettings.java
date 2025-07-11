package hexlet.code.util;

public final class AppSettings {
    private AppSettings() {
        //for SONAR warning
        throw new IllegalStateException("Utility class");
    }

    // Flash constant
    public static final String FLASH_DANGER = "danger";
    public static final String FLASH_SUCCESS = "success";
    public static final String FLASH_TYPE = "flash-type";
    public static final String FLASH_INFO = "info";
    public static final String FLASH_WARNING = "warning";


    public static final String FLASH = "flash";
    public static final String PAGE_OK = "Страница успешно проверена";
    public static final String PAGE_EXIST = "Страница уже существует";
    public static final String PAGE_ADDED = "Страница успешно добавлена";

    public static final String URL_BAD = "Некорректный адрес URL"; // Некорректный URL"
    public static final String URL_EMPTY = "Поле URL не должно быть пустым";

    public static final String CHECK_ERROR = "Ошибка при проверке страницы: ";
}
