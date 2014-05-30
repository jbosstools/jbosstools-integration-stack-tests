package org.jboss.tools.esb.reddeer.editor;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.esb.reddeer.wizard.ESBActionWizard;
import org.jboss.tools.esb.reddeer.wizard.ESBListenerWizard;

/**
 * 
 * @author apodhrad
 *
 */
public class ESBService extends DefaultTreeItem {
	
	protected SWTWorkbenchBot bot = new SWTWorkbenchBot();

	public ESBService(String... path) {
		super(path);
	}

	public void setInvmScope(String scope) {
		bot.comboBoxWithLabel("Invm Scope:").setSelection(scope);
	}
	
	public ESBActionWizard addAction(String category, String action) {
		return addAction(category, action, "Add " + action);
	}

	public ESBActionWizard addAction(String category, String action, String title) {
		getItem("Actions").select();
		try {
			new ContextMenu("New", category, action + "...").select();
		} catch (Exception e) {
			new ContextMenu("New", category, action).select();
		}
		return new ESBActionWizard(title);
	}

	public TreeItem addNotifier(String name) {
		ESBActionWizard wizard = addAction("Routers", "Notifier");
		wizard.setName(name);
		wizard.finish();
		return getAction(name);
	}

	public TreeItem getAction(String name) {
		return getItem("Actions").getItem(name);
	}

	public ESBListenerWizard addListner(String listener) {
		getItem("Listeners").select();
		new ContextMenu("New", listener + "...").select();
		return new ESBListenerWizard(listener);
	}

	public void addHttpListener(String name, String channel) {
		ESBListenerWizard wizard = addListner("HTTP Gateway");
		wizard.setName(name);
		wizard.setChannelRef(channel);
		wizard.finish();
	}
}
