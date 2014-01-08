package org.jboss.tools.teiid.ui.bot.test;

import java.io.File;

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
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.State;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Type;
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
@Server(type = Type.ALL, state = State.RUNNING)
public class E2eRecursiveXmlTextTest extends SWTBotTestCase {
	
	//models
	private static final String PROJECT_NAME = "Recursive";
	private static final String EMPLOYEES_VIEW = "Employees";
	private static final String EMP_TABLE = "EmpTable";
	private static final String EMP_V = "EmpV";
	private static final String VDB = "RecursiveVDB";
	private static String EMPDATA_SOURCE = "EmpData";
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
	private static final String[] SUPERVISOR_COLUMNS1 = {"LastName : string", "FirstName : string", "MiddleInitial : string", "Street : string", 
		"City : string"};
	private static final String[] SUPERVISOR_COLUMNS2 = {"EmpId : positiveInteger", "Phone : string", "mgrID : positiveInteger"};
	
	//SQL
	private static final String SQL1 = "select * from EmpDoc.SimpleEmployeesDocument where SimpleEmployees.Employee.Name.Firstname='Orsal'";
	
	@BeforeClass
	public static void createProject(){
		//create project
		new ModelProjectWizard(0).create(PROJECT_NAME, true);
		
		//create connection profile to csv
		flatFileProfile = teiidBot.createFlatFileProfile(flatProfile, new File(RESOURCES_FLAT).getAbsolutePath());
	}
	
	@Test
	public void test01(){
		try{
		//create relational source model 
		FlatImportWizard importWizard = new FlatImportWizard();
		importWizard.setProfile(flatProfile);
		importWizard.setName(EMPDATA_SOURCE);
		importWizard.setFile("EmpData.csv     <<<<");
		importWizard.setViewModelName(EMPLOYEES_VIEW);
		importWizard.setViewTableName(EMP_TABLE);
		importWizard.execute(true);
} catch (Exception e){
			
		}
		
		try{
		//import xml schema
		XMLSchemaImportWizard xmlWizard = new XMLSchemaImportWizard();
		xmlWizard.setLocal(true);
		xmlWizard.setRootPath(new File(RESOURCES_XSD).getAbsolutePath());
		xmlWizard.setDestination(PROJECT_NAME);
		xmlWizard.setSchemas(new String[]{EMPLOYEES_SCHEMA_XSD});
		xmlWizard.execute();
} catch (Exception e){
			
		}
		
		try{
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
		//mModel.save();
} catch (Exception e){
			
		}
		
		try{
		//employees mapping transformation model
		Project project = teiidBot.modelExplorer().getProject(PROJECT_NAME);
		project.getProjectItem(EMP_DOC_VIEW+".xmi", SIMPLE_EMPLOYEES_DOCUMENT).open();//open = doubleclick
} catch (Exception e){
			
		}
		
		
		MappingDiagramEditor md = new MappingDiagramEditor(EMP_DOC_VIEW+".xmi");
		try{
		md.addMappingClassColumns(EMPLOYEE, EMPLOYEE_COLUMNS);
		//attributes must be in the same order!
		md.addMappingClassColumns(SUPERVISOR, SUPERVISOR_COLUMNS1);
		md.copyAttribute(EMPLOYEE, SUPERVISOR, "State");
		md.addMappingClassColumns(SUPERVISOR, SUPERVISOR_COLUMNS2);
		} catch (Exception e){
			
		}
		
		//employee - transf. diagram
		ModelEditor me = new ModelEditor(EMP_DOC_VIEW+".xmi");
		me.showMappingTransformation(EMPLOYEE);
		ModelExplorerView mew = TeiidPerspective.getInstance().getModelExplorerView();
		try{
		mew.addTransformationSource(PROJECT_NAME, EMPLOYEES_VIEW+".xmi", EMP_TABLE);
		new ModelEditor(EMP_DOC_VIEW+".xmi").save();
		} catch (Exception e){
			
		}
		
		//reconciller
		Reconciler rec = me.openReconciler();
		try{
		rec.bindAttributes("MiddleInitial : string", "MiddleName");
		rec.bindAttributes("Phone : string", "HomePhone");
		rec.bindAttributes("mgrID : biginteger", "Manager");
		rec.clearRemainingUnmatchedSymbols();
		rec.resolveTypes(ExpressionBuilder.KEEP_VIRTUAL_TARGET);
		rec.close();
		new ModelEditor(EMP_DOC_VIEW+".xmi").save();
		} catch (Exception e){
			
		}
		
		try{
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Show Parent Diagram").click();
		me.showMappingTransformation(SUPERVISOR);
		mew.addTransformationSource(PROJECT_NAME, EMPLOYEES_VIEW+".xmi", EMP_TABLE);
		InputSetEditor ise = me.openInputSetEditor(true);
		ise.createNewInputParam(EMPLOYEE, "mgrID : positiveInteger");
		ise.close();//and save
		me.save();
} catch (Exception e){
			
		}
		
		try{
		md = new MappingDiagramEditor(EMP_DOC_VIEW+".xmi");
		RecursionEditor recEd = md.clickOnRecursiveButton(SUPERVISOR);
		recEd.enableRecursion();
		//recEd.limitRecursion(3);
		recEd.close();
} catch (Exception e){
			
		}
		
		md.showTransformation();
		md.save();
		
		try{
		//reconciller
		rec = me.openReconciler();
		rec.bindAttributes("MiddleInitial : string", "MiddleName");
		rec.bindAttributes("Phone : string", "HomePhone");
		rec.bindAttributes("mgrID : biginteger", "Manager");
		rec.clearRemainingUnmatchedSymbols();
		rec.resolveTypes(ExpressionBuilder.KEEP_VIRTUAL_TARGET);
		rec.close();
} catch (Exception e){
			
		}
		
		try{
		//criteria builder
		CriteriaBuilder cb = me.criteriaBuilder();
		cb.selectLeftAttribute("INPUTS", "mgrID");
		cb.selectRightAttribute("Employees.EmpTable", "EmpId");
		cb.selectOperator(OperatorType.EQUALS);
		cb.apply();
		cb.close();
		me.save();
} catch (Exception e){
			
		}
		
		try{
		//create new view model
		CreateMetadataModel cmm = new CreateMetadataModel();
		cmm.setLocation(PROJECT_NAME);
		cmm.setName(EMP_V);
		cmm.setClass(ModelClass.RELATIONAL);
		cmm.setType(ModelType.VIEW);
		cmm.setModelBuilder(ModelBuilder.TRANSFORM_EXISTING);
		cmm.execute(true, PROJECT_NAME, EMPLOYEES_VIEW+".xmi");
} catch (Exception e){
			
		}
		
		try{
		//create data source
		mew.createDataSource(ConnectionSource.USE_MODEL_CONNECTION_INFO, PROJECT_NAME, EMPDATA_SOURCE +"Source.xmi");
} catch (Exception e){
			
		}
		
		try{
		//create new vdb
		CreateVDB createVDB = new CreateVDB();
		createVDB.setFolder(PROJECT_NAME);
		createVDB.setName(VDB);
		createVDB.execute(true);
} catch (Exception e){
			
		}

		try{
		VDBEditor editor = VDBEditor.getInstance(VDB + ".vdb");
		editor.show();
		editor.addModel(PROJECT_NAME, EMPDATA_SOURCE + "Source");
		editor.addModel(PROJECT_NAME, EMP_DOC_VIEW);//adds also EMPLOYEES_VIEW, EMPLOYEES_SCHEMA_XSD
		editor.addModel(PROJECT_NAME, EMP_V);
		editor.save();
} catch (Exception e){
			
		}
		
		try{
		//deploy, execute
		VDB vdb = new ModelExplorer().getModelProject(PROJECT_NAME).getVDB(VDB + ".vdb");
		vdb.deployVDB();
		vdb.executeVDB(true);
} catch (Exception e){
			
		}
		
		try{
		SQLScrapbookEditor sqlEd = new SQLScrapbookEditor("SQL Scrapbook0");
		sqlEd.show();

		// TESTSQL_1
		sqlEd.setText(SQL1);
		sqlEd.executeAll(true);

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		SQLResult result = DatabaseDevelopmentPerspective.getInstance()
				.getSqlResultsView().getByOperation(SQL1);
		assertEquals(SQLResult.STATUS_SUCCEEDED, result.getStatus());
		sqlEd.close();
} catch (Exception e){
			
		}
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
	
	//@Test
	public void test00(){
		openExisting();
	}
	
	
	private void openExisting(){
		new ShellMenu("File", "Import...").select();
		new DefaultShell("Import");
		new DefaultTreeItem(0, "General", "Existing Projects into Workspace").select();
		new PushButton("Next >").click();
		String path = "/home/lfabriko/Work/EXAMPLES/teiiddes/Recursive-clean/Recursive";//Recursive-clean/Recursive
		new DefaultCombo(0).setText(path);
		new SWTWorkbenchBot().activeShell().pressShortcut(Keystrokes.TAB);
		bot.sleep(2000);
		new PushButton("Finish").click();
	}
	
}
