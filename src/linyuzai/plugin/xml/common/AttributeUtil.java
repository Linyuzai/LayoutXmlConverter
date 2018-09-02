package linyuzai.plugin.xml.common;

import linyuzai.plugin.xml.attr.UAttribute;
import linyuzai.plugin.xml.attr.ViewAttrValue;
import linyuzai.plugin.xml.attr.XmlAttrName;
import linyuzai.plugin.xml.attr.XmlAttrValue;
import org.apache.http.util.TextUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AttributeUtil {

    public static final String VALUE_SHOULD_BE_AN_COLOR_STATE_LIST = " //value should be an color state list";
    public static final String VALUE_SHOULD_BE_AN_ID = " //value should be an id";
    public static final String CAN_NOT_SET_BY_CODE = " //Can not be set by code";

    public static boolean isParamsAttr(String xmlName) {
        return xmlName.equals(XmlAttrName.LAYOUT_WIDTH) || xmlName.equals(XmlAttrName.LAYOUT_HEIGHT);
    }

    public static boolean isLayoutAttr(String xmlName) {
        return xmlName.startsWith(XmlAttrName.LAYOUT_MARGIN) ||
                xmlName.equals(XmlAttrName.LAYOUT_GRAVITY) ||
                xmlName.equals(XmlAttrName.LAYOUT_WEIGHT) ||
                xmlName.equals(XmlAttrName.LAYOUT_ALIGN_PARENT_TOP) ||
                xmlName.equals(XmlAttrName.LAYOUT_ALIGN_PARENT_BOTTOM) ||
                xmlName.equals(XmlAttrName.LAYOUT_ALIGN_PARENT_LEFT) ||
                xmlName.equals(XmlAttrName.LAYOUT_ALIGN_PARENT_RIGHT) ||
                xmlName.equals(XmlAttrName.LAYOUT_ALIGN_TOP) ||
                xmlName.equals(XmlAttrName.LAYOUT_ALIGN_BOTTOM) ||
                xmlName.equals(XmlAttrName.LAYOUT_ALIGN_LEFT) ||
                xmlName.equals(XmlAttrName.LAYOUT_ALIGN_RIGHT) ||
                xmlName.equals(XmlAttrName.LAYOUT_CENTER_IN_PARENT) ||
                xmlName.equals(XmlAttrName.LAYOUT_CENTER_VERTICAL) ||
                xmlName.equals(XmlAttrName.LAYOUT_CENTER_HORIZONTAL) ||
                xmlName.equals(XmlAttrName.LAYOUT_TO_LEFT_OF) ||
                xmlName.equals(XmlAttrName.LAYOUT_TO_RIGHT_OF) ||
                xmlName.equals(XmlAttrName.LAYOUT_ABOVE) ||
                xmlName.equals(XmlAttrName.LAYOUT_BELOW) ||
                xmlName.equals(XmlAttrName.LAYOUT_ALIGN_BASELINE) ||
                xmlName.contains(XmlAttrName.LAYOUT_SCROLL_FLAGS) ||
                xmlName.contains(XmlAttrName.LAYOUT_BEHAVIOR);
    }

    public static int countOfElement(Element root) {
        int count = 1;
        if (root.elements() != null && root.elements().size() > 0) {
            for (Element element : root.elements()) {
                count += countOfElement(element);
            }
            return count;
        }
        return count;
    }

    public static List<List<UAttribute>> splitAttributes(List<Attribute> attributes) {
        List<UAttribute> paramsAttr = new ArrayList<>();
        List<UAttribute> layoutAttr = new ArrayList<>();
        List<UAttribute> otherAttr = new ArrayList<>();
        for (Attribute attr : attributes) {
            //System.out.println(attr.getQualifiedName());
            if (isParamsAttr(attr.getQualifiedName()))
                paramsAttr.add(new UAttribute(attr.getQualifiedName(), attr.getValue()));
            else if (isLayoutAttr(attr.getQualifiedName()))
                layoutAttr.add(new UAttribute(attr.getQualifiedName(), attr.getValue()));
            else
                otherAttr.add(new UAttribute(attr.getQualifiedName(), attr.getValue()));
        }
        if (paramsAttr.size() == 0) {
            UAttribute widthAttr = new UAttribute(XmlAttrName.LAYOUT_WIDTH, XmlAttrValue.LayoutParams.WRAP_CONTENT);
            UAttribute heightAttr = new UAttribute(XmlAttrName.LAYOUT_HEIGHT, XmlAttrValue.LayoutParams.WRAP_CONTENT);
            paramsAttr.add(widthAttr);
            paramsAttr.add(heightAttr);
        } else if (paramsAttr.size() == 1) {
            UAttribute widthOrHeightAttr = paramsAttr.get(0);
            if (widthOrHeightAttr.getName().equals(XmlAttrName.LAYOUT_WIDTH)) {
                UAttribute heightAttr = new UAttribute(XmlAttrName.LAYOUT_HEIGHT, XmlAttrValue.LayoutParams.WRAP_CONTENT);
                paramsAttr.add(heightAttr);
            } else if (widthOrHeightAttr.getName().equals(XmlAttrName.LAYOUT_HEIGHT)) {
                UAttribute widthAttr = new UAttribute(XmlAttrName.LAYOUT_WIDTH, XmlAttrValue.LayoutParams.WRAP_CONTENT);
                paramsAttr.add(0, widthAttr);
            }
        } else if (paramsAttr.size() == 2) {
            if (paramsAttr.get(0).getName().equals(XmlAttrName.LAYOUT_HEIGHT)) {
                Collections.swap(paramsAttr, 0, 1);
            }
        }
        List<List<UAttribute>> splitAttrs = new ArrayList<>();
        splitAttrs.add(otherAttr);
        splitAttrs.add(paramsAttr);
        splitAttrs.add(layoutAttr);
        return splitAttrs;
    }

    public static UAttribute filterTheme(List<UAttribute> uAttributes) {
        Iterator<UAttribute> ui = uAttributes.iterator();
        while (ui.hasNext()) {
            UAttribute ua = ui.next();
            if (ua.getName().contains(XmlAttrName.View.THEME)) {
                ui.remove();
                return ua;
            }
        }
        return null;
    }

    public static boolean isNullOrEmpty(String xmlAttribute) {
        return TextUtils.isEmpty(xmlAttribute) || xmlAttribute.equalsIgnoreCase("@null");
    }

    @NotNull
    public static String getAlpha(String xmlAlpha) {
        if (StringUtil.isAlpha(xmlAlpha))
            return xmlAlpha + "f";
        else if (TextUtils.isEmpty(xmlAlpha))
            return "0f";
        else
            return getInteger(xmlAlpha, true);
    }

    @NotNull
    public static String getTheme(String xmlTheme) {
        String attr = getAttrOrAndroidAttr(xmlTheme, true);
        if (attr != null)
            return attr;
        else if (xmlTheme.startsWith("@style/"))
            return "R.style." + xmlTheme.substring(7).replaceAll("\\.", "_");
        else if (xmlTheme.startsWith("@android:style/"))
            return "android.R.style." + xmlTheme.substring(15).replaceAll("\\.", "_");
        else if (TextUtils.isEmpty(xmlTheme))
            return "0";
        else
            return xmlTheme + StringUtil.VALUE_NOT_SUPPORT;
    }

    @NotNull
    public static String getId(String xmlId) {
        if (xmlId.startsWith("@+id/"))
            return "R.id." + xmlId.substring(5);
        else if (xmlId.startsWith("@id/"))
            return "R.id." + xmlId.substring(4);
        else if (xmlId.startsWith("@android:id/"))
            return "android.R.id." + xmlId.substring(12);
        else if (TextUtils.isEmpty(xmlId))
            return "0";
        else
            return xmlId + StringUtil.VALUE_NOT_SUPPORT;
    }

    @Nullable
    public static String getAttrOrAndroidAttr(String xmlAttrOrAndroidAttr, boolean replace) {
        if (xmlAttrOrAndroidAttr.startsWith("?attr/")) {
            if (replace)
                return "R.attr." + xmlAttrOrAndroidAttr.substring(6).replaceAll("\\.", "_");
            else
                return "R.attr." + xmlAttrOrAndroidAttr.substring(6);
        } else if (xmlAttrOrAndroidAttr.startsWith("?android:attr/")) {
            if (replace)
                return "android.R.attr." + xmlAttrOrAndroidAttr.substring(14).replaceAll("\\.", "_");
            else
                return "android.R.attr." + xmlAttrOrAndroidAttr.substring(14);
        } else if (xmlAttrOrAndroidAttr.startsWith("?attr/android:")) {
            if (replace)
                return "android.R.attr." + xmlAttrOrAndroidAttr.substring(14).replaceAll("\\.", "_");
            else
                return "android.R.attr." + xmlAttrOrAndroidAttr.substring(14);
        } else if (xmlAttrOrAndroidAttr.startsWith("?android:")) {
            if (replace)
                return "android.R.attr." + xmlAttrOrAndroidAttr.substring(9).replaceAll("\\.", "_");
            else
                return "android.R.attr." + xmlAttrOrAndroidAttr.substring(9);
        } else if (xmlAttrOrAndroidAttr.startsWith("?")) {
            if (replace)
                return "R.attr." + xmlAttrOrAndroidAttr.substring(1).replaceAll("\\.", "_");
            else
                return "R.attr." + xmlAttrOrAndroidAttr.substring(1);
        } else
            return null;
    }

    @NotNull
    public static String getWidthOrHeight(String value) {
        if (value.equals(XmlAttrValue.LayoutParams.MATCH_PARENT))
            return ViewAttrValue.LayoutParams.MATCH_PARENT;
        else if (value.equals(XmlAttrValue.LayoutParams.WRAP_CONTENT))
            return ViewAttrValue.LayoutParams.WRAP_CONTENT;
        else
            return getDimension(value, false);

    }

    @NotNull
    public static String getWeight(String xmlWeight) {
        if (StringUtil.isInteger(xmlWeight))
            return xmlWeight + "f";
        else return xmlWeight + StringUtil.VALUE_NOT_SUPPORT;

    }

    @NotNull
    public static String getDimension(String xmlDimension, boolean toFloat) {
        String attr = getAttrOrAndroidAttr(xmlDimension, false);
        if (attr != null) {
            if (toFloat)
                return "dimenAttr(" + attr + ").toFloat()";
            else
                return "dimenAttr(" + attr + ")";
        } else if (xmlDimension.contains("dp")) {
            String dpValue = xmlDimension.split("dp")[0];
            if (dpValue.equals("0")) {
                if (toFloat)
                    return "0f";
                else
                    return "0";
            } else {
                if (toFloat)
                    return "dip(" + xmlDimension.split("dp")[0] + ").toFloat()";
                else
                    return "dip(" + xmlDimension.split("dp")[0] + ")";
            }
        } else if (xmlDimension.contains("in")) {
            if (toFloat)
                return xmlDimension.split("in")[0] + "f //in";
            else
                return xmlDimension.split("in")[0] + " //in";
        } else if (xmlDimension.contains("mm")) {
            if (toFloat)
                return xmlDimension.split("mm")[0] + "f //mm";
            else
                return xmlDimension.split("mm")[0] + " //mm";
        } else if (xmlDimension.contains("pt")) {
            if (toFloat)
                return xmlDimension.split("pt")[0] + "f //pt";
            else
                return xmlDimension.split("pt")[0] + " //pt";
        } else if (xmlDimension.contains("px")) {
            if (toFloat)
                return xmlDimension.split("px")[0] + "f //px";
            else
                return xmlDimension.split("px")[0] + " //px";
        } else if (xmlDimension.contains("sp")) {
            if (toFloat)
                return xmlDimension.split("sp")[0] + "f //sp";
            else
                return xmlDimension.split("sp")[0] + " //sp";
        } else if (xmlDimension.startsWith("@dimen/")) {
            if (toFloat)
                return "dimen(R.dimen." + xmlDimension.substring(7) + ").toFloat()";
            else
                return "dimen(R.dimen." + xmlDimension.substring(7) + ")";
        } else if (xmlDimension.startsWith("@android:dimen/")) {
            if (toFloat)
                return "dimen(android.R.dimen." + xmlDimension.substring(15) + ").toFloat()";
            else
                return "dimen(android.R.dimen." + xmlDimension.substring(15) + ")";
        } else if (TextUtils.isEmpty(xmlDimension)) {
            if (toFloat)
                return "0f";
            else
                return "0";
        } else return xmlDimension + StringUtil.VALUE_NOT_SUPPORT;
    }

    @NotNull
    public static String getImage(String xmlImage) {
        if (xmlImage.startsWith("@mipmap/"))
            return "R.mipmap." + xmlImage.substring(8);
        else if (xmlImage.startsWith("@drawable/"))
            return "R.drawable." + xmlImage.substring(10);
        else if (xmlImage.startsWith("@android:mipmap/"))
            return "android.R.mipmap." + xmlImage.substring(16);
        else if (xmlImage.startsWith("@android:drawable/"))
            return "android.R.drawable." + xmlImage.substring(18);
        else if (TextUtils.isEmpty(xmlImage))
            return "0";
        else
            return xmlImage + StringUtil.VALUE_NOT_SUPPORT;
    }

    @NotNull
    public static String getDrawable(String xmlDrawable) {
        String drawable = getImage(xmlDrawable);
        if (!drawable.contains(StringUtil.VALUE_NOT_SUPPORT)) {
            return "resources.getDrawable(" + drawable + ")";
        }
        String color = getColor(xmlDrawable);
        if (!color.contains(StringUtil.VALUE_NOT_SUPPORT)) {
            ImportUtil.support(ImportUtil.COLOR_DRAWABLE);
            return "ColorDrawable(" + color + ")";
        }
        return xmlDrawable + StringUtil.VALUE_NOT_SUPPORT;
    }

    @NotNull
    public static String getColorStateList(String xmlColor) {
        String attr = getAttrOrAndroidAttr(xmlColor, false);
        if (attr != null)
            return "resources.getColorStateList(" + attr + ")";
        else if (xmlColor.startsWith("@color/"))
            return "resources.getColorStateList(R.color." + xmlColor.substring(7) + ")";
        else if (xmlColor.startsWith("@android:color/"))
            return "resources.getColorStateList(android.R.color" + xmlColor.substring(15) + ")";
        else
            return xmlColor + StringUtil.VALUE_NOT_SUPPORT;
    }

    @NotNull
    public static String getColor(String xmlColor) {
        String attr = getAttrOrAndroidAttr(xmlColor, false);
        if (attr != null)
            return "colorAttr(" + attr + ")";
        else if (xmlColor.startsWith("@color/"))
            return "resources.getColor(R.color." + xmlColor.substring(7) + ")";
        else if (xmlColor.startsWith("@android:color/"))
            return getSystemColor(xmlColor.substring(15));
        else if (TextUtils.isEmpty(xmlColor))
            return "0";
        else
            return getParseColor(xmlColor);
    }

    @NotNull
    @Contract(pure = true)
    public static String getSystemColor(String systemColor) {
        switch (systemColor) {
            case "transparent":
                return "Color.TRANSPARENT";
            case "white":
                return "Color.WHITE";
            case "black":
                return "Color.BLACK";
            case "red":
                return "Color.RED";
            default:
                return "resources.getColor(android.R.color." + systemColor + ")";
        }
    }

    @NotNull
    @Contract(pure = true)
    public static String getParseColor(String parseColor) {
        if (parseColor.startsWith("#")) {
            switch (parseColor) {
                case "#000000":
                    return "Color.BLACK";
                case "#444444":
                    return "Color.DKGRAY";
                case "#888888":
                    return "Color.GRAY";
                case "#CCCCCC":
                    return "Color.LTGRAY";
                case "#FFFFFF":
                    return "Color.WHITE";
                case "#FF0000":
                    return "Color.RED";
                case "#00FF00":
                    return "Color.GREEN";
                case "#0000FF":
                    return "Color.BLUE";
                case "#FFFF00":
                    return "Color.YELLOW";
                case "#00FFFF":
                    return "Color.CYAN";
                case "#FF00FF":
                    return "Color.MAGENTA";
                default:
                    return "Color.parseColor(\"" + parseColor + "\")";
            }
        } else
            return parseColor + StringUtil.VALUE_NOT_SUPPORT;
    }

    @NotNull
    public static String getString(String xmlString) {
        if (xmlString.startsWith("@string/"))
            return "resources.getString(R.string." + xmlString.substring(8) + ")";
        else if (xmlString.startsWith("@android:string/"))
            return "resources.getString(android.R.string." + xmlString.substring(16) + ")";
        else if (TextUtils.isEmpty(xmlString))
            return "\"\"";
        else
            return "\"" + xmlString + "\"";
    }

    @NotNull
    public static String getBoolean(String xmlBoolean) {
        if (xmlBoolean.equals(XmlAttrValue.TRUE))
            return XmlAttrValue.TRUE;
        else if (xmlBoolean.equals(XmlAttrValue.FALSE))
            return XmlAttrValue.FALSE;
        if (xmlBoolean.startsWith("@bool/"))
            return "resources.getBoolean(R.bool." + xmlBoolean.substring(6) + ")";
        else if (xmlBoolean.startsWith("@android:bool/"))
            return "resources.getBoolean(android.R.bool." + xmlBoolean.substring(14) + ")";
        else if (TextUtils.isEmpty(xmlBoolean))
            return XmlAttrValue.FALSE;
        else
            return xmlBoolean + StringUtil.VALUE_NOT_SUPPORT;
    }

    @NotNull
    public static String getInteger(String xmlInteger, boolean toFloat) {
        if (xmlInteger.startsWith("@integer/")) {
            if (toFloat)
                return "resources.getInteger(R.integer." + xmlInteger.substring(9) + ").toFloat()";
            else
                return "resources.getInteger(R.integer." + xmlInteger.substring(9) + ")";
        } else if (xmlInteger.startsWith("@android:integer/")) {
            if (toFloat)
                return "resources.getInteger(android.R.integer." + xmlInteger.substring(17) + ").toFloat()";
            else
                return "resources.getInteger(android.R.integer." + xmlInteger.substring(17) + ")";
        } else if (TextUtils.isEmpty(xmlInteger)) {
            if (toFloat)
                return "0f";
            else
                return "0";
        } else if (StringUtil.isInteger(xmlInteger)) {
            if (toFloat)
                return Integer.valueOf(xmlInteger).toString() + "f";
            else
                return Integer.valueOf(xmlInteger).toString();
        } else
            return xmlInteger + StringUtil.VALUE_NOT_SUPPORT;
    }

    @NotNull
    public static String getFloat(String xmlFloat) {
        if (StringUtil.isFloat(xmlFloat))
            return Float.valueOf(xmlFloat).toString() + "f";
        else
            return getInteger(xmlFloat, true);
    }

    @NotNull
    public static String getOrientation(String xmlOrientation) {
        ImportUtil.support(ImportUtil.LINEAR_LAYOUT);
        switch (xmlOrientation) {
            case XmlAttrValue.Orientation.VERTICAL:
                return ViewAttrValue.Orientation.VERTICAL;
            case XmlAttrValue.Orientation.HORIZONTAL:
                return ViewAttrValue.Orientation.HORIZONTAL;
            default:
                if (TextUtils.isEmpty(xmlOrientation))
                    return XmlAttrValue.Orientation.VERTICAL;
                else
                    return xmlOrientation + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    @NotNull
    public static String getMultiGravity(String xmlMultiGravity) {
        ImportUtil.support(ImportUtil.GRAVITY);
        String[] xmlGravityArray = xmlMultiGravity.replaceAll("\\s*", "").split("\\|");
        List<String> viewGravityList = new ArrayList<>();
        Arrays.stream(xmlGravityArray).forEach(it -> viewGravityList.add(getGravity(it)));
        return String.join(" or ", viewGravityList);
    }

    @NotNull
    public static String getGravity(String xmlGravity) {
        switch (xmlGravity) {
            case XmlAttrValue.Gravity.CENTER:
                return ViewAttrValue.Gravity.CENTER;
            case XmlAttrValue.Gravity.CENTER_VERTICAL:
                return ViewAttrValue.Gravity.CENTER_VERTICAL;
            case XmlAttrValue.Gravity.CENTER_HORIZONTAL:
                return ViewAttrValue.Gravity.CENTER_HORIZONTAL;
            case XmlAttrValue.Gravity.START:
                return ViewAttrValue.Gravity.START;
            case XmlAttrValue.Gravity.END:
                return ViewAttrValue.Gravity.END;
            case XmlAttrValue.Gravity.TOP:
                return ViewAttrValue.Gravity.TOP;
            case XmlAttrValue.Gravity.BOTTOM:
                return ViewAttrValue.Gravity.BOTTOM;
            case XmlAttrValue.Gravity.LEFT:
                return ViewAttrValue.Gravity.LEFT;
            case XmlAttrValue.Gravity.RIGHT:
                return ViewAttrValue.Gravity.RIGHT;
            case XmlAttrValue.Gravity.FILL:
                return ViewAttrValue.Gravity.FILL;
            case XmlAttrValue.Gravity.FILL_VERTICAL:
                return ViewAttrValue.Gravity.FILL_VERTICAL;
            case XmlAttrValue.Gravity.FILL_HORIZONTAL:
                return ViewAttrValue.Gravity.FILL_HORIZONTAL;
            case XmlAttrValue.Gravity.CLIP_VERTICAL:
                return ViewAttrValue.Gravity.CLIP_VERTICAL;
            case XmlAttrValue.Gravity.CLIP_HORIZONTAL:
                return ViewAttrValue.Gravity.CLIP_HORIZONTAL;
            default:
                if (TextUtils.isEmpty(xmlGravity))
                    return XmlAttrValue.Gravity.LEFT;
                else
                    return xmlGravity + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    @NotNull
    public static String getScaleType(String xmlScaleType) {
        ImportUtil.support(ImportUtil.IMAGE_VIEW);
        switch (xmlScaleType) {
            case XmlAttrValue.ScaleType.CENTER:
                return ViewAttrValue.ScaleType.CENTER;
            case XmlAttrValue.ScaleType.CENTER_CROP:
                return ViewAttrValue.ScaleType.CENTER_CROP;
            case XmlAttrValue.ScaleType.CENTER_INSIDE:
                return ViewAttrValue.ScaleType.CENTER_INSIDE;
            case XmlAttrValue.ScaleType.FIT_CENTER:
                return ViewAttrValue.ScaleType.FIT_CENTER;
            case XmlAttrValue.ScaleType.FIT_START:
                return ViewAttrValue.ScaleType.FIT_START;
            case XmlAttrValue.ScaleType.FIT_END:
                return ViewAttrValue.ScaleType.FIT_END;
            case XmlAttrValue.ScaleType.FIT_XY:
                return ViewAttrValue.ScaleType.FIT_XY;
            case XmlAttrValue.ScaleType.MATRIX:
                return ViewAttrValue.ScaleType.MATRIX;
            default:
                if (TextUtils.isEmpty(xmlScaleType))
                    return XmlAttrValue.ScaleType.CENTER;
                else
                    return xmlScaleType + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    @NotNull
    public static String getTextStyle(String xmlTextStyle) {
        ImportUtil.support(ImportUtil.TYPEFACE);
        boolean isBold = false;
        boolean isItalic = false;
        String[] textStyles = xmlTextStyle.split("\\|");
        for (String textStyle : textStyles) {
            if (textStyle.equals(XmlAttrValue.TextStyle.BOLD))
                isBold = true;
            else if (textStyle.equals(XmlAttrValue.TextStyle.ITALIC))
                isItalic = true;
        }
        if (isBold && isItalic)
            return ViewAttrValue.TextStyle.BOLD_ITALIC;
        else if (isBold)
            return ViewAttrValue.TextStyle.BOLD;
        else if (isItalic)
            return ViewAttrValue.TextStyle.ITALIC;
        else
            return ViewAttrValue.TextStyle.NORMAL;
    }

    @Contract(pure = true)
    public static String getInputType(String xmlInputType) {
        ImportUtil.support(ImportUtil.INPUT_TYPE);
        switch (xmlInputType) {
            case XmlAttrValue.InputType.NONE:
                return ViewAttrValue.InputType.NONE;
            case XmlAttrValue.InputType.TEXT:
                return ViewAttrValue.InputType.TEXT;
            case XmlAttrValue.InputType.TEXT_CAP_CHARACTERS:
                return ViewAttrValue.InputType.TEXT_CAP_CHARACTERS;
            case XmlAttrValue.InputType.TEXT_CAP_WORDS:
                return ViewAttrValue.InputType.TEXT_CAP_WORDS;
            case XmlAttrValue.InputType.TEXT_CAP_SENTENCES:
                return ViewAttrValue.InputType.TEXT_CAP_SENTENCES;
            case XmlAttrValue.InputType.TEXT_AUTO_CORRECT:
                return ViewAttrValue.InputType.TEXT_AUTO_CORRECT;
            case XmlAttrValue.InputType.TEXT_AUTO_COMPLETE:
                return ViewAttrValue.InputType.TEXT_AUTO_COMPLETE;
            case XmlAttrValue.InputType.TEXT_MULTI_LINE:
                return ViewAttrValue.InputType.TEXT_MULTI_LINE;
            case XmlAttrValue.InputType.TEXT_IME_MULTI_LINE:
                return ViewAttrValue.InputType.TEXT_IME_MULTI_LINE;
            case XmlAttrValue.InputType.TEXT_NO_SUGGESTIONS:
                return ViewAttrValue.InputType.TEXT_NO_SUGGESTIONS;
            case XmlAttrValue.InputType.TEXT_URI:
                return ViewAttrValue.InputType.TEXT_URI;
            case XmlAttrValue.InputType.TEXT_EMAIL_ADDRESS:
                return ViewAttrValue.InputType.TEXT_EMAIL_ADDRESS;
            case XmlAttrValue.InputType.TEXT_EMAIL_SUBJECT:
                return ViewAttrValue.InputType.TEXT_EMAIL_SUBJECT;
            case XmlAttrValue.InputType.TEXT_SHORT_MESSAGE:
                return ViewAttrValue.InputType.TEXT_SHORT_MESSAGE;
            case XmlAttrValue.InputType.TEXT_LONG_MESSAGE:
                return ViewAttrValue.InputType.TEXT_LONG_MESSAGE;
            case XmlAttrValue.InputType.TEXT_PERSON_NAME:
                return ViewAttrValue.InputType.TEXT_PERSON_NAME;
            case XmlAttrValue.InputType.TEXT_POSTAL_ADDRESS:
                return ViewAttrValue.InputType.TEXT_POSTAL_ADDRESS;
            case XmlAttrValue.InputType.TEXT_PASSWORD:
                return ViewAttrValue.InputType.TEXT_PASSWORD;
            case XmlAttrValue.InputType.TEXT_VISIBLE_PASSWORD:
                return ViewAttrValue.InputType.TEXT_VISIBLE_PASSWORD;
            case XmlAttrValue.InputType.TEXT_WEB_EDIT_TEXT:
                return ViewAttrValue.InputType.TEXT_WEB_EDIT_TEXT;
            case XmlAttrValue.InputType.TEXT_FILTER:
                return ViewAttrValue.InputType.TEXT_FILTER;
            case XmlAttrValue.InputType.TEXT_PHONETIC:
                return ViewAttrValue.InputType.TEXT_PHONETIC;
            case XmlAttrValue.InputType.TEXT_WEB_EMAIL_ADDRESS:
                return ViewAttrValue.InputType.TEXT_WEB_EMAIL_ADDRESS;
            case XmlAttrValue.InputType.TEXT_WEB_PASSWORD:
                return ViewAttrValue.InputType.TEXT_WEB_PASSWORD;
            case XmlAttrValue.InputType.NUMBER:
                return ViewAttrValue.InputType.NUMBER;
            case XmlAttrValue.InputType.NUMBER_SIGNED:
                return ViewAttrValue.InputType.NUMBER_SIGNED;
            case XmlAttrValue.InputType.NUMBER_DECIMAL:
                return ViewAttrValue.InputType.NUMBER_DECIMAL;
            case XmlAttrValue.InputType.NUMBER_PASSWORD:
                return ViewAttrValue.InputType.NUMBER_PASSWORD;
            case XmlAttrValue.InputType.PHONE:
                return ViewAttrValue.InputType.PHONE;
            case XmlAttrValue.InputType.DATETIME:
                return ViewAttrValue.InputType.DATETIME;
            case XmlAttrValue.InputType.DATE:
                return ViewAttrValue.InputType.DATE;
            case XmlAttrValue.InputType.TIME:
                return ViewAttrValue.InputType.TIME;
            default:
                return ViewAttrValue.InputType.TEXT;
        }
    }

    @NotNull
    public static String getMultiScrollFlags(String xmlMultiScrollFlags) {
        String[] xmlScrollFlagsArray = xmlMultiScrollFlags.replaceAll("\\s*", "").split("\\|");
        List<String> viewScrollFlagsList = new ArrayList<>();
        Arrays.stream(xmlScrollFlagsArray).forEach(it -> viewScrollFlagsList.add(getScrollFlags(it)));
        return String.join(" or ", viewScrollFlagsList);
    }

    @NotNull
    public static String getScrollFlags(String xmlScrollFlags) {
        switch (xmlScrollFlags) {
            case XmlAttrValue.ScrollFlag.ENTER_ALWAYS:
                return ViewAttrValue.ScrollFlag.ENTER_ALWAYS;
            case XmlAttrValue.ScrollFlag.ENTER_ALWAYS_COLLAPSED:
                return ViewAttrValue.ScrollFlag.ENTER_ALWAYS_COLLAPSED;
            case XmlAttrValue.ScrollFlag.EXIT_UNTIL_COLLAPSED:
                return ViewAttrValue.ScrollFlag.EXIT_UNTIL_COLLAPSED;
            case XmlAttrValue.ScrollFlag.SCROLL:
                return ViewAttrValue.ScrollFlag.SCROLL;
            case XmlAttrValue.ScrollFlag.SNAP:
                return ViewAttrValue.ScrollFlag.SNAP;
            default:
                if (TextUtils.isEmpty(xmlScrollFlags))
                    return XmlAttrValue.ScrollFlag.SCROLL;
                else
                    return xmlScrollFlags + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    @NotNull
    public static String getBehavior(String attributeValue) {
        if (TextUtils.isEmpty(attributeValue))
            return "null";
        else
            return attributeValue.replaceAll("\\$", ".") + "()";
    }

    public static String getAccessibilityLiveRegion(String xmlAccessibilityLiveRegion) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlAccessibilityLiveRegion) {
            case XmlAttrValue.AccessibilityLiveRegion.ASSERTIVE:
                return ViewAttrValue.AccessibilityLiveRegion.ASSERTIVE;
            case XmlAttrValue.AccessibilityLiveRegion.NONE:
                return ViewAttrValue.AccessibilityLiveRegion.NONE;
            case XmlAttrValue.AccessibilityLiveRegion.POLITE:
                return ViewAttrValue.AccessibilityLiveRegion.POLITE;
            default:
                if (TextUtils.isEmpty(xmlAccessibilityLiveRegion))
                    return XmlAttrValue.AccessibilityLiveRegion.NONE;
                else
                    return xmlAccessibilityLiveRegion + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    @NotNull
    public static String getMultiAutofillHints(String xmlMultiAutofillHints) {
        ImportUtil.support(ImportUtil.VIEW);
        String[] xmlAutofillHintsArray = xmlMultiAutofillHints.replaceAll("\\s*", "").split("\\|");
        List<String> viewAutofillHintsList = new ArrayList<>();
        Arrays.stream(xmlAutofillHintsArray).forEach(it -> viewAutofillHintsList.add(getAutofillHints(it)));
        return String.join(" ,", viewAutofillHintsList);
    }

    public static String getAutofillHints(String xmlAutofillHints) {
        switch (xmlAutofillHints) {
            case XmlAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_DATE:
                return ViewAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_DATE;
            case XmlAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_DAY:
                return ViewAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_DAY;
            case XmlAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_MONTH:
                return ViewAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_MONTH;
            case XmlAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_YEAR:
                return ViewAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_YEAR;
            case XmlAttrValue.AutofillHint.CREDIT_CARD_NUMBER:
                return ViewAttrValue.AutofillHint.CREDIT_CARD_NUMBER;
            case XmlAttrValue.AutofillHint.CREDIT_CARD_SECURITY_CODE:
                return ViewAttrValue.AutofillHint.CREDIT_CARD_SECURITY_CODE;
            case XmlAttrValue.AutofillHint.EMAIL_ADDRESS:
                return ViewAttrValue.AutofillHint.EMAIL_ADDRESS;
            case XmlAttrValue.AutofillHint.NAME:
                return ViewAttrValue.AutofillHint.NAME;
            case XmlAttrValue.AutofillHint.PASSWORD:
                return ViewAttrValue.AutofillHint.PASSWORD;
            case XmlAttrValue.AutofillHint.PHONE:
                return ViewAttrValue.AutofillHint.PHONE;
            case XmlAttrValue.AutofillHint.POSTAL_ADDRESS:
                return ViewAttrValue.AutofillHint.POSTAL_ADDRESS;
            case XmlAttrValue.AutofillHint.POSTAL_CODE:
                return ViewAttrValue.AutofillHint.POSTAL_CODE;
            case XmlAttrValue.AutofillHint.USERNAME:
                return ViewAttrValue.AutofillHint.USERNAME;
            default:
                return "\"" + xmlAutofillHints + "\"";
        }
    }

    public static String getPorterDuffMode(String xmlPorterDuffMode) {
        ImportUtil.support(ImportUtil.PORTER_DUFF);
        switch (xmlPorterDuffMode) {
            case XmlAttrValue.PorterDuffMode.ADD:
                return ViewAttrValue.PorterDuffMode.ADD;
            case XmlAttrValue.PorterDuffMode.CLEAR:
                return ViewAttrValue.PorterDuffMode.CLEAR;
            case XmlAttrValue.PorterDuffMode.DARKEN:
                return ViewAttrValue.PorterDuffMode.DARKEN;
            case XmlAttrValue.PorterDuffMode.DST:
                return ViewAttrValue.PorterDuffMode.DST;
            case XmlAttrValue.PorterDuffMode.DST_ATOP:
                return ViewAttrValue.PorterDuffMode.DST_ATOP;
            case XmlAttrValue.PorterDuffMode.DST_IN:
                return ViewAttrValue.PorterDuffMode.DST_IN;
            case XmlAttrValue.PorterDuffMode.DST_OUT:
                return ViewAttrValue.PorterDuffMode.DST_OUT;
            case XmlAttrValue.PorterDuffMode.DST_OVER:
                return ViewAttrValue.PorterDuffMode.DST_OVER;
            case XmlAttrValue.PorterDuffMode.LIGHTEN:
                return ViewAttrValue.PorterDuffMode.LIGHTEN;
            case XmlAttrValue.PorterDuffMode.MULTIPLY:
                return ViewAttrValue.PorterDuffMode.MULTIPLY;
            case XmlAttrValue.PorterDuffMode.OVERLAY:
                return ViewAttrValue.PorterDuffMode.OVERLAY;
            case XmlAttrValue.PorterDuffMode.SCREEN:
                return ViewAttrValue.PorterDuffMode.SCREEN;
            case XmlAttrValue.PorterDuffMode.SRC:
                return ViewAttrValue.PorterDuffMode.SRC;
            case XmlAttrValue.PorterDuffMode.SRC_ATOP:
                return ViewAttrValue.PorterDuffMode.SRC_ATOP;
            case XmlAttrValue.PorterDuffMode.SRC_IN:
                return ViewAttrValue.PorterDuffMode.SRC_IN;
            case XmlAttrValue.PorterDuffMode.SRC_OUT:
                return ViewAttrValue.PorterDuffMode.SRC_OUT;
            case XmlAttrValue.PorterDuffMode.SRC_OVER:
                return ViewAttrValue.PorterDuffMode.SRC_OVER;
            case XmlAttrValue.PorterDuffMode.XOR:
                return ViewAttrValue.PorterDuffMode.XOR;
            default:
                return xmlPorterDuffMode + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getDrawingCacheQuality(String xmlDrawingCacheQuality) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlDrawingCacheQuality) {
            case XmlAttrValue.DrawingCacheQuality.AUTO:
                return ViewAttrValue.DrawingCacheQuality.AUTO;
            case XmlAttrValue.DrawingCacheQuality.HIGH:
                return ViewAttrValue.DrawingCacheQuality.HIGH;
            case XmlAttrValue.DrawingCacheQuality.LOW:
                return ViewAttrValue.DrawingCacheQuality.LOW;
            default:
                return xmlDrawingCacheQuality + StringUtil.VALUE_NOT_SUPPORT;
        }
    }
}
