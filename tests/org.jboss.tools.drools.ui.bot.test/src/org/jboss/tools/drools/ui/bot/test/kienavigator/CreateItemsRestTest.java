package org.jboss.tools.drools.ui.bot.test.kienavigator;

import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateSpaceDialog;
import org.jboss.tools.drools.reddeer.kienavigator.structure.Project;
import org.jboss.tools.drools.reddeer.kienavigator.structure.Space;
import org.jboss.tools.drools.reddeer.view.KieNavigatorView;
import org.jboss.tools.drools.ui.bot.test.util.RestClient;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(state = ServerRequirementState.RUNNING)
@RunWith(RedDeerSuite.class)
public class CreateItemsRestTest extends KieNavigatorTestParent {
		
	private static final String 
			SPACE_NAME = "restname",
			SECOND_SPACE_NAME = "secondrestspace",
			OWNER_1 = "rest@drools.org",
			OWNER_2 = "rest@planner.org",
			REPO_1 = "restreponame1",
			REPO_2 = "restreponame2",
			REPO_3 = "restreponame3";

	@InjectRequirement
	private ServerRequirement serverReq;

	@Test
	public void createItemsRestTest() throws MalformedURLException, IOException {
		initServerStructure(knv);
		
		final Space firstSpace = RestClient.getSpace(SPACE_NAME);
		Assert.assertEquals(SPACE_NAME, firstSpace.getName());
		Assert.assertEquals(OWNER_1, firstSpace.getOwner());
		
		final Space secondSpace = RestClient.getSpace(SECOND_SPACE_NAME);
		Assert.assertEquals(SECOND_SPACE_NAME, secondSpace.getName());
		Assert.assertEquals(OWNER_2, secondSpace.getOwner());

		final Project[] projects = RestClient.getProjects(SPACE_NAME);
		Assert.assertEquals(2, projects.length);
		
		Assert.assertEquals(REPO_1, projects[0].getName());
		Assert.assertEquals(SPACE_NAME, projects[0].getSpaceName());
		
		Assert.assertEquals(REPO_2, projects[1].getName());
		Assert.assertEquals(SPACE_NAME, projects[1].getSpaceName());
		
		final Project[] projectsSpace2 = RestClient.getProjects(SECOND_SPACE_NAME);
		Assert.assertEquals(1, projectsSpace2.length);
		
		Assert.assertEquals(REPO_3, projectsSpace2[0].getName());
		Assert.assertEquals(SECOND_SPACE_NAME, projectsSpace2[0].getSpaceName());
	}

	private void initServerStructure(final KieNavigatorView knv) {
		final CreateSpaceDialog csd1 = knv.getServer(0).createSpace();
		csd1.setName(SPACE_NAME);
		csd1.setOwner(OWNER_1);
		csd1.ok();

		progressInformationWaiting();

		final CreateRepositoryDialog crd1 = knv.getSpace(0, SPACE_NAME).createRepository();
		crd1.setName(REPO_1);
		crd1.ok();

		progressInformationWaiting();
		
		final CreateRepositoryDialog crd2 = knv.getSpace(0, SPACE_NAME).createRepository();
		crd2.setName(REPO_2);
		crd2.ok();

		progressInformationWaiting();
		
		final CreateSpaceDialog csd2 = knv.getServer(0).createSpace();
		csd2.setName(SECOND_SPACE_NAME);
		csd2.setOwner(OWNER_2);
		csd2.ok();
		
		progressInformationWaiting();

		final CreateRepositoryDialog crd3 = knv.getSpace(0, SECOND_SPACE_NAME).createRepository();
		crd3.setName(REPO_3);
		crd3.ok();

		progressInformationWaiting();
	}
}
