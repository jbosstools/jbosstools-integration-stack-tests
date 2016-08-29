package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.WebServiceModelEditor;
import org.jboss.tools.teiid.reddeer.editor.XmlModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author psrna, skaleta
 * tested features: 
 * - create models using model wizard
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class CreateModelTest {
	private static final String PROJECT_NAME = "ModelWizardTestProject";
	public static final String RELATIONAL_SOURCE_MODEL_NAME = "relational_source";
	public static final String RELATIONAL_VIEW_MODEL_NAME = "relational_view";
	public static final String XML_VIEW_MODEL_NAME = "xml_view";
	public static final String XSD_DATATYPE_MODEL_NAME = "xsd_datatype";
	public static final String WEBSERVICE_MODEL_NAME = "webservice_view";

	@BeforeClass
	public static void before(){
		new WorkbenchShell().maximize();
	}
	
	@Before
	public void createProject() {
		new ModelExplorer().createProject(PROJECT_NAME);
	}

	@After
	public void cleanUp(){
		new ModelExplorer().deleteAllProjectsSafely();
	}

	@Test
	public void relationalSourceModel() {
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(RELATIONAL_SOURCE_MODEL_NAME)
				.selectModelClass(ModelClass.RELATIONAL)
				.selectModelType(ModelType.SOURCE)
				.finish();

		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(RELATIONAL_SOURCE_MODEL_NAME + ".xmi"));
		assertTrue(new RelationalModelEditor(RELATIONAL_SOURCE_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void relationalViewModel() {
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(RELATIONAL_VIEW_MODEL_NAME)
				.selectModelClass(ModelClass.RELATIONAL)
				.selectModelType(ModelType.VIEW)
				.finish();

		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(RELATIONAL_VIEW_MODEL_NAME + ".xmi"));
		assertTrue(new RelationalModelEditor(RELATIONAL_VIEW_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void xmlViewModel() {
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(XML_VIEW_MODEL_NAME)
				.selectModelClass(ModelClass.XML)
				.selectModelType(ModelType.VIEW)
				.finish();

		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(XML_VIEW_MODEL_NAME + ".xmi"));
		assertTrue(new XmlModelEditor(XML_VIEW_MODEL_NAME + ".xmi").isActive());
	}

	@Test
	public void xsdDatatypeModel() {
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(XSD_DATATYPE_MODEL_NAME)
				.selectModelClass(ModelClass.XSD)
				.selectModelType(ModelType.DATATYPE)
				.finish();
		
		new DefaultShell("Model Initializer");
		new PushButton("OK").click();

		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(XSD_DATATYPE_MODEL_NAME + ".xsd"));
		// TODO SchemaModelEditor is not implemented yet
		assertTrue(new XmlModelEditor(XSD_DATATYPE_MODEL_NAME + ".xsd").isActive());
	}

	@Test
	public void webserviceViewModel() {
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(WEBSERVICE_MODEL_NAME)
				.selectModelClass(ModelClass.WEBSERVICE)
				.selectModelType(ModelType.VIEW)
				.finish();

		assertTrue(new ModelExplorer().getProject(PROJECT_NAME).containsItem(WEBSERVICE_MODEL_NAME + ".xmi"));
		assertTrue(new WebServiceModelEditor(WEBSERVICE_MODEL_NAME + ".xmi").isActive());
	}
}
