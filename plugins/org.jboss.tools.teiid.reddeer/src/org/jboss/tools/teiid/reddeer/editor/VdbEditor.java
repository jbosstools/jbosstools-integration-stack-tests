package org.jboss.tools.teiid.reddeer.editor;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.widgets.Text;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.lookup.WidgetLookup;
import org.jboss.reddeer.core.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.jface.viewers.CellEditor;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.ccombo.DefaultCCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.teiid.reddeer.dialog.DataRolesDialog;

public class VdbEditor extends DefaultEditor {

	public VdbEditor(String name) {
		super(name);
		activate();
	}

	public static VdbEditor getInstance(String name) {
		name = (name.contains(".vdb")) ? name : name + ".vdb";
		return new VdbEditor(name);
	}
	
	public void saveAndClose(){
		save();
		close();
	}

	public void addModelsToVDB(String projectName, String[] models){
		activate();
		String model = "";
		try {
			for (int i = 0; i < models.length; i++) {
				model = models[i];
				if (model.contains("/")){
					addModel(model.split("/"));
				} else {
					addModel(projectName, model);
				}
			}
		} catch (Exception ex) {
			log.warn("Cannot add model " + model);
		}
		save();
	}

	public void addModel(String... pathToModel) {
		int i = pathToModel.length - 1;
		pathToModel[i] = (pathToModel[i].contains(".xmi")) ? pathToModel[i] : pathToModel[i] + ".xmi"; 
		new DefaultToolItem("Add model").click();
		new DefaultShell("Add File(s) to VDB");
		new DefaultTreeItem(pathToModel).select();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Add File(s) to VDB"), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public void setModelTranslator(String modelName, String sourceName, final String translatorName) {
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(modelName).select();
		new DefaultTable(1).getItem(sourceName).click(1);
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				((Text) WidgetLookup.getInstance().getFocusControl()).setText(translatorName);
				KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
			}
		});
	}

	public String getDataSourceName(String modelName) {
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(modelName).select();
		return new DefaultTable(1).getItem(0).getText(0);
	}
	
	public String getTranslatorName(String modelName) {
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(modelName).select();
		return new DefaultTable(1).getItem(0).getText(1);
	}

	public void synchronizeAll() {
		activate();
		AbstractWait.sleep(TimePeriod.SHORT);
		new PushButton("Synchronize All").click();
	}

	public void removeModel(String projectName, String model) {
		new DefaultCTabItem("Models").activate();
		new DefaultTable().getItem(model, 0).select();
		new DefaultToolItem("Remove selected model(s)").click();
		new PushButton("OK").click();
	}

	/**
	 * Enables/disables automatic WAR generation.
	 */
	public VdbEditor setGenerateRestWar(boolean check) {
		new DefaultCTabItem("UDF Jars").activate(); //show another tab because next command open Properties in the models tab (it isn't same like Properties tab in the header)
		new DefaultCTabItem("Properties").activate();
		new CheckBox(new DefaultGroup("General"), "Auto-generate REST WAR").toggle(check);
		return this;
	}

	public void setDescription(String description){
		new DefaultCTabItem("UDF Jars").activate(); //show another tab because next command open description in the models tab
		new DefaultCTabItem("Description").activate();
		new DefaultStyledText().setText(description);
	}
	
	public DataRolesDialog addDataRole() {
		new DefaultCTabItem("Data Roles").activate();
		new PushButton(2).click();
		return new DataRolesDialog(DataRolesDialog.CREATE_TITLE);
	}

	public DataRolesDialog getDataRole(String roleName) {
		new DefaultCTabItem("Data Roles").activate();
		new DefaultTable(0).getItem(roleName).select();
		new PushButton(3).click();
		return new DataRolesDialog(DataRolesDialog.EDIT_TITLE);
	}

	public void addTranslatorOverride(String overrideName, String translatorName) {
		new DefaultCTabItem("Translator Overrides").activate();
		new PushButton(new WithTooltipTextMatcher("Add a translator whose properties you want to override")).click();

		new DefaultShell("Add Translator Override");
		new LabeledText("Name:").setText(overrideName);
		new DefaultTable().getItem(translatorName).select();
		new PushButton("OK").click();
	}

	public void addTranslatorOverrideProperty(String overrideName, String propertyName, final String propertyValue) {
		new DefaultCTabItem("Translator Overrides").activate();
		new DefaultTable(0).getItem(overrideName).select();

		if (new DefaultTable(1).containsItem(propertyName)) {
			// known property
			new DefaultTable(1).getItem(propertyName).select();
			new DefaultTable(1).getItem(propertyName).click(1);
			Display.syncExec(new Runnable() {
				@Override
				public void run() {
					((Text) WidgetLookup.getInstance().getFocusControl()).setText(propertyValue);
					KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
				}
			});

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
		new DefaultCTabItem("User Properties").activate();

		new PushButton(new WithTooltipTextMatcher("Add New Property")).click();

		new DefaultShell("Add New Property");
		new LabeledText("Name:").setText(name);
		new LabeledText("Value:").setText(value);

		new PushButton("OK").click();
	}

	public List<String> getTranslatorOverrides() {
		new DefaultCTabItem("Translator Overrides").activate();

		List<String> result = new ArrayList<>();
		for (TableItem it : new DefaultTable(0).getItems()) {
			result.add(it.getText());
		}

		return result;
	}

	public Properties getTranslatorOverrideProperties(String translatorOverrideName) {
		new DefaultCTabItem("Translator Overrides").activate();

		new DefaultTable(0).getItem(translatorOverrideName).select();

		Properties props = new Properties();

		for (TableItem it : new DefaultTable(1).getItems()) {
			props.put(it.getText(0), it.getText(1));
		}
		return props;
	}
	
	public void setVersion(int version){
		new LabeledText("Version").setText(Integer.toString(version));
	}
	
	public void setImportVDB(String nameVDB,int version, boolean dataPolicies){
		new PushButton("Show Import VDBs").click();
		new DefaultShell("Import VDBs");
		TableItem vdb = new DefaultTable().getItem("sourceVDB");
		vdb.click(1);
		new DefaultText(new CellEditor(vdb),0).setText(Integer.toString(version));
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		vdb.click(2);
		new DefaultCCombo(new CellEditor(vdb)).setSelection(Boolean.toString(dataPolicies));
		vdb.click();
		new PushButton("OK").click();
	}
	/**
	 * 
	 * @param pathToSchema must contain string with ".xsd"
	 */
	public void addSchema(String... pathToSchema ){		
		new DefaultToolItem("Add model").click();
		new DefaultShell("Add File(s) to VDB");		
		new DefaultTreeItem(pathToSchema).select(); ;
		new PushButton("OK").click();
	}
	
	public void removeSchema(String schema) {
		schema = (schema.contains(".xsd")) ? schema : schema + ".xsd";
		new DefaultCTabItem("Schemas").activate();
		new DefaultTable(0).getItem(schema).click();		
		new DefaultToolItem("Remove selected schema(s)").click();
		new PushButton("OK").click();
	}

	/**
	 * 
	 * @param pathToModel must contain string with ".jar"
	 */
	public void addUDFJar(String... pathToModel) {
		new DefaultCTabItem("UDF Jars").activate();
		new DefaultToolItem("Add UDF jar file").click();
		new DefaultShell("Select UDF jar");
		new RadioButton("Choose the UDF jar from the workspace.").click();
		new PushButton("OK").click();
		new DefaultShell("Choose UDF jar");
		new DefaultTreeItem(pathToModel).select();
		new PushButton("OK").click();
	}
	
	public void removeUDFJar(String UDFname) {
		UDFname = (UDFname.contains(".jar")) ? UDFname : UDFname + ".jar";
		new DefaultCTabItem("UDF Jars").activate();
		new DefaultTable(0).getItem(UDFname).click();		
		new DefaultToolItem("Remove selected UDF jar file(s)").click();
		new PushButton("OK").click();
	}
	/**
	 * 
	 * @param pathToFile must contain string with type of file
	 */
	public void addOtherFile(String... pathToFile) {
		new DefaultCTabItem("Other Files").activate();	
		new DefaultToolItem("Add file").click();
		new RadioButton("Choose the File from the workspace.").click();
		new PushButton("OK").click();
		new DefaultShell("Choose File");
		new DefaultTreeItem(pathToFile).select();
		new PushButton("OK").click();
	}

	public void addProjectDescription(String string) {
		new DefaultCTabItem("Description").activate();
		new DefaultStyledText().setText(string);		
	}

	public void addModelDescription(String editorSourceModel, String string) {
		editorSourceModel = (editorSourceModel.contains(".xmi")) ? editorSourceModel : editorSourceModel + ".xmi";
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(editorSourceModel).select();
		new DefaultCTabItem("Description").activate();		
		new DefaultStyledText().setText(string);
	}

	public void addDefaultMultiSource(String editorSourceModel) {
		editorSourceModel = (editorSourceModel.contains(".xmi")) ? editorSourceModel : editorSourceModel + ".xmi";
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(editorSourceModel).select();
		new DefaultCTabItem("Source Binding Definition").activate();
		new CheckBox("Multi-source").click();
		new PushButton("Add").click();
	}

	public void deleteMultiSourceModel(String editorSourceModel, String name) {
		editorSourceModel = (editorSourceModel.contains(".xmi")) ? editorSourceModel : editorSourceModel + ".xmi";
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(editorSourceModel).select();
		new DefaultCTabItem("Source Binding Definition").activate();
		new DefaultTable(1).getItem(name).select();
		new PushButton("Delete").click();
	}

	public void addModelProperty(String editorSourceModel, String propertyName, String propertyValue) {
		editorSourceModel = (editorSourceModel.contains(".xmi")) ? editorSourceModel : editorSourceModel + ".xmi";
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(editorSourceModel).select();
		new DefaultCTabItem("Properties").activate();	
		new PushButton(new WithTooltipTextMatcher("Add New Property")).click();
		new DefaultShell("Add New Property");
		new LabeledText("Name:").setText(propertyName);
		new LabeledText("Value:").setText(propertyValue);
		new PushButton("OK").click();
	}

	public void deleteModelProperty(String editorSourceModel, String propertyName) {
		editorSourceModel = (editorSourceModel.contains(".xmi")) ? editorSourceModel : editorSourceModel + ".xmi";
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(editorSourceModel).select();
		new DefaultCTabItem("Properties").activate();
		new DefaultTable(1).getItem(propertyName).select();		
		new PushButton(new WithTooltipTextMatcher("Remove Property")).click();
	}

	public void editTranslatorOverride(String oldName, String newName) {
		new DefaultCTabItem("Translator Overrides").activate();
		new DefaultTable(0).getItem(oldName).select();
		new PushButton(new WithTooltipTextMatcher("Edit the selected translator name")).click();
		new LabeledText("Name:").setText(newName);
		new PushButton("OK").click();
	}

	public void editTranslatorOverrideProperty(String translatorName, String oldName, String newName) {
		new DefaultCTabItem("Translator Overrides").activate();
		new DefaultTable(0).getItem(translatorName).select();
		new DefaultTable(1).getItem(oldName).select();
		new PushButton(new WithTooltipTextMatcher("Edit the selected custom property")).click();
		new DefaultShell("Edit Property");
		new LabeledText("Name:").setText(newName);
		new PushButton("OK").click();
	}

	public void deleteTranslatorOverrideProperty(String translatorName, String propertyName) {
		new DefaultCTabItem("Translator Overrides").activate();
		new DefaultTable(0).getItem(translatorName).select();
		new DefaultTable(1).getItem(propertyName).select();
		new PushButton(new WithTooltipTextMatcher("Remove the selected custom property")).click();
		
	}

	public void deleteTranslatorOverride(String translatorName) {
		new DefaultCTabItem("Translator Overrides").activate();
		new DefaultTable(0).getItem(translatorName).select();		
		new PushButton(new WithTooltipTextMatcher("Remove the selected overridden translator (all default property values will be used)")).click();
		new PushButton("OK").click();
	}


	
}
