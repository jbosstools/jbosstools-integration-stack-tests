package org.jboss.tools.drools.ui.bot.test.util;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;

public final class OpenUtility {

	private OpenUtility() {
	}

	public static void openResource(String project, String... path) {
		openResource(project, null, path);
	}

	public static void openResourceWith(String editor, String project, String... path) {
		openResource(project, editor, path);
	}

	private static ProjectItem openResource(String project, String editor, String... path) {
		PackageExplorer explorer = new PackageExplorer();
		explorer.open();

		ProjectItem pi = explorer.getProject(project).getProjectItem(path);
		pi.select();

		if (editor == null) {
			pi.open();
		} else {
			new ContextMenu("Open With", editor).select();
		}
		return explorer.getProject(project).getProjectItem(path);
	}

}
