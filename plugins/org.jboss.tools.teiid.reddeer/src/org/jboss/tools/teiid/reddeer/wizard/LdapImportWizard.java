package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.matcher.TreeItemTextMatcher;
import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.condition.TreeItemHasChild;

public class LdapImportWizard extends ImportWizardDialog {
	private static final String TABLE_NAME = "Table Name";

	private static final String SOURCE_MODEL_TABLE_ATTRIBUTES = "Source Model Table Attributes";

	private static final String SOURCE_MODEL_NAME = "Name";

	private static final String SOURCE_MODEL_DEFINITION = "Source Model Definition";

	private static final String CONNECTION_PROFILE = "Connection Profile";
	
	private String connectionProfile;
	private String projectName;
	private String modelName;

	private String connectionUrl;
	private String principalDnSuffix;


	private String[] selectedEntries;
	private String[] selectedColumns;

	public LdapImportWizard() {
		super("Teiid Designer", "LDAP Service >> Source Model");
	}
	
	
	public void execute() {
		open();
		fillModelInfoPage();
		next();
		fillSelectTablesPage();
		new PushButton("Validate").click();
		next();
		fillSelectColumnsPage();
		new PushButton("Validate").click();
		finish();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT);
	}
	
	private void fillSelectColumnsPage() {
		for(String column : selectedColumns){
			String[] path = column.split("/");
			selectColumnEntry(path[0], path[1], null);
		}
		
	}


	private void fillModelInfoPage(){
		new DefaultShell("Create Relational Model from LDAP Service");
		
		// select connection profile
		new DefaultCombo(new DefaultGroup(CONNECTION_PROFILE), 0).setSelection(connectionProfile);
		
		// select model project
		new PushButton(new DefaultGroup(SOURCE_MODEL_DEFINITION),"...").click();
		new SelectTargetFolder().select(projectName);
		
		// set model name
		new LabeledText(new DefaultGroup(SOURCE_MODEL_DEFINITION),SOURCE_MODEL_NAME).setText(modelName);
		
		
		new PushButton("Fetch Base DNs").click();
		LabeledCombo ldapBaseDnCombo = new LabeledCombo("LDAP Base DN:");
		new WaitUntil(new WidgetIsEnabled(ldapBaseDnCombo), TimePeriod.LONG);
		ldapBaseDnCombo.setSelection(principalDnSuffix);
		
	}
	
	private void fillSelectTablesPage(){
		for (String entry : selectedEntries){
			selectTableEntry(entry, null);
		}
	}
	
	public void selectColumnEntry(String tableName, String entryName, String entryAlias){
		new DefaultTreeItem(tableName).expand();
		
		TreeItem entryTreeItem = new DefaultTreeItem(tableName, entryName);
		entryTreeItem.select();
		entryTreeItem.setChecked(true);

		if (entryAlias != null){
			new LabeledText(new DefaultGroup(SOURCE_MODEL_TABLE_ATTRIBUTES),TABLE_NAME).setText(entryAlias);
		}


	}
	
	public void selectTableEntry(String entryName, String entryAlias) {

		String[] tablePath = entryName.split("/");
		TreeItemTextMatcher[] matchers = new TreeItemTextMatcher[tablePath.length + 3];

		// we need the RegexMatchers here because the text of the tree item changes --
		// it contains the number of children, but only *after* it has been
		// expanded at least once
		matchers[0] = new TreeItemTextMatcher("DIT");
		matchers[1] = new TreeItemTextMatcher(new RegexMatcher("^Root DSE.*"));
		matchers[2] = new TreeItemTextMatcher(new RegexMatcher('^' + principalDnSuffix + ".*"));
		for (int i = 0; i < tablePath.length; i++) {
			matchers[i + 3] = new TreeItemTextMatcher(new RegexMatcher('^' + tablePath[i] + ".*"));
		}

		// here we expand the tree items one by one, because the wizard
		// fetches the children of each item from the ldap server
		// and that takes some time, so we have to wait until
		// the child node is available
		// trying `new DefaultTreeItem(matchers)` right away fails.
		for (int i = 0; i < matchers.length - 1; i++) {
			TreeItemTextMatcher[] partialMatchers = new TreeItemTextMatcher[i + 1];
			System.arraycopy(matchers, 0, partialMatchers, 0, i + 1);
			DefaultTreeItem partialTreeItem = new DefaultTreeItem(partialMatchers);
			partialTreeItem.expand();
			new WaitUntil(new TreeItemHasChild(partialTreeItem, matchers[i + 1]));
		}

		TreeItem entryTreeItem = new DefaultTreeItem(matchers);

		entryTreeItem.select();
		entryTreeItem.setChecked(true);
		new DefaultToolItem("Add the selected LDAP entries").click();

		if (entryAlias != null) {
			new LabeledText(new DefaultGroup(SOURCE_MODEL_TABLE_ATTRIBUTES), TABLE_NAME).setText(entryAlias);
		}

	}
	
	
	public void setConnectionProfile(String connectionProfile) {
		this.connectionProfile = connectionProfile;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public void setConnectionUrl(String connectionUrl){
		this.connectionUrl = connectionUrl;
	}
	
	public void setSelectedEntries(String[] selectedEntries){
		this.selectedEntries = selectedEntries;
	}
	
	public void setPrincipalDnSuffix(String principalDnSuffix){
		this.principalDnSuffix = principalDnSuffix;
	}


	public void setSelectedColumns(String[] selectedColumns) {
		this.selectedColumns = selectedColumns;
	}

}
