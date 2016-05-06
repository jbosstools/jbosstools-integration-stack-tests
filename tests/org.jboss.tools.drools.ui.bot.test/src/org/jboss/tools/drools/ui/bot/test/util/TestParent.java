package org.jboss.tools.drools.ui.bot.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.core.lookup.ShellLookup;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.junit.screenshot.CaptureScreenshotException;
import org.jboss.reddeer.junit.screenshot.ScreenshotCapturer;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage.DroolsRuntime;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWithExamplesWizardPage;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWizard;
import org.jboss.tools.drools.ui.bot.test.Activator;
import org.jboss.tools.drools.ui.bot.test.annotation.Drools5Runtime;
import org.jboss.tools.drools.ui.bot.test.annotation.Drools6Runtime;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction")
@RunWith(RedDeerSuite.class)
public abstract class TestParent {
	private static final Logger LOGGER = Logger.getLogger(TestParent.class);
	private static final File SCREENSHOT_DIR = new File("screenshots");
	private static final String DEBUG_REGEX = "(SLF4J: .*[\r\n]+)+?" + "(kmodules: file:(/.*)+/kmodule.xml[\r\n]+)?";
	private static final String SUCCESSFUL_RUN_REGEX = DEBUG_REGEX + "Hello World[\r\n]+Goodbye cruel world[\r\n]+";
	protected static final String DEFAULT_DROOLS_RUNTIME_NAME = "defaultTestRuntime";
	public static final String DEFAULT_PROJECT_NAME = "defaultTestProject";

	private static final AtomicBoolean initialized = new AtomicBoolean(false);

	private final RuntimeVersion runtimeVersion;

	public TestParent() {
		this.runtimeVersion = RuntimeVersion.UNDEFINED;
	}

	@Rule
	public TestName name = new TestName();

	@ClassRule
	public static TestWatcher classWatcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			LOGGER.info(String.format("%n%n%25s Starting [%s]%n", "", description.getClassName()));
		}
	};

	@Rule
	public TestWatcher resultWatcher = new TestWatcher() {
		protected void starting(Description description) {
			LOGGER.info(String.format("==== %s ====", description.getMethodName()));
		};

		protected void succeeded(Description description) {
			LOGGER.info(String.format("succeded %s - %s", description.getClassName(), description.getMethodName()));
		};

		protected void skipped(AssumptionViolatedException e, Description description) {
			LOGGER.info(String.format("skipped %s - %s", description.getClassName(), description.getMethodName()));
		}

		protected void failed(Throwable e, Description description) {
			LOGGER.warn(String.format("failed %s - %s", description.getClassName(), description.getMethodName()));
		};
	};

	@BeforeClass
	public static void closeStartUpDialogsAndViews() {
		if (!initialized.getAndSet(true)) {
			try {
				new DefaultShell("JBoss Developer Studio Usage");
				new PushButton("No").click();
			} catch (Exception ex) {
				LOGGER.debug("JBoss Tools Usage dialog was not found.");
			}

			try {
				new WorkbenchView("Welcome") {
				}.close();
			} catch (Exception ex) {
				LOGGER.debug("Eclipse Welcome view not found.");
			}

			try {
				new DefaultEditor("JBoss Central").close();
			} catch (Exception ex) {
				LOGGER.debug("JBoss Central editor was not found.");
			}

			// maximizes the window
			final org.eclipse.swt.widgets.Shell shell = ShellLookup.getInstance().getActiveShell();
			Display.syncExec(new Runnable() {
				public void run() {
					shell.setMaximized(true);
				}
			});
		}
	}

	@Before
	public void setUpEnvironment() {
		// first set up the correct perspective
		UsePerspective def = getAnnotationOnMethod(getMethodName(), UsePerspective.class);
		boolean opened = false;
		try {
			if (def != null) {
				def.value().newInstance().open();
				opened = true;
			}
		} catch (InstantiationException ex) {
			LOGGER.error("Unable to instantiate perspective", ex);
		} catch (IllegalAccessException ex) {
			LOGGER.error("Unable to instantiate perspective", ex);
		}
		if (!opened) {
			new JavaPerspective().open();
		}

		// setup default project
		String methodName = getMethodName();
		if (getAnnotationOnMethod(methodName, UseDefaultProject.class) != null) {
			createDroolsProjectWithSamples(DEFAULT_PROJECT_NAME);
		}
	}

	@After
	public void cleanUp() {
		// take screenshot before cleaning up
		takeScreenshot(String.format("%s-%s", getClass().getName(), getTestName()));

		closeAllDialogs();

		// save and close editors
		while (true) {
			try {
				new DefaultEditor().save();
				new DefaultEditor().close();
			} catch (Exception ex) {
				break;
			}
		}

		// switch to Java perspective
		new JavaPerspective().open();

		// refresh and delete all projects (as running the projects creates logs)
		PackageExplorer pe = new PackageExplorer();
		pe.deleteAllProjects();
		pe.deleteAllProjects(); // BZ 1225885

		ConsoleView console = new ConsoleView();
		console.open();
		try {
			// FIXME uncomment once my pull request is applied
			// console.removeAllTerminatedLaunches();
			new DefaultToolItem("Remove All Terminated Launches").click();
		} catch (Exception ex) {
			LOGGER.debug("Console was not cleared", ex);
		}
	}

	protected void deleteAllRuntimes() {
		DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
		pref.open();
		for (DroolsRuntime runtime : pref.getDroolsRuntimes()) {
			pref.removeDroolsRuntime(runtime.getName());
		}
		pref.okCloseWarning();
	}

	protected String createTempDir(String name) {
		File dir = new File("tmp", name);
		dir.mkdirs();

		return dir.getAbsolutePath();
	}

	protected static String getTemplateText(String templateName) {
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		StringWriter w = new StringWriter();
		BufferedReader br = null;
		PrintWriter pw = null;
		try {
			br = new BufferedReader(new InputStreamReader(bundle.getResource(templateName).openStream()));
			pw = new PrintWriter(w);
			String l;
			while ((l = br.readLine()) != null) {
				pw.println(l);
			}
		} catch (IOException ex) {
			LOGGER.error("Unable to close template stream", ex);
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				throw new RuntimeException("Error closing BufferedReader", ex);
			}
			if (pw != null)
				pw.close();
		}

		return w.toString();
	}

	/**
	 * Takes the screenshot of Eclipse. It is stored in screenshot directory. The file name follows pattern
	 * <code>001-${name}.png</code>.
	 * 
	 * @param name
	 *            name of the screenshot (needs not be unique)
	 */
	protected static void takeScreenshot(String name) {
		if (!SCREENSHOT_DIR.exists()) {
			SCREENSHOT_DIR.mkdirs();
		}

		int index = SCREENSHOT_DIR.list().length;
		File screenshotFile = new File(SCREENSHOT_DIR, String.format("%03d-%s.png", index, name));
		try {
			ScreenshotCapturer.getInstance().captureScreenshot(screenshotFile.getAbsolutePath());
		} catch (CaptureScreenshotException e) {
			e.printStackTrace();
		}
	}

	private <T extends Annotation> T getAnnotationOnMethod(String methodName, Class<T> annotationClass) {
		Method m = null;
		try {
			m = getClass().getMethod(methodName);
		} catch (NoSuchMethodException ex) {
			return null;
		}
		return m.getAnnotation(annotationClass);
	}

	/**
	 * Try not to use thread sleep to wait for events (there has to be a better way)
	 */
	protected void waitASecond() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}
	}

	protected void closeAllDialogs() {
		// press as many "Cancel" buttons as possible to clear out the shells
		while (true) {
			try {
				new PushButton("Cancel").click();
			} catch (Exception ex) {
				break;
			}
		}
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}
	
	protected void createDroolsProjectWithSamples(String projectName) {
		NewDroolsProjectWizard newDroolsProjectWizard = new NewDroolsProjectWizard();
		newDroolsProjectWizard.open();
		newDroolsProjectWizard.getFirstPage().selectProjectWithExamples();
		newDroolsProjectWizard.next();
		NewDroolsProjectWithExamplesWizardPage pageWithExamples = newDroolsProjectWizard.getProjectWithExamplesPage();
		pageWithExamples.setProjectName(projectName);
		pageWithExamples.setUseDefaultLocation(true);
		pageWithExamples.checkAll();
		pageWithExamples.useRuntime();
		Assert.assertTrue("No runtime is installed.", pageWithExamples.getInstalledRuntimes().size() > 0);
		newDroolsProjectWizard.finish();
		new WaitWhile(new JobIsRunning());
	}

	protected String getResourcesLocation() {
		switch (getUsedVersion()) {
		case BRMS_5:
			return "src/main/rules";
		case BRMS_6:
			return "src/main/resources";
		default:
			return null;
		}
	}

	protected String getRulesLocation() {
		return getRulesLocation(DEFAULT_PROJECT_NAME);
	}

	protected String getRulesLocation(String projectName) {
		StringBuilder sb = new StringBuilder();

		sb.append(projectName).append("/").append(getResourcesLocation());
		if (getUsedVersion() == RuntimeVersion.BRMS_6) {
			sb.append("/").append("rules");
		}

		return sb.toString();
	}

	protected String[] getResourcePath(String resourceName) {
		return getResourcePath("rules", resourceName);
	}

	protected String[] getResourcePath(String packageName, String resourceName) {
		List<String> result = new LinkedList<String>();

		result.add(getResourcesLocation());
		if (getUsedVersion() == RuntimeVersion.BRMS_6) {
			result.add(packageName);
		}
		result.add(resourceName);

		return result.toArray(new String[result.size()]);
	}

	protected String getTestName() {
		return name.getMethodName().replaceAll("[\\[\\] ]", "");
	}

	protected String getMethodName() {
		return name.getMethodName().replaceAll("\\[\\d+\\]", "").replace("default", "").replaceAll("brms-.*\\.xml", "")
				.replaceAll("drools-.*\\.xml", "").replaceAll("eap-.*\\.xml", "").trim();
	}

	protected RuntimeVersion getUsedVersion() {
		if (getAnnotationOnMethod(getMethodName(), Drools6Runtime.class) != null) {
			return RuntimeVersion.BRMS_6;
		} else if (getAnnotationOnMethod(getMethodName(), Drools5Runtime.class) != null) {
			return RuntimeVersion.BRMS_5;
		} else {
			return runtimeVersion;
		}
	}
	
	protected void runAndCheckDroolsTest(String projectName) {
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		
		RunUtility.runAsJavaApplication(projectName, "src/main/java", "com.sample", "DroolsTest.java");
		new WaitUntil(new ApplicationIsTerminated());
		
		consoleView.open();
		String consoleText = consoleView.getConsoleText();
		LOGGER.debug(consoleText);
		Assert.assertNotNull("Console text is empty.", consoleText);
		Assert.assertTrue("Unexpected text in console:\n" + consoleText, consoleText.matches(SUCCESSFUL_RUN_REGEX));
	}
}
