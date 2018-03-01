package org.jboss.tools.drools.ui.bot.test.kienavigator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.button.YesButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateOrgUnitDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateProjectDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.item.RepositoryItem;
import org.jboss.tools.drools.reddeer.kienavigator.structure.OrganizationalUnit;
import org.jboss.tools.drools.reddeer.kienavigator.structure.Project;
import org.jboss.tools.drools.reddeer.kienavigator.structure.Repository;
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
	
	private static final Logger LOGGER = Logger.getLogger(CreateItemsRestTest.class);
	
	private static final String SPACE_NAME = "restname",
			OWNER = "rest@drools.org",
			GROUP_ID = "restgroupid",
			REPO_1 = "restreponame",
			REPO_2 = "restreponame2",
			PROJECT_NAME = "restprojectname",
			PROJECT_GID = "resrprojectgid",
			USER_1 = "restrepouser",
			USER_2 = "restrepouser2",
			PROJECT_VERSION = "restprojectversion",
			PROJECT_AID = "restprojectaid",
			GIT_URL = "git://restreponame";

	@InjectRequirement
	private ServerRequirement serverReq;

	@Test
	public void createItemsRestTest() throws MalformedURLException, IOException {
		initServerStructure(knv);

		OrganizationalUnit ou = RestClient.getOrganizationalUnit(SPACE_NAME);
		Assert.assertEquals(SPACE_NAME, ou.getName());
		Assert.assertEquals(OWNER, ou.getOwner());
		Assert.assertEquals(GROUP_ID, ou.getGroupId());

		List<String> repoList = ou.getRepositories();
		Assert.assertEquals(2, repoList.size());
		Assert.assertEquals(REPO_1, repoList.get(0));
		Assert.assertEquals(REPO_2, repoList.get(1));

		Repository repo = RestClient.getRepository(REPO_1);
		Assert.assertEquals(REPO_1, repo.getName());
		Assert.assertEquals("null", repo.getUsername());

		Repository repo2 = RestClient.getRepository(REPO_2);
		Assert.assertEquals(REPO_2, repo2.getName());
		Assert.assertEquals("null", repo2.getUsername());
		Assert.assertEquals(GIT_URL, repo.getGitUrl());

		List<Project> projectList = RestClient.getProjects(REPO_1);
		Assert.assertEquals(1, projectList.size());
		Project project = projectList.get(0);
		Assert.assertEquals(PROJECT_NAME, project.getName());
		Assert.assertEquals(PROJECT_GID, project.getGroupId());
		Assert.assertEquals(PROJECT_VERSION, project.getVersion());

		List<Project> projectList2 = RestClient.getProjects(REPO_2);
		Assert.assertEquals(1, projectList2.size());
	}

	private void initServerStructure(KieNavigatorView knv) {
		CreateOrgUnitDialog cod = knv.getServer(0).createOrgUnit();
		cod.setName(SPACE_NAME);
		cod.setOwner(OWNER);
		cod.setDefaultGroupId(GROUP_ID);
		cod.ok();

		progressInformationWaiting();

		CreateRepositoryDialog crd = knv.getOrgUnit(0, SPACE_NAME).createRepository();
		crd.setName(REPO_1);
		crd.setUsername(USER_1);
		crd.createNewRepository();
		crd.ok();

		progressInformationWaiting();

		CreateRepositoryDialog crd2 = knv.getOrgUnit(0, SPACE_NAME).createRepository();
		crd2.setName(REPO_2);
		crd2.setUsername(USER_2);
		crd2.cloneAnExistingRepository();
		crd2.setRepositoryUrl(REPO_URL);
		crd2.ok();

		progressInformationWaiting();

		RepositoryItem ri = knv.getRepository(0, SPACE_NAME, REPO_1);
		ri.importRepository();
		CreateProjectDialog cpd = ri.createProject();
		cpd.setName(PROJECT_NAME);
		cpd.setGroupId(PROJECT_GID);
		cpd.setArtifactId(PROJECT_AID);
		cpd.setVersion(PROJECT_VERSION);
		cpd.importProjectToWorkspace(false);
		cpd.ok();

		progressInformationWaiting();

		try {
			new DefaultShell("Connect to Server");
			new YesButton().click();
		} catch (Exception e) {
			LOGGER.debug("Dialog window with 'Connect to Server is not visible.'");
		}
	}
}
