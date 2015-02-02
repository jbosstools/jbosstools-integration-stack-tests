package org.jboss.tools.bpmn2.ui.bot.complex.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.tools.bpmn2.reddeer.dialog.JavaProjectWizard;
import org.jboss.tools.bpmn2.reddeer.editor.tests.tmp.ImportFileWizard;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;

public class JBPM6ComplexTestDefinitionRequirement implements Requirement<JBPM6ComplexTestDefinition> {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface JBPM6ComplexTestDefinition {
		public String projectName();
		public String importFolder() default "";
		public String saveAs();
		public String openFile();
		public String dependentOn() default "";
	}
	
	private JBPM6ComplexTestDefinition declaration;
	private static List<String> foldersToImport = new ArrayList<String>(Arrays.asList(""));
	
	@Override
	public boolean canFulfill() {
		return true;
	}

	@Override
	public void fulfill() {
		Display.getDisplay().syncExec(new Runnable() {
			public void run() {
				new DefaultShell().getSWTWidget().setMaximized(true);
			}
		});
		
		PackageExplorer pe = new PackageExplorer();
		if (!pe.containsProject(declaration.projectName())) {
			new JavaProjectWizard().execute(declaration.projectName());
			
			try {
				new DefaultShell("Open Associated Perspective?");
				new PushButton("No").click();
			} catch (WidgetNotFoundException e) {
				// ignore
			}
		}
		
		if(!foldersToImport.contains(declaration.importFolder())){
			foldersToImport.add(declaration.importFolder());
			new ImportFileWizard().importFile(declaration.importFolder());
		}
		
		Project project = new ProjectExplorer().getProject(declaration.projectName());
		project.getProjectItem(declaration.openFile()).open();
	}

	@Override
	public void setDeclaration(JBPM6ComplexTestDefinition declaration) {
		this.declaration = declaration;
		
	}

}
