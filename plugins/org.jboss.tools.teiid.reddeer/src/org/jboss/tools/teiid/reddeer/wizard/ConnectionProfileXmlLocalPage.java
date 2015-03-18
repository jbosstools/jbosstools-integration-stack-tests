package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 *
 */
public class ConnectionProfileXmlLocalPage extends WizardPage implements ConnectionProfileXmlPage {

	public static final String LABEL_FILE_NAME = "File Name";

	@Override
	public void setPath(String path) {
		new LabeledText(LABEL_FILE_NAME).setText(path);
	}
	
}
