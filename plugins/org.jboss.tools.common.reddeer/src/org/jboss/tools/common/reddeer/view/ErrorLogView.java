package org.jboss.tools.common.reddeer.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ViewMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;

/**
 * Represents <i>Error Log</i> view
 * 
 * @author tsedmik
 */
public class ErrorLogView extends LogView {

	@Override
	public void deleteLog() {
		open();
		DefaultToolItem deleteLog = new DefaultToolItem("Delete Log");
		log.info("Deleting all logs in Error log.");
		if (deleteLog.isEnabled()) {
			deleteLog.click();
			new DefaultShell("Confirm Delete");
			new PushButton("OK").click();
			new WaitWhile(new ShellWithTextIsActive("Confirm Delete"));
		} else {
			log.warn("Cannot delete Error log. It is already empty.");
		}
		new WorkbenchShell().setFocus();
	}

	@Override
	public List<LogMessage> getErrorMessages() {
		open();
		setFilter(ERROR_SEVERITY);
		open();
		DefaultTree tree = new DefaultTree();
		List<TreeItem> treeItems = tree.getAllItems();
		List<LogMessage> messages = new ArrayList<LogMessage>();
		for (TreeItem item : treeItems) {
			messages.add(new LogMessage(item, IStatus.ERROR));
		}
		return messages;
	}

	public void selectActivateOnNewEvents() {
		open();
		new ViewMenu("Activate on new events").select();
	}

	private void setFilter(String severity) {
		ViewMenu tmenu = new ViewMenu("Filters...");
		tmenu.select();
		new WaitUntil(new ShellWithTextIsAvailable("Log Filters"));
		new DefaultShell("Log Filters");
		new CheckBox(OK_SEVERITY).toggle(false);
		new CheckBox(INFORMATION_SEVERITY).toggle(false);
		new CheckBox(WARNING_SEVERITY).toggle(false);
		new CheckBox(ERROR_SEVERITY).toggle(false);
		new CheckBox(severity).toggle(true);
		new CheckBox("Limit visible events to:").toggle(false);
		new PushButton("OK").click();
	}
}
