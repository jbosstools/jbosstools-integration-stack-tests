package org.jboss.tools.drools.ui.bot.test.functional.view;

import java.io.File;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.matcher.WithRegexMatchers;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.drools.reddeer.editor.EnhancedTextEditor;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.view.AuditView;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.ApplicationIsTerminated;
import org.jboss.tools.drools.ui.bot.test.util.OpenUtility;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.jboss.tools.drools.ui.bot.test.util.RuntimeVersion;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AuditLogTest extends TestParent {
    public AuditLogTest() {
        super(RuntimeVersion.BRMS_6);
    }

    @Before
    public void addLoggerToSession() {
        OpenUtility.openResource(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
        EnhancedTextEditor editor = new EnhancedTextEditor();

        editor.setPosition(3, 0);
        editor.writeText("import org.kie.api.logger.KieRuntimeLogger;\n");
        editor.setPosition(18, 0);
        editor.writeText("            KieRuntimeLogger logger = ks.getLoggers().newFileLogger(kSession, \"test\");\n");
        editor.setPosition(26, 0);
        editor.writeText("\n            logger.close();\n");

        editor.save();
        editor.close();
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultProject
    public void testDefaultProject() {
        RunUtility.runAsJavaApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
        new WaitUntil(new ApplicationIsTerminated());

        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(DEFAULT_PROJECT_NAME).select();

        new ContextMenu(new WithRegexMatchers("Refresh.*").getMatchers()).select();

        explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(DEFAULT_PROJECT_NAME).getProjectItem("test.log").select();

        new ContextMenu(new WithRegexMatchers("Properties.*").getMatchers()).select();

        String location = new LabeledText("Location:").getText();
        new PushButton("Cancel").click();

        AuditView view = new AuditView();
        view.open();
        view.openLog(location);

        List<String> events = view.getEvents();
        Assert.assertEquals("Wrong number of audit log events", 3, events.size());
        Assert.assertTrue("Wrong event encountered", events.get(0).contains("Object inserted"));
        Assert.assertTrue("Wrong event encountered", events.get(1).contains("Activation executed"));
        Assert.assertTrue("Wrong event encountered", events.get(2).contains("Activation executed"));
    }

    @Test
    @UsePerspective(DroolsPerspective.class) @UseDefaultProject
    public void testBussinessAuditLog() throws Exception {
        String path = new File("resources/test150.log").getAbsolutePath();

        AuditView view = new AuditView();
        view.open();
        view.openLog(path);

        List<String> events = view.getEvents();
        // do not hunt for all the events, just check the number
        Assert.assertEquals(events.toString(), 313, events.size());
    }
}
