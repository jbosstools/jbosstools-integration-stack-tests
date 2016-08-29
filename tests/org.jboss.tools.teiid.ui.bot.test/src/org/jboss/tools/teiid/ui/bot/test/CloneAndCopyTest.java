package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ProblemsViewEx;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author skaleta
 * 
 * Tested features:
 * - copy model action
 * - save as model action
 * - clone project action
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class CloneAndCopyTest {
	private static final String PROJECT_NAME = "CloneAndCopyProject";
	
	private static final String RELATIONAL_SOURCE_MODEL = PROJECT_NAME + "/original/RelationalSourceModel.xmi";	
	private static final String RELATIONAL_VIEW_MODEL = PROJECT_NAME + "/original/RelationalViewModel.xmi";
	private static final String XML_VIEW_MODEL = PROJECT_NAME + "/original/xml/XmlViewModel.xmi";
	private static final String XML_SCHEMA_MODEL = PROJECT_NAME + "/original/XmlSchemaModel.xsd";	
	private static final String WEBSERVICE_VIEW_MODEL = PROJECT_NAME + "/original/web/WebServiceViewModel.xmi";
	
	private ModelExplorer modelExplorer;
	
	@BeforeClass
	public static void before(){
		new WorkbenchShell().maximize();
	}
	
	@Before
	public void importProject(){
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject(PROJECT_NAME);
		modelExplorer.getProject(PROJECT_NAME).refresh();
	}
	
	@After
	public void cleanUp(){
		modelExplorer.deleteAllProjectsSafely();
	}
	
	@Test
	public void testCopyRelationalSourceModel(){
		String newModelName = "RelationalSourceModelCopy";
		modelExplorer.copyModel(RELATIONAL_SOURCE_MODEL, newModelName, PROJECT_NAME + "/Copy", ModelClass.RELATIONAL, ModelType.SOURCE);
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("Copy", newModelName + ".xmi"));
		new ProblemsViewEx().checkErrors();
	}
	
	@Test
	public void testCopyRelationalViewModel(){
		String newModelName = "RelationalViewModelCopy";
		modelExplorer.copyModel(RELATIONAL_VIEW_MODEL, newModelName, PROJECT_NAME + "/Copy", ModelClass.RELATIONAL, ModelType.VIEW);
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("Copy", newModelName + ".xmi"));
		new ProblemsViewEx().checkErrors();	
	}
	
	@Test
	public void testCopyXmlViewModel(){
		String newModelName = "XmlViewModelCopy";
		modelExplorer.copyModel(XML_VIEW_MODEL, newModelName, PROJECT_NAME + "/Copy", ModelClass.XML, ModelType.VIEW);
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("Copy", newModelName + ".xmi"));
		new ProblemsViewEx().checkErrors();	
	}
	
	@Test
	public void testCopyXmlSchemaModel(){
		String newModelName = "XmlSchemaModelCopy";
		modelExplorer.copyModel(XML_SCHEMA_MODEL, newModelName, PROJECT_NAME + "/Copy", ModelClass.XSD, ModelType.DATATYPE);
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("Copy", newModelName + ".xsd"));
		new ProblemsViewEx().checkErrors();
	}
	
	@Test
	public void testCopyWebServiceViewModel(){
		String newModelName = "WebServiceViewModelCopy";
		modelExplorer.copyModel(WEBSERVICE_VIEW_MODEL, newModelName, PROJECT_NAME + "/Copy", ModelClass.WEBSERVICE, ModelType.VIEW);
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("Copy", newModelName + ".xmi"));
		new ProblemsViewEx().checkErrors();	
	}
	
	@Test
	public void testSaveAsRelationalSourceModel(){
		String newModelName = "RelationalSourceModelSaveAs";
		modelExplorer.saveModelAs(RELATIONAL_SOURCE_MODEL, newModelName, PROJECT_NAME + "/SaveAs", Boolean.FALSE);
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("SaveAs", newModelName + ".xmi"));
		new ProblemsViewEx().checkErrors();			
	}
	
	@Test
	public void testSaveAsRelationalViewModel(){
		String newModelName = "RelationalViewModelSaveAs";
		modelExplorer.saveModelAs(RELATIONAL_VIEW_MODEL, newModelName, PROJECT_NAME + "/SaveAs", Boolean.FALSE);
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("SaveAs", newModelName + ".xmi"));
		new ProblemsViewEx().checkErrors();			
	}
	
	@Test
	public void testSaveAsXmlViewModel(){
		String newModelName = "XmlViewModelSaveAs";
		modelExplorer.saveModelAs(XML_VIEW_MODEL, newModelName, PROJECT_NAME + "/SaveAs", Boolean.FALSE);
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("SaveAs", newModelName + ".xmi"));
		new ProblemsViewEx().checkErrors();		
	}
	
	@Test
	public void testSaveAsXmlSchemaModel(){
		String newModelName = "XmlSchemaModelSaveAs";
		modelExplorer.saveModelAs(XML_SCHEMA_MODEL, newModelName, PROJECT_NAME + "/SaveAs", Boolean.TRUE);
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("SaveAs", newModelName + ".xsd"));
		new ProblemsViewEx().checkErrors();				
	}	
	
	@Test
	public void testSaveAsWebServiceViewModel(){
		String newModelName = "WebServiceViewModelSaveAs";
		modelExplorer.saveModelAs(WEBSERVICE_VIEW_MODEL, newModelName, PROJECT_NAME + "/SaveAs", Boolean.FALSE);
		assertTrue(modelExplorer.getProject(PROJECT_NAME).containsItem("SaveAs", newModelName + ".xmi"));
		new ProblemsViewEx().checkErrors();				
	}	
	
	@Test
	public void testCloneProject(){
		String clonedProjectName = "ClonedProject";
		new ShellMenu("Project","Clone Project").select();
		new DefaultShell("Clone Model Project");
		new LabeledText("Project name:").setText(clonedProjectName);
		new FinishButton().click();
		new WaitWhile(new ShellWithTextIsActive("Clone Model Project"), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
		
		assertTrue(modelExplorer.containsProject(clonedProjectName));
		
		Project clonedProject = modelExplorer.getProject(clonedProjectName);
		clonedProject.refresh();
		
		String[] modelPath = RELATIONAL_SOURCE_MODEL.split("/");
		String[] modelPathWithoutProject = Arrays.copyOfRange(modelPath, 1, modelPath.length);
		assertTrue(clonedProject.containsItem(modelPathWithoutProject));
		
		modelPath = RELATIONAL_VIEW_MODEL.split("/");
		modelPathWithoutProject = Arrays.copyOfRange(modelPath, 1, modelPath.length);
		assertTrue(clonedProject.containsItem(modelPathWithoutProject));
		
		modelPath = XML_VIEW_MODEL.split("/");
		modelPathWithoutProject = Arrays.copyOfRange(modelPath, 1, modelPath.length);
		assertTrue(clonedProject.containsItem(modelPathWithoutProject));
		
		modelPath = XML_SCHEMA_MODEL.split("/");
		modelPathWithoutProject = Arrays.copyOfRange(modelPath, 1, modelPath.length);
		assertTrue(clonedProject.containsItem(modelPathWithoutProject));
		
		modelPath = WEBSERVICE_VIEW_MODEL.split("/");
		modelPathWithoutProject = Arrays.copyOfRange(modelPath, 1, modelPath.length);
		assertTrue(clonedProject.containsItem(modelPathWithoutProject));
	}
}
