package org.jboss.tools.teiid.reddeer.dialog;

import java.util.Arrays;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.swt.impl.button.FinishButton;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;

public class MaterializationDialog extends AbstractDialog {

    public static class TypeOfJdg {
        public static final String JDG_66 = "6.6";
        public static final String JDG_71 = "7.1";
    }
    private static final Logger log = Logger.getLogger(MaterializationDialog.class);

    private DefaultShell dialog = null;

    public MaterializationDialog() {
        super("Generate Materialized JDG Module");
        dialog = new DefaultShell(title);
    }

    public MaterializationDialog setSourceModelName(String sourceModel) {
        log.info("JDG source model: '" + sourceModel + "'");
        new DefaultText(new DefaultGroup(dialog, "Source Model Information"), 0).setText(sourceModel);
        return this;
    }

    public MaterializationDialog setLocation(String... path) {
        log.info("Setting location to " + Arrays.toString(path));
        new PushButton(dialog, "...").click();
        new DefaultShell("Select a Folder");
        new DefaultTreeItem(path).select();
        new OkButton().click();
        activate();
        return this;
    }

    /**
     * @param type
     * @return
     */
    public MaterializationDialog setVersionOfJdg(String type) {
        log.info("Set version of JDG to '" + type + "'");
        new DefaultCombo(new DefaultGroup(dialog, "JDG Options;"), 0).setSelection(type);
        return this;
    }

    /**
     * Only for JDG 7.1
     */
    public MaterializationDialog setPrimaryCache(String primaryCacheName) {
        log.info("Set primary cache name to '" + primaryCacheName + "'");
        new DefaultText(new DefaultGroup(dialog, "JDG Options;"), 0).setText(primaryCacheName);
        return this;
    }

    /**
     * Only for JDG 7.1
     */
    public MaterializationDialog setStagingCache(String stagingCacheName) {
        log.info("Set staging cache name to '" + stagingCacheName + "'");
        new DefaultText(new DefaultGroup(dialog, "JDG Options;"), 1).setText(stagingCacheName);
        return this;
    }

    @Override
    public void finish() {
        new FinishButton(dialog).click();
    }
}
