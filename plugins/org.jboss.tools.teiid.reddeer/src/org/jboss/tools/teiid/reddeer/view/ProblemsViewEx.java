package org.jboss.tools.teiid.reddeer.view;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;

public class ProblemsViewEx extends ProblemsView {
	
	/**
	 * Checks whether there are present errors and fails with description of errors if yes. 
	 */
	public static void checkErrors(){
		// temporally until problem with Errors sync is present.
		new ShellMenu("Project", "Clean...").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new OkButton().click();
		AbstractWait.sleep(TimePeriod.SHORT);
		
		AbstractWait.sleep(TimePeriod.SHORT);
		List<Problem> problems = new ProblemsView().getProblems(ProblemType.ERROR);
		String msg = "";
		for (Problem problem : problems){
			msg += problem.getPath() + "/" + problem.getResource() + "/" + problem.getLocation() + " | " + problem.getDescription() + "\n";
		}
		assertTrue("Validation Errors:\n"+msg, problems.isEmpty());
	}

}
