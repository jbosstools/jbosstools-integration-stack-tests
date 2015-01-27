package org.jboss.tools.bpmn2.ui.bot.complex.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.jboss.reddeer.eclipse.condition.MarkerIsUpdating;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.bpmn2.reddeer.DefaultCheckBox;
import org.jboss.tools.bpmn2.reddeer.ProcessEditorView;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.test.validator.JBPM6Validator;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

public abstract class JBPM6ComplexTest {
	
	private JBPM6ComplexTestDefinition definition;
	private Logger log;
	private Class<?> obj;
	
	private Method modelMethod;
	private List<Method> validateMethods;
	private List<Method> runMethods;
	
	private static boolean configureShellHandled;
	private static final int MAX_DISPLAYED_ERRORS = 100;
	private static List<String> usedBaseFiles = new ArrayList<String>();
	
	private String diagramSourceCode = "";
	private String filenameTitle;
	
	protected static final String VARIABLE1 = "VariableOne";
	protected static final String VARIABLE2 = "VariableTwo";
	
	
	
	public JBPM6ComplexTest() {
		validateMethods = new ArrayList<Method>();
		runMethods = new ArrayList<Method>();
		obj = getClass();
		definition = obj.getAnnotation(JBPM6ComplexTestDefinition.class);
		log = Logger.getLogger(getClass());
		filenameTitle = definition.saveAs().substring(0, definition.saveAs().length()-6);
		
		if(definition.openFile().startsWith("Base")) {
			if(!usedBaseFiles.contains(definition.openFile())) {
				usedBaseFiles.add(definition.openFile());
			}
		}
		
		Method[] methods = obj.getMethods();
		
		for(Method method : methods) {
			if(method.isAnnotationPresent(TestPhase.class)) {
				TestPhase testPhase = (TestPhase) method.getAnnotation(TestPhase.class);
				switch (testPhase.phase()) {
					case MODEL:
						modelMethod = method; 
						break;
					case VALIDATE:
						validateMethods.add(method); 
						break;
					case RUN:
						runMethods.add(method); 
						break;
				}
			}
		}
	}
	
	@Test
	public void jbpm6ComplexTest() throws Throwable {
		
		try {
			modelMethod.invoke(this);
			
			saveAs(definition.saveAs());
			
			ProcessEditorView editor = new ProcessEditorView(filenameTitle);
			diagramSourceCode = editor.getSourceText();
			
			if(validateMethods.size() > 0) {
				for(Method method : validateMethods) {
					method.invoke(this);
				}
			} else {
				defaultValidate();
			}
			
			for(Method method : runMethods) {
				method.invoke(this, createKieSession());
			}
			
			captureScreenshotWithDescription("ok-"+definition.saveAs().substring(0, definition.saveAs().length()-6));
			
		} catch(InvocationTargetException e) {
			captureScreenshotWithDescription("error-"+definition.saveAs().substring(0, definition.saveAs().length()-6));
			throw e.getCause();
		} catch (Exception e) {
			captureScreenshotWithDescription("error-"+definition.saveAs().substring(0, definition.saveAs().length()-6));
			throw e;
		}
	}
	
	public void saveAs(String filename) {
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
	}
	
	private void defaultValidate() {

		JBPM6Validator validator = new JBPM6Validator();
		
		boolean result = validator.validate(diagramSourceCode);
		log.info("\tjBPM validation result '" + (result ? "valid" : "not valid") + "'");
		Assert.assertTrue(validator.getResultMessage(), result);
		
		
		ProblemsView problems = new ProblemsView();
		problems.open();
		new WaitWhile(new MarkerIsUpdating());
		new WaitWhile(new JobIsRunning());
		new WaitUntil(new ErrorAppearOrDisappear(problems), TimePeriod.getCustom(5), false);
		
		List<TreeItem> errorList = problems.getAllErrors();
		
		if(errorList.size() >= MAX_DISPLAYED_ERRORS) {
			removeFilesFromProject(usedBaseFiles);
			errorList = problems.getAllErrors();
			usedBaseFiles.clear();
		}
		
		StringBuilder error = new StringBuilder();
		for (TreeItem e : errorList) {
			if (e.getCell(1).startsWith(filenameTitle)) {
				error.append(e.getCell(0) + "\n");
			}
		}
		
		boolean isErrorEmpty = error.length() == 0;
		log.info("\tEditor validation result '" + (isErrorEmpty ? "OK" : error) + "'");
		Assert.assertTrue(error.toString(), isErrorEmpty);
	}
	
	private KieSession createKieSession() {
		if(diagramSourceCode.compareTo("") == 0) {
			throw new IllegalStateException("File was not stored yet");
		}
		Resource resource = ResourceFactory.newByteArrayResource(diagramSourceCode.getBytes());
 		resource.setResourceType(ResourceType.BPMN2);
		resource.setSourcePath("/home"); // it is not checked
		
		KieHelper kieHelper = new KieHelper();
		KieBase kieBase = kieHelper.addResource(resource).build();
		KieSession kSession = kieBase.newKieSession();
		
		return kSession;
	}
	
	private void removeFilesFromProject(List<String> files) {
		Project project = new ProjectExplorer().getProject(definition.projectName());
		for(String filename : files) {
			project.getProjectItem(filename).delete();
		}
	}
	
	private void captureScreenshotWithDescription(String description) {
		String fileName = "target/screenshots/" + description + "-" + getClass().getSimpleName() + "." + 
				SWTBotPreferences.SCREENSHOT_FORMAT.toLowerCase();
		SWTUtils.captureScreenshot(fileName);
	}
	
	private class ErrorAppearOrDisappear implements WaitCondition {

		private int oldCount;
		private ProblemsView problems;
		
		public ErrorAppearOrDisappear(ProblemsView problemsView) {
			problems = problemsView;
			oldCount = problems.getAllErrors().size();
		}
		
		@Override
		public boolean test() {
			int newCount = problems.getAllErrors().size();
			return oldCount != newCount;
		}

		@Override
		public String description() {
			return "Waiting if for old counnt("+oldCount+") of errors change";
		}
		
	}
}
