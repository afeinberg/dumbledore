package dumbledore.utils;

/**
 *
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

    public static <T> T notNull(T t) {
        return notNull(t, "This object MUST be non-null.");
    }

    public static <T> T notNull(T t, String message) {
        if(t == null)
            throw new IllegalArgumentException(message);
        return t;
    }
}
