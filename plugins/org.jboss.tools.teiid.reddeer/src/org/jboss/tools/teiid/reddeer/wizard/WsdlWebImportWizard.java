package org.jboss.tools.teiid.reddeer.wizard;

import java.util.Properties;

import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

/**
 * Wizard for importing relational model from WSDL
 * 
 * @author apodhrad
 * 
 */
public class WsdlWebImportWizard extends ImportWizardDialog {

	Properties importProps;//modelName, project, [wsdlName] OR [wsdlUrl, securityType, username, password], 
	
	public static final String IMPORT_WSDL_FROM_WORKSPACE = "Workspace...";
	public static final String IMPORT_WSDL_FROM_URL = "URL...";
	public static final String HTTPBASIC_SECURITY_TYPE = "HTTPBasic";
	
	public WsdlWebImportWizard() {
		super("Teiid Designer", "WSDL File or URL >> Web Service Model");
	}
	
	public WsdlWebImportWizard(Properties importProps) {
		super("Teiid Designer", "WSDL File or URL >> Web Service Model");
		this.importProps = importProps;
	}

	public void importWsdl(String name, String project, String wsdl) {//import from workspace
		open();

		new SWTWorkbenchBot().textWithLabel("Web Service Model Name").setText(name);
		new SWTWorkbenchBot().textInGroup("Target Workspace Folder").setText(project);
		new SWTWorkbenchBot().button("Workspace...").click();

		new DefaultShell("WSDL File Selection");
		new DefaultTreeItem(project, wsdl).select();
		new PushButton("OK").click();

		next();
		next();
		next();
		next();

		finish();
	}
	
	@Override 
	public void open(){
		try {
			super.open();
		} catch (Exception e){
			new DefaultTreeItem("Teiid Designer").collapse();
			new DefaultTreeItem("Teiid Designer", "WSDL File or URL >> Web Service Model").expand();
			new DefaultTreeItem("Teiid Designer", "WSDL File or URL >> Web Service Model").select();
			next();
		}
	}
	
	public void importWsdl(Properties importProps, String wsdlLocation){
		open();
		
		importWsdlWithoutOpen(importProps, wsdlLocation);
	}
	
	public void importWsdlWithoutOpen(Properties importProps, String wsdlLocation){

				//first page
				AbstractWait.sleep(TimePeriod.getCustom(2));
				fillFirstPage(importProps, wsdlLocation);
				next();
				
				//second page (Validation Problems)
				//review validation problems
				try {
					new SWTWorkbenchBot().textWithLabel("Validation Problems").getText();
					next();
				} catch (Exception ex){
					System.err.println("Page Validation Problems not shown");
				}
				
				//second page 2 (Namespace Resolution)
				//nothing - namespace resolution 
				try {
					new SWTWorkbenchBot().textWithLabel("Namespace Resolution").getText();
					fillSecondPage(importProps);
					next();
				} catch (Exception ex){
					System.err.println("Page Namespace Resolution not shown");
				}
				
				//third page 
				//select desired wsdl operations -- list (WSDL Operations Selection)
				fillThirdPage(importProps);
				next();
				
				//fourth page (Schema Workspace Location Selection)
				//schema workspace location selection - where will be xsd located 
				fillFourthPage(importProps);
				next();
				
				//fifth page (XML Model Generation)
				//xml model generation: generate virtual xml doc model, location, xml model name
				fillFifthPage(importProps);
				finish();
	}

	@Override
	public void finish() {
		super.finish();

		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	//TODO move to private attributes
	protected void fillFirstPage(Properties importProps, String wsdlLocation){
		String modelName = importProps.getProperty("modelName");//webservice model name
		String project = importProps.getProperty("project");
		String wsdlName = importProps.getProperty("wsdlName");
		
		new SWTWorkbenchBot().textWithLabel("Web Service Model Name").setText(modelName);
		new SWTWorkbenchBot().textInGroup("Target Workspace Folder").setText(project);
		if (wsdlLocation.equals(IMPORT_WSDL_FROM_WORKSPACE)){
			new PushButton(IMPORT_WSDL_FROM_WORKSPACE).click();
			new DefaultShell("WSDL File Selection");
			new DefaultTreeItem(project, wsdlName).select();
			new PushButton("OK").click();return;
		}
		if (wsdlLocation.equals(IMPORT_WSDL_FROM_URL)){
			//property wsdl url
			//property security type -> none | httpbasic (username, password)
			new PushButton("URL...").click();
			new WaitUntil(new ShellWithTextIsAvailable("WSDL URL"));
			new DefaultShell("WSDL URL").setFocus();
			new SWTWorkbenchBot().textWithLabel("Enter WSDL URL:").setText(importProps.getProperty("wsdlUrl"));
			if (importProps.getProperty("securityType") != null){
				if (importProps.getProperty("securityType").equals(HTTPBASIC_SECURITY_TYPE)){
					new org.jboss.reddeer.swt.impl.combo.DefaultCombo().setText("HTTPBasic");
					new SWTWorkbenchBot().textWithLabel("User Name").setText(importProps.getProperty("username"));
					new SWTWorkbenchBot().textWithLabel("Password").setText(importProps.getProperty("password"));
				}
			}
			new PushButton("OK").click();//wait till wsdl is loaded
		}
	}
	
	public void fillSecondPage(Properties importProps){
		
	}
	
	public void fillThirdPage(Properties importProps){
		
	}
	
	public void fillFourthPage(Properties importProps){
		
	}

	public void fillFifthPage(Properties importProps){
		if (importProps.getProperty("modelNameResponses") != null){
			new SWTWorkbenchBot().textWithLabel("XML Model:").setText(importProps.getProperty("modelNameResponses")); //responses model name- model name is set on first page as webservice model name
		}
		if (importProps.getProperty("generateVirtualXML") != null){
			new CheckBox().click();
		}
		//new org.jboss.reddeer.swt.impl.combo.DefaultCombo().setText("/importTest");// - already set as target ws folder on 1. page, the project
	}	
	
	

}
