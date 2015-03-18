package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for the creation of new models using Teiid Designer wizard.
 * 
 * @author psrna
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.PRESENT)
public class ModelWizardTest {

	private static final String PROJECT_NAME = "ModelWizardTestProject";
	public static final String RELATIONAL_SOURCE_MODEL_NAME = "relational_source";
	public static final String RELATIONAL_VIEW_MODEL_NAME = "relational_view";
	public static final String XML_VIEW_MODEL_NAME = "xml_view";
	public static final String XSD_DATATYPE_MODEL_NAME = "xsd_datatype";
	public static final String WEBSERVICE_MODEL_NAME = "webservice_view";
	public static final String MODELEXT_MODEL_NAME = "modelext";
	public static final String FUNCTION_MODEL_NAME = "function_userdef";

	@BeforeClass
	public static void beforeClass() {

		new org.jboss.reddeer.swt.impl.menu.ShellMenu("Project", "Build Automatically").select();
		new ModelExplorerManager().createProject(PROJECT_NAME);
	}

	@AfterClass
	public static void saveAllFiles() {

		new ShellMenu("File", "Save All").select();
		new SWTWorkbenchBot().sleep(1000);
	}

	@After
	public void afterMethod() {

		System.out.println("TEST METHOD END");
	}

	@Test
	public void relationalSourceModel() {

		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(RELATIONAL_SOURCE_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
		createModel.setType(CreateMetadataModel.ModelType.SOURCE);
		createModel.execute();

		assertTrue(getProject().containsItem(RELATIONAL_SOURCE_MODEL_NAME + ".xmi"));
		assertTrue(new ModelEditor(RELATIONAL_SOURCE_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void relationalViewModel() {

		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(RELATIONAL_VIEW_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
		createModel.setType(CreateMetadataModel.ModelType.VIEW);
		createModel.execute();

		assertTrue(getProject().containsItem(RELATIONAL_VIEW_MODEL_NAME + ".xmi"));
		assertTrue(new ModelEditor(RELATIONAL_VIEW_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void xmlViewModel() {

		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(XML_VIEW_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.XML);
		createModel.setType(CreateMetadataModel.ModelType.VIEW);
		createModel.execute();

		assertTrue(getProject().containsItem(XML_VIEW_MODEL_NAME + ".xmi"));
		assertTrue(new ModelEditor(XML_VIEW_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void xsdDatatypeModel() {

		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(XSD_DATATYPE_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.XSD);
		createModel.setType(CreateMetadataModel.ModelType.DATATYPE);
		createModel.execute();

		assertTrue(getProject().containsItem(XSD_DATATYPE_MODEL_NAME + ".xsd"));
		assertTrue(new ModelEditor(XSD_DATATYPE_MODEL_NAME + ".xsd").isActive());
	}

	@Test
	public void webserviceViewModel() {

		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(WEBSERVICE_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.WEBSERVICE);
		createModel.setType(CreateMetadataModel.ModelType.VIEW);
		createModel.execute();

		assertTrue(getProject().containsItem(WEBSERVICE_MODEL_NAME + ".xmi"));
		assertTrue(new ModelEditor(WEBSERVICE_MODEL_NAME + ".xmi").isActive());
	}

	protected static Project getProject() {

		Project project = new ProjectExplorer().getProject(PROJECT_NAME);
		return project;
	}
}
