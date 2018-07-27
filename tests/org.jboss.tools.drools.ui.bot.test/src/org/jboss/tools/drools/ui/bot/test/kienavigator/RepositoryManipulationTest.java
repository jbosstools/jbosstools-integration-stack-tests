package org.jboss.tools.drools.ui.bot.test.kienavigator;

import java.util.List;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateSpaceDialog;
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
	
	private static final String 
			SPACE_NAME = "repotest",
			REPO_NAME = "newrepo",
			OWNER = "owner";

	@InjectRequirement
	private ServerRequirement serverReq;

	@Test
	public void repositoryTest() {
		ServerItem si = knv.getServers().get(0);
		
		int numberOfSpaces = si.getSpaces().size();

		CreateSpaceDialog cod = si.createSpace();
		cod.setName(SPACE_NAME);
		cod.setOwner(OWNER);
		cod.ok();

		progressInformationWaiting();
		
		Assert.assertEquals(numberOfSpaces + 1, si.getSpaces().size());

		CreateRepositoryDialog crd = knv.getSpace(0, SPACE_NAME).createRepository();
		crd.setName(REPO_NAME);
		crd.ok();

		progressInformationWaiting();

		List<RepositoryItem> riList = knv.getSpace(0, SPACE_NAME).getRepositories();
		Assert.assertEquals(1, riList.size());
		Assert.assertEquals(REPO_NAME, riList.get(0).getName());

		knv.getRepository(0, SPACE_NAME, REPO_NAME).deleteRepository().ok();

		progressInformationWaiting();
		
		riList = knv.getSpace(0, SPACE_NAME).getRepositories();
		Assert.assertEquals(0, riList.size());
		
		progressInformationWaiting();

		knv.getSpace(0, SPACE_NAME).deleteSpace().ok();
		
		progressInformationWaiting();

		Assert.assertEquals(numberOfSpaces, si.getSpaces().size());
	}
}
