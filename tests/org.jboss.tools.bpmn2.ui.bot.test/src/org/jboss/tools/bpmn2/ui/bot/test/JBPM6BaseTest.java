package org.jboss.tools.bpmn2.ui.bot.test;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.tools.bpmn2.reddeer.ProcessEditorView;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.ui.bot.test.validator.JBPM6Validator;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 */
public abstract class JBPM6BaseTest extends SWTBotTestCase {

	protected Logger log = Logger.getLogger(getClass());  
	
	protected ProcessEditorView editor; 
	
	protected ProblemsView problems;

	protected boolean bpmnValidation;
	
	private ProcessDefinition definition;
	
	/**
	 * 
	 */
	public JBPM6BaseTest() {
		definition = getClass().getAnnotation(ProcessDefinition.class);
		if (definition == null) {
			throw new RuntimeException("Validation failed. Missing @ProcessDefinition annotation.");
		}
		
		bpmnValidation = Boolean.parseBoolean(System.getProperty("bpmn.validation", "true"));

		editor = new ProcessEditorView(definition.name().replace("\\s+", ""));
		problems = new  ProblemsView();
	}
	
	/**
	 * Build the process model using the designer functions.
	 */
	public abstract void buildProcessModel();
	
	@Test
	public void executeTest() {
		try {
			openProcessFile();
			buildProcessModel();
			validateProcessModel();
		} catch (RuntimeException e) {
			captureScreenshotWithDescription("screenshot-error-process");
			throw e;
		} finally {
			closeProcessFile();
		}
	}
	
	/**
	 * 
	 */
	protected void saveProcessModel() {
		editor.setFocus();
		if (editor.isDirty()) {
			editor.save();
		}
	}
	
	/**
	 * 
	 */
	protected void validateProcessModel() {
		/*
		 * Make sure all content is saved.
		 */
		saveProcessModel();
		/*
		 * Capture the current state.
		 */
		captureScreenshotWithDescription("screenshot-ok-process");
		/*
		 * Validate using jBPM.
		 */
		log.info("Validating '" + editor.getTitle() + "':");
		JBPM6Validator validator = new JBPM6Validator();
		boolean result = validator.validate(editor.getSourceText());
		log.info("\tjBPM validation result '" + (result ? "valid" : "not valid") + "'");
		Assert.assertTrue(validator.getResultMessage(), result);
		/*
		 * Make sure there are no problems in the problems view.
		 */
		if (bpmnValidation) {
			StringBuilder error = new StringBuilder();
			List<TreeItem> errorList = problems.getAllErrors();
			for (TreeItem e : errorList) {
				if (e.getCell(1).startsWith(editor.getTitle())) {
					error.append(e.getCell(0) + "\n");
				}
			}
			
			boolean isErrorEmpty = error.length() == 0;
			log.info("\tEditor validation result '" + (isErrorEmpty ? "OK" : error) + "'");
			Assert.assertTrue(error.toString(), isErrorEmpty);
		}
	}
	
	/**
	 * 
	 */
	protected void openProcessFile() {
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
	
	/**
	 * 
	 */
	protected void closeProcessFile() {
		new SWTWorkbenchBot().closeAllShells();
		log.info("Closing '" + editor.getTitle() + "'");
		saveProcessModel();
		editor.close();
	}
	
	/**
	 * 
	 * @param description
	 */
	public void captureScreenshotWithDescription(String description) {
		String fileName = "target/screenshots/" + description + "-" + getClass().getSimpleName() + "." + 
				SWTBotPreferences.SCREENSHOT_FORMAT.toLowerCase();
		captureScreenshot(fileName);
	}
	
	/**
	 * 
	 * @return
	 */
	public ProcessEditorView getEditor() {
		return editor;
	}
	
	/**
	 * Maximizes active shell.
	 * 
	 * Taken from ui.bot.ext.
	 */
	private static void maximizeActiveShell() {
		Display.getDisplay().syncExec(new Runnable() {
			public void run() {
				new DefaultShell().getSWTWidget().setMaximized(true);
			}
		});
	}
}
