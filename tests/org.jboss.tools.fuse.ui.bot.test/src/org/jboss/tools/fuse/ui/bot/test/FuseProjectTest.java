package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class FuseProjectTest extends DefaultTest {

	protected Logger log = Logger.getLogger(FuseProjectTest.class);

	@Test
	public void camelCxfCodeTest() {

		try {
			ProjectFactory.createProject("test", "camel-cxf-code-first-archetype");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelCxfContractTest() {

		try {
			ProjectFactory.createProject("test", "camel-cxf-contract-first-archetype");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelDroolsTest() {

		try {
			ProjectFactory.createProject("test", "camel-drools-archetype");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelWebServiceTest() {

		try {
			ProjectFactory.createProject("test", "camel-webservice-archetype");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelActiveMQTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-activemq");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelAPIComponentTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-api-component");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelBlueprintTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-blueprint");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelComponentTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-component");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelCxfCodeFirstBlueprint() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-cxf-code-first-blueprint");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelCxfContractFirstBlueprint() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-cxf-contract-first-blueprint");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelDataFormatTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-dataformat");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelJavaTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-java");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelSCRTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-scr");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelSpringTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-spring");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelSpringDMTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-spring-dm");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelWARTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-war");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelWebTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-web");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelWebConsoleTest() {

		try {
			ProjectFactory.createProject("test", "camel-archetype-webconsole");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelCxfJaxRSTest() {

		try {
			ProjectFactory.createProject("test", "cxf-jaxrs-service");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	@Test
	public void camelCxfJaxWSTest() {

		try {
			ProjectFactory.createProject("test", "cxf-jaxws-javafirst");
		} catch (FuseArchetypeNotFoundException e) {
			fail("Archetype is not available");
		}
		assertTrue("Project is not present in Project Explorer", isPresent());
		assertFalse("Project was created with errors", hasErrors());
	}

	private boolean hasErrors() {

		new ProblemsView().open();
		for (TreeItem item : new DefaultTree().getItems()) {
			if (item.getText().toLowerCase().contains("error")) return true;
		}
		return false;
	}

	private boolean isPresent() {

		return new ProjectExplorer().containsProject("test");
	}

	@After
	public void clean() throws Exception {

		new WorkbenchShell();
		new ProjectExplorer().deleteAllProjects();
		new WorkbenchShell();
	}
}
