package org.jboss.tools.bpel.reddeer.condition;

import java.util.List;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;

/**
 * Returns true if there is no error
 * 
 * @author apodhrad
 * 
 */
public class NoErrorExists extends AbstractWaitCondition {

	private List<Problem> errors;

	@Override
	public boolean test() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		problemsView.getProblems(ProblemType.ERROR);
		return errors.isEmpty();
	}

	@Override
	public String description() {
		StringBuffer result = new StringBuffer();
		if (errors != null && errors.size() > 0) {
			result.append("There are the following " + errors.size() + " errors:");
			result.append(System.getProperty("line.separator"));
			for (Problem error : errors) {
				result.append(error.getDescription());
				result.append(System.getProperty("line.separator"));
			}
		} else {
			result.append("Tehere are no errors");
		}
		return result.toString();
	}
}
