package org.jboss.tools.switchyard.reddeer.widget;

import org.eclipse.swt.graphics.Point;
import org.jboss.tools.switchyard.reddeer.utils.ControlUtils;
import org.jboss.tools.switchyard.reddeer.utils.MouseUtils;

/**
 * Represents a radio button widget.
 * 
 * @author apodhrad
 * 
 */
public class RadioButtonExt extends org.eclipse.reddeer.swt.impl.button.RadioButton {

	public RadioButtonExt(String label) {
		super(label);
	}

	@Override
	public void click() {
		Point point = ControlUtils.getAbsolutePoint(swtWidget);
		MouseUtils.click(point.x, point.y);
	}

}
