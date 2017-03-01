package org.jboss.tools.teiid.reddeer.view;

import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportFromFileSystemWizard;

public class PropertiesViewExt extends PropertiesView{
	
	/**
	 * Import udf to workspace and set udf to udf function
	 * @param projectName
	 * @param model  - model with udf function
	 * @param function - udf function
	 * @param udfName
	 * @param pathToUdf f.e. /home/...../myJava.jar
	 */
	public static void setUdf(String projectName, String model, String function, String udfName, String pathToUdf){
		ImportFromFileSystemWizard.openWizard()
				.setPath(pathToUdf)
				.setFolder(projectName)
				.selectFile(udfName)
				.setCreteTopLevelFolder(true)
				.finish();
	
		new ModelExplorer().openModelEditor(projectName, model + ".xmi", function);
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.getProperty("Extension", "relational:UDF Jar Path").getTreeItem().doubleClick();
		new PushButton("...").click();
			
		new DefaultShell("Select UDF jar");
		new PushButton("OK").click();
	
		new DefaultShell("Choose UDF jar");
		new DefaultTreeItem(projectName,"lib",udfName).select();
		new PushButton("OK").click();	
		new RelationalModelEditor(model+".xmi").save();
		new RelationalModelEditor(model+".xmi").close();
	}
}
