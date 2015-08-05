package org.jboss.tools.teiid.reddeer;

import java.util.Arrays;
import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorerView;

public class Procedure extends ModelObject{

	private String project;

	private String model;

	private String procedure;
	
	private String[] pathToModel;
	
	public static class Type{
		public static final String RELSRC_PROCEDURE = "Procedure";
		public static final String RELSRC_SOURCE_FUNCTION = "Source Function";
		public static final String RELVIEW_USER_DEFINED_FUNCTION = "User Defined Function";
		public static final String RELVIEW_PROCEDURE = "Procedure";
	}
	
	
	//private static final String TRANSFORMATION_SQL = "Transformation SQL";
	//private static final String PROPERTIES = "Properties";
	private static final String PARAMETERS = "Parameters";
	private static final String RESULT_SET = "Result Set";
	private static final String DESCRIPTION = "Description";
	//private static final String NATIVE_QUERY = "Native Query";
	
	public static class Template{
		public static final String INSERT = "INSERT Procedure";
		public static final String UPDATE= "UPDATE Procedure";
		public static final String DELETE = "DELETE Procedure";
		public static final String SOAP_WS_CREATE = "SOAP Web Service - \"Create\" Procedure";
		public static final String SOAP_WS_EXTRACT = "SOAP Web Service - \"Extract\" Procedure";
		public static final String REST = "REST Procedure";
		public static final String SIMPLE = "Simple Procedure";
		
	}
	
	public static class UpdateCount{
		public static final String AUTO = "AUTO";
		public static final String MULTIPLE = "MULTIPLE";
		public static final String ZERO = "ZERO";
		public static final String ONE = "ONE";
	}
	

	public static class FunctionProperties{
		public static String DETERMINISTIC = "Deterministic";
		public static String RETURNS_NULL_ON_NULL = "Returns null on null";
		public static String VARIABLE_ARGUMENTS = "Variable Arguments";
	}
	
	public static class AggregateProperties{
		public static String AGG = "Aggregate";
		public static String ALLOWS_DISTINCT = "Allows Distinct";
		public static String ALLOWS_ORDER_BY = "Allows Order-by";
		public static String ANALYTIC = "Analytic";
		public static String DECOMPOSABLE = "Decomposable";
		public static String USES_DISTINCT_ROWS = "Uses Distinct Rows";
	}
	
	public Procedure(String project, String model, String procedure) {
		this.project = project;
		this.model = model;
		this.procedure = procedure;
	}
	
	public Procedure(String... pathToModel){
		this.pathToModel = pathToModel;
	}

	public void addParameter(String name, String type) {
		addParameterName(name);
		addParameterType(name, type);
	}
	
	public void addParameter2(String name, String type) {
		addParameterName2(name);
		addParameterType2(name, type);
	}
	
	public Procedure(){
		
	}
	
	/**
	 * 
	 * @param b
	 * @param name of parameter
	 */
	public void addParameter(boolean b, String name){
		updatePathToModel();
		String[] pathToProcedure = Arrays.copyOf(pathToModel, pathToModel.length+1);
		pathToProcedure[pathToProcedure.length-1] = procedure;
		new DefaultTreeItem(pathToProcedure).select();//procedure
		new ContextMenu("New Child", "Procedure Parameter").select();
		
		String[] pathToNewParam = Arrays.copyOf(pathToProcedure, pathToProcedure.length+1);
		pathToNewParam[pathToNewParam.length-1] = "NewProcedureParameter";
		new DefaultTreeItem(pathToNewParam).select();	
		ModelEditor me = new ModelEditor("BooksInfo.xmi");
		me.show();
		ModelExplorerView modelView = TeiidPerspective.getInstance().getModelExplorerView();
		modelView.open();
		new DefaultTreeItem(pathToNewParam).select();
		new ContextMenu("Rename...").select();//highlights text to be edited
		new DefaultText("NewProcedureParameter").setText(name);
		new DefaultTreeItem(pathToModel).select();//click somewhere else
		//me.save();
	}
	
	public void addParameterType(String parameterName, String type, boolean b){
		updatePathToModel();
		String[] path = Arrays.copyOf(pathToModel, pathToModel.length+2);
		path[path.length-2]= procedure;
		path[path.length-1] = parameterName;
		
		new DefaultTreeItem(path).select();
		new ContextMenu("Modeling", "Set Datatype").select();

		Shell shell = new DefaultShell("Select a Datatype");
		new SWTWorkbenchBot().table().getTableItem(type).select();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	private void addParameterName(String parameter) {
		new DefaultTreeItem(project, model, procedure).select();
		new ContextMenu("New Child", "Procedure Parameter").select();
		new LabeledText("NewProcedureParameter").setText(parameter);

		// bot.waitUntil(new TreeContainsNode(bot.tree(), project, model,
		// procedure, parameter), TaskDuration.NORMAL.getTimeout());
	}
	
	private void addParameterName2(String parameter) {
		new ModelExplorerView().open();
		new DefaultTreeItem(project, model, procedure).select();
		new ContextMenu("New Child", "Procedure Parameter").select();
		new DefaultText("NewProcedureParameter").setText(parameter);
		new DefaultTreeItem(project, model, procedure).collapse();
			}

	private void addParameterType(String parameter, String type) {
		new DefaultTreeItem(project, model, procedure).select();
		new ContextMenu("Modeling", "Set Datatype").select();

		Shell shell = new DefaultShell("Select a Datatype");
		new SWTWorkbenchBot().table().getTableItem(type).select();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	private void addParameterType2(String parameter, String type) {
		new ModelExplorerView().open();
		new DefaultTreeItem(project, model, procedure).expand();
		new DefaultTreeItem(project, model, procedure, parameter).select();
		new ContextMenu("Modeling", "Set Datatype").select();
		new SWTWorkbenchBot().table().getTableItem(type).select();
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Select a Datatype"), TimePeriod.LONG);
		new ModelEditor(model).save();
	}
	
	/**
	 * Creates a procedure
	 * @param name of procedure to be created
	 * @param procedure - true if procedure is created, false if function
	 */
	public void create(String name, boolean procedure) {
		updatePathToModel();
		this.procedure = name;
		
		new DefaultTreeItem(pathToModel).select();
		new ContextMenu("New Child", "Procedure...").select();
		if (procedure){
			new PushButton("OK").click();
		}
		new LabeledText("Name").setText(name);
		new PushButton("OK").click();
	}

	private void updatePathToModel() {
		if (pathToModel == null){
			pathToModel = new String[2];
			pathToModel[0] = this.project;
			pathToModel[1] = this.model;
		}
	}
	
	public void create(String name, Properties props) {
		
		//select procedure type
		String type = props.getProperty("type");
		new RadioButton(type).click();
		
		new PushButton("OK").click();
		
		//Name
		new LabeledText("Name").setText(name);
		
		String prop = null;
		//Name In Source
		if ((prop = props.getProperty("nameInSrc")) != null){
			new LabeledText("Name In Source").setText(prop);
		}
		if(Type.RELVIEW_PROCEDURE.equals(type)){
			new DefaultShell("Create Relational View Procedure");
		} else if(Type.RELVIEW_USER_DEFINED_FUNCTION.equals(type)){
			new DefaultShell("Create User Defined Function");
		}
		//Properties
		fillProperties(type, props);
		
		//Parameters
		fillParameters(type, props);
		
		//Result Set
		fillResultSet(type, props);
		
		//Description
		fillDescription(type, props);
		
		//Native Query
		fillNativeQuery(type, props);
		
		//Transformation SQL
		fillTransformationSQL(type, props);
		
		if (new ShellWithTextIsAvailable("Create Relational View Procedure").test()) {
			new DefaultShell("Create Relational View Procedure");
		} else {
			new DefaultShell();
		}
		
		new PushButton("OK").click();
	}
	
	private void fillTransformationSQL(String type, Properties props) {
		if (type.equals(Type.RELVIEW_PROCEDURE) && !props.containsKey("nativeQuery")){
			super.fillTransformationSQL(props);
		}
	}

	private void fillNativeQuery(String type, Properties props) {
		if (type.equals(Type.RELSRC_PROCEDURE)){
			super.fillNativeQuery(props);
		}
		
	}

	private void fillDescription(String type, Properties props) {
		String prop = props.getProperty("description");//text of description
		if (prop != null){
			new SWTWorkbenchBot().tabItem(DESCRIPTION).activate();
			//ALL set text
			new SWTWorkbenchBot().styledText().setText(prop);
		}
		
		
	}

	private void fillResultSet(String type, Properties props) {
		if (type.equals(Type.RELSRC_PROCEDURE) || type.equals(Type.RELVIEW_PROCEDURE)){
			String prop = props.getProperty("includeResultSet");//true
			if (prop != null){
				new SWTWorkbenchBot().tabItem(RESULT_SET).activate();
				//include
				new CheckBox("Include").click();
				//name
				new SWTWorkbenchBot().text(3).setText(props.getProperty("resultSetName"));//name 
				//add, delete, move up, move down
				prop = props.getProperty("cols");//colName1,colName2,colName3,...
				if (prop != null){
					setupTableProps(prop);
				}
			}
		}
	}


	private void fillParameters(String type, Properties props) {
		String prop = props.getProperty("params");// paramName1,paramName2,...
		if (prop != null) {
			new DefaultTabItem(PARAMETERS).activate();
			// ALL add, delete, move up, move down
			String[] cols = { prop };
			if (prop.contains(",")) {
				cols = prop.split(",");
			}
			for (String col : cols) {
				addProcParam(col, false);
			}
		}
		prop = props.getProperty("returnParam");
		if (prop != null) {
			new DefaultTabItem(PARAMETERS).activate();
			addProcParam(prop, true);
		}
	}

	private void addProcParam(String name, boolean isReturn) {
		// TOOD: datatyp
		new PushButton("Add").click();
		int lines = new DefaultTable().getItems().size();
		new DefaultTable().getItem(lines - 1).select();
		new PushButton("Edit...").click();
		new DefaultShell("Edit Parameter");
		new LabeledText("Name").setText(name);
		if(isReturn){
			new LabeledCombo("Direction").setSelection("RETURN");
		}
		new PushButton("OK").click();
	}


	private void fillProperties(String type, Properties props) {
		new DefaultTabItem(PROPERTIES).activate();
		if (type.equals(Type.RELSRC_PROCEDURE) || type.equals(Type.RELVIEW_PROCEDURE)){
			//update count new org.jboss.reddeer.swt.impl.combo.DefaultCombo().getSelection()
			//non-prepared new org.jboss.reddeer.swt.impl.button.CheckBox("Non-Prepared").click();
			String prop = props.getProperty("updateCount");//AUTO,MULTIPLE,...
			if (prop != null){
				new DefaultCombo().setSelection(prop);
			}
			prop = props.getProperty("nonPrepared");//true
			if (prop != null){
				new CheckBox("Non-Prepared").click();
			}
		}
		if (type.equals(Type.RELSRC_SOURCE_FUNCTION)){
			//function properties:
			//deterministic, returns null on null, variable args
			String prop = props.getProperty("functionProps");
			if (prop != null){
				checkboxProperties(prop);
			}
			
			//aggregate properties:
			//aggregate, allows distinct, allows order-by, analytic, decomposable, uses distinct rows
			prop = props.getProperty("aggregateProps");
			if (prop != null){
				checkboxProperties(prop);
			}
		}
		if (type.equals(Type.RELVIEW_USER_DEFINED_FUNCTION)){
			//function properties:
			//function category, java class, java method, udf jar path
			String prop = props.getProperty("functionCategory");
			if (prop != null){
				new LabeledText("Function Category").setText(prop);
			}
			prop = props.getProperty("javaClass");
			if (prop != null){
				new LabeledText("Java Class").setText(prop);
			}
			prop = props.getProperty("javaMethod");
			if (prop != null){
				new LabeledText("Java Method").setText(prop);
			}
			prop = props.getProperty("udfJarPath");
			if (prop != null){
				new LabeledText("UDF Jar Path").setText(prop);
			}
			
			//deterministic, returns null on null, variable args
			prop = props.getProperty("functionProps");
			if (prop != null){
				checkboxProperties(prop);
			}
			
			//aggregate properties:
			//aggregate, allows distinct, allows order-by, analytic, decomposable, uses distinct rows
			prop = props.getProperty("aggregateProps");
			if (prop != null){
				checkboxProperties(prop);
			}
		}
	}
	
	private void checkboxProperties(String loadedProp){
		String[] props = {loadedProp};
		if (loadedProp.contains(",")){
			props = loadedProp.split(",");
		}
		for (String prop : props){
			new CheckBox(prop).click();
		}
	}

	//1. rel src
		//1.1 procedure
		//properties (*), parameters(**), result set, description(***), native query

	//1.2 src function
	//properties, parameters(**), description(***)
	
	//2. rel view
	//2.1 procedure
	//properties(*), parameters(**), transformation sql, result set, description(***)
	
	//2.2 udf
	//properties, parameters(**), description(***)
	
}
