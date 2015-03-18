package org.jboss.tools.bpmn2.ui.bot.complex.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationException;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.jboss.reddeer.eclipse.condition.MarkerIsUpdating;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
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
			
			saveViaReddeer();
			
			resolvePossibleConflictsAfterImportInterface();
			
			ProcessEditorView editor = new ProcessEditorView(filenameTitle);
			diagramSourceCode = editor.getSourceText();
			
			removeBaseFileFromProject();
			
			StringBuilder error = new StringBuilder();
			for(Method method : validateMethods) {
				try{
					method.invoke(this);
				}catch(InvocationTargetException e){
					error.append(method.getName()).append(e.getCause().toString()).append("\n");
				}catch (Exception e) {
					error.append(method.getName()).append(e.toString()).append("\n");
				}
			}
			if(error.toString().length() > 0) {
				throw new ValidationException(error.toString());
			}
			
			if(definition.noErrorsInValidation()){
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
	
	private void defaultValidate() {

		JBPM6Validator validator = new JBPM6Validator();
		
		boolean result = validator.validate(diagramSourceCode);
		log.info("\tjBPM validation result '" + (result ? "valid" : "not valid") + "'");
		Assert.assertTrue(validator.getResultMessage(), result);
		
		String error = getErrorsFromProblemsView(filenameTitle, null);
		
		boolean isErrorEmpty = error.length() == 0;
		log.info("\tEditor validation result '" + (isErrorEmpty ? "OK" : error) + "'");
		Assert.assertTrue(error.toString(), isErrorEmpty);
	}
	
	private KieSession createKieSession() {
		if(diagramSourceCode.compareTo("") == 0) {
			throw new IllegalStateException("File was not stored yet");
		}
		
		KieHelper kieHelper = new KieHelper();
		
		if(definition.dependentOn().compareTo("") != 0) {
			kieHelper = addDependenciesToKieHelper(kieHelper, definition.dependentOn());
		}
		
		Resource resource = ResourceFactory.newByteArrayResource(diagramSourceCode.getBytes());
 		resource.setResourceType(ResourceType.BPMN2);
		resource.setSourcePath("/home"); // it is not checked
		
		KieBase kieBase = kieHelper.addResource(resource).build();
		KieSession kSession = kieBase.newKieSession();
		
		
		
		return kSession;
	}
	
	private void removeBaseFileFromProject() {
		Project project = new ProjectExplorer().getProject(definition.projectName());
		project.getProjectItem(definition.openFile()).delete();
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
	
	private KieHelper addDependenciesToKieHelper(KieHelper helper, String dependencyFileName) {
		Project project = new ProjectExplorer().getProject(definition.projectName());
		project.getProjectItem(definition.dependentOn()).open();
		
		Resource resource = null;
		
		if(dependencyFileName.endsWith("bpmn2")){
			ProcessEditorView editor = new ProcessEditorView();
			editor.setFocus();
			String source = editor.getSourceText();
			resource = ResourceFactory.newByteArrayResource(source.getBytes());
			resource.setResourceType(ResourceType.BPMN2);
		} else
		if(dependencyFileName.endsWith("drl")) {
			String source = new DefaultStyledText().getText();
			resource = ResourceFactory.newByteArrayResource(source.getBytes());
			resource.setResourceType(ResourceType.DRL);
		}
 		
 		
		resource.setSourcePath("/home/dependency"); // it is not checked
		
		helper = helper.addResource(resource);
		
		return helper;
	}
	
	private void saveViaReddeer(){
		new WaitWhile(new SaveWasNotSuccessfull());
	}
	
	private class SaveWasNotSuccessfull implements WaitCondition {

		@Override
		public boolean test() {
			try {
				new ShellMenu(new String[]{"File", "Save All"}).select();
			} catch(SWTLayerException e) {
				return true;
			}
			
			return false;
		}

		@Override
		public String description() {
			return "Wait for successfull save operation";
		}
		
	}
	
	protected boolean isInSourceCode(String text) {
		return diagramSourceCode.contains(text);
	}
	
	protected String getErrorsFromProblemsView(String resourcePrefix, String descriptionPrefix){
		ProblemsView problems = new ProblemsView();
		problems.open();
		new WaitWhile(new MarkerIsUpdating());
		new WaitWhile(new JobIsRunning());
		new WaitUntil(new ErrorAppearOrDisappear(problems), TimePeriod.getCustom(5), false);
		
		List<TreeItem> errorList = problems.getAllErrors();
		
		StringBuilder error = new StringBuilder();
		for (TreeItem e : errorList) {
			if(resourcePrefix != null && descriptionPrefix != null) {
				if (e.getCell(1).startsWith(resourcePrefix) && e.getCell(0).startsWith(descriptionPrefix)) {
					error.append(e.getCell(0) + "\n");
				}
			} else if(resourcePrefix != null) {
				if (e.getCell(1).startsWith(resourcePrefix)) {
					error.append(e.getCell(0) + "\n");
				}
			} else if(descriptionPrefix != null) {
				if (e.getCell(0).startsWith(descriptionPrefix)) {
					error.append(e.getCell(0) + "\n");
				}
			}
			
		}
		
		return error.toString();
	}
	
	private void resolvePossibleConflictsAfterImportInterface() {
		try{
			new DefaultShell("Selection Needed").setFocus();
			new PushButton("Select All").click();
			new PushButton("OK").click();
		}catch(SWTLayerException e) {
			// no conflicts
		}
	}
}
