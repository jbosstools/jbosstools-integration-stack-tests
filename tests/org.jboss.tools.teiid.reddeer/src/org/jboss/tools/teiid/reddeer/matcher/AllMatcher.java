package org.jboss.tools.teiid.reddeer.matcher;

import org.eclipse.swt.widgets.Combo;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class AllMatcher extends BaseMatcher{

	@Override
	public boolean matches(Object item) {
		/*if (item.getClass().toString().contains("org.eclipse.swt.widgets.Combo")){
			System.out.println(item.getClass());
		}*/
		if (item instanceof Combo){
			((Combo)item).select(0);
		}
		return true;
	}

	@Override
	public void describeTo(Description description) {
		// TODO Auto-generated method stub
		
	}

}
