package org.jboss.tools.teiid.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.teiid.reddeer.condition.IsItemAdded;

/**
 * Wizard for importing relational model from WSDL
 * 
 * @author apodhrad
 * 
 */
public class WsdlImportWizard extends TeiidImportWizard {

	public static final String IMPORTER = "Web Service Source >> Source and View Model (SOAP)";

	private String profile;
	private List<String> requestElements;
	private List<String> responseElements;
	private List<String> operations;
	private String projectName;  
	private String sourceModelName;
	private String viewModelName;

	public WsdlImportWizard() {
		super(IMPORTER);
		requestElements = new ArrayList<String>();
		responseElements = new ArrayList<String>();
		operations = new ArrayList<String>();
	}
	
	public String getViewModelName() {
		return viewModelName;
	}

	public void setViewModelName(String viewModelName) {
		this.viewModelName = viewModelName;
	}
	
	public String getSourceModelName() {
		return sourceModelName;
	}

	public void setSourceModelName(String sourceModelName) {
		this.sourceModelName = sourceModelName;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	public void setProjectName(String projectName){ 

		this.projectName = projectName;
	}

	public void addRequestElement(String path) {
		requestElements.add(path);
	}

	public void addResponseElement(String path) {
		responseElements.add(path);
	}
	
	public void addOperation(String name) {
		operations.add(name);	
	}

	@Override
	public void open() {
		log.info("Open " + IMPORTER);
		new ShellMenu(getMenuPath()).select();
		new DefaultShell(getDialogTitle());
		log.info("Select " + IMPORTER);
		new DefaultTreeItem("Teiid Designer", IMPORTER).select();
		stupidWait();
		next();
		stupidWait();
	}

	@Override
	public void execute() {
		open();
		new DefaultCombo(0).setSelection(profile);//TODO end point - binding, service mode
		for (String operationName : operations){
			selectOperation(operationName);			
		}
				
		new LabeledText(new DefaultGroup("Source Model Definition"),"Name").setText(sourceModelName);
		
		
		new LabeledText(new DefaultGroup("View Model Definition"),"Name").setText(viewModelName);
		
		new PushButton("Next >").click();
		
		for (String operation : operations){
			new DefaultCombo(new DefaultGroup("Operations"),0).setSelection(operation);
			
			for (String path : requestElements) {
				if (path.split("/")[0].equals(operation)) addElement("Request", path);
			}
			// Add response elements
			for (String path : responseElements) {
				if (path.split("/")[0].equals(operation + "Response")) addElement("Response", path);
			}
			
		}
		
		//TODO source model definition, view model def, procedure gen. options
		//TODO select operation; for request procedure: check generated sql statement, CRUD with element info; body/header
		//TODO response: body, header, root path; operations with columns? (ordinality)
		//TODO wrapper procedure: generated procedure name
		// Add request elements

		finish();
	}

	private void addElement(String tab, String path) {
		log.info("Add " + tab + " element '" + path + "'");
		new DefaultTabItem(tab).activate();
		try {
			new DefaultTreeItem(path.split("/")).select();
		} catch (Exception e) {
		}
		new PushButton("Add").click();
		/*String lastItem = getLastItem(path);
		new WaitUntil(new IsItemAdded(lastItem, tab), TimePeriod.NORMAL);*/
	}
	
	private void selectOperation(String operation){
		new DefaultTable(new DefaultGroup("Select the desired WSDL Operations"),0).getItem(operation).setChecked(true);	
		
	}

	private String getLastItem(String path) {
		String[] items = path.split("/");
		return items[items.length - 1];
	}

	// The are some problems on Win7_32. We need to wait due to possible WSDL
	// reading (not sure).
	private void stupidWait() {
		long time = 10 * 1000;
		log.info("Stupid waiting for " + time + " ms");
		new SWTWorkbenchBot().sleep(time);
	}
}
