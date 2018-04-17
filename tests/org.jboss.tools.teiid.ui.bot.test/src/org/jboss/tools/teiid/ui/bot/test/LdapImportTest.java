package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Properties;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.swt.SWT;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TableEditor;
import org.jboss.tools.teiid.reddeer.matcher.ModelColumnMatcher;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.LdapImportWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {
		ConnectionProfileConstants.LDAP,
		ConnectionProfileConstants.RHDS })
public class LdapImportTest {

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private static final String NEW_PROJECT = "LdapImport";
	private static final String LDAP_MODEL = "LdapImp";
	private static final String RHDS_MODEL = "RhdsImp";

	@Before
	public void before() {
		if (new ShellMenuItem(new WorkbenchShell(), "Project", "Build Automatically").isSelected()) {
			new ShellMenuItem(new WorkbenchShell(), "Project", "Build Automatically").select();
		}
		new ModelExplorer().createProject(NEW_PROJECT);
	}

    @After
    public void after(){
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + RHDS_MODEL);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/Check_" + RHDS_MODEL);

        new ModelExplorer().deleteAllProjectsSafely();
    }

	@Test
	public void rhdsImport() {

		Properties cpProperties = teiidServer.getServerConfig().getConnectionProfile("rhds").asProperties();
		Properties importProperties = new ResourceFileHelper()
				.getProperties(new File("resources/importWizard/rhds.properties").getAbsolutePath());

		LdapImportWizard wizard = LdapImportWizard.openWizard();
		wizard.setConnectionProfile(ConnectionProfileConstants.RHDS)
				.setLdapBaseDN(cpProperties.getProperty("principalDnSuffix"))
				.setModelName(RHDS_MODEL)
				.setProjectFolder(NEW_PROJECT)
                .setJndiName("java:/"+ ConnectionProfileConstants.RHDS )
				.nextPage();
		String objects;
		objects = importProperties.getProperty("selectedEntries");
		wizard.selectEntries(objects.split(","))
				.nextPage();
		objects = importProperties.getProperty("selectedColumns");
		wizard.selectColumns(objects.split(","))
				.finish();
		
		ModelExplorer modelExplorer = new ModelExplorer();
		Project project = modelExplorer.getProject(NEW_PROJECT);
		project.getProjectItem(RHDS_MODEL + ".xmi").open();

		RelationalModelEditor editor = new RelationalModelEditor(RHDS_MODEL + ".xmi");
		TableEditor tableEditor = editor.openTableEditor();
		tableEditor.openTab(TableEditor.Tabs.COLUMNS);
		
		DefaultTable table = new DefaultTable(0);

		assertColumns(table, importProperties.getProperty("selectedColumns"));

		modelExplorer.activate();

		ProjectItem modelItem = project.getProjectItem(RHDS_MODEL + ".xmi");
		addColumn(modelItem, "ou=People", "dn", "string");

		new ModelExplorer().simulateTablesPreview(teiidServer, NEW_PROJECT, RHDS_MODEL, new String[] { "ou=People", "ou=Apps" });

        new ServersViewExt().undeployVdb(teiidServer.getName(), "Check_" + RHDS_MODEL);
	}

	@Test
	public void ldapImport() {

		Properties cpProperties = teiidServer.getServerConfig().getConnectionProfile("ldap").asProperties();
		Properties importProperties = new ResourceFileHelper()
				.getProperties(new File("resources/importWizard/ldap.properties").getAbsolutePath());

		LdapImportWizard wizard = LdapImportWizard.openWizard();
		wizard.setConnectionProfile(ConnectionProfileConstants.LDAP)
				.setLdapBaseDN(cpProperties.getProperty("principalDnSuffix"))
				.setModelName(LDAP_MODEL)
				.setJndiName("java:/" + ConnectionProfileConstants.LDAP)
				.setProjectFolder(NEW_PROJECT)
				.nextPage();
		String objects;
		objects = importProperties.getProperty("selectedEntries");
		wizard.selectEntries(objects.split(","))
				.nextPage();
		objects = importProperties.getProperty("selectedColumns");
		wizard.selectColumns(objects.split(","))
				.finish();

		ModelExplorer modelExplorer = new ModelExplorer();
		Project project = modelExplorer.getProject(NEW_PROJECT);
		project.getProjectItem(LDAP_MODEL + ".xmi").open();

		RelationalModelEditor editor = new RelationalModelEditor(LDAP_MODEL + ".xmi");
		TableEditor tableEditor = editor.openTableEditor();
		tableEditor.openTab(TableEditor.Tabs.COLUMNS);
		
		DefaultTable table = new DefaultTable(0);

		assertColumns(table, importProperties.getProperty("selectedColumns"));

		modelExplorer.activate();

		ProjectItem modelItem = project.getProjectItem(LDAP_MODEL + ".xmi");
		addColumn(modelItem, "ou=groups", "dn", "string");
		if(new JiraClient().isIssueClosed("TEIIDDES-2793)")){
			new ModelExplorer().simulateTablesPreview(teiidServer, NEW_PROJECT, LDAP_MODEL, new String[] { "ou=people", "ou=groups" });
		}
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
			ProjectItem tableItem = modelItem.getProjectItem(tableName);

			tableItem.select();
			new ContextMenuItem("New Child", "Column").select();
			new DefaultText(0).setText(columnName);
			// Type Enter, selecting something else does not seem to work
			KeyboardFactory.getKeyboard().type(SWT.CR);
			// wait until the column is renamed, should probably implement a condition
			AbstractWait.sleep(TimePeriod.DEFAULT);
			new ModelExplorer().activate();
			tableItem.getProjectItem(columnName).select();
			new ContextMenuItem("Modeling", "Set Datatype").select();
			new DefaultTable().getItem(dataType).select();
			;
			new OkButton().click();
			new RelationalModelEditor(modelItem.getName()).save();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

}
