package org.jboss.tools.teiid.ui.bot.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.manager.ImportMetadataManager;
import org.jboss.tools.teiid.reddeer.manager.ModelExplorerManager;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel;
import org.jboss.tools.teiid.reddeer.wizard.CreateMetadataModel.ModelBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class BasicTest {

	private static final String PROJECT = "XmlView";
	private static TeiidBot teiidBot = new TeiidBot();
	private static final String MODEL = "model";
	private static final String XSD = "UpdatesSchema.xsd";
	private static int i = 0;

	@BeforeClass
	public static void prepare() {
		new ModelExplorerManager().createProject(PROJECT);
		Properties props = new Properties();
		props.setProperty("local", "true");
		props.setProperty("rootPath", teiidBot.toAbsolutePath("resources/xsd"));
		props.setProperty("schemas", XSD);
		props.setProperty("destination", PROJECT);
		new ImportMetadataManager().importXMLSchema(PROJECT, props);
	}

	// BooksInput, BooksUpdate, ISBNInput : mns:ISBNType, putResults :
	// mns:putResultsType
	private static String[] virtDocs = new String[] { "BooksInput",
			"BooksUpdate", "ISBNInput : mns:ISBNType",
			"putResults : mns:putResultsType" };

	@Test
	public void test() {

		prepareModel(++i, new String[] { "BooksInput" });
		output(i, "BooksInput");
	}

	@Test
	public void test2() {

		String elem = "BooksUpdate";
		prepareModel(++i, new String[] { elem });
		output(i, elem);
	}

	@Test
	public void test3() {

		String elem = "ISBNInput : mns:ISBNType";
		prepareModel(++i, new String[] { elem });
		output(i, elem);
	}

	@Test
	public void test4() {

		String elem = "putResults : mns:putResultsType";
		prepareModel(++i, new String[] { elem });
		output(i, elem);
	}

	@Test
	public void test5() {

		String[] elems = new String[] { virtDocs[0], virtDocs[1] };
		prepareModel(++i, elems);
		for (String s : elems) {

			output(i, s);
		}
	}

	@Test
	public void test6() {

		String[] elems = new String[] { virtDocs[1], virtDocs[2] };
		prepareModel(++i, elems);
		for (String s : elems) {

			output(i, s);
		}
	}

	@Test
	public void test7() {

		String[] elems = new String[] { virtDocs[0], virtDocs[2] };
		prepareModel(++i, elems);
		for (String s : elems) {

			output(i, s);
		}
	}

	@Test
	public void test8() {

		String[] elems = new String[] { virtDocs[0], virtDocs[3] };
		prepareModel(++i, elems);
		for (String s : elems) {

			output(i, s);
		}
	}

	@Test
	public void test9() {

		String[] elems = new String[] { virtDocs[1], virtDocs[2] };
		prepareModel(++i, elems);
		for (String s : elems) {

			output(i, s);
		}
	}

	@Test
	public void test10() {

		String[] elems = new String[] { virtDocs[1], virtDocs[2], virtDocs[3] };
		prepareModel(++i, elems);
		for (String s : elems) {

			output(i, s);
		}
	}

	private void prepareModel(int i, String[] virtDocs) {

		CreateMetadataModel createModel = new CreateMetadataModel();
		createModel.setLocation(PROJECT);
		createModel.setName(MODEL + i);
		createModel.setClass(CreateMetadataModel.ModelClass.XML);
		createModel.setType(CreateMetadataModel.ModelType.VIEW);
		createModel.setModelBuilder(ModelBuilder.BUILD_FROM_XML_SCHEMA);
		createModel.setPathToXmlSchema(new String[] { PROJECT, XSD });
		createModel.setVirtualDocuments(virtDocs);
		createModel.execute();
	}

	private void output(int j, String elem) {

		new ModelExplorer().open();
		int s = 0;

		// BooksInput, BooksUpdate, ISBNInput : mns:ISBNType, putResults :
		// mns:putResultsType
		String el = "";
		String el2 = "";

		if (elem.equals(virtDocs[0])) {
			el = "BooksInputDocument";
			el2 = elem;
		}
		if (elem.equals(virtDocs[1])) {
			el = "BooksUpdateDocument";
			el2 = elem;
		}
		if (elem.equals(virtDocs[2])) {
			el = "ISBNInputDocument";
			el2 = "ISBNInput";
		}
		if (elem.equals(virtDocs[3])) {
			el = "putResultsDocument";
			el2 = "putResults";
		}

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter("/home/tsedmik/tmp/myfile.txt", true)));
			out.println(MODEL + j + ", " + elem);
			s = new DefaultTreeItem(PROJECT, MODEL + j + ".xmi", el, el2)
					.getItems().size();
			for (int i = 0; i < s; i++) {
				String data = new DefaultTreeItem(PROJECT, MODEL + j + ".xmi",
						el, el2).getItems().get(i).getText();
				out.println(data);
			}
			out.close();
		} catch (IOException ex) {

		}
	}
}
