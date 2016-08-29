package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ProblemsViewEx;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.STOPPED)
@OpenPerspective(value = TeiidPerspective.class)
public class ModelRefactoringTest {

	private static final String PROJECT_NAME = "ModelRefactoringProject";

	private Project project;

	@Before
	public void importProject() {
		TeiidBot teiidBot = new TeiidBot();
		new ModelExplorer().importProject(PROJECT_NAME);
		project = teiidBot.modelExplorer().getProject(PROJECT_NAME);
		project.refresh();
		AbstractWait.sleep(TimePeriod.SHORT);
		project.refresh(); // refresh again (getting desperate here)
	}

	@After
	public void deleteProject() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		try {
			new ShellMenu("File", "Close All").select();
		} catch (Exception ex) {
			// no editors open, ignore
		}

		new WorkbenchShell();
		new ModelExplorer();
		project.select();
		project.refresh();
		project.delete(true);
	}

	@Test
	public void renameSourceModel() {
		renameItem(project.getProjectItem("partssupplier.xmi"), "partssupplier_X.xmi");
		checkDependentModel("partssupplier_X", "partssupplier_view.xmi", "SUPPLIER");
		checkDependentModel("partssupplier_X", "views", "partssupplier_view_2.xmi", "SUPPLIER");
		new ProblemsViewEx().checkErrors();
	}

	@Test
	public void renameSourceModelInFolder() {
		renameItem(project.getProjectItem("sources", "books.xmi"), "books_X.xmi");
		checkDependentModel("books_X", "books_view_2.xmi", "PUBLISHERS");
		checkDependentModel("books_X", "views", "books_view.xmi", "PUBLISHERS");
		new ProblemsViewEx().checkErrors();
	}

	@Test
	public void renameViewModel() {
		renameItem(project.getProjectItem("partssupplier_view_3.xmi"), "partssupplier_view_X.xmi");
		checkDependentModel("partssupplier_view_X", "partssupplier_view_4.xmi", "SUPPLIER");
		new ProblemsViewEx().checkErrors();
	}

	@Test
	public void renameViewModelInFolder() {
		renameItem(project.getProjectItem("views", "partssupplier_view_2.xmi"), "partssupplier_view_X.xmi");
		checkDependentModel("partssupplier_view_X", "partssupplier_view_3.xmi", "SUPPLIER");
		new ProblemsViewEx().checkErrors();
	}

	@Test
	public void renameFolder() {
		renameItem(project.getProjectItem("sources"), "sources_X");
		checkDependentModel("books", "books_view_2.xmi", "PUBLISHERS");
		checkDependentModel("books", "views", "books_view.xmi", "PUBLISHERS");
		new ProblemsViewEx().checkErrors();
	}

	@Test
	public void moveSourceModel() {
		moveItem(project.getProjectItem("sources", "books.xmi"), "sources2");
		new WaitWhile(new JobIsRunning());
		new ProblemsViewEx().checkErrors();
		checkDependentModel("books", "books_view_2.xmi", "PUBLISHERS");
		checkDependentModel("books", "views", "books_view.xmi", "PUBLISHERS");
	}

	@Test
	public void moveSourceModelWithUpdate() {
		moveItem(project.getProjectItem("sources", "books.xmi"), "sources2");
		new WaitWhile(new JobIsRunning());

		// workaround designer bug
		updateImports("views", "books_view.xmi");
		updateImports("books_view_2.xmi");

		new ProblemsViewEx().checkErrors();
		checkDependentModel("books", "books_view_2.xmi", "PUBLISHERS");
		checkDependentModel("books", "views", "books_view.xmi", "PUBLISHERS");
	}

	@Test
	public void moveViewModelIntoFolder() {
		moveItem(project.getProjectItem("partssupplier_view_3.xmi"), "views");
		new ProblemsViewEx().checkErrors();
		checkDependentModel("partssupplier_view_3", "partssupplier_view_4.xmi", "SUPPLIER");
	}

	@Test
	public void moveViewModelIntoFolderWithUpdate() {
		moveItem(project.getProjectItem("partssupplier_view_3.xmi"), "views");

		updateImports("partssupplier_view_4.xmi");
		updateImports("views", "partssupplier_view_3.xmi");

		new ProblemsViewEx().checkErrors();
		checkDependentModel("partssupplier_view_3", "partssupplier_view_4.xmi", "SUPPLIER");
	}

	@Test
	public void moveViewModelOutOfFolder() {
		moveItem(project.getProjectItem("views", "partssupplier_view_2.xmi"));
		new ProblemsViewEx().checkErrors();
		checkDependentModel("partssupplier_view_2", "partssupplier_view_3.xmi", "SUPPLIER");
	}

	@Test
	public void moveViewModelOutOfFolderWithUpdate() {
		moveItem(project.getProjectItem("views", "partssupplier_view_2.xmi"));

		updateImports("partssupplier_view_2.xmi");
		updateImports("partssupplier_view_3.xmi");
		updateImports("partssupplier_view_4.xmi");

		new ProblemsViewEx().checkErrors();
		checkDependentModel("partssupplier_view_2", "partssupplier_view_3.xmi", "SUPPLIER");
	}

	@Test
	public void moveSourceModelIntoFolder() {
		moveItem(project.getProjectItem("partssupplier.xmi"), "sources2");

		ModelExplorer modelView = TeiidPerspective.getInstance().getModelExplorerView();
		modelView.openModelEditor(PROJECT_NAME, "partssupplier_view.xmi");
		modelView.openModelEditor(PROJECT_NAME, "views", "partssupplier_view_2.xmi");
		modelView.openModelEditor(PROJECT_NAME, "partssupplier_view_3.xmi");
		modelView.openModelEditor(PROJECT_NAME, "partssupplier_view_4.xmi");
		new ProblemsViewEx().checkErrors();
	}

	@Test
	public void moveSourceModelIntoFolderWithUpdate() {
		moveItem(project.getProjectItem("partssupplier.xmi"), "sources2");

		// workaround designer bug
		updateImports("partssupplier_view.xmi");
		updateImports("views", "partssupplier_view_2.xmi");
		updateImports("partssupplier_view_3.xmi");
		updateImports("partssupplier_view_4.xmi");
		updateImports("partssupplier_view.xmi");

		ModelExplorer modelView = TeiidPerspective.getInstance().getModelExplorerView();
		modelView.openModelEditor(PROJECT_NAME, "partssupplier_view.xmi");
		modelView.openModelEditor(PROJECT_NAME, "views", "partssupplier_view_2.xmi");
		modelView.openModelEditor(PROJECT_NAME, "partssupplier_view_3.xmi");
		modelView.openModelEditor(PROJECT_NAME, "partssupplier_view_4.xmi");
		new ProblemsViewEx().checkErrors();
	}

	@Test
	public void moveSourceModelOutOfFolder() {
		moveItem(project.getProjectItem("sources", "books.xmi"));
		new ProblemsViewEx().checkErrors();
		checkDependentModel("books", "books_view_2.xmi", "PUBLISHERS");
		checkDependentModel("books", "views", "books_view.xmi", "PUBLISHERS");
	}

	@Test
	public void moveSourceModelOutOfFolderWithUpdate() {
		moveItem(project.getProjectItem("sources", "books.xmi"));

		// workaround designer bug
		updateImports("views", "books_view.xmi");
		updateImports("books_view_2.xmi");

		new ProblemsViewEx().checkErrors();
		checkDependentModel("books", "books_view_2.xmi", "PUBLISHERS");
		checkDependentModel("books", "views", "books_view.xmi", "PUBLISHERS");
	}

	@Test
	public void renameFolderWithViewModel() {
		renameItem(project.getProjectItem("views"), "views_X");

		ModelExplorer modelView = TeiidPerspective.getInstance().getModelExplorerView();
		modelView.openModelEditor(PROJECT_NAME, "partssupplier_view.xmi");
		modelView.openModelEditor(PROJECT_NAME, "views_X", "partssupplier_view_2.xmi");
		modelView.openModelEditor(PROJECT_NAME, "partssupplier_view_3.xmi");
		modelView.openModelEditor(PROJECT_NAME, "partssupplier_view_4.xmi");

		new ProblemsViewEx().checkErrors();
	}

	private void updateImports(String... path) {
		new DefaultShell();
		new ModelExplorer();
		project.getProjectItem(path).select();
		new ContextMenu("Modeling", "Update Imports").select();
		new WaitWhile(new ShellWithTextIsAvailable("Progress Information"), TimePeriod.SHORT);
	}

	private void renameItem(ProjectItem projectItem, String newName) {
		projectItem.select();
		String oldName = projectItem.getName();
		new ContextMenu("Refactor", "Rename...").select();
		new DefaultShell("Rename Resource " + oldName);
		new LabeledText("New name:").setText(newName);
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive("Rename Resource " + oldName));
		assertFalse("Refactoring failed", new ShellWithTextIsAvailable("Rename Resource " + oldName).test());

	}

	private void moveItem(ProjectItem projectItem, String... newPath) {
		projectItem.select();
		new ContextMenu("Refactor", "Move...").select();
		new DefaultShell("Move Resource");
		new DefaultTree();
		String[] fullPath = new String[newPath.length + 1];
		fullPath[0] = PROJECT_NAME;
		System.arraycopy(newPath, 0, fullPath, 1, newPath.length);
		new DefaultTreeItem(fullPath).select();
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsActive("Move Resource"));
	}

	private void checkDependentModel(String expectedSourceTable, String... path) {
		ModelExplorer modelView = TeiidPerspective.getInstance().getModelExplorerView();
		modelView.openTransformationDiagram(PROJECT_NAME, path);

		ModelEditor me = new ModelEditor(path[path.length - 2]);
		AbstractWait.sleep(TimePeriod.NORMAL);
		me.showTransformation();
		assertTrue("Transformation not updated after refactoring",
				me.getTransformation().contains(expectedSourceTable));
	}

}
