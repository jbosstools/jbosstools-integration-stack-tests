package org.jboss.tools.drools.ui.bot.test.kienavigator;

import java.util.List;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.AddRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateOrgUnitDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.item.RepositoryItem;
import org.jboss.tools.drools.reddeer.kienavigator.item.ServerItem;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(state = ServerRequirementState.RUNNING)
@RunWith(RedDeerSuite.class)
public class RepositoryManipulationTest extends KieNavigatorTestParent {
	
	private static final String SPACE_NAME = "repotest",
			REPO_NAME = "newrepo",
			OWNER = "owner";

	@InjectRequirement
	private ServerRequirement serverReq;

	@Test
	public void repositoryTest() {
		ServerItem si = knv.getServers().get(0);

		CreateOrgUnitDialog cod = si.createOrgUnit();
		cod.setName(SPACE_NAME);
		cod.setOwner(OWNER);
		cod.ok();

		progressInformationWaiting();

		CreateRepositoryDialog crd = knv.getOrgUnit(0, SPACE_NAME).createRepository();
		crd.setName(REPO_NAME);
		crd.ok();

		progressInformationWaiting();

		knv.getRepository(0, SPACE_NAME, REPO_NAME).removeRepository().yes();

		progressInformationWaiting();

		AddRepositoryDialog ard = knv.getOrgUnit(0, SPACE_NAME).addRepository();
		ard.selectRepository(REPO_NAME);
		ard.ok();

		progressInformationWaiting();

		List<RepositoryItem> riList = knv.getOrgUnit(0, SPACE_NAME).getRepositories();
		Assert.assertEquals(1, riList.size());
		Assert.assertEquals(REPO_NAME, riList.get(0).getName());
	}
}
