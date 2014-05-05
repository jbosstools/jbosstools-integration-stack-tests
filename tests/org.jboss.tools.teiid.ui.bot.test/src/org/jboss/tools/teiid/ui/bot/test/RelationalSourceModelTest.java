package org.jboss.tools.teiid.ui.bot.test;

import java.util.Properties;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.teiid.reddeer.Procedure;
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
public class RelationalSourceModelTest extends SWTBotTestCase {

	private static final String PROJECT = "RelSrcModel";
	private static final String MODEL = "relModel.xmi";
	private static TeiidBot teiidBot = new TeiidBot();
	
	@BeforeClass
	public static void before(){
		new ModelExplorerManager().createProject(PROJECT);
		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT);
		createModel.setName(MODEL);
		createModel.setClass(CreateMetadataModel.ModelClass.RELATIONAL);
		createModel.setType(CreateMetadataModel.ModelType.SOURCE);
		createModel.execute();
	}
	
	//@Test
	public void generateFileTranslatorProcedures(){
		//create rel. src model with this option
	}
	
	//@Test
	public void generateWebServiceTranslatorProcedures(){
		
	}
	
	//@Test
	public void copyFromExistingModel(){
		
	}
	
	//@Test
	public void createTab(){
		String tab = "tab";
		new ModelExplorerManager().getModelExplorerView().newBaseTable(PROJECT, MODEL+".xmi", tab);
		
		assertTrue(teiidBot.checkResource(PROJECT,  MODEL + ".xmi", tab));
	}
	
	@Test
	public void createProcedure(){
		String proc = "proc";
		Properties props = new Properties();
		props.setProperty("type", Procedure.Type.RELSRC_PROCEDURE);
		props.setProperty("nativeQuery", "select name from mytab");
		props.setProperty("description", "this is relsrc proc");
		props.setProperty("includeResultSet", "true");
		props.setProperty("resultSetName", "resSet");
		props.setProperty("cols", "col1,col2,col3");
		props.setProperty("params", "myparam1,myparam2");
		props.setProperty("updateCount", Procedure.UpdateCount.ONE);
		props.setProperty("nonPrepared", "true");
		
		new ModelExplorerManager().getModelExplorerView().newProcedure(PROJECT, MODEL, proc, props);
		
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "myparam1 : string(4000)"));
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "myparam2 : string(4000)"));
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "resSet", "col1 : string(4000)"));
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "resSet", "col2 : string(4000)"));
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "resSet", "col3 : string(4000)"));
	}
	
	@Test
	public void createSrcFunction(){
		String proc = "srcFunction";
		Properties props = new Properties();
		props.setProperty("type", Procedure.Type.RELSRC_SOURCE_FUNCTION);
		props.setProperty("description", "this is relsrc proc");
		props.setProperty("params", "myparam2,myparam3");
		props.setProperty("functionProps", Procedure.FunctionProperties.DETERMINISTIC+","+Procedure.FunctionProperties.VARIABLE_ARGUMENTS);
		props.setProperty("aggregateProps", Procedure.AggregateProperties.AGG + ","+Procedure.AggregateProperties.DECOMPOSABLE);
		
		new ModelExplorerManager().getModelExplorerView().newProcedure(PROJECT, MODEL, proc, props);
		
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "myparam2 : string(4000)"));
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "myparam3 : string(4000)"));
	}
	
	//@Test
	public void createSourceFunction(){
		
	}
	
	//@Test
	public void createView(){
		
	}
	
	//@Test
	public void newAssociation(){
		//created by selecting with Ctrl two columns -> right-click -> new assoc
		//can be specified in the properties of the link
	}
	
	//TODO associations 1:N,...
}
