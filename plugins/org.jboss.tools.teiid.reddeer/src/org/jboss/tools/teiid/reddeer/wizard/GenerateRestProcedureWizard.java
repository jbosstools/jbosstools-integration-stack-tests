package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.matcher.WithMnemonicTextMatcher;

public class GenerateRestProcedureWizard {

	private static final String TITLE = "Generate REST Virtual Procedures";
	private String projectName;

	public GenerateRestProcedureWizard(){
		focus();
	}
	
	public GenerateRestProcedureWizard setProject(String projectName){
		this.projectName = projectName;

		focus();
		new PushButton(new DefaultGroup("View Model Definition"), 0, new WithMnemonicTextMatcher("...")).click();

		new DefaultShell("Select a Folder");
		new DefaultTreeItem(projectName).select();
		new PushButton("OK").click();
		
		return this;
	}

	public GenerateRestProcedureWizard setExistingTargetModel(String targetName) {
		focus();

		new PushButton(new DefaultGroup("View Model Definition"), 1, new WithMnemonicTextMatcher("...")).click();

		new DefaultShell("Select View Model");
		new DefaultTreeItem(projectName, targetName).select();
		new PushButton("OK").click();

		return this;
	}
	
	public GenerateRestProcedureWizard setNewTargetModel(String targetName){
		focus();
		new LabeledText("Name:").setText(targetName);
		
		return this;
	}
	
	public GenerateRestProcedureWizard setTables(String... tables){
		focus();
		
		DefaultTable table = new DefaultTable(new DefaultGroup("Table Selection"), 0);
		for (String t : tables) {
			table.getItem(t).setChecked(true);
		}
		
		return this;
	}

	public GenerateRestProcedureWizard focus() {
		new DefaultShell(TITLE);
		return this;
	}
	
	public void finish(){
		focus();
		new PushButton("OK").click();
	}
}
