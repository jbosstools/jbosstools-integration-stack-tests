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
public class RelationalSourceModelTest extends SWTBotTestCase {

	@Test
	public void generateFileTranslatorProcedures(){
		//create rel. src model with this option
	}
	
	@Test
	public void generateWebServiceTranslatorProcedures(){
		
	}
	
	@Test
	public void copyFromExistingModel(){
		
	}
	
	@Test
	public void createTab(){
		
	}
	
	@Test
	public void createProcedure(){
		
	}
	
	@Test
	public void createSourceFunction(){
		
	}
	
	@Test
	public void createView(){
		
	}
	
	@Test
	public void newAssociation(){
		//created by selecting with Ctrl two columns -> right-click -> new assoc
		//can be specified in the properties of the link
	}
	
	//TODO associations 1:N,...
}
