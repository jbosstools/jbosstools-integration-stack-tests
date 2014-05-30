package org.jboss.tools.teiid.reddeer.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static List<File> find(String path, String matcher) {
		List<File> files = new ArrayList<File>();

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return files;

		for (File file : list) {
			if (file.getName().matches(matcher)) {
				files.add(file);
			}
			if (file.isDirectory()) {
				files.addAll(find(file.getAbsolutePath(), matcher));
			}
		}

		return files;
	}
}
