package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.NextButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.combo.DefaultCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.dialog.EditColumnDialog;
/**
 * Wizard for importing a flat file source into a model project
 * 
 * @author apodhrad
 * 
 */
public class FlatImportWizard extends TeiidImportWizard {

	public static final String DIALOG_TITLE = "Import From Flat File Source";
	
	public static final String LABEL_FORMAT_OPRIONS = "Format Options";

	private FlatImportWizard() {
		super(DIALOG_TITLE, "File Source (Flat) >> Source and View Model");
		log.info("Flat file import Wizard is opened");
	}

	public static FlatImportWizard getInstance(){
		return new FlatImportWizard();
	}
	
	public static FlatImportWizard openWizard(){
		FlatImportWizard wizard = new FlatImportWizard();
		wizard.open();
		return wizard;
	}
	
	public FlatImportWizard nextPage(){
		log.info("Go to next wizard page");
		activate();
		AbstractWait.sleep(TimePeriod.SHORT);
		new PushButton("Next >");
		new NextButton().click();
		return this;
	}
	
	public FlatImportWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	static class TexttableFunctionOptions {
		String INCLUDE_HEADER = "Include HEADER";
		String INCLUDE_SKIP = "Include SKIP";
		String INCLUDE_QUOTE = "Include QUOTE";
		String INCLUDE_ESCAPE = "Include ESCAPE";
		String INCLUDE_NO_TRIM = "Include NO TRIM";
	}

	public enum DelimiterCharacter {

		COMMA("Comma \',\'"),
		SPACE("Space \'\'"),
		TAB("Tab"),
		SEMICOLON("Semicolon \':\'"),
		BAR("Bar \'|\'"),
		OTHER("Custom character:");

		private String label;
		private String delimiterCharacter;

		private DelimiterCharacter(String label) {
			this(label, null);
		}

		private DelimiterCharacter(String label, String delimiterCharacter) {
			this.label = label;
			this.delimiterCharacter = delimiterCharacter;
		}

		public String getLabel() {
			return label;
		}

		public String getDelimiterCharacter() {
			return delimiterCharacter;
		}

		public static DelimiterCharacter other(String delimiterCharacter) {
			DelimiterCharacter other = DelimiterCharacter.OTHER;
			other.delimiterCharacter = delimiterCharacter;
			return other;
		}
	}

	public FlatImportWizard selectImportMode(String importMode) {
		log.info("Select import mode to '" + importMode + "'");
		activate();
		new RadioButton(importMode).click();
		return this;
	}

	public FlatImportWizard selectLocalFileImportMode() {
		return selectImportMode("Flat file on local file system");
	}

	public FlatImportWizard selectRemoteUrlImportMode() {
		return selectImportMode("Flat file via remote URL");
	}

	public FlatImportWizard selectProfile(String profile) {
		log.info("Select profile to '" + profile + "'");
		activate();
		new DefaultCombo(0).setSelection(profile);
		return this;
	}

	public FlatImportWizard setSourceModel(String sourceModelName) {
		log.info("Set source model to '" + sourceModelName + "'");
		activate();
		new LabeledText("Name:").setText(sourceModelName);
		return this;
	}

	public FlatImportWizard setProject(String project) {
		log.info("Set project to '" + project + "'");
		activate();
		new PushButton("...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(project).select();
		new PushButton("OK").click();
		return this;
	}

	/**
	 * Only for character delimited csv format
	 */
	public FlatImportWizard checkHeaderNames() {
		log.info("Check 'Column names in header'");
		activate();
		new CheckBox("Column names in header").toggle(true);
		return this;
	}

	/**
	 * Only for character delimited csv format
	 */
	public FlatImportWizard setHeaderLine(String num) {
		checkHeaderNames();
		log.info("Set header line to '" + num + "'");
		activate();
		new LabeledText("Header line #").setText(num);
		return this;
	}

	public FlatImportWizard setDataLine(String num) {
		log.info("Set data line to '" + num + "'");
		activate();
		new LabeledText("Data line #").setText(num);
		return this;
	}
	
	public FlatImportWizard setJndiName(String jndiName) {
		log.info("Setting JNDI name");
		activate();
		new DefaultText(new DefaultGroup("JBoss Data Source Information"),0).setText(jndiName);
		return this;
	}

	/**
	 * Only for character delimited csv format
	 */
	public FlatImportWizard selectDelimiterCharacter(DelimiterCharacter delimiterCharacter) {
		log.info("Set dilimeter character to '" + delimiterCharacter + "'");
		activate();
		new PushButton("Configure Delimiters").click();
		new DefaultShell("Flat File Delimiter");
		new RadioButton(delimiterCharacter.getLabel()).click();
		if (delimiterCharacter.getDelimiterCharacter() != null) {
			new DefaultText().setText(delimiterCharacter.getDelimiterCharacter());
		}
		new PushButton("OK").click();
		return this;
	}

	public FlatImportWizard selectFile(String fileName) {
		new DefaultTable(new DefaultGroup("Available Data Files"),0).getItem(fileName+"     <<<<").setChecked(true);
		return this;
	}

	public FlatImportWizard setViewModel(String viewModelName) {
		log.info("Set view model name to '" + viewModelName + "'");
		activate();
		new LabeledText("Name:").setText(viewModelName);
		return this;
	}

	public FlatImportWizard setViewTable(String viewTableName) {
		log.info("Set view table name to '" + viewTableName + "'");
		activate();
		new LabeledText("New view table name:").setText(viewTableName);
		return this;
	}
	
	public FlatImportWizard setFixedWidth() {
		log.info("set fixed width csv");
		activate();
		new RadioButton("Fixed width").toggle(true);
		if (new ShellIsActive("Column Format Changed").test()){
			new PushButton("Yes").click();
		}
		return this;
	}
	
	/**
	 * Only for csv with fixed width
	 */
	public FlatImportWizard setConfigureDelimiters(String delimiterCharacter) {
		log.info("Set view table name to '" + delimiterCharacter + "'");
		activate();
		new PushButton("Configure Delimiters").click();
		new DefaultShell("Flat File Delimiter");
		new CheckBox("Use Default (new line character)").toggle(false);
		new RadioButton("Custom character:  ").click();
		new DefaultText().setText(delimiterCharacter);
		new PushButton("OK").click();
		return this;
	}
	
	/**
	 * Only for csv with fixed width
	 */
	public EditColumnDialog addColumn() {
		activate();
		new PushButton("ADD").click();
		return new EditColumnDialog();
	}
}
