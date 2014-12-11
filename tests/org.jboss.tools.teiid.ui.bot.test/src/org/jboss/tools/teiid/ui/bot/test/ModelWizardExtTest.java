package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel;
import org.jboss.tools.teiid.reddeer.wizard.WsdlWebImportWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author lfabriko
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class ModelWizardExtTest {

	private static final String PROJECT_NAME = "ModelWizardExt";
	private static final String WEBSERVICE_MODEL_NAME = "WsViewBuildFromWSDL";
	private static final Logger log = Logger.getLogger(ModelWizardExtTest.class);

	@BeforeClass
	public static void setup() {

		new TeiidBot().uncheckBuildAutomatically();
		new ModelExplorerManager().createProject(PROJECT_NAME);
	}

	public void relSrcFileTranslatorTest() {
		// TODO not implemented yet!
	}

	public void relSrcWSTranslatorTest() {
		// TODO not implemented yet!
	}

	public void relSrcCopyExistingTest() {
		// TODO not implemented yet!
	}

	public void relViewTransformExistingTest() {
		// TODO not implemented yet!
	}

	public void relViewCopyExistingTest() {
		// TODO not implemented yet!
	}

	public void xmlViewCopyExistingTest() {
		// TODO not implemented yet!
	}

	public void xmlViewBuildFromSchemaTest() {
		// TODO not implemented yet!
	}

	public void xsdDatatypeCopyExistingTest() {
		// TODO not implemented yet!
	}

	public void wsViewCopyExistingTest() {
		// TODO not implemented yet!
	}

	@Test
	public void wsViewBuildFromWSDLorURLTest() {

		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(WEBSERVICE_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.WEBSERVICE);
		createModel.setType(CreateMetadataModel.ModelType.VIEW);
		createModel.setModelBuilder(CreateMetadataModel.ModelBuilder.BUILD_FROM_WSDL_URL);

		Properties iProps = new Properties();
		iProps.setProperty("modelName", WEBSERVICE_MODEL_NAME);
		iProps.setProperty("project", PROJECT_NAME);
		iProps.setProperty("wsdlUrl", "http://www.webservicex.com/globalweather.asmx?WSDL");
		createModel.setWsProps(iProps);
		createModel.setWsdlLocation(WsdlWebImportWizard.IMPORT_WSDL_FROM_URL);

		try {
			createModel.execute();
		} catch (Exception e) {
			log.error("wsViewBuildFromWSDLorURLTest: Couldn't create metadata model");
		}

		assertTrue(new ProjectExplorer().getProject(PROJECT_NAME).containsItem(WEBSERVICE_MODEL_NAME + ".xmi"));
		assertTrue(new ModelEditor(WEBSERVICE_MODEL_NAME + ".xmi").isActive());

		// TODO preview data
	}

	// TODO ??? same as wsViewBuildFromWSDLorURLTest, but use wsdl from
	// jbossws-cxf war on localhost
}
