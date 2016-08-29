package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase;

import java.io.File;

import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class XmlLocalConnectionProfileWizard extends TeiidProfileWizard {

	private XmlLocalConnectionProfileWizard(String name) {
		super("XML Local File Source",name);
		log.info("Local XML profile wizard is opened");
	}
	
	public static XmlLocalConnectionProfileWizard getInstance(){
		return new XmlLocalConnectionProfileWizard(null);
	}
	
	public static XmlLocalConnectionProfileWizard openWizard(String name){
		XmlLocalConnectionProfileWizard wizard = new XmlLocalConnectionProfileWizard(name);
		wizard.open();
		return wizard;
	}

	public XmlLocalConnectionProfileWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		return this;
	}
	
	public XmlLocalConnectionProfileWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public XmlLocalConnectionProfileWizard setFile(String path) {
		log.info("Path to file is : '" + path + "'");
		activate();
		path = new File(path).getAbsolutePath();
		new LabeledText("File Name").setText(path);
		activate();
		return this;
	}
	
	@Override
	@Deprecated
	/**
	 * deprecated - because local xml connection profile wizard don't have any test connection
	 */
	public XmlLocalConnectionProfileWizard testConnection(){
		return this;
	}
}
