package org.jboss.tools.teiid.reddeer.requirement;

import org.jboss.tools.runtime.reddeer.Properties;
import org.w3c.dom.Element;

public class ConnectionProfileProperties extends Properties {

	public java.util.Properties getAllProperties() {
		java.util.Properties result = new java.util.Properties();
		for (Element element : getAny()) {
			String elemKey = element.getNodeName();
			if (elemKey != null) {
				result.put(elemKey, element.getTextContent());
			}
		}
		return result;
	}

}
