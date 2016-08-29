package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.jface.viewers.CellEditor;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ccombo.DefaultCCombo;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.connection.ConnectionProfileConstants;
import org.jboss.tools.teiid.reddeer.connection.TeiidJDBCHelper;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement;
import org.jboss.tools.teiid.reddeer.requirement.TeiidServerRequirement.TeiidServer;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.VdbWizard;
import org.jboss.tools.teiid.reddeer.wizard.exports.DDLTeiidExportWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.DDLTeiidImportWizard;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author mkralik
 */

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)

@TeiidServer(state = ServerReqState.RUNNING, connectionProfiles={
		ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER,
})

public class ImportDDLtest {
	@InjectRequirement
	private static TeiidServerRequirement teiidServer;

	private static final String PROJECT_NAME = "DDLimport";
	private static final String NAME_SOURCE_MODEL = "sourceModel";
	private static final String NAME_VIEW_ORIGINAL_MODEL = "viewModelOriginal";
	private static final String NAME_VIEW_MODEL = "viewModel";
	private static final String NAME_VDB = "ddlVDB";
	private static final String PATH_TO_DDL = new File("resources/ddl/viewModel.ddl").getAbsolutePath();
	private static final String PATH_TO_ORIGINAL_DDL = new File("resources/ddl/viewModelOriginal.ddl").getAbsolutePath();

	@BeforeClass
	public static void openPerspective() {
		new ModelExplorer().importProject(PROJECT_NAME);
    	new ModelExplorer().changeConnectionProfile(ConnectionProfileConstants.ORACLE_11G_PARTS_SUPPLIER, PROJECT_NAME, NAME_SOURCE_MODEL);
	}	

	@Test
	public void importDDL(){
		//ddl importer 
		DDLTeiidImportWizard importWizard = new DDLTeiidImportWizard();
		importWizard.open();
		importWizard.setPath(PATH_TO_DDL)
					.setFolder(PROJECT_NAME)
					.setName(NAME_VIEW_MODEL)
					.setModelType(DDLTeiidImportWizard.View_Type)
					.generateValidDefaultSQL(true)
					.next();
		importWizard.finish();
		
		new ModelExplorer().getModelProject(PROJECT_NAME).open();
		new DefaultTreeItem(PROJECT_NAME,NAME_VIEW_MODEL + ".xmi","tempTable").doubleClick();
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultCTabItem("Table Editor").activate();
		new DefaultTabItem("Base Tables").activate();
		List<TableItem> items = new DefaultTable().getItems();
		
		//check if table was set to temp
		assertTrue("true".equals(items.get(0).getText(10)));
		
		//set supports update
		TableItem row = items.get(0);
		if(row.getText(5).equals("false")){
			row.doubleClick(5);
			new DefaultCCombo(new CellEditor(row)).setSelection("true");
			row.click();
			if (new ShellWithTextIsActive("Table 'Supports Update' Property Changed").test()){
				new PushButton("Yes").click();
			}
			new ShellMenu("File", "Save All").select();
		}
		
		VdbWizard.openVdbWizard()
				.setLocation(PROJECT_NAME)
				.setName(NAME_VDB)
				.addModel(PROJECT_NAME,NAME_VIEW_MODEL + ".xmi")
				.finish();

		new ModelExplorer().deployVdb(PROJECT_NAME, NAME_VDB);
		
		TeiidJDBCHelper jdbchelper = new TeiidJDBCHelper(teiidServer, NAME_VDB);
		try {
			List<ResultSet> resultSets = jdbchelper.executeMultiQuery(
					"select * from \"viewModel\".\"tempTable\";",
					"INSERT INTO \"viewModel\".\"tempTable\" VALUES ('testID1',10,true);",
					"INSERT INTO \"viewModel\".\"tempTable\" VALUES ('testID2','10',false);",
					"select * from \"viewModel\".\"tempTable\";",
					"select * from ( exec \"viewModel\".\"getProduct\"('PRD01095') ) AS X_X ;",
					"select * from ( exec \"viewModel\".\"selfProc\"('hello') ) AS X_X ;"
					);
			//first select is empty
			assertFalse(resultSets.get(0).isBeforeFirst()); 
			//test global temp table
			ResultSet rs = resultSets.get(1);
 			rs.next();
 			assertEquals("testID1", rs.getString(1));
 			assertEquals("true", rs.getString(3));
 			rs.next();
 			assertEquals("testID2", rs.getString(1));
 			assertEquals("false", rs.getString(3));
 			//test procedures
 			rs = resultSets.get(2);
 			rs.next();
 			assertEquals("Intel Corporation3", rs.getString(1));
 			rs = resultSets.get(3);
 			rs.next();
 			assertEquals("hello", rs.getString(1));
 			
 		} catch (SQLException e) {
 			fail(e.getMessage());
 		}
		jdbchelper.closeConnection();
	}
	
	@Test
	public void exportOriginalDDL(){
		//TEIIDDES-2827
		new ModelExplorer().getModelProject(PROJECT_NAME).open();
		new DefaultTreeItem(PROJECT_NAME,NAME_VIEW_ORIGINAL_MODEL + ".xmi").select();

		DDLTeiidExportWizard exportDDL = new DDLTeiidExportWizard();		
		exportDDL.open();
		exportDDL.setLocation(PROJECT_NAME,NAME_VIEW_ORIGINAL_MODEL + ".xmi")
	     		 .next();
		exportDDL.exportToWorkspace("originalDDL", PROJECT_NAME)
			     .finish();
		
		new ModelExplorer().getModelProject(PROJECT_NAME).open();
		new DefaultTreeItem(PROJECT_NAME,"originalDDL").doubleClick();
		
		String generetedText = new DefaultStyledText().getText();
		generetedText=generetedText.replaceAll("\r|\n|\t", "");
		
		String expectedText = null;
		try {
			expectedText = convertFileToString(new File(PATH_TO_ORIGINAL_DDL));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		expectedText=expectedText.replaceAll("\r|\n|\t", "");
		assertTrue(expectedText.equals(generetedText));
	}
	
	private String convertFileToString(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append('\n');
		}
		reader.close();
		return stringBuilder.toString();
	}
}
