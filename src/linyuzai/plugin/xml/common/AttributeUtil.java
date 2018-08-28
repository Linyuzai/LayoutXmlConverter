package linyuzai.plugin.xml.common;

import linyuzai.plugin.xml.attr.*;
import org.apache.http.util.TextUtils;
import org.dom4j.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AttributeUtil {

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
            UAttribute widthAttr = new UAttribute(XmlAttrName.LAYOUT_WIDTH, XmlAttrValue.WRAP_CONTENT);
            UAttribute heightAttr = new UAttribute(XmlAttrName.LAYOUT_HEIGHT, XmlAttrValue.WRAP_CONTENT);
            paramsAttr.add(widthAttr);
            paramsAttr.add(heightAttr);
        } else if (paramsAttr.size() == 1) {
            UAttribute widthOrHeightAttr = paramsAttr.get(0);
            if (widthOrHeightAttr.getName().equals(XmlAttrName.LAYOUT_WIDTH)) {
                UAttribute heightAttr = new UAttribute(XmlAttrName.LAYOUT_HEIGHT, XmlAttrValue.WRAP_CONTENT);
                paramsAttr.add(heightAttr);
            } else if (widthOrHeightAttr.getName().equals(XmlAttrName.LAYOUT_HEIGHT)) {
                UAttribute widthAttr = new UAttribute(XmlAttrName.LAYOUT_WIDTH, XmlAttrValue.WRAP_CONTENT);
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
            if (ua.getName().contains("android:theme")) {
                ui.remove();
                return ua;
            }
        }
        return null;
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

    public static String getTheme(String xmlTheme) {
        if (xmlTheme.startsWith("?attr/")) {
            return "R.attr." + xmlTheme.substring(6).replaceAll("\\.", "_");
        } else if (xmlTheme.startsWith("?android:attr/")) {
            return "android.R.attr." + xmlTheme.substring(14).replaceAll("\\.", "_");
        } else if (xmlTheme.startsWith("@style/"))
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
        if (xmlId.contains("@+id/"))
            return "R.id." + xmlId.substring(5);
        else if (xmlId.contains("@id/"))
            return "R.id." + xmlId.substring(4);
        else if (xmlId.contains("@android:id/"))
            return "android.R.id." + xmlId.substring(12);
        else if (TextUtils.isEmpty(xmlId))
            return "0";
        else
            return xmlId + StringUtil.VALUE_NOT_SUPPORT;
    }

    public static String getWidthOrHeight(String value) {
        if (value.equals(XmlAttrValue.MATCH_PARENT))
            return ViewAttrValue.MATCH_PARENT;
        else if (value.equals(XmlAttrValue.WRAP_CONTENT))
            return ViewAttrValue.WRAP_CONTENT;
        else
            return getDimension(value, false);

    }

    public static String getWeight(String xmlWeight) {
        if (StringUtil.isInteger(xmlWeight))
            return xmlWeight + "f";
        else return xmlWeight + StringUtil.VALUE_NOT_SUPPORT;

    }

    @NotNull
    public static String getDimension(String xmlDimension, boolean toFloat) {
        if (xmlDimension.startsWith("?attr/")) {
            if (toFloat)
                return "dimenAttr(R.attr." + xmlDimension.substring(6) + ").toFloat()";
            else
                return "dimenAttr(R.attr." + xmlDimension.substring(6) + ")";
        } else if (xmlDimension.startsWith("?android:attr/")) {
            if (toFloat)
                return "dimenAttr(android.R.attr." + xmlDimension.substring(14) + ").toFloat()";
            else
                return "dimenAttr(android.R.attr." + xmlDimension.substring(14) + ")";
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
    public static String getColor(String xmlColor) {
        if (xmlColor.startsWith("?attr/"))
            return "colorAttr(R.attr." + xmlColor.substring(6) + ")";
        else if (xmlColor.startsWith("?android:attr/"))
            return "colorAttr(android.R.attr." + xmlColor.substring(14) + ")";
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

    public static String getFloat(String xmlFloat) {
        if (StringUtil.isFloat(xmlFloat))
            return Float.valueOf(xmlFloat).toString() + "f";
        else
            return getInteger(xmlFloat, true);
    }

    public static String getOrientation(String xmlOrientation) {
        switch (xmlOrientation) {
            case XmlAttrValue.ORIENTATION_VERTICAL:
                return ViewAttrValue.ORIENTATION_VERTICAL;
            case XmlAttrValue.ORIENTATION_HORIZONTAL:
                return ViewAttrValue.ORIENTATION_HORIZONTAL;
            default:
                if (TextUtils.isEmpty(xmlOrientation))
                    return XmlAttrValue.ORIENTATION_VERTICAL;
                else
                    return xmlOrientation + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getMultiGravity(String xmlMultiGravity) {
        String[] xmlGravityArray = xmlMultiGravity.replaceAll("\\s*", "").split("\\|");
        List<String> viewGravityList = new ArrayList<>();
        Arrays.stream(xmlGravityArray).forEach(it -> viewGravityList.add(getGravity(it)));
        return String.join(" or ", viewGravityList);
    }

    public static String getGravity(String xmlGravity) {
        switch (xmlGravity) {
            case XmlAttrValue.GRAVITY_CENTER:
                return ViewAttrValue.GRAVITY_CENTER;
            case XmlAttrValue.GRAVITY_CENTER_VERTICAL:
                return ViewAttrValue.GRAVITY_CENTER_VERTICAL;
            case XmlAttrValue.GRAVITY_CENTER_HORIZONTAL:
                return ViewAttrValue.GRAVITY_CENTER_HORIZONTAL;
            case XmlAttrValue.GRAVITY_START:
                return ViewAttrValue.GRAVITY_START;
            case XmlAttrValue.GRAVITY_END:
                return ViewAttrValue.GRAVITY_END;
            case XmlAttrValue.GRAVITY_TOP:
                return ViewAttrValue.GRAVITY_TOP;
            case XmlAttrValue.GRAVITY_BOTTOM:
                return ViewAttrValue.GRAVITY_BOTTOM;
            case XmlAttrValue.GRAVITY_LEFT:
                return ViewAttrValue.GRAVITY_LEFT;
            case XmlAttrValue.GRAVITY_RIGHT:
                return ViewAttrValue.GRAVITY_RIGHT;
            case XmlAttrValue.GRAVITY_FILL:
                return ViewAttrValue.GRAVITY_FILL;
            case XmlAttrValue.GRAVITY_FILL_VERTICAL:
                return ViewAttrValue.GRAVITY_FILL_VERTICAL;
            case XmlAttrValue.GRAVITY_FILL_HORIZONTAL:
                return ViewAttrValue.GRAVITY_FILL_HORIZONTAL;
            case XmlAttrValue.GRAVITY_CLIP_VERTICAL:
                return ViewAttrValue.GRAVITY_CLIP_VERTICAL;
            case XmlAttrValue.GRAVITY_CLIP_HORIZONTAL:
                return ViewAttrValue.GRAVITY_CLIP_HORIZONTAL;
            default:
                if (TextUtils.isEmpty(xmlGravity))
                    return XmlAttrValue.GRAVITY_LEFT;
                else
                    return xmlGravity + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getScaleType(String xmlScaleType) {
        switch (xmlScaleType) {
            case XmlAttrValue.SCALE_TYPE_CENTER:
                return ViewAttrValue.SCALE_TYPE_CENTER;
            case XmlAttrValue.SCALE_TYPE_CENTER_CROP:
                return ViewAttrValue.SCALE_TYPE_CENTER_CROP;
            case XmlAttrValue.SCALE_TYPE_CENTER_INSIDE:
                return ViewAttrValue.SCALE_TYPE_CENTER_INSIDE;
            case XmlAttrValue.SCALE_TYPE_FIT_CENTER:
                return ViewAttrValue.SCALE_TYPE_FIT_CENTER;
            case XmlAttrValue.SCALE_TYPE_FIT_START:
                return ViewAttrValue.SCALE_TYPE_FIT_START;
            case XmlAttrValue.SCALE_TYPE_FIT_END:
                return ViewAttrValue.SCALE_TYPE_FIT_END;
            case XmlAttrValue.SCALE_TYPE_FIT_XY:
                return ViewAttrValue.SCALE_TYPE_FIT_XY;
            case XmlAttrValue.SCALE_TYPE_MATRIX:
                return ViewAttrValue.SCALE_TYPE_MATRIX;
            default:
                if (TextUtils.isEmpty(xmlScaleType))
                    return XmlAttrValue.SCALE_TYPE_CENTER;
                else
                    return xmlScaleType + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getTextStyle(String xmlTextStyle) {
        boolean isBold = false;
        boolean isItalic = false;
        String[] textStyles = xmlTextStyle.split("\\|");
        for (String textStyle : textStyles) {
            if (textStyle.equals(XmlAttrValue.TEXT_STYLE_BOLD))
                isBold = true;
            else if (textStyle.equals(XmlAttrValue.TEXT_STYLE_ITALIC))
                isItalic = true;
        }
        if (isBold && isItalic)
            return ViewAttrValue.TEXT_STYLE_BOLD_ITALIC;
        else if (isBold)
            return ViewAttrValue.TEXT_STYLE_BOLD;
        else if (isItalic)
            return ViewAttrValue.TEXT_STYLE_ITALIC;
        else
            return ViewAttrValue.TEXT_STYLE_NORMAL;
    }

    public static String getInputType(String xmlInputType) {
        switch (xmlInputType) {
            case XmlAttrValue.INPUT_TYPE_NONE:
                return ViewAttrValue.INPUT_TYPE_NONE;
            case XmlAttrValue.INPUT_TYPE_TEXT:
                return ViewAttrValue.INPUT_TYPE_TEXT;
            case XmlAttrValue.INPUT_TYPE_TEXT_CAP_CHARACTERS:
                return ViewAttrValue.INPUT_TYPE_TEXT_CAP_CHARACTERS;
            case XmlAttrValue.INPUT_TYPE_TEXT_CAP_WORDS:
                return ViewAttrValue.INPUT_TYPE_TEXT_CAP_WORDS;
            case XmlAttrValue.INPUT_TYPE_TEXT_CAP_SENTENCES:
                return ViewAttrValue.INPUT_TYPE_TEXT_CAP_SENTENCES;
            case XmlAttrValue.INPUT_TYPE_TEXT_AUTO_CORRECT:
                return ViewAttrValue.INPUT_TYPE_TEXT_AUTO_CORRECT;
            case XmlAttrValue.INPUT_TYPE_TEXT_AUTO_COMPLETE:
                return ViewAttrValue.INPUT_TYPE_TEXT_AUTO_COMPLETE;
            case XmlAttrValue.INPUT_TYPE_TEXT_MULTI_LINE:
                return ViewAttrValue.INPUT_TYPE_TEXT_MULTI_LINE;
            case XmlAttrValue.INPUT_TYPE_TEXT_IME_MULTI_LINE:
                return ViewAttrValue.INPUT_TYPE_TEXT_IME_MULTI_LINE;
            case XmlAttrValue.INPUT_TYPE_TEXT_NO_SUGGESTIONS:
                return ViewAttrValue.INPUT_TYPE_TEXT_NO_SUGGESTIONS;
            case XmlAttrValue.INPUT_TYPE_TEXT_URI:
                return ViewAttrValue.INPUT_TYPE_TEXT_URI;
            case XmlAttrValue.INPUT_TYPE_TEXT_EMAIL_ADDRESS:
                return ViewAttrValue.INPUT_TYPE_TEXT_EMAIL_ADDRESS;
            case XmlAttrValue.INPUT_TYPE_TEXT_EMAIL_SUBJECT:
                return ViewAttrValue.INPUT_TYPE_TEXT_EMAIL_SUBJECT;
            case XmlAttrValue.INPUT_TYPE_TEXT_SHORT_MESSAGE:
                return ViewAttrValue.INPUT_TYPE_TEXT_SHORT_MESSAGE;
            case XmlAttrValue.INPUT_TYPE_TEXT_LONG_MESSAGE:
                return ViewAttrValue.INPUT_TYPE_TEXT_LONG_MESSAGE;
            case XmlAttrValue.INPUT_TYPE_TEXT_PERSON_NAME:
                return ViewAttrValue.INPUT_TYPE_TEXT_PERSON_NAME;
            case XmlAttrValue.INPUT_TYPE_TEXT_POSTAL_ADDRESS:
                return ViewAttrValue.INPUT_TYPE_TEXT_POSTAL_ADDRESS;
            case XmlAttrValue.INPUT_TYPE_TEXT_PASSWORD:
                return ViewAttrValue.INPUT_TYPE_TEXT_PASSWORD;
            case XmlAttrValue.INPUT_TYPE_TEXT_VISIBLE_PASSWORD:
                return ViewAttrValue.INPUT_TYPE_TEXT_VISIBLE_PASSWORD;
            case XmlAttrValue.INPUT_TYPE_TEXT_WEB_EDIT_TEXT:
                return ViewAttrValue.INPUT_TYPE_TEXT_WEB_EDIT_TEXT;
            case XmlAttrValue.INPUT_TYPE_TEXT_FILTER:
                return ViewAttrValue.INPUT_TYPE_TEXT_FILTER;
            case XmlAttrValue.INPUT_TYPE_TEXT_PHONETIC:
                return ViewAttrValue.INPUT_TYPE_TEXT_PHONETIC;
            case XmlAttrValue.INPUT_TYPE_TEXT_WEB_EMAIL_ADDRESS:
                return ViewAttrValue.INPUT_TYPE_TEXT_WEB_EMAIL_ADDRESS;
            case XmlAttrValue.INPUT_TYPE_TEXT_WEB_PASSWORD:
                return ViewAttrValue.INPUT_TYPE_TEXT_WEB_PASSWORD;
            case XmlAttrValue.INPUT_TYPE_NUMBER:
                return ViewAttrValue.INPUT_TYPE_NUMBER;
            case XmlAttrValue.INPUT_TYPE_NUMBER_SIGNED:
                return ViewAttrValue.INPUT_TYPE_NUMBER_SIGNED;
            case XmlAttrValue.INPUT_TYPE_NUMBER_DECIMAL:
                return ViewAttrValue.INPUT_TYPE_NUMBER_DECIMAL;
            case XmlAttrValue.INPUT_TYPE_NUMBER_PASSWORD:
                return ViewAttrValue.INPUT_TYPE_NUMBER_PASSWORD;
            case XmlAttrValue.INPUT_TYPE_PHONE:
                return ViewAttrValue.INPUT_TYPE_PHONE;
            case XmlAttrValue.INPUT_TYPE_DATETIME:
                return ViewAttrValue.INPUT_TYPE_DATETIME;
            case XmlAttrValue.INPUT_TYPE_DATE:
                return ViewAttrValue.INPUT_TYPE_DATE;
            case XmlAttrValue.INPUT_TYPE_TIME:
                return ViewAttrValue.INPUT_TYPE_TIME;
            default:
                return ViewAttrValue.INPUT_TYPE_TEXT;
        }
    }

    public static String getMultiScrollFlags(String xmlMultiScrollFlags) {
        String[] xmlScrollFlagsArray = xmlMultiScrollFlags.replaceAll("\\s*", "").split("\\|");
        List<String> viewScrollFlagsList = new ArrayList<>();
        Arrays.stream(xmlScrollFlagsArray).forEach(it -> viewScrollFlagsList.add(getScrollFlags(it)));
        return String.join(" or ", viewScrollFlagsList);
    }

    public static String getScrollFlags(String attributeValue) {
        switch (attributeValue) {
            case XmlAttrValue.SCROLL_FLAG_ENTER_ALWAYS:
                return ViewAttrValue.SCROLL_FLAG_ENTER_ALWAYS;
            case XmlAttrValue.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED:
                return ViewAttrValue.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED;
            case XmlAttrValue.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED:
                return ViewAttrValue.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED;
            case XmlAttrValue.SCROLL_FLAG_SCROLL:
                return ViewAttrValue.SCROLL_FLAG_SCROLL;
            case XmlAttrValue.SCROLL_FLAG_SNAP:
                return ViewAttrValue.SCROLL_FLAG_SNAP;
            default:
                if (TextUtils.isEmpty(attributeValue))
                    return XmlAttrValue.SCROLL_FLAG_SCROLL;
                else
                    return attributeValue + StringUtil.VALUE_NOT_SUPPORT;
        }
    }

    public static String getBehavior(String attributeValue) {
        if (TextUtils.isEmpty(attributeValue))
            return "null";
        else
            return attributeValue.replaceAll("\\$", ".") + "()";
    }
}
