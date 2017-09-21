package org.jboss.tools.switchyard.ui.bot.test;

import static org.jboss.tools.switchyard.reddeer.project.SwitchYardProject.MAIN_RESOURCE;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.Java2WSDLWizard;
import org.jboss.tools.switchyard.reddeer.wizard.WSDL2JavaWizard;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@SwitchYard
@RunWith(RedDeerSuite.class)
public class ProjectExplorerJavaWSDLTest {

	public static final String PROJECT = "java_wsdl";
	public static final String PACKAGE = "com.example.switchyard." + PROJECT;

	@InjectRequirement
	private static SwitchYardRequirement switchYardRequirement;

	@BeforeClass
	public static void createProjects() {
		saveAndCloseSwitchYardFile();

		new WorkbenchShell().maximize();
		new ProjectExplorer().open();

		// Use SOAP binding due to SWITCHYARD-2310
		switchYardRequirement.project(PROJECT).binding("SOAP").create();

		new SwitchYardProject(PROJECT).getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/" + PROJECT, "Hello.java");

		new SwitchYardProject(PROJECT).getProjectItem("src/main/resources").select();
		new ImportFileWizard().importFile("resources/" + PROJECT, "Service.wsdl");
	}

	@AfterClass
	public static void deleteSwitchYardProject() {
		saveAndCloseSwitchYardFile();
		new ProjectExplorer().deleteAllProjects();
	}

	public static void saveAndCloseSwitchYardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Test
	public void generateWrappedWsdlFromJava() throws Exception {
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "Hello.java").select();
		Java2WSDLWizard wizard = new Java2WSDLWizard().openDialog();
		assertEquals(PROJECT + "/" + MAIN_RESOURCE, wizard.getEnterOrSelectTheParentFolderTXT().getText());
		wizard.getFileNameTXT().setText("HelloWrapped");
		wizard.next();
		wizard.getUseWrappedMessagesCHB().toggle(true);
		wizard.getUseImportsForGeneratedTypeSchemaCHB().toggle(false);
		wizard.finish();

		File file = new SwitchYardProject(PROJECT).getResourceExt("HelloWrapped.wsdl").toFile();
		assertFiles(file, "HelloWrapped.wsdl", "HelloWrapped2.wsdl");
	}

	@Test
	public void generateUnwrappedWsdlFromJava() throws Exception {
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "Hello.java").select();
		Java2WSDLWizard wizard = new Java2WSDLWizard().openDialog();
		assertEquals(PROJECT + "/" + MAIN_RESOURCE, wizard.getEnterOrSelectTheParentFolderTXT().getText());
		wizard.getFileNameTXT().setText("HelloUnwrapped");
		wizard.next();
		wizard.getUseWrappedMessagesCHB().toggle(false);
		wizard.getUseImportsForGeneratedTypeSchemaCHB().toggle(false);
		wizard.finish();

		File file = new SwitchYardProject(PROJECT).getResourceExt("HelloUnwrapped.wsdl").toFile();
		assertFiles(file, "HelloUnwrapped.wsdl", "HelloUnwrapped2.wsdl");
	}

	@Test
	public void generateJavaFromWsdl() throws Exception {
		new SwitchYardProject(PROJECT).getResource("Service.wsdl").select();
		WSDL2JavaWizard wizard = new WSDL2JavaWizard().openDialog();
		assertEquals(PACKAGE, wizard.getPackageTXT().getText());
		assertEquals("/java_wsdl/src/main/resources/Service.wsdl", wizard.getWSDLFileTXT().getText());
		wizard.getCreateWrapperForMessagePartsCHB().toggle(false);
		wizard.getGenerateParameterAndResultTypesCHB().toggle(true);
		wizard.getOverwriteExistingFilesCHB().toggle(true);
		wizard.finish();

		new SwitchYardProject(PROJECT).getClass(PACKAGE, "IdocAssign.java");
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "IdocAssignTab.java");
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "ObjectFactory.java");
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "package-info.java");
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "Qname.java");
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "RfcExceptionMessage.java");
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "ZidocInboundXmlSoapHttp.java");
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "ZidocInboundXmlSoapHttpException.java");
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "ZidocInboundXmlSoapHttpResponse.java");
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "ZidocInboundXmlSoapHttpRfcException.java");
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "ZidocInboundXmlSoapHttpRfcExceptions.java");
		new SwitchYardProject(PROJECT).getClass(PACKAGE, "ZIDOCXMLSOAPWS.java");
	}

	private static void assertFiles(File actualFile, String... expectedFileNamess) throws IOException {
		List<String> actualLines = Files.readAllLines(actualFile.toPath(), Charset.defaultCharset());

		List<String> expectedLines = null;
		for (String expectedFileName : expectedFileNamess) {
			File expectedFile = new File("resources/" + PROJECT + "/" + expectedFileName);
			expectedLines = Files.readAllLines(expectedFile.toPath(), Charset.defaultCharset());
			if (actualLines.equals(expectedLines)) {
				break;
			}
		}

		if (expectedLines != null) {
			assertEquals(toString(expectedLines), toString(actualLines));
		}
	}

	private static String toString(Collection<String> collection) {
		StringBuffer result = new StringBuffer();
		for (String item : collection) {
			result.append(item).append("\n");
		}
		return result.toString();
	}

}
