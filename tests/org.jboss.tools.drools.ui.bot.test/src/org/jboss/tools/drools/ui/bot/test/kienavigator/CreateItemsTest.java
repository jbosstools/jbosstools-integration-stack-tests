package org.jboss.tools.drools.ui.bot.test.kienavigator;

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
import org.jboss.tools.drools.reddeer.kienavigator.item.ServerItem;
import org.jboss.tools.drools.reddeer.kienavigator.properties.OrgUnitProperties;
import org.jboss.tools.drools.reddeer.kienavigator.properties.ProjectProperties;
import org.jboss.tools.drools.reddeer.kienavigator.properties.RepositoryProperties;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(state = ServerRequirementState.RUNNING)
@RunWith(RedDeerSuite.class)
public class CreateItemsTest extends KieNavigatorTestParent {
	
	private static final Logger LOGGER = Logger.getLogger(CreateItemsTest.class);
	
	private static final String SPACE_NAME = "orgname",
			OWNER = "owner@email.com",
			GROUP_ID = "orggroupid",
			REPO_NAME = "reponame",
			REPO_USER = "repouser",
			PROJECT_NAME = "projectname",
			PROJECT_GID = "projectgid",
			PROJECT_AID = "projectaid",
			PROJECT_VERSION = "projectversion";

	@InjectRequirement
	private ServerRequirement serverReq;

	@Test
	public void createItemsTest() {
		ServerItem si = knv.getServers().get(0);

		CreateOrgUnitDialog cod = si.createOrgUnit();
		cod.setName(SPACE_NAME);
		cod.setOwner(OWNER);
		cod.setDefaultGroupId(GROUP_ID);
		cod.ok();

		progressInformationWaiting();

		OrgUnitProperties op = knv.getOrgUnit(0, SPACE_NAME).properties();
		Assert.assertEquals(SPACE_NAME, op.getOrganizationName());
		Assert.assertEquals(OWNER, op.getOwner());
		Assert.assertEquals(GROUP_ID, op.getDefaultGroupId());
		op.ok();

		CreateRepositoryDialog crd = knv.getOrgUnit(0, SPACE_NAME).createRepository();
		crd.setName(REPO_NAME);
		crd.setUsername(REPO_USER);
		crd.createNewRepository();
		crd.ok();

		progressInformationWaiting();

		RepositoryProperties rp = knv.getRepository(0, SPACE_NAME, REPO_NAME).properties();
		Assert.assertEquals(REPO_NAME, rp.getRepositoryName());
		Assert.assertEquals(SPACE_NAME, rp.getOrganizationalUnit());
		Assert.assertEquals("null", rp.getUserName());
		rp.ok();

		RepositoryItem ri = knv.getRepository(0, SPACE_NAME, REPO_NAME);
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

		ProjectProperties pp = knv.getProject(0, SPACE_NAME, REPO_NAME, PROJECT_NAME).properties();
		Assert.assertEquals(PROJECT_NAME, pp.getProjectName());
		Assert.assertEquals(REPO_NAME, pp.getRepository());
		Assert.assertEquals(PROJECT_GID, pp.getGroupId());
		Assert.assertEquals(PROJECT_VERSION, pp.getVersion());
		pp.ok();
	}
}
