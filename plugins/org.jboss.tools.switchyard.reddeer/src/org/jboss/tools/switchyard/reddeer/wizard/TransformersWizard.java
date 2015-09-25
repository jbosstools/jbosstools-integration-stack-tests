package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.List;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.list.DefaultList;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.switchyard.reddeer.shell.SelectDozerFileShell;

/**
 * 
 * @author apodhrad
 *
 */
public class TransformersWizard extends WizardDialog {

	public static final String TITLE = "New Transformers";

	public static final String TRANSFORMER_TYPE_DOZER = "Dozer Transformer";
	public static final String TRANSFORMER_TYPE_JAVA = "Java Transformer";
	public static final String TRANSFORMER_TYPE_JAXB = "JAXB Transformer";
	public static final String TRANSFORMER_TYPE_JSON = "JSON Transformer";
	public static final String TRANSFORMER_TYPE_XSL = "XSL Transformer";
	public static final String TRANSFORMER_TYPE_SMOOKS = "Smooks Transformer";

	public static final int COLUMN_FROM = 0;
	public static final int COLUMN_TO = 1;

	public TransformersWizard() {
		activate();
	}

	public TransformersWizard activate() {
		new DefaultShell(TITLE);
		return this;
	}

	public TransformersWizard newDozerFile(String fileName) {
		getNewBTN().click();
		NewDozerTransformerFileWizard dozerWizard = new NewDozerTransformerFileWizard();
		dozerWizard.setFilename(fileName);
		dozerWizard.finish();
		return activate();
	}


	public TransformersWizard browseDozerFile(String fileName) {
		getBrowseBTN().click();
		SelectDozerFileShell wizard = new SelectDozerFileShell();
		wizard.setFileName(fileName);
		wizard.waitForTableHasRows();
		wizard.ok();
		return activate();
	}
	
	public LabeledCombo getTransformerTypeCMB() {
		return new LabeledCombo("Transformer Type:");
	}

	public CheckBox getCreateNewSmooksfileCHK() {
		return new CheckBox("Create new Smooks file");
	}

	public PushButton getRemovePackagesBTN() {
		return new PushButton("Remove Packages");
	}

	public PushButton getAddPackagesBTN() {
		return new PushButton("Add Packages");
	}

	public PushButton getClearAllBTN() {
		return new PushButton(new DefaultGroup("Dozer File Options"), "Clear All");
	}

	public PushButton getDownBTN() {
		return new PushButton(new DefaultGroup("Dozer File Options"), "Down");
	}

	public PushButton getUpBTN() {
		return new PushButton(new DefaultGroup("Dozer File Options"), "Up");
	}

	public PushButton getRemoveBTN() {
		return new PushButton(new DefaultGroup("Dozer File Options"), "Remove");
	}

	public PushButton getNewBTN() {
		return new PushButton(new DefaultGroup("Dozer File Options"), "New...");
	}

	public PushButton getBrowseBTN() {
		return new PushButton(new DefaultGroup("Dozer File Options"), "Browse...");
	}

	public PushButton getDeselectAllBTN() {
		return new PushButton("Deselect All");
	}

	public PushButton getSelectAllBTN() {
		return new PushButton("Select All");
	}

	public Button getFinishBTN() {
		return new PushButton("Finish");
	}

	public Button getCancelBTN() {
		return new PushButton("Cancel");
	}

	public Button getNextBTN() {
		return new PushButton("Next >");
	}

	public Button getBackBTN() {
		return new PushButton("< Back");
	}

	public RadioButton getCreateNewSmooksFileRDO() {
		return new RadioButton("Create new Smooks file");
	}

	public Text getSmooksFileTXT() {
		return new LabeledText("Smooks file:");
	}

	public void setSmooksFile(String text) {
		getSmooksFileTXT().setText(text);
	}

	public void getSmooksfile() {
		getSmooksFileTXT().getText();
	}

	public CheckBox getCreateNewXSLfileCHK() {
		return new CheckBox("Create new XSL file");
	}

	public RadioButton getCreatenewXSLfileRDO() {
		return new RadioButton("Create new XSL file");
	}

	public Text getXSLFileTXT() {
		return new LabeledText("XSL file:");
	}

	public void setXSLFile(String text) {
		getXSLFileTXT().setText(text);
	}

	public void getXSLFile() {
		getXSLFileTXT().getText();
	}

	public Table getTransformerTypePairTBL() {
		return new DefaultTable(0);
	}

	public TableItem getTransformerTypePairTBI(int row) {
		return new DefaultTableItem(row);
	}

	public List getDozerFileOptionsLST() {
		return new DefaultList(new DefaultGroup("Dozer File Options"));
	}

}
