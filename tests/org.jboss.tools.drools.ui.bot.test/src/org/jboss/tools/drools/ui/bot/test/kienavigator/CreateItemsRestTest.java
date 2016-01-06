package org.jboss.tools.drools.ui.bot.test.kienavigator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateOrgUnitDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateProjectDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.item.RepositoryItem;
import org.jboss.tools.drools.reddeer.kienavigator.structure.OrganizationalUnit;
import org.jboss.tools.drools.reddeer.kienavigator.structure.Project;
import org.jboss.tools.drools.reddeer.kienavigator.structure.Repository;
import org.jboss.tools.drools.reddeer.view.KieNavigatorView;
import org.jboss.tools.drools.ui.bot.test.util.RestClient;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(type = { ServerReqType.EAP, ServerReqType.WildFly }, state = ServerReqState.RUNNING)
@RunWith(RedDeerSuite.class)
public class CreateItemsRestTest extends KieNavigatorTestParent {

	@InjectRequirement
	private ServerRequirement serverReq;

	@Test
	public void createItemsRestTest() throws MalformedURLException, IOException {
		initServerStructure(knv);

		OrganizationalUnit ou = RestClient.getOrganizationalUnit("restname");
		Assert.assertEquals("restname", ou.getName());
		Assert.assertEquals("restdescr", ou.getDescription());
		Assert.assertEquals("rest@drools.org", ou.getOwner());
		Assert.assertEquals("restgroupid", ou.getGroupId());

		List<String> repoList = ou.getRepositories();
		Assert.assertEquals(2, repoList.size());
		Assert.assertEquals("restreponame", repoList.get(0));
		Assert.assertEquals("restreponame2", repoList.get(1));

		Repository repo = RestClient.getRepository("restreponame");
		Assert.assertEquals("restreponame", repo.getName());
		Assert.assertEquals("restrepodescript", repo.getDescription());
		Assert.assertEquals("restrepouser", repo.getUsername());

		Repository repo2 = RestClient.getRepository("restreponame2");
		Assert.assertEquals("restreponame2", repo2.getName());
		Assert.assertEquals("restrepodescript2", repo2.getDescription());
		Assert.assertEquals("restrepouser2", repo2.getUsername());
		Assert.assertEquals("git://restreponame2", repo.getGitUrl());

		List<Project> projectList = RestClient.getProjects("restreponame");
		Assert.assertEquals(1, projectList.size());
		Project project = projectList.get(0);
		Assert.assertEquals("restprojectname", project.getName());
		Assert.assertEquals("restprojectdescr", project.getDescription());
		Assert.assertEquals("resrprojectgid", project.getGroupId());
		Assert.assertEquals("restprojectversion", project.getVersion());

		List<Project> projectList2 = RestClient.getProjects("restreponame2");
		Assert.assertEquals(4, projectList2.size());
		Assert.assertEquals("helloworld-brms-kmodule", projectList2.get(0).getName());
		Assert.assertEquals("bpms-project", projectList2.get(1).getName());
		Assert.assertEquals("my-store-brms-kmodule", projectList2.get(2).getName());
		Assert.assertEquals("decision-table-kmodule", projectList2.get(3).getName());
	}

	private void initServerStructure(KieNavigatorView knv) {
		CreateOrgUnitDialog cod = knv.getServer(0).createOrgUnit();
		cod.setName("restname");
		cod.setDescription("restdescr");
		cod.setOwner("rest@drools.org");
		cod.setDefaultGroupId("restgroupid");
		cod.ok();

		progressInformationWaiting();

		CreateRepositoryDialog crd = knv.getOrgUnit(0, "restname").createRepository();
		crd.setName("restreponame");
		crd.setDescription("restrepodescript");
		crd.setUsername("restrepouser");
		crd.createNewRepository();
		crd.ok();

		progressInformationWaiting();

		CreateRepositoryDialog crd2 = knv.getOrgUnit(0, "restname").createRepository();
		crd2.setName("restreponame2");
		crd2.setDescription("restrepodescript2");
		crd2.setUsername("restrepouser2");
		crd2.cloneAnExistingRepository();
		crd2.setRepositoryUrl(REPO_URL);
		crd2.ok();

		progressInformationWaiting();

		RepositoryItem ri = knv.getRepository(0, "restname", "restreponame");
		ri.importRepository();
		CreateProjectDialog cpd = ri.createProject();
		cpd.setName("restprojectname");
		cpd.setDescription("restprojectdescr");
		cpd.setGroupId("resrprojectgid");
		cpd.setArtifactId("restprojectaid");
		cpd.setVersion("restprojectversion");
		cpd.importProjectToWorkspace(false);
		cpd.ok();

		progressInformationWaiting();

		new DefaultShell("Connect to Server");
		new YesButton().click();
	}
}
