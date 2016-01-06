package org.jboss.tools.teiid.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
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

	public static final String LABEL_FORMAT_OPRIONS = "Format Options";
	public static final String LOCAL_FILE_MODE = "Flat file on local file system";
	public static final String REMOTE_URL_MODE = "Flat file via remote url";

	public FlatImportWizard() {
		super("File Source (Flat) >> Source and View Model");
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

	public static class FlatFileImportMode {
		public static final String FLAT_FILE_ON_LOCAL_FILE_SYSTEM = "Flat file on local file system";
		public static final String FLAT_FILE_VIA_REMOTE_URL = "Flat file via remote URL";
	}

	public FlatImportWizard selectImportMode(String importMode) {
		log.info("Select import mode to '" + importMode + "'");
		new RadioButton(importMode).click();
		return this;
	}

	public FlatImportWizard selectLocalFileImportMode() {
		return selectImportMode(LOCAL_FILE_MODE);
	}

	public FlatImportWizard selectRemoteUrlImportMode() {
		return selectImportMode(REMOTE_URL_MODE);
	}

	public FlatImportWizard selectProfile(String profile) {
		log.info("Select profile to '" + profile + "'");
		new DefaultCombo(0).setSelection(profile);
		return this;
	}

	public FlatImportWizard setSourceModel(String sourceModelName) {
		log.info("Set source model to '" + sourceModelName + "'");
		new SWTWorkbenchBot().textWithLabel("Name:").setText(sourceModelName + "Source");
		return this;
	}

	public FlatImportWizard setProject(String project) {
		log.info("Set project to '" + project + "'");
		new PushButton("...").click();
		new DefaultShell("Select a Folder");
		new DefaultTreeItem(project).select();
		new PushButton("OK").click();
		return this;
	}

	public FlatImportWizard checkHeaderNames() {
		log.info("Check 'Column names in header'");
		new CheckBox("Column names in header").toggle(true);
		return this;
	}

	public FlatImportWizard setHeaderLine(String num) {
		checkHeaderNames();
		log.info("Set header line to '" + num + "'");
		new LabeledText("Header line #").setText(num);
		return this;
	}

	public FlatImportWizard setDataLine(String num) {
		log.info("Set data line to '" + num + "'");
		new LabeledText("Data line #").setText(num);
		return this;
	}

	public FlatImportWizard selectDelimiterCharacter(DelimiterCharacter delimiterCharacter) {
		log.info("Set dilimeter character to '" + delimiterCharacter + "'");
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
		setCheckedFile(fileName, true);
		return this;
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

	public FlatImportWizard setViewModel(String viewModelName) {
		log.info("Set view model name to '" + viewModelName + "'");
		new LabeledText("Name:").setText(viewModelName);
		return this;
	}

	public FlatImportWizard setViewTable(String viewTableName) {
		log.info("Set view table name to '" + viewTableName + "'");
		new LabeledText("New view table name:").setText(viewTableName);
		return this;
	}

	@Override
	public void execute() {
		throw new UnsupportedOperationException();
	}

}
