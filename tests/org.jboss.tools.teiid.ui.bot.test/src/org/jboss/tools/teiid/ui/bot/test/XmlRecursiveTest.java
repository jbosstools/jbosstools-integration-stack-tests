package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.InputSetEditorDialog;
import org.jboss.tools.teiid.reddeer.editor.RecursionEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.editor.XmlModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ProblemsViewEx;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author lfabriko, mmakovy, skaleta
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING)
public class XmlRecursiveTest {
	private static final String PROJECT_NAME = "XmlRecursiveProject";
	private static final String VIEW_MODEL = "EmpDoc.xmi";
	private static final String EMPLOYEE_MC = "Employee";
	private static final String SUPERVISOR_MC = "Supervisor";
	private static final String VDB_NAME = "RecursiveVDB";
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	private ModelExplorer modelExplorer;
	private ResourceFileHelper fileHelper;
	private TeiidJDBCHelper jdbcHelper;

	@Before
	public void setUp() throws Exception {
		new WorkbenchShell().maximize();
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject(PROJECT_NAME);
		modelExplorer.refreshProject(PROJECT_NAME);
		fileHelper = new ResourceFileHelper();
		jdbcHelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
		
		fileHelper.copyFileToServer(new File("resources/flat/EmpData.csv").getAbsolutePath(), 
				teiidServer.getServerConfig().getServer().getHome() + "/standalone/data/EmpData.csv");
		new ServersViewExt().refreshServer(teiidServer.getName());
	}
	
	@After
	public void cleanUp() {
		modelExplorer.deleteAllProjectsSafely();
	}

	@Test
	public void test() throws Exception {
		// 1. Create Virtual Document XML model
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(VIEW_MODEL.substring(0,6))
				.selectModelClass(MetadataModelWizard.ModelClass.XML)
				.selectModelType(MetadataModelWizard.ModelType.VIEW)
				.selectModelBuilder(MetadataModelWizard.ModelBuilder.BUILD_FROM_XML_SCHEMA)
				.nextPage()
				.selectXMLSchemaFile(PROJECT_NAME, "EmployeesSchema.xsd")
				.addElement("SimpleEmployees")
				.finish();

		// 2. Create Employees Mapping Transformation Model
		modelExplorer.openModelEditor(PROJECT_NAME, VIEW_MODEL);
		XmlModelEditor editor = new XmlModelEditor(VIEW_MODEL);
		
		editor.openDocument("SimpleEmployeesDocument");
		
		assertTrue(editor.listMappingClassAttributes(EMPLOYEE_MC).size() == 8);
		assertTrue(editor.listMappingClassAttributes(SUPERVISOR_MC).size() == 0);

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
		
		List<String> supAttrs = editor.listMappingClassAttributes(SUPERVISOR_MC);	
		List<String> empAttrs = editor.listMappingClassAttributes(EMPLOYEE_MC);
		assertTrue(supAttrs.size() == 9);
		assertTrue(empAttrs.size() == 9);
		for(String empAttr : empAttrs){
			assertTrue(supAttrs.contains(empAttr));
		}	

		// 2.5. Employee definition
		editor.openMappingClass(EMPLOYEE_MC);
		
		TransformationEditor empTransfEditor = editor.openTransformationEditor();
		empTransfEditor.insertAndValidateSql(fileHelper.getSql("XmlRecursiveTest/Employee"));
		empTransfEditor.close();
		
		editor.save();
		editor.returnToParentDiagram();
		
		// 2.6. Supervisor definition
		editor.openMappingClass(SUPERVISOR_MC);
		
		InputSetEditorDialog inputSetEditor = editor.openInputSetEditor();
		inputSetEditor.addNewInputParam("Employee", "mgrID : positiveInteger");
		inputSetEditor.finish();
		
		TransformationEditor supTransfEditor = editor.openTransformationEditor();
		supTransfEditor.insertAndValidateSql(fileHelper.getSql("XmlRecursiveTest/Supervisor"));
		supTransfEditor.close();
	
		editor.save();

		ProblemsViewEx.checkErrors();

		// 3. Create new VDB and check document without recursion		
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, VIEW_MODEL)
				.finish();
		
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);

		assertTrue(jdbcHelper.isQuerySuccessful("SELECT * FROM Employees.EmpTable",true));
					
        String output = jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM EmpDoc.SimpleEmployeesDocument");          
		String oneOfExpectedEmployee = "<Employee><Name><LastName>Kisselmeyer</LastName><FirstName>Abbiegale</FirstName>"
				+ "<MiddleInitial>Tikvica</MiddleInitial></Name><Address><Street>123 State St.</Street><City>New York</City>"
				+ "<State>NY</State></Address><EmpId>9000059</EmpId><Phone>670-270-7947</Phone></Employee>";
		assertTrue(output.contains(oneOfExpectedEmployee));
		
		// 4. Enable recursion and test it
		String recursionQuery = "SELECT * FROM EmpDoc.SimpleEmployeesDocument WHERE SimpleEmployees.Employee.EmpId LIKE '90001%'"; 
		
		// 4.1. Test recursion without limit
		editor.activate();
		RecursionEditor recursionEditor = editor.openRecursiveEditor();		
		recursionEditor.toggleRecursion(true);
		recursionEditor.close();
		editor.save();
		
		VdbEditor.getInstance(VDB_NAME).synchronizeAll();
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		
 		output = jdbcHelper.executeQueryWithXmlStringResult(recursionQuery);
		String expectedOutput = fileHelper.getXml("XmlRecursiveTest/RecursionWithoutLimit");
		assertEquals(output, expectedOutput);	
		
		// 4.2. Test recursion with limit and actions when exceeded
		editor.activate();
		recursionEditor = editor.openRecursiveEditor();		
		recursionEditor.limitRecursion(3);
		recursionEditor.setActionWhenLimitExceeded(RecursionEditor.LimitAction.THROW);
		recursionEditor.close();
		editor.save();
		VdbEditor.getInstance(VDB_NAME).synchronizeAll();
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		assertFalse(jdbcHelper.isQuerySuccessful(recursionQuery,true));
		
		editor.activate();
		recursionEditor = editor.openRecursiveEditor();		
		recursionEditor.setActionWhenLimitExceeded(RecursionEditor.LimitAction.DISCARD);
		recursionEditor.close();
		editor.save();
		VdbEditor.getInstance(VDB_NAME).synchronizeAll();
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		output = jdbcHelper.executeQueryWithXmlStringResult(recursionQuery);
		expectedOutput = fileHelper.getXml("XmlRecursiveTest/RecursionWithLimit");
		assertEquals(output, expectedOutput);
		
		editor.activate();
		recursionEditor = editor.openRecursiveEditor();		
		recursionEditor.setActionWhenLimitExceeded(RecursionEditor.LimitAction.RECORD);
		recursionEditor.close();
		editor.save();
		VdbEditor.getInstance(VDB_NAME).synchronizeAll();
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		output = jdbcHelper.executeQueryWithXmlStringResult(recursionQuery);
		expectedOutput = fileHelper.getXml("XmlRecursiveTest/RecursionWithLimit");
		assertEquals(output, expectedOutput);	
		// TODO what exactly is record here? - no mention in documentation
	}

}
