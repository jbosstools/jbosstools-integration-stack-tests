package org.jboss.tools.bpmn2.ui.bot.test;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.jboss.reddeer.eclipse.condition.MarkerIsUpdating;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.bpmn2.reddeer.ProcessEditorView;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.ui.bot.test.validator.JBPM6Validator;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

/**
 * 
 */
public abstract class JBPM6BaseTest extends SWTBotTestCase {

	protected Logger log = Logger.getLogger(getClass());  
	
	protected ProcessEditorView editor; 
	
	protected ProblemsView problems;

	protected boolean bpmnValidation;
	
	private ProcessDefinition definition;
	
	private String diagramSourceCode;
	
	protected String diagramFileLocation;
	
	protected JbpmAssertionsForBPMN2 jbpmAsserter;
	
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
		jbpmAsserter = new JbpmAssertionsForBPMN2();
	}
	
	/**
	 * Build the process model using the designer functions.
	 */
	public abstract void buildProcessModel();
	
	/**
	 * Should run builded model, and assert if expected events have occurred
	 */
	public abstract void assertRunOfProcessModel(KieSession kSession);
	
	public void runProcessModel() {
		Resource resource = ResourceFactory.newByteArrayResource(diagramSourceCode.getBytes());
		resource.setResourceType(ResourceType.BPMN2);
		resource.setSourcePath(diagramFileLocation);
		
		KieHelper kieHelper = new KieHelper();
		KieBase kieBase = kieHelper.addResource(resource).build();
		KieSession kSession = kieBase.newKieSession();
		
		assertRunOfProcessModel(kSession);
	}
	
	@Test
	public void executeTest() {
		try {
			openProcessFile();
			buildProcessModel();
			validateProcessModel();
			runProcessModel();
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
			new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
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
		diagramSourceCode = editor.getSourceText();
		boolean result = validator.validate(diagramSourceCode);
		log.info("\tjBPM validation result '" + (result ? "valid" : "not valid") + "'");
		Assert.assertTrue(validator.getResultMessage(), result);
		/*
		 * Make sure there are no problems in the problems view.
		 */
		if (bpmnValidation) {
			StringBuilder error = new StringBuilder();
			new WaitUntil(new NoProblemsExists(), TimePeriod.NORMAL, false);
			new WaitWhile(new MarkerIsUpdating());
			problems = new ProblemsView();
			problems.open();
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
	
	private class NoProblemsExists implements WaitCondition {

		private ProblemsView problemsView;

		@Override
		public boolean test() {
			problemsView = new ProblemsView();
			problemsView.open();

			List<TreeItem> errors = problemsView.getAllErrors();
			
			return errors.isEmpty(); 
			
		}

		@Override
		public String description() {
			return "NoProblemsExist";
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
		ProjectItem diagramFile = new PackageExplorer().getProject(definition.project()).getProjectItem(editor.getTitle() + ".bpmn2");
		diagramFile.select();
		
		new ContextMenu("Properties").select();
		new DefaultShell("Properties for " + definition.name() + ".bpmn2").setFocus();
		diagramFileLocation = new LabeledText("Location:").getText();
		new PushButton("OK").click();
		
		diagramFile.open();
		
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
