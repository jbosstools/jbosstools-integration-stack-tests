package org.jboss.tools.teiid.reddeer.wizard.newWizard;

import org.hamcrest.Matcher;
import org.jboss.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for creating a new model project
 * 
 * @author apodhrad
 * 
 */
public class NewModelProjectWizard extends NewWizardDialog {

private static NewModelProjectWizard INSTANCE;
	
	public static final String DIALOG_TITLE = "New Model Project";

	public static final String FOLDER_TYPE_SOURCES = "sources";
	public static final String FOLDER_TYPE_VIEWS = "views";
	public static final String FOLDER_TYPE_SCHEMAS = "schemas";
	public static final String FOLDER_TYPE_WEB_SERVICES = "web_services";
	public static final String FOLDER_TYPE_FUNCTIONS = "functions";
	public static final String FOLDER_TYPE_EXTENSIONS = "extensions";
	
	private NewModelProjectWizard() {
		super("Teiid Designer", "Teiid Model Project");
		log.info("Metadata model wizard is opened");
	}
	
	public static NewModelProjectWizard getInstance(){
		if(INSTANCE==null){
			INSTANCE=new NewModelProjectWizard();
		}
		return INSTANCE;
	}
	
	public static NewModelProjectWizard openWizard(){
		NewModelProjectWizard wizard = getInstance();
		wizard.open();
		return wizard;
	}
	
	public NewModelProjectWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	public NewModelProjectWizard setProjectName(String name) {
		activate();
		log.info("Set project name to '" + name + "'");
		new LabeledText("Project name:").setText(name);
		return this;
	}
	
	/**
	 * @param folders use one of folder type (ModelProjectWizard.FOLDER_TYPE_ ...)
	 */
	public NewModelProjectWizard chooseFolder(String...folders){
		activate();
		log.info("Choose default folders: '" + folders + "'");
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
	
	public NewModelProjectWizard nextPage(){
		log.info("Go to next wizard page");
		super.next();
		return this;
	}

	/**
	 * use nextPage()
	 */
	@Deprecated
	@Override
	public void next(){
		super.next();
	}
}
