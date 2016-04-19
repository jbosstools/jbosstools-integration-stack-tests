package org.jboss.tools.common.reddeer.ext;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchSite;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.core.handler.WidgetHandler;
import org.jboss.reddeer.core.lookup.ShellLookup;
import org.jboss.reddeer.core.lookup.WorkbenchPartLookup;
import org.jboss.reddeer.core.matcher.AndMatcher;
import org.jboss.reddeer.core.matcher.ClassMatcher;
import org.jboss.reddeer.core.matcher.MatcherBuilder;
import org.jboss.reddeer.core.reference.ReferencedComposite;
import org.jboss.reddeer.core.resolver.WidgetResolver;
import org.jboss.reddeer.core.util.DiagnosticTool;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;

/**
 * Widget lookup provides methods for looking up eclipse widgets.
 * 
 * @author Jiri Peterka
 * @author Jaroslav Jankovic
 */
public class WidgetLookupExt {

	private static WidgetLookupExt instance = null;
	private static final Logger logger = Logger.getLogger(WidgetLookupExt.class);

	private WidgetLookupExt() {
	}

	/**
	 * Gets instance of WidgetLookupExt.
	 * 
	 * @return WidgetLookupExt instance
	 */
	public static WidgetLookupExt getInstance() {
		if (instance == null)
			instance = new WidgetLookupExt();
		return instance;
	}

	/**
	 * Method looks for active widget located in specified referenced composite, laying on specified index and matching
	 * specified matchers.
	 *
	 * @param <T>
	 *            the generic type
	 * @param refComposite
	 *            reference composite to search for widgets
	 * @param clazz
	 *            class type of widget
	 * @param index
	 *            index of widget within referenced composite
	 * @param matchers
	 *            matchers to match widget
	 * @return widget located withing specified referenced composite, laying on specified index and matching specified
	 *         matchers
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Widget> T activeWidget(ReferencedComposite refComposite, Class<T> clazz, int index,
			Matcher... matchers) {
		logger.debug("Looking up active widget with class type " + clazz.getName() + ", index " + index + " and "
				+ createMatcherDebugMsg(matchers));

		ClassMatcher cm = new ClassMatcher(clazz);
		Matcher[] allMatchers = MatcherBuilder.getInstance().addMatcher(matchers, cm);
		AndMatcherExt am = new AndMatcherExt(allMatchers);

		Control parentControl = getParentControl(refComposite);
		WidgetIsFoundExt found = new WidgetIsFoundExt(parentControl, index, am.getMatchers());
		try {
			new WaitUntil(found, TimePeriod.SHORT);
		} catch (WaitTimeoutExpiredException ex) {
			String exceptionText = "No matching widget found with " + am.toString();
			exceptionText += "\n" + new DiagnosticTool().getDiagnosticInformation(parentControl);
			logger.error("Active widget with class type " + clazz.getName() + " and index " + index + " was not found");
			throw new CoreLayerException(exceptionText, ex);
		}

		logger.debug("Active widget with class type " + clazz.getName() + " and index " + index + " was found");
		return (T) found.getWidget();
	}

	/**
	 * Method looks for active widgets located in specified referenced composite and matching specified matchers.
	 *
	 * @param <T>
	 *            the generic type
	 * @param refComposite
	 *            reference composite to search for widgets
	 * @param clazz
	 *            class type of widgets
	 * @param matchers
	 *            matchers to match widget
	 * @return widgets located in specified referenced composite and matching specified matchers
	 */
	public <T extends Widget> List<T> activeWidgets(ReferencedComposite refComposite, Class<T> clazz,
			Matcher<?>... matchers) {
		logger.debug("Looking up active widgets with class type " + clazz.getName() + " and "
				+ createMatcherDebugMsg(matchers));

		ClassMatcher cm = new ClassMatcher(clazz);
		Matcher<?>[] allMatchers = MatcherBuilder.getInstance().addMatcher(matchers, cm);
		AndMatcher am = new AndMatcher(allMatchers);

		List<T> foundWidgets = activeWidgets(refComposite.getControl(), am);
		logger.debug("Found " + foundWidgets.size() + " widgets");
		return foundWidgets;
	}

	private Control getParentControl(ReferencedComposite refComposite) {
		if (refComposite == null) {
			return findParent();
		}
		return refComposite.getControl();
	}

	/**
	 * Finds parent control of active widget .
	 *
	 * @return parent control of active widget or throws an exception if null
	 */
	public Control findParent() {
		logger.debug("No parent specified, finding one");
		Control parent = getActiveWidgetParentControl();
		logger.debug("Parent found successfully");

		if (parent == null) {
			logger.error("Unable to determine active parent");
			throw new CoreLayerException("Unable to determine active parent");
		}

		return parent;
	}

	/**
	 * Finds active widget or reference composite matching given matcher.
	 *
	 * @param <T>
	 *            the generic type
	 * @param refComposite
	 *            given reference composite
	 * @param matcher
	 *            given matcher
	 * @return active widget
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Widget> List<T> activeWidgets(Control refComposite, Matcher matcher) {
		logger.trace("Looking up widgets with specified parent and matchers");
		List<T> widgets = findControls(refComposite, matcher, true);
		logger.trace(widgets.size() + " widget(s) found");
		return widgets;
	}

	/**
	 * Finds out whether extra shell (shell different than workbench shell) is active.
	 * 
	 * @return true if extra shell is active, false otherwise
	 */
	public boolean isExtraShellActive() {
		IWorkbenchPartReference activeWorkbenchReference = WorkbenchPartLookup.getInstance()
				.findActiveWorkbenchPartReference();
		Shell activeWorkbenchParentShell = getShellForActiveWorkbench(activeWorkbenchReference);

		Shell activeShell = ShellLookup.getInstance().getActiveShell();
		if (activeWorkbenchParentShell == null || activeWorkbenchParentShell != activeShell) {
			return true;
		}
		return false;
	}

	/**
	 * Gets active parent control. Method finds either active workbench referenced control active shell.
	 *
	 * @return active workbench control or active shell
	 */
	public Control getActiveWidgetParentControl() {
		Control control = null;

		IWorkbenchPartReference activeWorkbenchReference = WorkbenchPartLookup.getInstance()
				.findActiveWorkbenchPartReference();
		Shell activeWorkbenchParentShell = getShellForActiveWorkbench(activeWorkbenchReference);
		Shell activeShell = ShellLookup.getInstance().getActiveShell();

		if ((activeWorkbenchParentShell == null || !activeWorkbenchParentShell.equals(activeShell))
				&& activeShell != null) {
			logger.trace("Setting active shell with title \"" + WidgetHandler.getInstance().getText(activeShell)
					+ "\" as the parent");
			control = activeShell;
		} else {
			if (activeWorkbenchReference != null) {
				logger.trace("Setting workbench part with title \"" + getTitle(activeWorkbenchReference)
						+ "\"as the parent");
				control = WorkbenchPartLookup.getInstance().getWorkbenchControl(activeWorkbenchReference);
			}
		}
		return control;
	}

	/**
	 * Gets the shell for active workbench.
	 *
	 * @param workbenchReference
	 *            the workbench reference
	 * @return the shell for active workbench
	 */
	protected Shell getShellForActiveWorkbench(IWorkbenchPartReference workbenchReference) {
		if (workbenchReference == null) {
			return null;
		}
		IWorkbenchPart wPart = workbenchReference.getPart(true);
		if (wPart == null) {
			return null;
		}
		IWorkbenchSite wSite = wPart.getSite();
		if (wSite == null) {
			return null;
		}
		return wSite.getShell();
	}

	/**
	 * Get widget with given index from list of widgets .
	 *
	 * @param <T>
	 *            the generic type
	 * @param widgets
	 *            list of widgets
	 * @param index
	 *            widget index
	 * @return widget with given index or null if out of range
	 */
	public <T extends Widget> T getProperWidget(List<T> widgets, int index) {
		T widget = null;
		if (widgets.size() > index) {
			logger.trace("Selecting widget with the specified index (" + index + ")");
			widget = widgets.get(index);
		} else {
			logger.trace("The specified index is bigger than the size of found widgets (" + index + " > "
					+ widgets.size() + ")");
		}
		return widget;
	}

	/**
	 * Finds list of controls matching specified matcher for active parent widget.
	 *
	 * @param <T>
	 *            the generic type
	 * @param matcher
	 *            matcher to match parent controls
	 * @param recursive
	 *            true for recursive lookup of control widgets
	 * @return list of parent controls for active parent or single parent control
	 */
	public <T extends Widget> List<T> findActiveParentControls(final Matcher<T> matcher, final boolean recursive) {
		List<T> findControls = findControls(getActiveWidgetParentControl(), matcher, recursive);
		return findControls;
	}

	/**
	 * Finds list of controls matching specified matchers for parent widget.
	 * 
	 * @param parentWidget
	 *            parent widget to search for controls
	 * @param matcher
	 *            matcher to match controls
	 * @param recursive
	 *            true for recursive lookup
	 * @return list of control widgets matching specified matchers of single parent control
	 */
	private <T extends Widget> List<T> findControls(final Widget parentWidget, final Matcher<T> matcher,
			final boolean recursive) {
		List<T> ret = Display.syncExec(new ResultRunnable<List<T>>() {

			@Override
			public List<T> run() {
				List<T> findControlsUI = findControlsUI(parentWidget, matcher, recursive);
				return findControlsUI;
			}
		});
		return ret;
	}

	/**
	 * Gets control with focus.
	 * 
	 * @return control with focus
	 */
	public Control getFocusControl() {
		Control c = Display.syncExec(new ResultRunnable<Control>() {
			@Override
			public Control run() {
				Control focusControl = Display.getDisplay().getFocusControl();
				return focusControl;
			}
		});
		return c;
	}

	/**
	 * Gets list of children control widgets located within specified parent widget matching specified matcher.
	 * 
	 * @param parentWidget
	 *            parent widget
	 * @param matcher
	 *            matcher to match widgets
	 * @param recursive
	 *            true for recursive search, false otherwise
	 * @return children control widget matching specified matcher
	 */
	@SuppressWarnings("unchecked")
	private <T extends Widget> List<T> findControlsUI(final Widget parentWidget, final Matcher<T> matcher,
			final boolean recursive) {

		if ((parentWidget == null) || parentWidget.isDisposed())
			return new ArrayList<T>();

		if (!visible(parentWidget)) {
			return new ArrayList<T>();
		}

		LinkedHashSet<T> controls = new LinkedHashSet<T>();

		if (matcher.matches(parentWidget) && !controls.contains(parentWidget))
			try {
				controls.add((T) parentWidget);
			} catch (ClassCastException exception) {
				throw new IllegalArgumentException("The specified matcher should only match against is declared type.",
						exception);
			}
		if (recursive) {
			List<Widget> children = WidgetResolver.getInstance().getChildren(parentWidget);
			controls.addAll(findControlsUI(children, matcher, recursive));
		}
		return new ArrayList<T>(controls);
	}

	/**
	 * Gets list of children control widgets matching specified matcher from specified list of widgets. Method can be
	 * used recursively to get all children in descendants.
	 * 
	 * Note: Must be used in UI Thread
	 * 
	 * @param widgets
	 *            list of widgets to get children from
	 * @param matcher
	 *            matcher to match widgets
	 * @param recursive
	 *            true for recursive search, false otherwise
	 * @return list of children widgets of widgets from specified list
	 */
	private <T extends Widget> List<T> findControlsUI(final List<Widget> widgets, final Matcher<T> matcher,
			final boolean recursive) {
		LinkedHashSet<T> list = new LinkedHashSet<T>();
		for (Widget w : widgets) {
			list.addAll(findControlsUI(w, matcher, recursive));
		}
		return new ArrayList<T>(list);
	}

	/**
	 * Finds out whether widget is visible or not.
	 * 
	 * @param w
	 *            widget to resolve
	 * @return true if widget is visible, false otherwise
	 */
	private boolean visible(Widget w) {
		return !((w instanceof Control) && !((Control) w).getVisible());
	}

	private String getTitle(final IWorkbenchPartReference part) {
		return Display.syncExec(new ResultRunnable<String>() {
			@Override
			public String run() {
				return part.getTitle();
			}
		});
	}

	private String createMatcherDebugMsg(Matcher<?>[] matchers) {
		StringBuilder sb = new StringBuilder();

		if (matchers.length == 0) {
			sb.append("no matchers specified");
		} else {
			sb.append("following matchers specified (");
		}

		for (int ind = 0; ind < matchers.length; ind++) {
			sb.append(matchers[ind].getClass());
			if (ind < matchers.length - 1) {
				sb.append(", ");
			} else {
				sb.append(")");
			}
		}
		return sb.toString();
	}

	/**
	 * Find all parent widgets.
	 *
	 * @return the list
	 */
	public List<Control> findAllParentWidgets() {
		List<Control> allWidgets = findControls(findParent(), new BaseMatcher<Control>() {

			@Override
			public boolean matches(Object obj) {
				return true;
			}

			@Override
			public void describeTo(Description desc) {

			}

		}, true);
		return allWidgets;
	}

}
