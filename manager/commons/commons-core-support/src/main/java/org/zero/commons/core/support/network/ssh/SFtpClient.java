package org.zero.commons.core.support.network.ssh;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zero.commons.core.support.progress.ProgressMonitor;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
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
class SFtpClient {
	protected static final Logger log = LoggerFactory.getLogger(SFtpClient.class);
	private Session session;

	public SFtpClient(Session session) {
		this.session = session;
	}

	public static enum FTP_OP {
		upload, delete
	}

	public static class FtpFileInfo {
		private String path;
		private FTP_OP op;
		private File file;
		
		private ProgressMonitor monitor = null;
		private String remoteName;

		public FtpFileInfo(String path, FTP_OP op, String file) {
			this(path, op, new File(file), null, null);
		}

		public FtpFileInfo(String path, FTP_OP op, File file) {
			this(path, op, file, null, null);
		}
		

		public FtpFileInfo(String path, FTP_OP op, File file, String remoteName ,ProgressMonitor monitor) {
			super();
			this.path = path;
			this.op = op;
			this.file = file;
			this.remoteName = remoteName;
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

		public String getRemoteName() {
			if(remoteName == null ) {
				return file.getName();
			}
			return remoteName;
		}

		public void setRemoteName(String remoteName) {
			this.remoteName = remoteName;
		}
	}
	

	public void upload(String path, String f, ProgressMonitor monitor) throws JSchException, IOException {
		execute(new FtpFileInfo[] { new FtpFileInfo(path, FTP_OP.upload, new File(f), null, monitor) });
	}

	public void upload(String path, File f, ProgressMonitor monitor) throws JSchException, IOException {
		execute(new FtpFileInfo[] { new FtpFileInfo(path, FTP_OP.upload, f, null, monitor) });
	}

	
	public void upload(String path, String f, String remoteFileName , ProgressMonitor monitor) throws JSchException, IOException {
		execute(new FtpFileInfo[] { new FtpFileInfo(path, FTP_OP.upload, new File(f), remoteFileName, monitor) });
	}
	
	public void upload(String path, File f, String remoteFileName , ProgressMonitor monitor) throws JSchException, IOException {
		execute(new FtpFileInfo[] { new FtpFileInfo(path, FTP_OP.upload, f, remoteFileName, monitor) });
	}
	
	public void delete(String path, String f) throws JSchException, IOException {
		execute(new FtpFileInfo[] { new FtpFileInfo(path, FTP_OP.delete, f) });
	}

	protected void execute(FtpFileInfo[] files) {
		try {
			        
			for (FtpFileInfo command : files) {

				ChannelSftp channel = null;
				try {
					channel = (ChannelSftp) session.openChannel("sftp");
					        
					if(channel.isConnected()) {
						System.out.println("Already Connected");
						channel.start();
					}
					else {
						System.out.println("SFtp Connect");
						channel.connect();
					}
				} catch (JSchException e1) {
					e1.printStackTrace();
					throw new RuntimeException("Open SFtp Channel Exception");
				}
				String targetPath = "";
				String remoteFileName = "";

				try {
					//channel.connect();
					if (command.getPath().startsWith("/")) {
						targetPath = command.getPath();
					} else if (command.getPath().startsWith("~")) {
						targetPath = command.getPath().replace("~", channel.getHome());
					} else {
						targetPath = channel.getHome() + "/" + command.getPath();
					}

					SftpATTRS directoryStat = null;
					try {
						directoryStat = channel.stat(targetPath);
					} catch (Exception e) {
						log.debug("SFTP DIRECTORY {} NOT FOUND", targetPath);
					}

					if (directoryStat == null) {
						try {
							log.debug("SFTP MKDIR {} ", targetPath);
							channel.mkdir(targetPath);
						} catch (Exception e) {
							throw new RuntimeException("SFTP Make Directory Exception", e);
						}

					}
					try {
						log.debug("{} SFTP Change Directory {}", session.getHost(), targetPath);
						channel.cd(targetPath);
					} catch (SftpException e) {
						e.printStackTrace();
						throw new RuntimeException("SFTP Change Directory Exception", e);
					}

					if (command.getOp() == FTP_OP.upload) {
						remoteFileName = command.getRemoteName();
						FileInputStream in = null;
						long fileSize = command.getFile().length();
						log.debug("Send File Size [{}]", fileSize);
						try {
							in = new FileInputStream(command.getFile());
							channel.put(in, remoteFileName, new SFtpProgressMonitorWrap(fileSize, command.getMonitor()), ChannelSftp.OVERWRITE);
							if (log.isDebugEnabled()) {
								log.debug("{} SFTP UPLOAD FINISH {}", session.getHost(), command);
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
							channel.rm(remoteFileName);
						} catch (SftpException e) {
							throw new RuntimeException(e);
						}
					} else {
						throw new RuntimeException("Unkown File Operation Type");
					}

					if (log.isDebugEnabled()) {
						log.debug("{} \n SFtp Finish : {}", new Object[] { session.getHost(), command });
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new RuntimeException("Sftp Operation Exception[" + e1.getMessage() + "]");
				} finally {
					if (channel.isConnected()) {
						channel.disconnect();
					}
				}
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
