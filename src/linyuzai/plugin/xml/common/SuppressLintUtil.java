package linyuzai.plugin.xml.common;

import java.util.HashSet;
import java.util.Set;

public class SuppressLintUtil {
    public static final String RTL_HARDCODED = "RtlHardcoded";

    private static final Set<String> suppressLintSet = new HashSet<>();

    public static void suppress(StringBuilder builder, String suppress) {
        builder.append("@SuppressLint(\"").append(suppress).append("\")\n");
    }

    public static void suppressLint(String suppress) {
        suppressLintSet.add(suppress);
    }

    public static void suppressFromSet(StringBuilder builder) {
        suppressLintSet.parallelStream().forEach(it -> suppress(builder, it));
    }

    public static void clearSuppress() {
        suppressLintSet.clear();
    }
}
