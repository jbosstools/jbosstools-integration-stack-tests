package org.jboss.tools.teiid.ui.bot.test.reproducers;

import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.AssertBot;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.dialog.TableDialog;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class TeiidDes3125Test {
    private static final String PROJECT_NAME = "TransformationToolsProject";
    private static final String VIEW_MODEL = "PartsView.xmi";

    private ModelExplorer modelExplorer;

    @BeforeClass
    public static void before() {
        new WorkbenchShell().maximize();
    }

    @Before
    public void importProject() {
        modelExplorer = new ModelExplorer();
        modelExplorer.importProject(PROJECT_NAME);
        modelExplorer.refreshProject(PROJECT_NAME);
    }

    @Test
    public void testParenthesis() {
        String tableName = "parenthesesTestTable";
        String expectedSql = new ResourceFileHelper().getSql("Reproducers/parentheses");
        modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, VIEW_MODEL);
        TableDialog dialog = new TableDialog(true);
        dialog.setName(tableName);
        dialog.finish();

        RelationalModelEditor editor = new RelationalModelEditor(VIEW_MODEL);
        TransformationEditor tEditor = editor.openTransformationDiagram(ModelEditor.ItemType.TABLE, tableName);
        tEditor.setTransformation(expectedSql);
        tEditor.saveAndValidateSql();
        String actaul = tEditor.getTransformation();
        AssertBot.transformationEquals(actaul.replaceAll("\\s", ""), expectedSql.replaceAll("\\s", ""));
    }
}