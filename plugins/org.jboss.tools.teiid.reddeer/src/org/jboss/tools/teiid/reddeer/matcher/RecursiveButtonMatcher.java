package org.jboss.tools.teiid.reddeer.matcher;

import org.eclipse.draw2d.Clickable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.jboss.reddeer.core.util.Display;
import org.jboss.tools.teiid.reddeer.editor.ModelEditor;

public class RecursiveButtonMatcher extends BaseMatcher<EditPart> {
	private String mappingClassPrefix;

	public RecursiveButtonMatcher(String mappingClassPrefix) {
		this.mappingClassPrefix = mappingClassPrefix;
	}

	@Override
	public boolean matches(Object item) {
		if (item instanceof GraphicalEditPart) {
			ModelEditorItemMatcher mappingClassMatcher = new ModelEditorItemMatcher(ModelEditor.ItemType.MAPPING_CLASS, mappingClassPrefix);
			if (mappingClassMatcher.matches(item)){
				for (Object child : ((GraphicalEditPart) item).getFigure().getChildren()) {
					if (child instanceof Clickable){
						final Clickable recursiveButton = (Clickable) child;
						Display.syncExec(new Runnable() {
							@Override
							public void run() {
								recursiveButton.doClick();
							}
						});
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {

	}

	@Factory
	public static RecursiveButtonMatcher createMatcher(String mappingClassPrefix) {
		return new RecursiveButtonMatcher(mappingClassPrefix);
	}

}
