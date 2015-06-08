package org.jboss.tools.switchyard.ui.bot.test.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

/**
 * 
 * @author apodhrad
 *
 */
public class TemplateHandler {

	public static final String TEMPLATES_DIR = "resources/templates";

	private static TemplateHandler instance;

	private Configuration cfg;;

	private TemplateHandler() {
		cfg = new Configuration(new Version(2, 3, 21));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.US);
		try {
			cfg.setDirectoryForTemplateLoading(new File(TEMPLATES_DIR));
		} catch (IOException ioe) {
			throw new TemplateHandlerException("Cannot use templeate folder '" + TEMPLATES_DIR + "'", ioe);
		}
	}

	public static TemplateHandler getInstance() {
		if (instance == null) {
			instance = new TemplateHandler();
		}
		return instance;
	}

	public static String javaSource(String javaTemplateName, String packageName) {
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("package", packageName);
		return javaSource(javaTemplateName, dataModel);
	}

	public static String javaSource(String javaTemplateName, Map<String, Object> dataModel) {
		return getInstance().getSource(javaTemplateName, dataModel);
	}

	public String getSource(String templateName) {
		return getSource(templateName, new HashMap<String, Object>());
	}

	public String getSource(String templateName, Map<String, Object> dataModel) {
		if (!templateName.endsWith(".ftl")) {
			templateName += ".ftl";
		}
		StringWriter result = new StringWriter();
		try {
			Template template = cfg.getTemplate(templateName);
			template.process(dataModel, result);
		} catch (IOException ioe) {
			throw new TemplateHandlerException("Cannot read template '" + templateName + "'", ioe);
		} catch (TemplateException te) {
			throw new TemplateHandlerException("Cannot read template '" + templateName + "'", te);
		}

		return result.toString();
	}
}
