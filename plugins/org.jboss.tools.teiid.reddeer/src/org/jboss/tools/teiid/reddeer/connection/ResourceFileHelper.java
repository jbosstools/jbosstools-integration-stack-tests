package org.jboss.tools.teiid.reddeer.connection;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class ResourceFileHelper {
	private Transformer transformer;
	
	public ResourceFileHelper(){
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads XML file from resources, parses it and returns his content.
	 * @param filePath - path of file in folder 'resources/xml/' (.../<FILE>)
	 */
	public String getXml(String filePath) {	
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		return parseXmlFile(filePath);
	}
	
	/**
	 * Loads XML file from resources, parses it and returns his content (without header). 
	 * @param filePath - path of file in folder 'resources/xml/' (.../<FILE>)
	 */
	public String getXmlNoHeader(String filePath) {	
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		return parseXmlFile(filePath);
	}
	
	private String parseXmlFile(String filePath){
		try {	
	        filePath = (filePath.contains(".xml")) ? filePath : filePath + ".xml";
	        filePath = "resources/xml/" + filePath;
	        InputStream is = new FileInputStream(new File(filePath).getAbsolutePath());
	        StreamSource source = new StreamSource(is);         
	        StringWriter sw = new StringWriter();
	        StreamResult result = new StreamResult(sw);             
	        transformer.transform(source, result);
	        return sw.getBuffer().toString().replaceAll("\r|\n|\t", "");  
		} catch (FileNotFoundException | TransformerException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Loads file with prepared SQL from resources and returns his content.
	 * @param filePath - path of file in folder 'resources/sql/' (.../<FILE>)
	 */
	public String getSql(String filePath) {
		try {
			filePath = (filePath.contains(".sql")) ? filePath : filePath + ".sql";
			filePath = "resources/sql/" + filePath;
			StringBuilder result = new StringBuilder();
			Scanner scanner = new Scanner(new File(filePath));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();
			return result.toString(); 
		} catch (FileNotFoundException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public String getFlatFile(String filePath){
		try {
			filePath = "resources/flat/" + filePath;
			StringBuilder result = new StringBuilder();
			Scanner scanner = new Scanner(new File(filePath));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}
			scanner.close();
			return result.toString(); 
		} catch (FileNotFoundException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Copies specified file from resources to specified server file (replace if existing).
	 */
	public void copyFileToServer(String resourcesFilePath, String serverFilePath) throws IOException{
		Path source = Paths.get(resourcesFilePath);
		Path target = Paths.get(serverFilePath);
		Files.copy(source, target, REPLACE_EXISTING);
	}
	
	/**
	 * Loads properties file.
	 * @param fileName - (resources/...)
	 */
	public Properties getProperties(String fileName) {
		try {
			Properties props = new Properties();
			props.load(new FileReader(fileName));
			return props;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Loads specified file.
	 */
	public String loadFileAsString(String fileName) {
		String result = "";
		File f = new File(fileName);
		try (BufferedReader in = new BufferedReader(new FileReader(f))) {
			;
			String line = null;

			while ((line = in.readLine()) != null) {
				result = result.concat(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
