package org.jboss.tools.drools.ui.bot.test.kienavigator;

import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.AddRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateOrgUnitDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.item.RepositoryItem;
import org.jboss.tools.drools.reddeer.view.KieNavigatorView;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(type = {ServerReqType.EAP, ServerReqType.WildFly}, state = ServerReqState.RUNNING)
@RunWith(RedDeerSuite.class)
public class RepositoryManipulationTest extends KieNavigatorTestParent {
	
	@InjectRequirement
	private ServerRequirement serverReq;
	
	@Test
	public void repositoryTest() {
		KieNavigatorView knv = new KieNavigatorView();
		knv.open();
		CreateOrgUnitDialog cod = knv.getServer(0).createOrgUnit();
		cod.setName("repotest");
		cod.setOwner("owner");
		cod.ok();
		
		CreateRepositoryDialog crd = knv.getOrgUnit(0, "repotest").createRepository();
		crd.setName("newrepo");
		crd.ok();
		knv.getRepository(0, "repotest", "newrepo").removeRepository().yes();
		
		AddRepositoryDialog ard = knv.getOrgUnit(0, "repotest").addRepository();
		ard.selectRepository("newrepo");
		ard.ok();
		
		List<RepositoryItem> riList = knv.getOrgUnit(0, "repotest").getRepositories();
		Assert.assertEquals(1, riList.size());
		Assert.assertEquals("newrepo", riList.get(0).getName());
	}
}
