package org.jboss.tools.drools.ui.bot.test.kienavigator;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateOrgUnitDialog;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.CreateRepositoryDialog;
import org.jboss.tools.drools.reddeer.kienavigator.item.RepositoryItem;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(state = ServerRequirementState.RUNNING)
@RunWith(RedDeerSuite.class)
public class CloneRepoTest extends KieNavigatorTestParent {
	
	private static final String SPACE_NAME = "clonename",
			OWNER = "somebody",
			GROUP_ID = "gid",
			REPO_NAME = "clonerepo";

	@InjectRequirement
	private ServerRequirement serverReq;

	@Test
	public void cloneRepoTest() {
		CreateOrgUnitDialog cod = knv.getServer(0).createOrgUnit();
		cod.setName(SPACE_NAME);
		cod.setOwner(OWNER);
		cod.setDefaultGroupId(GROUP_ID);
		cod.ok();

		progressInformationWaiting();

		CreateRepositoryDialog crd = knv.getOrgUnit(0, SPACE_NAME).createRepository();
		crd.setName(REPO_NAME);
		crd.cloneAnExistingRepository();
		crd.setRepositoryUrl(REPO_URL);
		crd.ok();

		progressInformationWaiting();

		RepositoryItem ri = knv.getRepository(0, SPACE_NAME, REPO_NAME);
		ri.importRepository();
		ri.showInGitRepositoryView();

		Assert.assertEquals(true, new DefaultTree().getItems().get(0).getText().contains(REPO_NAME));
	}
}
