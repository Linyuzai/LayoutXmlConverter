package linyuzai.plugin.xml.converter;

import linyuzai.plugin.xml.attr.*;
import linyuzai.plugin.xml.common.*;
import org.dom4j.Element;

import java.util.List;

public class AnkoConverter {

    public interface Callback {
        void onUpdate(int value);
    }

    private UApplication application;
    private StringBuilder noteBuilder;
    private StringBuilder codeBuilder;
    private StringBuilder importBuilder;
    private StringBuilder suppressBuilder;
    private Callback callback;
    private int progress;

    public AnkoConverter(UApplication application, Callback callback) {
        this.application = application;
        this.callback = callback;
        this.noteBuilder = new StringBuilder();
        this.codeBuilder = new StringBuilder();
        this.importBuilder = new StringBuilder();
        this.suppressBuilder = new StringBuilder();
        this.progress = 1;
    }

    public String convert(String name, Element root) {
        writeNotes();
        writeActivityStart(name);
        writeAnnotation();
        writeOnCreateStart();
        writeView(root, 2);
        writeOnCreateEnd();
        writeActivityEnd();

        writePackage();
        writeImport();

        return importBuilder.append("\n").append(noteBuilder).append(codeBuilder).toString();
    }

    private void writePackage() {
        importBuilder.append("package anko\n\n");
    }

    private void writeImport() {
        ImportUtil.importClass(importBuilder, ImportUtil.ACTIVITY);
        ImportUtil.importClass(importBuilder, ImportUtil.BUNDLE);
        ImportUtil.importFromSet(importBuilder);
        ImportUtil.importMultiClass(importBuilder, ImportUtil.ANKO);
        ImportUtil.importR(importBuilder, application.getPackageName());
        ImportUtil.clearImport();
    }

    private void writeNotes() {
        noteBuilder.append("/**\n");
        noteBuilder.append(" * Generate with Plugin\n");
        noteBuilder.append(" * @plugin Kotlin Anko Converter For Xml\n");
        noteBuilder.append(" * @version 1.3.0\n");
        noteBuilder.append(" */\n");
    }

    private void writeActivityStart(String className) {
        codeBuilder.append("class ").append(className).append(" : Activity() {\n\n");
    }

    private void writeActivityEnd() {
        codeBuilder.append("}\n");
    }

    private void writeAnnotation() {
        SuppressLintUtil.suppressFromSet(suppressBuilder);
        SuppressLintUtil.clearSuppress();
    }

    private void writeOnCreateStart() {
        codeBuilder.append("\toverride fun onCreate(savedInstanceState: Bundle?) {\n")
                .append("\t\tsuper.onCreate(savedInstanceState)\n");
    }

    private void writeOnCreateEnd() {
        codeBuilder.append("\t}\n");
    }

    private void writeViewStart(String viewName, UAttribute theme, int deep, boolean withEnd) {
        writeTab(deep);
        if (viewName.equals("android.support.v7.widget.RecyclerView")) {
            ImportUtil.supportMulti(ImportUtil.ANKO_RECYCLER_VIEW);
        }
        if (viewName.equals("android.support.v7.widget.Toolbar")) {
            ImportUtil.support("org.jetbrains.anko.appcompat.v7.toolbar");
        }
        codeBuilder.append(ConvertUtil.convertViewName(viewName, theme != null, it -> {
            if (it.equals("android.support.v4.widget"))
                ImportUtil.supportMulti(ImportUtil.ANKO_V4);
            if (it.equals("android.support.v7.widget"))
                ImportUtil.supportMulti(ImportUtil.ANKO_V7);
            if (it.equals("android.support.design.widget")) {
                ImportUtil.supportMulti(ImportUtil.DESIGN);
                ImportUtil.supportMulti(ImportUtil.ANKO_DESIGN);
            }
        }));
        if (theme != null) {
            codeBuilder.append("(").append(AttributeUtil.getTheme(theme.getValue())).append(")");
        }
        if (withEnd) {
            if (theme == null)
                codeBuilder.append("()");
        } else {
            codeBuilder.append(" {\n");
        }
    }

    private void writeViewEnd(int deep) {
        writeTab(deep);
        codeBuilder.append("}");
    }

    private void writeLayoutParams(List<UAttribute> params, List<UAttribute> layout, int deep) {
        if (deep == 2) {
            codeBuilder.append("\n");
            return;
        }
        String widthValue = params.get(0).getValue();
        String heightValue = params.get(1).getValue();
        if (widthValue.equals(XmlAttrValue.LayoutParams.WRAP_CONTENT) && heightValue.equals(XmlAttrValue.LayoutParams.WRAP_CONTENT)) {
            if (layout.size() > 0)
                codeBuilder.append(".lparams");
        } else if (widthValue.equals(XmlAttrValue.LayoutParams.WRAP_CONTENT)) {
            codeBuilder.append(".lparams(height = ").append(AttributeUtil.getWidthOrHeight(heightValue)).append(")");
        } else if (heightValue.equals(XmlAttrValue.LayoutParams.WRAP_CONTENT)) {
            codeBuilder.append(".lparams(width = ").append(AttributeUtil.getWidthOrHeight(widthValue)).append(")");
        } else {
            codeBuilder.append(".lparams(width = ").append(AttributeUtil.getWidthOrHeight(widthValue))
                    .append(", height = ").append(AttributeUtil.getWidthOrHeight(heightValue)).append(")");
        }
        if (layout.size() > 0) {
            codeBuilder.append(" {\n");
            for (UAttribute attr : layout)
                writeAttribute(null, attr.getName(), attr.getValue(), deep + 1, false);
            writeTab(deep);
            codeBuilder.append("}");
        }
        codeBuilder.append("\n");
    }

    private void writeOrientationEnableAttribute(String className, String attributeName, String attributeValue, int deep) {
        boolean isVerticalEnable = false;
        boolean isHorizontalEnable = false;
        if (attributeValue.contains(XmlAttrValue.Orientation.VERTICAL))
            isVerticalEnable = true;
        if (attributeValue.contains(XmlAttrValue.Orientation.HORIZONTAL))
            isHorizontalEnable = true;
        if (isVerticalEnable) {
            writeAttribute(className, attributeName, XmlAttrValue.Orientation.VERTICAL + ";" +
                    XmlAttrValue.TRUE, deep, true);
        } else {
            writeAttribute(className, attributeName, XmlAttrValue.Orientation.VERTICAL + ";" +
                    XmlAttrValue.FALSE, deep, true);
        }
        writeTab(deep);
        if (isHorizontalEnable) {
            writeAttribute(className, attributeName, XmlAttrValue.Orientation.HORIZONTAL + ";" +
                    XmlAttrValue.TRUE, deep, true);
        } else {
            writeAttribute(className, attributeName, XmlAttrValue.Orientation.HORIZONTAL + ";" +
                    XmlAttrValue.FALSE, deep, true);
        }
    }

    private void writeAttribute(String className, String attributeName, String attributeValue, int deep, boolean isRecursion) {
        if (!isRecursion)
            writeTab(deep);
        String name = attributeName;
        String value = attributeValue;
        String extra = "";
        boolean isEqualsOperator = true;
        boolean isNotSupportAttribute = false;
        boolean isNote = false;
        boolean isFunction = false;
        if (AttributeUtil.isNullOrEmpty(attributeValue)) {
            isNote = true;
            extra = "// value is empty or null";
        } else if (attributeName.startsWith("android:")) {
            switch (attributeName) {
                /**
                 * View Attrs Start
                 */
                case XmlAttrName.View.ID:
                    name = ViewAttrName.View.ID;
                    value = AttributeUtil.getId(attributeValue);
                    break;
                case XmlAttrName.View.ACCESSIBILITY_LIVE_REGION:
                    name = ViewAttrName.View.ACCESSIBILITY_LIVE_REGION;
                    value = AttributeUtil.getAccessibilityLiveRegion(attributeValue);
                    break;
                case XmlAttrName.View.ACCESSIBILITY_TRAVERSAL_AFTER:
                    name = ViewAttrName.View.ACCESSIBILITY_TRAVERSAL_AFTER;
                    value = AttributeUtil.getId(attributeValue);
                    break;
                case XmlAttrName.View.ACCESSIBILITY_TRAVERSAL_BEFORE:
                    name = ViewAttrName.View.ACCESSIBILITY_TRAVERSAL_BEFORE;
                    value = AttributeUtil.getId(attributeValue);
                    break;
                case XmlAttrName.View.ALPHA:
                    name = ViewAttrName.View.ALPHA;
                    value = AttributeUtil.getAlpha(attributeValue);
                    break;
                case XmlAttrName.View.AUTOFILL_HINTS:
                    name = ViewAttrName.View.AUTOFILL_HINTS;
                    value = AttributeUtil.getMultiAutofillHint(attributeValue);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.View.BACKGROUND:
                    if (StringUtil.isImage(attributeValue)) {
                        name = ViewAttrName.View.BACKGROUND_RESOURCE;
                        value = AttributeUtil.getImage(attributeValue);
                    } else {
                        name = ViewAttrName.View.BACKGROUND_COLOR;
                        value = AttributeUtil.getColor(attributeValue);
                    }
                    break;
                case XmlAttrName.View.BACKGROUND_TINT:
                    name = ViewAttrName.View.BACKGROUND_TINT;
                    value = AttributeUtil.getColorStateList(attributeValue);
                    break;
                case XmlAttrName.View.BACKGROUND_TINT_MODE:
                    name = ViewAttrName.View.BACKGROUND_TINT_MODE;
                    value = AttributeUtil.getPorterDuffMode(attributeValue);
                    break;
                case XmlAttrName.View.CLICKABLE:
                    name = ViewAttrName.View.CLICKABLE;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.CONTENT_DESCRIPTION:
                    name = ViewAttrName.View.CONTENT_DESCRIPTION;
                    value = AttributeUtil.getString(attributeValue);
                    break;
                case XmlAttrName.View.CONTEXT_CLICKABLE:
                    name = ViewAttrName.View.CONTEXT_CLICKABLE;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.DEFAULT_FOCUS_HIGHLIGHT_ENABLED:
                    name = ViewAttrName.View.DEFAULT_FOCUS_HIGHLIGHT_ENABLED;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.DRAWING_CACHE_QUALITY:
                    name = ViewAttrName.View.DRAWING_CACHE_QUALITY;
                    value = AttributeUtil.getDrawingCacheQuality(attributeValue);
                    break;
                case XmlAttrName.View.DUPLICATE_PARENT_STATE:
                    name = ViewAttrName.View.DUPLICATE_PARENT_STATE;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.ELEVATION:
                    name = ViewAttrName.View.ELEVATION;
                    value = AttributeUtil.getDimension(attributeValue, true);
                    break;
                case XmlAttrName.View.FADE_SCROLLBARS:
                    name = ViewAttrName.View.FADE_SCROLLBARS;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.FADING_EDGE:
                    writeAttribute(className, XmlAttrName.View.REQUIRES_FADING_EDGE, attributeValue, deep, true);
                    return;
                case XmlAttrName.View.FADING_EDGE_LENGTH:
                    name = ViewAttrName.View.FADING_EDGE_LENGTH;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.View.FILTER_TOUCHES_WHEN_OBSCURED:
                    name = ViewAttrName.View.FILTER_TOUCHES_WHEN_OBSCURED;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.FITS_SYSTEM_WINDOWS:
                    name = ViewAttrName.View.FITS_SYSTEM_WINDOWS;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.FOCUSABLE:
                    if (attributeValue.equals(XmlAttrValue.Focusable.AUTO)) {
                        isNote = true;
                        extra = " //Auto is default";
                    } else {
                        name = ViewAttrName.View.FOCUSABLE;
                        value = AttributeUtil.getBoolean(attributeValue);
                    }
                    break;
                case XmlAttrName.View.FOCUSABLE_IN_TOUCH_MODE:
                    name = ViewAttrName.View.FOCUSABLE_IN_TOUCH_MODE;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.FOCUSED_BY_DEFAULT:
                    name = ViewAttrName.View.FOCUSED_BY_DEFAULT;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.FORCE_HAS_OVERLAPPING_RENDERING:
                    name = ViewAttrName.View.FORCE_HAS_OVERLAPPING_RENDERING;
                    value = AttributeUtil.getBoolean(attributeValue);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.View.FOREGROUND:
                    name = ViewAttrName.View.FOREGROUND;
                    value = AttributeUtil.getDrawable(attributeValue);
                    break;
                case XmlAttrName.View.FOREGROUND_GRAVITY:
                    name = ViewAttrName.View.FOREGROUND_GRAVITY;
                    value = AttributeUtil.getMultiGravity(attributeValue);
                    break;
                case XmlAttrName.View.FOREGROUND_TINT:
                    name = ViewAttrName.View.FOREGROUND_TINT;
                    value = AttributeUtil.getColorStateList(attributeValue);
                    break;
                case XmlAttrName.View.FOREGROUND_TINT_MODE:
                    name = ViewAttrName.View.FOREGROUND_TINT_MODE;
                    value = AttributeUtil.getPorterDuffMode(attributeValue);
                    break;
                case XmlAttrName.View.HAPTIC_FEEDBACK_ENABLED:
                    name = ViewAttrName.View.HAPTIC_FEEDBACK_ENABLED;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.IMPORTANT_FOR_ACCESSIBILITY:
                    name = ViewAttrName.View.IMPORTANT_FOR_ACCESSIBILITY;
                    value = AttributeUtil.getImportantForAccessibility(attributeValue);
                    break;
                case XmlAttrName.View.IMPORTANT_FOR_AUTOFILL:
                    name = ViewAttrName.View.IMPORTANT_FOR_AUTOFILL;
                    value = AttributeUtil.getImportantForAutofill(attributeValue);
                    break;
                case XmlAttrName.View.IS_SCROLL_CONTAINER:
                    name = ViewAttrName.View.IS_SCROLL_CONTAINER;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.KEEP_SCREEN_ON:
                    name = ViewAttrName.View.KEEP_SCREEN_ON;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.KEYBOARD_NAVIGATION_CLUSTER:
                    name = ViewAttrName.View.KEYBOARD_NAVIGATION_CLUSTER;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.LABEL_FOR:
                    name = ViewAttrName.View.LABEL_FOR;
                    value = AttributeUtil.getId(attributeValue);
                    break;
                case XmlAttrName.View.LAYER_TYPE:
                    name = ViewAttrName.View.LAYER_TYPE;
                    value = AttributeUtil.getLayerType(attributeValue) + ", null";
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.View.LAYOUT_DIRECTION:
                    name = ViewAttrName.View.LAYOUT_DIRECTION;
                    value = AttributeUtil.getLayoutDirection(attributeValue);
                    break;
                case XmlAttrName.View.LONG_CLICKABLE:
                    name = ViewAttrName.View.LONG_CLICKABLE;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.MIN_HEIGHT:
                    name = ViewAttrName.View.MIN_HEIGHT;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.MIN_WIDTH:
                    name = ViewAttrName.View.MIN_WIDTH;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.NESTED_SCROLLING_ENABLED:
                    name = ViewAttrName.View.NESTED_SCROLLING_ENABLED;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.NEXT_CLUSTER_FORWARD:
                    name = ViewAttrName.View.NEXT_CLUSTER_FORWARD;
                    if (StringUtil.isId(attributeValue)) {
                        value = AttributeUtil.getId(attributeValue);
                    } else {
                        isNote = true;
                        extra = StringUtil.VALUE_SHOULD_BE_AN_ID;
                    }
                    break;
                case XmlAttrName.View.NEXT_FOCUS_DOWN:
                    name = ViewAttrName.View.NEXT_FOCUS_DOWN;
                    if (StringUtil.isId(attributeValue)) {
                        value = AttributeUtil.getId(attributeValue);
                    } else {
                        isNote = true;
                        extra = StringUtil.VALUE_SHOULD_BE_AN_ID;
                    }
                    break;
                case XmlAttrName.View.NEXT_FOCUS_FORWARD:
                    name = ViewAttrName.View.NEXT_FOCUS_FORWARD;
                    if (StringUtil.isId(attributeValue)) {
                        value = AttributeUtil.getId(attributeValue);
                    } else {
                        isNote = true;
                        extra = StringUtil.VALUE_SHOULD_BE_AN_ID;
                    }
                    break;
                case XmlAttrName.View.NEXT_FOCUS_LEFT:
                    name = ViewAttrName.View.NEXT_FOCUS_LEFT;
                    if (StringUtil.isId(attributeValue)) {
                        value = AttributeUtil.getId(attributeValue);
                    } else {
                        isNote = true;
                        extra = StringUtil.VALUE_SHOULD_BE_AN_ID;
                    }
                    break;
                case XmlAttrName.View.NEXT_FOCUS_RIGHT:
                    name = ViewAttrName.View.NEXT_FOCUS_RIGHT;
                    if (StringUtil.isId(attributeValue)) {
                        value = AttributeUtil.getId(attributeValue);
                    } else {
                        isNote = true;
                        extra = StringUtil.VALUE_SHOULD_BE_AN_ID;
                    }
                    break;
                case XmlAttrName.View.NEXT_FOCUS_UP:
                    name = ViewAttrName.View.NEXT_FOCUS_UP;
                    if (StringUtil.isId(attributeValue)) {
                        value = AttributeUtil.getId(attributeValue);
                    } else {
                        isNote = true;
                        extra = StringUtil.VALUE_SHOULD_BE_AN_ID;
                    }
                    break;
                case XmlAttrName.View.ON_CLICK:
                    isFunction = true;
                    name = ViewAttrName.View.ON_CLICK;
                    break;
                case XmlAttrName.View.OUTLINE_PROVIDER:
                    name = ViewAttrName.View.OUTLINE_PROVIDER;
                    value = AttributeUtil.getOutlineProvider(attributeValue);
                    break;
                case XmlAttrName.View.OVER_SCROLL_MODE:
                    name = ViewAttrName.View.OVER_SCROLL_MODE;
                    value = AttributeUtil.getOverScrollMode(attributeValue);
                    break;
                case XmlAttrName.View.PADDING:
                    name = ViewAttrName.View.PADDING;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.PADDING_BOTTOM:
                    name = ViewAttrName.View.PADDING_BOTTOM;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.PADDING_END:
                    name = ViewAttrName.View.PADDING_END;
                    value = "paddingStart, paddingTop, " + AttributeUtil.getDimension(attributeValue, false) + ", paddingBottom";
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.View.PADDING_HORIZONTAL:
                    name = ViewAttrName.View.PADDING_HORIZONTAL;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.PADDING_LEFT:
                    name = ViewAttrName.View.PADDING_LEFT;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.PADDING_RIGHT:
                    name = ViewAttrName.View.PADDING_RIGHT;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.PADDING_START:
                    name = ViewAttrName.View.PADDING_START;
                    value = AttributeUtil.getDimension(attributeValue, false) + ", paddingTop, paddingEnd, paddingBottom";
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.View.PADDING_TOP:
                    name = ViewAttrName.View.PADDING_TOP;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.PADDING_VERTICAL:
                    name = ViewAttrName.View.PADDING_VERTICAL;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.POINTER_ICON:
                    name = ViewAttrName.View.POINTER_ICON;
                    value = "PointerIcon.getSystemIcon(context, " + AttributeUtil.getPointerIconType(attributeValue) + ")";
                    break;
                case XmlAttrName.View.REQUIRES_FADING_EDGE:
                    if (attributeValue.contains(";")) {
                        String[] attrArr = attributeValue.split(";");
                        switch (attrArr[0]) {
                            case XmlAttrValue.Orientation.VERTICAL:
                                name = ViewAttrName.View.REQUIRES_FADING_EDGE_VERTICAL;
                                break;
                            case XmlAttrValue.Orientation.HORIZONTAL:
                                name = ViewAttrName.View.REQUIRES_FADING_EDGE_HORIZONTAL;
                                break;
                        }
                        value = attrArr[1];
                    } else {
                        writeOrientationEnableAttribute(className, attributeName, attributeValue, deep);
                        return;
                    }
                    break;
                case XmlAttrName.View.ROTATION:
                    name = ViewAttrName.View.ROTATION;
                    value = AttributeUtil.getFloat(attributeValue);
                    break;
                case XmlAttrName.View.ROTATION_X:
                    name = ViewAttrName.View.ROTATION_X;
                    value = AttributeUtil.getFloat(attributeValue);
                    break;
                case XmlAttrName.View.ROTATION_Y:
                    name = ViewAttrName.View.ROTATION_Y;
                    value = AttributeUtil.getFloat(attributeValue);
                    break;
                case XmlAttrName.View.SAVE_ENABLED:
                    name = ViewAttrName.View.SAVE_ENABLED;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.SCALE_X:
                    name = ViewAttrName.View.SCALE_X;
                    value = AttributeUtil.getFloat(attributeValue);
                    break;
                case XmlAttrName.View.SCALE_Y:
                    name = ViewAttrName.View.SCALE_Y;
                    value = AttributeUtil.getFloat(attributeValue);
                    break;
                case XmlAttrName.View.SCROLL_INDICATORS:
                    if (attributeValue.equals(XmlAttrValue.NONE)) {
                        isNote = true;
                        extra = StringUtil.NOTHING_TO_SET;
                    } else {
                        name = ViewAttrName.View.SCROLL_INDICATORS;
                        value = AttributeUtil.getMultiScrollIndicator(attributeValue);
                    }
                    break;
                case XmlAttrName.View.SCROLL_X:
                    name = ViewAttrName.View.SCROLL_X;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.SCROLL_Y:
                    name = ViewAttrName.View.SCROLL_Y;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.SCROLLBAR_ALWAYS_DRAW_HORIZONTAL_TRACK:
                case XmlAttrName.View.SCROLLBAR_ALWAYS_DRAW_VERTICAL_TRACK:
                    isNote = true;
                    extra = StringUtil.CAN_NOT_SET_BY_CODE;
                    break;
                case XmlAttrName.View.SCROLLBAR_DEFAULT_DELAY_BEFORE_FADE:
                    name = ViewAttrName.View.SCROLLBAR_DEFAULT_DELAY_BEFORE_FADE;
                    value = AttributeUtil.getInteger(attributeValue, false);
                    break;
                case XmlAttrName.View.SCROLLBAR_FADE_DURATION:
                    name = ViewAttrName.View.SCROLLBAR_FADE_DURATION;
                    value = AttributeUtil.getInteger(attributeValue, false);
                    break;
                case XmlAttrName.View.SCROLLBAR_SIZE:
                    name = ViewAttrName.View.SCROLLBAR_SIZE;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.View.SCROLLBAR_STYLE:
                    name = ViewAttrName.View.SCROLLBAR_STYLE;
                    value = AttributeUtil.getScrollBarStyle(attributeValue);
                    break;
                case XmlAttrName.View.SCROLLBAR_THUMB_HORIZONTAL:
                case XmlAttrName.View.SCROLLBAR_THUMB_VERTICAL:
                case XmlAttrName.View.SCROLLBAR_TRACK_HORIZONTAL:
                case XmlAttrName.View.SCROLLBAR_TRACK_VERTICAL:
                    isNote = true;
                    extra = StringUtil.CAN_NOT_SET_BY_CODE;
                    break;
                case XmlAttrName.View.SCROLLBARS:
                    if (attributeValue.contains(";")) {
                        String[] attrArr = attributeValue.split(";");
                        switch (attrArr[0]) {
                            case XmlAttrValue.Orientation.VERTICAL:
                                name = ViewAttrName.View.SCROLLBARS_VERTICAL;
                                break;
                            case XmlAttrValue.Orientation.HORIZONTAL:
                                name = ViewAttrName.View.SCROLLBARS_HORIZONTAL;
                                break;
                        }
                        value = attrArr[1];
                    } else {
                        writeOrientationEnableAttribute(className, attributeName, attributeValue, deep);
                        return;
                    }
                    break;
                case XmlAttrName.View.SOUND_EFFECTS_ENABLED:
                    name = ViewAttrName.View.SOUND_EFFECTS_ENABLED;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.View.STATE_LIST_ANIMATOR:
                    name = ViewAttrName.View.STATE_LIST_ANIMATOR;
                    value = AttributeUtil.getStateListAnimator(attributeValue);
                    break;
                case XmlAttrName.View.TAG:
                    name = ViewAttrName.View.TAG;
                    value = AttributeUtil.getString(attributeValue);
                    break;
                case XmlAttrName.View.TEXT_ALIGNMENT:
                    name = ViewAttrName.View.TEXT_ALIGNMENT;
                    value = AttributeUtil.getTextAlignment(attributeValue);
                    break;
                case XmlAttrName.View.TEXT_DIRECTION:
                    name = ViewAttrName.View.TEXT_DIRECTION;
                    value = AttributeUtil.getTextDirection(attributeValue);
                    break;
                /*case XmlAttrName.THEME:
                    //Not be Here
                    break;*/
                case XmlAttrName.View.TOOLTIP_TEXT:
                    name = ViewAttrName.View.TOOLTIP_TEXT;
                    value = AttributeUtil.getString(attributeValue);
                    break;
                case XmlAttrName.View.TRANSFORM_PIVOT_X:
                    name = ViewAttrName.View.TRANSFORM_PIVOT_X;
                    value = AttributeUtil.getDimension(attributeValue, true);
                    break;
                case XmlAttrName.View.TRANSFORM_PIVOT_Y:
                    name = ViewAttrName.View.TRANSFORM_PIVOT_Y;
                    value = AttributeUtil.getDimension(attributeValue, true);
                    break;
                case XmlAttrName.View.TRANSITION_NAME:
                    name = ViewAttrName.View.TRANSITION_NAME;
                    value = AttributeUtil.getString(attributeValue);
                    break;
                case XmlAttrName.View.TRANSLATION_X:
                    name = ViewAttrName.View.TRANSLATION_X;
                    value = AttributeUtil.getDimension(attributeValue, true);
                    break;
                case XmlAttrName.View.TRANSLATION_Y:
                    name = ViewAttrName.View.TRANSLATION_Y;
                    value = AttributeUtil.getDimension(attributeValue, true);
                    break;
                case XmlAttrName.View.TRANSLATION_Z:
                    name = ViewAttrName.View.TRANSLATION_Z;
                    value = AttributeUtil.getDimension(attributeValue, true);
                    break;
                case XmlAttrName.View.VERTICAL_SCROLLBAR_POSITION:
                    name = ViewAttrName.View.VERTICAL_SCROLLBAR_POSITION;
                    value = AttributeUtil.getVerticalScrollbarPosition(attributeValue);
                    break;
                case XmlAttrName.View.VISIBILITY:
                    name = ViewAttrName.View.VISIBILITY;
                    value = AttributeUtil.getVisibility(attributeValue);
                    break;
                /**
                 * View Attrs End
                 */
                case XmlAttrName.TEXT:
                    name = ViewAttrName.TEXT;
                    value = AttributeUtil.getString(attributeValue);
                    break;
                case XmlAttrName.TEXT_SIZE:
                    name = ViewAttrName.TEXT_SIZE;
                    value = AttributeUtil.getDimension(attributeValue, true);
                    break;
                case XmlAttrName.TEXT_COLOR:
                    name = ViewAttrName.TEXT_COLOR;
                    value = AttributeUtil.getColor(attributeValue);
                    break;
                case XmlAttrName.TEXT_COLOR_HINT:
                    name = ViewAttrName.HINT_TEXT_COLOR;
                    value = AttributeUtil.getColor(attributeValue);
                    break;
                case XmlAttrName.TEXT_STYLE:
                    name = ViewAttrName.TEXT_STYLE;
                    value = "typeface, " + AttributeUtil.getTextStyle(attributeValue);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.View.GRAVITY:
                    name = ViewAttrName.ViewGroup.LayoutParams.GRAVITY;
                    value = AttributeUtil.getMultiGravity(attributeValue);
                    break;
                case XmlAttrName.MAX_LINES:
                    name = ViewAttrName.MAX_LINES;
                    value = AttributeUtil.getInteger(attributeValue, false);
                    break;
                case XmlAttrName.ORIENTATION:
                    name = ViewAttrName.ORIENTATION;
                    value = AttributeUtil.getOrientation(attributeValue);
                    break;
                case XmlAttrName.SRC:
                    name = ViewAttrName.IMAGE_RESOURCE;
                    value = AttributeUtil.getImage(attributeValue);
                    break;
                case XmlAttrName.SCALE_TYPE:
                    name = ViewAttrName.SCALE_TYPE;
                    value = AttributeUtil.getScaleType(attributeValue);
                    break;
                case XmlAttrName.SINGLE_LINE:
                    name = ViewAttrName.SINGLE_LINE;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.HINT:
                    name = ViewAttrName.HINT;
                    value = AttributeUtil.getString(attributeValue);
                    break;
                case XmlAttrName.INPUT_TYPE:
                    name = ViewAttrName.INPUT_TYPE;
                    value = AttributeUtil.getInputType(attributeValue);
                    break;
                case XmlAttrName.ViewGroup.LayoutParams.LAYOUT_MARGIN:
                    name = ViewAttrName.ViewGroup.LayoutParams.MARGIN;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.ViewGroup.LayoutParams.LAYOUT_MARGIN_TOP:
                    name = ViewAttrName.ViewGroup.LayoutParams.MARGIN_TOP;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.ViewGroup.LayoutParams.LAYOUT_MARGIN_RIGHT:
                    name = ViewAttrName.ViewGroup.LayoutParams.MARGIN_RIGHT;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.ViewGroup.LayoutParams.LAYOUT_MARGIN_BOTTOM:
                    name = ViewAttrName.ViewGroup.LayoutParams.MARGIN_BOTTOM;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.ViewGroup.LayoutParams.LAYOUT_MARGIN_LEFT:
                    name = ViewAttrName.ViewGroup.LayoutParams.MARGIN_LEFT;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.ViewGroup.LayoutParams.LAYOUT_MARGIN_START:
                    name = ViewAttrName.ViewGroup.LayoutParams.MARGIN_START;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.ViewGroup.LayoutParams.LAYOUT_MARGIN_END:
                    name = ViewAttrName.ViewGroup.LayoutParams.MARGIN_END;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.ViewGroup.LayoutParams.LAYOUT_MARGIN_VERTICAL:
                    name = ViewAttrName.ViewGroup.LayoutParams.MARGIN_VERTICAL;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.ViewGroup.LayoutParams.LAYOUT_MARGIN_HORIZONTAL:
                    name = ViewAttrName.ViewGroup.LayoutParams.MARGIN_HORIZONTAL;
                    value = AttributeUtil.getDimension(attributeValue, false);
                    break;
                case XmlAttrName.ViewGroup.LayoutParams.LAYOUT_GRAVITY:
                    name = ViewAttrName.ViewGroup.LayoutParams.GRAVITY;
                    value = AttributeUtil.getMultiGravity(attributeValue);
                    break;
                case XmlAttrName.LAYOUT_WEIGHT:
                    name = ViewAttrName.WEIGHT;
                    value = AttributeUtil.getWeight(attributeValue);
                    break;
                case XmlAttrName.LAYOUT_ABOVE:
                    name = ViewAttrName.ABOVE;
                    value = AttributeUtil.getId(attributeValue);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_BELOW:
                    name = ViewAttrName.BELOW;
                    value = AttributeUtil.getId(attributeValue);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_TO_LEFT_OF:
                    name = ViewAttrName.LEFT_OF;
                    value = AttributeUtil.getId(attributeValue);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_TO_RIGHT_OF:
                    name = ViewAttrName.RIGHT_OF;
                    value = AttributeUtil.getId(attributeValue);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_ALIGN_TOP:
                    name = ViewAttrName.ALIGN_TOP;
                    value = AttributeUtil.getId(attributeValue);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_ALIGN_BOTTOM:
                    name = ViewAttrName.ALIGN_BOTTOM;
                    value = AttributeUtil.getId(attributeValue);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_ALIGN_LEFT:
                    name = ViewAttrName.ALIGN_LEFT;
                    value = AttributeUtil.getId(attributeValue);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_ALIGN_RIGHT:
                    name = ViewAttrName.ALIGN_RIGHT;
                    value = AttributeUtil.getId(attributeValue);
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_CENTER_VERTICAL:
                    switch (attributeValue) {
                        case XmlAttrValue.TRUE:
                            name = ViewAttrName.CENTER_VERTICAL;
                            value = ViewAttrValue.EMPTY;
                            break;
                        case XmlAttrValue.FALSE:
                            name = "//" + ViewAttrName.CENTER_VERTICAL;
                            break;
                        default:
                            name = "//" + ViewAttrName.CENTER_VERTICAL;
                            extra = StringUtil.VALUE_NOT_SUPPORT;
                            break;
                    }
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_CENTER_HORIZONTAL:
                    switch (attributeValue) {
                        case XmlAttrValue.TRUE:
                            name = ViewAttrName.CENTER_HORIZONTAL;
                            value = ViewAttrValue.EMPTY;
                            break;
                        case XmlAttrValue.FALSE:
                            name = "//" + ViewAttrName.CENTER_HORIZONTAL;
                            break;
                        default:
                            name = "//" + ViewAttrName.CENTER_HORIZONTAL;
                            extra = StringUtil.VALUE_NOT_SUPPORT;
                            break;
                    }
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_CENTER_IN_PARENT:
                    switch (attributeValue) {
                        case XmlAttrValue.TRUE:
                            name = ViewAttrName.CENTER_IN_PARENT;
                            value = ViewAttrValue.EMPTY;
                            break;
                        case XmlAttrValue.FALSE:
                            name = "//" + ViewAttrName.CENTER_IN_PARENT;
                            break;
                        default:
                            name = "//" + ViewAttrName.CENTER_IN_PARENT;
                            extra = StringUtil.VALUE_NOT_SUPPORT;
                            break;
                    }
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_ALIGN_PARENT_TOP:
                    switch (attributeValue) {
                        case XmlAttrValue.TRUE:
                            name = ViewAttrName.ALIGN_PARENT_TOP;
                            value = ViewAttrValue.EMPTY;
                            break;
                        case XmlAttrValue.FALSE:
                            name = "//" + ViewAttrName.ALIGN_PARENT_TOP;
                            break;
                        default:
                            name = "//" + ViewAttrName.ALIGN_PARENT_TOP;
                            extra = StringUtil.VALUE_NOT_SUPPORT;
                            break;
                    }
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_ALIGN_PARENT_BOTTOM:
                    switch (attributeValue) {
                        case XmlAttrValue.TRUE:
                            name = ViewAttrName.ALIGN_PARENT_BOTTOM;
                            value = ViewAttrValue.EMPTY;
                            break;
                        case XmlAttrValue.FALSE:
                            name = "//" + ViewAttrName.ALIGN_PARENT_BOTTOM;
                            break;
                        default:
                            name = "//" + ViewAttrName.ALIGN_PARENT_BOTTOM;
                            extra = StringUtil.VALUE_NOT_SUPPORT;
                            break;
                    }
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_ALIGN_PARENT_LEFT:
                    switch (attributeValue) {
                        case XmlAttrValue.TRUE:
                            name = ViewAttrName.ALIGN_PARENT_LEFT;
                            value = ViewAttrValue.EMPTY;
                            break;
                        case XmlAttrValue.FALSE:
                            name = "//" + ViewAttrName.ALIGN_PARENT_LEFT;
                            break;
                        default:
                            name = "//" + ViewAttrName.ALIGN_PARENT_LEFT;
                            extra = StringUtil.VALUE_NOT_SUPPORT;
                            break;
                    }
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_ALIGN_PARENT_RIGHT:
                    switch (attributeValue) {
                        case XmlAttrValue.TRUE:
                            name = ViewAttrName.ALIGN_PARENT_RIGHT;
                            value = ViewAttrValue.EMPTY;
                            break;
                        case XmlAttrValue.FALSE:
                            name = "//" + ViewAttrName.ALIGN_PARENT_RIGHT;
                            break;
                        default:
                            name = "//" + ViewAttrName.ALIGN_PARENT_RIGHT;
                            extra = StringUtil.VALUE_NOT_SUPPORT;
                            break;
                    }
                    isEqualsOperator = false;
                    break;
                case XmlAttrName.LAYOUT_ALIGN_BASELINE:
                    name = ViewAttrName.ALIGN_BASELINE;
                    value = AttributeUtil.getId(attributeValue);
                    isEqualsOperator = false;
                    break;
                default:
                    isNotSupportAttribute = true;
                    break;
            }
        } else if (attributeName.contains(XmlAttrName.POPUP_THEME)) {
            name = ViewAttrName.POPUP_THEME;
            value = AttributeUtil.getTheme(attributeValue);
        } else if (attributeName.contains(XmlAttrName.LAYOUT_SCROLL_FLAGS)) {
            name = ViewAttrName.SCROLL_FLAGS;
            value = AttributeUtil.getMultiScrollFlag(attributeValue);
        } else if (attributeName.contains(XmlAttrName.LAYOUT_BEHAVIOR)) {
            name = ViewAttrName.BEHAVIOR;
            if (attributeValue.contains("@")) {
                value = "Class.forName(" + AttributeUtil.getString(attributeValue) + ").newInstance() as CoordinatorLayout.Behavior<*>?";
            } else {
                value = AttributeUtil.getBehavior(attributeValue);
            }
        } else {
            isNotSupportAttribute = true;
        }
        if (isFunction) {
            codeBuilder.append(name).append(" {\n");
            writeTab(deep + 1);
            codeBuilder.append("//").append(value).append("()\n");
            writeTab(deep);
            codeBuilder.append("}\n");
        } else {
            if (isNotSupportAttribute)
                isNote = true;
            if (isNote)
                codeBuilder.append("//");
            if (isEqualsOperator)
                codeBuilder.append(name).append(" = ").append(value);
            else
                codeBuilder.append(name).append("(").append(value).append(")");
            if (isNotSupportAttribute)
                codeBuilder.append(" //not support attribute");
            codeBuilder.append(extra).append("\n");
        }
    }

    private void writeTab(int deep) {
        for (int i = 0; i < deep; i++)
            codeBuilder.append("\t");
    }

    private void writeView(Element root, int deep) {
        if (callback != null)
            callback.onUpdate(progress++);
        List<List<UAttribute>> attributes = AttributeUtil.splitAttributes(root.attributes());
        UAttribute themeAttr = AttributeUtil.filterTheme(attributes.get(0));
        int otherCount = attributes.get(0).size();
        writeViewStart(root.getName(), themeAttr, deep, otherCount == 0 && root.elements().size() == 0);
        for (UAttribute attribute : attributes.get(0)) {
            writeAttribute(root.getName(), attribute.getName(), attribute.getValue(), deep + 1, false);
        }
        for (Element element : root.elements()) {
            writeView(element, deep + 1);
        }
        if (otherCount != 0 || root.elements().size() != 0)
            writeViewEnd(deep);
        writeLayoutParams(attributes.get(1), attributes.get(2), deep);
        if (callback != null)
            callback.onUpdate(progress++);
    }
}
