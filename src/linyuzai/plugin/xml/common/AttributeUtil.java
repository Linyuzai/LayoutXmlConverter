package linyuzai.plugin.xml.common;

import linyuzai.plugin.xml.attr.UAttribute;
import linyuzai.plugin.xml.attr.WidgetAttrValue;
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

    public static boolean isParamsAttr(String xmlName) {
        return xmlName.equals(XmlAttrName.ViewGroup.LayoutParams.LAYOUT_WIDTH) || xmlName.equals(XmlAttrName.ViewGroup.LayoutParams.LAYOUT_HEIGHT);
    }

    public static boolean isLayoutAttr(String xmlName) {
        return xmlName.startsWith(XmlAttrName.ViewGroup.LayoutParams.LAYOUT_MARGIN) ||
                xmlName.equals(XmlAttrName.ViewGroup.LayoutParams.LAYOUT_GRAVITY) ||
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
            UAttribute widthAttr = new UAttribute(XmlAttrName.ViewGroup.LayoutParams.LAYOUT_WIDTH, XmlAttrValue.LayoutParams.WRAP_CONTENT);
            UAttribute heightAttr = new UAttribute(XmlAttrName.ViewGroup.LayoutParams.LAYOUT_HEIGHT, XmlAttrValue.LayoutParams.WRAP_CONTENT);
            paramsAttr.add(widthAttr);
            paramsAttr.add(heightAttr);
        } else if (paramsAttr.size() == 1) {
            UAttribute widthOrHeightAttr = paramsAttr.get(0);
            if (widthOrHeightAttr.getName().equals(XmlAttrName.ViewGroup.LayoutParams.LAYOUT_WIDTH)) {
                UAttribute heightAttr = new UAttribute(XmlAttrName.ViewGroup.LayoutParams.LAYOUT_HEIGHT, XmlAttrValue.LayoutParams.WRAP_CONTENT);
                paramsAttr.add(heightAttr);
            } else if (widthOrHeightAttr.getName().equals(XmlAttrName.ViewGroup.LayoutParams.LAYOUT_HEIGHT)) {
                UAttribute widthAttr = new UAttribute(XmlAttrName.ViewGroup.LayoutParams.LAYOUT_WIDTH, XmlAttrValue.LayoutParams.WRAP_CONTENT);
                paramsAttr.add(0, widthAttr);
            }
        } else if (paramsAttr.size() == 2) {
            if (paramsAttr.get(0).getName().equals(XmlAttrName.ViewGroup.LayoutParams.LAYOUT_HEIGHT)) {
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
            return WidgetAttrValue.LayoutParams.MATCH_PARENT;
        else if (value.equals(XmlAttrValue.LayoutParams.WRAP_CONTENT))
            return WidgetAttrValue.LayoutParams.WRAP_CONTENT;
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
                ImportUtil.support(ImportUtil.COLOR);
                return "Color.TRANSPARENT";
            case "white":
                ImportUtil.support(ImportUtil.COLOR);
                return "Color.WHITE";
            case "black":
                ImportUtil.support(ImportUtil.COLOR);
                return "Color.BLACK";
            case "red":
                ImportUtil.support(ImportUtil.COLOR);
                return "Color.RED";
            default:
                return "resources.getColor(android.R.color." + systemColor + ")";
        }
    }

    @NotNull
    @Contract(pure = true)
    public static String getParseColor(String parseColor) {
        ImportUtil.support(ImportUtil.COLOR);
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
                return WidgetAttrValue.Orientation.VERTICAL;
            case XmlAttrValue.Orientation.HORIZONTAL:
                return WidgetAttrValue.Orientation.HORIZONTAL;
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
                return WidgetAttrValue.Gravity.CENTER;
            case XmlAttrValue.Gravity.CENTER_VERTICAL:
                return WidgetAttrValue.Gravity.CENTER_VERTICAL;
            case XmlAttrValue.Gravity.CENTER_HORIZONTAL:
                return WidgetAttrValue.Gravity.CENTER_HORIZONTAL;
            case XmlAttrValue.Gravity.START:
                return WidgetAttrValue.Gravity.START;
            case XmlAttrValue.Gravity.END:
                return WidgetAttrValue.Gravity.END;
            case XmlAttrValue.Gravity.TOP:
                return WidgetAttrValue.Gravity.TOP;
            case XmlAttrValue.Gravity.BOTTOM:
                return WidgetAttrValue.Gravity.BOTTOM;
            case XmlAttrValue.Gravity.LEFT:
                return WidgetAttrValue.Gravity.LEFT;
            case XmlAttrValue.Gravity.RIGHT:
                return WidgetAttrValue.Gravity.RIGHT;
            case XmlAttrValue.Gravity.FILL:
                return WidgetAttrValue.Gravity.FILL;
            case XmlAttrValue.Gravity.FILL_VERTICAL:
                return WidgetAttrValue.Gravity.FILL_VERTICAL;
            case XmlAttrValue.Gravity.FILL_HORIZONTAL:
                return WidgetAttrValue.Gravity.FILL_HORIZONTAL;
            case XmlAttrValue.Gravity.CLIP_VERTICAL:
                return WidgetAttrValue.Gravity.CLIP_VERTICAL;
            case XmlAttrValue.Gravity.CLIP_HORIZONTAL:
                return WidgetAttrValue.Gravity.CLIP_HORIZONTAL;
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
                return WidgetAttrValue.ScaleType.CENTER;
            case XmlAttrValue.ScaleType.CENTER_CROP:
                return WidgetAttrValue.ScaleType.CENTER_CROP;
            case XmlAttrValue.ScaleType.CENTER_INSIDE:
                return WidgetAttrValue.ScaleType.CENTER_INSIDE;
            case XmlAttrValue.ScaleType.FIT_CENTER:
                return WidgetAttrValue.ScaleType.FIT_CENTER;
            case XmlAttrValue.ScaleType.FIT_START:
                return WidgetAttrValue.ScaleType.FIT_START;
            case XmlAttrValue.ScaleType.FIT_END:
                return WidgetAttrValue.ScaleType.FIT_END;
            case XmlAttrValue.ScaleType.FIT_XY:
                return WidgetAttrValue.ScaleType.FIT_XY;
            case XmlAttrValue.ScaleType.MATRIX:
                return WidgetAttrValue.ScaleType.MATRIX;
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
            return WidgetAttrValue.TextStyle.BOLD_ITALIC;
        else if (isBold)
            return WidgetAttrValue.TextStyle.BOLD;
        else if (isItalic)
            return WidgetAttrValue.TextStyle.ITALIC;
        else
            return WidgetAttrValue.TextStyle.NORMAL;
    }

    @Contract(pure = true)
    public static String getInputType(String xmlInputType) {
        ImportUtil.support(ImportUtil.INPUT_TYPE);
        switch (xmlInputType) {
            case XmlAttrValue.InputType.NONE:
                return WidgetAttrValue.InputType.NONE;
            case XmlAttrValue.InputType.TEXT:
                return WidgetAttrValue.InputType.TEXT;
            case XmlAttrValue.InputType.TEXT_CAP_CHARACTERS:
                return WidgetAttrValue.InputType.TEXT_CAP_CHARACTERS;
            case XmlAttrValue.InputType.TEXT_CAP_WORDS:
                return WidgetAttrValue.InputType.TEXT_CAP_WORDS;
            case XmlAttrValue.InputType.TEXT_CAP_SENTENCES:
                return WidgetAttrValue.InputType.TEXT_CAP_SENTENCES;
            case XmlAttrValue.InputType.TEXT_AUTO_CORRECT:
                return WidgetAttrValue.InputType.TEXT_AUTO_CORRECT;
            case XmlAttrValue.InputType.TEXT_AUTO_COMPLETE:
                return WidgetAttrValue.InputType.TEXT_AUTO_COMPLETE;
            case XmlAttrValue.InputType.TEXT_MULTI_LINE:
                return WidgetAttrValue.InputType.TEXT_MULTI_LINE;
            case XmlAttrValue.InputType.TEXT_IME_MULTI_LINE:
                return WidgetAttrValue.InputType.TEXT_IME_MULTI_LINE;
            case XmlAttrValue.InputType.TEXT_NO_SUGGESTIONS:
                return WidgetAttrValue.InputType.TEXT_NO_SUGGESTIONS;
            case XmlAttrValue.InputType.TEXT_URI:
                return WidgetAttrValue.InputType.TEXT_URI;
            case XmlAttrValue.InputType.TEXT_EMAIL_ADDRESS:
                return WidgetAttrValue.InputType.TEXT_EMAIL_ADDRESS;
            case XmlAttrValue.InputType.TEXT_EMAIL_SUBJECT:
                return WidgetAttrValue.InputType.TEXT_EMAIL_SUBJECT;
            case XmlAttrValue.InputType.TEXT_SHORT_MESSAGE:
                return WidgetAttrValue.InputType.TEXT_SHORT_MESSAGE;
            case XmlAttrValue.InputType.TEXT_LONG_MESSAGE:
                return WidgetAttrValue.InputType.TEXT_LONG_MESSAGE;
            case XmlAttrValue.InputType.TEXT_PERSON_NAME:
                return WidgetAttrValue.InputType.TEXT_PERSON_NAME;
            case XmlAttrValue.InputType.TEXT_POSTAL_ADDRESS:
                return WidgetAttrValue.InputType.TEXT_POSTAL_ADDRESS;
            case XmlAttrValue.InputType.TEXT_PASSWORD:
                return WidgetAttrValue.InputType.TEXT_PASSWORD;
            case XmlAttrValue.InputType.TEXT_VISIBLE_PASSWORD:
                return WidgetAttrValue.InputType.TEXT_VISIBLE_PASSWORD;
            case XmlAttrValue.InputType.TEXT_WEB_EDIT_TEXT:
                return WidgetAttrValue.InputType.TEXT_WEB_EDIT_TEXT;
            case XmlAttrValue.InputType.TEXT_FILTER:
                return WidgetAttrValue.InputType.TEXT_FILTER;
            case XmlAttrValue.InputType.TEXT_PHONETIC:
                return WidgetAttrValue.InputType.TEXT_PHONETIC;
            case XmlAttrValue.InputType.TEXT_WEB_EMAIL_ADDRESS:
                return WidgetAttrValue.InputType.TEXT_WEB_EMAIL_ADDRESS;
            case XmlAttrValue.InputType.TEXT_WEB_PASSWORD:
                return WidgetAttrValue.InputType.TEXT_WEB_PASSWORD;
            case XmlAttrValue.InputType.NUMBER:
                return WidgetAttrValue.InputType.NUMBER;
            case XmlAttrValue.InputType.NUMBER_SIGNED:
                return WidgetAttrValue.InputType.NUMBER_SIGNED;
            case XmlAttrValue.InputType.NUMBER_DECIMAL:
                return WidgetAttrValue.InputType.NUMBER_DECIMAL;
            case XmlAttrValue.InputType.NUMBER_PASSWORD:
                return WidgetAttrValue.InputType.NUMBER_PASSWORD;
            case XmlAttrValue.InputType.PHONE:
                return WidgetAttrValue.InputType.PHONE;
            case XmlAttrValue.InputType.DATETIME:
                return WidgetAttrValue.InputType.DATETIME;
            case XmlAttrValue.InputType.DATE:
                return WidgetAttrValue.InputType.DATE;
            case XmlAttrValue.InputType.TIME:
                return WidgetAttrValue.InputType.TIME;
            default:
                return WidgetAttrValue.InputType.TEXT;
        }
    }

    @NotNull
    public static String getMultiScrollFlag(String xmlMultiScrollFlag) {
        String[] xmlScrollFlagArray = xmlMultiScrollFlag.replaceAll("\\s*", "").split("\\|");
        List<String> viewScrollFlagList = new ArrayList<>();
        Arrays.stream(xmlScrollFlagArray).forEach(it -> viewScrollFlagList.add(getScrollFlag(it)));
        return String.join(" or ", viewScrollFlagList);
    }

    @NotNull
    public static String getScrollFlag(String xmlScrollFlag) {
        switch (xmlScrollFlag) {
            case XmlAttrValue.ScrollFlag.ENTER_ALWAYS:
                return WidgetAttrValue.ScrollFlag.ENTER_ALWAYS;
            case XmlAttrValue.ScrollFlag.ENTER_ALWAYS_COLLAPSED:
                return WidgetAttrValue.ScrollFlag.ENTER_ALWAYS_COLLAPSED;
            case XmlAttrValue.ScrollFlag.EXIT_UNTIL_COLLAPSED:
                return WidgetAttrValue.ScrollFlag.EXIT_UNTIL_COLLAPSED;
            case XmlAttrValue.ScrollFlag.SCROLL:
                return WidgetAttrValue.ScrollFlag.SCROLL;
            case XmlAttrValue.ScrollFlag.SNAP:
                return WidgetAttrValue.ScrollFlag.SNAP;
            default:
                if (TextUtils.isEmpty(xmlScrollFlag))
                    return XmlAttrValue.ScrollFlag.SCROLL;
                else
                    return xmlScrollFlag + StringUtil.VALUE_NOT_SUPPORT;
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
                return WidgetAttrValue.AccessibilityLiveRegion.ASSERTIVE;
            case XmlAttrValue.AccessibilityLiveRegion.NONE:
                return WidgetAttrValue.AccessibilityLiveRegion.NONE;
            case XmlAttrValue.AccessibilityLiveRegion.POLITE:
                return WidgetAttrValue.AccessibilityLiveRegion.POLITE;
            default:
                if (TextUtils.isEmpty(xmlAccessibilityLiveRegion))
                    return XmlAttrValue.AccessibilityLiveRegion.NONE;
                else
                    return xmlAccessibilityLiveRegion + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    @NotNull
    public static String getMultiAutofillHint(String xmlMultiAutofillHint) {
        ImportUtil.support(ImportUtil.VIEW);
        String[] xmlAutofillHintArray = xmlMultiAutofillHint.replaceAll("\\s*", "").split("\\|");
        List<String> viewAutofillHintList = new ArrayList<>();
        Arrays.stream(xmlAutofillHintArray).forEach(it -> viewAutofillHintList.add(getAutofillHint(it)));
        return String.join(", ", viewAutofillHintList);
    }

    public static String getAutofillHint(String xmlAutofillHint) {
        switch (xmlAutofillHint) {
            case XmlAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_DATE:
                return WidgetAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_DATE;
            case XmlAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_DAY:
                return WidgetAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_DAY;
            case XmlAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_MONTH:
                return WidgetAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_MONTH;
            case XmlAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_YEAR:
                return WidgetAttrValue.AutofillHint.CREDIT_CARD_EXPIRATION_YEAR;
            case XmlAttrValue.AutofillHint.CREDIT_CARD_NUMBER:
                return WidgetAttrValue.AutofillHint.CREDIT_CARD_NUMBER;
            case XmlAttrValue.AutofillHint.CREDIT_CARD_SECURITY_CODE:
                return WidgetAttrValue.AutofillHint.CREDIT_CARD_SECURITY_CODE;
            case XmlAttrValue.AutofillHint.EMAIL_ADDRESS:
                return WidgetAttrValue.AutofillHint.EMAIL_ADDRESS;
            case XmlAttrValue.AutofillHint.NAME:
                return WidgetAttrValue.AutofillHint.NAME;
            case XmlAttrValue.AutofillHint.PASSWORD:
                return WidgetAttrValue.AutofillHint.PASSWORD;
            case XmlAttrValue.AutofillHint.PHONE:
                return WidgetAttrValue.AutofillHint.PHONE;
            case XmlAttrValue.AutofillHint.POSTAL_ADDRESS:
                return WidgetAttrValue.AutofillHint.POSTAL_ADDRESS;
            case XmlAttrValue.AutofillHint.POSTAL_CODE:
                return WidgetAttrValue.AutofillHint.POSTAL_CODE;
            case XmlAttrValue.AutofillHint.USERNAME:
                return WidgetAttrValue.AutofillHint.USERNAME;
            default:
                return "\"" + xmlAutofillHint + "\"";
        }
    }

    public static String getPorterDuffMode(String xmlPorterDuffMode) {
        ImportUtil.support(ImportUtil.PORTER_DUFF);
        switch (xmlPorterDuffMode) {
            case XmlAttrValue.PorterDuffMode.ADD:
                return WidgetAttrValue.PorterDuffMode.ADD;
            case XmlAttrValue.PorterDuffMode.CLEAR:
                return WidgetAttrValue.PorterDuffMode.CLEAR;
            case XmlAttrValue.PorterDuffMode.DARKEN:
                return WidgetAttrValue.PorterDuffMode.DARKEN;
            case XmlAttrValue.PorterDuffMode.DST:
                return WidgetAttrValue.PorterDuffMode.DST;
            case XmlAttrValue.PorterDuffMode.DST_ATOP:
                return WidgetAttrValue.PorterDuffMode.DST_ATOP;
            case XmlAttrValue.PorterDuffMode.DST_IN:
                return WidgetAttrValue.PorterDuffMode.DST_IN;
            case XmlAttrValue.PorterDuffMode.DST_OUT:
                return WidgetAttrValue.PorterDuffMode.DST_OUT;
            case XmlAttrValue.PorterDuffMode.DST_OVER:
                return WidgetAttrValue.PorterDuffMode.DST_OVER;
            case XmlAttrValue.PorterDuffMode.LIGHTEN:
                return WidgetAttrValue.PorterDuffMode.LIGHTEN;
            case XmlAttrValue.PorterDuffMode.MULTIPLY:
                return WidgetAttrValue.PorterDuffMode.MULTIPLY;
            case XmlAttrValue.PorterDuffMode.OVERLAY:
                return WidgetAttrValue.PorterDuffMode.OVERLAY;
            case XmlAttrValue.PorterDuffMode.SCREEN:
                return WidgetAttrValue.PorterDuffMode.SCREEN;
            case XmlAttrValue.PorterDuffMode.SRC:
                return WidgetAttrValue.PorterDuffMode.SRC;
            case XmlAttrValue.PorterDuffMode.SRC_ATOP:
                return WidgetAttrValue.PorterDuffMode.SRC_ATOP;
            case XmlAttrValue.PorterDuffMode.SRC_IN:
                return WidgetAttrValue.PorterDuffMode.SRC_IN;
            case XmlAttrValue.PorterDuffMode.SRC_OUT:
                return WidgetAttrValue.PorterDuffMode.SRC_OUT;
            case XmlAttrValue.PorterDuffMode.SRC_OVER:
                return WidgetAttrValue.PorterDuffMode.SRC_OVER;
            case XmlAttrValue.PorterDuffMode.XOR:
                return WidgetAttrValue.PorterDuffMode.XOR;
            default:
                return xmlPorterDuffMode + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getDrawingCacheQuality(String xmlDrawingCacheQuality) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlDrawingCacheQuality) {
            case XmlAttrValue.DrawingCacheQuality.AUTO:
                return WidgetAttrValue.DrawingCacheQuality.AUTO;
            case XmlAttrValue.DrawingCacheQuality.HIGH:
                return WidgetAttrValue.DrawingCacheQuality.HIGH;
            case XmlAttrValue.DrawingCacheQuality.LOW:
                return WidgetAttrValue.DrawingCacheQuality.LOW;
            default:
                return xmlDrawingCacheQuality + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getImportantForAccessibility(String xmlImportantForAccessibility) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlImportantForAccessibility) {
            case XmlAttrValue.ImportantForAccessibility.AUTO:
                return WidgetAttrValue.ImportantForAccessibility.AUTO;
            case XmlAttrValue.ImportantForAccessibility.NO:
                return WidgetAttrValue.ImportantForAccessibility.NO;
            case XmlAttrValue.ImportantForAccessibility.NO_HIDE_DESCENDANTS:
                return WidgetAttrValue.ImportantForAccessibility.NO_HIDE_DESCENDANTS;
            case XmlAttrValue.ImportantForAccessibility.YES:
                return WidgetAttrValue.ImportantForAccessibility.YES;
            default:
                return xmlImportantForAccessibility + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getImportantForAutofill(String xmlImportantForAutofill) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlImportantForAutofill) {
            case XmlAttrValue.ImportantForAutofill.AUTO:
                return WidgetAttrValue.ImportantForAutofill.AUTO;
            case XmlAttrValue.ImportantForAutofill.NO:
                return WidgetAttrValue.ImportantForAutofill.NO;
            case XmlAttrValue.ImportantForAutofill.NO_EXCLUDE_DESCENDANTS:
                return WidgetAttrValue.ImportantForAutofill.NO_EXCLUDE_DESCENDANTS;
            case XmlAttrValue.ImportantForAutofill.YES:
                return WidgetAttrValue.ImportantForAutofill.YES;
            case XmlAttrValue.ImportantForAutofill.YES_EXCLUDE_DESCENDANTS:
                return WidgetAttrValue.ImportantForAutofill.YES_EXCLUDE_DESCENDANTS;
            default:
                return xmlImportantForAutofill + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getLayerType(String xmlLayerType) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlLayerType) {
            case XmlAttrValue.LayerType.HARDWARE:
                return WidgetAttrValue.LayerType.HARDWARE;
            case XmlAttrValue.LayerType.NONE:
                return WidgetAttrValue.LayerType.NONE;
            case XmlAttrValue.LayerType.SOFTWARE:
                return WidgetAttrValue.LayerType.SOFTWARE;
            default:
                return xmlLayerType + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getLayoutDirection(String xmlLayoutDirection) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlLayoutDirection) {
            case XmlAttrValue.LayoutDirection.INHERIT:
                return WidgetAttrValue.LayoutDirection.INHERIT;
            case XmlAttrValue.LayoutDirection.LOCALE:
                return WidgetAttrValue.LayoutDirection.LOCALE;
            case XmlAttrValue.LayoutDirection.LTR:
                return WidgetAttrValue.LayoutDirection.LTR;
            case XmlAttrValue.LayoutDirection.RTL:
                return WidgetAttrValue.LayoutDirection.RTL;
            default:
                return xmlLayoutDirection + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getOutlineProvider(String xmlOutlineProvider) {
        ImportUtil.support(ImportUtil.VIEW_OUTLINE_PROVIDER);
        switch (xmlOutlineProvider) {
            case XmlAttrValue.OutlineProvider.BACKGROUND:
                return WidgetAttrValue.OutlineProvider.BACKGROUND;
            case XmlAttrValue.OutlineProvider.BOUNDS:
                return WidgetAttrValue.OutlineProvider.BOUNDS;
            case XmlAttrValue.OutlineProvider.NONE:
                return WidgetAttrValue.OutlineProvider.NONE;
            case XmlAttrValue.OutlineProvider.PADDED_BOUNDS:
                return WidgetAttrValue.OutlineProvider.PADDED_BOUNDS;
            default:
                return xmlOutlineProvider + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getOverScrollMode(String xmlOverScrollMode) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlOverScrollMode) {
            case XmlAttrValue.OverScrollMode.ALWAYS:
                return WidgetAttrValue.OverScrollMode.ALWAYS;
            case XmlAttrValue.OverScrollMode.IF_CONTENT_SCROLLS:
                return WidgetAttrValue.OverScrollMode.IF_CONTENT_SCROLLS;
            case XmlAttrValue.OverScrollMode.NEVER:
                return WidgetAttrValue.OverScrollMode.NEVER;
            default:
                return xmlOverScrollMode + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getPointerIconType(String xmlPointerIconType) {
        ImportUtil.support(ImportUtil.POINTER_ICON);
        switch (xmlPointerIconType) {
            case XmlAttrValue.PointerIconType.ALIAS:
                return WidgetAttrValue.PointerIconType.ALIAS;
            case XmlAttrValue.PointerIconType.ALL_SCROLL:
                return WidgetAttrValue.PointerIconType.ALL_SCROLL;
            case XmlAttrValue.PointerIconType.ARROW:
                return WidgetAttrValue.PointerIconType.ARROW;
            case XmlAttrValue.PointerIconType.CELL:
                return WidgetAttrValue.PointerIconType.CELL;
            case XmlAttrValue.PointerIconType.CONTEXT_MENU:
                return WidgetAttrValue.PointerIconType.CONTEXT_MENU;
            case XmlAttrValue.PointerIconType.COPY:
                return WidgetAttrValue.PointerIconType.COPY;
            case XmlAttrValue.PointerIconType.CROSSHAIR:
                return WidgetAttrValue.PointerIconType.CROSSHAIR;
            case XmlAttrValue.PointerIconType.GRAB:
                return WidgetAttrValue.PointerIconType.GRAB;
            case XmlAttrValue.PointerIconType.GRABBING:
                return WidgetAttrValue.PointerIconType.GRABBING;
            case XmlAttrValue.PointerIconType.HAND:
                return WidgetAttrValue.PointerIconType.HAND;
            case XmlAttrValue.PointerIconType.HELP:
                return WidgetAttrValue.PointerIconType.HELP;
            case XmlAttrValue.PointerIconType.HORIZONTAL_DOUBLE_ARROW:
                return WidgetAttrValue.PointerIconType.HORIZONTAL_DOUBLE_ARROW;
            case XmlAttrValue.PointerIconType.NO_DROP:
                return WidgetAttrValue.PointerIconType.NO_DROP;
            case XmlAttrValue.PointerIconType.NULL:
                return WidgetAttrValue.PointerIconType.NULL;
            case XmlAttrValue.PointerIconType.TEXT:
                return WidgetAttrValue.PointerIconType.TEXT;
            case XmlAttrValue.PointerIconType.TOP_LEFT_DIAGONAL_DOUBLE_ARROW:
                return WidgetAttrValue.PointerIconType.TOP_LEFT_DIAGONAL_DOUBLE_ARROW;
            case XmlAttrValue.PointerIconType.TOP_RIGHT_DIAGONAL_DOUBLE_ARROW:
                return WidgetAttrValue.PointerIconType.TOP_RIGHT_DIAGONAL_DOUBLE_ARROW;
            case XmlAttrValue.PointerIconType.VERTICAL_DOUBLE_ARROW:
                return WidgetAttrValue.PointerIconType.VERTICAL_DOUBLE_ARROW;
            case XmlAttrValue.PointerIconType.VERTICAL_TEXT:
                return WidgetAttrValue.PointerIconType.VERTICAL_TEXT;
            case XmlAttrValue.PointerIconType.WAIT:
                return WidgetAttrValue.PointerIconType.WAIT;
            case XmlAttrValue.PointerIconType.ZOOM_IN:
                return WidgetAttrValue.PointerIconType.ZOOM_IN;
            case XmlAttrValue.PointerIconType.ZOOM_OUT:
                return WidgetAttrValue.PointerIconType.ZOOM_OUT;
            default:
                return xmlPointerIconType + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getMultiScrollIndicator(String xmlMultiScrollIndicator) {
        ImportUtil.support(ImportUtil.VIEW);
        String[] xmlScrollIndicatorArray = xmlMultiScrollIndicator.replaceAll("\\s*", "").split("\\|");
        List<String> viewScrollIndicatorList = new ArrayList<>();
        Arrays.stream(xmlScrollIndicatorArray).forEach(it -> viewScrollIndicatorList.add(getScrollIndicator(it)));
        return String.join(" or ", viewScrollIndicatorList);
    }

    @Contract(pure = true)
    public static String getScrollIndicator(String xmlScrollIndicator) {
        switch (xmlScrollIndicator) {
            case XmlAttrValue.ScrollIndicator.BOTTOM:
                return WidgetAttrValue.ScrollIndicator.BOTTOM;
            case XmlAttrValue.ScrollIndicator.END:
                return WidgetAttrValue.ScrollIndicator.END;
            case XmlAttrValue.ScrollIndicator.LEFT:
                return WidgetAttrValue.ScrollIndicator.LEFT;
            case XmlAttrValue.ScrollIndicator.RIGHT:
                return WidgetAttrValue.ScrollIndicator.RIGHT;
            case XmlAttrValue.ScrollIndicator.START:
                return WidgetAttrValue.ScrollIndicator.START;
            case XmlAttrValue.ScrollIndicator.TOP:
                return WidgetAttrValue.ScrollIndicator.TOP;
            default:
                return xmlScrollIndicator + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getScrollBarStyle(String xmlScrollBarStyle) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlScrollBarStyle) {
            case XmlAttrValue.ScrollBarStyle.SCROLLBARS_INSIDE_INSET:
                return WidgetAttrValue.ScrollBarStyle.SCROLLBARS_INSIDE_INSET;
            case XmlAttrValue.ScrollBarStyle.SCROLLBARS_INSIDE_OVERLAY:
                return WidgetAttrValue.ScrollBarStyle.SCROLLBARS_INSIDE_OVERLAY;
            case XmlAttrValue.ScrollBarStyle.SCROLLBARS_OUTSIDE_INSET:
                return WidgetAttrValue.ScrollBarStyle.SCROLLBARS_OUTSIDE_INSET;
            case XmlAttrValue.ScrollBarStyle.SCROLLBARS_OUTSIDE_OVERLAY:
                return WidgetAttrValue.ScrollBarStyle.SCROLLBARS_OUTSIDE_OVERLAY;
            default:
                return xmlScrollBarStyle + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    @NotNull
    public static String getStateListAnimator(String xmlStateListAnimator) {
        ImportUtil.support(ImportUtil.ANIMATOR_INFLATER);
        if (StringUtil.isDrawableRes(xmlStateListAnimator)) {
            return "AnimatorInflater.loadStateListAnimator(context, " + getImage(xmlStateListAnimator) + ")";
        } else
            return xmlStateListAnimator + StringUtil.VALUE_NOT_SUPPORT;
    }

    public static String getTextAlignment(String xmlTextAlignment) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlTextAlignment) {
            case XmlAttrValue.TextAlignment.CENTER:
                return WidgetAttrValue.TextAlignment.CENTER;
            case XmlAttrValue.TextAlignment.GRAVITY:
                return WidgetAttrValue.TextAlignment.GRAVITY;
            case XmlAttrValue.TextAlignment.INHERIT:
                return WidgetAttrValue.TextAlignment.INHERIT;
            case XmlAttrValue.TextAlignment.TEXT_END:
                return WidgetAttrValue.TextAlignment.TEXT_END;
            case XmlAttrValue.TextAlignment.TEXT_START:
                return WidgetAttrValue.TextAlignment.TEXT_START;
            case XmlAttrValue.TextAlignment.VIEW_END:
                return WidgetAttrValue.TextAlignment.VIEW_END;
            case XmlAttrValue.TextAlignment.VIEW_START:
                return WidgetAttrValue.TextAlignment.VIEW_START;
            default:
                return xmlTextAlignment + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getTextDirection(String xmlTextDirection) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlTextDirection) {
            case XmlAttrValue.TextDirection.ANY_RTL:
                return WidgetAttrValue.TextDirection.ANY_RTL;
            case XmlAttrValue.TextDirection.FIRST_STRONG:
                return WidgetAttrValue.TextDirection.FIRST_STRONG;
            case XmlAttrValue.TextDirection.FIRST_STRONG_LTR:
                return WidgetAttrValue.TextDirection.FIRST_STRONG_LTR;
            case XmlAttrValue.TextDirection.FIRST_STRONG_RTL:
                return WidgetAttrValue.TextDirection.FIRST_STRONG_RTL;
            case XmlAttrValue.TextDirection.INHERIT:
                return WidgetAttrValue.TextDirection.INHERIT;
            case XmlAttrValue.TextDirection.LOCALE:
                return WidgetAttrValue.TextDirection.LOCALE;
            case XmlAttrValue.TextDirection.LTR:
                return WidgetAttrValue.TextDirection.LTR;
            case XmlAttrValue.TextDirection.RTL:
                return WidgetAttrValue.TextDirection.RTL;
            default:
                return xmlTextDirection + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getVerticalScrollbarPosition(String xmlVerticalScrollbarPosition) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlVerticalScrollbarPosition) {
            case XmlAttrValue.VerticalScrollbarPosition.SCROLLBAR_POSITION_DEFAULT:
                return WidgetAttrValue.VerticalScrollbarPosition.SCROLLBAR_POSITION_DEFAULT;
            case XmlAttrValue.VerticalScrollbarPosition.SCROLLBAR_POSITION_LEFT:
                return WidgetAttrValue.VerticalScrollbarPosition.SCROLLBAR_POSITION_LEFT;
            case XmlAttrValue.VerticalScrollbarPosition.SCROLLBAR_POSITION_RIGHT:
                return WidgetAttrValue.VerticalScrollbarPosition.SCROLLBAR_POSITION_RIGHT;
            default:
                return xmlVerticalScrollbarPosition + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getVisibility(String xmlVisibility) {
        ImportUtil.support(ImportUtil.VIEW);
        switch (xmlVisibility) {
            case XmlAttrValue.Visibility.GONE:
                return WidgetAttrValue.Visibility.GONE;
            case XmlAttrValue.Visibility.INVISIBLE:
                return WidgetAttrValue.Visibility.INVISIBLE;
            case XmlAttrValue.Visibility.VISIBLE:
                return WidgetAttrValue.Visibility.VISIBLE;
            default:
                return xmlVisibility + StringUtil.VALUE_NOT_SUPPORT;
        }
    }
}
