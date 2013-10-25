package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.datatools.ui.FlatFileProfile;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.reference.ReferencedComposite;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.VDB;
import org.jboss.tools.teiid.reddeer.editor.CriteriaBuilder;
import org.jboss.tools.teiid.reddeer.editor.CriteriaBuilder.OperatorType;
import org.jboss.tools.teiid.reddeer.editor.InputSetEditor;
import org.jboss.tools.teiid.reddeer.editor.MappingDiagramEditor;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.Reconciler;
import org.jboss.tools.teiid.reddeer.editor.Reconciler.ExpressionBuilder;
import org.jboss.tools.teiid.reddeer.editor.RecursionEditor;
import org.jboss.tools.teiid.reddeer.editor.SQLScrapbookEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView.ConnectionSource;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel.ModelBuilder;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel.ModelClass;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel.ModelType;
import org.jboss.tools.teiid.reddeer.wizard.CreateVDB;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.XMLSchemaImportWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Automatized testscript tests importing XML schema, modeling recursive XML documents, using the TEXTTABLE 
 * function to access the data in a relational fashion, executing queries that return XML documents 
 * @author lfabriko
 *
 */
@Perspective(name = "Teiid Designer")
//@Server(type = Type.ALL, state = State.RUNNING)
public class E2eRecursiveXmlTextTest extends SWTBotTestCase {
	
	//models
	private static final String PROJECT_NAME = "Recursive";
	private static final String EMPLOYEES_VIEW = "Employees";
	private static final String EMP_TABLE = "EmpTable";
	private static final String EMP_V = "EmpV";
	private static final String VDB = "RecursiveVDB";
	private static final String EMPDATA_SOURCE = "EmpData_source";
	private static final String EMPLOYEES_SCHEMA_XSD = "EmployeesSchema.xsd";
	private static final String EMP_DOC_VIEW = "EmpDoc";
	private static final String SIMPLE_EMPLOYEES_DOCUMENT = "SimpleEmployeesDocument";
	
	//profile
	private static FlatFileProfile flatFileProfile;
	private static final String flatProfile = "Flat Profile";
	
	//elements
	private static final String ROOT_ELEM = "SimpleEmployees";
	private static final String EMPLOYEE = "Employee";
	private static final String SUPERVISOR = "Supervisor";
		
	//various
	private static TeiidBot teiidBot = new TeiidBot();
	
	//paths
	private static final String RESOURCES_FLAT = "resources/flat";
	private static final String RESOURCES_XSD = "resources/xsd";
	private static final String[] SUPERVISOR_XML_PATH = {SIMPLE_EMPLOYEES_DOCUMENT, ROOT_ELEM, "sequence", EMPLOYEE+" : SimpleEmployeeType", "sequence", SUPERVISOR+" : SimpleEmployeeType"};
	private static final String[] EMPLOYEE_COLUMNS = {"mgrID : positiveInteger"};
	private static final String[] SUPERVISOR_COLUMNS = {"LastName : string", "FirstName : string", "MiddleInitial : string", "Street : string", 
		"City : string", "State : State_._type", "EmpId : positiveInteger", "Phone : string", "mgrID : positiveInteger"};
	
	//SQL
	private static final String SQL1 = "select * from EmpDoc.SimpleEmployeesDocument where SimpleEmployees.Employee.Name.Firstname='Orsal'";
	
	@BeforeClass
	public static void createProject(){
		//create project
		/*new ModelProjectWizard(0).create(PROJECT_NAME, true);*/
		
		//create connection profile to csv
		flatFileProfile = teiidBot.createFlatFileProfile(flatProfile, RESOURCES_FLAT);
	}
	
	@Test
	public void test01(){
		//create relational source model 
		/*FlatImportWizard importWizard = new FlatImportWizard();
		importWizard.setProfile(flatProfile);
		importWizard.setName(EMPDATA_SOURCE);
		importWizard.setFile("EmpData.csv     <<<<");
		importWizard.setViewModelName(EMPLOYEES_VIEW);
		importWizard.setViewTableName(EMP_TABLE);
		importWizard.execute(true);

		//import xml schema
		XMLSchemaImportWizard xmlWizard = new XMLSchemaImportWizard();
		xmlWizard.setLocal(true);
		xmlWizard.setRootPath(RESOURCES_XSD);
		xmlWizard.setDestination(PROJECT_NAME);
		xmlWizard.setSchemas(new String[]{EMPLOYEES_SCHEMA_XSD});
		xmlWizard.execute();
		
		//create virtual document XML model
		CreateMetadataModel mModel = new CreateMetadataModel();
		mModel.setLocation(PROJECT_NAME);
		mModel.setClass(ModelClass.XML);
		mModel.setModelBuilder(ModelBuilder.BUILD_FROM_XML_SCHEMA);
		mModel.setType(ModelType.VIEW);
		mModel.setName(EMP_DOC_VIEW);
		mModel.open();
		mModel.fillFirstPage(true);
		mModel.fillSecondPage(new String[]{PROJECT_NAME, EMPLOYEES_SCHEMA_XSD}, ROOT_ELEM);
		mModel.next();
		mModel.next();
		//check the supervisor node
		new DefaultTreeItem(SUPERVISOR_XML_PATH).setChecked(true);
		mModel.finish();
		mModel.save();*/
		
		//employees mapping transformation model
		Project project = teiidBot.modelExplorer().getProject(PROJECT_NAME);
		project.getProjectItem(EMP_DOC_VIEW+".xmi", SIMPLE_EMPLOYEES_DOCUMENT).open();//open = doubleclick
		
		MappingDiagramEditor md = new MappingDiagramEditor(EMP_DOC_VIEW+".xmi");
		/*md.addMappingClassColumns(EMPLOYEE, EMPLOYEE_COLUMNS);
		md.addMappingClassColumns(SUPERVISOR, SUPERVISOR_COLUMNS);*/
		
		//employee - transf. diagram
		ModelEditor me = new ModelEditor(EMP_DOC_VIEW+".xmi");
		/*me.showMappingTransformation(EMPLOYEE);*/
		ModelExplorerView mew = TeiidPerspective.getInstance().getModelExplorerView();
		/*mew.addTransformationSource(PROJECT_NAME, EMPLOYEES_VIEW+".xmi", EMP_TABLE);
		new ModelEditor(EMP_DOC_VIEW+".xmi").save();*/
		
		//reconciller
		/*Reconciler rec = me.openReconciler();
		new SWTBot().widgets(new InnerButtonWithToolTipMatcher());*/
		/*rec.bindAttributes("MiddleInitial : string", "MiddleName");
		rec.bindAttributes("Phone : string", "HomePhone");
		rec.bindAttributes("mgrID : biginteger", "Manager");
		rec.clearRemainingUnmatchedSymbols();
		rec.resolveTypes(ExpressionBuilder.KEEP_VIRTUAL_TARGET);*/
		/*rec.close();
		new ModelEditor(EMP_DOC_VIEW+".xmi").save();*/
		
		/*new SWTWorkbenchBot().toolbarButtonWithTooltip("Show Parent Diagram").click();
		me.showMappingTransformation(SUPERVISOR);
		mew.addTransformationSource(PROJECT_NAME, EMPLOYEES_VIEW+".xmi", EMP_TABLE);
		InputSetEditor ise = me.openInputSetEditor(true);
		ise.createNewInputParam(EMPLOYEE, "mgrID : positiveInteger");
		ise.close();//and save
		me.save();*/
		
		new MappingDiagramEditor(EMP_DOC_VIEW+".xmi");
		RecursionEditor recEd = md.clickOnRecursiveButton(SUPERVISOR);
		recEd.enableRecursion();
		/*recEd.limitRecursion(3);
		recEd.close();*/
		
		
		me.showTransformation();
		//reconciller
		/*rec = me.openReconciler();
		rec.bindAttributes("MiddleInitial : string", "MiddleName");
		rec.bindAttributes("Phone : string", "HomePhone");
		rec.bindAttributes("mgrID : biginteger", "Manager");
		rec.clearRemainingUnmatchedSymbols();
		rec.resolveTypes(ExpressionBuilder.KEEP_VIRTUAL_TARGET);
		rec.close();
		
		//criteria builder
		CriteriaBuilder cb = me.criteriaBuilder();
		cb.selectLeftAttribute("INPUTS", "INPUTS.mgrID");
		cb.selectRightAttribute("Employees.EmpTable", "Employees.EmpTable.empId");
		cb.selectOperator(OperatorType.EQUALS);
		cb.apply();
		cb.close();
		me.save();
		
		//create new view model
		CreateMetadataModel cmm = new CreateMetadataModel();
		cmm.setName(EMP_V);
		cmm.setClass(ModelClass.RELATIONAL);
		cmm.setType(ModelType.VIEW);
		cmm.setModelBuilder(ModelBuilder.TRANSFORM_EXISTING);
		cmm.execute(ModelBuilder.TRANSFORM_EXISTING, PROJECT_NAME, EMPLOYEES_VIEW+".xmi");
		
		//uncomment @Server
		
		//create data source
		mew.createDataSource(ConnectionSource.USE_MODEL_CONNECTION_INFO, PROJECT_NAME, EMPDATA_SOURCE+".xmi");
		
		//create new vdb
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(PROJECT_NAME);
		createVDB.setName(VDB);
		createVDB.execute(true);

		VDBEditor editor = VDBEditor.getInstance(VDB + ".vdb");
		editor.show();
		editor.addModel(PROJECT_NAME, EMPDATA_SOURCE);
		editor.addModel(PROJECT_NAME, EMP_DOC_VIEW);
		editor.addModel(PROJECT_NAME, EMPLOYEES_VIEW);
		editor.addModel(PROJECT_NAME, EMPLOYEES_SCHEMA_XSD);
		editor.addModel(PROJECT_NAME, EMP_V);
		editor.save();
		
		//deploy, execute
		VDB vdb = new ModelExplorer().getModelProject(PROJECT_NAME).getVDB(VDB + ".vdb");
		vdb.deployVDB();
		vdb.executeVDB(true);
		
		SQLScrapbookEditor sqlEd = new SQLScrapbookEditor("SQL Scrapbook0");
		sqlEd.show();

		// TESTSQL_1
		sqlEd.setText(SQL1);
		sqlEd.executeAll(true);

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		SQLResult result = DatabaseDevelopmentPerspective.getInstance()
				.getSqlResultsView().getByOperation(SQL1);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		sqlEd.close();*/
		
	
		System.out.println();
	}
	
	@Before
	public void beforeMethod(){
		System.out.println("STARTING TEST METHOD");
	}
	
	@After
	public void afterMethod(){
		System.out.println("TEST METHOD END");
	}
	
	@AfterClass
	public static void closeShells(){
		new SWTWorkbenchBot().closeAllShells();
	}
	
	@Test
	public void test00(){
		openExisting();
	}
	
	
	private void openExisting(){
		new ShellMenu("File", "Import...").select();
		new DefaultShell("Import");
		new DefaultTreeItem(0, "General", "Existing Projects into Workspace").select();
		new PushButton("Next >").click();
		String path = "/home/lfabriko/Work/EXAMPLES/teiiddes/Recursive";//Recursive-clean/Recursive
		new DefaultCombo(0).setText(path);
		new SWTWorkbenchBot().activeShell().pressShortcut(Keystrokes.TAB);
		bot.sleep(2000);
		new PushButton("Finish").click();
		//new WaitWhile(new IsInProgress(), TimePeriod.NORMAL);
	}
	

	private class InnerButtonWithToolTipMatcher extends BaseMatcher {
		
		
		@Override
		public boolean matches(Object o) {//ToolItem
			if (o instanceof ToolBar){//new Button(((ToolBar)o).getParent(), SWT.CHECK);
			//if (o instanceof Button){
				//System.out.println(((ToolBar)o).getItemCount());
				//WidgetHandler.getInstance().click((Button)o);
				//columnButtons.add((ToolBar)o);
				//new CheckBox(new a((ToolBar)o)).click();
				
			return true;
			}
			return false;
		}

		@Override
		public void describeTo(Description arg0) {
			// TODO Auto-generated method stub
		}
	}
}
