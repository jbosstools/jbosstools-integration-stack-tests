package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Wizard for importing a flat file source into a model project
 * 
 * @author apodhrad
 * 
 */
public class FlatImportWizard extends TeiidImportWizard {

	private static FlatImportWizard INSTANCE;

	public static final String DIALOG_TITLE = "Import From Flat File Source";
	
	public static final String LABEL_FORMAT_OPRIONS = "Format Options";

	private FlatImportWizard() {
		super("File Source (Flat) >> Source and View Model");
		log.info("Flat file import Wizard is opened");
	}

	public static FlatImportWizard getInstance(){
		if(INSTANCE==null){
			INSTANCE=new FlatImportWizard();
		}
		return INSTANCE;
	}
	
	public static FlatImportWizard openWizard(){
		FlatImportWizard wizard = getInstance();
		wizard.open();
		return wizard;
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
		OTHER("Other");

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

	public FlatImportWizard checkHeaderNames() {
		log.info("Check 'Column names in header'");
		activate();
		new CheckBox("Column names in header").toggle(true);
		return this;
	}

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

	public FlatImportWizard selectDelimiterCharacter(DelimiterCharacter delimiterCharacter) {
		log.info("Set dilimeter character to '" + delimiterCharacter + "'");
		activate();
		new PushButton("Edit Delimiter Character").click();
		new DefaultShell("Select Delimiter Character");
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
	
	public FlatImportWizard nextPage(){
		log.info("Go to next wizard page");
		new NextButton().click();
		return this;
	}
}
