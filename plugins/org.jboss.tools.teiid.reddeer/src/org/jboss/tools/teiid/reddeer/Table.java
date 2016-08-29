package org.jboss.tools.teiid.reddeer;

import java.util.Properties;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

@Deprecated // TODO refactor to TableDialog
public class Table extends ModelObject {

	public enum Type {
		VIEW, SRC;
	}

	public static final String PROPERTIES = "Properties";
	public static final String COLUMNS = "Columns";
	public static final String PRIMARY_KEY = "Primary Key";
	public static final String UNIQUE_CONSTRAINT = "Unique Constraint";
	public static final String FOREIGN_KEYS = "Foreign Keys";
	public static final String INDEXES = "Indexes";

	public static class Template {
		public static final String SIMPLE = "Simple SELECT";
		public static final String SELECT_WITH_JOIN = "SELECT with Join Criteria";
		public static final String UNION_QUERY = "UNION Query";
		public static final String FLAT_FILE = "Flat File - Local Source";
		public static final String XML_FILE_LOCAL = "XML File - Local Source";
		public static final String XML_FILE_URL = "XML File - URL Source";
	}

	public void create(Type type, String name, Properties props) {
		new DefaultShell("Create Relational View Table");
		// Name
		new LabeledText("Name").setText(name);

		String prop = null;
		// Name In Source
		if ((prop = props.getProperty("nameInSrc")) != null) {
			new LabeledText("Name In Source").setText(prop);
		}

		// Properties
		fillProperties(type, props);

		// Columns
		fillColumns(type, props);

		fillPrimaryKey(type, props);

		fillUniqueConstraint(type, props);

		// Native Query
		fillNativeQuery(type, props);

		// Transformation SQL
		fillTransformationSQL(type, props);

		fillIndexes(type, props);

		new PushButton("OK").click();
	}

	private void fillIndexes(Type type, Properties props) {

	}

	private void fillUniqueConstraint(Type type, Properties props) {

	}

	private void fillPrimaryKey(Type type, Properties props) {

	}

	private void fillColumns(Type type, Properties props) {
		String prop = props.getProperty("cols");
		if (prop != null) {
			new SWTWorkbenchBot().tabItem(COLUMNS).activate();
			// ALL add, delete, move up, move down
			setupTableProps(prop);
		}
	}

	private void fillProperties(Type type, Properties props) {
		new SWTWorkbenchBot().tabItem(PROPERTIES).activate();
		if (type.equals(Type.VIEW)) {

		}

	}

	private void fillTransformationSQL(Type type, Properties props) {
		if (type.equals(Type.VIEW)) {
			super.fillTransformationSQL(props);
		}
	}

	private void fillNativeQuery(Type type, Properties props) {
		if (type.equals(Type.VIEW)) {
			super.fillNativeQuery(props);
		}

	}

}
