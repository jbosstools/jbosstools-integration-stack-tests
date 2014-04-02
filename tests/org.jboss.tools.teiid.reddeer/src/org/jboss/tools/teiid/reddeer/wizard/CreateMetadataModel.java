package org.jboss.tools.teiid.reddeer.wizard;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;

import java.util.Properties;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.hamcrest.Matcher;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.reference.ReferencedComposite;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

/**
 * Creates a new metadata model.
 * 
 * @author Lucia Jelinkova, lfabriko
 * 
 */
public class CreateMetadataModel extends NewWizardDialog {

	public static class ModelClass {

		public static final String RELATIONAL = "Relational";

		public static final String XML = "XML";

		public static final String XSD = "XML Schema (XSD)";

		public static final String WEBSERVICE = "Web Service";

		public static final String MODEL_EXTENSION = "Model Extension (Deprecated)";

		public static final String FUNCTION = "Function (Deprecated)";

	}

	public static class ModelType {

		public static final String SOURCE = "Source Model";

		public static final String VIEW = "View Model";

		public static final String DATATYPE = "Datatype Model";

		public static final String EXTENSION = "Model Class Extension";

		public static final String FUNCTION = "User Defined Function";

	}
	
	public static class ModelBuilder {
		
		public static final String TRANSFORM_EXISTING = "Transform from an existing model";
		
		public static final String COPY_EXISTING = "Copy from an existing model of the same model class";
		
		public static final String BUILD_FROM_XML_SCHEMA = "Build XML documents from XML schema";
		public static final String BUILD_FROM_WSDL_URL = "Build from existing WSDL file(s) or URL";
	}

	private String location;

	private String name;

	private String clazz;

	private String type;
	
	private String modelBuilder;
	private String[] pathToExistingModel;
	private String[] pathToXmlSchema;
	private String rootElement;
	private Properties wsProps;
	private String wsdlLocation;

	private String[] selectedElement;
	
	public String[] getPathToXmlSchema() {
		return pathToXmlSchema;
	}

	public void setPathToXmlSchema(String[] pathToXmlSchema) {
		this.pathToXmlSchema = pathToXmlSchema;
	}

	public String[] getPathToExistingModel() {
		return pathToExistingModel;
	}

	public void setPathToExistingModel(String[] pathToExistingModel) {
		this.pathToExistingModel = pathToExistingModel;
	}

	public String getRootElement() {
		return rootElement;
	}

	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}

	public CreateMetadataModel() {
		super("Teiid Designer", "Teiid Metadata Model");
	}

	public void execute() {
		open();
		
		fillFirstPage();
		
		if (clazz.equals(ModelClass.WEBSERVICE) && (modelBuilder != null) && modelBuilder.equals(ModelBuilder.BUILD_FROM_WSDL_URL)){
			next();
			new WsdlWebImportWizard().importWsdlWithoutOpen(wsProps, wsdlLocation);;
			return;
		}
		
		//next steps are in case model builder is selected
		if (modelBuilder != null){
			next();
			fillSecondPage();//sometimes do nothing
			if (new PushButton("Next >").isEnabled()){
				next();//relational view model + transform existing  (9): just finish now
			}
			//3,4,5
		}
		//for XSD and WS is wizard same as in import
		//ad 9) xmlview + xsd:
		//3rd page: selected documents statistics (nothing)
		//next
		//4th page: preview generated documents (tree)
		//finish
		if (clazz.equals(ModelClass.XML) && (modelBuilder != null) && modelBuilder.equals(ModelBuilder.BUILD_FROM_XML_SCHEMA)){
			next();
			fillFourthPage();
		}
		
		finish();
		
		//xsd
		if (new DefaultShell().getText().contains("Model Initializer")){
			new SWTWorkbenchBot().table().select("XML Schema (2001)");
			new PushButton("OK").click();
		}
	}
	
	public void fillFourthPage() {
		if (clazz.equals(ModelClass.XML) && modelBuilder.equals(ModelBuilder.BUILD_FROM_XML_SCHEMA)){
			new DefaultTreeItem(selectedElement).setChecked(true);//TODO in cycle
		}
		
	}

	@Override
	public void open(){
		try {
			super.open();
		} catch (Exception e){
			new DefaultTreeItem("Teiid Designer").collapse();
			new DefaultTreeItem("Teiid Designer", "Teiid Metadata Model").expand();
			new DefaultTreeItem("Teiid Designer", "Teiid Metadata Model").select();
			next();
		}
	}

	public void fillFirstPage() {
		new SWTWorkbenchBot().textWithLabel("Location:").setText(location);
		new SWTWorkbenchBot().textWithLabel("Model Name:").setText(name);
		new DefaultCombo().setSelection(clazz);
		new DefaultCombo(1).setSelection(type);		
		if (modelBuilder != null){
			new DefaultTable().select(modelBuilder);
		}
		/*if (modelBuilder != null){
			next(); 
		}*/
	}
	
	@Override
	public void next(){
		if (new PushButton("Next >").isEnabled()){
			new PushButton("Next >").click();
		}
	}
	
	
	public void fillSecondPage(){//TODO - redo, can be also web service view model... 
	/*	if (pathToExistingModel != null){//type = cp ex or transform
			new PushButton("...").click();
			new DefaultTreeItem(pathToExistingModel).select();
			new PushButton("OK").click();
		} else if ((rootElement != null) && (pathToXmlSchema != null)){//XSD datatype, cp ex
			new PushButton(0).click();
			new DefaultTreeItem(pathToXmlSchema).select();
			new PushButton("OK").click();
			new DefaultTable().select(rootElement);
			new PushButton(1).click();// >
		}*/
		if ((clazz.equals(ModelClass.XSD)) && (modelBuilder.equals(ModelBuilder.COPY_EXISTING))
				|| (clazz.equals(ModelClass.XML) && modelBuilder.equals(ModelBuilder.BUILD_FROM_XML_SCHEMA))){
			new PushButton(0).click();
			new DefaultTreeItem(pathToXmlSchema).select();
			new PushButton("OK").click();
			new DefaultTable().select(rootElement);
			new PushButton(1).click();// >
		}
		else if (modelBuilder.equals(ModelBuilder.COPY_EXISTING) || modelBuilder.equals(ModelBuilder.TRANSFORM_EXISTING)){
			new PushButton("...").click();
			new DefaultTreeItem(pathToExistingModel).select();
			new PushButton("OK").click();
		} 
		
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setClass(String clazz) {
		this.clazz = clazz;
	}
	
	public void setModelBuilder(String modelBuilder) {
		this.modelBuilder = modelBuilder;
	}

	public void setWsProps(Properties wsProps) {
		this.wsProps = wsProps;
	}

	public void setWsdlLocation(String wsdlLocation) {
		this.wsdlLocation = wsdlLocation;
	}

	public String[] getSelectedElement() {
		return selectedElement;
	}

	public void setSelectedElement(String[] selectedElement) {
		this.selectedElement = selectedElement;
	}
	
	//TODO add support for other choices
	//1. ws view, cp ex  -> next -> model -> finish
	//2. ws view, build from wsdl or URL -> next -> properties -> next -> next -> next-> FINISH disabled!
	//3. rel src, file translator -> next -> available proc -> finish
	//4. rel src, ws tran -> next -> avail. proc -> finish
	//5. rel src, copy from ex -> next -> ex. model -> finish
	//6. rel view, transform -> next -> ex. model, settings -> finish
	//7. rel view, copy from ex -> next -> ex. model -> finish
	//8. xml view, cp ex -> next -> model -> finish
	//9. xml view, build from xsd -> xsd, properties -> next -> doc statistics -> next -> preview -> finish
	//10. xsd dtype, cp ex ->  next ->model -> finish
	
}
