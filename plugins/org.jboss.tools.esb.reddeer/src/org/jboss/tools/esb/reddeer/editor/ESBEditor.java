package org.jboss.tools.esb.reddeer.editor;

import static org.junit.Assert.assertFalse;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.esb.reddeer.wizard.ESBActionWizard;
import org.jboss.tools.esb.reddeer.wizard.ESBListenerWizard;
import org.jboss.tools.esb.reddeer.wizard.ESBProviderWizard;
import org.jboss.tools.esb.reddeer.wizard.ESBServiceWizard;

/**
 * 
 * @author apodhrad
 * 
 */
public class ESBEditor extends DefaultEditor {

	public static final String ESB_FILE = "jboss-esb.xml";
	public static final String MESSAGE_FLOW_PRIORITY = "Message Flow Priority:";

	private String name;
	private SWTWorkbenchBot bot = new SWTWorkbenchBot();

	public ESBEditor() {
		this(ESB_FILE);
	}

	public ESBEditor(String name) {
		super(name);
		this.name = name;
	}

	public String getName() {
		System.out.println("IsDirty? " + isDirty());
		return name + (isDirty() ? "*" : "");
	}

	public ESBProviderWizard addProvider(String provider) {
		selectTree();
		new DefaultTreeItem(getName(), "Providers").select();
		new ContextMenu("New", provider + "...").select();
		return new ESBProviderWizard(provider);
	}

	public void addHttpProvider(String name, String channel) {
		ESBProviderWizard wizard = new ESBEditor().addProvider("HTTP Provider");
		assertFalse(new PushButton("Next >").isEnabled());
		wizard.setName(name);
		wizard.next();
		wizard.setChannel(channel);
		wizard.finish();
	}

	public ESBEditor addService(String name, String category, String description) {
		selectTree();
		new DefaultTreeItem(getName(), "Services").select();
		new ContextMenu("Add Service...").select();

		ESBServiceWizard wizard = new ESBServiceWizard();
		wizard.setName(name);
		wizard.setCategory(category);
		wizard.setDescription(description);
		wizard.finish();
		return this;
	}

	public ESBListenerWizard addListenerToService(String service, String listener) {
		selectTree();
		new DefaultTreeItem(getName(), "Services", service, "Listeners").select();
		new ContextMenu("New", listener + "...").select();
		return new ESBListenerWizard(listener);
	}

	public ESBActionWizard addCustomActionToService(String service) {
		selectTree();
		new DefaultTreeItem(getName(), "Services", service, "Actions").select();
		new ContextMenu("New", "Custom Action...").select();
		return new ESBActionWizard("Add Action");
	}

	public ESBActionWizard addActionToService(String service, String category, String action) {
		selectTree();
		new DefaultTreeItem(getName(), "Services", service, "Actions").select();
		try {
			new ContextMenu("New", category, action + "...").select();
		} catch (Exception e) {
			new ContextMenu("New", category, action).select();
		}
		return new ESBActionWizard("Add " + action);
	}

	public ESBActionWizard addActionToService(String service, String category, String action, String title) {
		selectTree();
		new DefaultTreeItem(getName(), "Services", service, "Actions").select();
		try {
			new ContextMenu("New", category, action + "...").select();
		} catch (Exception e) {
			new ContextMenu("New", category, action).select();
		}
		return new ESBActionWizard(title);
	}

	public ESBEditor setMessageFlowPriority(String priority) {
		getMessageFlowPriorityCombo().setSelection(priority);
		return this;
	}

	public String getMessageFlowPriority() {
		return getMessageFlowPriorityCombo().getText();
	}

	public Combo getMessageFlowPriorityCombo() {
		selectTree();
		return new MessageFlowPriorityCombo();
	}

	public String getSource() {
		selectSource();
		return new DefaultStyledText().getText();
	}

	public void selectTree() {
		new DefaultCTabItem("Tree").activate();
	}

	public void selectSource() {
		new DefaultCTabItem("Source").activate();
	}

	public ESBService getService(String service) {
		selectTree();
		return new ESBService(getName(), "Services", service);
	}

	public void setText(String label, String text) {
		bot.textWithLabel(label).setText(text);
	}

	public String getText(String label) {
		return bot.textWithLabel(label).getText();
	}

	public void setCombo(String label, String text) {
		bot.comboBoxWithLabel(label).setSelection(text);
	}

	private class MessageFlowPriorityCombo extends DefaultCombo {

		public MessageFlowPriorityCombo() {
			this.swtWidget = new SWTWorkbenchBot().comboBoxWithLabel(MESSAGE_FLOW_PRIORITY).widget;
		}

	}

	public void saveAndClose() {
		save();
		close();
	}
}
