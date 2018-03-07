package org.jboss.tools.teiid.ui.bot.test.imports;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.junit.execution.annotation.RunIf;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.teiid.reddeer.ImportHelper;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.preference.TeiidDesignerPreferencePage;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.TeiidConnectionImportWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {
		ConnectionProfileConstants.SAP_IQ })
public class SapIq {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	public ImportHelper importHelper = null;

	private static final String PROJECT_NAME_JDBC = "jdbcImportTest";
	private static final String PROJECT_NAME_TEIID = "TeiidConnImporter";

	private static final String MODEL_NAME_SAP_IQ = "sapIQmodel";

	@Before
	public void before() {
		if (new ShellMenuItem(new WorkbenchShell(), "Project", "Build Automatically").isSelected()) {
			new ShellMenuItem(new WorkbenchShell(), "Project", "Build Automatically").select();
		}
		new ModelExplorer().createProject(PROJECT_NAME_JDBC);
		WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
		preferences.open();
		new TeiidDesignerPreferencePage(preferences).setTeiidConnectionImporterTimeout(240);
		new ModelExplorer().importProject(PROJECT_NAME_TEIID);
		new ModelExplorer().selectItem(PROJECT_NAME_TEIID);
		new ServersViewExt().refreshServer(teiidServer.getName());
		importHelper = new ImportHelper();
	}

    @After
    public void after(){
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.SAP_IQ);
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/" + ConnectionProfileConstants.SAP_IQ + "_DS");
        new ServersViewExt().deleteDatasource(teiidServer.getName(), "java:/Check_" + MODEL_NAME_SAP_IQ);

        new ModelExplorer().deleteAllProjectsSafely();
    }

	@Test
	public void sapIqTeiidtest() {
		Map<String,String> teiidImporterProperties = new HashMap<String, String>();
		teiidImporterProperties.put(TeiidConnectionImportWizard.IMPORT_PROPERTY_TABLE_NAME_PATTERN, "Small%");
		importHelper.importModelTeiid(PROJECT_NAME_TEIID, ConnectionProfileConstants.SAP_IQ, MODEL_NAME_SAP_IQ, teiidImporterProperties, TimePeriod.getCustom(120), teiidServer);		
		importHelper.checkImportedModelTeiid(PROJECT_NAME_TEIID, MODEL_NAME_SAP_IQ, "SmallA", "SmallB");
	}

	@Test
	@Jira("TEIIDDES-3073")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void sapIqJDBCtest() {
		importHelper.importModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_SAP_IQ, ConnectionProfileConstants.SAP_IQ, "bqt-server/TABLE/SmallA,bqt-server/TABLE/SmallB", false);
		importHelper.checkImportedTablesInModelJDBC(PROJECT_NAME_JDBC, MODEL_NAME_SAP_IQ, "SmallA", "SmallB", teiidServer);
	}

}