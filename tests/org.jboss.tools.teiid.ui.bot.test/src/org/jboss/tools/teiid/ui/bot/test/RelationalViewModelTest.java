package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author lfabriko
 *
 */
@Perspective(name = "Teiid Designer")
public class RelationalViewModelTest extends SWTBotTestCase {

	private static String PROJECT_NAME = "RelViewModelTest";
	private static String RELATIONAL_VIEW_MODEL_NAME = "viewModel.xmi";

	@BeforeClass
	public static void prepare(){
		new ModelExplorerManager().createProject(PROJECT_NAME);
		
		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT_NAME);
		createModel.setName(RELATIONAL_VIEW_MODEL_NAME);
		createModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
		createModel.setType(CreateMetadataModel.ModelType.VIEW);
		createModel.execute();
	}
	
	//@Test//?
	public void copyFromExistingModel(){
		
	}
	
	//@Test//?
	public void transformFromExistingModel(){
		
	}
	
	//@Test
	public void createTab(){
		
	}
	
	@Test
	public void createProcedure(){
		new ModelExplorerManager().createNewProcedure(PROJECT_NAME, RELATIONAL_VIEW_MODEL_NAME, "myProc");
	}
	
	//@Test
	public void createUDF(){
		
	}
	
	//@Test
	public void createMaterializedView(){
		//and test it
	}
	
	//create index?
	//TODO transformation editor - define select, insert, update, delete; verify results
	//---> transformation tests? virtual procedures
	
	//TODO? vdb smoke test - for editor - or E2eDataRoles test;
}
