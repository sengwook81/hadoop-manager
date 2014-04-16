package org.zero.command.runner.cmd;

import org.springframework.stereotype.Component;
import org.zero.commons.core.support.compress.ZCompressUtil;
import org.zero.commons.core.support.compress.ZCompressUtil.COMPRESSOR_TYPE;

@Component("decompress")
public class DeCompress implements CommandRunner {

	@Override
	public void RunCommand(String[] args) {
		if (args.length == 0) {
			System.out.println("USAGE srcFile targetPath");
		}
		String srcFile = args[0];
		String targetPath = args[1];

		if (srcFile.toLowerCase().endsWith("tar.gz")) {
			ZCompressUtil.decompress(srcFile, targetPath, COMPRESSOR_TYPE.TAR_GZ);
		} else if (srcFile.toLowerCase().endsWith("tar")) {
			ZCompressUtil.decompress(srcFile, targetPath, COMPRESSOR_TYPE.TAR);
		} else if (srcFile.toLowerCase().endsWith("zip")) {
			ZCompressUtil.decompress(srcFile, targetPath, COMPRESSOR_TYPE.ZIP);
		}
	}
}
