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
public class RelationalViewModelTest extends SWTBotTestCase {
	
	private static String PROJECT = "relViewTest";
	private static String MODEL = "view.xmi";
	private static TeiidBot teiidBot = new TeiidBot();
	
	@BeforeClass
	public static void prepare(){
		new ModelExplorerManager().createProject(PROJECT);
		
		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT);
		createModel.setName(MODEL);
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
		String proc = "proc2";
		Properties props = new Properties();
		props.setProperty("type", Procedure.Type.RELVIEW_PROCEDURE);
		props.setProperty("template", Procedure.Template.INSERT);
		props.setProperty("description", "this is relview procedure");
		props.setProperty("includeResultSet", "true");
		props.setProperty("resultSetName", "rsname");
		props.setProperty("cols", "col1,col2");
		props.setProperty("params", "param1,param2,param3");
		props.setProperty("updateCount", Procedure.UpdateCount.MULTIPLE);
		props.setProperty("nonPrepared", "true");
		
		new ModelExplorerManager().getModelExplorerView().newProcedure(PROJECT, MODEL, proc, props);
		
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "param1 : string(4000)"));
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "param2 : string(4000)"));
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "param3 : string(4000)"));
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "rsname", "col1 : string(4000)"));
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "rsname", "col2 : string(4000)"));
	}
	
	@Test
	public void createUDF(){
		String proc = "udf";
		Properties props = new Properties();
		props.setProperty("type", Procedure.Type.RELVIEW_USER_DEFINED_FUNCTION);
		props.setProperty("description", "this is relview udf");
		props.setProperty("params", "param1,param2");
		props.setProperty("functionCategory", "myFunctCateg");
		props.setProperty("javaClass", "myClass");
		props.setProperty("javaMethod", "myJavaMethod");
		props.setProperty("udfJarPath", "/some/path.jar");
		props.setProperty("functionProps", Procedure.FunctionProperties.RETURNS_NULL_ON_NULL);
		props.setProperty("aggregateProps", Procedure.AggregateProperties.AGG + "," + Procedure.AggregateProperties.ANALYTIC);
		
		new ModelExplorerManager().getModelExplorerView().newProcedure(PROJECT, MODEL, proc, props);
		
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "param1 : string(4000)"));
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, proc, "param2 : string(4000)"));
		//TODO view properties
	}
	
	@Test
	public void addParamToProcedure(){
		String procName = "myProc";
		Procedure proc = new ModelExplorerManager().createProcedure(PROJECT, MODEL, procName);
		proc.addParameter2("qtyIn", "short : xs:int");
		
		assertTrue(teiidBot.checkResource(PROJECT, MODEL, procName, "qtyIn : short"));
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
