package org.jboss.tools.teiid.ui.bot.test.reproducers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.CancelButton;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class TeiidDes3106Test {
    private static final String PROJECT_NAME = "ProductsProject";
    private static final String VIEW_MODEL = "ProductsView.xmi";
    private static final String VIEW_TABLE = "PRODUCTDATA";

    private ModelExplorer modelExplorer;

    @Before
    public void importProject() {
        modelExplorer = new ModelExplorer();
        modelExplorer.importProject(PROJECT_NAME);
        modelExplorer.refreshProject(PROJECT_NAME);
    }

    @After
    public void after() {
        new ModelExplorer().deleteAllProjectsSafely();
    }

    @Test
    public void testValidationCheck() {
        modelExplorer.selectItem(PROJECT_NAME, VIEW_MODEL, VIEW_TABLE);
        new ContextMenuItem("Modeling", "Materialize").select();
        new WaitUntil(new ShellIsActive("Generate Materialized JDG Module"), TimePeriod.DEFAULT);
        DefaultShell wizard = new DefaultShell("Generate Materialized JDG Module");
        new DefaultText(new DefaultGroup(wizard, "JDG Options"), 0).setText("primaryCache");
        new DefaultText(new DefaultGroup(wizard, "JDG Options"), 1).setText("stagingCache");

        new DefaultText(new DefaultGroup(wizard, "Source Model Information"), 0).setText("Wrong-source-name");
        assertThat(new DefaultText(wizard, 6).getText(), equalToIgnoringCase(
            " Invalid model name: Invalid character '-' at position 6. Valid chars: [a-z] [A-Z] [0-9] or '_'"));

        new DefaultText(new DefaultGroup(wizard, "Source Model Information"), 0).setText("correcT_name_1");
        assertThat(new DefaultText(wizard, 6).getText(),
            equalToIgnoringCase("Click Finish to create materialized table"));
        new CancelButton(wizard).click();
    }
}
