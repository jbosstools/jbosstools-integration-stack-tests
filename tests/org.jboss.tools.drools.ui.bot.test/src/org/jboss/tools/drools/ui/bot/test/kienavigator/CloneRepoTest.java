package org.jboss.tools.drools.ui.bot.test.kienavigator;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateOrgUnitDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.item.RepositoryItem;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(type = {ServerReqType.EAP, ServerReqType.WildFly}, state = ServerReqState.RUNNING)
@RunWith(RedDeerSuite.class)
public class CloneRepoTest extends KieNavigatorTestParent {

	@InjectRequirement
	private ServerRequirement serverReq;
	
	@Test
	public void cloneRepoTest() {
 		CreateOrgUnitDialog cod = knv.getServer(0).createOrgUnit();
		cod.setName("clonename");
		cod.setOwner("somebody");
		cod.setDefaultGroupId("gid");
		cod.ok();
		
		progressInformationWaiting();
		
		CreateRepositoryDialog crd = knv.getOrgUnit(0, "clonename").createRepository();
		crd.setName("clonerepo");
		crd.cloneAnExistingRepository();
		crd.setRepositoryUrl(REPO_URL);
		crd.ok();
		
		progressInformationWaiting();
	
		RepositoryItem ri = knv.getRepository(0, "clonename", "clonerepo");
		ri.importRepository();
		ri.showInGitRepositoryView();
		
		Assert.assertEquals(true, new DefaultTree().getItems().get(0).getText().contains("clonerepo"));
	}
}
