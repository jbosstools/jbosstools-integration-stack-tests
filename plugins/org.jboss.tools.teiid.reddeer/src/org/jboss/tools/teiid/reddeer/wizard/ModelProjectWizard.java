package org.jboss.tools.teiid.reddeer.wizard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.view.GuidesView;

/**
 * Wizard for creating a new model project
 * 
 * @author apodhrad
 * 
 */
public class ModelProjectWizard extends NewWizardDialog {

	public static final String CATEGORY = "Teiid Designer";
	public static final String PROJECT_TITLE = "Teiid Model Project";

	public ModelProjectWizard() {
		super(CATEGORY, PROJECT_TITLE);
	}
	
	public ModelProjectWizard(int currentPage) {
		super(CATEGORY, PROJECT_TITLE);
	}

	public void create(String name) {
		open();
		new ModelProjectPage().setProjectName(name);
		finish();
	}
	
	@Override
	public void open(){
		try {
			super.open();
		} catch (Exception e){
			new DefaultTreeItem(CATEGORY).collapse();
			new DefaultTreeItem(CATEGORY, PROJECT_TITLE).expand();
			new DefaultTreeItem(CATEGORY, PROJECT_TITLE).select();
			next();
		}
	}
	
	/**
	 * Create new model project via action sets
	 * @param name project name (e.g. MyFirstProject)
	 * @param viaGuides true -- via action from modelling guide, false -- via main menu
	 */
	public void create(String name, boolean viaGuides){
		if (viaGuides){
			new GuidesView().chooseAction("Model JDBC Source", "Define Teiid Model Project");
			new DefaultShell("Define Model Project");
			new SWTWorkbenchBot().button("New...").click();
			new DefaultShell("New Model Project");
			new LabeledText("Project name:").setText(name);
			//TODO: select folders - source, schema, view,...
			finish();
			new DefaultShell("Define Model Project");
			new PushButton("OK").click();
		} else {
			create(name);
		}
		
	}
	
	/**
	 * Create new model project via action sets
	 * @param name project name (e.g. MyFirstProject)
	 * @param viaGuides true -- via action from modelling guide, false -- via main menu
	 * @param folders 
	 */
	public void create(String name, boolean viaGuides, String... folders){
		if (viaGuides){
			new GuidesView().chooseAction("Model JDBC Source", "Define Teiid Model Project");
			new SWTWorkbenchBot().button("New...").click();
			
			//New Model Project (1. page)
			new ModelProjectPage().setProjectName(name);
			new SWTWorkbenchBot().button("&Next >").click();
			//Project References (2. page)
			new SWTWorkbenchBot().button("&Next >").click();
			//Model Project Options (3. page)
			checkSelectedFolders(folders);
			finish();
			
			new SWTWorkbenchBot().shell("Define Model Project").close();//this shell isn't active after executing method create(name)
		} else {
			create(name);
		}	
	}
	
	/**
	 * Checks selected folders
	 * @param folders the folders which should be created in a project
	 */
	private void checkSelectedFolders(String... folders){
		List<String> selectedFoldersArray = Arrays.asList(folders);
		String[] allFolders0 = {"sources", "views", "schemas", "web_services", "functions", "extensions"};
		HashMap<String, Integer> allFoldersMap = new HashMap<String, Integer>();
		
		for (int i = 0; i < allFolders0.length; i++){
			allFoldersMap.put(allFolders0[i], i);
		}
	
		for (String folderName : allFoldersMap.keySet()){
			if (selectedFoldersArray.contains(folderName)){
				new SWTWorkbenchBot().checkBox("Name", allFoldersMap.get(folderName)).select();
			} else {
				new SWTWorkbenchBot().checkBox("Name", allFoldersMap.get(folderName)).deselect();
			}
		}
		
	}
	
	

}
