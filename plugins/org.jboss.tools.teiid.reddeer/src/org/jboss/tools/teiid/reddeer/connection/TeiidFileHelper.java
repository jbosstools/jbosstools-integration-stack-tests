package org.jboss.tools.teiid.reddeer.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class TeiidFileHelper {

	/**
	 * Loads XML file from resources, parses it and returns as String.
	 * @param fileName - name of file in folder 'resources/expectedResults/'
	 * @param formatted - using false will remove every \r \n \t char
	 */
	public String getXmlExpectedResult(String fileName, boolean formatted) throws FileNotFoundException, TransformerException{	
		TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        
        fileName = (fileName.contains(".xml")) ? fileName : fileName + ".xml";
        String pathToFile = "resources/expectedResults/" + fileName;
        InputStream is = new FileInputStream(new File(pathToFile).getAbsolutePath());
        StreamSource source = new StreamSource(is);         
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);             
        transformer.transform(source, result);
       
        String expected = sw.getBuffer().toString();
        return (formatted) ? expected : expected.replaceAll("\r|\n|\t", "");  
	}
	
}
