package org.jboss.tools.teiid.ui.bot.test;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.InputSetEditor;
import org.jboss.tools.teiid.reddeer.editor.MappingDiagramEditor;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.RecursionEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.CreateVDB;
import org.jboss.tools.teiid.reddeer.wizard.ImportProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Automatized testscript tests importing XML schema, modeling recursive XML documents, using the TEXTTABLE function to
 * access the data in a relational fashion, executing queries that return XML documents
 * 
 * @author lfabriko, mmakovy
 * 
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class E2eRecursiveXmlTextTest {

	protected final Logger log = Logger.getLogger(this.getClass());

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private static final String RESOURCES_FLAT = "resources/flat/EmpData.csv";
	private static final String PROJECT_NAME = "Recursive";
	private static final String VDB_NAME = "RecursiveVDB";
	private static final String EMPDATA_SOURCE = "EmpData_Source";
	private static final String EMPLOYEES_SCHEMA_XSD = "EmployeesSchema.xsd";
	private static final String EMP_DOC_VIEW = "EmpDoc";
	private static final String SIMPLE_EMPLOYEES_DOCUMENT = "SimpleEmployeesDocument";
	private static final String EMPLOYEE_TRANSFORMATION = "SELECT Employees.EmpTable.LastName, Employees.EmpTable.FirstName, Employees.EmpTable.MiddleName "
			+ "AS MiddleInitial, Employees.EmpTable.Street, Employees.EmpTable.City, Employees.EmpTable.State, convert(Employees.EmpTable.EmpId, "
			+ "biginteger) AS EmpId,Employees.EmpTable.HomePhone AS Phone, convert(Employees.EmpTable.Manager,biginteger) AS mgrID FROM "
			+ "Employees.EmpTable";

	private static final String SUPERVISOR_TRANSFORMATION = "SELECT Employees.EmpTable.LastName, Employees.EmpTable.FirstName, "
			+ "Employees.EmpTable.MiddleName AS MiddleInitial, Employees.EmpTable.Street,"
			+ " Employees.EmpTable.City, Employees.EmpTable.State, convert(Employees.EmpTable.EmpId, biginteger) AS EmpId, Employees.EmpTable.HomePhone AS "
			+ "Phone, convert(Employees.EmpTable.Manager, biginteger) AS mgrID FROM Employees.EmpTable WHERE INPUTS.mgrID = Employees.EmpTable.EmpId";
	private static final String TESTSQL1 = "SELECT * FROM EmpDoc.SimpleEmployeesDocument WHERE SimpleEmployees.Employee.Name.Firstname LIKE '%i%'";
	private static final String TESTSQL2 = "SELECT * FROM Employees.EmpTable";
	private static final String TESTSQL3 = "SELECT * FROM EmpDoc.SimpleEmployeesDocument";

	private static final String ROOT_ELEM = "SimpleEmployees";
	private static final String EMPLOYEE = "Employee";
	private static final String SUPERVISOR = "Supervisor";

	private static TeiidBot teiidBot = new TeiidBot();

	private static final String[] EMPLOYEE_COLUMNS = { "mgrID : positiveInteger" };
	private static final String[] SUPERVISOR_COLUMNS1 = {
		"LastName : string",
		"FirstName : string",
		"MiddleInitial : string",
		"Street : string",
		"City : string" };
	private static final String[] SUPERVISOR_COLUMNS2 = {
		"EmpId : positiveInteger",
		"Phone : string",
		"mgrID : positiveInteger" };

	@BeforeClass
	public static void createProject() throws Exception {
		new TeiidBot().uncheckBuildAutomatically();
	}

	@Test
	public void recursiveXmlTest() throws Exception {

		Path source = Paths.get(new File(RESOURCES_FLAT).getAbsolutePath());
		Path target = Paths
				.get(teiidServer.getServerConfig().getServerBase().getHome() + "/standalone/data/EmpData.csv");

		Files.copy(source, target, REPLACE_EXISTING);

		ImportProjectWizard projectWizard = new ImportProjectWizard("resources/projects/Recursive");
		projectWizard.execute();

		// create virtual document XML model
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.setLocation(PROJECT_NAME);
		modelWizard.setModelName(EMP_DOC_VIEW);
		modelWizard.selectModelClass(ModelClass.XML);
		modelWizard.selectModelType(ModelType.VIEW);
		modelWizard.selectModelBuilder(ModelBuilder.BUILD_FROM_XML_SCHEMA);
		modelWizard.next();
		modelWizard.selectXMLSchemaFile(PROJECT_NAME, EMPLOYEES_SCHEMA_XSD);
		modelWizard.addElement(ROOT_ELEM);
		modelWizard.finish();

		new ServerManager().getServersViewExt().refreshServer(teiidServer.getName());

		// employees mapping transformation model
		Project project = teiidBot.modelExplorer().getProject(PROJECT_NAME);
		project.getProjectItem(EMP_DOC_VIEW + ".xmi", SIMPLE_EMPLOYEES_DOCUMENT).open();

		MappingDiagramEditor mappingDiagramEditor = new MappingDiagramEditor(EMP_DOC_VIEW + ".xmi");
		mappingDiagramEditor.addMappingClassColumns(EMPLOYEE, EMPLOYEE_COLUMNS);

		// attributes must be in the same order!
		mappingDiagramEditor.addMappingClassColumns(SUPERVISOR, SUPERVISOR_COLUMNS1);
		stupidWait(20);
		mappingDiagramEditor.copyAttribute(EMPLOYEE, SUPERVISOR, "State");
		mappingDiagramEditor.addMappingClassColumns(SUPERVISOR, SUPERVISOR_COLUMNS2);

		// employee - transf. diagram
		new ModelEditor(EMP_DOC_VIEW + ".xmi").showMappingTransformation(EMPLOYEE);
		new ModelEditor(EMP_DOC_VIEW + ".xmi").setTransformation(EMPLOYEE_TRANSFORMATION);
		new ModelEditor(EMP_DOC_VIEW + ".xmi").save();

		ModelEditor modelEditor = new ModelEditor(EMP_DOC_VIEW + ".xmi");
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Show Parent Diagram").click();

		// supervisor document
		modelEditor.showMappingTransformation(SUPERVISOR);
		InputSetEditor ise = modelEditor.openInputSetEditor(true);
		ise.createNewInputParam(EMPLOYEE, "mgrID : positiveInteger");
		ise.close();

		modelEditor.save();

		new SWTWorkbenchBot().toolbarButtonWithTooltip("Show Parent Diagram").click();
		modelEditor.showMappingTransformation(SUPERVISOR);
		modelEditor.setTransformation(SUPERVISOR_TRANSFORMATION);

		modelEditor.save();

		RecursionEditor recEd = mappingDiagramEditor.clickOnRecursiveButton(SUPERVISOR);
		recEd.enableRecursion();
		recEd.close();
		mappingDiagramEditor.showTransformation();
		mappingDiagramEditor.save();

		// create new vdb
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(PROJECT_NAME);
		createVDB.setName(VDB_NAME);
		createVDB.execute(true);

		// add models to the vdb
		VDBEditor editor = VDBEditor.getInstance(VDB_NAME + ".vdb");
		editor.show();
		editor.addModel(PROJECT_NAME, EMPDATA_SOURCE);
		editor.addModel(PROJECT_NAME, EMP_DOC_VIEW);
		editor.save();

		// execute vdb
		VDB vdb = new ModelExplorer().getModelProject(PROJECT_NAME).getVDB(VDB_NAME + ".vdb");
		vdb.executeVDB();

		// check sql

		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);

		// TODO CHECK VDB more precisely
		assertTrue(jdbchelper.isQuerySuccessful(TESTSQL1,true));
		assertTrue(jdbchelper.isQuerySuccessful(TESTSQL2,true));
		assertTrue(jdbchelper.isQuerySuccessful(TESTSQL3,true));
	}

	@AfterClass
	public static void closeShells() {
		new SWTWorkbenchBot().closeAllShells();
	}

	private void stupidWait(int seconds) {
		long time = seconds * 1000;
		log.info("Stupid waiting for " + seconds + " s");
		new SWTWorkbenchBot().sleep(time);
	}
}
