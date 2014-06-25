package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.suite.TeiidSuite;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author lfabriko
 */
@Perspective(name = "Teiid Designer")
@RunWith(TeiidSuite.class)
public class RelationalSourceModelTest extends RedDeerTest {

	private static final String PROJECT = "RelSrcModel";
	private static final String MODEL = "relModel.xmi";
	private static TeiidBot teiidBot = new TeiidBot();
	
	@BeforeClass
	public static void before() {
		
		new ModelExplorerManager().createProject(PROJECT);

		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.activate();
		modelWizard.setLocation(PROJECT);
		modelWizard.setModelName(MODEL);
		modelWizard.selectModelClass(ModelClass.RELATIONAL);
		modelWizard.selectModelType(ModelType.SOURCE);
		modelWizard.finish();
	}
	
	@Test
	public void createProcedure() {

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
	public void createSrcFunction() {

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
	
	//TODO associations 1:N,...
}
