package org.jboss.tools.runtime.reddeer.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.jboss.tools.runtime.reddeer.Namespaces;
import org.jboss.tools.runtime.reddeer.RuntimeBase;
import org.jboss.tools.runtime.reddeer.wizard.JBPMRuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.wizard.JBPMRuntimeWizard;
import org.osgi.framework.Bundle;

/**
 * jBPM Runtime
 * 
 * @author apodhrad
 * 
 */
@XmlRootElement(name = "jbpm", namespace = Namespaces.SOA_REQ)
@XmlAccessorType(XmlAccessType.FIELD)
public class RuntimeJBPM extends RuntimeBase {

	@Override
	public void create() {
		// Add runtime
		JBPMRuntimePreferencePage jbpmRuntimePreferencePage = new JBPMRuntimePreferencePage();
		jbpmRuntimePreferencePage.open();
		JBPMRuntimeWizard jbpmRuntimeWizard = jbpmRuntimePreferencePage.addRuntime();
		jbpmRuntimeWizard.setName(getName());
		jbpmRuntimeWizard.setLocation(getHome());
		jbpmRuntimeWizard.ok();
		jbpmRuntimePreferencePage.ok();
	}

	@Override
	public boolean exists() {
		Bundle bundle = Platform.getBundle("org.jboss.tools.jbpm.common");
		File installationsFile = new File(Platform.getStateLocation(bundle).toFile(), "jbpm-installations.xml");
		if (!installationsFile.exists()) {
			return false;
		}
		List<String> runtimes = getJBPMRuntimes(installationsFile);
		return runtimes.contains(name);
	}

	protected List<String> getJBPMRuntimes(File installationsFile) {
		List<String> runtimes = new ArrayList<String>();

		Reader reader = null;
		try {
			reader = new FileReader(installationsFile);
			XMLMemento memento = XMLMemento.createReadRoot(reader);
			IMemento[] children = memento.getChildren("installation");
			for (int i = 0; i < children.length; i++) {
				String name = children[i].getString("name");
				runtimes.add(name);
			}
		} catch (WorkbenchException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return runtimes;
	}

}
