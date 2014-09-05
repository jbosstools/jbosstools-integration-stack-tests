package org.jboss.tools.switchyard.reddeer.editor;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XPathEvaluator {

	private static final DocumentBuilderFactory DOC_FACTORY = DocumentBuilderFactory.newInstance();
	private static final XPath XPATH = XPathFactory.newInstance().newXPath();

	private Document doc;
	
	public XPathEvaluator(Reader reader) {
		try {
			doc = DOC_FACTORY.newDocumentBuilder().parse(new InputSource(reader));
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean evaluateBoolean(String expr) {
		try {
			return (Boolean) XPATH.evaluate(expr, doc, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			System.out.println("Error evaluating xPath '" + expr + "'");
			return false;
		}
	}

	public String evaluateString(String expr) {
		try {
			return (String) XPATH.evaluate(expr, doc, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			System.out.println("Error evaluating xPath '" + expr + "'");
			return null;
		}
	}
	
	public static void main(String[] args) throws Exception {
		XPathEvaluator xpath = new XPathEvaluator(new FileReader("/home/apodhrad/Temp/switchyard.xml"));
		System.out.println(xpath.evaluateString("count(/switchyard/composite/component)"));
		System.out.println(xpath.evaluateString("count(/switchyard/composite/component/implementation.camel) = 1"));
		System.out.println(xpath.evaluateBoolean("/switchyard/composite/component/@name = 'MyProcess'"));
	}

}
