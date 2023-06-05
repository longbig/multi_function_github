package com.longbig.multifunction.utils;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author yuyunlong
 * @date 2023/6/3 20:47
 * @description
 */
@Slf4j
public class XmlHelper {

    /**
     * xml格式数据转Java对象
     *
     * @param xmlData
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseXmlToObject(String xmlData, Class<T> clazz) {
        try {
            Document document = DocumentHelper.parseText(xmlData);
            Element root = document.getRootElement();

            T object = clazz.newInstance();

            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                String tagName = field.getName();
                String value = getElementValue(root, tagName);
                if (value != null) {
                    field.setAccessible(true);
                    field.set(object, value);
                }
            }

            return object;
        } catch (DocumentException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            log.error("parseXmlToObject error, e = {}", e);
        }
        return null;
    }

    private static String getElementValue(Element parent, String tagName) {
        Element element = parent.element(tagName);
        if (element != null) {
            return element.getTextTrim();
        }
        return null;
    }
}
