package org.jboss.tools.teiid.ui.bot.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.editor.CriteriaBuilder;
import org.jboss.tools.teiid.reddeer.editor.CriteriaBuilder.OperatorType;
import org.jboss.tools.teiid.reddeer.editor.InputSetEditor;
import org.jboss.tools.teiid.reddeer.editor.MappingDiagramEditor;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.Reconciler;
import org.jboss.tools.teiid.reddeer.editor.Reconciler.ExpressionBuilder;
import org.jboss.tools.teiid.reddeer.editor.RecursionEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView.ConnectionSourceType;
import org.jboss.tools.teiid.reddeer.wizard.CreateVDB;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.XMLSchemaImportWizard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Automatized testscript tests importing XML schema, modeling recursive XML
 * documents, using the TEXTTABLE function to access the data in a relational
 * fashion, executing queries that return XML documents
 * 
 * @author lfabriko
 * 
 */
@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class E2eRecursiveXmlTextTest extends SWTBotTestCase {
	
	@InjectRequirement
	private TeiidServerRequirement teiidServer;

	// models
	private static final String PROJECT_NAME = "Recursive";
	private static final String EMPLOYEES_VIEW = "Employees";
	private static final String EMP_TABLE = "EmpTable";
	private static final String EMP_V = "EmpV";
	private static final String VDB = "RecursiveVDB";
	private static String EMPDATA_SOURCE = "EmpData";
	private static final String EMPLOYEES_SCHEMA_XSD = "EmployeesSchema.xsd";
	private static final String EMP_DOC_VIEW = "EmpDoc";
	private static final String SIMPLE_EMPLOYEES_DOCUMENT = "SimpleEmployeesDocument";

	// profile
	private static FlatFileProfile flatFileProfile;
	private static final String flatProfile = "Flat Profile";

	// elements
	private static final String ROOT_ELEM = "SimpleEmployees";
	private static final String EMPLOYEE = "Employee";
	private static final String SUPERVISOR = "Supervisor";

	// various
	private static TeiidBot teiidBot = new TeiidBot();

	// paths
	private static final String RESOURCES_FLAT = "resources/flat";
	private static final String RESOURCES_XSD = "resources/xsd";
	private static final String[] SUPERVISOR_XML_PATH = { SIMPLE_EMPLOYEES_DOCUMENT, ROOT_ELEM, "sequence",
			EMPLOYEE + " : SimpleEmployeeType", "sequence", SUPERVISOR + " : SimpleEmployeeType" };
	private static final String[] EMPLOYEE_COLUMNS = { "mgrID : positiveInteger" };
	private static final String[] SUPERVISOR_COLUMNS1 = { "LastName : string", "FirstName : string",
			"MiddleInitial : string", "Street : string", "City : string" };
	private static final String[] SUPERVISOR_COLUMNS2 = { "EmpId : positiveInteger", "Phone : string",
			"mgrID : positiveInteger" };

	private static final String SQL = "select * from EmpDoc.SimpleEmployeesDocument where SimpleEmployees.Employee.Name.Firstname='Orsal'";

	@BeforeClass
	public static void createProject() throws Exception {
		new WorkbenchShell().maximize();

		// create project
		new ModelExplorerManager().createProject(PROJECT_NAME, true);

		// create connection profile to csv
		flatFileProfile = new ConnectionProfileManager().createCPFlatFile(flatProfile,
				new File(RESOURCES_FLAT).getAbsolutePath());
	}

	@Test
	public void recursiveXmlTest() throws Exception {
		// create relational source model
		FlatImportWizard importWizard = new FlatImportWizard();
		importWizard.open();
		importWizard.selectLocalFileImportMode();
		importWizard.next();
		importWizard.selectProfile(flatProfile);
		importWizard.selectFile("EmpData.csv     <<<<");
		importWizard.setSourceModel(EMPDATA_SOURCE);
		importWizard.setProject(PROJECT_NAME);
		importWizard.next();
		importWizard.next();
		importWizard.next();
		importWizard.setViewModel(EMPLOYEES_VIEW);
		importWizard.setViewTable(EMP_TABLE);
		importWizard.setProject(PROJECT_NAME);
		importWizard.finish();

		// import xml schema
		XMLSchemaImportWizard xmlWizard = new XMLSchemaImportWizard();
		xmlWizard.open();
		xmlWizard.selectLocalImportMode();
		xmlWizard.next();
		xmlWizard.setFromDirectory(new File(RESOURCES_XSD).getAbsolutePath());
		xmlWizard.setToDirectory(PROJECT_NAME);
		xmlWizard.selectSchema(EMPLOYEES_SCHEMA_XSD);
		xmlWizard.finish();

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

		// employees mapping transformation model
		Project project = teiidBot.modelExplorer().getProject(PROJECT_NAME);
		project.getProjectItem(EMP_DOC_VIEW + ".xmi", SIMPLE_EMPLOYEES_DOCUMENT).open();

		MappingDiagramEditor mappingDiagramEditor = new MappingDiagramEditor(EMP_DOC_VIEW + ".xmi");
		mappingDiagramEditor.addMappingClassColumns(EMPLOYEE, EMPLOYEE_COLUMNS);

		// attributes must be in the same order!
		mappingDiagramEditor.addMappingClassColumns(SUPERVISOR, SUPERVISOR_COLUMNS1);
		mappingDiagramEditor.copyAttribute(EMPLOYEE, SUPERVISOR, "State");
		mappingDiagramEditor.addMappingClassColumns(SUPERVISOR, SUPERVISOR_COLUMNS2);

		// employee - transf. diagram
		new ModelEditor(EMP_DOC_VIEW + ".xmi").showMappingTransformation(EMPLOYEE);
		ModelExplorerView mew = TeiidPerspective.getInstance().getModelExplorerView();
		mew.addTransformationSource(PROJECT_NAME, EMPLOYEES_VIEW + ".xmi", EMP_TABLE);
		new ModelEditor(EMP_DOC_VIEW + ".xmi").save();

		// reconciller
		Reconciler rec = new ModelEditor(EMP_DOC_VIEW + ".xmi").openReconciler();
		rec.bindAttributes("MiddleInitial : string", "MiddleName");
		rec.bindAttributes("Phone : string", "HomePhone");
		rec.bindAttributes("mgrID : biginteger", "Manager");
		rec.clearRemainingUnmatchedSymbols();
		rec.resolveTypes(ExpressionBuilder.KEEP_VIRTUAL_TARGET);
		rec.close();

		ModelEditor modelEditor = new ModelEditor(EMP_DOC_VIEW + ".xmi");
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Show Parent Diagram").click();
		modelEditor.showMappingTransformation(SUPERVISOR);
		mew.addTransformationSource(PROJECT_NAME, EMPLOYEES_VIEW + ".xmi", EMP_TABLE);
		InputSetEditor ise = modelEditor.openInputSetEditor(true);
		ise.createNewInputParam(EMPLOYEE, "mgrID : positiveInteger");
		ise.close();// and save
		modelEditor.save();

		mappingDiagramEditor = new MappingDiagramEditor(EMP_DOC_VIEW + ".xmi");
		RecursionEditor recEd = mappingDiagramEditor.clickOnRecursiveButton(SUPERVISOR);
		recEd.enableRecursion();
		recEd.close();
		mappingDiagramEditor.showTransformation();
		mappingDiagramEditor.save();

		// reconciller
		rec = modelEditor.openReconciler();
		rec.bindAttributes("MiddleInitial : string", "MiddleName");
		rec.bindAttributes("Phone : string", "HomePhone");
		rec.bindAttributes("mgrID : biginteger", "Manager");
		rec.clearRemainingUnmatchedSymbols();
		rec.resolveTypes(ExpressionBuilder.KEEP_VIRTUAL_TARGET);
		rec.close();

		// criteria builder
		CriteriaBuilder cb = modelEditor.criteriaBuilder();
		cb.selectLeftAttribute("INPUTS", "mgrID");
		cb.selectRightAttribute("Employees.EmpTable", "EmpId");
		cb.selectOperator(OperatorType.EQUALS);
		cb.apply();
		cb.close();
		modelEditor.save();

		// create new view model
		modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.setLocation(PROJECT_NAME);
		modelWizard.setModelName(EMP_V);
		modelWizard.selectModelClass(ModelClass.RELATIONAL);
		modelWizard.selectModelType(ModelType.VIEW);
		modelWizard.selectModelBuilder(ModelBuilder.TRANSFORM_EXISTING);
		modelWizard.next();
		modelWizard.setExistingModel(PROJECT_NAME, EMPLOYEES_VIEW + ".xmi");
		modelWizard.finish();

		// create data source
		mew.createDataSource(ConnectionSourceType.USE_MODEL_CONNECTION_INFO, null, PROJECT_NAME, EMPDATA_SOURCE
				+ "Source.xmi");

		// create new vdb
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(PROJECT_NAME);
		createVDB.setName(VDB);
		createVDB.execute(true);

		// add models to the vdb
		VDBEditor editor = VDBEditor.getInstance(VDB + ".vdb");
		editor.show();
		editor.addModel(PROJECT_NAME, EMPDATA_SOURCE + "Source");
		editor.addModel(PROJECT_NAME, EMP_DOC_VIEW);
		editor.addModel(PROJECT_NAME, EMP_V);
		editor.save();

		// deploy vdb
		VDB vdb = new ModelExplorer().getModelProject(PROJECT_NAME).getVDB(VDB + ".vdb");
		vdb.deployVDB();

		// check sql
		checkSql(SQL);
	}

	@AfterClass
	public static void closeShells() {
		new SWTWorkbenchBot().closeAllShells();
	}

	private void checkSql(String sql) throws SQLException {
		// register teiid driver
		DriverManager.registerDriver(teiidServer.getTeiidDriver());
		// create connection
		Connection conn = DriverManager.getConnection("jdbc:teiid:" + VDB + "@mm://localhost:31000", "user", "user");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		conn.close();
	}
}
