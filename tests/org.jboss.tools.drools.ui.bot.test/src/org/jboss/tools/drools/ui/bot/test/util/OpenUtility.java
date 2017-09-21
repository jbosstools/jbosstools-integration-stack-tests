package org.jboss.tools.drools.ui.bot.test.util;

import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;

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
		PackageExplorerPart explorer = new PackageExplorerPart();
		explorer.open();

		ProjectItem pi = explorer.getProject(project).getProjectItem(path);
		pi.select();

		if (editor == null) {
			pi.open();
		} else {
			new ContextMenuItem("Open With", editor).select();
		}
		return explorer.getProject(project).getProjectItem(path);
	}

}
