package org.jboss.tools.switchyard.reddeer.condition;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;

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
