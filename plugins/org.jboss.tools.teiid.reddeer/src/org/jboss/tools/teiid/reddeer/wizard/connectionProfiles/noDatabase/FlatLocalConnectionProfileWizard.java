package org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.noDatabase;

import java.io.File;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.jface.condition.WindowIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CancelButton;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.teiid.reddeer.wizard.connectionProfiles.ConnectionProfileWizard;

public class FlatLocalConnectionProfileWizard extends ConnectionProfileWizard {
	
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
	
	public FlatLocalConnectionProfileWizard activateWizard() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
    @Override
    public void cancel() {
        log.info("Cancel wizard");
        new CancelButton().click();
        new WaitWhile(new WindowIsAvailable(this));
        try {
            new WaitWhile(new JobIsRunning());
        } catch (NoClassDefFoundError e) {
            // do nothing, reddeer.workbench plugin is not available
        }
    }

	public FlatLocalConnectionProfileWizard setFile(String path) {
		log.info("Path to file is : '" + path + "'");
		activateWizard();
		path = new File(path).getAbsolutePath();
		new DefaultText(0).setText(path);
		activateWizard();
		return this;
	}
	
	public FlatLocalConnectionProfileWizard setCharset(String charset) {
		log.info("Set charset to: '" + charset + "'");
		activateWizard();
		new LabeledCombo("Select charset:").setSelection(charset);
		activateWizard();
		return this;
	}
	
	public FlatLocalConnectionProfileWizard setStyle(String style) {
		log.info("Set style to: '" + style + "'");
		activateWizard();
		new LabeledCombo("Select flatfile style:").setSelection(style);
		activateWizard();
		return this;
	}
	
	@Override
	public FlatLocalConnectionProfileWizard testConnection(){
		new PushButton("Test Connection").click();
		new DefaultShell("Success");
		new PushButton("OK").click();
		activateWizard();
		return this;
	}

}
