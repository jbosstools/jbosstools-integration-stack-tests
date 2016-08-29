package org.jboss.tools.teiid.reddeer.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

public class SQLResult {
	private static Logger logger = new Logger(SQLResult.class);
	public static final String STATUS_SUCCEEDED = "Succeeded";

	private TreeItem resultRow;

	public SQLResult(TreeItem resultRow) {
		this.resultRow = resultRow;
	}

	public String getStatus() {
		return resultRow.getCell(0);
	}

	public int getCount() {
		resultRow.doubleClick();
		new DefaultCTabItem("Result1").activate();
		return new DefaultTable().rowCount();
	}

	/**
	 * Converts current SQL result into CSV format
	 * 
	 * @return
	 */
	public String getResultAsCSV() {
		resultRow.doubleClick();
		new DefaultCTabItem("Result1").activate();
		DefaultTable table = new DefaultTable();
		int colNum = table.getHeaders().size();
		StringBuilder csv = new StringBuilder();
		for (int i = 1; i < table.getHeaders().size(); i++) {
			csv.append('\"').append(table.getHeaders().get(i)).append('\"').append(',');
		}
		csv.setCharAt(csv.length() - 1, '\n');
		for (TableItem t : table.getItems()) {
			for (int i = 1; i < colNum; i++) {
				csv.append('\"').append(t.getText(i)).append('\"').append(',');
			}
			csv.setCharAt(csv.length() - 1, '\n');
		}
		return csv.toString();
	}

	/**
	 * Compares current query result with expected result. It converts the result into CSV.
	 * 
	 * @param pathToExpectedResult
	 *            path to CSV file with expected result
	 * @return true if the current and expected result are the same
	 */
	public boolean compareCSVQueryResults(File pathToExpectedResult) {
		String expected = null;
		try {
			expected = convertFileToString(pathToExpectedResult);
		} catch (IOException e) {
			logger.error("Can't compare query resuts", e);
			throw new RuntimeException(e);
		}
		String actual = getResultAsCSV();
		boolean result = expected.equals(actual);
		if (!result) {
			logger.warn("Comparing of query results failed: Expected result: " + expected + "\nBut was: " + actual);
		}
		return result;
	}

	/**
	 * Converts file into String and skips blank lines
	 * 
	 * @param file
	 *            Path to file to be converted
	 * @return file as a String
	 * @throws IOException
	 *             if a file has not been found
	 */
	private String convertFileToString(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			if (line.trim().length() == 0) {
				continue;
			}
			stringBuilder.append(line);
			stringBuilder.append('\n');
		}
		reader.close();
		return stringBuilder.toString();
	}

}
