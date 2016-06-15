package org.jboss.tools.teiid.reddeer.editor;

import java.awt.event.KeyEvent;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.jface.viewers.CellEditor;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ccombo.DefaultCCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;

public class TableEditor extends AbstractModelEditor {
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
	}
	
	private final String fromEditor;
	
	public TableEditor(String title, String fromEditor){
		super(title);
		this.fromEditor = fromEditor;
		new DefaultCTabItem(TABLE_EDITOR).activate();
		AbstractWait.sleep(TimePeriod.SHORT);
	}

	@Override
	public void close() {
		new DefaultCTabItem(fromEditor).activate();
	}
	
	/**
	 * Opens specified tab.
	 * Note: every model (diagram) can have unique tabs
	 */
	public void openTab(String tabName){
		new DefaultTabItem(tabName).activate();
	}
	
	/**
	 * Deletes specified row in table
	 */
	public void deleteRow(int rowIndex){
		new DefaultTable().getItem(rowIndex).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new ContextMenu("Delete").select();
		AbstractWait.sleep(TimePeriod.SHORT);
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
	
	/**
	 * Sets specified text into specified cell of table.
	 * Note: row index starts from 0.
	 */
	public void setCellText(int rowIndex, String columnName, String value){
		setCellText(rowIndex, new DefaultTable().getHeaderIndex(columnName), value);
	}
	

	/**
	 * Sets specified text into specified cell of table.
	 * @param firstCellValue - defines row by text of cell in first column.
	 */
	public void setCellText(String firstCellText, int columnIndex, String value){
		new DefaultTable().getItem(firstCellText).doubleClick(columnIndex);
		new DefaultText(new CellEditor(new DefaultTable().getItem(firstCellText), columnIndex)).setText(value);
		KeyboardFactory.getKeyboard().type(KeyEvent.VK_TAB);
		AbstractWait.sleep(TimePeriod.SHORT);
	}
	
	/**
	 * Sets specified text into specified cell of table.
	 * @param firstCellValue - defines row by text of cell in first column.
	 */
	public void setCellText(String firstCellText, String columnName, String value){
		setCellText(firstCellText, new DefaultTable().getHeaderIndex(columnName), value);
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
		new PropertiesView().getProperty(propertyPath).getTreeItem().doubleClick();
		new DefaultCCombo().setSelection(value);
		Display.asyncExec(new Runnable() {
			@Override
			public void run() {
				new PropertiesView();
			}
		});	
		AbstractWait.sleep(TimePeriod.SHORT);
		try {
			new DefaultShell("Confirm SQL Update");
			new PushButton("Yes").click();
			new WaitWhile(new ShellWithTextIsActive("Confirm SQL Update"), TimePeriod.NORMAL);
		} catch (SWTLayerException e) {	
			// shell not opened -> continue
		}
		this.show();
	}
		
}
