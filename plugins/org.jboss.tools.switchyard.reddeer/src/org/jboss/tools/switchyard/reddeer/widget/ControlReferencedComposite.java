package org.jboss.tools.switchyard.reddeer.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.reference.ReferencedComposite;
import org.jboss.tools.switchyard.reddeer.utils.ControlUtils;

/**
 * 
 * @author apodhrad
 *
 */
public class ControlReferencedComposite implements ReferencedComposite {

	private ReferencedComposite ref;
	private Matcher<?> matcher;
	private int index;

	public ControlReferencedComposite(ReferencedComposite ref, Matcher<?> matcher, int index) {
		this.ref = ref;
		this.matcher = matcher;
		this.index = index;
	}

	@Override
	public Control getControl() {
		List<Control> controls = new ControlUtils().findAllWidgets(ref.getControl());
		List<Control> matchedControls = new ArrayList<Control>();
		for (Control control : controls) {
			if (matcher.matches(control)) {
				matchedControls.add(control);
			}
		}
		if (matchedControls.size() < index + 1) {
			throw new RuntimeException("Cannot find control");
		}
		return matchedControls.get(index);
	}

}
