package org.jboss.tools.teiid.reddeer.manager;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.ModelProject;
import org.jboss.tools.teiid.reddeer.Procedure;
import org.jboss.tools.teiid.reddeer.WAR;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.perspective.DatabaseDevelopmentPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.SQLResult;
import org.jboss.tools.teiid.reddeer.wizard.ModelProjectWizard;

public class ModelExplorerManager {

	/**
	 * Click on the project in Model Explorer
	 * 
	 * @return
	 */
	public ModelProject selectModelProject(String modelProjectName) {
		ModelExplorer me = new ModelExplorer();
		me.open();
		return me.getModelProject(modelProjectName);
	}

	/**
	 * New Teiid Model Project
	 * 
	 * @param modelProjectName
	 */
	public void createProject(String modelProjectName, boolean viaGuides) {
		new ModelProjectWizard(0).create(modelProjectName, viaGuides);
	}

	public void createProject(String modelProjectName) {
		new ModelProjectWizard(0).create(modelProjectName);
	}

	public void createProject(String modelProjectName, boolean viaGuides, String... folders) {
		new ModelProjectWizard(0).create(modelProjectName, viaGuides, folders);
	}

	public void changeConnectionProfile(String connectionProfile, String projectName, String... projectItem) {
		new ModelExplorer().changeConnectionProfile(connectionProfile, projectName, projectItem);
	}

	public void createDataSource(String modelExplorerViewConnSourceType, String connProfile,
			String... pathToSourceModel) {
		new ModelExplorer().createDataSource(modelExplorerViewConnSourceType, connProfile, pathToSourceModel);
		;
	}

	public void openModel(String... pathToModel) {
		if (!pathToModel[pathToModel.length - 1].contains(".")) {
			pathToModel[pathToModel.length - 1] = pathToModel[pathToModel.length - 1] + ".xmi";
		}
		new ModelExplorer().openModelEditor(pathToModel);
	}

	public WAR getWAR(String projectName, String warName) {
		return new WAR(projectName, warName);
	}

	public Procedure getProcedure(String project, String model, String procedure) {
		new ModelExplorer().open();
		new DefaultTreeItem(new DefaultTree(0), project, model, procedure).select();
		return new Procedure(project, model, procedure);
	}

	public Procedure createProcedure(String project, String model, String procedure) {
		return new ModelExplorer().newProcedure(project, model, procedure, true);
	}

	public ModelExplorer getModelExplorerView() {
		return new ModelExplorer();
	}

	public void previewModelObject(List<String> params, String... pathToObject) {
		new ModelExplorer().open();
		new DefaultTreeItem(new DefaultTree(0), pathToObject).select();
		new ContextMenu("Modeling", "Preview Data").select();
		try {
			new PushButton("Yes").click();
		} catch (Exception e) {

		}
		
		if ((params != null) && (!params.isEmpty())) {
			new DefaultShell("Preview Data");
			int i = 0;
			for (String paramName : params) {// expects the params are sorted
				new SWTWorkbenchBot().text(i).setText(paramName);
				i++;
			}
			new PushButton("OK").click();
		}
		
		new PushButton("OK").click();

	}

	public boolean checkPreviewOfModelObject(String previewSQL) {
		// wait while is in progress
		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		// wait while dialog Preview data... is active
		new WaitWhile(new ShellWithTextIsActive(new RegexMatcher("Preview.*")), TimePeriod.LONG);
		SQLResult result = DatabaseDevelopmentPerspective.getInstance().getSqlResultsView().getByOperation(previewSQL);
		return result.getStatus().equals(SQLResult.STATUS_SUCCEEDED);
	}
}
