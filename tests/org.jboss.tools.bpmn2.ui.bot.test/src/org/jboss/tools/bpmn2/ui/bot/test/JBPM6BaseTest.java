package org.jboss.tools.bpmn2.ui.bot.test;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.bpmn2.reddeer.view.BPMN2Editor;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.ui.bot.test.validator.JBPM6Validator;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public abstract class JBPM6BaseTest extends SWTBotTestCase {

	protected Logger log = Logger.getLogger(getClass());  
	
	private ProcessDefinition definition;
	
	private BPMN2Editor editor; 
	
	public JBPM6BaseTest() {
		/*
		 * Initialize
		 */
		definition = getClass().getAnnotation(ProcessDefinition.class);
		if (definition == null) {
			throw new RuntimeException("Validation failed. Missing @ProcessDefinition annotation.");
		}
		
		editor = new BPMN2Editor(definition.name().replace("\\s+", ""));
	}
	
	public abstract void buildProcessModel();
	
	@Test
	public void executeTest() {
		try {
			openProcessFile();
			buildProcessModel();
			validateProcessModel();
		} finally {
			closeProcessFile();
		}
	}
	
	public void repairProcessModel() {
		/*
		 * Fix IDs dialog.
		 */
		new DefaultShell("Selection Needed").setFocus();
		new PushButton("Select All").click();
		new PushButton("OK").click();
		/*
		 * BPMN2 nature dialog
		 */
		try {
			editor.projectNature(false);
		} catch (SWTLayerException e ) {
			// The dialog may not appear
		}
	}
	
	public void validateProcessModel() {
		/*
		 * Make sure all content is saved.
		 */
		editor.setFocus();
		if (editor.isDirty()) {
			editor.save();
		}
		
		/*
		 * Sometimes generated IDs are not unique. Fix
		 * it!
		 */
		try {
			repairProcessModel();
		} catch (SWTLayerException e) {
			log.warn(e.getMessage());
		}
		
		/*
		 * Capture the current state.
		 */
		captureScreenshot();
		/*
		 * Validate.
		 */
		log.info("Validating '" + editor.getTitle() + "'");
		JBPM6Validator validator = new JBPM6Validator();
		boolean result = validator.validate(editor.getSourceText());
		Assert.assertTrue(validator.getResultMessage(), result);
	}
	
	public void openProcessFile() {
		/*
		 * Open process definition.
		 */
		maximizeActiveShell();
		new PackageExplorer().getProject(definition.project()).getProjectItem(editor.getTitle() + ".bpmn2").open();
		/*
		 * Activate requested editing profile.
		 */
		editor.activateTool("Profiles", definition.profile());
	}
	
	public void closeProcessFile() {
		new SWTWorkbenchBot().closeAllShells();
		log.info("Closing '" + editor.getTitle() + "'");
		editor.close();
	}
	
	public void captureScreenshot() {
		String fileName = "target/screenshots/screenshot-process-definition-" + getClass().getSimpleName() + "." + 
				SWTBotPreferences.SCREENSHOT_FORMAT.toLowerCase();
		captureScreenshot(fileName);
	}
	
	public BPMN2Editor getEditor() {
		return editor;
	}
	
	/**
	 * Maximizes active shell.
	 * 
	 * Taken from ui.bot.ext.
	 */
	private static void maximizeActiveShell() {
		final Shell shell = (Shell) (new SWTBot().activeShell().widget);
		new SWTBot().getDisplay().syncExec(new Runnable() {

			public void run() {
				shell.setMaximized(true);

			}
		});
	}
}
