package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel;
import org.jboss.tools.teiid.reddeer.wizard.WsdlWebImportWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author lfabriko
 *
 */
@Perspective(name = "Teiid Designer")
public class ModelWizardExtTest extends SWTBotTestCase {

	
	private static final String PROJECT_NAME = "ModelWizardExt";
	private static final String WEBSERVICE_MODEL_NAME = "WsViewBuildFromWSDL";

	@BeforeClass
	public static void setup(){
		new TeiidBot().uncheckBuildAutomatically();
		new ModelExplorerManager().createProject(PROJECT_NAME);
	}
	
	public void relSrcFileTranslatorTest(){
		
	}
	
	public void relSrcWSTranslatorTest(){
		
	}
	
	public void relSrcCopyExistingTest(){
		
	}
	
	public void relViewTransformExistingTest(){
		
	}
	
	public void relViewCopyExistingTest(){
		
	}
	
	public void xmlViewCopyExistingTest(){
		
	}
	
	public void xmlViewBuildFromSchemaTest(){
		
	}
	
	public void xsdDatatypeCopyExistingTest(){
		
	}
	
	public void wsViewCopyExistingTest(){
		
	}
	
	@Test
	public void wsViewBuildFromWSDLorURLTest(){
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
		} catch (Exception e){
			System.err.println("wsViewBuildFromWSDLorURLTest: Couldn't create metadata model");
			e.printStackTrace();
		}
		
		assertTrue(new ProjectExplorer().getProject(PROJECT_NAME).containsItem(WEBSERVICE_MODEL_NAME + ".xmi"));

		assertTrue(new ModelEditor(WEBSERVICE_MODEL_NAME + ".xmi").isActive());
	
	}
}
