package linyuzai.plugin.xml.common;

import linyuzai.plugin.xml.attr.*;
import org.apache.http.util.TextUtils;
import org.dom4j.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributeUtil {

    public static boolean isParamsAttr(String xmlName) {
        return xmlName.equals(XmlAttrName.LAYOUT_WIDTH) || xmlName.equals(XmlAttrName.LAYOUT_HEIGHT);
    }

    public static boolean isLayoutAttr(String xmlName) {
        return xmlName.startsWith(XmlAttrName.LAYOUT_MARGIN) ||
                xmlName.equals(XmlAttrName.LAYOUT_GRAVITY) ||
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
                xmlName.equals(XmlAttrName.LAYOUT_ALIGN_BASELINE);
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

    @NotNull
    public static String getAlpha(String xmlAlpha) {
        if (xmlAlpha.contains("@dimen/"))
            return "resources.getDimension(R.dimen." + xmlAlpha.substring(7) + ")";
        else if (xmlAlpha.contains("@android:dimen/"))
            return "resources.getDimension(R.dimen." + xmlAlpha.substring(15) + ")";
        else if (TextUtils.isEmpty(xmlAlpha))
            return "0f";
        else if (StringUtil.isAlpha(xmlAlpha))
            return xmlAlpha + "f";
        else
            return xmlAlpha + StringUtil.VALUE_NOT_SUPPORT;
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
        else if (value.contains("dp"))
            return "dip(" + value.split("dp")[0] + ")";
        else if (TextUtils.isEmpty(value))
            return ViewAttrValue.WRAP_CONTENT;
        else
            return value;

    }

    @NotNull
    public static String getDimension(String xmlDimension) {
        if (xmlDimension.contains("dp"))
            return "dip(" + xmlDimension.split("dp")[0] + ")";
        else if (xmlDimension.contains("in"))
            return xmlDimension.split("in")[0] + " //in";
        else if (xmlDimension.contains("mm"))
            return xmlDimension.split("mm")[0] + " //mm";
        else if (xmlDimension.contains("pt"))
            return xmlDimension.split("pt")[0] + " //pt";
        else if (xmlDimension.contains("px"))
            return xmlDimension.split("px")[0] + " //px";
        else if (xmlDimension.contains("sp"))
            return xmlDimension.split("sp")[0] + "f //sp";
        else if (xmlDimension.contains("@dimen/"))
            return "dimen(R.dimen." + xmlDimension.substring(7) + ")";
        else if (xmlDimension.contains("@android:dimen/"))
            return "dimen(android.R.dimen." + xmlDimension.substring(15) + ")";
        else if (TextUtils.isEmpty(xmlDimension))
            return "0";
        else return xmlDimension + StringUtil.VALUE_NOT_SUPPORT;
    }

    @NotNull
    public static String getImage(String xmlImage) {
        if (xmlImage.contains("@mipmap/"))
            return "R.mipmap." + xmlImage.substring(8);
        else if (xmlImage.contains("@drawable/"))
            return "R.drawable." + xmlImage.substring(10);
        else if (xmlImage.contains("@android:mipmap/"))
            return "android.R.mipmap." + xmlImage.substring(16);
        else if (xmlImage.contains("@android:drawable/"))
            return "android.R.drawable." + xmlImage.substring(18);
        else if (TextUtils.isEmpty(xmlImage))
            return "0";
        else
            return xmlImage + StringUtil.VALUE_NOT_SUPPORT;
    }

    @NotNull
    public static String getColor(String xmlColor) {
        if (xmlColor.contains("@color/"))
            return "resources.getColor(R.color." + xmlColor.substring(7) + ")";
        else if (xmlColor.contains("@android:color/"))
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
        if (xmlString.contains("@string/"))
            return "resources.getString(R.string." + xmlString.substring(8) + ")";
        else if (xmlString.contains("@android:string/"))
            return "resources.getString(android.R.string." + xmlString.substring(16) + ")";
        else if (TextUtils.isEmpty(xmlString))
            return "\"\"";
        else
            return "\"" + xmlString + "\"";
    }

    public static String getBoolean(String xmlBoolean) {
        if (xmlBoolean.contains("@bool/"))
            return "resources.getBoolean(R.bool." + xmlBoolean.substring(6) + ")";
        else if (xmlBoolean.contains("@android:bool/"))
            return "resources.getBoolean(android.R.bool." + xmlBoolean.substring(14) + ")";
        else if (TextUtils.isEmpty(xmlBoolean))
            return XmlAttrValue.FALSE;
        else
            return xmlBoolean + StringUtil.VALUE_NOT_SUPPORT;
    }

    @NotNull
    public static String getInteger(String xmlInteger) {
        if (xmlInteger.contains("@integer/"))
            return "resources.getInteger(R.integer." + xmlInteger.substring(9) + ")";
        else if (xmlInteger.contains("@android:integer/"))
            return "resources.getInteger(android.R.integer." + xmlInteger.substring(17) + ")";
        else if (TextUtils.isEmpty(xmlInteger))
            return "0";
        else if (StringUtil.isInteger(xmlInteger))
            return Integer.valueOf(xmlInteger).toString();
        else
            return xmlInteger + StringUtil.VALUE_NOT_SUPPORT;
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
}
