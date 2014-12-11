package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author lfabriko
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class RelationalViewModelTest {

	private static String PROJECT = "relViewTest";
	private static String MODEL = "view.xmi";
	private static TeiidBot teiidBot = new TeiidBot();

	@BeforeClass
	public static void prepare() {

		new ModelExplorerManager().createProject(PROJECT);
		
		MetadataModelWizard modelWizard = new MetadataModelWizard();
		modelWizard.open();
		modelWizard.activate();
		modelWizard.setLocation(PROJECT);
		modelWizard.setModelName(MODEL);
		modelWizard.selectModelClass(ModelClass.RELATIONAL);
		modelWizard.selectModelType(ModelType.VIEW);
		modelWizard.finish();
	}

	@Test
	public void createProcedure() {

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
	public void createUDF() {

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

		// TODO view properties
	}

	@Test
	public void addParamToProcedure() {

		String procName = "myProc";
		Procedure proc = new ModelExplorerManager().createProcedure(PROJECT, MODEL, procName);
		proc.addParameter2("qtyIn", "short : xs:int");

		assertTrue(teiidBot.checkResource(PROJECT, MODEL, procName, "qtyIn : short"));
	}

	// create index?
	// TODO transformation editor - define select, insert, update, delete;
	// verify results
	// ---> transformation tests? virtual procedures

	// TODO? vdb smoke test - for editor - or E2eDataRoles test;
}
