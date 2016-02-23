package org.jboss.tools.teiid.reddeer.matcher;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

public class RefArrowMatcher extends BaseMatcher<EditPart> {

	@Override
	public boolean matches(Object item) {
		if (item instanceof GraphicalEditPart){
			if (item.getClass().toString()
					.equals("class org.teiid.designer.mapping.ui.part.MappingExtentEditPart")){
				IFigure figure = ((GraphicalEditPart) item).getFigure();
				if (figure.getChildren().size() == 1){
					if (figure.getChildren().get(0).getClass().toString()
							.equals("class org.eclipse.draw2d.Polygon")){												
						return true;								
					}
				}
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description desc) {
		desc.appendText("is a reference arrow");
		
	}
	
	@Factory
	public static RefArrowMatcher createRefArrowMatcher() {
		return new RefArrowMatcher();
	}
}
