package org.jboss.tools.bpmn2.ui.bot.test.validator;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.drools.compiler.kie.builder.impl.ResultsImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.Results;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;

/**
 * 
 */
public class JBPM6Validator {

	Results results;

	/**
	 * 
	 */
	public JBPM6Validator() {

	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean validate(File file) {
		Resource resource = KieServices.Factory.get().getResources().newFileSystemResource(file);
		resource.setTargetPath("/" + file.getName());
		return validate(resource);
	}

	/**
	 * 
	 * @param xml
	 * @return
	 */
	public boolean validate(String xml) {
		Resource resource = KieServices.Factory.get().getResources().newByteArrayResource(xml.getBytes());
		/*
		 * ISSUE: if this path does not end with bpmn2 or bpmn then the validation will pass as nothing happened wrong
		 * event though the definition contains errors. IMO the behaviour should be based on the resource type set in
		 * validate(Resource) method but it's ignored.
		 */
		resource.setTargetPath("/ValidatedProcess.bpmn2");
		return validate(resource);
	}

	/**
	 * 
	 * @param resource
	 * @return
	 */
	protected boolean validate(Resource resource) {
		resource.setResourceType(ResourceType.BPMN2);

		try {
			KieServices ks = KieServices.Factory.get();
			KieFileSystem kfs = ks.newKieFileSystem();
			kfs.write(resource);

			KieBuilder kb = ks.newKieBuilder(kfs);
			kb.buildAll();

			results = kb.getResults();
		} catch (Exception e) {
			Writer sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));

			ResultsImpl resultsImpl = new ResultsImpl();
			resultsImpl.addMessage(Level.ERROR, "Unknown", sw.toString());

			results = resultsImpl;
		}

		return !results.hasMessages(Level.ERROR);
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getErrorList() {
		List<String> ret = new ArrayList<String>();
		if (results != null) {
			for (Message m : results.getMessages(Level.ERROR)) {
				ret.add(m.toString());
			}
		}
		return ret;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getWarningList() {
		List<String> ret = new ArrayList<String>();
		if (results != null) {
			for (Message m : results.getMessages(Level.WARNING)) {
				ret.add(m.toString());
			}
		}
		return ret;
	}

	/**
	 * 
	 * @return
	 */
	public String getResultMessage() {
		String resultMessage = "";
		if (results != null) {
			resultMessage = results.toString();
		}
		return resultMessage;
	}

}