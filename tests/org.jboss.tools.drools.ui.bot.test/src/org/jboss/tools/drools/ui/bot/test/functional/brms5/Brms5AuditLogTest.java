package org.jboss.tools.drools.ui.bot.test.functional.brms5;

import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.drools.reddeer.perspective.DroolsPerspective;
import org.jboss.tools.drools.reddeer.view.AuditView;
import org.jboss.tools.drools.ui.bot.test.annotation.Drools5Runtime;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.group.SmokeTest;
import org.jboss.tools.drools.ui.bot.test.util.ApplicationIsTerminated;
import org.jboss.tools.drools.ui.bot.test.util.RunUtility;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class Brms5AuditLogTest extends TestParent {
	
	@InjectRequirement
	private RuntimeRequirement droolsRequirement;

    @Test
    @Category(SmokeTest.class)
    @UsePerspective(DroolsPerspective.class)
    @UseDefaultProject
    @Drools5Runtime
    public void testDefaultProject() {
        RunUtility.runAsJavaApplication(DEFAULT_PROJECT_NAME, "src/main/java", "com.sample", "DroolsTest.java");
        new WaitUntil(new ApplicationIsTerminated());

        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(DEFAULT_PROJECT_NAME).select();

        new ContextMenu(new RegexMatcher("Refresh.*")).select();

        explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(DEFAULT_PROJECT_NAME).getProjectItem("test.log").select();

        new ContextMenu(new RegexMatcher("Properties.*")).select();

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
}
