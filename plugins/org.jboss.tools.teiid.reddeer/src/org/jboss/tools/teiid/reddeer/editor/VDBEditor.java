package org.jboss.tools.teiid.reddeer.editor;

import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class VDBEditor extends SWTBotEditor {
	
	public VDBEditor(String name){
		super(new SWTWorkbenchBot().editorByTitle(name).getReference(), new SWTWorkbenchBot());
	}
	
	public static VDBEditor getInstance(String name){
		VDBEditor editor = new VDBEditor(name);
		editor.show();
		return editor;
	}
	
	/**
	 * 
	 * @param projectName
	 * @param modelXmi whole name (with ending)
	 */
	public void addModel(String projectName, String modelXmi){
		if (! modelXmi.contains(".")){
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
	
	/**
	 * 
	 * @param projectName
	 * @param model
	 * @param longerPath true if path to model contains folders
	 */
	public void addModel(boolean longerPath, String... pathToModel){
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Add model").click();
		
		SWTBotShell shell = bot.shell("Add File(s) to VDB");
		shell.activate();
		shell.bot().tree(0).expandNode(pathToModel).select();

		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	public String getModel(int index){
		return new SWTWorkbenchBot().table(0).cell(index, 0);
	}

	public void synchronizeAll() {

		if (new PushButton("Synchronize All").isEnabled()){
			new PushButton("Synchronize All").click();
		}
	}

	//TODO CHECK
	public void removeModel(String projectName, String model) {
		//ctab item models
		new DefaultCTabItem("Models").activate();
		new DefaultTable().getItem(model, 0).select();
		new SWTWorkbenchBot().toolbarButtonWithTooltip("Remove selected model(s)").click();
		new PushButton("OK").click();
	}
	
	//TODO ALL FOLLOWING VIA PROPERTIES
	
	//1. ctab item models 
		//1a. model details
		//1b. source binding definition
		//1c. problems
	
	public void operateModelsTab(Properties props){
		
	}
	
	//2. ctab item udf jars!!!!!!!!!!!!!
		//tooltip add udf jar file 
	public void operateUdfJarsTab(Properties props){
		
	}
	
	
	//3. ctab item other files
		//tooltip add file
	public void operateOtherFilesTab(Properties props){
		
	}
	
	
	//4. ctab data roles !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//tooltip add data role, edit selected data roles, remove selected data roles
		//shell
	public void operateDataRolesTab(Properties props){
		
	}
	
	//5. ctab properties!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void operatePropertiesTab(Properties props){
		
	}
	
	//6. ctab description
	public void operateDescriptionTab(Properties props){
		
	}
	//7. ctab translator overrides
	public void operateTranslatorOverridesTab(Properties props){
	
}
}
