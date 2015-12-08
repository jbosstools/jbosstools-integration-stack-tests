package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.junit.JUnitView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.fuse.reddeer.condition.JUnitHasFinished;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.editor.DataTransformationEditor;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.utils.ResourceHelper;
import org.jboss.tools.fuse.reddeer.wizard.NewFuseTransformationTestWizard;
import org.jboss.tools.fuse.reddeer.wizard.NewFuseTransformationWizard;
import org.jboss.tools.fuse.reddeer.wizard.NewFuseTransformationWizard.TransformationType;
import org.jboss.tools.fuse.reddeer.wizard.NewFuseTransformationWizard.TypeDefinition;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test covers Data Transformation Tooling
 * 
 * @author tsedmik
 */
@RunWith(RedDeerSuite.class)
public class DataTransformationTest extends DefaultTest {

	/**
	 * <p>Basic Test</p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>import 'starter' project from 'resources/projects/starter'</li>
	 * <li>create a new Data Transformation</li>
	 * <li>modify the Camel Route (connect Data Transformation node)</li>
	 * <li>create a new Data Transformation test</li>
	 * <li>run test and verify output</li>
	 * </ol>
	 */
	@Test
	public void testBasics() {

		ProjectFactory.importExistingProject(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/projects/starter"), "starter", true, true);
		new CamelProject("starter").openCamelContext("camel-context.xml");
		CamelEditor editor = new CamelEditor("camel-context.xml");
		editor.activate();
		editor.deleteCamelComponent("file:target/messa...");
		editor.addCamelComponent("Data Transformation", 200, 200);
		NewFuseTransformationWizard wizard = new NewFuseTransformationWizard();
		wizard.setTransformationID("xml2json");
		wizard.setSourceType(TransformationType.XML);
		wizard.setTargetType(TransformationType.JSON);
		wizard.next();
		wizard.setXMLTypeDefinition(TypeDefinition.Schema);
		wizard.setXMLSourceFile("abc-order.xsd");
		wizard.next();
		wizard.setJSONTypeDefinition(TypeDefinition.Schema);
		wizard.setJSONTargetFile("xyz-order.json");
		wizard.finish();
		editor.activate();
		editor.addConnection("file:src/data?fil...", "ref:xml2json");
		editor.doOperation("ref:xml2json", "Add", "Components", "File");
		editor.selectEditPart("file:directoryName");
		editor.setUriProperty("file:target/messages?fileName=xyz-order.json");
		editor.close(true);

		DataTransformationEditor transEditor = new DataTransformationEditor("transformation.xml");
		transEditor.createNewVariable("ORIGIN");
		transEditor.createVariableTransformation("ABCOrder", "ORIGIN", "XyzOrder", new String[] {"XyzOrder", "origin"});
		transEditor.createExpressionTransformation("ABCOrder", "Header", "approvalID", "XyzOrder", new String[] {"XyzOrder", "approvalCode"});
		transEditor.createTransformation("ABCOrder", new String[] {"ABCOrder", "header", "customerNum"}, "XyzOrder", new String[] {"XyzOrder", "custId"});
		transEditor.createTransformation("ABCOrder", new String[] {"ABCOrder", "header", "orderNum"}, "XyzOrder", new String[] {"XyzOrder", "orderId"});
		transEditor.createTransformation("ABCOrder", new String[] {"ABCOrder", "header", "status"}, "XyzOrder", new String[] {"XyzOrder", "priority"});
		transEditor.createTransformation("ABCOrder", new String[] {"ABCOrder", "orderItems", "item", "id"}, "XyzOrder", new String[] {"XyzOrder", "lineItems", "itemId"});
		transEditor.createTransformation("ABCOrder", new String[] {"ABCOrder", "orderItems", "item", "price"}, "XyzOrder", new String[] {"XyzOrder", "lineItems", "cost"});
		transEditor.createTransformation("ABCOrder", new String[] {"ABCOrder", "orderItems", "item", "quantity"}, "XyzOrder", new String[] {"XyzOrder", "lineItems", "amount"});

		new CamelProject("starter").selectProjectItem("src/main/resources", "META-INF", "spring", "camel-context.xml");
		NewFuseTransformationTestWizard test = new NewFuseTransformationTestWizard();
		test.open();
		test.setPackage("example");
		test.selectTransformationID("xml2json");
		test.finish();
		TextEditor javaEditor = new TextEditor("TransformationTest.java");
		javaEditor.insertLine(25, "startEndpoint.sendBodyAndHeader(readFile(\"src/data/abc-order.xml\"), \"approvalID\", \"AUTO_OK\");");
		javaEditor.save();
		new ShellMenu("Run", "Run").select();
		new WaitUntil(new ShellWithTextIsAvailable("Run As"));
		new DefaultShell("Run As");
		new DefaultTableItem("JUnit Test").select();
		new PushButton("OK").click();
		new WaitUntil(new JUnitHasFinished(), TimePeriod.getCustom(20));
		new WorkbenchShell();
		assertEquals("Result of JUnit test is wrong", "1/1", new JUnitView().getRunStatus());
		new WaitUntil(new ConsoleHasText("{\"custId\":\"ACME-123\",\"priority\":\"GOLD\",\"orderId\":\"ORDER1\",\"origin\":\"ORIGIN\",\"approvalCode\":\"AUTO_OK\",\"lineItems\":[{\"itemId\":\"PICKLE\",\"amount\":1000,\"cost\":2.25},{\"itemId\":\"BANANA\",\"amount\":400,\"cost\":1.25}]}"));
	}
}
