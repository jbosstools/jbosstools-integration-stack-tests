package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.manager.ServerManager;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author lfabriko
 *
 */
@Perspective(name = "Teiid Designer")
public class ServerOpsTest extends SWTBotTestCase {

	public static final String MODEL_PROJECT = "jdbcImportTest";

	private static TeiidBot teiidBot = new TeiidBot();
	private static final String DV6_PROPERTIES = "dv6.properties";
	private static final String DV6_SERVER = "EAP-6.1";
	
	@BeforeClass
	public static void before(){
		new ShellMenu("Project", "Build Automatically").select();
		new ModelExplorerManager().createProject(MODEL_PROJECT);
		new ServerManager().addServer(DV6_PROPERTIES);
		new ServerManager().startServer(DV6_SERVER);
	}
	
	@Test
	public void teiidTest(){//TODO --> move to "server - smoke tests": create source model from VDB, run DV6 and SOA5 simple mgmt tests 
			//firstly create teiid vdb
		//create source model from teiid vdb
	}
	

}
