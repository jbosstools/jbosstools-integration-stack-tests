package org.jboss.tools.bpmn2.reddder.editor.tests;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.jboss.reddeer.eclipse.condition.MarkerIsUpdating;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.bpmn2.reddeer.DefaultCheckBox;
import org.jboss.tools.bpmn2.reddeer.ProcessEditorView;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ImportResourceRequirement.ImportResource;
import org.jboss.tools.bpmn2.ui.bot.test.validator.JBPM6Validator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.kie.api.KieBase;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

public class ImportResourceTest {
	protected Logger log;
	protected ImportResource definition;
	protected String lastSavedName;
	protected ProcessEditorView editor;
	protected Process process;
	
	private static boolean configureShellHandled;
	private String diagramSourceCode;
	
	protected static final String STRING_VAR_ONE = "Property_1";
	protected static final String STRING_VAR_TWO = "Property_2";
	protected static final String INT_VAR_ONE = "Property_3";
	protected static final String INT_VAR_TWO = "Property_4";
	protected static final String BOOL_VAR = "Property_5";
	protected static final String OBJECT_VAR = "Property_6";
	
	protected static final String PACKAGE_BASE = "org.jboss.tools.bpmn2";
	
	public ImportResourceTest() {
		this.definition = getClass().getAnnotation(ImportResource.class);
		this.log = Logger.getLogger(getClass());
	}

	@Before
	public void openFile() {
		Project project = new ProjectExplorer().getProject(definition.projectName());
		project.getProjectItem(definition.baseDiagramFileName()).open();
		log.debug("Opened file: " + definition.baseDiagramFileName());
		editor = new ProcessEditorView(definition.baseDiagramFileName().substring(0, definition.baseDiagramFileName().length()-6));
	}
	
	public void saveAsAndValidate(String filename) {
		new ShellMenu(new String[]{"File", "Save As..."}).select();
		new DefaultShell("Save As");
		new DefaultTreeItem(definition.projectName()).select();
		new LabeledText("File name:").setText(filename);
		new PushButton("OK").click();
		
		if(!configureShellHandled) {
			try{
				new DefaultShell("Configure BPMN2 Project Nature");
				new DefaultCheckBox().setChecked(true);
				new PushButton("Yes").click();
			} catch (SWTLayerException e) {
				// probably previously configured
			}
			configureShellHandled = true;
		}
		
		validate(filename);
	}
	
	private void validate(String filename) {
		/*
		 * Validate using jBPM.
		 */
		log.info("Validating '" + editor.getTitle() + "':");
		JBPM6Validator validator = new JBPM6Validator();
		diagramSourceCode = editor.getSourceText();
		boolean result = validator.validate(diagramSourceCode);
		log.info("\tjBPM validation result '" + (result ? "valid" : "not valid") + "'");
		Assert.assertTrue(validator.getResultMessage(), result);
		
		StringBuilder error = new StringBuilder();
		new WaitUntil(new NoProblemsExists(), TimePeriod.NORMAL, false);
		new WaitWhile(new MarkerIsUpdating());
		ProblemsView problems = new ProblemsView();
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
		
		
		captureScreenshotWithDescription(filename.substring(0, filename.length()-6) + "-ok");
	}
	
	
	protected KieSession createKieSession() {
		Resource resource = ResourceFactory.newByteArrayResource(diagramSourceCode.getBytes());
 		resource.setResourceType(ResourceType.BPMN2);
		resource.setSourcePath("./NonExistingProFormaPath");
		
		KieHelper kieHelper = new KieHelper();
		KieBase kieBase = kieHelper.addResource(resource).build();
		KieSession kSession = kieBase.newKieSession();
		
		return kSession;
	}
	
	private void captureScreenshotWithDescription(String description) {
		String fileName = "target/screenshots/" + description + "-" + getClass().getSimpleName() + "." + 
				SWTBotPreferences.SCREENSHOT_FORMAT.toLowerCase();
		SWTUtils.captureScreenshot(fileName);
	}
	
	@BeforeClass
	public static void maximizeActiveShell() {
		Display.getDisplay().syncExec(new Runnable() {
			public void run() {
				new DefaultShell().getSWTWidget().setMaximized(true);
			}
		});
		
		configureShellHandled = false;
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
}
