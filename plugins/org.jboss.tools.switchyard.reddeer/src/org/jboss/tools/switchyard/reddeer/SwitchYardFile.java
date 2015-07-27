package org.jboss.tools.switchyard.reddeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.jboss.tools.switchyard.reddeer.editor.XPathEvaluator;

/**
 * 
 * @author apodhrad
 *
 */
public class SwitchYardFile {

	private File file;

	public SwitchYardFile(String projectName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		IFile iFile = project.getFile("src/main/resources/META-INF/switchyard.xml");
		file = iFile.getLocation().toFile();
	}

	public String xpath(String expr) {
		XPathEvaluator xpath = new XPathEvaluator(file);
		String result = xpath.evaluateString(expr);
		return result;
	}

	public String getSource() throws IOException {
		StringBuffer source = new StringBuffer();
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = in.readLine()) != null) {
			source.append(line).append("\n");
		}
		in.close();
		return source.toString();
	}
}
