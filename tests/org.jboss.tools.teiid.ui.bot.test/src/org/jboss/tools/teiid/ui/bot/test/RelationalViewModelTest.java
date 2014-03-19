package org.jboss.tools.teiid.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.tools.teiid.ui.bot.test.requirement.PerspectiveRequirement.Perspective;
import org.junit.Test;

/**
 * 
 * @author lfabriko
 *
 */
@Perspective(name = "Teiid Designer")
public class RelationalViewModelTest extends SWTBotTestCase {

	@Test//?
	public void copyFromExistingModel(){
		
	}
	
	@Test//?
	public void transformFromExistingModel(){
		
	}
	
	@Test
	public void createTab(){
		
	}
	
	@Test
	public void createProcedure(){
		
	}
	
	@Test
	public void createUDF(){
		
	}
	
	@Test
	public void createMaterializedView(){
		//and test it
	}
	
	//create index?
	//TODO transformation editor - define select, insert, update, delete; verify results
	//---> transformation tests? virtual procedures
	
	//TODO? vdb smoke test - for editor - or E2eDataRoles test;
}
