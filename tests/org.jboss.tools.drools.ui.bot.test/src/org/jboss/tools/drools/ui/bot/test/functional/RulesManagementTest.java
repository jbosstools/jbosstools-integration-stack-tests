package org.jboss.tools.drools.ui.bot.test.functional;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.perspectives.ResourcePerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.drools.reddeer.dialog.DroolsRuntimeDialog;
import org.jboss.tools.drools.reddeer.editor.DrlEditor;
import org.jboss.tools.drools.reddeer.editor.RuleEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.preference.DroolsRuntimesPreferencePage;
import org.jboss.tools.drools.reddeer.wizard.NewDroolsProjectWizard;
import org.jboss.tools.drools.ui.bot.test.annotation.Drools6Runtime;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.group.SmokeTest;
import org.jboss.tools.drools.ui.bot.test.util.ApplicationIsTerminated;
import org.jboss.tools.drools.ui.bot.test.util.OpenUtility;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
public class RulesManagementTest extends TestParent {
    private static final Logger LOGGER = Logger.getLogger(RulesManagementTest.class);
    private static final String DEBUG_REGEX = "(SLF4J: .*\n)+?" +
            "(kmodules: file:(/.*)+/kmodule.xml\n)?";
    private static final String SUCCESSFUL_RUN_REGEX = DEBUG_REGEX + "Hello World\nGoodbye cruel world\n";

    @Test
    @UsePerspective(JavaPerspective.class) @Drools6Runtime @UseDefaultProject
    public void testRunRulesFromContextMenu() {
        ConsoleView console = new ConsoleView();
        console.open();

        RunUtility.runAsJavaApplication(true, DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
        new WaitUntil(new ApplicationIsTerminated());
        waitASecond(); // this is quite annoying - the text is updated AFTER the application is terminated

        console.open();
        String consoleText = console.getConsoleText();
        Assert.assertNotNull("Console text was empty.", consoleText);
        LOGGER.debug(consoleText);
        Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.matches(SUCCESSFUL_RUN_REGEX));
    }

    @Test
    @UsePerspective(JavaPerspective.class) @Drools6Runtime @UseDefaultProject
    public void testRunRulesFromToolbar() {
        ConsoleView console = new ConsoleView();
        console.open();

        RunUtility.runAsJavaApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
        new WaitUntil(new ApplicationIsTerminated());
        waitASecond(); // this is quite annoying - the text is updated AFTER the application is terminated

        console.open();
        String consoleText = console.getConsoleText();
        Assert.assertNotNull("Console text was empty.", consoleText);
        LOGGER.debug(consoleText);
        Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.matches(SUCCESSFUL_RUN_REGEX));
    }

    @Test
    @UsePerspective(JavaPerspective.class)
    public void testRunRulesWithDefaultRuntime() {
        final String runtimeName = "testRunRulesWithDefaultRuntime";
        final String projectName = "testRunRulesWithDefaultRuntime";
        DroolsRuntimesPreferencePage pref = new DroolsRuntimesPreferencePage();
        pref.open();
        DroolsRuntimeDialog dialog = pref.addDroolsRuntime();
        dialog.setName(runtimeName);
        dialog.createNewRuntime(createTempDir("testRunRulesWithDefaultRuntime"));
        dialog.ok();
        pref.setDroolsRuntimeAsDefault(runtimeName);
        pref.okCloseWarning();

        NewDroolsProjectWizard wiz = new NewDroolsProjectWizard();
        wiz.createDefaultProjectWithAllSamples(projectName);


        ConsoleView console = new ConsoleView();
        console.open();

        RunUtility.runAsJavaApplication(projectName, "src/main/java", "com.sample", "DroolsTest.java");
        new WaitUntil(new ApplicationIsTerminated());
        waitASecond(); // this is quite annoying - the text is updated AFTER the application is terminated

        console.open();
        String consoleText = console.getConsoleText();
        Assert.assertNotNull("Console text was empty.", consoleText);
        LOGGER.debug(consoleText);
        Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.matches(SUCCESSFUL_RUN_REGEX));
    }

    @Test
    @UsePerspective(JavaPerspective.class) @Drools6Runtime @UseDefaultProject
    public void testRenameProject() {
        final String oldName = DEFAULT_PROJECT_NAME;
        final String newName = "renamed" + oldName;

        PackageExplorer explorer = new PackageExplorer();
        Assert.assertTrue("The original project was not created.", explorer.containsProject(oldName));
        explorer.getProject(oldName).select();

        new ContextMenu(new RegexMatcher("Refactor.*"), new RegexMatcher("Rename.*")).select();

        new DefaultShell("Rename Java Project");
        new LabeledText("New name:").setText(newName);
        new PushButton("OK").click();
        new WaitWhile(new JobIsRunning());

        waitASecond();
        Assert.assertFalse("The original project is still present.", explorer.containsProject(oldName));
        Assert.assertTrue("The renamed project is not present.", explorer.containsProject(newName));
    }

    @Test
    @UsePerspective(JavaPerspective.class) @Drools6Runtime @UseDefaultProject
    public void testSetBreakpoint() {
        OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));

        RuleEditor editor = new DrlEditor().showRuleEditor();
        editor.setPosition(8, 0);

        new ResourcePerspective().open();
        new JavaPerspective().open();

        try {
            new ShellMenu(new RegexMatcher("Run"), new RegexMatcher("Toggle Breakpoint.*")).select();
        } catch (SWTLayerException ex) {
            if ("Menu item is not enabled".equals(ex.getMessage())) {
                Assert.fail("Toggle Breakpoint menu item is not enabled!");
            } else {
                throw ex;
            }
        }
    }

    @Test @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class) @Drools6Runtime @UseDefaultProject
    public void testDebugRule() {
        OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath("Sample.drl"));

        new DrlEditor().showRuleEditor().setBreakpoint(8);

        RunUtility.debugAsDroolsApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");

        ConsoleView console = new ConsoleView();
        console.open();
        String consoleText = console.getConsoleText();
        Assert.assertNotNull("Console text was empty.", consoleText);
        LOGGER.debug(consoleText);
        Assert.assertTrue("Unexpected text in console\n" + consoleText, consoleText.matches(DEBUG_REGEX));

        // wait a moment before Debug perspective is fully loaded
        waitASecond();

        new ShellMenu(new RegexMatcher("Run"), new RegexMatcher("Resume.*")).select();
        console.open();
        consoleText = console.getConsoleText();
        Assert.assertTrue("Wrong console text found\n" + consoleText, consoleText.matches(SUCCESSFUL_RUN_REGEX));
    }

}
