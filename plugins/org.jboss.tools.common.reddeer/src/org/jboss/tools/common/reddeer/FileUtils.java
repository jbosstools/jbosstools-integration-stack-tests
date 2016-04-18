package org.jboss.tools.common.reddeer;

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
	 * @param from
	 *            a file system path to the 'from' directory
	 * @param to
	 *            a file system path to the destination
	 * @throws IOException
	 */
	public static void copyDirectory(String fromPath, String toPath) throws IOException {

		Path from = Paths.get(fromPath);
		Path to = Paths.get(toPath);
		CopyOption[] options = new CopyOption[] {
			StandardCopyOption.REPLACE_EXISTING,
			StandardCopyOption.COPY_ATTRIBUTES };
		Files.copy(from, to, options);
		for (File item : from.toFile().listFiles()) {
			if (item.isDirectory()) {
				copyDirectory(item.toString(), toPath + File.separator + item.getName());
			} else {
				Files.copy(Paths.get(item.toString()), Paths.get(toPath + File.separator + item.getName()), options);
			}
		}
	}

	/**
	 * Recursively deletes a given directory
	 * 
	 * @param dir
	 *            Directory to delete
	 * @return true - the directory was deleted, false - otherwise
	 */
	public static boolean deleteDir(File dir) {

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}
}
