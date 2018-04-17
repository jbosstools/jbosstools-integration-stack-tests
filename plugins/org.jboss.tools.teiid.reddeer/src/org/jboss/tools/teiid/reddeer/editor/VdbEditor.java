package org.jboss.tools.teiid.reddeer.editor;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.lookup.WidgetLookup;
import org.eclipse.reddeer.core.matcher.WithTooltipTextMatcher;
import org.eclipse.reddeer.jface.viewers.CellEditor;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CancelButton;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.ccombo.DefaultCCombo;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.list.DefaultList;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
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

    @Override
    public void save() {
        if (new ShellMenuItem(new WorkbenchShell(), "File", "Save").isEnabled()) {
            super.save();
        }
    }

	public void addModelsToVDB(String projectName, String[] models){
		activate();
		String model = "";
        for (int i = 0; i < models.length; i++) {
            model = models[i];
            if (model.contains("/")) {
                addModel(model.split("/"));
            } else {
                addModel(projectName, model);
			}
		}
        save();
	}

	public void addModel(String... pathToModel) {
		int i = pathToModel.length - 1;
		pathToModel[i] = (pathToModel[i].contains(".xmi")) ? pathToModel[i] : pathToModel[i] + ".xmi"; 
		new DefaultToolItem("Add model").click();
        DefaultShell addFileDialog = new DefaultShell("Add File(s) to VDB");
        try {
            new DefaultTreeItem(pathToModel).select();
            new OkButton(addFileDialog).click();
        } catch (Exception ex) {
            log.warn(
                "Cannot add model to vdb or maybe model was added automatically. Path to model is: "
                        + pathToModel[pathToModel.length - 1]);
            new CancelButton(addFileDialog).click();
        }
		new WaitWhile(new ShellIsActive("Add File(s) to VDB"), TimePeriod.LONG);
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
		PushButton synchronize = new PushButton("Synchronize All");
		if(synchronize.isEnabled()) {
		    synchronize.click();
		}
        if (new ShellIsActive("Unsaved Models In Workspace").test()) {
		    new OkButton().click();
		}
        if (new ShellIsActive("Confirm").test()) {
            new OkButton().click();
        }
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
		new PushButton(5).click();
		return new DataRolesDialog(DataRolesDialog.CREATE_TITLE);
	}

	public DataRolesDialog getDataRole(String roleName) {
		new DefaultCTabItem("Data Roles").activate();
		new DefaultTable(0).getItem(roleName).select();
		new PushButton(6).click();
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
	
	public String getVersion(){
		return new LabeledText("Version").getText();
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
	public String getProjectDescription() {
		new DefaultCTabItem("Schemas").activate();	//because there are two description tabs
		new DefaultCTabItem("Description").activate();
		return new DefaultStyledText().getText();
	}

	public void addModelDescription(String editorSourceModel, String string) {
		editorSourceModel = (editorSourceModel.contains(".xmi")) ? editorSourceModel : editorSourceModel + ".xmi";
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(editorSourceModel).select();
		new DefaultCTabItem("Description").activate();		
		new DefaultStyledText().setText(string);
	}
	public String getModelDescription(String model) {
		model = (model.contains(".xmi")) ? model : model + ".xmi";
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(model).select();
		new DefaultCTabItem("Description").activate();		
		return new DefaultStyledText().getText();
	}

	public void addDefaultMultiSource(String editorSourceModel) {
		editorSourceModel = (editorSourceModel.contains(".xmi")) ? editorSourceModel : editorSourceModel + ".xmi";
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(editorSourceModel).select();
		new DefaultCTabItem("Source Binding Definition").activate();
		new CheckBox("Multi-source").click();
		new PushButton("Add").click();
	}
	public void addMultiSource(String editorSourceModel, String sourceName, String translatorName, String jndiName) {
		editorSourceModel = (editorSourceModel.contains(".xmi")) ? editorSourceModel : editorSourceModel + ".xmi";
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(editorSourceModel).select();
		new DefaultCTabItem("Source Binding Definition").activate();
		new CheckBox("Multi-source").click();
		new PushButton("Add").click();		
		new DefaultTable(1).getItem(new DefaultTable(1).rowCount()-1).click(0);		
		new DefaultText(new CellEditor(new DefaultTable(1).getItem(new DefaultTable(1).rowCount()-1), 0)).setText(sourceName);
		
		new DefaultCTabItem("Source Binding Definition").activate();
		new DefaultTable(1).getItem(new DefaultTable(1).rowCount()-1).click(1);
		new DefaultCCombo(new CellEditor(new DefaultTable(1).getItem(new DefaultTable(1).rowCount()-1), 1)).setSelection(translatorName);
		
		new DefaultCTabItem("Source Binding Definition").activate();
		new DefaultTable(1).getItem(new DefaultTable(1).rowCount()-1).click(2);		
		new DefaultCCombo(new CellEditor(new DefaultTable(1).getItem(new DefaultTable(1).rowCount()-1), 2)).setText(jndiName);
		
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
	public List<TableItem> getListOfModelProperties(String editorSourceModel) {
		editorSourceModel = (editorSourceModel.contains(".xmi")) ? editorSourceModel : editorSourceModel + ".xmi";
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(editorSourceModel).select();
		new DefaultCTabItem("Properties").activate();	
		return new DefaultTable(1).getItems();
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

	public void setAllowedLanguage(String language){
		new DefaultCTabItem("Properties").activate();
		new PushButton(new WithTooltipTextMatcher("Add new allowed language (i.e. pearl, javascript, etc...)")).click();
		new DefaultShell("Add Allowed Language");
		new DefaultText().setText(language);
		new PushButton("OK").click();
	}
	public String[] getListOfAllowedLanguage(){
		new DefaultCTabItem("Properties").activate();			
		return new DefaultList(new DefaultGroup("Allowed Languages")).getListItems();
	}
	
	public void checkAddColumnCheckbox(String model){
		new DefaultCTabItem("Models").activate();
		new DefaultTable(0).getItem(model + ".xmi").select();
		new DefaultCTabItem("Source Binding Definition").activate();
		if(!new CheckBox("Multi-source").isChecked()){
			new CheckBox("Multi-source").click();
		}
		new CheckBox("Add Column      ").click();
	}
	
	public boolean isCheckedMultiSource(String model){
		new DefaultCTabItem("Models").activate();	
		new DefaultCTabItem("Source Binding Definition").activate();
		new DefaultTable(0).getItem(model + ".xmi").select();
		return new CheckBox("Multi-source").isChecked();
	}
	
	public boolean isCheckedAddColumn(String model){
		new DefaultCTabItem("Models").activate();	
		new DefaultTable(0).getItem(model + ".xmi").select();
		new DefaultCTabItem("Source Binding Definition").activate();		
		return new CheckBox("Add Column      ").isChecked();
	}
	
	public List<TableItem> getListOfSources(String model){
		new DefaultCTabItem("Models").activate();	
		new DefaultTable(0).getItem(model + ".xmi").select();
		new DefaultCTabItem("Source Binding Definition").activate();
		return new DefaultTable(1).getItems();		
	}

    public List<TableItem> getListOfModels() {
        activate();
        new DefaultCTabItem("Models").activate();
        return new DefaultTable(0).getItems();
    }

    public void setFilterCondition(String condition, Boolean isDataRolesTab) {
        if(isDataRolesTab) {
            new LabeledText(new DefaultShell("New VDB Data Role"),"Filter").setText(condition);	
        }else {
            new LabeledText("Filter").setText(condition);
        }
    }

    public void clearFilterCondition() {
        new PushButton(new WithTooltipTextMatcher("Clear Filter")).click();
    }

    public void setFilterModelsType(String type , Boolean isDataRolesTab) {
        if(isDataRolesTab) {
            new DefaultCombo(new DefaultShell("New VDB Data Role")).setSelection(type);
        }else {
            new DefaultCombo().setSelection(type);
        }
    }

    public void deployVdb() {
        new PushButton("Deploy").click();
        new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
        new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
        if (new ShellIsAvailable("Create VDB Data Source").test()) {
            new DefaultShell("Create VDB Data Source");
            new PushButton("Create Source").click();
            new WaitWhile(new ShellIsActive("Create VDB Data Source"), TimePeriod.DEFAULT);
        }
    }

    public void testVdb() {
        new PushButton("Test").click();
        new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
        new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
    }

    public void saveAsXml() {
        new PushButton("Save as XML").click();
        AbstractWait.sleep(TimePeriod.MEDIUM);
    }
}
