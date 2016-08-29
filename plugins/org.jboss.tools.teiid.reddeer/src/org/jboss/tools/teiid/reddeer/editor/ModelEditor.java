package org.jboss.tools.teiid.reddeer.editor;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.core.matcher.AndMatcher;
import org.jboss.reddeer.swt.api.CTabItem;
import org.jboss.reddeer.swt.api.TabItem;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.spinner.LabeledSpinner;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.tools.teiid.reddeer.dialog.CriteriaBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ExpressionBuilderDialog;
import org.jboss.tools.teiid.reddeer.dialog.ReconcilerDialog;
import org.jboss.tools.teiid.reddeer.matcher.AttributeMatcher;
import org.jboss.tools.teiid.reddeer.matcher.IsTransformation;
import org.jboss.tools.teiid.reddeer.matcher.ModelColumnMatcher;
import org.jboss.tools.teiid.reddeer.matcher.ModelEditorItemMatcher;
import org.jboss.tools.teiid.reddeer.matcher.TableItemMatcher;
import org.jboss.tools.teiid.reddeer.modeling.ModelColumn;
import org.jboss.tools.teiid.reddeer.modeling.ModelProcedureParameter;
import org.jboss.tools.teiid.reddeer.modeling.ModelTable;
import org.jboss.tools.teiid.reddeer.widget.TeiidStyledText;

/**
 * This class represents Model Editor in Teiid Designer perspective.
 * 
 * @author apodhrad
 * 
 */
@Deprecated
public class ModelEditor extends SWTBotEditor {

	public static final String BASE_TABLES = "Base Tables";
	public static final String PROCEDURES = "Procedures";
	public static final String PROCEDURE_PARAMETERS = "Procedure Parameters";
	public static final String FOREIGN_KEYS = "Foreign Keys";
	public static final String PRIMARY_KEYS = "Primary Keys";
	public static final String UNIQUE_CONSTRAINTS = "Unique Constraints";
	public static final String TRANSFORMATION_DIAGRAM = "Transformation Diagram";
	public static final String MAPPING_DIAGRAM = "Mapping Diagram";
	public static final String PACKAGE_DIAGRAM = "Package Diagram";
	public static final String TABLE_EDITOR = "Table Editor";
	public static final String COLUMNS = "Columns";

	private SWTBotGefViewer viewer;

	public ModelEditor(String title) {
		super(new SWTWorkbenchBot().editorByTitle(title).getReference(), new SWTWorkbenchBot());
		new SWTWorkbenchBot().sleep(5 * 1000);
	}

	private GraphicalEditor getGraphicalEditor(String tabLabel) {
		final CTabItem tabItem = showTab(tabLabel);
		GraphicalEditor graphicalEditor = syncExec(new Result<GraphicalEditor>() {

			@Override
			public GraphicalEditor run() {
				Object obj = tabItem.getSWTWidget().getData();
				if (obj instanceof GraphicalEditor) {
					return (GraphicalEditor) obj;
				}
				return null;
			}
		});
		return graphicalEditor;
	}

	public SWTBotGefViewer getGraphicalViewer(String tabLabel) {
		final GraphicalEditor graphicalEditor = getGraphicalEditor(tabLabel);
		GraphicalViewer graphicalViewer = syncExec(new Result<GraphicalViewer>() {

			@Override
			public GraphicalViewer run() {
				Object obj = graphicalEditor.getAdapter(GraphicalViewer.class);
				if (obj instanceof GraphicalViewer) {
					return (GraphicalViewer) obj;
				}
				return null;
			}
		});

		return new SWTBotGefViewer(graphicalViewer);
	}

	public CTabItem showTabItem(String label) {
		DefaultCTabItem tabItem = new DefaultCTabItem(label);
		tabItem.activate();
		return tabItem;
	}

	public TabItem showSubTabItem(String label) {
		DefaultTabItem tabItem = new DefaultTabItem(label);
		tabItem.activate();
		return tabItem;
	}

	public CTabItem showTab(String label) {
		DefaultCTabItem tabItem = new DefaultCTabItem(label);
		tabItem.activate();
		return tabItem;
	}

	@Deprecated // use %ModelEditor.openTransformantion()
	public void showTransformation() {
		viewer = getGraphicalViewer(TRANSFORMATION_DIAGRAM);
		viewer.editParts(IsTransformation.isTransformation()).get(0).select();
		viewer.clickContextMenu("Edit");
		new SWTWorkbenchBot().sleep(5 * 1000);
	}

	@Deprecated // use TransformationEditor.openCriteriaBuilder()
	public CriteriaBuilderDialog openCriteriaBuilder() {
		bot.toolbarButtonWithTooltip("Criteria Builder").click();
		return new CriteriaBuilderDialog();
	}

	@Deprecated // use TransformationEditor.openExpressionBuilder()
	public ExpressionBuilderDialog openExpressionBuilder() {
		SWTBotToolbarButton tbb = bot.toolbarButtonWithTooltip("Expression Builder");
		tbb.click();
		System.out.println(tbb.getClass().getName());
		return new ExpressionBuilderDialog();
	}

	@Deprecated // use TransformationEditor.openReconciler()
	public ReconcilerDialog openReconciler() {
		bot.toolbarButtonWithTooltip("Reconcile Transformation SQL with Target Columns").click();
		return new ReconcilerDialog();
	}

	@Deprecated // TODO move to TransformationEditor
	public String getTransformation() {
		return bot.styledText(0).getText();
	}

	@Deprecated // use TransformationEditor
	public void setTransformation(String text) {
		new SWTWorkbenchBot().styledText(0).setText(text);
	}

	@Deprecated // TODO move to TransformationEditor
	public void typeTransformation(String text) {
		new DefaultStyledText(0);
		KeyboardFactory.getKeyboard().type(text);
	}

	@Deprecated // use TransformationEditor
	public void setCoursorPositionInTransformation(int position) {
		new DefaultStyledText(0).selectPosition(position);
		new TeiidStyledText(0).mouseClickOnCaret();
	}

	@Deprecated // use TransformationEditor
	public void saveAndValidateSql() {
		clickButtonOnToolbar("Save/Validate SQL");
	}

	@Deprecated // use TransforamtionEditor
	public void clickButtonOnToolbar(String button) {
		bot.toolbarButtonWithTooltip(button).click();
	}

	public ModelDiagram getModeDiagram(String label) {
		return getModelDiagram(label, PACKAGE_DIAGRAM);
	}

	public ModelDiagram getModelDiagram(String label, String tab) {
		SWTBotGefViewer viewer = getGraphicalViewer(tab);
		SWTBotGefEditPart editPart = viewer.getEditPart(label);
		if (editPart != null) {
			return new ModelDiagram(editPart);
			// return new ModelDiagram(viewer.getEditPart(label));
		} else {
			return null;
		}
	}

	public void selectParts(List<SWTBotGefEditPart> parts) {
		if (viewer == null) {
			viewer = getGraphicalViewer(MAPPING_DIAGRAM);
		}
		viewer.select(parts);
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	/**
	 * TODO should be in TableEditor (analyze dynamic VDB test)
	 */
	public List<ModelColumn> getColumns(String tableName) {
		List<ModelColumn> result = new ArrayList<ModelColumn>();

		setFocus();
		showTabItem(ModelEditor.TABLE_EDITOR);
		showSubTabItem(COLUMNS);
		DefaultTable table = new DefaultTable(0);
		for (TableItem it : table.getItems()) {
			ModelColumn c = new ModelColumn(it);
			if (c.getLocation().equalsIgnoreCase(tableName)) {
				result.add(c);
			}
		}

		return result;
	}

	/**
	 * TODO should be in TableEditor (analyze dynamic VDB test)
	 */
	public List<ModelTable> getTables() {
		List<ModelTable> result = new ArrayList<ModelTable>();

		setFocus();
		showTabItem(ModelEditor.TABLE_EDITOR);
		showSubTabItem(BASE_TABLES);
		DefaultTable table = new DefaultTable(0);
		for (TableItem it : table.getItems()) {
			result.add(new ModelTable(it));
		}

		return result;
	}

	/**
	 * TODO should be in RelationalModelEditor
	 * 
	 * @param removeFromTransformation
	 *            - whether remove column from transformation too
	 */
	public void deleteColumnFromTable(String tableName, String columnName, boolean removeFromTransformation) {
		setFocus();
		selectParts(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.TABLE, tableName)));
		selectParts(viewer.editParts(new AttributeMatcher(columnName, ModelEditorItemMatcher.TABLE, tableName)));
		new ContextMenu("Delete").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		PushButton button = (removeFromTransformation) ? new PushButton("Yes") : new PushButton("No");
		button.click();
	}

	/**
	 * TODO should be in RelationalModelEditor
	 */
	public void renameColumn(String tableName, String columnName, String newName) {
		setFocus();
		selectParts(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.TABLE, tableName)));
		selectParts(viewer.editParts(new AttributeMatcher(columnName, ModelEditorItemMatcher.TABLE, tableName)));
		new ContextMenu("Rename").select();
		new DefaultText(0).setText(newName);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
	}

	/**
	 * TODO should be in RelationalModelEditor
	 */
	public void setDataTypeToColumn(String tableName, String columnName, String dataType, Integer length) {
		setFocus();
		selectParts(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.TABLE, tableName)));
		selectParts(viewer.editParts(new AttributeMatcher(columnName, ModelEditorItemMatcher.TABLE, tableName)));
		new ContextMenu("Modeling", "Set Datatype").select();
		new DefaultShell("Select a Datatype");
		new DefaultText(0).setText(dataType);
		new DefaultTable().getItem(0).click();
		if (length != null) {
			new LabeledSpinner("'string' length value").setValue(length);
		}
		new PushButton("OK").click();
	}

	/**
	 * TODO only in dynamic VDB test 
	 * mmakovy: Since we do not have replacement yet, I added this method back because of
	 * DynamicVDBTest
	 */
	public ModelProcedureParameter getProcedureParameter(String procName, String parameterName) {
		setFocus();
		showTabItem(ModelEditor.TABLE_EDITOR);
		showSubTabItem(PROCEDURE_PARAMETERS);
		DefaultTable table = new DefaultTable(0);
		int locationIndex = table.getHeaderIndex("Location");
		int nameIndex = table.getHeaderIndex("Name");
		@SuppressWarnings("unchecked")
		List<TableItem> matchingItems = table.getItems(new AndMatcher(new TableItemMatcher(locationIndex, procName),
				new TableItemMatcher(nameIndex, parameterName)));
		if (matchingItems.size() == 1) {
			return new ModelProcedureParameter(matchingItems.get(0));
		} else {
			return null;
		}
	}

	/**
	 * TODO only in dynamic VDB test 
	 * mmakovy: Since we do not have replacement yet, I added this method back because of
	 * DynamicVDBTest
	 */
	public ModelColumn getColumn(String tableName, String columnName) {
		setFocus();
		showTabItem(ModelEditor.TABLE_EDITOR);
		showSubTabItem(COLUMNS);
		DefaultTable table = new DefaultTable(0);
		List<TableItem> matchingItems = table.getItems(new ModelColumnMatcher(tableName, columnName));
		if (matchingItems.size() == 1) {
			return new ModelColumn(matchingItems.get(0));
		} else {
			return null;
		}
	}

	/**
	 * TODO only in dynamic VDB test 
	 * mmakovy: Since we do not have replacement yet, I added this method back because of
	 * DynamicVDBTest
	 */
	public ModelTable getTable(String tableName) {

		setFocus();
		showTabItem(ModelEditor.TABLE_EDITOR);
		showSubTabItem(BASE_TABLES);
		DefaultTable table = new DefaultTable(0);

		int headerIndex = table.getHeaderIndex("Name");
		if (table.containsItem(tableName, headerIndex)) {
			return new ModelTable(table.getItem(tableName, headerIndex));
		} else {
			return null;
		}
	}
}
