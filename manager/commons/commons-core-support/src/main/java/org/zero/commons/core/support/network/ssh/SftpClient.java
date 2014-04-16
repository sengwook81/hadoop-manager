package org.zero.commons.core.support.network.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zero.commons.core.support.progress.ProgressMonitor;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * SFtp 전송 클래스.
 * 
 * @author SengWook Jung
 * 
 */
public class SftpClient {
	protected static final Logger log = LoggerFactory.getLogger(SftpClient.class);
	SecureShellSession sshSession = null;

	public SftpClient(SecureShellSession session) {
		this.sshSession = session;
	}

	public static enum FTP_OP {
		upload, delete
	}

	public static class FtpFileInfo {
		private String path;
		private FTP_OP op;
		private File file;
		private ProgressMonitor monitor = null;

		public FtpFileInfo(String path, FTP_OP op, String fileName) {
			this(path, op, new File(fileName), null);
		}

		public FtpFileInfo(String path, FTP_OP op, File file) {
			this(path, op, file, null);
		}

		public FtpFileInfo(String path, FTP_OP op, File file, ProgressMonitor monitor) {
			super();
			this.path = path;
			this.op = op;
			this.file = file;
			this.monitor = monitor;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public FTP_OP getOp() {
			return op;
		}

		public void setOp(FTP_OP op) {
			this.op = op;
		}

		@Override
		public String toString() {
			return "FtpFileInfo [path=" + path + ", op=" + op + ", file=" + file + "]";
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public ProgressMonitor getMonitor() {
			return monitor;
		}

		public void setMonitor(ProgressMonitor monitor) {
			this.monitor = monitor;
		}
	}

	public void upload(String path, String f, ProgressMonitor monitor) throws JSchException, IOException {
		execute(new FtpFileInfo[] { new FtpFileInfo(path, FTP_OP.upload, new File(f), monitor) });
	}

	public void upload(String path, File f, ProgressMonitor monitor) throws JSchException, IOException {
		execute(new FtpFileInfo[] { new FtpFileInfo(path, FTP_OP.upload, f, monitor) });
	}

	public void delete(String path, String f) throws JSchException, IOException {
		execute(new FtpFileInfo[] { new FtpFileInfo(path, FTP_OP.delete, f) });
	}

	protected void execute(FtpFileInfo[] files) throws JSchException {
		Session session = sshSession.getSession();
		try {

			for (FtpFileInfo command : files) {

				ChannelSftp channelExec = (ChannelSftp) session.openChannel("sftp");
				String targetPath = "";
				String remoteFileName = "";

				channelExec.connect();
				if (command.getPath().startsWith("/")) {
					targetPath = command.getPath();
				}
				else if (command.getPath().startsWith("~"))
				{
					targetPath = command.getPath().replace("~", channelExec.getHome());
				}
				else {
					targetPath = channelExec.getHome() + "/" + command.getPath();
				}

				SftpATTRS directoryStat = null;
				try {
					directoryStat = channelExec.stat(targetPath);
				} catch (Exception e) {
					log.debug("SFTP DIRECTORY {} NOT FOUND", targetPath);
				}

				if (directoryStat == null) {
					try {
						log.debug("SFTP MKDIR {} ", targetPath);
						channelExec.mkdir(targetPath);
					} catch (Exception e) {
						throw new RuntimeException("SFTP Make Directory Exception", e);
					}

				}
				try {
					log.debug("{} SFTP Change Directory {}", sshSession.getHost(), targetPath);
					// channelExec.mkdir(command.getPath());
					channelExec.cd(targetPath);
				} catch (SftpException e) {
					e.printStackTrace();
					throw new RuntimeException("SFTP Change Directory Exception", e);
				}

				if (command.getOp() == FTP_OP.upload) {
					remoteFileName = command.getFile().getName();
					FileInputStream in = null;
					long fileSize = command.getFile().length();
					log.debug("Send File Size [{}]", fileSize);
					try {
						in = new FileInputStream(command.getFile());
						channelExec.put(in, remoteFileName, new SftpProgressMonitorWrap(fileSize, command.getMonitor()), ChannelSftp.OVERWRITE);
						if (log.isDebugEnabled()) {
							log.debug("{} SFTP UPLOAD FINISH {}", sshSession.getHost(), command);
						}
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					} catch (SftpException e) {
						throw new RuntimeException(e);
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

				} else if (command.getOp() == FTP_OP.delete) {

					remoteFileName = targetPath;
					if (remoteFileName.endsWith("/")) {
						remoteFileName = remoteFileName + command.getFile().getName();
					} else {
						remoteFileName = remoteFileName + "/" + command.getFile().getName();
					}

					try {
						channelExec.rm(remoteFileName);
					} catch (SftpException e) {
						throw new RuntimeException(e);
					}
				} else {
					throw new RuntimeException("Unkown File Operation Type");
				}

				if (log.isDebugEnabled()) {
					log.debug("{} \n SFtp Finish : {}", new Object[] { sshSession.getHost(), command });
				}
			}

		} catch (RuntimeException e) {
			throw e;
		} catch (SftpException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
