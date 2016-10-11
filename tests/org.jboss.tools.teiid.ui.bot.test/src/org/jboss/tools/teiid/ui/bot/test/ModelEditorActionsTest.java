package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.dialog.IndexDialog;
import org.jboss.tools.teiid.reddeer.dialog.TableDialog;
import org.jboss.tools.teiid.reddeer.dialog.ViewDialog;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TableEditor.Tabs;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ProblemsViewEx;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.NewProcedureWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author mmajerni
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)

public class ModelEditorActionsTest {

	private static final String PROJECT_NAME = "ModelEditorActionsProject";	
	private static final String VIEW_MODEL_TABLE_DIALOG = "ViewTableDialog";	
	private static final String VIEW_MODEL_MANUAL_ADD_TABLE = "ViewManualAdd";
	private static final String VIEW_MODEL_PROCEDURE_MANUAL_FILL = "ViewProcedureManualFill";	
	private static final String VIEW_MODEL_INDEX = "ViewIndex";	
	private static final String SOURCE_MODEL_TABLE_DIALOG = "SourceTableDialog";
	private static final String SOURCE_MODEL_MANUAL_ADD_TABLE = "SourceManualAdd";
	private static final String SOURCE_MODEL_PROCEDURE_DIALOG = "SourceProcedureDialog";
	private static final String SOURCE_MODEL_PROCEDURE_MANUAL_FILL = "SourceProcedureManualFill";
	private static final String SOURCE_MODEL_VIEW_DIALOG = "SourceViewDialog";
	private static final String SOURCE_MODEL_INDEX_DIALOG = "SourceIndexDialog";
		
	private ModelExplorer modelExplorer;
	
	@BeforeClass
	public static void before(){
		new WorkbenchShell().maximize();
	}
	
	@Before
	public void createProject() {
		modelExplorer = new ModelExplorer();
		modelExplorer.createProject(PROJECT_NAME);	
		modelExplorer.maximize();
	}
	
	@After
	public void cleanUp(){
		new ModelExplorer().deleteAllProjectsSafely();
	}
	
	@Test
	public void viewTableDialog(){
		
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(VIEW_MODEL_TABLE_DIALOG)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL)
				.selectModelType(MetadataModelWizard.ModelType.VIEW)
				.finish();
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, VIEW_MODEL_TABLE_DIALOG +".xmi");
		
		// HELP TABLE
		new TableDialog(true)
				.setName("HelpingTable")
				.addColumn("helpColumn", "string", "4000")
				.addUniqueConstraint("UChelp", "UChelpSource", "helpColumn")
				.finish();

		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, VIEW_MODEL_TABLE_DIALOG +".xmi");
		new TableDialog(true)
				.setName("Table")
				.setNameInSource("TableInSource")
				//Properties TAB
				.setCardinality("1")
				.chceckSupportUpdateIsSystemTable(true, true)
				.setDescription("This is table description")
				//SQL tab
				.setDefaultSQL()			
				//Columns TAB
				.addColumn("PKcolumn", "biginteger", "4000")
				.addColumn("FKcolumn", "string", "4000")
				.addColumn("UCcolumn", "string", "100")
				.addColumn("test", "string", "100")
				.deleteColumn("test")
				//PK TAB
				.setPrimaryKey("PrimaryKey", "PrimaryKeySource", "PKcolumn")
				//UC TAB
				.addUniqueConstraint("UniqueConstraints", "UniqueConstraintsSource", "UCcolumn")
				.editUniqueConstraint("UniqueConstraints", "UniqueConstraint", "UniqueConstraintsSource", "UCcolumn")
				.addUniqueConstraint("DeleteTest", "DeleteTestSource", "UCcolumn")
				.deleteUniqueConstraint("DeleteTest", "UCcolumn")
				//FK TAB
				.addForeignKey("ForeignKeys", "ForeignKeysSource", "HelpingTable: UChelp", "FKcolumn")
				.editForeignKey("ForeignKeys","ForeignKey", "ForeignKeysSource", "HelpingTable: UChelp", "FKcolumn")
				.addForeignKey("test", "testSource", "HelpingTable: UChelp", "FKcolumn")
				.deleteForeignKey("test")
				//Index TAB
				.addIndex("Indexes", "UCcolumn", false,false,false)
				.editIndex("Indexes", "Index", "UCcolumn", false,false,false)
				.addIndex("test", "UCcolumn", true, true, true)
				.deleteIndex("test", "UCcolumn");
					
		new DefaultShell("Create Relational View Table");
		new DefaultTabItem("Columns").activate();
		assertEquals(3,new DefaultTable(0).rowCount());	
		
		new DefaultTabItem("UC").activate();
		assertEquals("UniqueConstraint : UCcolumn" , new DefaultTable(0).getItem(0).getText());	
		assertEquals(1,new DefaultTable(0).rowCount());	
		
		new DefaultTabItem("FKs").activate();
		assertEquals("ForeignKey" , new DefaultTable(0).getItem(0).getText());
		assertEquals(1,new DefaultTable(0).rowCount());	

		new DefaultTabItem("Indexes").activate();
		assertEquals("Index : UCcolumn" , new DefaultTable(0).getItem(0).getText());
		assertEquals(1,new DefaultTable(0).rowCount());	
		
		new PushButton("OK").click();		
			
  		org.jboss.tools.teiid.reddeer.editor.TableEditor tableEditor = new RelationalModelEditor( VIEW_MODEL_TABLE_DIALOG +".xmi").openTableEditor();
		
		tableEditor.openTab("Foreign Keys");
		assertEquals("FKcolumn : string(4000)", tableEditor.getCellText(0, "Columns"));
		tableEditor.openTab("Primary Keys");
		assertEquals("PKcolumn : biginteger(4000)", tableEditor.getCellText(0, "Columns"));
		tableEditor.openTab("Unique Constraints");
		assertEquals("helpColumn : string(4000)", tableEditor.getCellText(0, "Columns"));
		assertEquals("UCcolumn : string(100)", tableEditor.getCellText(1, "Columns"));
		tableEditor.openTab("Columns");
		assertEquals("UChelp", tableEditor.getCellText(0, "Unique Keys"));
		assertEquals("PrimaryKey", tableEditor.getCellText(1, "Unique Keys"));
		assertEquals("ForeignKey", tableEditor.getCellText(2, "Foreign Keys"));
		tableEditor.openTab("Base Tables");
		assertEquals("TableInSource", tableEditor.getCellText(1, "Name In Source"));
		assertEquals("This is table description", tableEditor.getCellText(1, "Description")); 		
		tableEditor.save();	
	}
	
	@Test
	public void viewManualFillTable(){
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(VIEW_MODEL_MANUAL_ADD_TABLE)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL)
				.selectModelType(MetadataModelWizard.ModelType.VIEW)
				.finish();
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi");
		
		new TableDialog(true)
				.setName("TableManual")
				.finish();
		
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor(VIEW_MODEL_MANUAL_ADD_TABLE +".xmi").openTableEditor();
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");		
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(0, "Name", "APcolumn");
		editor.setCellText(0, "Length", "100");
		editor.setDatatype("Columns",0, 29, "string");
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(1, "Name", "FKcolumn");
		editor.setCellText(1, "Length", "500");
		editor.setDatatype("Columns",1, 29, "int");
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");	
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(2, "Name", "PKcolumn");
		editor.setDatatype("Columns",2, 29, "boolean");
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(3, "Name", "UCcolumn");
		editor.setCellText(3, "Length", "4000");
		editor.setDatatype("Columns",3, 29, "decimal");	
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.ACCESS_PATTERN, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual","APcolumn : string(100)");
		editor.openTab(Tabs.ACCESS_PATTERNS);
		editor.setCellText(0, "Name", "AccessPattern");
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.FOREIGN_KEY, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual","FKcolumn : int");
		editor.openTab(Tabs.FOREIGN_KEYS);
		editor.setCellText(0, "Name", "ForeignKey");
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.PRIMARY_KEY, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual","PKcolumn : boolean");
		editor.openTab(Tabs.PRIMARY_KEYS);
		editor.setCellText(0, "Name", "PrimaryKey");		
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.UNIQUE_CONSTRAINT, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual","UCcolumn : decimal");
		editor.openTab(Tabs.UNIQUE_CONSTRAINTS);
		editor.setCellText(0, "Name", "UniqueConstraint");		

		editor.openTab("Access Patterns");
		assertEquals("APcolumn : string(100)", editor.getCellText(0, "Columns"));		
		editor.openTab("Foreign Keys");
		assertEquals("FKcolumn : int", editor.getCellText(0, "Columns"));
		editor.openTab("Primary Keys");
		assertEquals("PKcolumn : boolean", editor.getCellText(0, "Columns"));
		editor.openTab("Unique Constraints");
		assertEquals("UCcolumn : decimal", editor.getCellText(0, "Columns"));
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");
		new TableDialog(true)
				.setName("SiblingTable")
				.finish();		

		editor.save();
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");		
		NewProcedureWizard.createViewProcedure()
				.setName("SiblingProcedure")
				.finish();			

		editor.save();
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.INDEX, PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");
		new IndexDialog(true)
				.setName("SiblingIndex")
				.finish();			
	
		editor.save();
		
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi","SiblingTable"));
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi","SiblingProcedure"));
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, VIEW_MODEL_MANUAL_ADD_TABLE +".xmi","SiblingIndex"));
	}
	
	@Test 
	public void manualFillProcedureView(){
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(VIEW_MODEL_PROCEDURE_MANUAL_FILL)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL)
				.selectModelType(MetadataModelWizard.ModelType.VIEW)
				.finish();
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, VIEW_MODEL_PROCEDURE_MANUAL_FILL +".xmi");
		NewProcedureWizard.createViewProcedure()
				.setName("Procedure")
				//.setTransformationSql("BEGIN SELECT 'test' AS A; END")
				.finish();			
			
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor(VIEW_MODEL_PROCEDURE_MANUAL_FILL +".xmi").openTableEditor();
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE_PARAMETER, PROJECT_NAME,VIEW_MODEL_PROCEDURE_MANUAL_FILL +".xmi", "Procedure");
		editor.openTab(Tabs.PROCEDURE_PARAMETERS);
		editor.setCellText(0, "Name", "ProcedureParameter");
		editor.setCellText(0, "Length", "100");
		editor.setDatatype("Procedure Parameters", 0, 11, "string");	
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE_RESULTSET, PROJECT_NAME,VIEW_MODEL_PROCEDURE_MANUAL_FILL +".xmi", "Procedure");
		editor.openTab(Tabs.PROCEDURE_RESULTS);
		editor.setCellText(0, "Name", "ProcedureResult");
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME,VIEW_MODEL_PROCEDURE_MANUAL_FILL +".xmi","Procedure", "ProcedureResult");
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(0, "Name", "Column");
		editor.setDatatype("Columns", 0, 29, "int");	
		
		//ProblemsViewEx.checkErrors();
		
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, VIEW_MODEL_PROCEDURE_MANUAL_FILL +".xmi", "Procedure", "ProcedureParameter : string(100)" ));
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, VIEW_MODEL_PROCEDURE_MANUAL_FILL +".xmi", "Procedure", "ProcedureResult"));
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, VIEW_MODEL_PROCEDURE_MANUAL_FILL +".xmi", "Procedure", "ProcedureResult", "Column : int"));
	}
	
	@Test 
	public void viewIndexDialog(){
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(VIEW_MODEL_INDEX)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL)
				.selectModelType(MetadataModelWizard.ModelType.VIEW)
				.finish();
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME,VIEW_MODEL_INDEX +".xmi");
		
		new TableDialog(true)
				.setName("Table")
				.finish();
		
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor(VIEW_MODEL_INDEX +".xmi").openTableEditor();
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME,VIEW_MODEL_INDEX +".xmi", "Table");		
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(0, "Name", "Column");		
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.INDEX, PROJECT_NAME,VIEW_MODEL_INDEX +".xmi");
		
		new IndexDialog(true)
			.setName("Index")
			.setNameInSource("IndexSource")
			.addReferencedColumns("Table", "Column",true)
			.addProperties(true, true, true)
			.addDescription("This is Index description")
			.finish();
		
		editor.save();		
		AbstractWait.sleep(TimePeriod.getCustom(5));
		editor.activate();
		editor.openTab("Indexes");
		assertEquals("Index",  editor.getCellText(0, "Name"));
		//assertEquals("IndexSource", editor.getCellText(0, "Name In Source"));		//BUG??
		assertEquals("true", editor.getCellText(0, "Nullable"));
		assertEquals("true", editor.getCellText(0, "Auto Update"));
		assertEquals("true", editor.getCellText(0, "Unique"));
		assertEquals("Column", editor.getCellText(0, "Columns"));
		assertEquals("This is Index description",editor.getCellText(0, "Description"));		
	}
	
	@Test
	public void sourceTableDialog(){		
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(SOURCE_MODEL_TABLE_DIALOG)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL)
				.selectModelType(MetadataModelWizard.ModelType.SOURCE)
				.finish();
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, SOURCE_MODEL_TABLE_DIALOG +".xmi");
		
		// HELP TABLE
		new TableDialog(false)
				.setName("HelpingTable")
				.addColumn("helpColumn", "string", "4000")
				.addUniqueConstraint("UChelp", "UChelpSource", "helpColumn")
				.finish();		
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, SOURCE_MODEL_TABLE_DIALOG +".xmi");
		new TableDialog(false)
				.setName("Table")
				.setNameInSource("TableInSource")
				//Properties TAB
				.setCardinality("1")
				.chceckSupportUpdateIsSystemTable(true, true)
				.setDescription("This is table description")
				//Columns TAB
				.addColumn("PKcolumn", "biginteger", "4000")
				.addColumn("FKcolumn", "string", "4000")
				.addColumn("UCcolumn", "string", "100")
				.addColumn("IndexColumn", "string", "256")
				.addColumn("test", "string", "100")
				.deleteColumn("test")
				//PK TAB
				.setPrimaryKey("PrimaryKey", "PrimaryKeySource", "PKcolumn")
				//UC TAB
				.addUniqueConstraint("UniqueConstraints", "UniqueConstraintsSource", "UCcolumn")
				.editUniqueConstraint("UniqueConstraints", "UniqueConstraint", "UniqueConstraintsSource", "UCcolumn")
				.addUniqueConstraint("DeleteTest", "DeleteTestSource", "UCcolumn")
				.deleteUniqueConstraint("DeleteTest", "UCcolumn")
				//FK TAB
				.addForeignKey("ForeignKeys", "ForeignKeysSource", "HelpingTable: UChelp", "FKcolumn")
				.editForeignKey("ForeignKeys","ForeignKey", "ForeignKeysSource", "HelpingTable: UChelp", "FKcolumn")
				.addForeignKey("test", "testSource", "HelpingTable: UChelp", "FKcolumn")
				.deleteForeignKey("test")
				//Index TAB
				.addIndex("Indexes", "IndexColumn", false,false,false)				
				.editIndex("Indexes", "Index", "IndexColumn", false,false,false)
				.addIndex("test", "IndexColumn", true, true, true)
				.deleteIndex("test", "IndexColumn")
				.finish();			
		
		
  		org.jboss.tools.teiid.reddeer.editor.TableEditor tableEditor = new RelationalModelEditor( SOURCE_MODEL_TABLE_DIALOG +".xmi").openTableEditor();
		
		tableEditor.openTab("Foreign Keys");
		assertEquals("FKcolumn : string(4000)", tableEditor.getCellText(0, "Columns"));
		tableEditor.openTab("Primary Keys");
		assertEquals("PKcolumn : biginteger(4000)", tableEditor.getCellText(0, "Columns"));
		tableEditor.openTab("Unique Constraints");
		assertEquals("helpColumn : string(4000)", tableEditor.getCellText(0, "Columns"));
		assertEquals("UCcolumn : string(100)", tableEditor.getCellText(1, "Columns"));
		tableEditor.openTab("Columns");
		assertEquals("UChelp", tableEditor.getCellText(0, "Unique Keys"));
		assertEquals("PrimaryKey", tableEditor.getCellText(1, "Unique Keys"));
		assertEquals("ForeignKey", tableEditor.getCellText(2, "Foreign Keys"));
		tableEditor.openTab("Base Tables");
		assertEquals("TableInSource", tableEditor.getCellText(1, "Name In Source"));
		assertEquals("This is table description", tableEditor.getCellText(1, "Description")); 		
		tableEditor.save();				
	}
	
	@Test
	public void sourceManualFillTable(){
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName( SOURCE_MODEL_MANUAL_ADD_TABLE)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL)
				.selectModelType(MetadataModelWizard.ModelType.SOURCE)
				.finish();
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi");
		
		new TableDialog(false)
				.setName("TableManual")
				.finish();
		
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor( SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi").openTableEditor();
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");		
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(0, "Name", "APcolumn");
		editor.setCellText(0, "Length", "100");
		editor.setDatatype("Columns", 0, 29, "string");		
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(1, "Name", "FKcolumn");
		editor.setCellText(1, "Length", "500");
		editor.setDatatype("Columns", 1, 29, "int");
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");	
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(2, "Name", "PKcolumn");
		editor.setDatatype("Columns", 2, 29, "boolean");
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(3, "Name", "UCcolumn");
		editor.setCellText(3, "Length", "4000");
		editor.setDatatype("Columns", 3, 29, "decimal");	
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.ACCESS_PATTERN, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual","APcolumn : string(100)");
		editor.openTab(Tabs.ACCESS_PATTERNS);
		editor.setCellText(0, "Name", "AccessPattern");
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.FOREIGN_KEY, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual","FKcolumn : int");
		editor.openTab(Tabs.FOREIGN_KEYS);
		editor.setCellText(0, "Name", "ForeignKey");
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.PRIMARY_KEY, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual","PKcolumn : boolean");
		editor.openTab(Tabs.PRIMARY_KEYS);
		editor.setCellText(0, "Name", "PrimaryKey");		
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.UNIQUE_CONSTRAINT, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual","UCcolumn : decimal");
		editor.openTab(Tabs.UNIQUE_CONSTRAINTS);
		editor.setCellText(0, "Name", "UniqueConstraint");
		
		editor.openTab("Access Patterns");
		assertEquals("APcolumn : string(100)", editor.getCellText(0, "Columns"));		
		editor.openTab("Foreign Keys");
		assertEquals("FKcolumn : int", editor.getCellText(0, "Columns"));
		editor.openTab("Primary Keys");
		assertEquals("PKcolumn : boolean", editor.getCellText(0, "Columns"));
		editor.openTab("Unique Constraints");
		assertEquals("UCcolumn : decimal", editor.getCellText(0, "Columns"));
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");
		new TableDialog(false)
				.setName("SiblingTable")
				.finish();	
		
		editor.save();
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");		
		NewProcedureWizard.createSourceProcedure()		
				.setName("SiblingProcedure")
				.finish();
		
		editor.save();		
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.VIEW, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");
		new ViewDialog()
				.setName("SiblingView")
				.finish();	
		
		editor.save();	
		
		modelExplorer.addSiblingToModelItem(ModelExplorer.ChildType.INDEX, PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi", "TableManual");
		new IndexDialog(false)
				.setName("SiblingIndex")
				.finish();	
		
		editor.save();			
		
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi","SiblingTable"));
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi","SiblingProcedure"));
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi","SiblingView"));
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, SOURCE_MODEL_MANUAL_ADD_TABLE +".xmi","SiblingIndex"));		
	}
	
	@Test
	public void sourceProcedureDialog(){
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(SOURCE_MODEL_PROCEDURE_DIALOG)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL)
				.selectModelType(MetadataModelWizard.ModelType.SOURCE)
				.finish();
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, SOURCE_MODEL_PROCEDURE_DIALOG +".xmi");
		
		NewProcedureWizard.createSourceProcedure()		
				.setName("Procedure")
				.setNameInSource("ProcedureSource")
				.setPropertyTab("ONE", true)
				.addParameter("Parameter", "double", "100", "OUT")
				.addParameter("test", "string", "4000", "IN")
				.deleteParameter("test")
				.toggleResultSet(true)
				.setResultSetName("ResultSet")
				.addResultSetColumn("Column", "string", "4000")
				.setDescription("This is procedure description in source model")
				.finish();					
		
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor(SOURCE_MODEL_PROCEDURE_DIALOG +".xmi").openTableEditor();
		editor.openTab("Columns");
		assertEquals("Column", editor.getCellText(0, "Name"));	
		assertEquals("4000", editor.getCellText(0, "Length"));	
		assertEquals("string", editor.getCellText(0, "Datatype"));	
		editor.openTab("Procedure Parameters");
		assertEquals("Parameter", editor.getCellText(0, "Name"));	
		assertEquals("OUT", editor.getCellText(0, "Direction"));
		assertEquals("100", editor.getCellText(0, "Length"));	
		assertEquals("double", editor.getCellText(0, "Datatype"));	
		editor.openTab("Procedure Results");
		assertEquals("ResultSet", editor.getCellText(0, "Name"));	
		editor.openTab("Procedures");
		assertEquals("Procedure", editor.getCellText(0, "Name"));	
		assertEquals("ProcedureSource", editor.getCellText(0, "Name In Source"));	
		assertEquals("ONE", editor.getCellText(0, "Update Count"));
		assertEquals("This is procedure description in source model", editor.getCellText(0, "Description"));	
		editor.save();
	}
	
	@Test 
	public void manualFillProcedureSource(){
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(SOURCE_MODEL_PROCEDURE_MANUAL_FILL)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL)
				.selectModelType(MetadataModelWizard.ModelType.SOURCE)
				.finish();
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE, PROJECT_NAME, SOURCE_MODEL_PROCEDURE_MANUAL_FILL +".xmi");
		
		NewProcedureWizard.createSourceProcedure()
				.setName("Procedure")
				.finish();		
		
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor(SOURCE_MODEL_PROCEDURE_MANUAL_FILL +".xmi").openTableEditor();
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE_PARAMETER, PROJECT_NAME,SOURCE_MODEL_PROCEDURE_MANUAL_FILL +".xmi", "Procedure");
		editor.openTab(Tabs.PROCEDURE_PARAMETERS);
		editor.setCellText(0, "Name", "ProcedureParameter");
		editor.setCellText(0, "Length", "100");
		editor.setDatatype("Procedure Parameters", 0, 11, "string");		
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.PROCEDURE_RESULTSET, PROJECT_NAME,SOURCE_MODEL_PROCEDURE_MANUAL_FILL +".xmi", "Procedure");
		editor.openTab(Tabs.PROCEDURE_RESULTS);
		editor.setCellText(0, "Name", "ProcedureResult");
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME,SOURCE_MODEL_PROCEDURE_MANUAL_FILL +".xmi","Procedure", "ProcedureResult");
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(0, "Name", "Column");
		editor.setDatatype("Columns", 0, 29, "int");		
	
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, SOURCE_MODEL_PROCEDURE_MANUAL_FILL +".xmi", "Procedure", "ProcedureParameter : string(100)" ));
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, SOURCE_MODEL_PROCEDURE_MANUAL_FILL +".xmi", "Procedure", "ProcedureResult"));
		assertEquals(true, modelExplorer.containsItem(PROJECT_NAME, SOURCE_MODEL_PROCEDURE_MANUAL_FILL +".xmi", "Procedure", "ProcedureResult", "Column : int"));
	}
	
	@Test 
	public void sourceViewDialog(){
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(SOURCE_MODEL_VIEW_DIALOG)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL)
				.selectModelType(MetadataModelWizard.ModelType.SOURCE)
				.finish();
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME, SOURCE_MODEL_VIEW_DIALOG +".xmi");
		
		// HELP TABLE
		new TableDialog(false)
				.setName("HelpingTable")
				.addColumn("helpColumn", "string", "4000")
				.addUniqueConstraint("UChelp", "UChelpSource", "helpColumn")
				.finish();
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.VIEW, PROJECT_NAME, SOURCE_MODEL_VIEW_DIALOG +".xmi");
	
		new ViewDialog()
				.setName("View")
				.setNameInSource("ViewSource")
				.setCardinality("2")
				.chceckSupportUpdateIsSystemTable(true, true)
				//.setMaterializedTable("HelpingTable")			//TEIID 2922
				.setDescription("This is View description")
				.addColumn("Column", "string", "4000")
				.finish();
		
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor(SOURCE_MODEL_VIEW_DIALOG +".xmi").openTableEditor();
		editor.openTab("Views");
		
		assertEquals("View", editor.getCellText(0, "Name"));
		assertEquals("ViewSource", editor.getCellText(0, "Name In Source"));
		assertEquals("true", editor.getCellText(0, "System"));
		//assertEquals("2",editor.getCellText(0, "Cardinality"));		//TEIID 2923
		assertEquals("true",editor.getCellText(0, "Supports Update"));
		//assertEquals("true", editor.getCellText(0, "Materialized"));
		//assertEquals("HelpingTable", editor.getCellText(0, "Materialized Table"));
		assertEquals("This is View description", editor.getCellText(0, "Description"));		

	}
	
	@Test 
	public void sourceIndexDialog(){
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME)
				.setModelName(SOURCE_MODEL_INDEX_DIALOG)
				.selectModelClass(MetadataModelWizard.ModelClass.RELATIONAL)
				.selectModelType(MetadataModelWizard.ModelType.SOURCE)
				.finish();
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.TABLE, PROJECT_NAME,SOURCE_MODEL_INDEX_DIALOG +".xmi");		
		new TableDialog(false)
				.setName("Table")
				.finish();
		
		org.jboss.tools.teiid.reddeer.editor.TableEditor editor = new RelationalModelEditor(SOURCE_MODEL_INDEX_DIALOG +".xmi").openTableEditor();
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.COLUMN, PROJECT_NAME, SOURCE_MODEL_INDEX_DIALOG +".xmi", "Table");		
		editor.openTab(Tabs.COLUMNS);
		editor.setCellText(0, "Name", "Column");		
		
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.INDEX, PROJECT_NAME,SOURCE_MODEL_INDEX_DIALOG +".xmi");		
		new IndexDialog(false)
			.setName("Index")
			.setNameInSource("IndexSource")
			.addReferencedColumns("Table", "Column",false)
			.addProperties(true, true, true)
			.addDescription("This is Index description")
			.finish();
		
		editor.save();		
		AbstractWait.sleep(TimePeriod.getCustom(5));
		editor.activate();
		editor.openTab("Indexes");		
		
		assertEquals("Index",  editor.getCellText(0, "Name"));
		//assertEquals("IndexSource", editor.getCellText(0, "Name In Source"));		// TEIID 2921
		assertEquals("true", editor.getCellText(0, "Nullable"));
		assertEquals("true", editor.getCellText(0, "Auto Update"));
		assertEquals("true", editor.getCellText(0, "Unique"));
		assertEquals("Column", editor.getCellText(0, "Columns"));
		assertEquals("This is Index description",editor.getCellText(0, "Description"));
		editor.save();
	}
	
}
