package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.extensions.FlatFileProfileExt;
import org.jboss.tools.teiid.reddeer.manager.ConnectionProfileManager;
import org.jboss.tools.teiid.reddeer.manager.ImportManager;
import org.jboss.tools.teiid.reddeer.manager.ModelProjectManager;
import org.jboss.tools.teiid.reddeer.manager.VDBManager;
import org.jboss.tools.teiid.reddeer.matcher.ToolBarButtonWithLabel;
import org.jboss.tools.teiid.reddeer.wizard.FlatImportWizard;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.State;
import org.jboss.tools.teiid.ui.bot.test.requirement.ServerRequirement.Type;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test Flat File >> Source and View model, i.e. TEXTTABLE in teiid 
 * 
 * @author lfabriko
 *
 */

@Perspective(name = "Database Development")
//@Server(type = Type.ALL, state = State.RUNNING)//uses info about server - swtbot.properties
public class FlatFileImportTest extends SWTBotTestCase{

	private static final String MODEL_PROJECT_NAME = "FlatImport";
	private static final String VDB_NAME = "vdb1";

	@BeforeClass
	public static void prepare(){
		new ModelProjectManager().createNewModelProject(MODEL_PROJECT_NAME);
		System.out.println();
		//new SWTBot().widgets(new ToolBarButtonWithLabel("New Connection Profile"));//todo 
		
	}
	
	//TEST TAB DELIMITER + VARIOUS ENDS OF LINES + VARIOUS TEXTTABLE OPTIONS
	/**
	 * delimiter: tab, 
	 * texttable function: not escaped, 
	 * end of row: enter
	 */
	/*@Test
	public void testTabNotEscEndEnter() {
		// create all flat file conn profiles
		FlatFileProfileExt flatProfile = new FlatFileProfileExt();
		new ConnectionProfileManager().createFlatFileProfileExt(flatProfile);

		// import flat file >> source and view
		FlatImportWizard fiwizard = new FlatImportWizard();
		new ImportManager().importModel(fiwizard, MODEL_PROJECT_NAME);

		// execute
		System.out.println();
		// create and execute VDB
		new VDBManager().createVDB(MODEL_PROJECT_NAME, VDB_NAME);
	}*/
	
	/**
	 * delimiter: tab, 
	 * texttable function: escaped, 
	 * end of row: enter
	 */
	public void testTabEscEndEnter(){
		
	}
	
	/**
	 * delimiter: tab, 
	 * texttable function: not escaped, 
	 * end of row: escaped \n
	 */
	public void testTabNotEscEndEsc(){
		
	}
	
	/**
	 * delimiter: tab, 
	 * texttable function: escaped, 
	 * end of row: escaped \n
	 */
	public void testTabEscEndEsc(){
		
	}
	
	
	//TEST OTHER DELIMITERS - ROW END WITH ENTER
	
}
