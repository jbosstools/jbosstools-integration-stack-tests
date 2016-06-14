package org.jboss.tools.fuse.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 * 
 */
public class CamelXmlFileWizard extends NewWizardDialog {

	public CamelXmlFileWizard() {
		super("JBoss Fuse", "Camel XML File");
	}

	public CamelXmlFileWizard openWizard() {
		super.open();
		return this;
	}

	public CamelXmlFileWizard setName(String name) {
		new LabeledText("File name:").setText(name);
		return this;
	}

}
