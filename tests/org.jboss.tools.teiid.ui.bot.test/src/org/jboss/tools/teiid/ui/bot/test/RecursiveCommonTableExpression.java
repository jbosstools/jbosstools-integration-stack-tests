package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
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
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = { ConnectionProfileConstants.ORACLE_11G_BQT2 })
public class RecursiveCommonTableExpression {

	private static final String PROJECT_NAME = "RecursiveCTE";
	private static final String SOURCE_MODEL_NAME = "Oracle_11g_bqt2.xmi";
	private static final String VIEW_MODEL_NAME = "Oracle11View.xmi";
	private static final String VDB_NAME = "RCTEVDB";
	private static final String TRANSFORMATION_SQL = "with a as (select intkey, 0 as lvl from Oracle11View.smallb where intkey = 1 UNION ALL select n.intkey, rcte.lvl + 1 as lvl from Oracle11View.smallb n inner join a rcte on n.intkey = rcte.intkey + 1) select * from a";

	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	@Before
	public void before() {
		new ModelExplorer().importProject(PROJECT_NAME);
		new ModelExplorer().changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_BQT2, PROJECT_NAME,
				SOURCE_MODEL_NAME);
	}

	@Test
	public void testRCTE() {
		new ModelExplorer().openModelEditor(PROJECT_NAME, VIEW_MODEL_NAME);
		RelationalModelEditor editor = new RelationalModelEditor(VIEW_MODEL_NAME);
	    TransformationEditor transformationEditor =  editor.openTransformationDiagram(ModelEditor.ItemType.TABLE, "TestTable");
	    transformationEditor.insertAndValidateSql(TRANSFORMATION_SQL);

		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, SOURCE_MODEL_NAME)
				.addModel(PROJECT_NAME, VIEW_MODEL_NAME)
				.finish();
		
		new ModelExplorer().deployVdb(PROJECT_NAME, VDB_NAME);

		TeiidJDBCHelper JDBCHelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);

		assertEquals(49, JDBCHelper.getNumberOfResults("SELECT * FROM TestTable"));
	}

}
