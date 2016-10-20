package org.jboss.tools.drools.ui.bot.test.kienavigator;

import org.apache.log4j.Logger;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateOrgUnitDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateProjectDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.item.RepositoryItem;
import org.jboss.tools.drools.reddeer.kienavigator.item.ServerItem;
import org.jboss.tools.drools.reddeer.kienavigator.properties.OrgUnitProperties;
import org.jboss.tools.drools.reddeer.kienavigator.properties.ProjectProperties;
import org.jboss.tools.drools.reddeer.kienavigator.properties.RepositoryProperties;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(type = { ServerReqType.EAP, ServerReqType.WildFly }, state = ServerReqState.RUNNING)
@RunWith(RedDeerSuite.class)
public class CreateItemsTest extends KieNavigatorTestParent {
	
	private static final Logger LOGGER = Logger.getLogger(CreateItemsTest.class);

	@InjectRequirement
	private ServerRequirement serverReq;

	@Test
	public void createItemsTest() {
		ServerItem si = knv.getServers().get(0);

		CreateOrgUnitDialog cod = si.createOrgUnit();
		cod.setName("orgname");
		cod.setOwner("owner@email.com");
		cod.setDefaultGroupId("orggroupid");
		cod.ok();

		progressInformationWaiting();

		OrgUnitProperties op = knv.getOrgUnit(0, "orgname").properties();
		Assert.assertEquals("orgname", op.getOrganizationName());
		Assert.assertEquals("owner@email.com", op.getOwner());
		Assert.assertEquals("orggroupid", op.getDefaultGroupId());
		op.ok();

		CreateRepositoryDialog crd = knv.getOrgUnit(0, "orgname").createRepository();
		crd.setName("reponame");
		crd.setUsername("repouser");
		crd.createNewRepository();
		crd.ok();

		progressInformationWaiting();

		RepositoryProperties rp = knv.getRepository(0, "orgname", "reponame").properties();
		Assert.assertEquals("reponame", rp.getRepositoryName());
		Assert.assertEquals("orgname", rp.getOrganizationalUnit());
		Assert.assertEquals("null", rp.getUserName());
		rp.ok();

		RepositoryItem ri = knv.getRepository(0, "orgname", "reponame");
		ri.importRepository();
		CreateProjectDialog cpd = ri.createProject();
		cpd.setName("projectname");
		cpd.setGroupId("projectgid");
		cpd.setArtifactId("projectaid");
		cpd.setVersion("projectversion");
		cpd.importProjectToWorkspace(false);
		cpd.ok();

		progressInformationWaiting();

		try {
			new DefaultShell("Connect to Server");
			new YesButton().click();
		} catch (Exception e) {
			LOGGER.debug("Dialog window with 'Connect to Server is not visible.'");
		}

		ProjectProperties pp = knv.getProject(0, "orgname", "reponame", "projectname").properties();
		Assert.assertEquals("projectname", pp.getProjectName());
		Assert.assertEquals("reponame", pp.getRepository());
		Assert.assertEquals("projectgid", pp.getGroupId());
		Assert.assertEquals("projectversion", pp.getVersion());
		pp.ok();
	}
}
