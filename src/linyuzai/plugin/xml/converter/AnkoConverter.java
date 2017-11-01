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
        noteBuilder.append(" * @version 1.0.0\n");
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

    private void writeViewStart(String viewName, int deep) {
        writeTab(deep);
        codeBuilder.append(ConvertUtil.convertViewName(viewName)).append(" {\n");
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
        if (widthValue.equals(XmlAttrValue.WRAP_CONTENT) && heightValue.equals(XmlAttrValue.WRAP_CONTENT)) {
            if (layout.size() > 0)
                codeBuilder.append(".lparams");
        } else if (widthValue.equals(XmlAttrValue.WRAP_CONTENT)) {
            codeBuilder.append(".lparams(height = ").append(AttributeUtil.getWidthOrHeight(heightValue)).append(")");
        } else if (heightValue.equals(XmlAttrValue.WRAP_CONTENT)) {
            codeBuilder.append(".lparams(width = ").append(AttributeUtil.getWidthOrHeight(widthValue)).append(")");
        } else {
            codeBuilder.append(".lparams(width = ").append(AttributeUtil.getWidthOrHeight(widthValue))
                    .append(", height = ").append(AttributeUtil.getWidthOrHeight(heightValue)).append(")");
        }
        if (layout.size() > 0) {
            codeBuilder.append(" {\n");
            for (UAttribute attr : layout)
                writeAttribute(attr.getName(), attr.getValue(), deep + 1);
            writeTab(deep);
            codeBuilder.append("}");
        }
        codeBuilder.append("\n");
    }

    private void writeAttribute(String attributeName, String attributeValue, int deep) {
        writeTab(deep);
        String name = attributeName;
        String value = attributeValue;
        String extra = "";
        boolean isEqualsOperator = true;
        boolean isNotSupportAttribute = false;
        if (attributeName.startsWith("android:layout_")) {
            if (attributeName.startsWith("android:layout_margin")) {
                switch (attributeName) {
                    case XmlAttrName.LAYOUT_MARGIN:
                        name = ViewAttrName.MARGIN;
                        value = AttributeUtil.getDimension(attributeValue);
                        break;
                    case XmlAttrName.LAYOUT_MARGIN_TOP:
                        name = ViewAttrName.MARGIN_TOP;
                        value = AttributeUtil.getDimension(attributeValue);
                        break;
                    case XmlAttrName.LAYOUT_MARGIN_RIGHT:
                        name = ViewAttrName.MARGIN_RIGHT;
                        value = AttributeUtil.getDimension(attributeValue);
                        break;
                    case XmlAttrName.LAYOUT_MARGIN_BOTTOM:
                        name = ViewAttrName.MARGIN_BOTTOM;
                        value = AttributeUtil.getDimension(attributeValue);
                        break;
                    case XmlAttrName.LAYOUT_MARGIN_LEFT:
                        name = ViewAttrName.MARGIN_LEFT;
                        value = AttributeUtil.getDimension(attributeValue);
                        break;
                    case XmlAttrName.LAYOUT_MARGIN_START:
                        name = ViewAttrName.MARGIN_START;
                        value = AttributeUtil.getDimension(attributeValue);
                        break;
                    case XmlAttrName.LAYOUT_MARGIN_END:
                        name = ViewAttrName.MARGIN_END;
                        value = AttributeUtil.getDimension(attributeValue);
                        break;
                    case XmlAttrName.LAYOUT_MARGIN_VERTICAL:
                        name = ViewAttrName.MARGIN_VERTICAL;
                        value = AttributeUtil.getDimension(attributeValue);
                        break;
                    case XmlAttrName.LAYOUT_MARGIN_HORIZONTAL:
                        name = ViewAttrName.MARGIN_HORIZONTAL;
                        value = AttributeUtil.getDimension(attributeValue);
                        break;
                    default:
                        isNotSupportAttribute = true;
                        break;
                }
            } else {
                switch (attributeName) {
                    case XmlAttrName.LAYOUT_GRAVITY:
                        name = ViewAttrName.GRAVITY;
                        value = AttributeUtil.getMultiGravity(attributeValue);
                        ImportUtil.add(ImportUtil.GRAVITY);
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
            }
        } else if (attributeName.startsWith("android:padding")) {
            switch (attributeName) {
                case XmlAttrName.LAYOUT_PADDING:
                    name = ViewAttrName.PADDING;
                    value = AttributeUtil.getDimension(attributeValue);
                    break;
                case XmlAttrName.LAYOUT_PADDING_TOP:
                    name = ViewAttrName.PADDING_TOP;
                    value = AttributeUtil.getDimension(attributeValue);
                    break;
                case XmlAttrName.LAYOUT_PADDING_RIGHT:
                    name = ViewAttrName.PADDING_RIGHT;
                    value = AttributeUtil.getDimension(attributeValue);
                    break;
                case XmlAttrName.LAYOUT_PADDING_BOTTOM:
                    name = ViewAttrName.PADDING_BOTTOM;
                    value = AttributeUtil.getDimension(attributeValue);
                    break;
                case XmlAttrName.LAYOUT_PADDING_LEFT:
                    name = ViewAttrName.PADDING_LEFT;
                    value = AttributeUtil.getDimension(attributeValue);
                    break;
                case XmlAttrName.LAYOUT_PADDING_VERTICAL:
                    name = ViewAttrName.PADDING_VERTICAL;
                    value = AttributeUtil.getDimension(attributeValue);
                    break;
                case XmlAttrName.LAYOUT_PADDING_HORIZONTAL:
                    name = ViewAttrName.PADDING_HORIZONTAL;
                    value = AttributeUtil.getDimension(attributeValue);
                    break;
                default:
                    isNotSupportAttribute = true;
                    break;
            }
        } else if (attributeName.startsWith("android:text")) {
            switch (attributeName) {
                case XmlAttrName.TEXT:
                    name = ViewAttrName.TEXT;
                    value = AttributeUtil.getString(attributeValue);
                    break;
                case XmlAttrName.TEXT_SIZE:
                    name = ViewAttrName.TEXT_SIZE;
                    value = AttributeUtil.getDimension(attributeValue);
                    break;
                case XmlAttrName.TEXT_COLOR:
                    name = ViewAttrName.TEXT_COLOR;
                    value = AttributeUtil.getColor(attributeValue);
                    break;
                case XmlAttrName.TEXT_COLOR_HINT:
                    name = ViewAttrName.HINT_TEXT_COLOR;
                    value = AttributeUtil.getColor(attributeValue);
                    break;
                default:
                    isNotSupportAttribute = true;
                    break;
            }
        } else {
            switch (attributeName) {
                case XmlAttrName.ALPHA:
                    name = ViewAttrName.ALPHA;
                    value = AttributeUtil.getAlpha(attributeValue);
                    break;
                case XmlAttrName.BACKGROUND:
                    if (StringUtil.isImage(attributeValue)) {
                        name = ViewAttrName.BACKGROUND_RESOURCE;
                        value = AttributeUtil.getImage(attributeValue);
                    } else {
                        name = ViewAttrName.BACKGROUND_COLOR;
                        value = AttributeUtil.getColor(attributeValue);
                        ImportUtil.add(ImportUtil.COLOR);
                    }
                    break;
                case XmlAttrName.GRAVITY:
                    name = ViewAttrName.GRAVITY;
                    value = AttributeUtil.getMultiGravity(attributeValue);
                    ImportUtil.add(ImportUtil.GRAVITY);
                    break;
                case XmlAttrName.ID:
                    name = ViewAttrName.ID;
                    value = AttributeUtil.getId(attributeValue);
                    break;
                case XmlAttrName.MAX_LINES:
                    name = ViewAttrName.MAX_LINES;
                    value = AttributeUtil.getInteger(attributeValue);
                    break;
                case XmlAttrName.ORIENTATION:
                    name = ViewAttrName.ORIENTATION;
                    value = AttributeUtil.getOrientation(attributeValue);
                    ImportUtil.add(ImportUtil.LINEAR_LAYOUT);
                    break;
                case XmlAttrName.SRC:
                    name = ViewAttrName.IMAGE_RESOURCE;
                    value = AttributeUtil.getImage(attributeValue);
                    break;
                case XmlAttrName.SCALE_TYPE:
                    name = ViewAttrName.SCALE_TYPE;
                    value = AttributeUtil.getScaleType(attributeValue);
                    ImportUtil.add(ImportUtil.IMAGE_VIEW);
                    break;
                case XmlAttrName.SINGLE_LINE:
                    name = ViewAttrName.SINGLE_LINE;
                    value = AttributeUtil.getBoolean(attributeValue);
                    break;
                case XmlAttrName.HINT:
                    name = ViewAttrName.HINT;
                    value = AttributeUtil.getString(attributeValue);
                    break;
                default:
                    isNotSupportAttribute = true;
                    break;
            }
        }
        if (isNotSupportAttribute)
            codeBuilder.append("//");
        if (isEqualsOperator)
            codeBuilder.append(name).append(" = ").append(value);
        else
            codeBuilder.append(name).append("(").append(value).append(")");
        if (isNotSupportAttribute)
            codeBuilder.append(" //not support attribute");
        codeBuilder.append(extra).append("\n");
    }

    private void writeTab(int deep) {
        for (int i = 0; i < deep; i++)
            codeBuilder.append("\t");
    }

    private void writeView(Element root, int deep) {
        if (callback != null)
            callback.onUpdate(progress++);
        writeViewStart(root.getName(), deep);
        List<List<UAttribute>> attributes = AttributeUtil.splitAttributes(root.attributes());
        for (UAttribute attribute : attributes.get(0)) {
            writeAttribute(attribute.getName(), attribute.getValue(), deep + 1);
        }
        for (Element element : root.elements()) {
            writeView(element, deep + 1);
        }
        writeViewEnd(deep);
        writeLayoutParams(attributes.get(1), attributes.get(2), deep);
        if (callback != null)
            callback.onUpdate(progress++);
    }
}
