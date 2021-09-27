package fun.bookish.blueberry.core.utils;

public class SystemUtils {

    private SystemUtils() {}

    public static String osName() {
        return System.getProperty("os.name");
    }

    public static boolean isWin() {
        return osName().toUpperCase().contains("WIN");
    }

    public static boolean isLinux() {
        return osName().toUpperCase().contains("LINUX");
    }

}
