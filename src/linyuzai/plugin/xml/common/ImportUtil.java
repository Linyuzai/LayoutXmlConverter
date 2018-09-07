package linyuzai.plugin.xml.common;

import java.util.HashSet;
import java.util.Set;

public class ImportUtil {
    private static final String IMPORT = "import ";

    public static final String ANKO = "org.jetbrains.anko";
    public static final String ANKO_V4 = "org.jetbrains.anko.support.v4";
    public static final String ANKO_V7 = "org.jetbrains.anko.appcompat.v7";
    public static final String ANKO_DESIGN = "org.jetbrains.anko.design";
    public static final String ANKO_RECYCLER_VIEW = "org.jetbrains.anko.recyclerview.v7";

    public static final String ACTIVITY = "android.app.Activity";
    public static final String BUNDLE = "android.os.Bundle";

    public static final String VIEW = "android.view";
    public static final String GRAPHICS = "android.graphics";
    public static final String TEXT = "android.text";
    public static final String WIDGET = "android.widget";
    public static final String ANIMATION = "android.animation";
    public static final String VIEW_ANIMATION = "android.view.animation";

    public static final String COLOR = "android.graphics.Color";
    public static final String TYPEFACE = "android.graphics.Typeface";
    public static final String GRAVITY = "android.view.Gravity";
    public static final String INPUT_TYPE = "android.text.InputType";
    public static final String LINEAR_LAYOUT = "android.widget.LinearLayout";
    public static final String IMAGE_VIEW = "android.widget.ImageView";
    public static final String PORTER_DUFF = "android.graphics.PorterDuff";
    public static final String COLOR_DRAWABLE = "android.graphics.drawable.ColorDrawable";
    public static final String VIEW_OUTLINE_PROVIDER = "android.view.ViewOutlineProvider";
    public static final String POINTER_ICON = "android.view.PointerIcon";
    public static final String ANIMATOR_INFLATER = "android.animation.AnimatorInflater";

    public static final String DESIGN = "android.support.design.widget";

    private static final Set<String> supportSet = new HashSet<>();
    private static final Set<String> multiSupportSet = new HashSet<>();

    public static void importClass(StringBuilder builder, String classTag) {
        builder.append(IMPORT).append(classTag).append("\n");
    }

    public static void importMultiClass(StringBuilder builder, String packageTag) {
        builder.append(IMPORT).append(packageTag).append(".*").append("\n");
    }

    public static void importR(StringBuilder builder, String packageTag) {
        builder.append("\n").append(IMPORT).append(packageTag).append(".R").append("\n");
    }

    public static void support(String importClass) {
        supportSet.add(importClass);
    }

    public static void supportMulti(String importClass) {
        multiSupportSet.add(importClass);
    }

    public static void importFromSet(StringBuilder builder) {
        multiSupportSet.parallelStream().forEach(it -> importMultiClass(builder, it));
        supportSet.parallelStream().forEach(it -> importClass(builder, it));
    }

    public static void clearImport() {
        supportSet.clear();
    }
}
