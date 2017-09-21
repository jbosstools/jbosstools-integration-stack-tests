package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.ResourceFileHelper;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.dialog.InputSetEditorDialog;
import org.jboss.tools.teiid.reddeer.dialog.XmlDocumentBuilderDialog;
import org.jboss.tools.teiid.reddeer.editor.TransformationEditor;
import org.jboss.tools.teiid.reddeer.editor.XmlModelEditor;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.view.ProblemsViewEx;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.newWizard.VdbWizard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author skaleta
 */
@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
@TeiidServer(state = ServerRequirementState.RUNNING, connectionProfiles = {ConnectionProfileConstants.ORACLE_11G_BOOKS})
public class XmlStagingTableTest {
	private static final String PROJECT_NAME = "XmlStagingTableProject";
	private static final String VIEW_MODEL = "SchemaModel.xmi";
	private static final String VDB_NAME = "XmlStagingVdb";
	
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private ModelExplorer modelExplorer;
	private TeiidJDBCHelper jdbcHelper;
	private ResourceFileHelper fileHelper;

	@Before
	public void importProject() {
		new WorkbenchShell().maximize();
		modelExplorer = new ModelExplorer();
		modelExplorer.importProject(PROJECT_NAME);
		modelExplorer.refreshProject(PROJECT_NAME);
		modelExplorer.changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_BOOKS, PROJECT_NAME, "sources", "Books.xmi");
		jdbcHelper = new TeiidJDBCHelper(teiidServer, VDB_NAME);
		fileHelper = new ResourceFileHelper();
	}

	@Test
	public void test() throws SQLException {
		// 1. Create an XML document model using a schema
		MetadataModelWizard.openWizard()
				.setLocation(PROJECT_NAME, "views")
				.setModelName(VIEW_MODEL.substring(0, 11))
				.selectModelClass(MetadataModelWizard.ModelClass.XML)
				.selectModelType(MetadataModelWizard.ModelType.VIEW)
				.selectModelBuilder(MetadataModelWizard.ModelBuilder.BUILD_FROM_XML_SCHEMA)
				.nextPage()
				.selectXMLSchemaFile(PROJECT_NAME, "schemas", "PublisherSchema.xsd")
				.addElement("ResultSet")
				.finish();

		// 2. Model XML document without staging table
		XmlModelEditor editor = new XmlModelEditor(VIEW_MODEL);
		
		modelExplorer.renameModelItem("NoStagingDocument", PROJECT_NAME, "views", "SchemaModel.xmi", "ResultSetDocument");
		
		editor.openMappingClass("publisher");
		TransformationEditor pubTransfEditor = editor.openTransformationEditor();
		pubTransfEditor.insertAndValidateSql(fileHelper.getSql("XmlStagingTableTest/Publisher"));
		pubTransfEditor.close();
		editor.returnToParentDiagram();
		
		editor.openMappingClass("book");
		InputSetEditorDialog inputSetEditor = editor.openInputSetEditor();
		inputSetEditor.addNewInputParam("publisher","publisherId : double");
		inputSetEditor.finish();
		TransformationEditor bookTransfEditor = editor.openTransformationEditor();
		bookTransfEditor.insertAndValidateSql(fileHelper.getSql("XmlStagingTableTest/Book"));
		bookTransfEditor.close();
		editor.returnToParentDiagram();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();
		
		ProblemsViewEx.checkErrors();
		
		// 3. Model XML document with staging table
		modelExplorer.addChildToModelItem(ModelExplorer.ChildType.XML_DOCUMENT, PROJECT_NAME, "views", VIEW_MODEL);
		XmlDocumentBuilderDialog xmlDocumentBuilder = new XmlDocumentBuilderDialog();
		xmlDocumentBuilder.setSchema(PROJECT_NAME,"schemas","PublisherSchema.xsd")
				.addElement("ResultSet");
		xmlDocumentBuilder.finish();
		
		modelExplorer.renameModelItem("StagingDocument", PROJECT_NAME, "views", VIEW_MODEL, "ResultSetDocument");
		
		editor.createStagingTable("ResultSet");
		editor.openStagingTable("ST_ResultSet");
		TransformationEditor stTransfEditor = editor.openTransformationEditor();
		stTransfEditor.insertAndValidateSql(fileHelper.getSql("XmlStagingTableTest/StagingTable"));
		stTransfEditor.close();
		editor.returnToParentDiagram();

		editor.openMappingClass("publisher");		
		pubTransfEditor = editor.openTransformationEditor();
		pubTransfEditor.insertAndValidateSql(fileHelper.getSql("XmlStagingTableTest/StPublisher"));
		pubTransfEditor.close();
		editor.returnToParentDiagram();
 		
		editor.openMappingClass("book");
		inputSetEditor = editor.openInputSetEditor();
		inputSetEditor.addNewInputParam("publisher","publisherId : double");
		inputSetEditor.finish();
		bookTransfEditor = editor.openTransformationEditor();
		bookTransfEditor.insertAndValidateSql(fileHelper.getSql("XmlStagingTableTest/StBook"));
		bookTransfEditor.close();
		editor.returnToParentDiagram();
		
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.save();

		ProblemsViewEx.checkErrors();

		// 4.Create a VDB and deploy
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(VDB_NAME)
				.addModel(PROJECT_NAME, "views", VIEW_MODEL)
				.finish();
		modelExplorer.deployVdb(PROJECT_NAME, VDB_NAME);
		
		// 5. Test created models		
		String outputWithSt = jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM StagingDocument ORDER BY publisherId, isbn");
		String outputWithoutSt = jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM NoStagingDocument ORDER BY publisherId, isbn");
		assertEquals(outputWithSt, outputWithoutSt);

		outputWithSt = jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM StagingDocument WHERE title LIKE '%Software%' ORDER BY publisherId, isbn"); 
		outputWithoutSt = jdbcHelper.executeQueryWithXmlStringResult("SELECT * FROM NoStagingDocument WHERE title LIKE '%Software%' ORDER BY publisherId, isbn");
		assertEquals(outputWithSt, outputWithoutSt);
	}
}
