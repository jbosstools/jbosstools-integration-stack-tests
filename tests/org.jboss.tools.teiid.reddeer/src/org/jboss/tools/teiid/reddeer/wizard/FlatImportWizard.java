package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

/**
 * Wizard for importing a flat file source into a model project
 * 
 * @author apodhrad
 * 
 */
public class FlatImportWizard extends TeiidImportWizard {

	public static final String LABEL_FORMAT_OPRIONS = "Format Options";

	private String profile;
	private String file;
	private String sourceModelName;//source model name
	private String viewModelName;
	private String viewTableName;
	private String importMode;
	private String projectName;
	
	private int headerLine;
	private int dataLine;
	
	private boolean editDelimiterCharacter = false;
	private boolean editTexttableFunctionOptions = false;
	
	static class DelimiterCharacter{
		String COMMA = "Comma \',\'";
		String SPACE = "Space \'\'";
		String TAB = "Tab";
		String SEMICOLON = "Semicolon \':\'";
		String BAR = "Bar \'|\'";
		String OTHER = "Other";
	}
	
	static class TexttableFunctionOptions{
		String INCLUDE_HEADER = "Include HEADER";
		String INCLUDE_SKIP = "Include SKIP";
		String INCLUDE_QUOTE = "Include QUOTE";
		String INCLUDE_ESCAPE = "Include ESCAPE";
		String INCLUDE_NO_TRIM = "Include NO TRIM";
	}
	
	private String delimiterCharacter;//should be set to something from static class
	private List<String> texttableFunctionOptions = new ArrayList<String>();
	
	public List<String> getTexttableFunctionOptions() {
		return texttableFunctionOptions;
	}

	public void setTexttableFunctionOptions(List<String> texttableFunctionOptions) {
		this.texttableFunctionOptions = texttableFunctionOptions;
	}

	public static class FlatFileImportMode {
		public static final String FLAT_FILE_ON_LOCAL_FILE_SYSTEM = "Flat file on local file system";
		public static final String FLAT_FILE_VIA_REMOTE_URL = "Flat file via remote URL";
	}

	public boolean isEditTexttableFunctionOptions() {
		return editTexttableFunctionOptions;
	}

	public void setEditTexttableFunctionOptions(boolean editTexttableFunctionOptions) {
		this.editTexttableFunctionOptions = editTexttableFunctionOptions;
	}

	public FlatImportWizard() {
		super("File Source (Flat) >> Source and View Model");
		// default settings
		headerLine = 1;
		dataLine = 2;
	}
	
	public void setViewModelName(String viewModelName) {
		this.viewModelName = viewModelName;
	}

	public void setViewTableName(String viewTableName) {
		this.viewTableName = viewTableName;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setSourceModelName(String name) {
		this.sourceModelName = name;
	}

	public void setHeaderLine(int headerLine) {
		this.headerLine = headerLine;
	}

	public void setDataLine(int dataLine) {
		this.dataLine = dataLine;
	}
	
	public void setImportMode(String importMode) {
		this.importMode = importMode;
	}

	@Deprecated
	@Override
	public void execute() {
		executeBeginOfWizard();
		
		new SWTWorkbenchBot().textWithLabel("Name:").setText(sourceModelName + "View");

		new LabeledText("New view table name:").setText(sourceModelName + "Table");

		finish();
	}
	
	@Deprecated
	public void execute(boolean setViewNames) {		
		executeBeginOfWizard();
		if (projectName != null){
			new PushButton("...").click();
			new DefaultTreeItem(projectName).select();
			new PushButton("OK").click();
		}
		new SWTWorkbenchBot().textWithLabel("Name:").setText(viewModelName);

		new LabeledText("New view table name:").setText(viewTableName);

		finish();
	}
	
	@Deprecated
	private void executeBeginOfWizard(){
		open();
		//first page
		new RadioButton(importMode).click();
		next();
		
		//second page
		new DefaultCombo(0).setSelection(profile);
		setCheckedFile(file, true);
		// TODO: LabeledText
		new SWTWorkbenchBot().textWithLabel("Name:").setText(sourceModelName + "Source");
		if (projectName != null){
			new PushButton("...").click();
			new DefaultTreeItem(projectName).select();
			new PushButton("OK").click();
		}
		next();
		
		//third page
		next();
		
		//fourth page
		if (headerLine > 0) {
			new CheckBox("Column names in header").toggle(true);
			new LabeledText("Header line #").setText(Integer.toString(headerLine));
			new LabeledText("Data line #").setText(Integer.toString(dataLine));
		} else {
			new CheckBox("Column names in header").toggle(false);
		}

		next();
	}

	protected void setCheckedFile(String fileName, boolean checked) {
		SWTBotTable table = new SWTWorkbenchBot().tableInGroup("Available Data Files");
		SWTBotTableItem item = table.getTableItem(fileName);
		if (checked) {
			item.check();
		} else {
			item.uncheck();
		}
	}
	
	//refactored part
	public void execute2() {
		open();
		fillFirstPage();
		
		fillSecondPage();
		
		fillThirdPage();
		
		fillFourthPage();
		
		fillFifthPage();
		
		finish();
	}
	
	
	private void fillFirstPage(){
		//first page
		if (importMode != null){
			new RadioButton(importMode).click();
		}
		next();
	}
	
	private void fillSecondPage(){
		//second page
		new DefaultCombo(0).setSelection(profile);
		setCheckedFile(file, true);
		if (sourceModelName != null){
			new SWTWorkbenchBot().textWithLabel("Name:").setText(sourceModelName + "Source");
		}
		next();
	}
	
	private void fillThirdPage(){
		next();
	}
	
	private void fillFourthPage() {
		// fourth page
		if (headerLine > 0) {
			new CheckBox("Column names in header").toggle(true);
			new LabeledText("Header line #").setText(Integer
					.toString(headerLine));
			if (dataLine > 0) {
				new LabeledText("Data line #").setText(Integer
						.toString(dataLine));
			}
		}

		else {
			new CheckBox("Column names in header").toggle(false);
		}
		
		//edit delimiter character - if != comma
		if (editDelimiterCharacter){
			//TEST
			new PushButton("Edit Delimiter Character").click();
			new RadioButton(delimiterCharacter).click();
			new PushButton("OK").click();
		}
		
		if (editTexttableFunctionOptions){
			new PushButton("Edit TEXTTABLE() function options").click();
			for (String texttableOption : this.texttableFunctionOptions){
				new CheckBox(texttableOption).toggle(true);
			}
			new PushButton("OK").click();
			
		}
		
		next();
	}
	
	private void fillFifthPage() {
		if (sourceModelName != null){
			new SWTWorkbenchBot().textWithLabel("Name:").setText(sourceModelName + "View");
			new LabeledText("New view table name:").setText(sourceModelName + "Table");
		}
	}

	public boolean isEditDelimiterCharacter() {
		return editDelimiterCharacter;
	}

	public void setEditDelimiterCharacter(boolean editDelimiterCharacter) {
		this.editDelimiterCharacter = editDelimiterCharacter;
	}

	public String getOtherDelimiter() {
		return delimiterCharacter;
	}

	public void setOtherDelimiter(String otherDelimiter) {
		this.delimiterCharacter = otherDelimiter;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
