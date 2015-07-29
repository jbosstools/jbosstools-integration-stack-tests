package org.jboss.tools.switchyard.reddeer.preference.implementation;

import java.util.List;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.switchyard.reddeer.condition.TableHasRow;
import org.jboss.tools.switchyard.reddeer.widget.CellEditor;
import org.jboss.tools.switchyard.reddeer.widget.DefaultCCombo;

/**
 * Represents a properties page "Implementation" for Rules (DRL) implementation.
 * 
 * @author apodhrad, tsedmik
 */
public class ImplementationKnowledgePage extends PreferencePage {

	public ImplementationKnowledgePage selectTab(String tab) {
		new DefaultTabItem(tab).activate();
		return this;
	}

	public ImplementationKnowledgePage selectGeneral() {
		return selectTab("General");
	}

	public ImplementationKnowledgePage selectOperations() {
		return selectTab("Operations");
	}

	public ImplementationKnowledgePage selectAdvanced() {
		return selectTab("Advanced");
	}

	public ImplementationKnowledgePage addOperation(String operation, String type, String eventId) {
		addItem("Operations", operation, "CCOMBO" + type, eventId);
		return this;
	}

	public ImplementationKnowledgePage removeOperation(String operation, String type, String eventId) {
		return this;
	}

	public ImplementationKnowledgePage addGlobal(String from, String to) {
		addItem("Globals", from, to);
		return this;
	}

	public List<TableItem> getGlobals() {
		return new DefaultTable(new DefaultSection("Globals")).getItems();
	}

	public ImplementationKnowledgePage addInput(String from, String to, String output) {
		addItem("Inputs", from, to, output);
		return this;
	}

	public List<TableItem> getInputs() {
		return new DefaultTable(new DefaultSection("Inputs")).getItems();
	}

	public ImplementationKnowledgePage addOutput(String from, String to) {
		addItem("Outputs", from, to);
		return this;
	}

	public List<TableItem> getOutputs() {
		return new DefaultTable(new DefaultSection("Outputs")).getItems();
	}

	public ImplementationKnowledgePage addFault(String from, String to) {
		addItem("Faults", from, to);
		return this;
	}

	public List<TableItem> getFaults() {
		return new DefaultTable(new DefaultSection("Faults")).getItems();
	}

	public ImplementationKnowledgePage addChannel(String name, String operation, String reference, String clazz) {
		addItem("Channels", name, operation, reference, "CHANNEL" + clazz);
		return this;
	}

	public ImplementationKnowledgePage addListener(String clazz) {
		addItem("Listeners", "LISTENER" + clazz);
		return this;
	}

	public ImplementationKnowledgePage addLogger(String type, String log, String interval) {
		addItem("Loggers", "CCOMBO" + type, log, interval);
		return this;
	}

	public ImplementationKnowledgePage addProperty(String name, String value) {
		addItem("Properties", name, value);
		return this;
	}

	private void addItem(String section, String... values) {
		new DefaultSection(section).setExpanded(true);
		Button addButton = new PushButton(new DefaultSection(section), "Add");
		if (!addButton.isEnabled()) {
			throw new IllegalStateException("Cannot add an item in '" + section
					+ "'. Ensure that an operation is selected.");
		}
		addButton.click();
		List<TableItem> tableItems = new DefaultTable(new DefaultSection(section)).getItems();
		if (tableItems.isEmpty()) {
			throw new IllegalStateException("There is no item to edit.");
		}
		TableItem tableItem = tableItems.get(tableItems.size() - 1);
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null) {
				continue;
			}
			CellEditor cellEditor = new CellEditor(tableItem, i);
			cellEditor.activate();
			if (values[i].startsWith("TEXT")) {
				new DefaultText(cellEditor).setText(values[i].substring("TEXT".length()));
			} else if (values[i].startsWith("CCOMBO")) {
				new DefaultCCombo(cellEditor).setSelection(values[i].substring("CCOMBO".length()));
			} else if (values[i].startsWith("CLASS")) {
				String clazz = values[i].substring("CLASS".length());
				addClass(cellEditor, null, clazz);
			} else if (values[i].startsWith("CHANNEL")) {
				String clazz = values[i].substring("CHANNEL".length());
				addClass(cellEditor, "Channel", clazz);
			} else if (values[i].startsWith("LISTENER")) {
				String clazz = values[i].substring("LISTENER".length());
				addClass(cellEditor, "Event Listener", clazz);
			} else {
				new DefaultText(cellEditor).setText(values[i]);
			}
			cellEditor.deactivate();
		}
	}

	private void addClass(CellEditor cellEditor, String title, String clazz) {
		new PushButton(cellEditor, "...").click();
		if (title != null) {
			new DefaultShell(title);
		} else {
			new DefaultShell();
		}
		new DefaultText().setText(clazz);
		new WaitUntil(new TableHasRow(new DefaultTable(), new RegexMatcher(".*" + clazz + ".*")));
		new OkButton().click();
		if (title != null) {
			new WaitWhile(new ShellWithTextIsAvailable(title));
		}
	}
}
