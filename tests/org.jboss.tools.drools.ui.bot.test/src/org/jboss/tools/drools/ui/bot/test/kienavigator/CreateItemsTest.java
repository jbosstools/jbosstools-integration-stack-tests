package org.jboss.tools.drools.ui.bot.test.kienavigator;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.PushButton;
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

@Server(type = {ServerReqType.EAP, ServerReqType.WildFly}, state = ServerReqState.RUNNING)
@RunWith(RedDeerSuite.class)
public class CreateItemsTest extends KieNavigatorTestParent {
	
	@InjectRequirement
	private ServerRequirement serverReq;
	
	@Test
	public void createItemsTest() {
		ServerItem si = knv.getServers().get(0);
		
		CreateOrgUnitDialog cod = si.createOrgUnit();
		cod.setName("orgname");
		cod.setDescription("orgdescr");
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
		crd.setDescription("repodescript");
		crd.setUsername("repouser");
		crd.createNewRepository();
		crd.ok();
		
		progressInformationWaiting();
		
		RepositoryProperties rp = knv.getRepository(0, "orgname", "reponame").properties();
		Assert.assertEquals("reponame", rp.getRepositoryName());
		Assert.assertEquals("orgname", rp.getOrganizationalUnit());
		Assert.assertEquals("repodescript", rp.getDescription());
		Assert.assertEquals("repouser", rp.getUserName());
		rp.ok();
		
		RepositoryItem ri = knv.getRepository(0, "orgname", "reponame");
		ri.importRepository();
		CreateProjectDialog cpd = ri.createProject();
		cpd.setName("projectname");
		cpd.setDescription("projectdecr");
		cpd.setGroupId("projectgid");
		cpd.setArtifactId("projectaid");
		cpd.setVersion("projectversion");
		cpd.importProjectToWorkspace(false);
		cpd.ok();
		
		progressInformationWaiting();
		
		new DefaultShell("Connect to Server");
		new PushButton("Yes").click(); // confirm dialog
		
		ProjectProperties pp = knv.getProject(0, "orgname", "reponame", "projectname").properties();		
		Assert.assertEquals("projectname", pp.getProjectName());
		Assert.assertEquals("reponame", pp.getRepository());
		Assert.assertEquals("projectdecr", pp.getDescription());
		Assert.assertEquals("projectgid", pp.getGroupId());
		Assert.assertEquals("projectversion", pp.getVersion());
		pp.ok();
	}
}
