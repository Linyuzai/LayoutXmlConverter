package linyuzai.plugin.xml.common;

import linyuzai.plugin.xml.attr.UApplication;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {
    public interface ViewNameConvertCallback {
        void onViewNameConvert(String packageName);
    }

    @NotNull
    public static String convertClassName(String className) {
        List<String> names = new ArrayList<>();
        if (className.contains("_")) {
            String[] nameArray = className.split("_");
            for (int i = 0; i < nameArray.length; i++) {
                if (i > 0 || !(nameArray[i].equals("layout") || nameArray[i].equals("activity") ||
                        nameArray[i].equals("fragment"))) {
                    names.add(nameArray[i].substring(0, 1).toUpperCase() + nameArray[i].substring(1));
                }
            }
        } else {
            names.add(className.substring(0, 1).toUpperCase() + className.substring(1));
        }
        if (names.size() == 0)
            names.add("Anko");
        StringBuilder nameBuilder = new StringBuilder();
        for (String name : names)
            nameBuilder.append(name);
        return nameBuilder.append("Activity").toString();
    }

    @NotNull
    public static String convertViewName(String viewName, ViewNameConvertCallback callback) {
        if (viewName.contains(".")) {
            int index = viewName.lastIndexOf(".");
            if (callback != null)
                callback.onViewNameConvert(viewName.substring(0, index));
            viewName = viewName.substring(index + 1);
        }
        return viewName.substring(0, 1).toLowerCase() + viewName.substring(1);
    }

    public static UApplication convertManifest(InputStream is) {
        UApplication application = new UApplication();
        try {
            Document document = new SAXReader().read(is);
            Element root = document.getRootElement();
            Attribute attribute = root.attribute("package");
            application.setPackageName(attribute.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return application;
    }
}
