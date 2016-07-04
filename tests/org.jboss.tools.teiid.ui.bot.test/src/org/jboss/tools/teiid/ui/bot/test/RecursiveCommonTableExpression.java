package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for Recursive Common Table Expressions
 * 
 * @author mmakovy
 * 
 */

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = { ConnectionProfileConstants.ORACLE_11G_BQT2 })
public class RecursiveCommonTableExpression {

	private static final String PROJECT_NAME = "RecursiveCTE";
	private static final String SOURCE_MODEL_NAME = "Oracle_11g_bqt2.xmi";
	private static final String VIEW_MODEL_NAME = "Oracle11View.xmi";
	private static final String VDB_NAME = "RCTEVDB";
	private static final String TRANSFORMATION_SQL = "with a as (select intkey, 0 as lvl from Oracle11View.smallb where intkey = 1 UNION ALL select n.intkey, rcte.lvl + 1 as lvl from Oracle11View.smallb n inner join a rcte on n.intkey = rcte.intkey + 1) select * from a";

	private static TeiidBot teiidBot = new TeiidBot();

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	@Before
	public void before() {
		new ModelExplorer().importProject(teiidBot.toAbsolutePath("resources/projects/" + PROJECT_NAME));
		new ModelExplorer().changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_BQT2, PROJECT_NAME,
				SOURCE_MODEL_NAME);

	}

	@Test
	public void testRCTE() {

		ModelExplorer modelView = TeiidPerspective.getInstance().getModelExplorerView();
		modelView.openTransformationDiagram(PROJECT_NAME, VIEW_MODEL_NAME, "TestTable");
		ModelEditor me = new ModelEditor(VIEW_MODEL_NAME);
		me.showTransformation();
		me.setTransformation(TRANSFORMATION_SQL);
		me.saveAndValidateSql();

		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, SOURCE_MODEL_NAME)
				.addModel(PROJECT_NAME, VIEW_MODEL_NAME)
				.finish();
		
		new ModelExplorer().deployVdb(PROJECT_NAME, VDB_NAME);

		TeiidJDBCHelper JDBCHelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);

		assertEquals(50, JDBCHelper.getNumberOfResults("SELECT * FROM TestTable"));
	}

}
