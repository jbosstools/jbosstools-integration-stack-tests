package org.jboss.tools.runtime.reddeer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;

import org.w3c.dom.Element;

/**
 * 
 * @author apodhrad
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Properties {

	@XmlAnyElement
	private List<Element> any;

	public List<Element> getAny() {
		return any;
	}

	public void setAny(List<Element> any) {
		this.any = any;
	}

	public String getProperty(String key) {
		for (Element element : any) {
			String elemKey = element.getNodeName();
			if (elemKey != null && elemKey.equals(key)) {
				return element.getTextContent();
			}
		}
		return null;
	}
	
	public List<String> getProperties(String key) {
		List<String> values = new ArrayList<String>();
		for (Element element : any) {
			String elemKey = element.getNodeName();
			if (elemKey != null && elemKey.equals(key)) {
				values.add(element.getTextContent());
			}
		}
		return values;
	}
}
