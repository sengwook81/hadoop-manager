package org.zero.commons.core.support.file;

import java.io.File;
import java.io.IOException;

public class ZFileUtils {

	public static String createTempDirectory() throws IOException
	{
		File folder = File.createTempFile("temp", null);  
		folder.delete();  
		folder.mkdir();  
		return folder.getPath();
	}
}
