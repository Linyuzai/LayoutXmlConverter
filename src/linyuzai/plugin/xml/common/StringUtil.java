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

    public static boolean isImage(String image) {
        return image.contains("@mipmap/") || image.contains("@drawable/") ||
                image.contains("@android:mipmap/") || image.contains("@android:drawable/");
    }
}
