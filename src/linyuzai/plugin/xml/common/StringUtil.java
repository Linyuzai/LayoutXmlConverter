package linyuzai.plugin.xml.common;

public class StringUtil {
    public static final String VALUE_NOT_SUPPORT = " //not support value";

    public static boolean isAlpha(String alpha) {
        try {
            float f = Float.valueOf(alpha);
            return f >= 0.0f && f <= 1.0f;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isInteger(String integer) {
        try {
            Integer.valueOf(integer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isFloat(String f) {
        try {
            Float.valueOf(f);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isImage(String image) {
        return image.startsWith("@mipmap/") || image.startsWith("@drawable/") ||
                image.startsWith("@android:mipmap/") || image.startsWith("@android:drawable/");
    }

    public static boolean isId(String id) {
        return id.startsWith("@+id/") || id.startsWith("@id/") || id.startsWith("@android:id/");
    }

    public static boolean isColorRes(String colorRes) {
        return colorRes.startsWith("@color/") || colorRes.startsWith("@android:color/");
    }

    public static boolean isAttr(String attr) {
        return attr.startsWith("?attr/") || attr.startsWith("?android:attr/") ||
                attr.startsWith("?attr/android:") || attr.startsWith("?android:") ||
                attr.startsWith("?");
    }
}
