package org.jboss.tools.teiid.reddeer.editor;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefFigureCanvas;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefViewer;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.ui.IEditorReference;
import org.hamcrest.Matcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.core.matcher.AndMatcher;
import org.jboss.reddeer.swt.api.CTabItem;
import org.jboss.reddeer.swt.api.TabItem;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.spinner.LabeledSpinner;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.teiid.reddeer.matcher.AttributeMatcher;
import org.jboss.tools.teiid.reddeer.matcher.ButtonWithToolTipMatcher;
import org.jboss.tools.teiid.reddeer.matcher.IsTransformation;
import org.jboss.tools.teiid.reddeer.matcher.ModelColumnMatcher;
import org.jboss.tools.teiid.reddeer.matcher.ModelEditorItemMatcher;
import org.jboss.tools.teiid.reddeer.matcher.TableItemMatcher;
import org.jboss.tools.teiid.reddeer.matcher.WaitForFigure;
import org.jboss.tools.teiid.reddeer.matcher.WithBounds;
import org.jboss.tools.teiid.reddeer.matcher.WithLabel;
import org.jboss.tools.teiid.reddeer.modeling.ModelColumn;
import org.jboss.tools.teiid.reddeer.modeling.ModelProcedureParameter;
import org.jboss.tools.teiid.reddeer.modeling.ModelTable;
import org.jboss.tools.teiid.reddeer.widget.SWTBotGefFigure;
import org.jboss.tools.teiid.reddeer.widget.TeiidStyledText;

/**
 * This class represents Model Editor in Teiid Designer perspective.
 * 
 * @author apodhrad
 * 
 */
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
	private static final String DIAGRAM = "Diagram";
	private static final String INPUT_SET = "Input Set";

	private SWTBotGefViewer viewer;

	public ModelEditor(SWTBotEditor editor, SWTWorkbenchBot bot) {
		this(editor.getReference(), bot);
	}

	public ModelEditor(IEditorReference editorReference, SWTWorkbenchBot bot) {
		super(editorReference, bot);
		new SWTWorkbenchBot().sleep(5 * 1000);
	}

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

	public TabItem showSubTab(String label) {
		DefaultTabItem tabItem = new DefaultTabItem(label);
		tabItem.activate();
		return tabItem;
	}

	public void showTransformation() {
		viewer = getGraphicalViewer(TRANSFORMATION_DIAGRAM);
		viewer.editParts(IsTransformation.isTransformation()).get(0).select();
		viewer.clickContextMenu("Edit");
		new SWTWorkbenchBot().sleep(5 * 1000);
	}

	@Deprecated
	public void showMappingTransformation(String label) {

		viewer = getGraphicalViewer(MAPPING_DIAGRAM);
		viewer.getEditPart(label).select();
		viewer.clickContextMenu("Edit");

		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultShell().setFocus();
	}

	public CriteriaBuilder openCriteriaBuilder() {
		bot.toolbarButtonWithTooltip("Criteria Builder").click();
		return new CriteriaBuilder();
	}
	
	public ExpressionBuilder openExpressionBuilder(){
		SWTBotToolbarButton tbb =  bot.toolbarButtonWithTooltip("Expression Builder");
		tbb.click();
		System.out.println(tbb.getClass().getName());
		return new ExpressionBuilder();
	}

	public void setTransformationProcedureBody(String procedure) {
		String transformationText = getTransformation();
		transformationText = transformationText.replaceAll("<--.*-->;", procedure);

		TeiidStyledText styledText = new TeiidStyledText(0);
		styledText.setText(transformationText);
		styledText.navigateTo(2, procedure.length() / 2);
		styledText.mouseClickOnCaret();
	}

	/**
	 * 
	 * @param procedure
	 * @param notReplacingDefault
	 *            false if editor contains <--.*--> to be replaced, true otherwise
	 */
	public void setTransformationProcedureBody(String procedure, boolean notReplacingDefault) {
		String transformationText = getTransformation();// ""
		if (transformationText.equals("") || (notReplacingDefault)) {
			transformationText = procedure;
		} else {
			transformationText = transformationText.replaceAll("<--.*-->;", procedure);
		}

		TeiidStyledText styledText = new TeiidStyledText(0);
		styledText.setFocus();
		styledText.setText(transformationText);
		styledText.navigateTo(2, procedure.length() / 2);
		styledText.mouseClickOnCaret();
	}

	public String getTransformation() {
		return bot.styledText(0).getText();
	}

	public void setTransformation(String text) {
		new SWTWorkbenchBot().styledText(0).setText(text);
	}
	
	public void typeTransformation(String text) {
		new SWTWorkbenchBot().styledText(0).typeText(text);
	}

	public void saveAndValidateSql() {
		clickButtonOnToolbar("Save/Validate SQL");
	}

	public void clickButtonOnToolbar(String button) {
		bot.toolbarButtonWithTooltip(button).click();
	}

	public void showTransformation(String label) {
		SWTBotGefFigure figureBot = figureWithLabel(label);
		editFigure(figureBot);
	}

	public void editFigure(SWTBotGefFigure figureBot) {
		Rectangle rectangle = figureBot.getAbsoluteBounds();
		SWTBotGefFigureCanvas canvas = getFigureCanvas();
		canvas.mouseMoveLeftClick(rectangle.x + 1, rectangle.y + 1);
		canvas.contextMenu("Edit").click();
		bot().waitUntil(new DefaultCondition() {

			@Override
			public boolean test() throws Exception {
				try {
					bot.styledText();
					return true;
				} catch (WidgetNotFoundException wnfe) {
					return false;
				}
			}

			@Override
			public String getFailureMessage() {
				return "Process wasn't completed";
			}
		});
		bot().styledText();
	}

	private SWTBotGefFigureCanvas getFigureCanvas() {
		Matcher<FigureCanvas> matcher = widgetOfType(FigureCanvas.class);
		return new SWTBotGefFigureCanvas(bot.widget(matcher, 0));
	}

	public SWTBotGefFigure figureWithLabel(String label) {
		return figureWithLabel(label, 0);
	}

	public SWTBotGefFigure figureWithLabel(String label, int index) {
		Matcher<IFigure> matcher = new WithLabel(label);
		return new SWTBotGefFigure(figure(matcher, index));
	}

	public SWTBotGefFigure tFigure() {
		Matcher<IFigure> matcher = allOf(instanceOf(ImageFigure.class), new WithBounds(40, 60));
		return new SWTBotGefFigure(figure(matcher, 0));
	}

	public IFigure figure(Matcher<IFigure> matcher, int index) {
		SWTBotGefFigureCanvas canvas = getFigureCanvas();
		WaitForFigure waitForFigure = new WaitForFigure(matcher, (FigureCanvas) canvas.widget);
		bot().waitUntil(waitForFigure);
		return waitForFigure.get(index);
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

	public void deleteLabeledItem(String label) {
		viewer = getGraphicalViewer(MAPPING_DIAGRAM);
		viewer.select(label);
		viewer.clickContextMenu("Delete");
		log.info("Deleting labeled item " + label);
	}

	public Reconciler openReconciler() {
		bot.toolbarButtonWithTooltip("Reconcile Transformation SQL with Target Columns").click();
		SWTBotShell shell = bot.shell("Reconcile Virtual Target Columns");
		shell.activate();
		return new Reconciler(shell);
	}

	public void openInputSetEditor() {
		viewer = getGraphicalViewer(DIAGRAM);
		getModelDiagram(INPUT_SET, DIAGRAM).select();
		viewer.clickContextMenu("Edit");
	}

	public InputSetEditor openInputSetEditor(boolean param) {
		viewer = getGraphicalViewer(DIAGRAM);
		getModelDiagram(INPUT_SET, DIAGRAM).select();
		viewer.clickContextMenu("Edit Input Set");
		return new InputSetEditor();
	}

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
	 * @param removeFromTransformation - whether remove column from transformation too
	 */
	public void deleteColumnFromTable(String tableName, String columnName, boolean removeFromTransformation){
		setFocus();
		selectParts(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.TABLE, tableName)));
		selectParts(viewer.editParts(new AttributeMatcher(columnName, ModelEditorItemMatcher.TABLE, tableName)));
		new ContextMenu("Delete").select();	
		AbstractWait.sleep(TimePeriod.SHORT);
		PushButton button = (removeFromTransformation) ? new PushButton("Yes") : new PushButton("No");
		button.click();
	}
	
	public void renameColumn(String tableName, String columnName, String newName){
		setFocus();
		selectParts(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.TABLE, tableName)));
		selectParts(viewer.editParts(new AttributeMatcher(columnName, ModelEditorItemMatcher.TABLE, tableName)));
		new ContextMenu("Rename").select();
		new DefaultText(0).setText(newName);
		new SWTWorkbenchBot().activeShell().pressShortcut(Keystrokes.TAB);
	}
	
	public void setDataTypeToColumn(String tableName, String columnName, String dataType, Integer length){
		setFocus();
		selectParts(viewer.editParts(new ModelEditorItemMatcher(ModelEditorItemMatcher.TABLE, tableName)));
		selectParts(viewer.editParts(new AttributeMatcher(columnName, ModelEditorItemMatcher.TABLE, tableName)));
		new ContextMenu("Modeling","Set Datatype").select();
		new DefaultShell("Select a Datatype");
		new DefaultText(0).setText(dataType);
		new DefaultTable().getItem(0).click();
		if (length != null){
			new LabeledSpinner("'string' length value").setValue(length);
		}		
		new PushButton("OK").click();
	}

	public ModelProcedureParameter getProcedureParameters(String procName) {
		setFocus();
		showTabItem(ModelEditor.TABLE_EDITOR);
		showSubTabItem(BASE_TABLES);
		DefaultTable table = new DefaultTable(0);

		int headerIndex = table.getHeaderIndex("Location");
		if (table.containsItem(procName, headerIndex)) {
			return new ModelProcedureParameter(table.getItem(procName, headerIndex));
		} else {
			return null;
		}
	}

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

	// TODO transformation editor options - supports update
}
