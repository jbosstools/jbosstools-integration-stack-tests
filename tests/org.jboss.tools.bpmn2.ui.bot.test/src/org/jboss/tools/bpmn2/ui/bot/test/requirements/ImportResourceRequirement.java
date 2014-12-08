package org.jboss.tools.bpmn2.ui.bot.test.requirements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.jboss.tools.bpmn2.reddeer.dialog.JavaProjectWizard;
import org.jboss.tools.bpmn2.reddeer.editor.tests.tmp.ImportFileWizard;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ImportResourceRequirement.ImportResource;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

public class ImportResourceRequirement implements Requirement<ImportResource>{
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface ImportResource {

		String projectName();
		
		String folderToImportName();
		
		String baseDiagramFileName();
		
	}
	
	private ImportResource requirement;
	
	@Override
	public boolean canFulfill() {
		return true;
	}

	@Override
	public void fulfill() {
		
		PackageExplorer pe = new PackageExplorer();
		if (!pe.containsProject(requirement.projectName())) {
			new JavaProjectWizard().execute(requirement.projectName());
			
			try {
				new DefaultShell("Open Associated Perspective?");
				new PushButton("No").click();
			} catch (WidgetNotFoundException e) {
				// ignore
			}
			
			new ImportFileWizard().importFile(requirement.folderToImportName());
		}
	}
	
	@Override
	public void setDeclaration(ImportResource declaration) {
		this.requirement = declaration;
	}
}
