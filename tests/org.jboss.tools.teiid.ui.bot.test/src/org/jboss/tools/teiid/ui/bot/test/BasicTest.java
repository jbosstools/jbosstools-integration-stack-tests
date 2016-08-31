package org.jboss.tools.teiid.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.ModelClass;
import org.jboss.tools.teiid.reddeer.ModelType;
import org.jboss.tools.teiid.reddeer.perspective.TeiidPerspective;
import org.jboss.tools.teiid.reddeer.view.ModelExplorer;
import org.jboss.tools.teiid.reddeer.wizard.MetadataModelWizard;
import org.jboss.tools.teiid.reddeer.wizard.imports.XMLSchemaImportWizard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RedDeerSuite.class)
@OpenPerspective(TeiidPerspective.class)
public class BasicTest {

	private static final String PROJECT = "XmlView";
	private static final String MODEL = "model";
	private static final String XSD = "UpdatesSchema.xsd";
	private static int i = 0;

	@BeforeClass
	public static void prepare() {
		if (new ShellMenu("Project", "Build Automatically").isSelected()) {
			new ShellMenu("Project", "Build Automatically").select();
		}
		new ModelExplorer().createProject(PROJECT);
		
		XMLSchemaImportWizard wizard = new XMLSchemaImportWizard();
		wizard.setLocal(true);
		wizard.setRootPath(new File("resources/xsd").getAbsolutePath());
		wizard.setSchemas(new String[] { XSD });
		wizard.setDestination(PROJECT);
		wizard.execute();
	}

	@AfterClass
	public static void pauseAfter() {
		System.out.println("pause");
	}

	// BooksInput, BooksUpdate, ISBNInput : mns:ISBNType, putResults :
	// mns:putResultsType
	private static String[] virtDocs = new String[] {
		"BooksInput",
		"BooksUpdate",
		"ISBNInput : mns:ISBNType",
		"putResults : mns:putResultsType" };

	@Test
	public void test() {

		String[] elems = new String[] { "BooksInput" };
		prepareModel(++i, elems);
		checkModel(i, elems);
	}

	@Test
	public void test2() {

		String[] elems = new String[] { "BooksUpdate" };
		prepareModel(++i, elems);
		checkModel(i, elems);

	}

	@Test
	public void test3() {

		String[] elems = new String[] { "ISBNInput : mns:ISBNType", "BooksUpdate" };
		prepareModel(++i, elems);
		checkModel(i, elems);
	}

	@Test
	public void test4() {

		String[] elems = new String[] { "putResults : mns:putResultsType" };
		prepareModel(++i, elems);
		checkModel(i, elems);
	}

	@Test
	public void test5() {

		String[] elems = new String[] { virtDocs[0], virtDocs[1] };
		prepareModel(++i, elems);
		checkModel(i, elems);
	}

	@Test
	public void test6() {

		String[] elems = new String[] { virtDocs[1], virtDocs[2] };
		prepareModel(++i, elems);
		checkModel(i, elems);
	}

	@Test
	public void test7() {

		String[] elems = new String[] { virtDocs[0], virtDocs[2] };
		prepareModel(++i, elems);
		checkModel(i, elems);
	}

	@Test
	public void test8() {

		String[] elems = new String[] { virtDocs[0], virtDocs[3] };
		prepareModel(++i, elems);
		checkModel(i, elems);
	}

	@Test
	public void test9() {

		String[] elems = new String[] { virtDocs[1], virtDocs[2] };
		prepareModel(++i, elems);
		checkModel(i, elems);
	}

	@Test
	public void test10() {

		String[] elems = new String[] { virtDocs[1], virtDocs[2], virtDocs[3] };
		prepareModel(++i, elems);
		checkModel(i, elems);
	}

	private void prepareModel(int i, String[] virtDocs) {

		MetadataModelWizard wizard = new MetadataModelWizard();
		wizard.open();
		wizard.setLocation(PROJECT).setModelName(MODEL + i).selectModelClass(ModelClass.XML)
				.selectModelType(ModelType.VIEW)
				.selectModelBuilder(org.jboss.tools.teiid.reddeer.ModelBuilder.BUILD_FROM_XML_SCHEMA).next();
		wizard.selectXMLSchemaFile(new String[] { PROJECT, XSD });
		for (String elem : virtDocs) {
			wizard.addElement(elem);
		}

		wizard.finish();

	}

	private void checkModel(int i2, String[] elems) {
		ModelExplorer modelExplorer = new ModelExplorer();
		modelExplorer.open();
		for (String elem : elems) {
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
			modelExplorer.activate();
			new DefaultTreeItem(PROJECT, MODEL + i2 + ".xmi", el, el2);
			assertTrue(new ModelExplorer().getProject(PROJECT).containsItem(MODEL + i2 + ".xmi", el, el2,
					"mns = http://www.teiid.org/UpdateBooks_Output"));
		}

	}

}
