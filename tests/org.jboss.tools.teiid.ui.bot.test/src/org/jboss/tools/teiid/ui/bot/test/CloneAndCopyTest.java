package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed;
import org.jboss.tools.common.reddeer.condition.IssueIsClosed.Jira;
import org.jboss.tools.teiid.reddeer.ModelBuilder;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.SaveAsDialog;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author skaleta
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class CloneAndCopyTest {
	private static final String PROJECT_NAME = "CloneAndCopyProject";
	
	private static final String DIR_ORIGINAL_PATH = PROJECT_NAME + "/original";
	private static final String DIR_COPY_NAME = "Copy";
	private static final String DIR_COPY_PATH = PROJECT_NAME+ "/" + DIR_COPY_NAME;
	private static final String DIR_SAVEAS_NAME = "SaveAs";
	private static final String DIR_SAVEAS_PATH = PROJECT_NAME + "/" + DIR_SAVEAS_NAME;
	
	private static final String ORIGINAL_RELATIONAL_SOURCE_MODEL_PATH = DIR_ORIGINAL_PATH + "/RelationalSourceModel.xmi";
	private static final String RELATIONAL_SOURCE_MODEL_COPY_NAME = "RelationalSourceModelCopy";
	private static final String RELATIONAL_SOURCE_MODEL_SAVE_NAME = "RelationalSourceModelSaveAs";
	
	private static final String ORIGINAL_RELATIONAL_VIEW_MODEL_PATH = DIR_ORIGINAL_PATH + "/RelationalViewModel.xmi";
	private static final String RELATIONAL_VIEW_MODEL_COPY_NAME = "RelationalViewModelCopy";
	private static final String RELATIONAL_VIEW_MODEL_SAVE_NAME = "RelationalViewModelSaveAs";
	
	private static final String ORIGINAL_XML_VIEW_MODEL_PATH = DIR_ORIGINAL_PATH + "/xml/XmlViewModel.xmi";
	private static final String XML_VIEW_MODEL_COPY_NAME = "XmlViewModelCopy";
	private static final String XML_VIEW_MODEL_SAVE_NAME = "XmlViewModelSaveAs";

	private static final String ORIGINAL_XML_SCHEMA_MODEL_PATH = DIR_ORIGINAL_PATH + "/XmlSchemaModel.xsd";
	private static final String XML_SCHEMA_MODEL_COPY_NAME = "XmlSchemaModelCopy";
	private static final String XML_SCHEMA_MODEL_SAVE_NAME = "XmlSchemaModelSaveAs";
	
	private static final String ORIGINAL_WEBSERVICE_VIEW_MODEL_PATH = DIR_ORIGINAL_PATH + "/web/WebServiceViewModel.xmi";
	private static final String WEBSERVICE_VIEW_MODEL_COPY_NAME = "WebServiceViewModelCopy";
	private static final String WEBSERVICE_VIEW_MODEL_SAVE_NAME = "WebServiceViewModelSaveAs";
	
	private static Project project;
	
	@BeforeClass
	public static void importProject() {
		new WorkbenchShell().maximize();
		
		TeiidBot teiidBot = new TeiidBot();
		new ImportManager().importProject(teiidBot.toAbsolutePath("resources/projects/" + PROJECT_NAME));
	
		project = teiidBot.modelExplorer().getProject(PROJECT_NAME);
	}
	
	@Before
	public void beforeTest(){
		project.select();
		project.refresh();	
	}
	
	@After
	public void cleanUpAfterTest(){
		// validation errors from one test occurs in another tests too -> delete model after test.
		for (ProjectItem item : project.getChild(DIR_COPY_NAME).getChildren()){
			item.delete();
		}
		for (ProjectItem item : project.getChild(DIR_SAVEAS_NAME).getChildren()){
			item.delete();
		}
	}
	
	@Test
	public void TestCopyRelationalSourceModel(){
		performCopyModel(ORIGINAL_RELATIONAL_SOURCE_MODEL_PATH,
						 RELATIONAL_SOURCE_MODEL_COPY_NAME, 
						 ModelClass.RELATIONAL, 
						 ModelType.SOURCE);
		checkModel(DIR_COPY_NAME, RELATIONAL_SOURCE_MODEL_COPY_NAME + ".xmi");
		
	}
	
	@Test
	@Jira("TEIIDDES-2641")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void TestCopyRelationalViewModel(){
		performCopyModel(ORIGINAL_RELATIONAL_VIEW_MODEL_PATH,
				         RELATIONAL_VIEW_MODEL_COPY_NAME, 
						 ModelClass.RELATIONAL, 
						 ModelType.VIEW);
		checkModel(DIR_COPY_NAME, RELATIONAL_VIEW_MODEL_COPY_NAME + ".xmi");	
	}
	
	@Test
	@Jira("TEIIDDES-2641")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void TestCopyXmlViewModel(){
		performCopyModel(ORIGINAL_XML_VIEW_MODEL_PATH,
				         XML_VIEW_MODEL_COPY_NAME, 
						 ModelClass.XML, 
						 ModelType.VIEW);
		checkModel(DIR_COPY_NAME, XML_VIEW_MODEL_COPY_NAME + ".xmi");
	}
	
	@Test
	public void TestCopyXmlSchemaModel(){
		performCopyModel(ORIGINAL_XML_SCHEMA_MODEL_PATH,
				         XML_SCHEMA_MODEL_COPY_NAME, 
						 ModelClass.XSD, 
						 ModelType.DATATYPE);
		checkModel(DIR_COPY_NAME, XML_SCHEMA_MODEL_COPY_NAME + ".xsd");
	}
	
	@Test
	@Jira("TEIIDDES-2641")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void TestCopyWebServiceViewModel(){
		performCopyModel(ORIGINAL_WEBSERVICE_VIEW_MODEL_PATH,
				         WEBSERVICE_VIEW_MODEL_COPY_NAME, 
						 ModelClass.WEBSERVICE, 
						 ModelType.VIEW);
		checkModel(DIR_COPY_NAME, WEBSERVICE_VIEW_MODEL_COPY_NAME + ".xmi");
	}
	
	@Test
	public void TestSaveAsRelationalSourceModel(){
		performSaveAsModel(ORIGINAL_RELATIONAL_SOURCE_MODEL_PATH, 
						   RELATIONAL_SOURCE_MODEL_SAVE_NAME,
						   Boolean.FALSE);
		checkModel(DIR_SAVEAS_NAME, RELATIONAL_SOURCE_MODEL_SAVE_NAME + ".xmi");
	}
	
	@Test
	@Jira("TEIIDDES-2641")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void TestSaveAsRelationalViewModel(){
		performSaveAsModel(ORIGINAL_RELATIONAL_VIEW_MODEL_PATH, 
				           RELATIONAL_VIEW_MODEL_SAVE_NAME,
				           Boolean.FALSE);
		checkModel(DIR_SAVEAS_NAME, RELATIONAL_VIEW_MODEL_SAVE_NAME + ".xmi");
	}
	
	@Test
	public void TestSaveAsXmlViewModel(){
		performSaveAsModel(ORIGINAL_XML_VIEW_MODEL_PATH, 
				           XML_VIEW_MODEL_SAVE_NAME,
						   Boolean.FALSE);
		checkModel(DIR_SAVEAS_NAME, XML_VIEW_MODEL_SAVE_NAME + ".xmi");	
	}
	
	@Test
	public void TestSaveAsXmlSchemaModel(){
		performSaveAsModel(ORIGINAL_XML_SCHEMA_MODEL_PATH, 
				           XML_SCHEMA_MODEL_SAVE_NAME,
						   Boolean.TRUE);
		checkModel(DIR_SAVEAS_NAME, XML_SCHEMA_MODEL_SAVE_NAME + ".xsd");			
	}	
	
	@Test
	@Jira("TEIIDDES-2641")
	@RunIf(conditionClass = IssueIsClosed.class)
	public void TestSaveAsWebServiceViewModel(){
		performSaveAsModel(ORIGINAL_WEBSERVICE_VIEW_MODEL_PATH, 
				           WEBSERVICE_VIEW_MODEL_SAVE_NAME,
				           Boolean.FALSE);
		checkModel(DIR_SAVEAS_NAME, WEBSERVICE_VIEW_MODEL_SAVE_NAME + ".xmi");		
	}	
	
	@Test
	public void TestCloneProject(){
		new ShellMenu("Project","Clone Project").select();
		new DefaultShell("Clone Model Project");
		new LabeledText("Project name:").setText("ClonedProject");
		new FinishButton().click();
		
		ModelExplorer modelExplorer = new TeiidBot().modelExplorer();
		assertTrue(modelExplorer.containsProject(PROJECT_NAME));
		
		Project clonedProject = modelExplorer.getProject("ClonedProject");
		clonedProject.refresh();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		
		String[] modelPath = ORIGINAL_RELATIONAL_SOURCE_MODEL_PATH.split("/");
		String[] modelPathWithoutProject = Arrays.copyOfRange(modelPath, 1, modelPath.length);
		assertTrue(clonedProject.containsItem(modelPathWithoutProject));
		
		modelPath = ORIGINAL_RELATIONAL_VIEW_MODEL_PATH.split("/");
		modelPathWithoutProject = Arrays.copyOfRange(modelPath, 1, modelPath.length);
		assertTrue(clonedProject.containsItem(modelPathWithoutProject));
		
		modelPath = ORIGINAL_XML_VIEW_MODEL_PATH.split("/");
		modelPathWithoutProject = Arrays.copyOfRange(modelPath, 1, modelPath.length);
		assertTrue(clonedProject.containsItem(modelPathWithoutProject));
		
		modelPath = ORIGINAL_XML_SCHEMA_MODEL_PATH.split("/");
		modelPathWithoutProject = Arrays.copyOfRange(modelPath, 1, modelPath.length);
		assertTrue(clonedProject.containsItem(modelPathWithoutProject));
		
		modelPath = ORIGINAL_WEBSERVICE_VIEW_MODEL_PATH.split("/");
		modelPathWithoutProject = Arrays.copyOfRange(modelPath, 1, modelPath.length);
		assertTrue(clonedProject.containsItem(modelPathWithoutProject));
	}
	
	/**
	 * Performs copy model action. 
	 * (new model location is set to "<PROJECT_NAME>/Copy" folder)
	 * 
	 * @param String originalModelPath - <PROJECT_NAME>/<FOLDER(S)>/<NAME>.<EXT> 
	 * 		- where the original model is located and its name
	 * @param newModelName - name of the new model
	 * @param newModelClass - model class of the new model
	 * @param newModelType - model type of the new model
	 */
	private void performCopyModel(String originalModelPath, 
								  String newModelName,
								  ModelClass newModelClass,
								  ModelType newModelType){
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.activate();
		modelWizard.setLocation(DIR_COPY_PATH.split("/"));
		modelWizard.setModelName(newModelName);
		modelWizard.selectModelClass(newModelClass);
		modelWizard.selectModelType(newModelType);
		modelWizard.selectModelBuilder(ModelBuilder.COPY_EXISTING);
		modelWizard.next();
		modelWizard.setExistingModel(originalModelPath.split("/"));
		modelWizard.finish();
		ShellMenu saveAllMenu = new ShellMenu("File","Save All");
		if (saveAllMenu.isEnabled()){
			saveAllMenu.select();
		}
	}
	
	/**
	 * Performs save model as action. 
	 * (new model location is set to "<PROJECT_NAME>/SaveAs" folder)
	 * 
	 * @param String originalModelPath - <PROJECT_NAME>/<FOLDER(S)>/<NAME>.<EXT> 
	 * 		- where the original model is located and its name
	 * @param newModelName - name of the new model
	 * @param isSchemaModel - whether original model is XML Schema Model or not (for dialog purposes).
	 */
	private void performSaveAsModel(String originalModelPath, 
									String newModelName, 
									boolean isSchemaModel){
		ModelExplorerManager manager = new ModelExplorerManager();
		manager.getModelExplorerView().open(originalModelPath.split("/"));
		SaveAsDialog saveAsDialog = new SaveAsDialog(isSchemaModel);
		saveAsDialog.open();
		saveAsDialog.activate();
		saveAsDialog.setLocation(DIR_SAVEAS_PATH.split("/"));
		saveAsDialog.setModelName(newModelName);
		saveAsDialog.ok();
	}
	
	/**
	 * Checks whether:
	 * - model exists
	 * - model has validation errors
	 * - ...
	 * @param path - path to new model (excluding project name)
	 */
	private void checkModel(String... path){
		project.refresh();
		AbstractWait.sleep(TimePeriod.SHORT);
		assertTrue(project.containsItem(path));
		assertTrue("There are validation errors", new ProblemsView().getProblems(ProblemType.ERROR).isEmpty());
		// TODO check composition?	
	}
}
