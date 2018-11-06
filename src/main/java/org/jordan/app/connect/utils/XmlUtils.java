package org.jordan.app.connect.utils;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * @author zhaord
 * @Description:
 * @date 2018/8/24下午3:53
 */
public class XmlUtils {

    /**
     * XML转对象
     *
     * @param xmlStr xml字串
     * @param t      对象类型
     * @return 对象
     */
    public static <T> T xmlToBean(String xmlStr, Class<T> t) {
        try {
            JAXBContext context = JAXBContext.newInstance(t);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(xmlStr);
            SAXParserFactory sax = SAXParserFactory.newInstance();
            //设置忽略命名空间
            sax.setNamespaceAware(false);
            XMLReader xmlReader = sax.newSAXParser().getXMLReader();
            Source source = new SAXSource(xmlReader, new InputSource(reader));
            return (T) unmarshaller.unmarshal(source);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转XML
     *
     * @param obj 对象
     * @return 字符串
     */
    public static String beanToXml(Object obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            marshaller.marshal(obj, out);
            try {
                return new String(out.toByteArray(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
