package org.jboss.tools.drools.ui.bot.test.functional;

import java.util.List;

import org.apache.log4j.Logger;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.drools.reddeer.dialog.DslLineDialog;
import org.jboss.tools.drools.reddeer.dialog.DslLineDialog.Scope;
import org.jboss.tools.drools.reddeer.editor.DslEditor;
import org.jboss.tools.drools.reddeer.editor.DslEditor.DslLine;
import org.jboss.tools.drools.reddeer.editor.DslEditor.SortBy;
import org.jboss.tools.drools.reddeer.wizard.NewDslWizard;
import org.jboss.tools.drools.ui.bot.test.annotation.Drools6Runtime;
import org.jboss.tools.drools.ui.bot.test.annotation.UseDefaultProject;
import org.jboss.tools.drools.ui.bot.test.annotation.UsePerspective;
import org.jboss.tools.drools.ui.bot.test.util.OpenUtility;
import org.jboss.tools.drools.ui.bot.test.util.TestParent;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeReqType;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement;
import org.jboss.tools.runtime.reddeer.requirement.RuntimeRequirement.Runtime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@Runtime(type = RuntimeReqType.DROOLS)
@RunWith(RedDeerSuite.class)
public class DslEditorTest extends TestParent {
    private static final Logger LOGGER = Logger.getLogger(DslEditorTest.class);
    private static final String MAPPING = "System.out.println(\"Hello World!\");";
    private static final String EXPRESSION = "Print Hello";
    
    @InjectRequirement
	private RuntimeRequirement droolsRequirement;

    @Before
    public void createDefaultDsl() {
        NewDslWizard wiz = new NewDslWizard();
        wiz.open();
        wiz.getFirstPage().setParentFolder(getRulesLocation());
        wiz.getFirstPage().setFileName(getTestName());
        wiz.getSamplesPage().setAddSampleDsl(true);
        wiz.finish();

        new DslEditor().close(true);
    }

    @Test
    @UsePerspective(JavaPerspective.class)
    @Drools6Runtime
    @UseDefaultProject
    public void testOpen() {
        Project project = new PackageExplorer().getProject(DEFAULT_PROJECT_NAME);
        project.getProjectItem(getResourcePath(getTestName() + ".dsl")).open();

        DslEditor editor = new DslEditor();
        Assert.assertNotSame("No DSL lines were generated", 0, editor.getDslLines().size());
        for (DslLine line : editor.getDslLines()) {
            LOGGER.info(line);
        }
    }

    @Test
    @UsePerspective(JavaPerspective.class)
    @Drools6Runtime
    @UseDefaultProject
    public void testAddExpression() {
        DslEditor editor = openTestDslFile();
        List<DslLine> origLines = editor.getDslLines();

        // add a new line
        DslLineDialog dialog = editor.add();
        dialog.setRuleMapping(MAPPING);
        dialog.setLanguageExpression(EXPRESSION);
        dialog.setScope(Scope.CONSEQUENCE);
        dialog.ok();

     // FIXME this is annoying - closing dialog makes editor loose focus and nothing works then
        editor = openTestDslFile();

        List<DslLine> newLines = editor.getDslLines();
        Assert.assertEquals("Line count is same!", origLines.size() + 1, newLines.size());

        DslLine line = newLines.get(newLines.size() - 1);
        Assert.assertEquals("Wrong mapping!", MAPPING, line.getMapping());
        Assert.assertEquals("Wrong expression!", EXPRESSION, line.getExpression());
        Assert.assertEquals("Wrong scope!", Scope.CONSEQUENCE.toEditorString(), line.getScope());
    }

    @Test
    @UsePerspective(JavaPerspective.class)
    @Drools6Runtime
    @UseDefaultProject
    public void testInsertExpression() {
        DslEditor editor = openTestDslFile();
        List<DslLine> origLines = editor.getDslLines();

        int position = origLines.size() /2;
        editor.selectLine(origLines.get(position));

        DslLineDialog dialog = editor.add();
        dialog.setRuleMapping(MAPPING);
        dialog.setLanguageExpression(EXPRESSION);
        dialog.setScope(Scope.CONSEQUENCE);
        dialog.ok();

        // FIXME this is annoying - closing dialog makes editor loose focus and nothing works then
        editor = openTestDslFile();

        List<DslLine> newLines = editor.getDslLines();
        Assert.assertEquals("Line count is same!", origLines.size() + 1, newLines.size());

        // FIXME this doesn't really work, it needs to be addressed in bz#1013750
        DslLine line = newLines.get(position + 1);
        Assert.assertEquals("Wrong mapping!", MAPPING, line.getMapping());
        Assert.assertEquals("Wrong expression!", EXPRESSION, line.getExpression());
        Assert.assertEquals("Wrong scope!", Scope.CONSEQUENCE.toEditorString(), line.getScope());
    }

    @Test
    @UsePerspective(JavaPerspective.class)
    @Drools6Runtime
    @UseDefaultProject
    public void testEditExpression() {
        DslEditor editor = openTestDslFile();
        List<DslLine> origLines = editor.getDslLines();
        int position = origLines.size() / 2;
        DslLine origLine = origLines.get(position);

        editor.selectLine(origLine);
        DslLineDialog dialog = editor.edit();
        dialog.setRuleMapping(MAPPING);
        dialog.setLanguageExpression(EXPRESSION);
        dialog.ok();

     // FIXME this is annoying - closing dialog makes editor loose focus and nothing works then
        editor = openTestDslFile();

        List<DslLine> newLines = editor.getDslLines();
        DslLine newLine = newLines.get(position);
        Assert.assertEquals("Line count is different!", origLines.size(), newLines.size());
        Assert.assertEquals("Scope differs", origLine.getScope(), newLine.getScope());
        Assert.assertEquals("Mapping is the same", MAPPING, newLine.getMapping());
        Assert.assertEquals("Rule expression is same", EXPRESSION, newLine.getExpression());
        Assert.assertEquals("Object is different", origLine.getObject(), newLine.getObject());
    }

    @Test
    @UsePerspective(JavaPerspective.class)
    @Drools6Runtime
    @UseDefaultProject
    public void testRemoveExpression() {
        DslEditor editor = openTestDslFile();
        List<DslLine> origLines = editor.getDslLines();
        int position = origLines.size() / 2;
        DslLine origLine = origLines.get(position);

        editor.selectLine(origLine);
        editor.remove();

        List<DslLine> newLines = editor.getDslLines();

        Assert.assertEquals("There are more lines than expected", origLines.size() - 1, newLines.size());
        Assert.assertFalse("Removed line is still present", newLines.contains(origLine));
    }

    @Test
    @UsePerspective(JavaPerspective.class)
    @Drools6Runtime
    @UseDefaultProject
    public void testSort() {
        DslEditor editor = openTestDslFile();
        List<DslLine> origLines = editor.getDslLines();

        editor.sort(SortBy.SCOPE);

        List<DslLine> newLines = editor.getDslLines();
        Assert.assertEquals("Different number of DSL lines", origLines.size(), newLines.size());

        for (DslLine line : origLines) {
            Assert.assertTrue("Missing line " + line, newLines.contains(line));
        }

        // TODO test the order of dsl lines?
    }

    @Test
    @UsePerspective(JavaPerspective.class)
    @Drools6Runtime
    @UseDefaultProject
    public void testOpenHandwrittenDsl() {
        OpenUtility.openResourceWith("Text Editor", DEFAULT_PROJECT_NAME, getResourcePath(getTestName() + ".dsl"));

        TextEditor txtEditor = new TextEditor();
        txtEditor.setText(getTemplateText("DslTestContent"));
        txtEditor.save();
        txtEditor.close();

        OpenUtility.openResourceWith("DSL Editor", DEFAULT_PROJECT_NAME, getResourcePath(getTestName() + ".dsl"));

        DslEditor editor = new DslEditor();
        List<DslLine> lines = editor.getDslLines();

        Assert.assertEquals("Not all text file lines are present", 29, lines.size());
    }

    private DslEditor openTestDslFile() {
        OpenUtility.openResource(DEFAULT_PROJECT_NAME, getResourcePath(getTestName() + ".dsl"));

        return new DslEditor();
    }
}
