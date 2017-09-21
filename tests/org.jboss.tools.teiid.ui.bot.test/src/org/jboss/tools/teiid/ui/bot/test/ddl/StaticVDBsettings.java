package org.jboss.tools.teiid.ui.bot.test.ddl;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

import java.util.List;

import org.eclipse.reddeer.eclipse.ui.markers.matcher.MarkerResourceMatcher;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.api.TableItem;
import org.jboss.tools.common.reddeer.JiraClient;
import org.jboss.tools.teiid.reddeer.DdlHelper;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.editor.VdbEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)

@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles={
		ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, ConnectionProfileConstants.POSTGRESQL_92_DVQE
})
public class StaticVDBsettings {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;
	
	@Rule
	public ErrorCollector collector = new ErrorCollector();
	public DdlHelper ddlHelper = null;
	
	private static final String PROJECT_NAME = "StaticVDBsettings";
	private static final String NAME_SOURCE_MODEL = "sourceModel";
	private static final String NAME_VDB = "StaticVDB";
	private static final String NAME_ORIGINAL_DYNAMIC_VDB = NAME_VDB + "-vdb.xml";
	
	private static final String NAME_GENERATED_DYNAMIC_VDB = "StaticVDBgenerated-vdb.xml";
	
	private static final String WORK_PROJECT_NAME = "workProject" ;
	
	@Before
	public void before() {
		ModelExplorer explorer = new ModelExplorer();
		explorer.deleteAllProjectsSafely();

		ddlHelper = new DdlHelper(collector);
		
		explorer.importProject("DDLtests/"+PROJECT_NAME);
		explorer.changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_SOURCE_MODEL);
		/* data source is needed when exported VDB will be tested to deploy on the server */
		explorer.changeConnectionProfile(ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, "secondSource");
		explorer.createDataSource("Use Connection Profile Info",ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, "secondSource");
		explorer.createDataSource("Use Connection Profile Info",ConnectionProfileConstants.SQL_SERVER_2008_PARTS_SUPPLIER, PROJECT_NAME, NAME_SOURCE_MODEL);
		explorer.createProject(WORK_PROJECT_NAME);
	}
	
	@After
	public void after(){
		new ModelExplorer().deleteAllProjectsSafely();
	}

	@Test
	public void importVdbTest(){
		ddlHelper.importVdb(PROJECT_NAME, NAME_ORIGINAL_DYNAMIC_VDB, WORK_PROJECT_NAME);
		
		checkVDB();
		
		/*all models must be opened before synchronize VDB*/
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME,NAME_SOURCE_MODEL+".xmi");
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME,NAME_VDB+".vdb");
		VdbEditor staticVdb = VdbEditor.getInstance(NAME_VDB);
		staticVdb.synchronizeAll();
		staticVdb.saveAndClose();
		/*test deploy generated VDB from dynamic VDB*/
		String status = ddlHelper.deploy(WORK_PROJECT_NAME, NAME_VDB, teiidServer);	
		collector.checkThat("vdb is not active", status, is("ACTIVE"));
	}
	
	
	private void checkVDB(){
		new ModelExplorer().openModelEditor(WORK_PROJECT_NAME, NAME_VDB + ".vdb");		
		VdbEditor vdbeditor= new VdbEditor(NAME_VDB + ".vdb");		
		
		collector.checkThat("Bad VDB version", vdbeditor.getVersion(), is("123"));
		collector.checkThat("Missing VDB description", vdbeditor.getProjectDescription(), is("This is VDB description"));		
		
		String[] list = vdbeditor.getListOfAllowedLanguage();	
		collector.checkThat("Missing allowed language", list[0], is("javascript"));
		collector.checkThat("Multi-source checkbox is unchecked", vdbeditor.isCheckedMultiSource(NAME_SOURCE_MODEL), is(true));
		if (new JiraClient().isIssueClosed("TEIIDDES-2990")){
			collector.checkThat("Add column checkbox is unchecked", vdbeditor.isCheckedAddColumn(NAME_SOURCE_MODEL), is(true));
		}
		List<TableItem> sources=vdbeditor.getListOfSources(NAME_SOURCE_MODEL);
		collector.checkThat("Source name for first model is bad set", sources.get(0).getText(0), is("sourceModel"));
		collector.checkThat("Translator name for first model is bad set", sources.get(0).getText(1), is("oracle"));
		collector.checkThat("JNDI name for first model is bad set", sources.get(0).getText(2), is("java:/sourceModel"));
		
		collector.checkThat("Source name for second model is bad set", sources.get(1).getText(0), is("second"));
		collector.checkThat("Translator name for second model is bad set", sources.get(1).getText(1), is("h2"));
		collector.checkThat("JNDI name for second model is bad set", sources.get(1).getText(2), is("java:/secondSource"));

		collector.checkThat("Missing Source model description", vdbeditor.getModelDescription(NAME_SOURCE_MODEL), is("This is Source Model description"));		
		
		List<TableItem> properties = vdbeditor.getListOfModelProperties(NAME_SOURCE_MODEL);
		collector.checkThat("Missing or bad set source model property name", properties.get(0).getText(0), is("SourceModelProperty"));
		collector.checkThat("Missing or bad set source model property value", properties.get(0).getText(1), is("10"));
		
		vdbeditor.close();
		
		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported view model",
				problemsView.getProblems(ProblemType.ERROR, new MarkerResourceMatcher(NAME_SOURCE_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new MarkerResourceMatcher(NAME_VDB + ".vdb")),
				empty());
	}
	
	@Test
	public void exportVdbTest(){
		String overrideName = "sqlOverride";	
		ddlHelper.createStaticVdb(NAME_VDB, PROJECT_NAME, NAME_SOURCE_MODEL);		

		new ModelExplorer().openModelEditor(PROJECT_NAME, NAME_VDB + ".vdb");
		VdbEditor vdbEditor = VdbEditor.getInstance(NAME_VDB);
		vdbEditor.setDescription("This is VDB description");
		vdbEditor.setVersion(123);
		vdbEditor.setAllowedLanguage("javascript");
		vdbEditor.addMultiSource(NAME_SOURCE_MODEL, "second", "sqlserver", "java:/secondSource");
		vdbEditor.checkAddColumnCheckbox(NAME_SOURCE_MODEL);
		vdbEditor.addUserDefinedProperty("VDBproperty", "propertyValue");
		vdbEditor.addModelDescription(NAME_SOURCE_MODEL, "This is Source Model description");
		vdbEditor.addModelProperty(NAME_SOURCE_MODEL, "SourceModelProperty", "10");
		
		vdbEditor.addTranslatorOverride(overrideName, "sqlserver");
		vdbEditor.addTranslatorOverrideProperty(overrideName, "CustomProperty", "customValue");
		vdbEditor.save();
		
		//create dynamic VDB
		String contentFile = ddlHelper.createDynamicVdb(PROJECT_NAME, NAME_VDB, NAME_GENERATED_DYNAMIC_VDB);
		checkExportedFile(contentFile);

		/*test deploy generated dynamic VDB from static VDB*/
		String status = ddlHelper.deploy(PROJECT_NAME, NAME_GENERATED_DYNAMIC_VDB, teiidServer); 		
		collector.checkThat("vdb is not active", status, is("ACTIVE"));
		
		ProblemsView problemsView = new ProblemsView();
		collector.checkThat("Errors in imported source model",
				problemsView.getProblems(ProblemType.ERROR, new MarkerResourceMatcher(NAME_SOURCE_MODEL + ".xmi")),
				empty());
		collector.checkThat("Errors in imported VDB",
				problemsView.getProblems(ProblemType.ERROR, new MarkerResourceMatcher(NAME_VDB + ".vdb")),
				empty());	
	}
	
	private void checkExportedFile(String contentFile){
		collector.checkThat("user defined property not in vdb",
				ddlHelper.getXPath(contentFile, "/vdb/property[@name=\"VDBproperty\"]/@value"), is("propertyValue"));

		// check overriden translator properties
		collector.checkThat("wrong translator override type",
				ddlHelper.getXPath(contentFile, "/vdb/translator[@type='sqlserver']/@name"),
					is("sqlOverride"));
		
		collector.checkThat("custom translator override property not created",
				ddlHelper.getXPath(contentFile,	"/vdb/translator[@type='sqlserver']/property[@name='CustomProperty']/@value"),
				is("customValue"));
		
		//check vdb settings
		collector.checkThat("database version is bad",
				ddlHelper.getXPath(contentFile,"/vdb/@version"),
					is("123"));
		
		collector.checkThat("missing VDB description",
				ddlHelper.getXPath(contentFile,"/vdb/description"),
					is("This is VDB description"));
		
		collector.checkThat("missing allowed languages",
				ddlHelper.getXPath(contentFile,"/vdb/property[@name='allowed-languages']/@value"),
					is("javascript"));

		//check source model settings
		collector.checkThat("missing model description",
				ddlHelper.getXPath(contentFile,"/vdb/model[@name='sourceModel']/description"),
					is("This is Source Model description"));
		collector.checkThat("missing multisource checkbox",
				ddlHelper.getXPath(contentFile,"/vdb/model[@name='sourceModel']/property[@name='multisource']/@value"),
					is("true"));
		collector.checkThat("missing multisource addcolumn checkbox",
				ddlHelper.getXPath(contentFile,"/vdb/model[@name='sourceModel']/property[@name='multisource.addColumn']/@value"),
					is("true"));
		collector.checkThat("missing source model property",
				ddlHelper.getXPath(contentFile,"/vdb/model[@name='sourceModel']/property[@name='SourceModelProperty']/@value"),
					is("10"));
		collector.checkThat("wrong default source model",
				ddlHelper.getXPath(contentFile,"/vdb/model[@name='sourceModel']/source[@connection-jndi-name='sourceModel']/@name"),
					is(NAME_SOURCE_MODEL));
		collector.checkThat("wrong multi source model",
				ddlHelper.getXPath(contentFile,"/vdb/model[@name='sourceModel']/source[@connection-jndi-name='java:/secondSource']/@name"),
					is("second"));
}
}

