package de.aqua.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//import static de.aqua.web.utils.FileUtil.readStreamToStringLine;
import static de.aqua.web.utils.StringUtil.split;

/**
 * Created by bdumitru on 7/12/2017.
 */
@SuppressWarnings("unused")
public class XmlUtil {

    private static Logger LOG = LoggerFactory.getLogger(XmlUtil.class);

    private static XPath xpath = XPathFactory.newInstance().newXPath();


    private static String extract(XPathExpression xpathExpr, Node node)
            throws XPathExpressionException {
        return (String) xpathExpr.evaluate(node, XPathConstants.STRING);
    }

    private static Boolean extractBoolean(XPathExpression xpathExpr, Node node)
            throws XPathExpressionException {
        return (Boolean) xpathExpr.evaluate(node, XPathConstants.BOOLEAN);
    }

    private static int extractInt(XPathExpression xpathExpr, Node node)
            throws XPathExpressionException {
        String value = xpathExpr.evaluate(node, XPathConstants.STRING).toString();
        return value.isEmpty() ? -1 : Integer.parseInt(value);
    }

//    private static NodeList readXmlFile(String fileName) throws ParserConfigurationException,
//            SAXException, IOException {
//        String content = readStreamToStringLine("ScenariosDetails/" + fileName);
//
//        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//        Document doc = documentBuilder.parse(new InputSource(new StringReader(content)));
//        return doc.getElementsByTagName("Scenario");
//    }

//    public static Collection<Object[]> readHotelBookingScenarios(String fileName) {
//        List<Object[]> scenarios = new ArrayList<Object[]>();
//        try {
//            XPathExpression loginUrl = xpath.compile("LoginUrl");
//            XPathExpression incrementer = xpath.compile("Incrementer");
//            XPathExpression input = xpath.compile("Input");
//            XPathExpression validations = xpath.compile("Validations");
//            XPathExpression testSummary = xpath.compile("TestSummary");
//
//            NodeList nodes = readXmlFile(fileName);
//            for (int i = 0; i < nodes.getLength(); i++) {
//                scenarios.add(new Object[]{
//                        extract(loginUrl, nodes.item(i)),
//                        extract(incrementer, nodes.item(i)),
//                        split(extract(input, nodes.item(i))),
//                        split(extract(validations, nodes.item(i))),
//                        extract(testSummary, nodes.item(i)),
//                });
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Error reading and parsing file:" + "ScenariosDetails/"
//                    + fileName);
//        }
//        return scenarios;
//    }

//    public static Collection<Object[]> readRestScenarios(String fileName) {
//        List<Object[]> scenarios = new ArrayList<Object[]>();
//        try {
//            XPathExpression serverRootUri = xpath.compile("ServerRootUri");
//            XPathExpression httpHeader = xpath.compile("HttpHeader");
//            XPathExpression proxy = xpath.compile("Proxy");
//            XPathExpression callType = xpath.compile("CallType");
//            XPathExpression callMessage = xpath.compile("CallMessage");
//            XPathExpression responseCode = xpath.compile("ResponseCode");
//            XPathExpression responseHeader = xpath.compile("ResponseHeader");
//            XPathExpression responseBody = xpath.compile("ResponseBody");
//            XPathExpression skip = xpath.compile("Skip");
//
//            NodeList nodes = readXmlFile(fileName);
//            for (int i = 0; i < nodes.getLength(); i++) {
//                scenarios.add(new Object[]{
//                        extract(serverRootUri, nodes.item(i)),
//                        split(extract(httpHeader, nodes.item(i)), "#"),
//                        split(extract(proxy, nodes.item(i)), "#"),
//                        extract(callType, nodes.item(i)),
//                        extract(callMessage, nodes.item(i)),
//                        extract(responseCode, nodes.item(i)),
//                        extract(responseHeader, nodes.item(i)),
//                        extract(responseBody, nodes.item(i)),
//                        split(extract(skip, nodes.item(i)), "#"),
//                });
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Error reading and parsing file:" + "ScenariosDetails/"
//                    + fileName);
//        }
//        return scenarios;
//    }

    public static String getPropertyFromXml(String fileName, String node) {
        String value = "";

        try {
            File xmlFile = new File("src/test/resources/properties/" + fileName + ".xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("properties");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node nlNode = nodeList.item(temp);
                if (nlNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nlNode;
                    value = element.getElementsByTagName(node).item(0).getTextContent();
                    break;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
