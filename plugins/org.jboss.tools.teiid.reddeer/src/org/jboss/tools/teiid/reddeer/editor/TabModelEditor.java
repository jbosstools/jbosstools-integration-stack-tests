package org.jboss.tools.teiid.reddeer.editor;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.core.matcher.WithTooltipTextMatcher;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.teiid.reddeer.dialog.IndexDialog;
import org.jboss.tools.teiid.reddeer.dialog.TableDialog;
import org.jboss.tools.teiid.reddeer.wizard.exports.DDLTeiidExportWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.NewProcedureWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;

public class TabModelEditor extends DefaultEditor{

    public TabModelEditor() {
        super();
        new DefaultCTabItem("Model Editor").activate();
        AbstractWait.sleep(TimePeriod.SHORT);
    }

    public void selectItem(String... pathToItem) {
        new DefaultTreeItem(pathToItem).select();
    }

    public TableDialog createRelationalTable() {
        new PushButton(new WithTooltipTextMatcher("Create relational table")).click();
        return new TableDialog(false);
    }

    public NewProcedureWizard createProcedureOrFunction() {
        new PushButton(new WithTooltipTextMatcher("Create relational procedure or function")).click();
        return new NewProcedureWizard();
    }

    public IndexDialog createRelationalIndex(boolean isViewModel) {
        new PushButton(new WithTooltipTextMatcher("Create relational index")).click();
        return new IndexDialog(isViewModel);
    }

    public void previewData() {
        new PushButton(new WithTooltipTextMatcher("Preview data for selected table or view")).click();
    }

    public void generateDynamicVdb() {
        new PushButton(new WithTooltipTextMatcher("Generate a deployable -vdb.xml representing the selected view or table")).click();
        new DefaultShell("Generate Data Service").setFocus();
    }

    public VdbWizard createVdb() {
        new DefaultHyperlink("Create VDB").activate();
        AbstractWait.sleep(TimePeriod.MEDIUM);
        return VdbWizard.getInstance();
    }

    public DDLTeiidExportWizard exportTeiidDDL() {
        new DefaultHyperlink("Export Teiid DDL").activate();
        AbstractWait.sleep(TimePeriod.MEDIUM);
        return DDLTeiidExportWizard.getInstance();
    }

    public void setConnectionProfile() {
        new DefaultHyperlink("Set Connection Profile").activate();
        AbstractWait.sleep(TimePeriod.MEDIUM);
        new DefaultShell("Set Connection Profile").setFocus();
    }

    public void setJBossDataSourceName() {
        new DefaultHyperlink("Set JBoss Data Source Name").activate();
        AbstractWait.sleep(TimePeriod.MEDIUM);
        new DefaultShell("Set JBoss Data Source JNDI Name").setFocus();
    }

    public void setTranslatorName() {
        new DefaultHyperlink("Set Translator Name").activate();
        AbstractWait.sleep(TimePeriod.MEDIUM);
        new DefaultShell("Set Translator Name").setFocus();
    }

    public void editTranslatorOverrides() {
        new DefaultHyperlink("Edit Translator Overrides").activate();
        AbstractWait.sleep(TimePeriod.MEDIUM);
        new DefaultShell("Edit Translator Override Properties").setFocus();
    }

    public void manageExtenstions() {
        new DefaultHyperlink("Manage Extensions").activate();
        AbstractWait.sleep(TimePeriod.MEDIUM);
        new DefaultShell("Manage Model Extension Definitions").setFocus();
    }

    public void showModelStatistics() {
        new DefaultHyperlink("Show Model Statistics").activate();
        AbstractWait.sleep(TimePeriod.MEDIUM);
        new DefaultShell("Model Statistics").setFocus();
    }

    public void editTransformation() {
        new DefaultHyperlink("Edit Transformation").activate();
        AbstractWait.sleep(TimePeriod.MEDIUM);
        new DefaultShell("Edit Transformation").setFocus();
    }

    public DefaultTabItem openPropertiesTab() {
        return new DefaultTabItem("Properties");
    }

    public DefaultTabItem openDescriptionTab() {
        return new DefaultTabItem("Description");
    }

    public void editDescription() {
        openDescriptionTab();
        new DefaultHyperlink("Edit...").activate();
        AbstractWait.sleep(TimePeriod.MEDIUM);
    }
}
