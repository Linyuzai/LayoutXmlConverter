package linyuzai.plugin.xml.common;

import java.util.HashSet;
import java.util.Set;

public class SuppressLintUtil {
    public static final String RTL_HARDCODED = "RtlHardcoded";

    private static final Set<String> importSet = new HashSet<>();

    public static void suppress(StringBuilder builder, String suppress) {
        builder.append("@SuppressLint(\"").append(suppress).append("\")\n");
    }

    public static void add(String suppress) {
        importSet.add(suppress);
    }

    public static void suppressFromSet(StringBuilder builder) {
        importSet.parallelStream().forEach(it -> suppress(builder, it));
    }

    public static void clearSuppress() {
        importSet.clear();
    }
}
