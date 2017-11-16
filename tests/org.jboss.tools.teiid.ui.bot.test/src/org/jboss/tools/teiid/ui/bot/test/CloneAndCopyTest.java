package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.FinishButton;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ProblemsViewEx;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard.ModelClass;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard.ModelType;
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
		modelExplorer.refreshProject(PROJECT_NAME);
	}
	
	@After
	public void cleanUp(){
		modelExplorer.deleteAllProjectsSafely();
	}
	
	@Test
	public void testCopyRelationalSourceModel(){
		String newModelName = "RelationalSourceModelCopy";
		modelExplorer.copyModel(RELATIONAL_SOURCE_MODEL, newModelName, PROJECT_NAME + "/Copy", ModelClass.RELATIONAL, ModelType.SOURCE);
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"Copy", newModelName + ".xmi"));
		ProblemsViewEx.checkErrors();
	}
	
	@Test
	public void testCopyRelationalViewModel(){
		String newModelName = "RelationalViewModelCopy";
		modelExplorer.copyModel(RELATIONAL_VIEW_MODEL, newModelName, PROJECT_NAME + "/Copy", ModelClass.RELATIONAL, ModelType.VIEW);
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"Copy", newModelName + ".xmi"));
		ProblemsViewEx.checkErrors();	
	}
	
	@Test
	public void testCopyXmlViewModel(){
		String newModelName = "XmlViewModelCopy";
		modelExplorer.copyModel(XML_VIEW_MODEL, newModelName, PROJECT_NAME + "/Copy", ModelClass.XML, ModelType.VIEW);
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"Copy", newModelName + ".xmi"));
		ProblemsViewEx.checkErrors();	
	}
	
	@Test
	public void testCopyXmlSchemaModel(){
		String newModelName = "XmlSchemaModelCopy";
		modelExplorer.copyModel(XML_SCHEMA_MODEL, newModelName, PROJECT_NAME + "/Copy", ModelClass.XSD, ModelType.DATATYPE);
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"Copy", newModelName + ".xsd"));
		ProblemsViewEx.checkErrors();
	}
	
	@Test
	public void testCopyWebServiceViewModel(){
		String newModelName = "WebServiceViewModelCopy";
		modelExplorer.copyModel(WEBSERVICE_VIEW_MODEL, newModelName, PROJECT_NAME + "/Copy", ModelClass.WEBSERVICE, ModelType.VIEW);
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"Copy", newModelName + ".xmi"));
		ProblemsViewEx.checkErrors();	
	}
	
	@Test
	public void testSaveAsRelationalSourceModel(){
        if (new JiraClient().isIssueClosed("TEIIDDES-3119")) {
            String newModelName = "RelationalSourceModelSaveAs";
            modelExplorer.saveModelAs(RELATIONAL_SOURCE_MODEL, newModelName, PROJECT_NAME + "/SaveAs", Boolean.FALSE);
            assertTrue(modelExplorer.containsItem(PROJECT_NAME, "SaveAs", newModelName + ".xmi"));
            ProblemsViewEx.checkErrors();
        }
	}
	
	@Test
	public void testSaveAsRelationalViewModel(){
		String newModelName = "RelationalViewModelSaveAs";
		modelExplorer.saveModelAs(RELATIONAL_VIEW_MODEL, newModelName, PROJECT_NAME + "/SaveAs", Boolean.FALSE);
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"SaveAs", newModelName + ".xmi"));
		ProblemsViewEx.checkErrors();		
	}
	
	@Test
	public void testSaveAsXmlViewModel(){
		String newModelName = "XmlViewModelSaveAs";
		modelExplorer.saveModelAs(XML_VIEW_MODEL, newModelName, PROJECT_NAME + "/SaveAs", Boolean.FALSE);
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"SaveAs", newModelName + ".xmi"));
		ProblemsViewEx.checkErrors();		
	}
	
	@Test
	public void testSaveAsXmlSchemaModel(){
		String newModelName = "XmlSchemaModelSaveAs";
		modelExplorer.saveModelAs(XML_SCHEMA_MODEL, newModelName, PROJECT_NAME + "/SaveAs", Boolean.TRUE);
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"SaveAs", newModelName + ".xsd"));
		ProblemsViewEx.checkErrors();				
	}	
	
	@Test
	public void testSaveAsWebServiceViewModel(){
		String newModelName = "WebServiceViewModelSaveAs";
		modelExplorer.saveModelAs(WEBSERVICE_VIEW_MODEL, newModelName, PROJECT_NAME + "/SaveAs", Boolean.FALSE);
		assertTrue(modelExplorer.containsItem(PROJECT_NAME,"SaveAs", newModelName + ".xmi"));
		ProblemsViewEx.checkErrors();			
	}	
	
	@Test
	public void testCloneProject(){
		String clonedProjectName = "ClonedProject";
		new ShellMenuItem(new WorkbenchShell(), "Project","Clone Project").select();
		new DefaultShell("Clone Model Project");
		new LabeledText("Project name:").setText(clonedProjectName);
		new FinishButton().click();
		new WaitWhile(new ShellIsActive("Clone Model Project"), TimePeriod.DEFAULT);
		AbstractWait.sleep(TimePeriod.SHORT);
		new WorkbenchShell();
		
		assertTrue(modelExplorer.containsProject(clonedProjectName));
				
		String[] modelPath = RELATIONAL_SOURCE_MODEL.split("/");
		assertTrue(modelExplorer.containsItem(modelPath));
		
		modelPath = RELATIONAL_VIEW_MODEL.split("/");
		assertTrue(modelExplorer.containsItem(modelPath));
		
		modelPath = XML_VIEW_MODEL.split("/");
		assertTrue(modelExplorer.containsItem(modelPath));
		
		modelPath = XML_SCHEMA_MODEL.split("/");
		assertTrue(modelExplorer.containsItem(modelPath));
		
		modelPath = WEBSERVICE_VIEW_MODEL.split("/");
		assertTrue(modelExplorer.containsItem(modelPath));
	}
}
