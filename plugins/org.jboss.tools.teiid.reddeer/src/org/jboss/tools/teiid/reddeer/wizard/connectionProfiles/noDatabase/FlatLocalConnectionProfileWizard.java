package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase;

import java.io.File;

import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;

public class FlatLocalConnectionProfileWizard extends TeiidProfileWizard {
	
	public static final String DIALOG_TITLE = "New Flat File Data Source Profile";

	private FlatLocalConnectionProfileWizard(String name) {
		super("Flat File Data Source",name);
		log.info("Local flat file profile wizard is opened");
	}
	
	public static FlatLocalConnectionProfileWizard getInstance(){
		return new FlatLocalConnectionProfileWizard(null);
	}
	
	public static FlatLocalConnectionProfileWizard openWizard(String name){
		FlatLocalConnectionProfileWizard wizard = new FlatLocalConnectionProfileWizard(name);
		wizard.open();
		return wizard;
	}

	public FlatLocalConnectionProfileWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		return this;
	}
	
	public FlatLocalConnectionProfileWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public FlatLocalConnectionProfileWizard setFile(String path) {
		log.info("Path to file is : '" + path + "'");
		activate();
		path = new File(path).getAbsolutePath();
		new DefaultText(0).setText(path);
		activate();
		return this;
	}
	
	public FlatLocalConnectionProfileWizard setCharset(String charset) {
		log.info("Set charset to: '" + charset + "'");
		activate();
		new LabeledCombo("Select charset:").setSelection(charset);
		activate();
		return this;
	}
	
	public FlatLocalConnectionProfileWizard setStyle(String style) {
		log.info("Set style to: '" + style + "'");
		activate();
		new LabeledCombo("Select flatfile style:").setSelection(style);
		activate();
		return this;
	}
	
	@Override
	public FlatLocalConnectionProfileWizard testConnection(){
		new PushButton("Test Connection").click();
		new DefaultShell("Success");
		new PushButton("OK").click();
		activate();
		return this;
	}

}
