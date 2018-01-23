package org.jboss.tools.teiid.reddeer.editor;

import java.awt.event.KeyEvent;
import java.util.List;

import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.reddeer.jface.viewers.CellEditor;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.exception.SWTLayerException;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ccombo.DefaultCCombo;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tab.DefaultTabItem;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.keyboard.KeyboardFactory;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;

public class TableEditor extends DefaultEditor {
	private static final String TABLE_EDITOR = "Table Editor";
	
	public static class Tabs{
		public static final String INPUTS = "Inputs";
		public static final String INTERFACES = "Interfaces";
		public static final String OPERATIONS = "Operations";
		public static final String OUTPUTS = "Outputs";
		public static final String SAMPLE_MESSAGES = "Sample Messages";
		public static final String BASE_TABLES = "Base Tables";
		public static final String COLUMNS = "Columns";
		public static final String PRIMARY_KEYS = "Primary Keys";
		public static final String FOREIGN_KEYS = "Foreign Keys";
		public static final String XML_DOCUMENTS = "Xml Documents";
		public static final String XML_ELEMENTS = "Xml Elements";
		public static final String XML_NAMESPACES = "Xml Namespaces";
		public static final String XML_ROOTS = "Xml Roots";
		public static final String XML_SEQUENCES = "Xml Sequences";
		public static final String PROCEDURES = "Procedures";
		public static final String PROCEDURE_PARAMETERS = "Procedure Parameters";
		public static final String PROCEDURE_RESULTS = "Procedure Results";
		public static final String UNIQUE_CONSTRAINTS = "Unique Constraints";
		public static final String ACCESS_PATTERNS = "Access Patterns";
		public static final String INDEXES = "Indexes";
	}
	
	public TableEditor(String name){
		super(name);
		new DefaultCTabItem(TABLE_EDITOR).activate();
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	public void close() {
		new DefaultCTabItem(new DefaultCTabItem(0).getText()).activate();
	}
	
	public void activate() {
		super.activate();
		new DefaultCTabItem(TABLE_EDITOR).activate();
	}
	
	/**
	 * Opens specified tab.
	 * Note: every model (diagram) can have unique tabs
	 */
	public void openTab(String tabName){
        this.activate();
        new DefaultTabItem(tabName).activate();
	}
	
	public List<TableItem> getRows(){
		return new DefaultTable().getItems();
	}
	
	/**
	 * @param iCellIndex, iCellText - defines row by text of cell in specified column.
	 */
	public TableItem getRow(int iCellIndex, String iCellText){
		return new DefaultTable().getItem(iCellText, iCellIndex);	
	}
	
	/**
	 * @param rowIndex - defines row by it's index.
	 */
	public TableItem getRow(int rowIndex){
		return new DefaultTable().getItem(rowIndex);	
	}
	
	/**
	 * @param columnName, cellText - defines row by text of cell in specified column.
	 */
	public TableItem getRow(String columnName, String cellText){
		DefaultTable table = new DefaultTable();
		try {
			return table.getItem(cellText, table.getHeaderIndex(columnName));
		} catch(CoreLayerException ex){
			return null;
		}	
	}
	
	/**
	 * Sets specified text into specified cell of table.
	 * Note: row, column index starts from 0.
	 */
	public void setCellText(int rowIndex, int columnIndex, String value){
		new DefaultTable().getItem(rowIndex).doubleClick(columnIndex);
		new DefaultText(new CellEditor(new DefaultTable().getItem(rowIndex), columnIndex)).setText(value);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
    public void setCellCombo(int rowIndex, String columnName, String value) {
        int columnIndex = new DefaultTable().getHeaderIndex(columnName);
        new DefaultTable().getItem(rowIndex).doubleClick(columnIndex);
        new DefaultCCombo(new CellEditor(new DefaultTable().getItem(rowIndex), columnIndex)).setSelection(value);
        KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
        AbstractWait.sleep(TimePeriod.SHORT);
    }

	/**
	 * Sets specified text into specified cell of table.
	 * Note: row index starts from 0.
	 */
	public void setCellText(int rowIndex, String columnName, String value){
		setCellText(rowIndex, new DefaultTable().getHeaderIndex(columnName), value);
	}
	

	/**
	 * Sets specified text into specified cell of table.
	 * @param iCellIndex, iCellText - defines row by text of cell in specified column.
	 */
	public void setCellText(int iCellIndex, String iCellText, int columnIndex, String value){
		TableItem item = new DefaultTable().getItem(iCellText, iCellIndex);
		item.doubleClick(columnIndex);
		new DefaultText(new CellEditor(item, columnIndex)).setText(value);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Sets specified text into specified cell of table.
	 * @param iCellIndex, iCellText - defines row by text of cell in specified column.
	 */
	public void setCellText(int iCellIndex, String iCellText, String columnName, String value){
		setCellText(iCellIndex, iCellText, new DefaultTable().getHeaderIndex(columnName), value);
	}	
	
	/**
	 * Sets specified text into specified cell of table via Properties view.
	 * Note: row index starts from 0.
	 */
	public void setCellTextViaProperties(int rowIndex, String value, String... propertyPath){
		new DefaultTable().getItem(rowIndex).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		setProperty(value, propertyPath);
	}
	
	/**
	 * Sets specified text into specified cell of table via Properties view.
	 *  @param firstCellValue - defines row by text of cell in first column.
	 */
	public void setCellTextViaProperties(String firstCellText, String value, String... propertyPath){
		new DefaultTable().getItem(firstCellText).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		setProperty(value, propertyPath);
	}
	
	/**
	 * TODO include - if cell of property value is not CCombo 
	 */
	private void setProperty(String value, String... propertyPath){
		new PropertySheet().getProperty(propertyPath).getTreeItem().doubleClick();
		new DefaultCCombo().setSelection(value);
		Display.asyncExec(new Runnable() {
			@Override
			public void run() {
				new PropertySheet();
			}
		});	
		AbstractWait.sleep(TimePeriod.SHORT);
		try {
			new DefaultShell("Confirm SQL Update");
			new PushButton("Yes").click();
			new WaitWhile(new ShellIsActive("Confirm SQL Update"), TimePeriod.DEFAULT);
		} catch (SWTLayerException e) {	
			// shell not opened -> continue
		}
		activate();
	}
		
	/**
	 * Gets specified text from specified cell of table.
	 * Note: row, column index starts from 0.
	 */
	public String getCellText(int rowIndex, int columnIndex){
		return new DefaultTable().getItem(rowIndex).getText(columnIndex);
	}
	
	/**
	 * Gets specified text from specified cell of table.
	 * Note: row index starts from 0.
	 */
	public String getCellText(int rowIndex, String columnName){
		return getCellText(rowIndex, new DefaultTable().getHeaderIndex(columnName));
	}
	
	/**
	 * Gets specified text into specified cell of table.
	 * @param iCellIndex, iCellText - defines row by text of cell in specified column.
	 */
	public String getCellText(int iCellIndex, String iCellText, int columnIndex){
		return new DefaultTable().getItem(iCellText, iCellIndex).getText(columnIndex);
	}
	
	/**
	 * Gets specified text into specified cell of table.
	 * @param iCellIndex, iCellText - defines row by text of cell in specified column.
	 */
	public String getCellText(int iCellIndex, String iCellText, String columnName){
		return getCellText(iCellIndex, iCellText, new DefaultTable().getHeaderIndex(columnName));
	}
	
	public void setDatatype(String tabName, int rowIndex, int columnIndex, String datatype){
		new DefaultTabItem(tabName).activate();
		new DefaultTable(0).getItem(rowIndex).doubleClick(columnIndex);
		new PushButton((new CellEditor(new DefaultTable().getItem(rowIndex), columnIndex))).click();
		new DefaultShell("Select a Datatype");
        new DefaultText(0).setText(datatype);
        AbstractWait.sleep(TimePeriod.SHORT); // wait if show only dataType in the table
        new DefaultTable().getItem(0).click();
		new PushButton("OK").click();		
	}
	
    @Override
    public void save() {
        if (new ShellMenuItem(new WorkbenchShell(), "File", "Save").isEnabled()) {
            super.save();
        }
    }
}
