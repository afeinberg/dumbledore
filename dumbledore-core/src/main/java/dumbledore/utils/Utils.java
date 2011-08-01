package dumbledore.utils;

/**
 * @author Alex Feinberg
 */
public class Utils {

    public static String getPackageName(Class<?> c) {
        String name = c.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }

    public static String getClassName(Class<?> c) {
        String name = c.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }
}
