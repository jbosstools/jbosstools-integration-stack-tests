package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Collection;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.common.reddeer.XPathEvaluator;
import org.jboss.tools.fuse.reddeer.ProjectType;
import org.jboss.tools.fuse.reddeer.SupportedCamelVersions;
import org.jboss.tools.fuse.reddeer.component.SAPIDocListServer;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.SAPRequirement.SAP;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

/**
 * Tests for checking a SAP version added by Camel editor.
 * 
 * @author apodhrad
 */
@SAP
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
public class SAPVersionTest {

	public static final String PROJECT_NAME = "sap-version";
	public static final ProjectType PROJECT_TYPE = ProjectType.SPRING;

	private String camelVersion;

	@Parameters(name = "{0}")
	public static Collection<String> getCamelVersions() {
		return SupportedCamelVersions.getCamelVersions();
	}

	public SAPVersionTest(String camelVersion) {
		this.camelVersion = camelVersion;
	}

	/**
	 * Deletes all projects
	 */
	@Before
	@After
	public void deleteAllProjects() {
		new WorkbenchShell();
		new ProjectExplorer().deleteAllProjects();
	}

	/**
	 * <p>
	 * Checks a sap version after adding a SAP component into the Camel editor (for all supported Camel versions).
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create an empty Fuse project based on Spring DSL</li>
	 * <li>open Project Explorer view and open the Camel XML file</li>
	 * <li>add a SAP component (namely 'sap-idoclist-server')</li>
	 * <li>save the Camel editor</li>
	 * <li>check the sap version in the file pom.xml</li>
	 * </ol>
	 */
	@Test
	public void testSAPVersion() throws Exception {
		if (camelVersion.equals(SupportedCamelVersions.CAMEL_2_15_1_REDHAT_621117)) {
			Assume.assumeTrue(new JiraClient().isIssueClosed("FUSETOOLS-2121"));
		}

		new WorkbenchShell();
		ProjectFactory.newProject(PROJECT_NAME).version(camelVersion).type(PROJECT_TYPE).create();
		new LogView().open();
		new LogView().deleteLog();

		new ProjectExplorer().open();
		new CamelProject(PROJECT_NAME).openCamelContext(PROJECT_TYPE.getCamelContext());
		new CamelEditor(PROJECT_TYPE.getCamelContext()).addCamelComponent(new SAPIDocListServer(), "Route _route1");
		new CamelEditor(PROJECT_TYPE.getCamelContext()).save();

		File pomFile = new File(new CamelProject(PROJECT_NAME).getFile(), "pom.xml");
		XPathEvaluator xpath = new XPathEvaluator(pomFile);
		String sapVersion = xpath.evaluateString("/project/dependencies/dependency[artifactId='camel-sap']/version");

		assertEquals("For Camel '" + camelVersion + "'", SupportedCamelVersions.getSAPVersion(camelVersion), sapVersion);
	}

}
