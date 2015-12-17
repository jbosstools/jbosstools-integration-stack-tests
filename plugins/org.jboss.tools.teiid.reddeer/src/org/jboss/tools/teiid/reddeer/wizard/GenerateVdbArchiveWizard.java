package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.condition.LabelWithTextIsAvailable;

public class GenerateVdbArchiveWizard extends WizardDialog  {

	private static final String DIALOG_TITLE = "Generate VDB Archive";
	

	public GenerateVdbArchiveWizard activate(){
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public GenerateVdbArchiveWizard setVersion(String version){
		new LabeledText("Version").setText(version);
		return this;
	}

	public GenerateVdbArchiveWizard setLocation(String... location){
		new PushButton("Change...").click();
		new SelectTargetFolder().select(location);
		return this;
	}
	
	public GenerateVdbArchiveWizard setArchiveName(String name){
		new LabeledText("Archive VDB Name").setText(name);
		return this;
	}
	
	public GenerateVdbArchiveWizard setFileName(String name){
		new LabeledText("VDB Archive File Name").setText(name);
		return this;
	}
	
	public GenerateVdbArchiveWizard setDdlAsDescription(boolean checked){
		new CheckBox("Set parsed DDL as the generated tables' descriptions").toggle(checked);
		return this;
	}

	public GenerateVdbArchiveWizard setOvewriteExisting(boolean checked){
		new CheckBox("Overwrite existing files").toggle(checked);
		return this;
	}

	public GenerateVdbArchiveWizard generate(){
		new PushButton("Generate").click();
		
		new WaitWhile(new LabelWithTextIsAvailable("Converting Dynamic Vdb to Xmi Vdb"));
		
		return this;
	}
	
	public List<String> getSourceModels(){
		return getModels("Source Models");
	}
	
	public List<String> getViewModels(){
		return getModels("View Models");
	}
	
	private List<String> getModels(String group){
		List<String> result = new ArrayList<String>();
		for(TableItem it : new DefaultTable(new DefaultGroup(group)).getItems()){
			result.add(it.getText());
		}
		return result;
	}
}
