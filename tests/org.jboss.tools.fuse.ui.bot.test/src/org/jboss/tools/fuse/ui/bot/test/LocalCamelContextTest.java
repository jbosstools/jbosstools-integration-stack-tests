package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tries to run projects from all available archetypes as Local Camel Context
 * 
 * @author tsedmik
 */
@RunWith(RedDeerSuite.class)
public class LocalCamelContextTest extends DefaultTest {

	private static List<String> archetypes;
	private List<String> problems = new ArrayList<String>();

	private static Logger log = Logger.getLogger(LocalCamelContextTest.class);

	@BeforeClass
	public static void initListOfArchetypes() {

		archetypes = ProjectFactory.getArchetypes();
	}

	@Test
	public void testRunAsLocalCamelContext() throws FuseArchetypeNotFoundException {

		for (String archetype : archetypes) {

			log.info("Creating a new project from '" + archetype + "' archetype");
			ProjectFactory.createProject(archetype, archetype);
			try {
				log.info("Trying to run the project as Local Camel Context");
				new CamelProject(archetype).runCamelContext();
				ConsoleView console = new ConsoleView();
				if (console.getConsoleText().contains("BUILD FAILURE")
						|| console.getConsoleText().toLowerCase().contains("error") || console.consoleIsTerminated()) {
					log.warn("There is a problem with building '" + archetype + "' project");
					problems.add(archetype + " - build error");
				}
			} catch (IndexOutOfBoundsException e) {
				log.warn("There is no Camel Context file in '" + archetype + "' project");
				problems.add(archetype + " - no Camel Context file available");
			} catch (WaitTimeoutExpiredException e) {
				log.warn("There is a problem with building '" + archetype + "' project");
				problems.add(archetype + " - build error");
			} catch (Exception e) {
				log.warn("There is a problem with creating '" + archetype + "' project");
				problems.add(archetype + " - project creation error");
			} finally {
				defaultClean();
				new ProjectExplorer().deleteAllProjects();
			}
		}

		if (!problems.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (String item : problems) {
				sb.append(item);
				sb.append("\n");
			}
			assertFalse("There are the following problems:\n" + sb, sb.toString().contains("error"));
		}
	}
}
