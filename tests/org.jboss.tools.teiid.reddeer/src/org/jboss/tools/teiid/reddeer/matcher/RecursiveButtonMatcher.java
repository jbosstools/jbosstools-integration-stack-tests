package org.jboss.tools.teiid.reddeer.matcher;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.asyncExec;

import java.util.List;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

public class RecursiveButtonMatcher extends BaseMatcher<EditPart>{

	private static final String MAPPING_CLASS = "<<Mapping Class>>";
	private static String prefixMappingClassRecursive;
	
	private Clickable b;
	
	public RecursiveButtonMatcher(String prefixMappingClassRecursive){
		this.prefixMappingClassRecursive = prefixMappingClassRecursive;
	}
	
	@Override
	public boolean matches(Object item) {
		
		boolean isMappingClass = false;
		boolean startsWithPrefix = false;
		
		if (item instanceof GraphicalEditPart) {
			if (item.getClass()
					.toString()
					.equals("class org.teiid.designer.diagram.ui.notation.uml.part.UmlClassifierEditPart")) {
				IFigure figure = ((GraphicalEditPart) item).getFigure();

				List<IFigure> children = figure.getChildren();// header,
																// footer,
																// triangle,
																// imagefigure,
																// button
				for (IFigure figure2 : children) {
					if (figure2 instanceof Clickable) {
						
						//verify correct mapping class
						for (IFigure sibling : children){
							if (sibling.getClass()
									.toString()
									.equals("class org.teiid.designer.diagram.ui.notation.uml.figure.UmlClassifierHeader")){
								List<IFigure> siblingChildren = sibling.getChildren();
								for (IFigure siblingChild : siblingChildren){
									if (siblingChild instanceof Label) {
										String text = ((Label) siblingChild).getText();
										if (text.equals(MAPPING_CLASS)) {
											isMappingClass = true;
										}
										if (text.startsWith(prefixMappingClassRecursive)) {
											startsWithPrefix = true;
										}
									}
								}
							}
						}
						System.out.println(isMappingClass +", "+ startsWithPrefix);
						
						if (isMappingClass && startsWithPrefix){
							b =(Clickable) figure2; 
							asyncExec(new VoidResult() {
								@Override
								public void run() {										
									b.doClick();
								}
							});
							return true;
						}
						
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
	public static RecursiveButtonMatcher createRecursiveButtonMatcher(String prefix) {
		return new RecursiveButtonMatcher(prefix);
	}

}
