package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for importing relational model from REST WS
 * 
 * @author mmakovy
 * 
 */

public class RestImportWizard extends TeiidImportWizard {

	private static final String IMPORTER = "Web Service Source >> Source and View Model (REST)";

	private String profileName;
	private String projectName;
	private String sourceModelName;
	private String viewModelName;
	private String procedureName;
	private String rootPath;
	private List<String> columns;

	public RestImportWizard() {
		super(IMPORTER);
		columns = new ArrayList<String>();
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setSourceModelName(String sourceModelName) {
		this.sourceModelName = sourceModelName;
	}

	public void setViewModelName(String viewModelName) {
		this.viewModelName = viewModelName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public void addColumn(String column) {
		columns.add(column);
	}

	public void open() {
		log.info("Open " + IMPORTER);
		new ShellMenu(getMenuPath()).select();
		new DefaultShell(getDialogTitle());
		log.info("Select " + IMPORTER);
		new DefaultTreeItem("Teiid Designer", IMPORTER).select();
		next();
	}

	@Override
	public void execute() {
		open();
		new DefaultCombo().setSelection(profileName);

		next();
		defineModel("Source Model Definition", projectName, sourceModelName);
		defineModel("View Model Definition", projectName, viewModelName);

		new LabeledText(new DefaultGroup("View Model Definition"), "New View Procedure Name:").setText(procedureName);

		next();
		next();
		defineRootPath(rootPath);
		for (String column : columns) {
			defineColumn(column);
		}

		finish();

	}

	private void defineModel(String section, String projectName, String modelName) {

		new PushButton(new DefaultGroup(section), "...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(projectName).select();
		new PushButton("OK").click();
		new LabeledText(new DefaultGroup(section), "Name:").setText(modelName);

	}

	private void defineRootPath(String path) {

		new DefaultTreeItem(path.split("/")).select();
		new ContextMenu("Set as root path").select();

	}

	private void defineColumn(String column) {
		new DefaultTreeItem((rootPath + "/" + column).split("/")).select();
		new PushButton("Add").click();

	}

}
