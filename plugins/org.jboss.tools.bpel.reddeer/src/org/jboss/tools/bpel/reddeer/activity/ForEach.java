package org.jboss.tools.bpel.reddeer.activity;

import org.eclipse.reddeer.swt.impl.styledtext.DefaultStyledText;

/**
 * 
 * @author apodhrad
 * 
 */
public class ForEach extends Activity {

	public ForEach(String name) {
		super(name, FOR_EACH);
	}

	public void addScope(String name) {
		add(SCOPE, name);
	}

	public Scope getScope() {
		return new Scope(null, this);
	}

	public ForEach setCounterValue(String startExpression, String finalExpression) {
		select();
		openProperties().selectTab("Counter Values");
		new DefaultStyledText(0).setText(startExpression);
		new DefaultStyledText(1).setText(finalExpression);
		save();

		return this;
	}
}
