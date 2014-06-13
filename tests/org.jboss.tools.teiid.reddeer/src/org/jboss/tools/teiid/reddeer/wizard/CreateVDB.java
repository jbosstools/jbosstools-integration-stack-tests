package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.tools.teiid.reddeer.view.GuidesView;

/**
 * Creates a new virtual database.
 * 
 * @author Lucia Jelinkova
 * 
 */
public class CreateVDB extends NewWizardDialog {

	private String folder;

	private String name;

	public CreateVDB() {
		super("Teiid Designer", "Teiid VDB");
	}

	public void execute() {
		open();
		fillFirstPage();
		finish();
	}
	
	/**
	 * Define new VDB
	 * @param viaGuides true if set via Modelling actions
	 */
	public void execute(boolean viaGuides) {

		if (viaGuides) {

			new GuidesView().chooseAction("Model JDBC Source", "Define VDB");
			AbstractWait.sleep(TimePeriod.SHORT);
			new DefaultShell("Define VDB").setFocus();
			
			new PushButton("New...").click();
			fillFirstPage();
			finish();
			
			new DefaultShell("Define VDB").setFocus();
			new PushButton("OK").click();
		} else {
			execute();
		}
	}

	private void fillFirstPage() {
		// TODO: LabeledText
		// new LabeledText("In Folder:").setText(folder);
		// TODO: Do we really need to set folder?
		// new SWTWorkbenchBot().textWithLabel("In Folder:").setText(folder);
		// TODO: LabeledText
		// new LabeledText("VDB Name:").setText(name);
		new SWTWorkbenchBot().textWithLabel("VDB Name:").setText(name);
		if (this.folder != null){
			new SWTWorkbenchBot().textWithLabel("In Folder:").setText(folder);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}
}
