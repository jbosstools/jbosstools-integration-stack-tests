package org.jboss.tools.teiid.reddeer.wizard.newWizard;

import java.util.Arrays;

import org.eclipse.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.hamcrest.Matcher;

/**
 * Wizard for creating a new model project
 * 
 * @author apodhrad
 * 
 */
public class NewModelProjectWizard extends NewMenuWizard {
	
	public static final String DIALOG_TITLE = "New Model Project";

	public static final String FOLDER_TYPE_SOURCES = "sources";
	public static final String FOLDER_TYPE_VIEWS = "views";
	public static final String FOLDER_TYPE_SCHEMAS = "schemas";
	public static final String FOLDER_TYPE_WEB_SERVICES = "web_services";
	public static final String FOLDER_TYPE_FUNCTIONS = "functions";
	public static final String FOLDER_TYPE_EXTENSIONS = "extensions";
	
	private NewModelProjectWizard() {
		super(DIALOG_TITLE, "Teiid Designer", "Teiid Model Project");
		log.info("Metadata model wizard is opened");
	}
	
	public static NewModelProjectWizard getInstance(){
		return new NewModelProjectWizard();
	}
	
	public static NewModelProjectWizard openWizard(){
		NewModelProjectWizard wizard = new NewModelProjectWizard();
		wizard.open();
		return wizard;
	}

	public NewModelProjectWizard nextPage(){
		log.info("Go to next wizard page");
		super.next();
		return this;
	}
	
	public NewModelProjectWizard activateWizard() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}
	
	public NewModelProjectWizard setProjectName(String name) {
		activateWizard();
		log.info("Set project name to '" + name + "'");
		new LabeledText("Project name:").setText(name);
		return this;
	}
	
	/**
	 * @param folders use one of folder type (ModelProjectWizard.FOLDER_TYPE_ ...)
	 */
	public NewModelProjectWizard chooseFolder(String...folders){
		activateWizard();
		log.info("Choose default folders: '" + Arrays.toString(folders) + "'");
		Matcher<String> matcher = new WithMnemonicTextMatcher("Name");
		for(int i=0;i<6;i++){
			new CheckBox(i,matcher).toggle(false);
		}
		for(String folder : folders){
			switch (folder) {
            case FOLDER_TYPE_SOURCES:
    			new CheckBox(0,matcher).toggle(true);
                     break;
            case FOLDER_TYPE_VIEWS:
    			new CheckBox(1,matcher).toggle(true);
                     break;
            case FOLDER_TYPE_SCHEMAS:
    			new CheckBox(2,matcher).toggle(true);
                     break;
            case FOLDER_TYPE_WEB_SERVICES:
    			new CheckBox(3,matcher).toggle(true);
                     break;
            case FOLDER_TYPE_FUNCTIONS:
    			new CheckBox(4,matcher).toggle(true);
                     break;
            case FOLDER_TYPE_EXTENSIONS:
    			new CheckBox(5,matcher).toggle(true);
                     break;
        }
		}
		return this;
	}
}
