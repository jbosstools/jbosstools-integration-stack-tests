package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.matcher.ModelColumnMatcher;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.imports.LdapImportWizard;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
		ConnectionProfileConstants.LDAP,
		ConnectionProfileConstants.RHDS })
public class LdapImportTest {

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private static final String NEW_PROJECT = "LdapImport";
	private static final String LDAP_MODEL = "LdapImp";
	private static final String RHDS_MODEL = "RhdsImp";

	@BeforeClass
	public static void prepare() {
		if (new ShellMenu("Project", "Build Automatically").isSelected()) {
			new ShellMenu("Project", "Build Automatically").select();
		}
		new ModelExplorer().createProject(NEW_PROJECT);
	}

	@Before
	public void openTeiidPerspective() {
		new TeiidPerspective().open();
	}

	@Test
	public void rhdsImport() {

		Properties cpProperties = teiidServer.getServerConfig().getConnectionProfile("rhds").asProperties();
		Properties importProperties = new ResourceFileHelper()
				.getProperties(new File("resources/importWizard/rhds.properties").getAbsolutePath());

		LdapImportWizard wizard = new LdapImportWizard();
		wizard.open();
	    wizard.setConnectionProfile(ConnectionProfileConstants.RHDS)
			  .setLdapBaseDN(cpProperties.getProperty("principalDnSuffix"))
			  .setModelName(RHDS_MODEL)
			  .setProjectFolder(NEW_PROJECT)
			  .next();
		String objects;
		objects = importProperties.getProperty("selectedEntries");
		wizard.selectEntries(objects.split(","))
			  .next();
		objects = importProperties.getProperty("selectedColumns");
		wizard.selectColumns(objects.split(","))
			  .finish();
		
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		Project project = modelExplorer.getProject(NEW_PROJECT);
		project.getProjectItem(RHDS_MODEL + ".xmi").open();

		ModelEditor editor = new ModelEditor(RHDS_MODEL + ".xmi");
		assertTrue(editor.isActive());
		editor.showTabItem(ModelEditor.TABLE_EDITOR);
		editor.showSubTabItem("Columns");
		DefaultTable table = new DefaultTable(0);

		assertColumns(table, importProperties.getProperty("selectedColumns"));

		modelExplorer.activate();

		ProjectItem modelItem = project.getProjectItem(RHDS_MODEL + ".xmi");
		addColumn(modelItem, "ou=People", "dn", "string");

		new ModelExplorer().simulateTablesPreview(teiidServer, NEW_PROJECT, RHDS_MODEL, new String[] { "ou=People", "ou=Apps" });

	}

	@Test
	@Jira("TEIIDDES-2793")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void ldapImport() {

		Properties cpProperties = teiidServer.getServerConfig().getConnectionProfile("ldap").asProperties();
		Properties importProperties = new ResourceFileHelper()
				.getProperties(new File("resources/importWizard/ldap.properties").getAbsolutePath());

		LdapImportWizard wizard = new LdapImportWizard();
		wizard.open();
	    wizard.setConnectionProfile(ConnectionProfileConstants.LDAP)
			  .setLdapBaseDN(cpProperties.getProperty("principalDnSuffix"))
			  .setModelName(LDAP_MODEL)
			  .setProjectFolder(NEW_PROJECT)
			  .next();
		String objects;
		objects = importProperties.getProperty("selectedEntries");
		wizard.selectEntries(objects.split(","))
			  .next();
		objects = importProperties.getProperty("selectedColumns");
		wizard.selectColumns(objects.split(","))
			  .finish();

		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		Project project = modelExplorer.getProject(NEW_PROJECT);
		project.getProjectItem(LDAP_MODEL + ".xmi").open();

		ModelEditor editor = new ModelEditor(LDAP_MODEL + ".xmi");
		assertTrue(editor.isActive());
		editor.showTabItem(ModelEditor.TABLE_EDITOR);
		editor.showSubTabItem("Columns");
		DefaultTable table = new DefaultTable(0);

		assertColumns(table, importProperties.getProperty("selectedColumns"));

		modelExplorer.activate();

		ProjectItem modelItem = project.getProjectItem(LDAP_MODEL + ".xmi");
		addColumn(modelItem, "ou=groups", "dn", "string");

		new ModelExplorer().simulateTablesPreview(teiidServer, NEW_PROJECT, LDAP_MODEL, new String[] { "ou=people", "ou=groups" });

	}

	private void assertColumns(DefaultTable table, String selectedColumns) {

		String[] columns = selectedColumns.split(",");
		for (String column : columns) {
			String[] path = column.split("/");
			assertEquals(1, table.getItems(new ModelColumnMatcher(path[0], path[1])).size());
		}

		assertEquals(columns.length, table.getItems().size());

	}

	private void addColumn(ProjectItem modelItem, String tableName, String columnName, String dataType) {
		try {
			new DefaultShell();
			new ModelExplorer().activate();
			ProjectItem tableItem = modelItem.getChild(tableName);

			tableItem.select();
			new ContextMenu("New Child", "Column").select();
			new DefaultText(0).setText(columnName);
			// Type Enter, selecting something else does not seem to work
			KeyboardFactory.getKeyboard().type(SWT.CR);
			// wait until the column is renamed, should probably implement a condition
			AbstractWait.sleep(TimePeriod.NORMAL);
			new ModelExplorer().activate();
			tableItem.getChild(columnName).select();
			new ContextMenu("Modeling", "Set Datatype").select();
			new DefaultTable().getItem(dataType).select();
			;
			new OkButton().click();
			new ModelEditor(modelItem.getName()).save();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

}
