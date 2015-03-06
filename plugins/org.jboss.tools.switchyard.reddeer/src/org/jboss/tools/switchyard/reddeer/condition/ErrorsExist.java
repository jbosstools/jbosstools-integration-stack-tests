package org.jboss.tools.switchyard.reddeer.condition;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.WaitCondition;

/**
 * 
 * @author apodhrad
 *
 */
public class ErrorsExist implements WaitCondition {
	
	private List<TreeItem> errors = new ArrayList<TreeItem>();

	@Override
	public boolean test() {
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		errors = problemsView.getAllErrors();
		return !errors.isEmpty();
	}

	@Override
	public String description() {
		return "There are no errors in Problems view";
	}
	
	public List<TreeItem> getAllErrors() {
		return errors;
	}

}
