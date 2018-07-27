package org.jboss.tools.drools.ui.bot.test.kienavigator;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateSpaceDialog;
import org.jboss.tools.drools.reddeer.kienavigator.item.RepositoryItem;
import org.jboss.tools.drools.reddeer.kienavigator.item.ServerItem;
import org.jboss.tools.drools.reddeer.kienavigator.properties.ProjectProperties;
import org.jboss.tools.drools.reddeer.kienavigator.properties.RepositoryProperties;
import org.jboss.tools.drools.reddeer.kienavigator.properties.SpaceProperties;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(state = ServerRequirementState.RUNNING)
@RunWith(RedDeerSuite.class)
public class CreateItemsTest extends KieNavigatorTestParent {
	
	private static final String 
			SPACE_NAME = "orgname",
			OWNER = "owner@email.com",
			GROUP_ID = "orggroupid",
			REPO_NAME = "reponame",
			PROJECT_GID = "projectgid",
			PROJECT_VERSION = "projectversion",
			DESCRIPTION = "some description";

	@InjectRequirement
	private ServerRequirement serverReq;

	@Test
	public void createItemsTest() {
		ServerItem si = knv.getServers().get(0);

		CreateSpaceDialog cod = si.createSpace();
		cod.setName(SPACE_NAME);
		cod.setOwner(OWNER);
		cod.setDefaultGroupId(GROUP_ID);
		cod.ok();

		progressInformationWaiting();

		SpaceProperties op = knv.getSpace(0, SPACE_NAME).properties();
		Assert.assertEquals(SPACE_NAME, op.getSpaceName());
		op.ok();

		CreateRepositoryDialog crd = knv.getSpace(0, SPACE_NAME).createRepository();
		crd.setName(REPO_NAME);
		crd.setGroupId(PROJECT_GID);
		crd.setVersion(PROJECT_VERSION);
		crd.setDescription(DESCRIPTION);
		crd.ok();

		progressInformationWaiting();

		RepositoryProperties rp = knv.getRepository(0, SPACE_NAME, REPO_NAME).properties();
		Assert.assertEquals(REPO_NAME, rp.getRepositoryName());
		Assert.assertEquals(SPACE_NAME, rp.getSpace());
		Assert.assertEquals(DESCRIPTION, rp.getDescription());
		rp.ok();

		RepositoryItem ri = knv.getRepository(0, SPACE_NAME, REPO_NAME);
		ri.importRepository();

		ProjectProperties pp = knv.getProject(0, SPACE_NAME, REPO_NAME, REPO_NAME).properties();
		Assert.assertEquals(REPO_NAME, pp.getProjectName());
		Assert.assertEquals(REPO_NAME, pp.getRepository());
		Assert.assertEquals(DESCRIPTION, pp.getDescription());
		Assert.assertEquals(PROJECT_GID, pp.getGroupId());
		Assert.assertEquals(PROJECT_VERSION, pp.getVersion());
		pp.ok();
	}
}
