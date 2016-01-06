package org.jboss.tools.switchyard.reddeer.condition;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;

/**
 * 
 * @author apodhrad
 *
 */
public class ErrorsExist extends AbstractWaitCondition {

	private List<Problem> errors = new ArrayList<Problem>();

	@Override
	public boolean test() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		errors = problemsView.getProblems(ProblemType.ERROR);
		return !errors.isEmpty();
	}

	@Override
	public String description() {
		return "There are no errors in Problems view";
	}

	public List<Problem> getAllErrors() {
		return errors;
	}

}
