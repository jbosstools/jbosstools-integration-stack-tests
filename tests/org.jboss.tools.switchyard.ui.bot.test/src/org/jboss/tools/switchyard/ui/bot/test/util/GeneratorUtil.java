package org.jboss.tools.switchyard.ui.bot.test.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsInstanceOf;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.matcher.WithStyleMatcher;
import org.jboss.reddeer.swt.util.Display;

public class GeneratorUtil {

	private static final ControlFinder finder = new GeneratorUtil().new ControlFinder();

	private GeneratorUtil() {

	}

	public static void generate(final Control control) {
		generate(control, System.out);
	}

	public static void generate(String file) {
		try {
			PrintStream out = new PrintStream(new File("/home/apodhrad/Temp", file));
			generate(new DefaultShell().getControl(), out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void generate(final Control control, final PrintStream out) {
		Display.syncExec(new Runnable() {

			@Override
			public void run() {
				if (control instanceof Shell) {
					out.println("new DefaultShell(\"" + WidgetHandler.getInstance().getText(control) + "\");");
				}
				List<Control> list = finder.find(control, new IsInstanceOf(Text.class));
				for (Control c : list) {
					String label = WidgetHandler.getInstance().getLabel(c);
					if (label != null && label.length() > 0) {
						out.println(generatedCode("LabeledText", label, getGroup(c)));
					}
				}
				list = finder.find(control, new IsInstanceOf(Combo.class));
				for (Control c : list) {
					String label = WidgetHandler.getInstance().getLabel(c);
					if (label != null && label.length() > 0) {
						out.println(generatedCode("LabeledCombo", label, getGroup(c)));
					}
				}
				list = finder.find(control, new IsInstanceOf(Button.class), new WithStyleMatcher(SWT.CHECK));
				for (Control c : list) {
					String label = WidgetHandler.getInstance().getText(c);
					if (label != null && label.length() > 0) {
						out.println(generatedCode("CheckBox", label, getGroup(c)));
					}
				}
				list = finder.find(control, new IsInstanceOf(Button.class), new WithStyleMatcher(SWT.PUSH));
				for (Control c : list) {
					String label = WidgetHandler.getInstance().getText(c);
					if (label != null && label.length() > 0) {
						out.println(generatedCode("PushButton", label, getGroup(c)));
					}
				}
				list = finder.find(control, new IsInstanceOf(Button.class), new WithStyleMatcher(SWT.RADIO));
				for (Control c : list) {
					String label = WidgetHandler.getInstance().getText(c);
					if (label != null && label.length() > 0) {
						out.println(generatedCode("RadioButton", label, getGroup(c)));
					}
				}
			}
		});
	}

	private static String generatedCode(String clazz, String label, String group) {
		if ((group != null && (group.equals("Operation Selector") || group.equals("Regular Expressions")))
				|| removeSpecialChars(label).equals("Cancel") || removeSpecialChars(label).equals("Back")
				|| removeSpecialChars(label).equals("Next") || removeSpecialChars(label).equals("Finish")) {
			return "";
		}
		group = (group != null ? "new DefaultGroup(\"" + group + "\"), " : "");
		return "public " + clazz + " get" + removeSpecialChars(label) + "() {\n\treturn new " + clazz + "(" + group
				+ "\"" + removeMnemonics(label) + "\");\n}\n";
	}

	private static String getGroup(Control control) {
		Composite parent;
		while ((parent = control.getParent()) != null) {
			if (parent instanceof Group) {
				return WidgetHandler.getInstance().getText(parent);
			}
			control = parent;
		}
		return null;

	}

	public abstract class Finder<T> {

		public List<T> find(T parent, Matcher<?>... matchers) {
			List<T> list = new ArrayList<T>();
			Stack<T> stack = new Stack<T>();
			// Initial push
			stack.push(parent);
			// Depth first search
			while (!stack.isEmpty()) {
				// Pop figure
				T child = stack.pop();
				// If null then continue
				if (child == null) {
					continue;
				}
				// Does it matches?
				boolean matches = true;
				for (Matcher<?> matcher : matchers) {
					if (!matcher.matches(child)) {
						matches = false;
						break;
					}
				}
				if (matches) {
					list.add(child);
				}
				// Push another children
				for (T t : getChildren(child)) {
					stack.push(t);
				}
			}
			return list;
		}

		public abstract List<T> getChildren(T child);
	}

	public class ControlFinder extends Finder<Control> {

		@Override
		public List<Control> getChildren(Control child) {
			List<Control> children = new ArrayList<Control>();
			if (child instanceof Composite) {
				Composite composite = (Composite) child;
				Control[] controls = composite.getChildren();
				for (Control control : controls) {
					children.add(control);
				}
			}
			return children;
		}

	}

	private static String removeMnemonics(String text) {
		return text.replaceAll("&", "");
	}

	private static String removeSpecialChars(String text) {
		return text.replaceAll(" |\\(|\\)|\\*|\\:|<|>|&|\\.", "");
	}

	public static void main(String[] args) {
		System.out.println(removeMnemonics("get<&Back..."));
	}

}
