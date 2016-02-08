package org.jboss.tools.teiid.reddeer.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.teiid.reddeer.widget.TeiidEditorTableItem;

public class VDBEditor extends SWTBotEditor {

	public VDBEditor(String name) {
		super(new SWTWorkbenchBot().editorByTitle(name).getReference(), new SWTWorkbenchBot());
	}

	public static VDBEditor getInstance(String name) {
		VDBEditor editor = new VDBEditor(name);
		editor.show();
		return editor;
	}

	/**
	 * 
	 * @param projectName
	 * @param modelXmi
	 *            whole name (with ending)
	 */
	public void addModel(String projectName, String modelXmi) {
		if (!modelXmi.contains(".")) {
			modelXmi = modelXmi + ".xmi";
		}
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Add model").click();

		SWTBotShell shell = bot.shell("Add File(s) to VDB");
		shell.activate();
		shell.bot().tree(0).expandNode(projectName).select(modelXmi);

		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public void addModelsToVDB(String projectName, String[] models){
		show();
		String model = "";
		try {
			for (int i = 0; i < models.length; i++) {
				model = models[i];
				addModel(projectName, model);
			}
		} catch (Exception ex) {
			log.warn("Cannot add model " + model);
		}
		save();
	}
	
	/**
	 * 
	 * @param projectName
	 * @param model
	 * @param longerPath
	 *            true if path to model contains folders
	 */
	public void addModel(boolean longerPath, String... pathToModel) {
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Add model").click();

		SWTBotShell shell = bot.shell("Add File(s) to VDB");
		shell.activate();
		shell.bot().tree(0).expandNode(pathToModel).select();

		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public String getModel(int index) {
		return new SWTWorkbenchBot().table(0).cell(index, 0);
	}

	public void setModelTranslator(String modelName, String sourceName, String translatorName) {
		new DefaultCTabItem("Content").activate();
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(modelName).select();
		TeiidEditorTableItem sourceItem = new TeiidEditorTableItem(new DefaultTable(1).getItem(sourceName));
		sourceItem.select();
		sourceItem.setText(1, translatorName);
	}

	public String getDataSourceName(String modelName) {
		new DefaultCTabItem("Content").activate();
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(modelName).select();
		return new DefaultTable(1).getItem(0).getText(0);
	}

	public void synchronizeAll() {

		if (new PushButton("Synchronize All").isEnabled()) {
			new PushButton("Synchronize All").click();
		}
	}

	// TODO CHECK
	public void removeModel(String projectName, String model) {
		// ctab item models
		new DefaultCTabItem("Models").activate();
		new DefaultTable().getItem(model, 0).select();
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Remove selected model(s)").click();
		new PushButton("OK").click();
	}

	public void setGenerateRestWar(boolean check) {
		new DefaultCTabItem("Advanced").activate();
		new DefaultCTabItem("Properties").activate();
		new CheckBox(new DefaultGroup("General"), "Auto-generate REST WAR").toggle(check);
	}

	public DataRolesEditor addDataRole() {
		new DefaultCTabItem("Advanced").activate();
		new DefaultCTabItem("Data Roles").activate();
		new DefaultToolItem("Add data role").click();
		return new DataRolesEditor(DataRolesEditor.CREATE_TITLE);
	}

	public DataRolesEditor getDataRole(String roleName) {
		new DefaultCTabItem("Advanced").activate();
		new DefaultCTabItem("Data Roles").activate();

		new DefaultTable(0).getItem(roleName).select();
		new DefaultToolItem("Edit selected data role").click();

		return new DataRolesEditor(DataRolesEditor.EDIT_TITLE);
	}

	public void addTranslatorOverride(String overrideName, String translatorName) {
		new DefaultCTabItem("Advanced").activate();
		new DefaultCTabItem("Translator Overrides").activate();
		new PushButton(new WithTooltipTextMatcher("Add a translator whose properties you want to override")).click();

		new DefaultShell("Add Translator Override");
		new LabeledText("Name:").setText(overrideName);
		new DefaultTable().getItem(translatorName).select();
		new PushButton("OK").click();
	}

	public void addTranslatorOverrideProperty(String overrideName, String propertyName, String propertyValue) {
		new DefaultCTabItem("Advanced").activate();
		new DefaultCTabItem("Translator Overrides").activate();
		new DefaultTable(0).getItem(overrideName).select();

		if (new DefaultTable(1).containsItem(propertyName)) {
			// known property
			TeiidEditorTableItem item = new TeiidEditorTableItem(new DefaultTable(1).getItem(propertyName));
			item.select();
			item.setText(1, propertyValue);

		} else {
			// custom property
			new PushButton(new WithTooltipTextMatcher("Add a custom property")).click();
			new DefaultShell("Add New Property");
			new LabeledText("Name:").setText(propertyName);
			new LabeledText("Value:").setText(propertyValue);
			new PushButton("OK").click();
		}
	}

	public void addUserDefinedProperty(String name, String value) {
		new DefaultCTabItem("Advanced").activate();
		new DefaultCTabItem("User Defined Properties").activate();

		new PushButton(new WithTooltipTextMatcher("Add New Property")).click();

		new DefaultShell("Add New Property");
		new LabeledText("Name:").setText(name);
		new LabeledText("Value:").setText(value);

		new PushButton("OK").click();
	}

	public List<String> getTranslatorOverrides() {
		new DefaultCTabItem("Advanced").activate();
		new DefaultCTabItem("Translator Overrides").activate();

		List<String> result = new ArrayList<>();
		for (TableItem it : new DefaultTable(0).getItems()) {
			result.add(it.getText());
		}

		return result;
	}

	public Properties getTranslatorOverrideProperties(String translatorOverrideName) {
		new DefaultCTabItem("Advanced").activate();
		new DefaultCTabItem("Translator Overrides").activate();

		new DefaultTable(0).getItem(translatorOverrideName).select();

		Properties props = new Properties();

		for (TableItem it : new DefaultTable(1).getItems()) {
			props.put(it.getText(0), it.getText(1));
		}
		return props;

	}

}
