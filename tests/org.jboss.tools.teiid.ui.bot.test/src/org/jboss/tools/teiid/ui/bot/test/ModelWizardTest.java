package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.junit.After;
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
		if (new ShellMenu("Project", "Build Automatically").isSelected()) {
			new ShellMenu("Project", "Build Automatically").select();
		}
		new ModelExplorer().createProject(PROJECT_NAME);
	}

	@After
	public void afterMethod() {

		System.out.println("TEST METHOD END");
	}

	@Test
	public void relationalSourceModel() {

		MetadataModelWizard wizard = new MetadataModelWizard();
		wizard.open();
		wizard.setLocation(PROJECT_NAME).setModelName(RELATIONAL_SOURCE_MODEL_NAME)
				.selectModelClass(ModelClass.RELATIONAL).selectModelType(ModelType.SOURCE).finish();

		assertTrue(getProject().containsItem(RELATIONAL_SOURCE_MODEL_NAME + ".xmi"));
		assertTrue(new ModelEditor(RELATIONAL_SOURCE_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void relationalViewModel() {

		MetadataModelWizard wizard = new MetadataModelWizard();
		wizard.open();
		wizard.setLocation(PROJECT_NAME).setModelName(RELATIONAL_VIEW_MODEL_NAME)
				.selectModelClass(ModelClass.RELATIONAL).selectModelType(ModelType.VIEW).finish();

		assertTrue(getProject().containsItem(RELATIONAL_VIEW_MODEL_NAME + ".xmi"));
		assertTrue(new ModelEditor(RELATIONAL_VIEW_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void xmlViewModel() {

		MetadataModelWizard wizard = new MetadataModelWizard();
		wizard.open();
		wizard.setLocation(PROJECT_NAME).setModelName(XML_VIEW_MODEL_NAME).selectModelClass(ModelClass.XML)
				.selectModelType(ModelType.VIEW).finish();

		assertTrue(getProject().containsItem(XML_VIEW_MODEL_NAME + ".xmi"));
		assertTrue(new ModelEditor(XML_VIEW_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void xsdDatatypeModel() {

		MetadataModelWizard wizard = new MetadataModelWizard();
		wizard.open();
		wizard.setLocation(PROJECT_NAME).setModelName(XSD_DATATYPE_MODEL_NAME).selectModelClass(ModelClass.XSD)
				.selectModelType(ModelType.DATATYPE).finish();
		new DefaultShell("Model Initializer");
		new PushButton("OK").click();

		assertTrue(getProject().containsItem(XSD_DATATYPE_MODEL_NAME + ".xsd"));
		assertTrue(new ModelEditor(XSD_DATATYPE_MODEL_NAME + ".xsd").isActive());
	}

	@Test
	public void webserviceViewModel() {
		MetadataModelWizard wizard = new MetadataModelWizard();
		wizard.open();
		wizard.setLocation(PROJECT_NAME).setModelName(WEBSERVICE_MODEL_NAME).selectModelClass(ModelClass.WEBSERVICE)
				.selectModelType(ModelType.VIEW).finish();

		assertTrue(getProject().containsItem(WEBSERVICE_MODEL_NAME + ".xmi"));
		assertTrue(new ModelEditor(WEBSERVICE_MODEL_NAME + ".xmi").isActive());
	}

	protected static Project getProject() {

		Project project = new ProjectExplorer().getProject(PROJECT_NAME);
		return project;
	}
}
