package org.jboss.tools.switchyard.ui.bot.test;

import static org.jboss.tools.switchyard.reddeer.wizard.TransformersWizard.COLUMN_FROM;
import static org.jboss.tools.switchyard.reddeer.wizard.TransformersWizard.COLUMN_TO;
import static org.jboss.tools.switchyard.reddeer.wizard.TransformersWizard.TRANSFORMER_TYPE_DOZER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardConnection;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.TransformersWizard;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@SwitchYard
@RunWith(RedDeerSuite.class)
public class SwitchYardEditorTransformersTest {

	public static final String PROJECT = "sy_transformer";
	public static final String PACKAGE = "com.example.switchyard." + PROJECT;

	public static final String SERVICE1 = "Service1";
	public static final String SERVICE2 = "Service2";
	public static final String SERVICE2_IMPL = "Service2Impl";

	public static final String OBJECT1_URI = "java:" + PACKAGE + ".Object1";
	public static final String OBJECT2_URI = "java:" + PACKAGE + ".Object2";

	public static final String DOZER_FILE_NEW = "dozer-new.xml";
	public static final String DOZER_FILE_EXISTING = "dozer.xml";

	@InjectRequirement
	private static SwitchYardRequirement switchYardRequirement;

	@BeforeClass
	public static void maximizeWorkbench() {
		new WorkbenchShell().maximize();
	}

	@BeforeClass
	public static void prepareProject() {
		switchYardRequirement.project(PROJECT).impl("Bean").create();

		new SwitchYardProject(PROJECT).getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/" + PROJECT, "Object1.java", "Object2.java", "Service1.java",
				"Service2.java");

		new SwitchYardProject(PROJECT).getProjectItem("src/main/resources").select();
		new ImportFileWizard()
				.importFile("resources/" + PROJECT, "Service1.wsdl", "Service2.wsdl", DOZER_FILE_EXISTING);
	}

	@After
	public void removeAllTransformsAndComponents() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();

		new SwitchYardEditor().getComposite().showProperties().selectTransforms().removeAll().ok();
		new SwitchYardEditor().save();

		try {
			new SwitchYardComponent(SERVICE1).delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			new SwitchYardComponent(SERVICE2_IMPL).delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			new SwitchYardProject(PROJECT).getClass(PACKAGE, SERVICE2_IMPL + ".java").delete();
		} catch (EclipseLayerException|CoreLayerException e) {
			// ok
		}
		new SwitchYardEditor().save();

	}

	@Test
	public void newDozerJava2JavaTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardProject(PROJECT).openSwitchYardFile();
		editor.addBeanImplementation().selectJavaInterface(SERVICE2).setName(SERVICE2_IMPL).finish();

		PromoteServiceWizard wizard = new Service(SERVICE2).promoteService();
		wizard.selectJavaInterface(SERVICE1);
		wizard.setServiceName(SERVICE1);
		wizard.doNotCreateTransformers();
		wizard.finish();

		new SwitchYardConnection().createRequiredTransformers();

		TransformersWizard transformersWizard = new TransformersWizard();
		transformersWizard.getTransformerTypeCMB().setSelection(TRANSFORMER_TYPE_DOZER);
		assertArraySize(0, transformersWizard.getDozerFileOptionsLST().getListItems());
		transformersWizard.newDozerFile(DOZER_FILE_NEW);
		assertArraySize(1, transformersWizard.getDozerFileOptionsLST().getListItems());
		assertEquals("/" + PROJECT + "/src/main/resources/" + DOZER_FILE_NEW, transformersWizard
				.getDozerFileOptionsLST().getListItems()[0]);
		transformersWizard.finish();

		editor.save();
		assertEquals(
				DOZER_FILE_NEW,
				editor.xpath("/switchyard/transforms/transform.dozer[@from='" + OBJECT1_URI + "' and @to='"
						+ OBJECT2_URI + "']/mappingFiles/entry[1]/@file"));
		assertEquals(
				DOZER_FILE_NEW,
				editor.xpath("/switchyard/transforms/transform.dozer[@from='" + OBJECT2_URI + "' and @to='"
						+ OBJECT1_URI + "']/mappingFiles/entry[1]/@file"));

	}

	@Test
	public void existingDozerJava2JavaFirstTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardProject(PROJECT).openSwitchYardFile();
		editor.addBeanImplementation().selectJavaInterface(SERVICE2).setName(SERVICE2_IMPL).finish();

		PromoteServiceWizard wizard = new Service(SERVICE2).promoteService();
		wizard.selectJavaInterface(SERVICE1);
		wizard.setServiceName(SERVICE1);
		wizard.doNotCreateTransformers();
		wizard.finish();

		new SwitchYardConnection().createRequiredTransformers();

		TransformersWizard transformersWizard = new TransformersWizard();
		transformersWizard.getTransformerTypeCMB().setSelection(TRANSFORMER_TYPE_DOZER);
		assertEquals(2, transformersWizard.getTransformerTypePairTBL().getItems().size());
		// Uncheck all pairs
		for (TableItem tableItem : transformersWizard.getTransformerTypePairTBL().getItems()) {
			tableItem.setChecked(false);
		}
		TableItem transformerTypePair = transformersWizard.getTransformerTypePairTBL().getItem(0);
		assertEquals("Object1 {" + PACKAGE + "}", transformerTypePair.getText(COLUMN_FROM));
		assertEquals("Object2 {" + PACKAGE + "}", transformerTypePair.getText(COLUMN_TO));
		transformerTypePair.setChecked(true);

		assertArraySize(0, transformersWizard.getDozerFileOptionsLST().getListItems());

		transformersWizard.browseDozerFile(DOZER_FILE_EXISTING);

		assertArraySize(1, transformersWizard.getDozerFileOptionsLST().getListItems());
		assertEquals("/" + PROJECT + "/src/main/resources/" + DOZER_FILE_EXISTING, transformersWizard
				.getDozerFileOptionsLST().getListItems()[0]);
		transformersWizard.finish();

		editor.save();
		assertEquals(
				DOZER_FILE_EXISTING,
				editor.xpath("/switchyard/transforms/transform.dozer[@from='" + OBJECT1_URI + "' and @to='"
						+ OBJECT2_URI + "']/mappingFiles/entry[1]/@file"));
		assertEquals(
				"",
				editor.xpath("/switchyard/transforms/transform.dozer[@from='" + OBJECT2_URI + "' and @to='"
						+ OBJECT1_URI + "']/mappingFiles/entry[1]/@file"));

	}

	@Test
	public void existingDozerJava2JavaSecondTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardProject(PROJECT).openSwitchYardFile();
		editor.addBeanImplementation().selectJavaInterface(SERVICE2).setName(SERVICE2_IMPL).finish();

		PromoteServiceWizard wizard = new Service(SERVICE2).promoteService();
		wizard.selectJavaInterface(SERVICE1);
		wizard.setServiceName(SERVICE1);
		wizard.doNotCreateTransformers();
		wizard.finish();

		new SwitchYardConnection().createRequiredTransformers();

		TransformersWizard transformersWizard = new TransformersWizard();
		transformersWizard.getTransformerTypeCMB().setSelection(TRANSFORMER_TYPE_DOZER);
		assertEquals(2, transformersWizard.getTransformerTypePairTBL().getItems().size());
		// Uncheck all pairs
		for (TableItem tableItem : transformersWizard.getTransformerTypePairTBL().getItems()) {
			tableItem.setChecked(false);
		}
		TableItem transformerTypePair = transformersWizard.getTransformerTypePairTBL().getItem(1);
		assertEquals("Object2 {" + PACKAGE + "}", transformerTypePair.getText(COLUMN_FROM));
		assertEquals("Object1 {" + PACKAGE + "}", transformerTypePair.getText(COLUMN_TO));
		transformerTypePair.setChecked(true);

		assertArraySize(0, transformersWizard.getDozerFileOptionsLST().getListItems());

		transformersWizard.browseDozerFile(DOZER_FILE_EXISTING);

		assertArraySize(1, transformersWizard.getDozerFileOptionsLST().getListItems());
		assertEquals("/" + PROJECT + "/src/main/resources/" + DOZER_FILE_EXISTING, transformersWizard
				.getDozerFileOptionsLST().getListItems()[0]);
		transformersWizard.finish();

		editor.save();
		assertEquals(
				"",
				editor.xpath("/switchyard/transforms/transform.dozer[@from='" + OBJECT1_URI + "' and @to='"
						+ OBJECT2_URI + "']/mappingFiles/entry[1]/@file"));
		assertEquals(
				DOZER_FILE_EXISTING,
				editor.xpath("/switchyard/transforms/transform.dozer[@from='" + OBJECT2_URI + "' and @to='"
						+ OBJECT1_URI + "']/mappingFiles/entry[1]/@file"));

	}

	@Test
	public void existingDozerJava2JavaBothTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardProject(PROJECT).openSwitchYardFile();
		editor.addBeanImplementation().selectJavaInterface(SERVICE2).setName(SERVICE2_IMPL).finish();

		PromoteServiceWizard wizard = new Service(SERVICE2).promoteService();
		wizard.selectJavaInterface(SERVICE1);
		wizard.setServiceName(SERVICE1);
		wizard.doNotCreateTransformers();
		wizard.finish();

		new SwitchYardConnection().createRequiredTransformers();

		TransformersWizard transformersWizard = new TransformersWizard();
		transformersWizard.getTransformerTypeCMB().setSelection(TRANSFORMER_TYPE_DOZER);
		assertArraySize(0, transformersWizard.getDozerFileOptionsLST().getListItems());

		transformersWizard.browseDozerFile(DOZER_FILE_EXISTING);

		assertArraySize(1, transformersWizard.getDozerFileOptionsLST().getListItems());
		assertEquals("/" + PROJECT + "/src/main/resources/" + DOZER_FILE_EXISTING, transformersWizard
				.getDozerFileOptionsLST().getListItems()[0]);
		transformersWizard.finish();

		editor.save();
		assertEquals(
				DOZER_FILE_EXISTING,
				editor.xpath("/switchyard/transforms/transform.dozer[@from='" + OBJECT1_URI + "' and @to='"
						+ OBJECT2_URI + "']/mappingFiles/entry[1]/@file"));
		assertEquals(
				DOZER_FILE_EXISTING,
				editor.xpath("/switchyard/transforms/transform.dozer[@from='" + OBJECT2_URI + "' and @to='"
						+ OBJECT1_URI + "']/mappingFiles/entry[1]/@file"));

	}

	@Test
	public void dozerXml2JavaTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardProject(PROJECT).openSwitchYardFile();
		editor.addBeanImplementation().selectJavaInterface(SERVICE2).setName(SERVICE2_IMPL).finish();

		PromoteServiceWizard wizard = new Service(SERVICE2).promoteService();
		wizard.selectWSDLInterface(SERVICE1 + ".wsdl");
		wizard.setServiceName(SERVICE1);
		wizard.doNotCreateTransformers();
		wizard.finish();

		new SwitchYardConnection().createRequiredTransformers();

		TransformersWizard transformersWizard = new TransformersWizard();
		transformersWizard.getTransformerTypeCMB().setSelection(TRANSFORMER_TYPE_DOZER);
		assertFalse("Finish button must be disabled", transformersWizard.getFinishBTN().isEnabled());
		transformersWizard.cancel();

	}

	private static void assertArraySize(int expectedSize, Object[] array) {
		if (array.length != expectedSize) {
			Assert.fail("Array contains " + array.length + "elements. Expected is " + expectedSize + ".");
		}
	}

}
