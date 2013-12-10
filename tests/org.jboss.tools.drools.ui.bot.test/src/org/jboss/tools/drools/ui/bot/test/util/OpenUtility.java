package org.jboss.tools.drools.ui.bot.test.util;

import org.eclipse.swt.widgets.MenuItem;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.lookup.MenuLookup;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.swt.util.Display;

public final class OpenUtility {

    private OpenUtility() {
    }

    public static void openResource(String project, String... path) {
        selectResource(project, path);
        open();
    }

    public static void openResourceWith(String editor, String project, String... path) {
        selectResource(project, path);
        openWith(editor);
    }

    public static void selectResource(String project, String... path) {
        PackageExplorer explorer = new PackageExplorer();
        explorer.open();
        explorer.getProject(project).getProjectItem(path).select();
    }

    private static void open() {
        new ContextMenu(new RegexMatchers("Open.*").getMatchers()).select();
    }

    private static void openWith(String editor) {
        MenuLookup ml = new MenuLookup();
        final MenuItem item = ml.lookFor(ml.getTopMenuMenuItemsFromFocus(), new RegexMatchers("Open With", editor).getMatchers());
        Display.syncExec(new Runnable() {
            public void run() {
                item.setSelection(true);
            }
        });
        ml.select(item);
    }
}
