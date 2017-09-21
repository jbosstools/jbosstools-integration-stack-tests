package org.jboss.tools.bpel.reddeer.activity;

import org.eclipse.reddeer.core.matcher.WithLabelMatcher;
import org.eclipse.reddeer.swt.api.Tree;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.bpel.reddeer.matcher.WithLabelMatcherExt;

/**
 * 
 * @author apodhrad
 * 
 */
public class Assign extends Activity {

	public static final String LABEL_FROM = "From:";
	public static final String LABEL_TO = "To:";

	public static final String VAR = "Variable";
	public static final String FIX = "Fixed Value";
	public static final String EXP = "Expression";

	public Assign(String name) {
		super(name, ASSIGN);
	}

	public Assign addFixToVar(String fixedValue, String variable) {
		return addFixToVar(fixedValue, new String[] { variable });
	}

	public Assign addFixToVar(String fixedValue, String[] variable) {
		return addAssign(FIX, new String[] { fixedValue }, VAR, variable);
	}

	public Assign addVarToExp(String variable, String expression) {
		return addVarToExp(new String[] { variable }, expression);
	}

	public Assign addFixToExp(String fixedValue, String exression) {
		return addAssign(FIX, new String[] { fixedValue }, EXP, new String[] { exression });
	}

	public Assign addVarToExp(String[] variable, String expression) {
		return addAssign(VAR, variable, EXP, new String[] { expression });
	}

	public Assign addExpToVar(String expression, String variable) {
		return addExpToVar(expression, new String[] { variable });
	}

	public Assign addExpToVar(String expression, String[] variable) {
		return addAssign(EXP, new String[] { expression }, VAR, variable);
	}

	public Assign addExpToExp(String expression1, String expression2) {
		return addAssign(EXP, new String[] { expression1 }, EXP, new String[] { expression2 });
	}

	public Assign addVarToVar(String[] variable1, String[] variable2) {
		return addAssign(VAR, variable1, VAR, variable2);
	}

	public Assign addAssign(String from, String[] valueFrom, String to, String[] valueTo) {
		openProperties().selectDetails();

		new PushButton("New").click();
		fillAssignement(LABEL_FROM, from, valueFrom);
		fillAssignement(LABEL_TO, to, valueTo);

		save();

		// Variable doesn't have initializer. Should it be generated?
		if (new DefaultShell().getText().equals("Initializer")) {
			new PushButton("Yes").click();
			save();
		}
		return this;
	}

	private void fillAssignement(String label, String assignment, String... value) {
		new LabeledCombo(label).setSelection(assignment);
		if (assignment.equals(VAR)) {
			Tree tree = new DefaultTree(new WithLabelMatcher(label));
			new DefaultTreeItem(tree, value).select();
		}
		if (assignment.equals(FIX)) {
			new DefaultText(0).setText("'" + value[0] + "'");
		}
		if (assignment.equals(EXP)) {
			new DefaultStyledText(new WithLabelMatcherExt(label)).setText(value[0]);
		}
	}

}
