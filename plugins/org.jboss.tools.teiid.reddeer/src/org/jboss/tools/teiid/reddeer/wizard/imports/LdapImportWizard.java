package org.jboss.tools.teiid.reddeer.wizard.imports;

import java.util.Arrays;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.matcher.TreeItemTextMatcher;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.NextButton;
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

public class LdapImportWizard extends TeiidImportWizard {
		
	public static final String DIALOG_TITLE = "Create Relational Model from LDAP Service";
	
	private static final String TABLE_NAME = "Table Name";
	private static final String SOURCE_MODEL_TABLE_ATTRIBUTES = "Source Model Table Attributes";
	private static final String SOURCE_MODEL_DEFINITION = "Source Model Definition";
	private static final String CONNECTION_PROFILE = "Connection Profile";
	
	private String principalDnSuffix;
	
	private LdapImportWizard() {
		super("LDAP Service >> Source Model");
		log.info("Import ldap wizard is opened");
	}
	
	public static LdapImportWizard getInstance(){
		return new LdapImportWizard();
	}
	
	public static LdapImportWizard openWizard(){
		LdapImportWizard wizard = new LdapImportWizard();
		wizard.open();
		return wizard;
	}
	
	@Override
	public void finish(){
		super.finish();
		new WaitWhile(new IsInProgress(), TimePeriod.SHORT, false);
	}

	public LdapImportWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		return this;
	}
	
	public LdapImportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public LdapImportWizard setConnectionProfile(String connectionProfile) {
		log.info("Set connectionProfile to: '" + connectionProfile + "'");
		activate();
		new DefaultCombo(new DefaultGroup(CONNECTION_PROFILE), 0).setSelection(connectionProfile);
		return this;
	}
		
	public LdapImportWizard setLdapBaseDN(String principalDnSuffix) {
		log.info("Set LDAP base dn to: '" + principalDnSuffix + "'");
		activate();
		new PushButton("Fetch Base DNs").click();
		LabeledCombo ldapBaseDnCombo = new LabeledCombo("LDAP Base DN:");
		new WaitUntil(new WidgetIsEnabled(ldapBaseDnCombo), TimePeriod.LONG);
		ldapBaseDnCombo.setSelection(principalDnSuffix);
		this.principalDnSuffix = principalDnSuffix;
		return this;
	}
	
	public LdapImportWizard setProjectFolder(String project) {
		log.info("Set project folder to: '" + project + "'");
		activate();
		new PushButton(new DefaultGroup(SOURCE_MODEL_DEFINITION), "...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(project).select();
		new PushButton("OK").click();
		activate();
		return this;
	}
	
	public LdapImportWizard setModelName(String modelName) {
		log.info("Set model name to: '" + modelName + "'");
		activate();
		new LabeledText("Name").setText(modelName);
		return this;
	}
	
	public LdapImportWizard autoCreateDataSource(boolean checked) {
		log.info("Auto-Create Data Source is : '" + checked + "'");
		activate();
		CheckBox checkBox = new CheckBox("Auto-create Data Source");
		if(checked != checkBox.isChecked()){
			checkBox.click();
		}
		return this;
	}
	
	public LdapImportWizard setJndiName(String JndiName) {
		log.info("Set JNDI name to: '" + JndiName + "'");
		activate();
		new LabeledText("JNDI Name").setText(JndiName);
		return this;
	}
	
	public LdapImportWizard selectEntries(String... selectedEntries) {
		log.info("Set entries to: '" + Arrays.toString(selectedEntries) + "'");
		activate();
		for (String entry : selectedEntries) {
			selectTableEntry(entry, null);
		}
		new PushButton("Validate").click();
		return this;
	}
	
	public LdapImportWizard selectColumns(String... selectedColumns) {
		log.info("Set columns to: '" + Arrays.toString(selectedColumns) + "'");
		activate();
		for (String column : selectedColumns) {
			String[] path = column.split("/");
			selectColumnEntry(path[0], path[1], null);
		}
		new PushButton("Validate").click();
		return this;
	}
	
	private void selectColumnEntry(String tableName, String entryName, String entryAlias) {
		new DefaultTreeItem(tableName).expand();

		TreeItem entryTreeItem = new DefaultTreeItem(tableName, entryName);
		entryTreeItem.select();
		entryTreeItem.setChecked(true);

		if (entryAlias != null) {
			new LabeledText(new DefaultGroup(SOURCE_MODEL_TABLE_ATTRIBUTES), TABLE_NAME).setText(entryAlias);
		}

	}

	private void selectTableEntry(String entryName, String entryAlias) {

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
}
