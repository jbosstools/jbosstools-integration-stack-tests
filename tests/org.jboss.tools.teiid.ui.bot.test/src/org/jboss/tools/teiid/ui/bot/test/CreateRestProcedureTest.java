package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfilesConstants;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.matcher.ModelColumnMatcher;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.util.SimpleHttpClient;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.GenerateRestProcedureWizard;
import org.jboss.tools.teiid.reddeer.wizard.ImportGeneralItemWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
	ConnectionProfilesConstants.ORACLE_11G_PARTS_SUPPLIER })
public class CreateRestProcedureTest {
	private static final String SUPPLIER_EXPECTED = "<elems pk_SUPPLIER_ID_in=\"S108\"><elem><SUPPLIER_ID>S108</SUPPLIER_ID><SUPPLIER_NAME>Olsen</SUPPLIER_NAME><SUPPLIER_STATUS>20</SUPPLIER_STATUS><SUPPLIER_CITY>Atlanta</SUPPLIER_CITY><SUPPLIER_STATE>GA</SUPPLIER_STATE></elem></elems>";

	private static final String PARTS_EXPECTED = "<elems pk_PART_ID_in=\"P305\"><elem><PART_ID>P305</PART_ID><PART_NAME>Cog</PART_NAME><PART_COLOR>Red</PART_COLOR><PART_WEIGHT>20</PART_WEIGHT></elem></elems>";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private static final String VIEW_MODEL_NAME = "PartsView.xmi";

	private static final String SOURCE_MODEL_NAME = "Parts.xmi";

	private static final String PROJECT_NAME = "CreateRestProcedureProject";
	private static final String VDB_NAME = PROJECT_NAME + "Vdb";

	private static final String TARGET_MODEL_NAME = "ProcView.xmi";

	private static final String REST_PROCEDURE_SUFFIX = "RestProc";

	private Project project;
	private static TeiidBot teiidBot = new TeiidBot();

	@Before
	public void importProject() {
		new ImportManager().importProject(teiidBot.toAbsolutePath("resources/projects/" + PROJECT_NAME));
		AbstractWait.sleep(TimePeriod.NORMAL);
		project = teiidBot.modelExplorer().getProject(PROJECT_NAME);
		project.select();
		project.refresh();
		new ModelExplorer().changeConnectionProfile(ConnectionProfilesConstants.ORACLE_11G_PARTS_SUPPLIER, PROJECT_NAME,
				SOURCE_MODEL_NAME);
	}

	@After
	public void deleteProject() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		try {
			new ShellMenu("File", "Save All").select();
		} catch (Exception ex) {
			// no editors open, ignore
		}
		AbstractWait.sleep(TimePeriod.NORMAL);

		try {
			new ShellMenu("File", "Close All").select();
		} catch (Exception ex) {
			// no editors open, ignore
		}
		AbstractWait.sleep(TimePeriod.NORMAL);

		new ServersViewExt().undeployVdb(teiidServer.getName(), VDB_NAME);

		new WorkbenchShell();
		new ModelExplorer();
		project.select();
		project.refresh();
		project.delete(true);
	}

	@Test
	public void testCreateProceduresFromSourceModel() {
		new WorkbenchShell();

		openRestProcedureWizard(SOURCE_MODEL_NAME);
		new GenerateRestProcedureWizard().setProject(PROJECT_NAME).setNewTargetModel(TARGET_MODEL_NAME)
				.setTables("PARTS", "SUPPLIER").finish();

		checkErrors();
		checkRestProcedure(TARGET_MODEL_NAME, "PARTS" + REST_PROCEDURE_SUFFIX, "PARTS/{pk_PART_ID_in}");
		checkRestProcedure(TARGET_MODEL_NAME, "SUPPLIER" + REST_PROCEDURE_SUFFIX, "SUPPLIER/{pk_SUPPLIER_ID_in}");

		createAndDeployVdb(TARGET_MODEL_NAME);
		generateAndDeployWAR();

		String modelUrl = TARGET_MODEL_NAME.replace(".xmi", "");
		checkWar(PARTS_EXPECTED, modelUrl + "/PARTS/P305");
		checkWar(SUPPLIER_EXPECTED, modelUrl + "/SUPPLIER/S108");
	}

	@Test
	public void testCreateProceduresFromViewModel() {
		new WorkbenchShell();

		openRestProcedureWizard(VIEW_MODEL_NAME);
		new GenerateRestProcedureWizard().setProject(PROJECT_NAME).setTables("PARTS", "SUPPLIER").finish();

		checkErrors();
		checkRestProcedure(VIEW_MODEL_NAME, "PARTS" + REST_PROCEDURE_SUFFIX, "PARTS/{pk_PART_ID_in}");
		checkRestProcedure(VIEW_MODEL_NAME, "SUPPLIER" + REST_PROCEDURE_SUFFIX, "SUPPLIER/{pk_SUPPLIER_ID_in}");

		createAndDeployVdb(VIEW_MODEL_NAME);
		generateAndDeployWAR();

		String modelUrl = VIEW_MODEL_NAME.replace(".xmi", "");
		checkWar(PARTS_EXPECTED, modelUrl + "/PARTS/P305");
		checkWar(SUPPLIER_EXPECTED, modelUrl + "/SUPPLIER/S108");

	}

	@Test
	public void testCannotSelectSourceModelAsProcedureTarget() {
		new WorkbenchShell();

		openRestProcedureWizard(SOURCE_MODEL_NAME);

		GenerateRestProcedureWizard wizard = new GenerateRestProcedureWizard().setProject(PROJECT_NAME);
		try {
			wizard.setExistingTargetModel(SOURCE_MODEL_NAME);
			fail("Should not allow selecting a source model as the target");
		} catch (CoreLayerException ex) {
			new PushButton("Cancel").click();
		}
	}

	@Test
	public void testCannotSetProcedureTargetToSourceModel() {
		openRestProcedureWizard(SOURCE_MODEL_NAME);

		GenerateRestProcedureWizard wizard = new GenerateRestProcedureWizard().setProject(PROJECT_NAME);
		try {
			wizard.setNewTargetModel(SOURCE_MODEL_NAME);
			wizard.setTables("PARTS", "SUPPLIER").finish();
			fail("Should not allow typing name of a source model with xmi extension as the target");
		} catch (Exception ex) {
			new PushButton("Cancel").click();
		}
		
		
		openRestProcedureWizard(SOURCE_MODEL_NAME);

		wizard = new GenerateRestProcedureWizard().setProject(PROJECT_NAME);
		try {
			wizard.setNewTargetModel(SOURCE_MODEL_NAME.replaceAll(".xmi$", ""));
			wizard.setTables("PARTS", "SUPPLIER").finish();
			fail("Should not allow typing name of a source model without xmi extension as the target");
		} catch (Exception ex) {
			new PushButton("Cancel").click();
		}
		
		teiidBot.modelEditor(SOURCE_MODEL_NAME).close();
	}

	private void openRestProcedureWizard(String modelName) {
		new ModelExplorer();
		project.getProjectItem(modelName).select();
		// TODO: move this into a new class in plugin
		new ContextMenu("Modeling", "Generate REST Virtual Procedures").select();
	}

	private void checkWar(String expected, String url) {
		String response = new SimpleHttpClient("http://localhost:8080/" + PROJECT_NAME + '/' + url)
				.setBasicAuth(teiidServer.getServerConfig().getServerBase().getProperty("teiidUser"),
						teiidServer.getServerConfig().getServerBase().getProperty("teiidPassword"))
				.get();
		assertEquals(expected, response);

	}

	private void createAndDeployVdb(String modelName) {
		VDBManager vdbManager = new VDBManager();
		vdbManager.createVDB(PROJECT_NAME, VDB_NAME);
		vdbManager.addModelsToVDB(PROJECT_NAME, VDB_NAME, new String[] { modelName });

		new ServersViewExt().refreshServer(teiidServer.getName());
		new VDBManager().deployVDB(new String[] { PROJECT_NAME, VDB_NAME + ".vdb" });
		new VDBManager().createVDBDataSource(new String[] { PROJECT_NAME, VDB_NAME + ".vdb" });
	}

	private void generateAndDeployWAR() {
		String war = PROJECT_NAME;

		Properties warProps = new Properties();
		warProps.setProperty("type", WAR.RESTEASY_TYPE);
		warProps.setProperty("contextName", war);
		warProps.setProperty("vdbJndiName", VDB_NAME);
		warProps.setProperty("saveLocation", teiidBot.toAbsolutePath("target"));
		warProps.setProperty("securityType", WAR.HTTPBasic_SECURITY);
		warProps.setProperty("realm", "teiid-security");
		warProps.setProperty("role", "user");

		new VDBManager().createWAR(warProps, PROJECT_NAME, VDB_NAME + ".vdb");

		Properties itemProps = new Properties();
		itemProps.setProperty("dirName", teiidBot.toAbsolutePath("target"));
		itemProps.setProperty("file", war + ".war");
		itemProps.setProperty("intoFolder", PROJECT_NAME);
		new ImportManager().importGeneralItem(ImportGeneralItemWizard.Type.FILE_SYSTEM, itemProps);

		new ModelExplorerManager().getWAR(PROJECT_NAME, war + ".war").deploy();

		AbstractWait.sleep(TimePeriod.getCustom(30));
	}

	private void checkRestProcedure(String modelName, String procedureName, String url) {
		// close all editors, just to be sure
		try {
			new ShellMenu("File", "Close All").select();
		} catch (Exception ex) {
			// no editors open, ignore
		}

		ModelExplorer mew = new ModelExplorer();
		mew.open(PROJECT_NAME, modelName);
		ModelEditor editor = teiidBot.modelEditor(modelName);
		assertTrue(editor.isActive());
		editor.showTabItem(ModelEditor.TABLE_EDITOR);
		editor.showSubTabItem("Procedures");

		DefaultTable table = new DefaultTable(0);

		int restMethodIndex = table.getHeaderIndex("REST:Rest Method");
		int restUriIndex = table.getHeaderIndex("REST:URI");

		TableItem procItem = table.getItems(new ModelColumnMatcher(modelName, procedureName)).get(0);

		// TODO: this might be fragile, see if it can be done differently
		assertEquals("Wrong REST url", url, procItem.getText(restUriIndex));
		assertEquals("Wrong REST method", "GET", procItem.getText(restMethodIndex));

	}

	private void checkErrors() {
		ProblemsView problems = new ProblemsView();
		assertTrue("There are validation errors", problems.getProblems(ProblemType.ERROR).isEmpty());
	}

}
