package org.jboss.tools.teiid.ui.bot.test.ddl;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

import java.util.List;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsResourceMatcher;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.teiid.reddeer.DdlHelper;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.dialog.DataRolesDialog;
import org.jboss.tools.teiid.reddeer.dialog.DataRolesDialog.PermissionType;
import org.jboss.tools.teiid.reddeer.dialog.GenerateVdbArchiveDialog;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportFromFileSystemWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)

@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles={
		ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, ConnectionProfileConstants.POSTGRESQL_92_DVQE
})
public class StaticVDBdataRoles {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	public DdlHelper ddlHelper = null;
	
	private static final String PROJECT_NAME = "StaticVDBdataRoles";
	private static final String NAME_SOURCE_MODEL = "postgresql92Model";
	private static final String NAME_VIEW_MODEL = "bqtViewModel";
	private static final String NAME_VDB = "DataRolesVdb";
	private static final String NAME_ORIGINAL_DYNAMIC_VDB = NAME_VDB + "-vdb.xml";
	
	private static final String NAME_GENERATED_DYNAMIC_VDB = "StaticVDBgenerated-vdb.xml";
	
	private static final String WORK_PROJECT_NAME = "workProject" ;
	
	@Before
	public void before() {
		ModelExplorer explorer = new ModelExplorer();
		explorer.deleteAllProjectsSafely();

		ddlHelper = new DdlHelper(collector);
		
		explorer.importProject("DDLtests/"+PROJECT_NAME);
		explorer.changeConnectionProfile(ConnectionProfileConstants.POSTGRESQL_92_DVQE, PROJECT_NAME, NAME_SOURCE_MODEL);
		/* data source is needed when exported VDB will be tested to deploy on the server */		
		explorer.createDataSource("Use Connection Profile Info",ConnectionProfileConstants.POSTGRESQL_92_DVQE, PROJECT_NAME, NAME_SOURCE_MODEL);
		
		explorer.createProject(WORK_PROJECT_NAME);
	}
	
	@After
	public void after(){
		new ModelExplorer().deleteAllProjectsSafely();
	}

	@Test
	public void importVdb(){
		ImportFromFileSystemWizard.openWizard()
				.setPath("resources/projects/DDLtests/"+PROJECT_NAME)
				.setFolder(WORK_PROJECT_NAME)
				.selectFile(NAME_ORIGINAL_DYNAMIC_VDB)
				.setCreteTopLevelFolder(false)
				.finish();
		GenerateVdbArchiveDialog wizard = new ModelExplorer().generateVdbArchive(WORK_PROJECT_NAME, NAME_ORIGINAL_DYNAMIC_VDB);
		wizard.next()
				.generate()
				.finish();		
		
		checkVDB();
		
		/*all models must be opened before synchronize VDB*/
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME,NAME_SOURCE_MODEL+".xmi");
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME,NAME_VDB+".vdb");
		VdbEditor staticVdb = VdbEditor.getInstance(NAME_VDB);
		staticVdb.synchronizeAll();
		new DefaultShell("Confirm");
		new PushButton("OK").click();
		staticVdb.saveAndClose();
		/*test deploy generated VDB from dynamic VDB*/
		String status = ddlHelper.deploy(WORK_PROJECT_NAME, NAME_VDB, teiidServer);	
		collector.checkThat("vdb is not active", status, is("ACTIVE"));
	}
	
	
	private void checkVDB(){		
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME, NAME_VDB + ".vdb");
		VdbEditor vdbEditor = VdbEditor.getInstance(NAME_VDB);

		DataRolesDialog dre = vdbEditor.getDataRole("readers");
		List<String> roles = dre.getRoles();
		collector.checkThat("wrong groups assigned to readers role", roles, contains("readers", "user"));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.READ, NAME_VIEW_MODEL +".xmi"), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.ALTER, NAME_VIEW_MODEL +".xmi"), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.CREATE, NAME_VIEW_MODEL +".xmi"), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.DELETE, NAME_VIEW_MODEL +".xmi"), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.EXECUTE, NAME_VIEW_MODEL +".xmi"), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.UPDATE, NAME_VIEW_MODEL +".xmi"), is(false));
		// TODO: change the following to int once TEIIDDES-2737 is resolved
		collector.checkThat("wrong permission for bqtViewModel.smalla.intnum",
				dre.getModelPermission(PermissionType.READ, NAME_VIEW_MODEL +".xmi", "smalla", "intnum : int"),
				is(false));
		collector.checkThat("wrong permission for bqtViewModel.smallb",
				dre.getModelPermission(PermissionType.READ, NAME_VIEW_MODEL +".xmi", "smallb"), is(false));

		collector.checkThat("wrong column mask for bqtViewModel.smalla.intnum",
				dre.getColumnMask("bqtViewModel.smalla.intnum"), is("CASE WHEN TRUE THEN 'SECRET' END"));
		collector.checkThat("wrong condition mask for bqtViewModel.smalla.intnum",
				dre.getColumnMaskCondition("bqtViewModel.smalla.intnum"), is(""));
		collector.checkThat("wrong column mask for bqtViewModel.smalla.stringnum",
				dre.getColumnMask("bqtViewModel.smalla.stringnum"), is("0"));
		collector.checkThat("wrong condition mask for bqtViewModel.smalla.stringnum",
				dre.getColumnMaskCondition("bqtViewModel.smalla.stringnum"), is("stringnum='1'"));
		dre.cancel();

		dre = vdbEditor.getDataRole("admins");
		roles = dre.getRoles();
		collector.checkThat("wrong groups assigned to admins role", roles, contains("admins"));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.READ, NAME_VIEW_MODEL +".xmi"), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.ALTER, NAME_VIEW_MODEL +".xmi"), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.CREATE, NAME_VIEW_MODEL +".xmi"), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.DELETE, NAME_VIEW_MODEL +".xmi"), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.EXECUTE, NAME_VIEW_MODEL +".xmi"), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.UPDATE, NAME_VIEW_MODEL +".xmi"), is(true));
		dre.cancel();

		dre = vdbEditor.getDataRole("updaters");
		roles = dre.getRoles();
		collector.checkThat("wrong groups assigned to updaters role", roles, contains("updaters"));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.READ, NAME_VIEW_MODEL +".xmi"), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.ALTER, NAME_VIEW_MODEL +".xmi"), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.CREATE, NAME_VIEW_MODEL +".xmi"), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.DELETE, NAME_VIEW_MODEL +".xmi"), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.EXECUTE, NAME_VIEW_MODEL +".xmi"), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.UPDATE, NAME_VIEW_MODEL +".xmi"), is(true));
		collector.checkThat("wrong permission for bqtViewModel.smalla.intkey",
				dre.getModelPermission(PermissionType.READ, NAME_VIEW_MODEL +".xmi", "smalla", "intkey : int"),
				is(true));
		collector.checkThat("wrong permission for bqtViewModel.smallb.intkey",
				dre.getModelPermission(PermissionType.READ, NAME_VIEW_MODEL +".xmi", "smallb", "intkey : int"),
				is(true));
		collector.checkThat("wrong permission for bqtViewModel.smalla.intnum",
				dre.getModelPermission(PermissionType.READ, NAME_VIEW_MODEL +".xmi", "smalla", "intnum : int"),
				is(false));
		collector.checkThat("wrong permission for bqtViewModel.smallb.intnum",
				dre.getModelPermission(PermissionType.READ, NAME_VIEW_MODEL +".xmi", "smallb", "intnum : int"),
				is(false));
		collector.checkThat("wrong row filter condition for bqtViewModel.smalla",
				dre.getRowFilterCondition("bqtViewModel.smalla"), is("booleanvalue=TRUE"));
		collector.checkThat("wrong row filter constraint value for bqtViewModel.smalla",
				dre.getRowFilterConstraint("bqtViewModel.smalla"), is("true"));
		collector.checkThat("wrong row filter condition for bqtViewModel.smallb",
				dre.getRowFilterCondition("bqtViewModel.smallb"), is("stringnum='1'"));
		collector.checkThat("wrong row filter constraint value for bqtViewModel.smallb",
				dre.getRowFilterConstraint("bqtViewModel.smallb"), is("false"));
		dre.cancel();

		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_VDB + ".vdb")),
				empty());
	}
	
	@Test
	public void exportVdb(){		
		VdbWizard.openVdbWizard()
				.setName(NAME_VDB)
				.setLocation(PROJECT_NAME)
				.addModel(PROJECT_NAME, NAME_VIEW_MODEL)				
				.finish();

		new ModelExplorer().openModelEditor(PROJECT_NAME, NAME_VDB + ".vdb");		
		VdbEditor vdbEditor = VdbEditor.getInstance(NAME_VDB);

		// setup data roles
		DataRolesDialog dre;

		dre = vdbEditor.addDataRole();
		dre.setName("readers");
		dre.addRole("readers");
		dre.addRole("user");
		dre.setModelPermission(PermissionType.READ, true, NAME_VIEW_MODEL + ".xmi");
		dre.setModelPermission(PermissionType.READ, false, NAME_VIEW_MODEL + ".xmi", "smalla", "intnum : int");
		dre.setModelPermission(PermissionType.READ, false, NAME_VIEW_MODEL + ".xmi", "smallb");
		dre.addColumnMask("", "CASE WHEN TRUE THEN 'SECRET' END", 0, NAME_VIEW_MODEL + ".xmi", "smalla", "intnum : int");
		dre.addColumnMask("stringnum='1'", "0", 1, NAME_VIEW_MODEL + ".xmi", "smalla", "stringnum : string(20)");
		dre.finish();

		dre = vdbEditor.addDataRole();
		dre.setName("admins");
		dre.addRole("admins");
		dre.setModelPermission(PermissionType.CREATE, true, NAME_VIEW_MODEL + ".xmi");
		dre.setModelPermission(PermissionType.READ, true, NAME_VIEW_MODEL + ".xmi");
		dre.setModelPermission(PermissionType.UPDATE, true, NAME_VIEW_MODEL + ".xmi");
		dre.setModelPermission(PermissionType.DELETE, true, NAME_VIEW_MODEL + ".xmi");
		dre.setModelPermission(PermissionType.ALTER, true, NAME_VIEW_MODEL + ".xmi");
		dre.setModelPermission(PermissionType.EXECUTE, true, NAME_VIEW_MODEL + ".xmi");
		dre.finish();

		dre = vdbEditor.addDataRole();
		dre.setName("updaters");
		dre.addRole("updaters");
		dre.setModelPermission(PermissionType.UPDATE, true, NAME_VIEW_MODEL + ".xmi");
		dre.setModelPermission(PermissionType.READ, false, NAME_VIEW_MODEL + ".xmi");
		dre.setModelPermission(PermissionType.READ, true, NAME_VIEW_MODEL + ".xmi", "smalla", "intkey : int");
		dre.setModelPermission(PermissionType.READ, true, NAME_VIEW_MODEL + ".xmi", "smallb", "intkey : int");
		dre.finish(); // workaround unrelated TEIIDDES-2742
		dre = vdbEditor.getDataRole("updaters");
		dre.addRowFilter("booleanvalue=TRUE", true, NAME_VIEW_MODEL + ".xmi", "smalla");
		dre.addRowFilter("stringnum='1'", false, NAME_VIEW_MODEL + ".xmi", "smallb");
		dre.finish();

		vdbEditor.save();

		// create dynamic vdb from static
		String contentFile = ddlHelper.createDynamicVdb(PROJECT_NAME, NAME_VDB, NAME_GENERATED_DYNAMIC_VDB);
		checkExportedFile(contentFile);
		
		/*test deploy generated dynamic VDB from static VDB*/
		String status = ddlHelper.deploy(PROJECT_NAME, NAME_GENERATED_DYNAMIC_VDB, teiidServer); 		
		collector.checkThat("vdb is not active", status, is("ACTIVE"));
		
		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported source model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_SOURCE_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(NAME_VDB + ".vdb")),
				empty());	
	}
	
	private void checkExportedFile(String dynamicVdbContent){
		// check read permission on view model
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-create", "false");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-read", "true");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-update", "false");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-delete", "false");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-execute", "false");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-alter", "false");

		// check read permission on smalla.intnum column
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-create", "");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-read", "false");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-update", "");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-delete", "");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-execute", "");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-alter", "");

		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "condition", "");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "mask",
						"CASE WHEN TRUE THEN 'SECRET' END");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "mask/@order", "0");

		// check column mask on smalla.stringnum
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.stringnum", "condition",
						"stringnum='1'");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.stringnum", "mask", "0");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.stringnum", "mask/@order", "1");

		// check read permission on smallb.intnum column
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-create", "");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-read", "false");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-update", "");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-delete", "");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-execute", "");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-alter", "");

		// check admin permissions on view model
		ddlHelper.checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-create", "true");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-read", "true");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-update", "true");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-delete", "true");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-execute", "true");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-alter", "true");

		// check row filter for smalla
		ddlHelper.checkXpathPermission(dynamicVdbContent, "updaters", "bqtViewModel.smalla", "condition", "booleanvalue=TRUE");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "updaters", "bqtViewModel.smalla", "condition/@constraint", "");

		// check row filter for smallb
		ddlHelper.checkXpathPermission(dynamicVdbContent, "updaters", "bqtViewModel.smallb", "condition", "stringnum='1'");
		ddlHelper.checkXpathPermission(dynamicVdbContent, "updaters", "bqtViewModel.smallb", "condition/@constraint", "false");

}
}

