package org.jboss.tools.teiid.ui.bot.test;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.connection.TeiidFileHelper;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.InputSetEditorDialog;
import org.jboss.tools.teiid.reddeer.editor.RecursionEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.editor.XmlModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author lfabriko, mmakovy, skaleta
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING)
public class XmlRecursiveTest {
	private static final String PROJECT_NAME = "XmlRecursiveProject";
	private static final String VIEW_MODEL = "EmpDoc.xmi";
	private static final String EMPLOYEE_MC = "Employee";
	private static final String SUPERVISOR_MC = "Supervisor";
	private static final String VDB_NAME = "RecursiveVDB";
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	private static ModelExplorer modelExplorer;

	@BeforeClass
	public static void importProject() throws IOException {
		new WorkbenchShell().maximize();
		
		Path source = Paths.get(new File("resources/flat/EmpData.csv").getAbsolutePath());
		Path target = Paths.get(teiidServer.getServerConfig().getServerBase().getHome() + "/standalone/data/EmpData.csv");
		Files.copy(source, target, REPLACE_EXISTING);
		new ServersViewExt().refreshServer(teiidServer.getName());
		
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject("resources/projects/" + PROJECT_NAME);
		modelExplorer.getProject(PROJECT_NAME).refresh();
	}

	@Test
	public void test() throws Exception {
		// 1. Create Virtual Document XML model
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.setLocation(PROJECT_NAME)
				.setModelName(VIEW_MODEL.substring(0,6))
				.selectModelClass(ModelClass.XML)
				.selectModelType(ModelType.VIEW)
				.selectModelBuilder(ModelBuilder.BUILD_FROM_XML_SCHEMA);
		modelWizard.next();
		modelWizard.selectXMLSchemaFile(PROJECT_NAME, "EmployeesSchema.xsd")
				.addElement("SimpleEmployees");
		modelWizard.finish();

		// 2. Create Employees Mapping Transformation Model
		modelExplorer.openModelEditor(PROJECT_NAME, VIEW_MODEL);
		XmlModelEditor editor = new XmlModelEditor(VIEW_MODEL);
		
		editor.openDocument("SimpleEmployeesDocument");
		
		assertTrue(editor.ListAttributesNames(EMPLOYEE_MC).size() == 8);
		assertTrue(editor.ListAttributesNames(SUPERVISOR_MC).size() == 0);

		editor.addAttribute(EMPLOYEE_MC, "mgrID : positiveInteger");
		
		editor.copyAttribute(EMPLOYEE_MC, SUPERVISOR_MC, "LastName");
		editor.copyAttribute(EMPLOYEE_MC, SUPERVISOR_MC, "FirstName");
		editor.copyAttribute(EMPLOYEE_MC, SUPERVISOR_MC, "MiddleInitial");
		editor.copyAttribute(EMPLOYEE_MC, SUPERVISOR_MC, "Street");
		editor.copyAttribute(EMPLOYEE_MC, SUPERVISOR_MC, "City");
		editor.copyAttribute(EMPLOYEE_MC, SUPERVISOR_MC, "State");
		editor.copyAttribute(EMPLOYEE_MC, SUPERVISOR_MC, "EmpId");
		editor.copyAttribute(EMPLOYEE_MC, SUPERVISOR_MC, "Phone");
		editor.copyAttribute(EMPLOYEE_MC, SUPERVISOR_MC, "mgrID");
		
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		
		List<String> supAttrs = editor.ListAttributesNames(SUPERVISOR_MC);	
		List<String> empAttrs = editor.ListAttributesNames(EMPLOYEE_MC);
		assertTrue(supAttrs.size() == 9);
		assertTrue(empAttrs.size() == 9);
		for(String empAttr : empAttrs){
			assertTrue(supAttrs.contains(empAttr));
		}	

		// 2.5. Employee definition
		editor.openMappingClass(EMPLOYEE_MC);
		
		TransformationEditor empTransfEditor = editor.openTransformationEditor();
		empTransfEditor.insertAndValidateSql("SELECT Employees.EmpTable.LastName, Employees.EmpTable.FirstName, Employees.EmpTable.MiddleName "
				+ "AS MiddleInitial, Employees.EmpTable.Street, Employees.EmpTable.City, Employees.EmpTable.State, convert(Employees.EmpTable.EmpId, "
				+ "biginteger) AS EmpId,Employees.EmpTable.HomePhone AS Phone, convert(Employees.EmpTable.Manager,biginteger) AS mgrID FROM "
				+ "Employees.EmpTable");
		empTransfEditor.close();
		
		editor.save();
		editor.returnToMappingClassOverview();
		
		// 2.6. Supervisor definition
		editor.openMappingClass(SUPERVISOR_MC);
		
		InputSetEditorDialog inputSetEditor = editor.openInputSetEditor();
		inputSetEditor.addNewInputParam("Employee", "mgrID : positiveInteger");
		inputSetEditor.finish();
		
		TransformationEditor supTransfEditor = editor.openTransformationEditor();
		supTransfEditor.insertAndValidateSql("SELECT Employees.EmpTable.LastName, Employees.EmpTable.FirstName, "
	    		+ "Employees.EmpTable.MiddleName AS MiddleInitial, Employees.EmpTable.Street, "
				+ "Employees.EmpTable.City, Employees.EmpTable.State, convert(Employees.EmpTable.EmpId, biginteger) AS EmpId, Employees.EmpTable.HomePhone AS "
				+ "Phone, convert(Employees.EmpTable.Manager, biginteger) AS mgrID FROM Employees.EmpTable WHERE INPUTS.mgrID = Employees.EmpTable.EmpId");
		supTransfEditor.close();
		
		AbstractWait.sleep(TimePeriod.SHORT);		
		editor.save();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		assertTrue("Validation errors!", new ProblemsView().getProblems(ProblemType.ERROR).isEmpty());

		// 3. Create new VDB and check document without recursion		
		VdbWizard vdbWizard = new VdbWizard();
		vdbWizard.open();
		vdbWizard.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, VIEW_MODEL);
		vdbWizard.finish();
		
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);

		TeiidJDBCHelper jdbcHelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
		assertTrue(jdbcHelper.isQuerySuccessful("SELECT * FROM Employees.EmpTable",true));
					
        String output = jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM EmpDoc.SimpleEmployeesDocument");          
		String oneOfexpectedEmployee = "<Employee><Name><LastName>Kisselmeyer</LastName><FirstName>Abbiegale</FirstName>"
				+ "<MiddleInitial>Tikvica</MiddleInitial></Name><Address><Street>123 State St.</Street><City>New York</City>"
				+ "<State>NY</State></Address><EmpId>9000059</EmpId><Phone>670-270-7947</Phone></Employee>";
		assertTrue(output.contains(oneOfexpectedEmployee));
		
		// 4. Enable recursion and test it
		String recursionQuery = "SELECT * FROM EmpDoc.SimpleEmployeesDocument WHERE SimpleEmployees.Employee.EmpId LIKE '90001%'"; 
		TeiidFileHelper fileHelper = new TeiidFileHelper();
		
		// 4.1. Test recursion without limit
		editor.show();
		RecursionEditor recursionEditor = editor.openRecursiveEditor();		
		recursionEditor.toggleRecursion(true);
		recursionEditor.close();
		editor.save();
		
		VDBEditor.getInstance(VDB_NAME).synchronizeAll();
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		
 		output = jdbcHelper.executeQueryWithXmlStringResult(recursionQuery);
		String expectedOutput = fileHelper.getXmlExpectedResult("forXmlRecursiveTest",false);
		assertEquals(output, expectedOutput);	
		
		// 4.2. Test recursion with limit and actions when exceeded
		editor.show();
		recursionEditor = editor.openRecursiveEditor();		
		recursionEditor.limitRecursion(3);
		recursionEditor.setActionWhenLimitExceeded(RecursionEditor.LimitAction.THROW);
		recursionEditor.close();
		editor.save();
		VDBEditor.getInstance(VDB_NAME).synchronizeAll();
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		assertFalse(jdbcHelper.isQuerySuccessful(recursionQuery,true));
		
		editor.show();
		recursionEditor = editor.openRecursiveEditor();		
		recursionEditor.setActionWhenLimitExceeded(RecursionEditor.LimitAction.DISCARD);
		recursionEditor.close();
		editor.save();
		VDBEditor.getInstance(VDB_NAME).synchronizeAll();
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		output = jdbcHelper.executeQueryWithXmlStringResult(recursionQuery);
		expectedOutput = fileHelper.getXmlExpectedResult("forXmlRecursiveTest2",false);
		assertEquals(output, expectedOutput);
		
		editor.show();
		recursionEditor = editor.openRecursiveEditor();		
		recursionEditor.setActionWhenLimitExceeded(RecursionEditor.LimitAction.RECORD);
		recursionEditor.close();
		editor.save();
		VDBEditor.getInstance(VDB_NAME).synchronizeAll();
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		output = jdbcHelper.executeQueryWithXmlStringResult(recursionQuery);
		expectedOutput = fileHelper.getXmlExpectedResult("forXmlRecursiveTest2",false);
		assertEquals(output, expectedOutput);	
		// TODO what exactly is record here? - no mention in documentation
	}

}
