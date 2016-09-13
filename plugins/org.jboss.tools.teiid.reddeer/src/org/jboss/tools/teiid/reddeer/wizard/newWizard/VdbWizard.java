package org.jboss.tools.teiid.reddeer.wizard.newWizard;

import java.util.Arrays;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class VdbWizard extends NewWizardDialog {
	
	public static final String DIALOG_TITLE = "New VDB";
	
	private VdbWizard() {
		super("Teiid Designer", "Teiid VDB");
		log.info("VDB wizard is opened");
	}
	
	public static VdbWizard getInstance(){
		return new VdbWizard();
	}
	
	public static VdbWizard openVdbWizard() {
		VdbWizard vdbWizard = new VdbWizard();
		vdbWizard.open();
		return vdbWizard;
	}
	
	@Override
	public void open() {
		super.open();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	@Override
	public void finish() {
		super.finish();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	public VdbWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public VdbWizard setLocation(String... location) {
		activate();
		log.info("Set location to '" + Arrays.toString(location) + "'");
		new PushButton("...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(location).select();
		new PushButton("OK").click();
		activate();
		return this;
	}
	
	public VdbWizard setName(String name) {
		activate();
		log.info("Set vdb name to '" + name + "'");
		new LabeledText("VDB Name:").setText(name);
		return this;
	}

	public VdbWizard addModel(String... pathToModel) {
		int i = pathToModel.length - 1;
		pathToModel[i] = (pathToModel[i].contains(".")) ? pathToModel[i] : pathToModel[i] + ".xmi";
		activate();
		log.info("Path to model: '" + Arrays.toString(pathToModel) + "'");
		new PushButton("Add").click();
		new DefaultShell("Select Models");
		new DefaultTreeItem(pathToModel).select();
		new PushButton("OK").click();
		activate();
		return this;
	}

}
