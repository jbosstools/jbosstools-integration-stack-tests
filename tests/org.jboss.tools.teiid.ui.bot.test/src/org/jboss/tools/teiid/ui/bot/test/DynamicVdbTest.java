package org.jboss.tools.teiid.ui.bot.test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace;

import java.io.File;
import java.io.StringReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.hamcrest.text.IsEmptyString;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsResourceMatcher;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.GenerateDynamicVdbDialog;
import org.jboss.tools.teiid.reddeer.dialog.GenerateVdbArchiveDialog;
import org.jboss.tools.teiid.reddeer.editor.DataRolesEditor;
import org.jboss.tools.teiid.reddeer.editor.DataRolesEditor.PermissionType;
import org.jboss.tools.teiid.reddeer.editor.RelationalModelEditor;
import org.jboss.tools.teiid.reddeer.editor.TableEditor;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.editor.VDBEditor;
import org.jboss.tools.teiid.reddeer.matcher.ModelEditorItemMatcher;
import org.jboss.tools.teiid.reddeer.matcher.TableItemMatcher;
import org.jboss.tools.teiid.reddeer.modeling.ModelColumn;
import org.jboss.tools.teiid.reddeer.modeling.ModelTable;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ServersViewExt;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportFromFileSystemWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.ImportProjectWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.xml.sax.InputSource;

@RunWith(RedDeerSuite.class)
@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles = {
	ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER,
	ConnectionProfileConstants.POSTGRESQL_92_DVQE })
public class DynamicVdbTest {

	private static final String PROCEDURE_MODEL = "ProcedureModel";

	@InjectRequirement
	private TeiidServerRequirement teiidServer;

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	private static final String PROJECT_NAME = "DynamicVdbProject";
	private static final String IMPORT_PROJECT_NAME = "ImportProject";
	private static final String BQT_MODEL_NAME = "postgresql92Model";
	private static final String VIEW_MODEL = "bqtViewModel";

	private static final String CREATE_FOREIGN_TABLE = "CREATE FOREIGN TABLE ";
	private static final String CREATE_VIRTUAL_TABLE = "CREATE VIEW ";

	private static final String UDF_LIB_PATH = "target/proc-udf/MyTestUdf/lib/";
	private static final String UDF_LIB = "MyTestUdf-1.0-SNAPSHOT.jar";

	@Before
	public void before() {
		
		new ModelExplorer().deleteAllProjectsSafely();
		
		ImportProjectWizard.openWizard()
			   .setPath(new File("resources/projects/" + PROJECT_NAME).getAbsolutePath())
		       .finish();
		
		new ModelExplorer().createProject(IMPORT_PROJECT_NAME);
	}

	@After
	public void after() {
		new ModelExplorer().deleteAllProjectsSafely();
	}

	@Test
	public void exportWithSourceAndViewModel() {

		String staticVdbName = "SourceAndViewVdb";
		String dynamicVdbName = staticVdbName + "Dynamic";

		// set connection profile
		new ModelExplorer().changeConnectionProfile(ConnectionProfileConstants.POSTGRESQL_92_DVQE, PROJECT_NAME,
				BQT_MODEL_NAME);

		// create static vdb
		createVdb(PROJECT_NAME, staticVdbName, VIEW_MODEL + ".xmi");

		// create dynamic vdb from static
		String dynamicVdbContent = createDynamicVdb(PROJECT_NAME, staticVdbName, dynamicVdbName);

		// get tables in original models
		List<ModelTable> sourceTables = getTables(PROJECT_NAME, BQT_MODEL_NAME);
		List<ModelTable> viewTables = getTables(PROJECT_NAME, VIEW_MODEL);

		// check source model

		System.out.println(getXPath(dynamicVdbContent, "/vdb/model[@name='postgresql92Model']"));
		collector.checkThat("wrong model type created",
				getXPath(dynamicVdbContent, "/vdb/model[@name='postgresql92Model']/@type"), is(""));
		collector.checkThat("wrong source jndi name",
				getXPath(dynamicVdbContent, "/vdb/model[@name='postgresql92Model']/source/@connection-jndi-name"),
				is(BQT_MODEL_NAME));
		collector.checkThat("wrong source translator name",
				getXPath(dynamicVdbContent, "/vdb/model[@name='postgresql92Model']/source/@translator-name"),
				is("postgresql"));
		checkMetadata(getXPath(dynamicVdbContent, "/vdb/model[@name='postgresql92Model']/metadata[1]"), sourceTables,
				CREATE_FOREIGN_TABLE);

		// check view model

		collector.checkThat("wrong model type created",
				getXPath(dynamicVdbContent, "/vdb/model[@name='bqtViewModel']/@type"), is("VIRTUAL"));
		checkMetadata(getXPath(dynamicVdbContent, "/vdb/model[@name='bqtViewModel']/metadata[1]"), viewTables,
				CREATE_VIRTUAL_TABLE);

		new ServersViewExt().createDatasource(teiidServer.getName(), ConnectionProfileConstants.POSTGRESQL_92_DVQE,
				BQT_MODEL_NAME);
		checkDeployOk(staticVdbName, dynamicVdbName);
		checkPreview(dynamicVdbName, "SELECT * FROM " + BQT_MODEL_NAME + ".smalla");
		checkPreview(dynamicVdbName, "SELECT * FROM " + VIEW_MODEL + ".smalla");

		checkContentsSame(staticVdbName, dynamicVdbContent);

	}

	@Test
	public void exportWithForeignKeys() {
		String staticVdbName = "FkVdb";
		String dynamicVdbName = staticVdbName + "Dynamic";
		String modelName = "FkModel";

		// create static vdb
		createVdb(PROJECT_NAME, staticVdbName, modelName + ".xmi");

		// create dynamic vdb from static
		String dynamicVdbContent = createDynamicVdb(PROJECT_NAME, staticVdbName, dynamicVdbName);

		collector.checkThat("wrong foreign key in ddl",
				getXPath(dynamicVdbContent, "/vdb/model[@name='FkModel']/metadata[1]"), new StringContains(
						"CONSTRAINT FKI_SECOND_THIRD_ID FOREIGN KEY(CatalogSecondID, CatalogThirdID) REFERENCES Catalog(ThirdID, SecondID)"));
	}

	@Test
	public void exportWithRestProcedure() {
		String staticVdbName = "RestProcedureVdb";
		String dynamicVdbName = staticVdbName + "Dynamic";

		// create static vdb
		createVdb(PROJECT_NAME, staticVdbName, PROCEDURE_MODEL + ".xmi");
		new ModelExplorer().getModelProject(PROJECT_NAME).open(staticVdbName + ".vdb");
		VDBEditor vdbEditor = VDBEditor.getInstance(staticVdbName + ".vdb");
		vdbEditor.setGenerateRestWar(true);
		vdbEditor.save();

		// create dynamic vdb from static
		String dynamicVdbContent = createDynamicVdb(PROJECT_NAME, staticVdbName, dynamicVdbName);

		// check auto-generate rest war property, this looks like this:
		// <property name="{http://teiid.org/rest}auto-generate" value="true"/>
		collector.checkThat("wrong value for Auto generate REST WAR property",
				getXPath(dynamicVdbContent, "/vdb/property[@name='{http://teiid.org/rest}auto-generate']/@value"),
				is("true"));

		// check procedure metadata
		String procViewMetadata2 = getXPath(dynamicVdbContent, "/vdb/model[@name='ProcedureModel']/metadata[1]")
				.replace('\n', ' ');

		collector.checkThat("rest options not in ddl", procViewMetadata2,
				new RegexMatcher(".*OPTIONS\\(.*\"REST:URI\" 'test/\\{p1\\}', \"REST:METHOD\" 'GET'\\).*"));
		collector.checkThat("RETURNS clause not in ddl", procViewMetadata2, new RegexMatcher(".*RETURNS TABLE.*"));
		collector.checkThat("wrong procedure body", procViewMetadata2, new RegexMatcher(".*XMLELEMENT.*"));
		collector.checkThat("REST namespace not set in ddl", procViewMetadata2,
				new RegexMatcher(".*SET NAMESPACE 'http://teiid.org/rest' AS REST;.*"));

		// check deployment
		checkDeployOk(staticVdbName, dynamicVdbName);
		checkPreview(dynamicVdbName, "EXEC testProc('param1')");

		checkContentsSame(staticVdbName, dynamicVdbContent);

	}

	@Test
	public void exportWithCustomProperties() {
		String staticVdbName = "CustomPropertiesVdb";
		String dynamicVdbName = staticVdbName + "Dynamic";

		// create static vdb
		createVdb(PROJECT_NAME, staticVdbName, PROCEDURE_MODEL + ".xmi");
		new ModelExplorer().getModelProject(PROJECT_NAME).open(staticVdbName + ".vdb");
		VDBEditor vdbEditor = VDBEditor.getInstance(staticVdbName + ".vdb");
		vdbEditor.addUserDefinedProperty("customVdbProperty", "someValue");
		vdbEditor.save();

		// create dynamic vdb from static
		String dynamicVdbContent = createDynamicVdb(PROJECT_NAME, staticVdbName, dynamicVdbName);

		collector.checkThat("user defined property not in vdb",
				getXPath(dynamicVdbContent, "/vdb/property[@name=\"customVdbProperty\"]/@value"), is("someValue"));

		checkContentsSame(staticVdbName, dynamicVdbContent);
	}

	
	@Test
	public void exportWithDataRoles() {
		String staticVdbName = "DataRolesVdb";
		String dynamicVdbName = staticVdbName + "Dynamic";
		String viewModelNameXmi = VIEW_MODEL + ".xmi";

		// create static vdb
		createVdb(PROJECT_NAME, staticVdbName, viewModelNameXmi);
		new ModelExplorer().getModelProject(PROJECT_NAME).open(staticVdbName + ".vdb");
		VDBEditor vdbEditor = VDBEditor.getInstance(staticVdbName + ".vdb");

		// setup data roles
		DataRolesEditor dre;

		dre = vdbEditor.addDataRole();
		dre.setName("readers");
		dre.addRole("readers");
		dre.addRole("user");
		dre.setModelPermission(PermissionType.READ, true, viewModelNameXmi);
		dre.setModelPermission(PermissionType.READ, false, viewModelNameXmi, "smalla", "intnum : int");
		dre.setModelPermission(PermissionType.READ, false, viewModelNameXmi, "smallb");
		dre.addColumnMask("", "CASE WHEN TRUE THEN 'SECRET' END", 0, viewModelNameXmi, "smalla", "intnum : int");
		dre.addColumnMask("stringnum='1'", "0", 1, viewModelNameXmi, "smalla", "stringnum : string(20)");
		dre.finish();

		dre = vdbEditor.addDataRole();
		dre.setName("admins");
		dre.addRole("admins");
		dre.setModelPermission(PermissionType.CREATE, true, viewModelNameXmi);
		dre.setModelPermission(PermissionType.READ, true, viewModelNameXmi);
		dre.setModelPermission(PermissionType.UPDATE, true, viewModelNameXmi);
		dre.setModelPermission(PermissionType.DELETE, true, viewModelNameXmi);
		dre.setModelPermission(PermissionType.ALTER, true, viewModelNameXmi);
		dre.setModelPermission(PermissionType.EXECUTE, true, viewModelNameXmi);
		dre.finish();

		dre = vdbEditor.addDataRole();
		dre.setName("updaters");
		dre.addRole("updaters");
		dre.setModelPermission(PermissionType.UPDATE, true, viewModelNameXmi);
		dre.setModelPermission(PermissionType.READ, false, viewModelNameXmi);
		dre.setModelPermission(PermissionType.READ, true, viewModelNameXmi, "smalla", "intkey : int");
		dre.setModelPermission(PermissionType.READ, true, viewModelNameXmi, "smallb", "intkey : int");
		dre.finish(); // workaround unrelated TEIIDDES-2742
		dre = vdbEditor.getDataRole("updaters");
		dre.addRowFilter("booleanvalue=TRUE", true, viewModelNameXmi, "smalla");
		dre.addRowFilter("stringnum='1'", false, viewModelNameXmi, "smallb");
		new DefaultTable().getItem(VIEW_MODEL + ".smallb").click(1); // workaround for unrelated TEIIDDES-2706
		dre.finish();

		vdbEditor.save();

		// create dynamic vdb from static
		String dynamicVdbContent = createDynamicVdb(PROJECT_NAME, staticVdbName, dynamicVdbName);

		// check read permission on view model
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-create", "false");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-read", "true");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-update", "false");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-delete", "false");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-execute", "false");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel", "allow-alter", "false");

		// check read permission on smalla.intnum column
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-create", "");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-read", "false");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-update", "");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-delete", "");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-execute", "");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "allow-alter", "");

		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "condition", "");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "mask",
				"CASE WHEN TRUE THEN 'SECRET' END");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.intnum", "mask/@order", "0");

		// check column mask on smalla.stringnum
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.stringnum", "condition",
				"stringnum='1'");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.stringnum", "mask", "0");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smalla.stringnum", "mask/@order", "1");

		// check read permission on smallb.intnum column
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-create", "");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-read", "false");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-update", "");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-delete", "");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-execute", "");
		checkXpathPermission(dynamicVdbContent, "readers", "bqtViewModel.smallb", "allow-alter", "");

		// check admin permissions on view model
		checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-create", "true");
		checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-read", "true");
		checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-update", "true");
		checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-delete", "true");
		checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-execute", "true");
		checkXpathPermission(dynamicVdbContent, "admins", "bqtViewModel", "allow-alter", "true");

		// check row filter for smalla
		checkXpathPermission(dynamicVdbContent, "updaters", "bqtViewModel.smalla", "condition", "booleanvalue=TRUE");
		checkXpathPermission(dynamicVdbContent, "updaters", "bqtViewModel.smalla", "condition/@constraint", "");

		// check row filter for smallb
		checkXpathPermission(dynamicVdbContent, "updaters", "bqtViewModel.smallb", "condition", "stringnum='1'");
		checkXpathPermission(dynamicVdbContent, "updaters", "bqtViewModel.smallb", "condition/@constraint", "false");

		// check deployment
		checkDeployOk(staticVdbName, dynamicVdbName);

		checkContentsSame(staticVdbName, dynamicVdbContent);
	}

	@Test
	public void exportWithTranslatorOverrides() {
		String staticVdbName = "TranslatorOverridesVdb";
		String dynamicVdbName = staticVdbName + "Dynamic";
		String overrideName = "postgresOverride";

		// set connection profile
		new ModelExplorer().changeConnectionProfile(ConnectionProfileConstants.POSTGRESQL_92_DVQE, PROJECT_NAME,
				BQT_MODEL_NAME);

		// create static vdb
		createVdb(PROJECT_NAME, staticVdbName, BQT_MODEL_NAME + ".xmi");
		new ModelExplorer().getModelProject(PROJECT_NAME).open(staticVdbName + ".vdb");
		VDBEditor vdbEditor = VDBEditor.getInstance(staticVdbName + ".vdb");

		// add translator overrides
		vdbEditor.addTranslatorOverride(overrideName, "postgresql");
		vdbEditor.addTranslatorOverrideProperty(overrideName, "Is Immutable", "false");
		vdbEditor.addTranslatorOverrideProperty(overrideName, "Trim string flag", "true");
		vdbEditor.addTranslatorOverrideProperty(overrideName, "MyCustomProperty", "customValue");
		vdbEditor.setModelTranslator(BQT_MODEL_NAME + ".xmi", BQT_MODEL_NAME, overrideName);
		vdbEditor.save();

		// create dynamic vdb from static
		String dynamicVdbContent = createDynamicVdb(PROJECT_NAME, staticVdbName, dynamicVdbName);

		// check translator
		collector.checkThat("overriden translator not set",
				getXPath(dynamicVdbContent, "/vdb/model[@name='postgresql92Model']/source/@translator-name"),
				is("postgresOverride"));

		// check overriden translator properties
		collector.checkThat("wrong translator override type",
				getXPath(dynamicVdbContent, "/vdb/translator[@name='postgresOverride']/@type"), is("postgresql"));
		collector
				.checkThat("translator override property with default value created",
						getXPath(dynamicVdbContent,
								"/vdb/translator[@name='postgresOverride']/property[@name='Immutable']/@value"),
						is(""));
		collector
				.checkThat("known translator override property not created",
						getXPath(dynamicVdbContent,
								"/vdb/translator[@name='postgresOverride']/property[@name='TrimStrings']/@value"),
						is("true"));
		collector.checkThat("custom translator override property not created",
				getXPath(dynamicVdbContent,
						"/vdb/translator[@name='postgresOverride']/property[@name='MyCustomProperty']/@value"),
				is("customValue"));
	}

	@Test
	public void importWithUdf() {
		String staticVdbName = "UdfProcedureVdb";
		String dynamicVdbName = staticVdbName + "-vdb.xml";

		importDynamicVdb(IMPORT_PROJECT_NAME, dynamicVdbName);
		createArchiveVdb(IMPORT_PROJECT_NAME, dynamicVdbName);

		new ModelExplorer().openModelEditor(IMPORT_PROJECT_NAME, PROCEDURE_MODEL + ".xmi");
		RelationalModelEditor editor = new RelationalModelEditor(PROCEDURE_MODEL + ".xmi");
	    TableEditor tableEditor = editor.openTableEditor();
	    tableEditor.openTab(TableEditor.Tabs.PROCEDURE_PARAMETERS);
	    
		collector.checkThat("return parameter not created", tableEditor.getRow("Name", "RETURNS"), notNullValue());
		collector.checkThat("return param is not of type RETURN", tableEditor.getCellText(1, "RETURNS", "Direction"), is("RETURN"));
		collector.checkThat("stringLeft parameter not created", tableEditor.getRow("Name", "stringLeft"), notNullValue());
		collector.checkThat("stringLeft param is not of type IN", tableEditor.getCellText(1, "stringLeft", "Direction"), is("IN"));

		new ModelExplorer().selectItem(IMPORT_PROJECT_NAME, PROCEDURE_MODEL + ".xmi", "udfConcatNull");

		PropertiesView propertiesView = new PropertiesView();
		collector.checkThat("UDF Jar path not set",
				propertiesView.getProperty("Extension", "relational:UDF Jar Path").getPropertyValue(),
				new IsNot<>(new IsEmptyString()));
		collector.checkThat("wrong function category",
				propertiesView.getProperty("Extension", "relational:Function Category").getPropertyValue(),
				is("MY_TESTING_FUNCTION_CATEGORY"));
		collector.checkThat("wrong java class",
				propertiesView.getProperty("Extension", "relational:Java Class").getPropertyValue(),
				is("userdefinedfunctions.MyConcatNull"));
		collector.checkThat("wrong java method",
				propertiesView.getProperty("Extension", "relational:Java Method").getPropertyValue(),
				is("myConcatNull"));

		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported view model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(PROCEDURE_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(staticVdbName + ".vdb")),
				empty());

	}

	@Test
	public void importWithCustomProperties() {

		String dynamicVdbName = "CustomPropertyVdb-vdb.xml";
		String staticVdbName = "CustomPropertyVdb";

		importDynamicVdb(IMPORT_PROJECT_NAME, dynamicVdbName);
		createArchiveVdb(IMPORT_PROJECT_NAME, dynamicVdbName);

		new ModelExplorer().getModelProject(IMPORT_PROJECT_NAME).open(staticVdbName + ".vdb");
		new DefaultCTabItem("Advanced").activate();
		new DefaultCTabItem("User Defined Properties").activate();

		String propValue = (String) collector.checkSucceeds(new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				DefaultTable table = new DefaultTable();
				return table.getItem("lib", 0).getText(1);
			}
		});

		collector.checkThat("wrong property value", propValue, is("test"));
	}

	@Test
	public void importWithMaterializedViews() {

		String staticVdbName = "MatViewsVdb";
		String dynamicVdbName = staticVdbName + "-vdb.xml";
		String viewModelName = "ViewModel.xmi";

		importDynamicVdb(IMPORT_PROJECT_NAME, dynamicVdbName);
		createArchiveVdb(IMPORT_PROJECT_NAME, dynamicVdbName);

		new ModelExplorer().openModelEditor(IMPORT_PROJECT_NAME, viewModelName);

		PropertiesView propertiesView = new PropertiesView();
		for (String tableName : new String[] { "internal_short_ttl", "internal_long_ttl", "external_long_ttl" }) {
			new ModelExplorer().getProject(IMPORT_PROJECT_NAME).getProjectItem(viewModelName, tableName)
					.select();
			collector.checkThat("materialized property not set",
					propertiesView.getProperty("Misc", "Materialized").getPropertyValue(), is("true"));
		}
		collector.checkThat("materialized table property not set",
				propertiesView.getProperty("Misc", "Materialized Table").getPropertyValue(),
				is("Source.DB.PUBLIC.MAT_VIEW"));

		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported view model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(viewModelName)),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(staticVdbName + ".vdb")),
				empty());

		RelationalModelEditor editor = new RelationalModelEditor(viewModelName);
	    TransformationEditor transformationEditor =  editor.openTransformationDiagram(ModelEditorItemMatcher.TABLE, "internal_short_ttl");

		collector.checkThat("cache hint not in transformation", transformationEditor.getTransformation().replaceAll("\\s+", " "),
				new StringContains("/*+ cache(ttl:100)*/"));
		
		editor.returnToPackageDiagram();
		transformationEditor =  editor.openTransformationDiagram(ModelEditorItemMatcher.TABLE, "internal_long_ttl");

		collector.checkThat("cache hint not in transformation", transformationEditor.getTransformation().replaceAll("\\s+", " "),
				new StringContains("/*+ cache(ttl:1000)*/"));

		// TODO: check all the other materialized properties once TEIIDDES-2745 is resolved
	}

	@Test
	public void importWithTranslatorOverrides() {

		String staticVdbName = "TranslatorOverridesVdb";
		String dynamicVdbName = staticVdbName + "-vdb.xml";

		importDynamicVdb(IMPORT_PROJECT_NAME, dynamicVdbName);
		createArchiveVdb(IMPORT_PROJECT_NAME, dynamicVdbName);
		
		new ModelExplorer().getModelProject(IMPORT_PROJECT_NAME).open(staticVdbName + ".vdb");
		VDBEditor vdbEditor = VDBEditor.getInstance(staticVdbName + ".vdb");

		Properties translatorOverrideProperties = vdbEditor.getTranslatorOverrideProperties("postgresOverride");
		collector.checkThat("Wrong value for custom property MyCustomProperty",
				translatorOverrideProperties.getProperty("MyCustomProperty"), is("customValue"));
		collector.checkThat("Wrong value for predefined property TrimStrings",
				translatorOverrideProperties.getProperty("Trim string flag"), is("true"));

		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(staticVdbName + ".vdb")),
				empty());
	}

	@Test
	public void importWithDataRoles() {
		String staticVdbName = "DataRolesVdb";
		String dynamicVdbName = staticVdbName + "-vdb.xml";
		String viewModelNameXmi = VIEW_MODEL + ".xmi";

		importDynamicVdb(IMPORT_PROJECT_NAME, dynamicVdbName);
		createArchiveVdb(IMPORT_PROJECT_NAME, dynamicVdbName);

		new ModelExplorer().getModelProject(IMPORT_PROJECT_NAME).open(staticVdbName + ".vdb");
		VDBEditor vdbEditor = VDBEditor.getInstance(staticVdbName + ".vdb");

		DataRolesEditor dre = vdbEditor.getDataRole("readers");
		List<String> roles = dre.getRoles();
		collector.checkThat("wrong groups assigned to readers role", roles, contains("readers", "user"));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.READ, viewModelNameXmi), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.ALTER, viewModelNameXmi), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.CREATE, viewModelNameXmi), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.DELETE, viewModelNameXmi), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.EXECUTE, viewModelNameXmi), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.UPDATE, viewModelNameXmi), is(false));
		// TODO: change the following to int once TEIIDDES-2737 is resolved
		collector.checkThat("wrong permission for bqtViewModel.smalla.intnum",
				dre.getModelPermission(PermissionType.READ, viewModelNameXmi, "smalla", "intnum : biginteger"),
				is(false));
		collector.checkThat("wrong permission for bqtViewModel.smallb",
				dre.getModelPermission(PermissionType.READ, viewModelNameXmi, "smallb"), is(false));

		collector.checkThat("wrong column mask for bqtViewModel.smalla.intnum",
				dre.getColumnMask("bqtViewModel.smalla.intnum"), is("CASE WHEN TRUE THEN 'SECRET' END"));
		collector.checkThat("wrong condition mask for bqtViewModel.smalla.intnum",
				dre.getColumnMaskCondition("bqtViewModel.smalla.intnum"), is(""));
		collector.checkThat("wrong column mask for bqtViewModel.smalla.stringnum",
				dre.getColumnMask("bqtViewModel.smalla.stringnum"), is("0"));
		collector.checkThat("wrong condition mask for bqtViewModel.smalla.stringnum",
				dre.getColumnMaskCondition("bqtViewModel.smalla.stringnum"), is("stringnum='1'"));
		dre.cancel();

		dre = vdbEditor.getDataRole("admins");
		roles = dre.getRoles();
		collector.checkThat("wrong groups assigned to admins role", roles, contains("admins"));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.READ, viewModelNameXmi), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.ALTER, viewModelNameXmi), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.CREATE, viewModelNameXmi), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.DELETE, viewModelNameXmi), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.EXECUTE, viewModelNameXmi), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.UPDATE, viewModelNameXmi), is(true));
		dre.cancel();

		dre = vdbEditor.getDataRole("updaters");
		roles = dre.getRoles();
		collector.checkThat("wrong groups assigned to updaters role", roles, contains("updaters"));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.READ, viewModelNameXmi), is(true));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.ALTER, viewModelNameXmi), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.CREATE, viewModelNameXmi), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.DELETE, viewModelNameXmi), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.EXECUTE, viewModelNameXmi), is(false));
		collector.checkThat("wrong permission for bqtViewModel",
				dre.getModelPermission(PermissionType.UPDATE, viewModelNameXmi), is(true));
		collector.checkThat("wrong permission for bqtViewModel.smalla.intkey",
				dre.getModelPermission(PermissionType.READ, viewModelNameXmi, "smalla", "intkey : biginteger"),
				is(true));
		collector.checkThat("wrong permission for bqtViewModel.smallb.intkey",
				dre.getModelPermission(PermissionType.READ, viewModelNameXmi, "smallb", "intkey : biginteger"),
				is(true));
		collector.checkThat("wrong permission for bqtViewModel.smalla.intnum",
				dre.getModelPermission(PermissionType.READ, viewModelNameXmi, "smalla", "intnum : biginteger"),
				is(false));
		collector.checkThat("wrong permission for bqtViewModel.smallb.intnum",
				dre.getModelPermission(PermissionType.READ, viewModelNameXmi, "smallb", "intnum : biginteger"),
				is(false));
		collector.checkThat("wrong row filter condition for bqtViewModel.smalla",
				dre.getRowFilterCondition("bqtViewModel.smalla"), is("booleanvalue=TRUE"));
		collector.checkThat("wrong row filter constraint value for bqtViewModel.smalla",
				dre.getRowFilterConstraint("bqtViewModel.smalla"), is("true"));
		collector.checkThat("wrong row filter condition for bqtViewModel.smallb",
				dre.getRowFilterCondition("bqtViewModel.smallb"), is("stringnum='1'"));
		collector.checkThat("wrong row filter constraint value for bqtViewModel.smallb",
				dre.getRowFilterConstraint("bqtViewModel.smallb"), is("false"));
		dre.cancel();

		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(staticVdbName + ".vdb")),
				empty());
	}

	@Test
	public void importWithForeignKeys() {

		String staticVdbName = "FkVdb";
		String dynamicVdbName = staticVdbName + "-vdb.xml";
		String sourceModelName = "FkModel";
		String viewModelName = "FkView";

		importDynamicVdb(IMPORT_PROJECT_NAME, dynamicVdbName);
		createArchiveVdb(IMPORT_PROJECT_NAME, dynamicVdbName);

		new ModelExplorer().openModelEditor(IMPORT_PROJECT_NAME, sourceModelName + ".xmi");
		RelationalModelEditor editor = new RelationalModelEditor(sourceModelName + ".xmi");
	    TableEditor tableEditor = editor.openTableEditor();

		// check unique constraint in source model
		tableEditor.openTab(TableEditor.Tabs.UNIQUE_CONSTRAINTS);
		TableItem uc = new DefaultTable(0).getItems(new TableItemMatcher(1, "UC")).get(0);
		collector.checkThat("column not referenced in unique constraint", uc.getText(3),
				new StringContains("SecondID"));
		collector.checkThat("column not referenced in unique constraint", uc.getText(3), new StringContains("ThirdID"));

		// check foreign key in source model
		tableEditor.openTab(TableEditor.Tabs.FOREIGN_KEYS);
		TableItem fk = new DefaultTable(0).getItems(new TableItemMatcher(1, "FKI_SECOND_THIRD_ID")).get(0);
		collector.checkThat("wrong unique key referenced by fk", fk.getText(6), startsWith("UC"));

		// check unique constraint in view model
		new ModelExplorer().openModelEditor(IMPORT_PROJECT_NAME, viewModelName + ".xmi");
		editor = new RelationalModelEditor(viewModelName + ".xmi");
		tableEditor = editor.openTableEditor();
		tableEditor.openTab(TableEditor.Tabs.UNIQUE_CONSTRAINTS);
		uc = new DefaultTable(0).getItems(new TableItemMatcher(1, "UC")).get(0);
		collector.checkThat("column not referenced in unique constraint", uc.getText(3),
				new StringContains("SecondID"));
		collector.checkThat("column not referenced in unique constraint", uc.getText(3), new StringContains("ThirdID"));

		// check foreign key in view model
		try {
			tableEditor.openTab(TableEditor.Tabs.FOREIGN_KEYS);
			List<TableItem> fks = new DefaultTable(0).getItems(new TableItemMatcher(1, "FKI_SECOND_THIRD_ID"));
			collector.checkThat("wrong unique key referenced by fk", fks.get(0).getText(6), startsWith("UC"));
		} catch (CoreLayerException e) {
			collector.addError(e);
		}

		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported view model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(viewModelName + ".xmi")),
				empty());
		collector.checkThat("Errors in imported source model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(sourceModelName + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(staticVdbName + ".vdb")),
				empty());
	}

	@Test
	public void importWithSourceAndViewModel() {

		String staticVdbName = "SourceAndViewVdb";
		String dynamicVdbName = staticVdbName + "-vdb.xml";

		importDynamicVdb(IMPORT_PROJECT_NAME, dynamicVdbName);
		createArchiveVdb(IMPORT_PROJECT_NAME, dynamicVdbName);

		new ModelExplorer().getModelProject(IMPORT_PROJECT_NAME).open(staticVdbName + ".vdb");
		VDBEditor vdbEditor = VDBEditor.getInstance(staticVdbName + ".vdb");
		collector.checkThat("wrong data source for source model", vdbEditor.getDataSourceName(BQT_MODEL_NAME + ".xmi"),
				is("postgresql92Model"));
		vdbEditor.saveAndClose();

		List<ModelTable> origSourceTables = getTables(PROJECT_NAME, BQT_MODEL_NAME);
		List<ModelTable> origViewTables = getTables(PROJECT_NAME, VIEW_MODEL);
		List<ModelTable> importedSourceTables = getTables(IMPORT_PROJECT_NAME, BQT_MODEL_NAME);
		List<ModelTable> importedViewTables = getTables(IMPORT_PROJECT_NAME, VIEW_MODEL);

		checkTablesSame(origSourceTables, importedSourceTables);
		checkTablesSame(origViewTables, importedViewTables);

		new ShellMenu("File", "Close All").select();
		
		new ModelExplorer().openModelEditor(IMPORT_PROJECT_NAME, VIEW_MODEL + ".xmi");
		RelationalModelEditor editor = new RelationalModelEditor(VIEW_MODEL + ".xmi");
	    TransformationEditor transformationEditor =  editor.openTransformationDiagram(ModelEditorItemMatcher.TABLE, "smalla");

		collector.checkThat("Wrong transformation for smalla", transformationEditor.getTransformation().replaceAll("\\s+", " "),
				new RegexMatcher("SELECT \\* FROM postgresql92Model\\.smalla"));

		editor.returnToPackageDiagram();
		transformationEditor =  editor.openTransformationDiagram(ModelEditorItemMatcher.TABLE, "smallb");

		collector.checkThat("Wrong transformation for smallb", transformationEditor.getTransformation().replaceAll("\\s+", " "),
				new RegexMatcher("SELECT \\* FROM postgresql92Model\\.smallb"));

		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported view model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(VIEW_MODEL + ".xmi")), empty());
		collector.checkThat("Errors in imported source model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(BQT_MODEL_NAME + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(staticVdbName + ".vdb")),
				empty());
	}

	@Test
	public void importWithRestProcedure() {
		String staticVdbName = "RestProcedureVdb";
		String dynamicVdbName = staticVdbName + "-vdb.xml";

		importDynamicVdb(IMPORT_PROJECT_NAME, dynamicVdbName);
		createArchiveVdb(IMPORT_PROJECT_NAME, dynamicVdbName);

		new ModelExplorer().openModelEditor(IMPORT_PROJECT_NAME, PROCEDURE_MODEL + ".xmi");
		RelationalModelEditor editor = new RelationalModelEditor(PROCEDURE_MODEL + ".xmi");
		TableEditor tableEditor = editor.openTableEditor();
		
		tableEditor.openTab(TableEditor.Tabs.PROCEDURES);
		collector.checkThat("REST method not set on procedure", tableEditor.getCellText(1, "testProc", "REST:Rest Method"), is("GET"));
		collector.checkThat("REST URI not set on procedure", tableEditor.getCellText(1, "testProc", "REST:URI"), is("test/{p1}"));
		
		tableEditor.openTab(TableEditor.Tabs.COLUMNS);
		collector.checkThat("wrong column name", tableEditor.getCellText(0, "testProc", "Name"), is("xml_out"));
		collector.checkThat("wrong column name", tableEditor.getCellText(0, "testProc", "Datatype"), is("XMLLiteral : xs:string"));

		tableEditor.openTab(TableEditor.Tabs.PROCEDURE_PARAMETERS);
		collector.checkThat("wrong parameter name", tableEditor.getCellText(0, "testProc", "Name"), is("p1"));
		collector.checkThat("wrong parameter name", tableEditor.getCellText(0, "testProc", "Native Type"), is("STRING"));
		collector.checkThat("wrong parameter name", tableEditor.getCellText(0, "testProc", "Datatype"), is("string"));
		
		tableEditor.close();

	    TransformationEditor transformationEditor =  editor.openTransformationDiagram(ModelEditorItemMatcher.PROCEDURE, "testProc");
		
		collector.checkThat("Wrong transformation for testProc", transformationEditor.getTransformation().replaceAll("\\s+"," "),
				is("BEGIN SELECT "
						+ "XMLELEMENT(NAME test, XMLFOREST(ProcedureModel.testProc.p1 AS elem1, 'elem2' AS elem2)) "
						+ "AS xml_out; END"));

		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported view model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(PROCEDURE_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(staticVdbName + ".vdb")),
				empty());
	}

	@Test
	public void importAndUpdateModels() {
		String staticVdbName = "SourceAndViewSmallerVdb";
		String dynamicVdbName = staticVdbName + "-vdb.xml";

		importDynamicVdb(PROJECT_NAME, dynamicVdbName);
		createArchiveVdb(PROJECT_NAME, dynamicVdbName);

		new ModelExplorer().openModelEditor(PROJECT_NAME, VIEW_MODEL + ".xmi");
		TableEditor tableEditor = new RelationalModelEditor(VIEW_MODEL + ".xmi").openTableEditor();
		
		tableEditor.openTab(TableEditor.Tabs.COLUMNS);		
		collector.checkThat("timestampvalue removed from smalla", tableEditor.getRow("Name", "timestampvalue"), notNullValue());
		collector.checkThat("doublenum not removed from smalla", tableEditor.getRow("Name", "doublenum"), nullValue());
		collector.checkThat("c1 not created in newViewA", tableEditor.getRow("Name", "c1"), notNullValue());
		
		tableEditor.openTab(TableEditor.Tabs.BASE_TABLES);
		collector.checkThat("smallb not removed",  tableEditor.getRow("Name", "smallb"), nullValue());
		
		new ModelExplorer().openModelEditor(PROJECT_NAME, BQT_MODEL_NAME + ".xmi");
		tableEditor = new RelationalModelEditor(BQT_MODEL_NAME + ".xmi").openTableEditor();
		
		tableEditor.openTab(TableEditor.Tabs.COLUMNS);
		collector.checkThat("timestampvalue removed from smalla", tableEditor.getRow("Name", "timestampvalue"), notNullValue());
		collector.checkThat("doublenum not removed from smalla", tableEditor.getRow("Name", "doublenum"), nullValue());
		List<TableItem> smallxRows = new ArrayList<>();
		for (TableItem row : tableEditor.getRows()){
			if (row.getText(0).equals("smallx")){
				smallxRows.add(row);
			}
		}
		collector.checkThat("wrong number of columns created in smallx", smallxRows.size(), is(16));
		
		tableEditor.openTab(TableEditor.Tabs.BASE_TABLES);
		collector.checkThat("smallb not removed",tableEditor.getRow("Name", "smallb"), nullValue());
		collector.checkThat("smallx not created", tableEditor.getRow("Name", "smallx"), notNullValue());

		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported view model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(VIEW_MODEL + ".xmi")), empty());
		collector.checkThat("Errors in imported source model",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(BQT_MODEL_NAME + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new ProblemsResourceMatcher(staticVdbName + ".vdb")),
				empty());
	}

	private void checkTablesSame(List<ModelTable> expected, List<ModelTable> actual) {
		for (ModelTable origTable : expected) {
			boolean tableFound = false;
			for (ModelTable newTable : actual) {
				if (origTable.getName().equals(newTable.getName())) {
					tableFound = true;
					for (ModelColumn origColumn : origTable.getColumns()) {
						boolean columnFound = false;
						for (ModelColumn newColumn : newTable.getColumns()) {
							if (origColumn.getName().equals(newColumn.getName())) {
								columnFound = true;

								collector.checkThat("Wrong datatype for column " + newColumn.getName(),
										newColumn.getDatatype(), is(origColumn.getDatatype()));
								collector.checkThat("Wrong name in source for column " + newColumn.getName(),
										newColumn.getNameInSource(), is(origColumn.getNameInSource()));
								collector.checkThat("Wrong native type for column " + newColumn.getName(),
										newColumn.getNativeType(), is(origColumn.getNativeType()));

								break;
							}
						}
						collector.checkThat(
								"Column " + origTable.getName() + "." + origColumn.getName() + " not created",
								columnFound, is(true));
					}
					break;
				}
			}
			collector.checkThat("Table " + origTable.getName() + " not created", tableFound, is(true));
		}
	}

	private void importDynamicVdb(String projectName, String dynamicVdbName) {	
		ImportFromFileSystemWizard.openWizard()
				.setPath("resources/dynamic-vdb")
				.setFolder(projectName)
				.selectFile(dynamicVdbName)
				.setCreteTopLevelFolder(false)
				.finish();	
	}

	private void createArchiveVdb(String projectName, String dynamicVdbName) {
		GenerateVdbArchiveDialog wizard = new ModelExplorer().generateVdbArchive(projectName, dynamicVdbName);
		wizard.next()
				.generate()
				.finish();
	}

	private void createVdb(String projectName, String vdbName, String... models) {
		
		VdbWizard.openVdbWizard()
				.setName(vdbName)
				.setLocation(projectName);
		for(String model : models){
			VdbWizard.getInstance().addModel(projectName, model);
		}
		VdbWizard.getInstance().finish();
		
	}

	private String createDynamicVdb(String projectName, String staticVdbName, String dynamicVdbName) {
		GenerateDynamicVdbDialog wizard = new ModelExplorer().generateDynamicVDB(projectName, staticVdbName);
		wizard.setName(dynamicVdbName)
				.next()
				.generate();
		String contents = wizard.getContents();
		wizard.finish();
		return contents;
	}

	private void checkXpathPermission(String xml, String role, String resource, String permission, String expected) {
		try {
			String path = String.format("/vdb/data-role[@name='%s']/permission[resource-name='%s']/%s", role, resource,
					permission);
			String perm = getXPath(xml, path);
			collector.checkThat("wrong value for " + path, perm, is(expected));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void checkMetadata(String ddl, List<ModelTable> tables, String createStatement) {
		for (ModelTable t : tables) {
			collector.checkThat("table " + t.getName() + " not in ddl", ddl,
					new StringContains(createStatement + t.getName()));
			for (ModelColumn c : t.getColumns()) {
				String columnDef = c.getName() + ' ' + c.getDatatype();
				collector.checkThat("column " + c.getName() + " not in ddl", ddl, new StringContains(columnDef));
			}
		}
	}

	private void checkDeployOk(String fileName, String dynamicVdbName) {
		new ModelExplorer().deployVdb(PROJECT_NAME, fileName + "-vdb.xml");
		new ServersViewExt().refreshServer(teiidServer.getName());
		String vdbStatusTooltip = new ServersViewExt().getVdbStatus(teiidServer.getName(), dynamicVdbName);
		collector.checkThat("vdb is not active", vdbStatusTooltip, containsString("State:  ACTIVE"));
	}

	private void checkPreview(final String vdbName, final String query) {
		collector.checkSucceeds(new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				TeiidJDBCHelper helper = new TeiidJDBCHelper(teiidServer, vdbName);
				ResultSet rs = helper.executeQueryWithResultSet(query);
				rs.close();
				helper.closeConnection();
				return null;
			}
		});
	}

	private void checkContentsSame(String staticVdbName, String dynamicVdbContent) {
		File projectFile = new ModelExplorer().getModelProject(PROJECT_NAME).getFile();
		String vdbPath = new File(projectFile, staticVdbName + "-vdb.xml").getAbsolutePath();
		String fileContents = new ResourceFileHelper().loadFileAsString(vdbPath);
		collector.checkThat("Created VDB different from VDB contents in wizard", fileContents,
				equalToIgnoringWhiteSpace(dynamicVdbContent.replace("\n", "")));
	}

	private List<ModelTable> getTables(String projectName, String modelName) {
		new ModelExplorer().openModelEditor(projectName, modelName + ".xmi");
		RelationalModelEditor editor = new RelationalModelEditor(modelName + ".xmi");
	    TableEditor tableEditor = editor.openTableEditor();
	    tableEditor.openTab(TableEditor.Tabs.BASE_TABLES);
	    List<ModelTable> tables = new ArrayList<>();
	    for (TableItem it : tableEditor.getRows()) {
	    	tables.add(new ModelTable(it));
		}
	    tableEditor.openTab(TableEditor.Tabs.COLUMNS);
	    for (ModelTable table : tables) {
	    	List<ModelColumn> columns = new ArrayList<>();
	    	for (TableItem it : tableEditor.getRows()) {
				ModelColumn column = new ModelColumn(it);
				if (column.getLocation().equalsIgnoreCase(table.getName())) {
					columns.add(column);
				}
			}
	    	table.setColumns(columns);
		}
		return tables;
	}

	private String getXPath(String xml, String path) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			return xpath.evaluate(path, new InputSource(new StringReader(xml)));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("error evaluating xpath");
		}
		return null;
	}
}
