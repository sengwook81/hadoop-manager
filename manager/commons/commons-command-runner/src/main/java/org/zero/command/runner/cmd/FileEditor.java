package org.zero.command.runner.cmd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

@Component("editor")
public class FileEditor implements CommandRunner {

	private static final String CHECKER_COMMENT_S = "HINSTALLER_SWAP_START_CHECKER";
	private static final String CHECKER_COMMENT_F = "HINSTALLER_SWAP_FINISH_CHECKER";
	private static final String XML_COMMENT_S = "<!--";
	private static final String XML_COMMENT_F = "-->";

	private static final String SHELL_COMMENT_S = "#";
	private static final String SHELL_COMMENT_F = "#";

	private static final String PROP_COMMENT_S = "#";
	private static final String PROP_COMMENT_F = "#";

	@Override
	public void RunCommand(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("USAGE srcFile targetPath");
		}
		String srcFile = args[0];
		String targetPath = args[1];

		String commentStart = XML_COMMENT_S + CHECKER_COMMENT_S + XML_COMMENT_F;
		String commentFinish = XML_COMMENT_S + CHECKER_COMMENT_F + XML_COMMENT_F;

		File src = new File(srcFile);
		File target = new File(targetPath);
		StringBuffer appendCache = new StringBuffer();
		StringBuffer beforeCache = new StringBuffer();
		StringBuffer afterCache = new StringBuffer();
		int posFlag = 0;
		BufferedReader br = new BufferedReader(new FileReader(target));
		String line = null;
		while ((line = br.readLine()) != null) {

			if (line.startsWith(commentStart)) {
				posFlag = 1;
				continue;
			} else if (line.startsWith(commentFinish)) {
				posFlag = 2;
				continue;
			}
			switch (posFlag) {
			case 0:
				beforeCache.append(line + "\n");
				break;
			case 1:
				// Pass
				break;
			case 2:
				afterCache.append(line + "\n");
				break;
			}
		}
		br.close();

		// Src Text Read
		br = new BufferedReader(new FileReader(src));
		line = null;
		while ((line = br.readLine()) != null) {
			appendCache.append(line + "\n");
		}
		br.close();

		//
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd-24h-MM");
		String format = simpleDateFormat.format(Calendar.getInstance().getTime());
		File movedFile = new File(targetPath + "-" + format);
		int counter = 0;
		while (!movedFile.exists()) {
			movedFile = new File(targetPath + "-" + format + "(" + (++counter) + ")");
		}
		FileUtils.copyFile(target, movedFile);
		target.delete();

		File newFile = new File(targetPath);

		BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));

		bw.write(beforeCache.toString());
		bw.write(commentStart);
		bw.write(appendCache.toString());
		bw.write(commentFinish);
		bw.write(afterCache.toString());

		bw.close();
	}
}
