package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 * 
 */
public class ConnectionProfileXmlUrlPage extends WizardPage implements ConnectionProfileXmlPage {

	public static final String LABEL_URL = "File Name";

	public ConnectionProfileXmlUrlPage() {
		
	}

	@Override
	public void setPath(String path) {
		new LabeledText(LABEL_URL).setText(path);
	}

}
