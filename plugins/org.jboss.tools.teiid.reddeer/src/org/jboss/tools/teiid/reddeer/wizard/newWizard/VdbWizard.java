package org.jboss.tools.teiid.reddeer.wizard.newWizard;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * @author skaleta
 */
public class VdbWizard extends NewWizardDialog {
	public static final String DIALOG_TITLE = "New VDB";
	
	public VdbWizard() {
		super("Teiid Designer", "Teiid VDB");
	}
	
	public static VdbWizard openVdbWizard() {
		VdbWizard vdbWizard = new VdbWizard();
		vdbWizard.open();
		return vdbWizard;
	}
	
	@Override
	public void finish() {
		super.finish();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	@Override
	public void open() {
		super.open();
		activate();
	}

	public VdbWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public VdbWizard setLocation(String... location) {
		activate();
		new PushButton("...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(location).select();
		new PushButton("OK").click();
		activate();
		return this;
	}
	
	public VdbWizard setName(String name) {
		activate();
		new LabeledText("VDB Name:").setText(name);
		return this;
	}

	public VdbWizard addModel(String... pathToModel) {
		if (!pathToModel[pathToModel.length - 1].contains(".")) {
			pathToModel[pathToModel.length - 1] = pathToModel[pathToModel.length - 1]
					.concat(".xmi");
		}
		activate();
		new PushButton("Add").click();
		new DefaultShell("Select Models");
		new DefaultTreeItem(pathToModel).select();
		new PushButton("OK").click();
		activate();
		return this;
	}

}
