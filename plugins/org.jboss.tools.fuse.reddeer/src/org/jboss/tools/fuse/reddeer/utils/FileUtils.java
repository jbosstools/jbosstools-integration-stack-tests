package org.jboss.tools.fuse.reddeer.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Facilitates manipulation with files
 * 
 * @author tsedmik
 */
public class FileUtils {

	/**
	 * Copy given directory, sub-directories and files
	 * 
	 * @param from a file system path to the 'from' directory
	 * @param to a file system path to the destination
	 * @throws IOException 
	 */
	public static void copyDirectory(String fromPath, String toPath) throws IOException {
		
		Path from = Paths.get(fromPath);
		Path to = Paths.get(toPath);
		CopyOption[] options = new CopyOption[] {StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES};
		Files.copy(from, to, options);
		for (File item : from.toFile().listFiles()) {
			if (item.isDirectory()) {
				copyDirectory(item.toString(), toPath + File.separator + item.getName());
			} else {
				Files.copy(Paths.get(item.toString()), Paths.get(toPath + File.separator + item.getName()), options);
			}
		}
	}
}
