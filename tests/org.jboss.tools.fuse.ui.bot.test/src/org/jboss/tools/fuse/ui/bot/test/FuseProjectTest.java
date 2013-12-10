package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.hamcrest.Matcher;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.matcher.TextMatcher;
import org.jboss.reddeer.swt.reference.ReferencedComposite;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.wizard.FuseProjectWizard;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 * 
 */
public class FuseProjectTest extends RedDeerTest {

	protected Logger log = Logger.getLogger(FuseProjectTest.class);

	@Test
	public void camelActiveMQTest() {
		createProject("camel-archetype-activemq");
	}

	@Test
	public void camelBlueprintTest() {
		createProject("camel-archetype-blueprint");
	}

	@Test
	public void camelComponentTest() {
		createProject("camel-archetype-component");
	}
	
	@Test
	public void camelCxfCodeFirstBlueprint() {
		createProject("camel-archetype-cxf-code-first-blueprint");
	}
	
	@Test
	public void camelCxfContractFirstBlueprint() {
		createProject("camel-archetype-cxf-contract-first-blueprint");
	}

	@Test
	public void camelDataFormatTest() {
		createProject("camel-archetype-dataformat");
	}

	@Test
	public void camelJavaTest() {
		createProject("camel-archetype-java");
	}

	@Test
	public void camelSpringTest() {
		createProject("camel-archetype-spring");
	}

	@Test
	public void camelSpringDMTest() {
		createProject("camel-archetype-spring-dm");
	}

	@Test
	public void camelWebTest() {
		createProject("camel-archetype-web");
	}

	@Test
	public void camelWebConsoleTest() {
		createProject("camel-archetype-webconsole");
	}

	@Test
	public void camelCxfJaxRSTest() {
		createProject("cxf-jaxrs-service");
	}

	@Test
	public void camelCxfJaxWSTest() {
		createProject("cxf-jaxws-javafirst");
	}

	@Test
	public void camelCxfCodeTest() {
		createProject("camel-cxf-code-first-archetype");
	}

	@Test
	public void camelCxfContractTest() {
		createProject("camel-cxf-contract-first-archetype");
	}

	@Test
	public void camelDroolsTest() {
		createProject("camel-drools-archetype");
	}

	@Test
	public void camelWebServiceTest() {
		createProject("camel-webservice-archetype");
	}

	private void createProject(String archetype) {
		FuseProjectWizard projectWizard = new FuseProjectWizard();
		projectWizard.open();
		projectWizard.next();
		projectWizard.setFilter(archetype);
		projectWizard.selectFirstArchetype();
		projectWizard.finish();

		AbstractWait.sleep(10 * 1000);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		List<TreeItem> errors = problemsView.getAllErrors();

		if (!errors.isEmpty()) {
			ProjectExplorer projectExplorer = new ProjectExplorer();
			projectExplorer.open();
			projectExplorer.getProjects().get(0).select();
			new ContextMenu("Maven", "Update Project...").select();
			new DefaultShell("Update Maven Project");
			new CheckBox("Force Update of Snapshots/Releases").toggle(true);
			new PushButton("OK").click();
			new WaitWhile(new JobIsRunning());
		}

		problemsView.open();
		errors = problemsView.getAllErrors();
		assertTrue("After creating the project '" + archetype
				+ "' there are the following errors:\n" + toString(errors), errors.isEmpty());
	}

	@After
	public void deleteAllProjects() throws Exception {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		for (Project project : projectExplorer.getProjects()) {
			try {
				project.delete(true);
			} catch (WaitTimeoutExpiredException e1) {
				clickContinue();
				new WaitWhile(new ShellWithTextIsActive("Delet Resources"),TimePeriod.LONG);
			}
		}
	}

	/**
	 * Clicks on Continue button when deleting a project which is not in sync
	 */
	protected void clickContinue() {
		List<Shell> list = Display.syncExec(new ResultRunnable<List<Shell>>() {

			@Override
			public List<Shell> run() {
				Matcher<String> matcher = new TextMatcher("Delete Resources");
				List<Shell> list = new ArrayList<Shell>();
				Shell[] shell = Display.getDisplay().getShells();
				for (int i = 0; i < shell.length; i++) {
					if (matcher.matches(shell[i])) {
						list.add(shell[i]);
					}
				}
				return list;
			}
		});
		SWTLayerException sle = null;
		for (final Shell sh : list) {
			ReferencedComposite ref = new ReferencedComposite() {

				@Override
				public Control getControl() {
					return sh;
				}

			};
			try {
				new PushButton(ref, "Continue").click();
				return;
			} catch (SWTLayerException e) {
				sle = e;
			}
		}
		if (sle != null) {
			throw sle;
		}
	}

	private String toString(List<TreeItem> items) {
		StringBuffer result = new StringBuffer();
		for (TreeItem item : items) {
			result.append(item.getText());
			result.append("\n");
		}
		return result.toString();
	}

}
