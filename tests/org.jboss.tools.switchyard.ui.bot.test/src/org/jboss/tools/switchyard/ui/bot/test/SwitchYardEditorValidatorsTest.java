package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.preference.CompositePropertiesPage;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.utils.PreferenceUtils;
import org.jboss.tools.switchyard.reddeer.wizard.ValidatorWizard;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Validators Test.
 * 
 * @author apodhrad
 *
 */
@SwitchYard
@RunWith(RedDeerSuite.class)
public class SwitchYardEditorValidatorsTest {

	public static final String PROJECT_NAME = "sy_validator_project";
	public static final String PROJECT_PACKAGE = "com.example.switchyard." + PROJECT_NAME;

	@InjectRequirement
	private static SwitchYardRequirement switchYardRequirement;

	private static String autoBuilding;

	@BeforeClass
	public static void turnOffAutoBuilding() {
		autoBuilding = PreferenceUtils.getAutoBuilding();
		PreferenceUtils.setAutoBuilding("false");
	}

	@AfterClass
	public static void turnBackAutoBuilding() {
		PreferenceUtils.setAutoBuilding(autoBuilding);
	}

	@BeforeClass
	public static void maximizeWorkbench() {
		new WorkbenchShell().maximize();
	}

	@BeforeClass
	public static void createProject() {
		switchYardRequirement.project(PROJECT_NAME).create();
	}

	@Before
	public void openSwitchYardFile() {
		new SwitchYardProject(PROJECT_NAME).openSwitchYardFile();
	}

	@After
	public void removeAllValidators() {
		new SwitchYardEditor().showProperties().selectValidators().removeAll().ok();
		new SwitchYardEditor().saveAndClose();
	}

	@Test
	public void validatorDTDTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		CompositePropertiesPage compositePropertiesPage = editor.showProperties();
		ValidatorWizard wizard = compositePropertiesPage.selectValidators().add();
		wizard.selectValidatorType("XML");
		wizard.next();
		wizard.setName("aaa");
		wizard.selectSchemaType("DTD");
		wizard.finish();
		compositePropertiesPage.activate().ok();
		editor.save();

		assertEquals("1", editor.xpath("count(/switchyard/validates)"));
		assertEquals("1", editor.xpath("count(/switchyard/validates/validate.xml)"));
		assertEquals("aaa", editor.xpath("/switchyard/validates/validate.xml/@name"));
		assertEquals("DTD", editor.xpath("/switchyard/validates/validate.xml/@schemaType"));

		editor.showProperties().selectValidators().selectByName("aaa").remove().ok();
		editor.save();
	}

	@Test
	public void validatorXMLSchemaTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		CompositePropertiesPage compositePropertiesPage = editor.showProperties();
		ValidatorWizard wizard = compositePropertiesPage.selectValidators().add();
		wizard.selectValidatorType("XML");
		wizard.next();
		wizard.setName("aaa");
		wizard.selectSchemaType("XMLSCHEMA");
		wizard.finish();
		compositePropertiesPage.activate().ok();
		editor.save();

		assertEquals("1", editor.xpath("count(/switchyard/validates)"));
		assertEquals("1", editor.xpath("count(/switchyard/validates/validate.xml)"));
		assertEquals("aaa", editor.xpath("/switchyard/validates/validate.xml/@name"));
		assertEquals("XML_SCHEMA", editor.xpath("/switchyard/validates/validate.xml/@schemaType"));

		editor.showProperties().selectValidators().selectByName("aaa").remove().ok();
		editor.save();
	}

	@Test
	public void validatorRelaxNGTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		CompositePropertiesPage compositePropertiesPage = editor.showProperties();
		ValidatorWizard wizard = compositePropertiesPage.selectValidators().add();
		wizard.selectValidatorType("XML");
		wizard.next();
		wizard.setName("aaa");
		wizard.selectSchemaType("RELAX_NG");
		wizard.finish();
		compositePropertiesPage.activate().ok();
		editor.save();

		assertEquals("1", editor.xpath("count(/switchyard/validates)"));
		assertEquals("1", editor.xpath("count(/switchyard/validates/validate.xml)"));
		assertEquals("aaa", editor.xpath("/switchyard/validates/validate.xml/@name"));
		assertEquals("RELAX_NG", editor.xpath("/switchyard/validates/validate.xml/@schemaType"));

		editor.showProperties().selectValidators().selectByName("aaa").remove().ok();
		editor.save();
	}

	@Test
	public void validatorJavaClassTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		CompositePropertiesPage compositePropertiesPage = editor.showProperties();
		ValidatorWizard wizard = compositePropertiesPage.selectValidators().add();
		wizard.selectValidatorType("Java");
		wizard.next();
		wizard.setName("aaa");
		wizard.setJavaClass("bbb");
		wizard.finish();
		compositePropertiesPage.activate().ok();
		editor.save();

		assertEquals("1", editor.xpath("count(/switchyard/validates)"));
		assertEquals("1", editor.xpath("count(/switchyard/validates/validate.java)"));
		assertEquals("aaa", editor.xpath("/switchyard/validates/validate.java/@name"));
		assertEquals("bbb", editor.xpath("/switchyard/validates/validate.java/@class"));

		editor.showProperties().selectValidators().selectByName("aaa").remove().ok();
		editor.save();
	}

	@Test
	public void validatorJavaBeanTest() throws Exception {
		SwitchYardEditor editor = new SwitchYardEditor();
		CompositePropertiesPage compositePropertiesPage = editor.showProperties();
		ValidatorWizard wizard = compositePropertiesPage.selectValidators().add();
		wizard.selectValidatorType("Java");
		wizard.next();
		wizard.setName("aaa");
		wizard.setBeanName("bbb");
		wizard.finish();
		compositePropertiesPage.activate().ok();
		editor.save();

		assertEquals("1", editor.xpath("count(/switchyard/validates)"));
		assertEquals("1", editor.xpath("count(/switchyard/validates/validate.java)"));
		assertEquals("aaa", editor.xpath("/switchyard/validates/validate.java/@name"));
		assertEquals("bbb", editor.xpath("/switchyard/validates/validate.java/@bean"));

		editor.showProperties().selectValidators().selectByName("aaa").remove().ok();
		editor.save();
	}

}
