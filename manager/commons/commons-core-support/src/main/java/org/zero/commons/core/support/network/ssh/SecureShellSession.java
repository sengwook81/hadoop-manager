package org.zero.commons.core.support.network.ssh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SecureShellSession {

	private String host = null;
	private int port = 22;
	private String username = null;
	private String password = null;
	private int timeout = 1000;
	private boolean singleSession;
	private int keepAlive;
	private Session lastSession = null;
	
	public void copySession(SecureShellSession session) throws JSchException {
		if(lastSession != null ) {
			lastSession.disconnect();
		}
		lastSession = session.getSession();
	}
	
	protected static final Logger log = LoggerFactory
			.getLogger(SecureShellSession.class);


	public SecureShellSession() {
		this.keepAlive = 5000;
		this.singleSession = false;
	}
	
	public SecureShellSession(int keepAlive) 	
	{
		this.keepAlive = keepAlive;
		this.singleSession = true;
	}
	
	public SecureShellSession(SecureShellSession secureShell) 	
	{
		this.keepAlive = secureShell.keepAlive;
		this.singleSession = secureShell.singleSession;
	}
	
	public String getHost() {
		return host;
	}

	public SecureShellSession setHost(String host) {
		this.host = host;
		return this;
	}

	public int getPort() {
		return port;
	}

	public SecureShellSession setPort(int port) {
		this.port = port;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public SecureShellSession setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public SecureShellSession setPassword(String password) {
		this.password = password;
		return this;
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	Session getSession() throws JSchException 
	{
		if(singleSession && lastSession != null) {
			try {
				lastSession.sendKeepAliveMsg();
			} catch (Exception e) {
				log.debug("Single Session Closed Create New One : [{}]",e );
				lastSession.disconnect();
				lastSession = null;
				lastSession = getNewSession(false);
			}
			return lastSession;
		}
		else
		{
			return getNewSession(false);
		}
	}
	
	void closeSession() 
	{
		if(!singleSession) 
		{
			if(lastSession != null && lastSession.isConnected()) 
			{
				lastSession.disconnect();
				lastSession =  null;
				log.debug("Ssh DisConnect Success {}", getHost());
			}
		}
	}
	
	public void distory() 
	{
		if(lastSession != null && lastSession.isConnected()) 
		{
			lastSession.disconnect();
			lastSession =  null;
			log.debug("Ssh Distory Success {}", getHost());
		}
	}
	
	protected Session getNewSession(boolean isSingle) throws JSchException {

		if (host == null || username == null) {
			throw new RuntimeException("Host or UserName is Empty");
		}
		JSch jsch = new JSch();

		Session session = jsch.getSession(username, host, port);
		// 3. 패스워드를 설정한다.
		session.setPassword(password);

		java.util.Properties config = new java.util.Properties();
		// 4-1. 호스트 정보를 검사하지 않는다.
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);

		log.debug("Ssh Try To Connect {}", host);
		// SingleSession 일경우.
		if(isSingle) {
			session.setServerAliveInterval(30);
			session.setServerAliveCountMax(Integer.MAX_VALUE);
		}
		// 5. 접속한다.
		session.connect(getTimeout());
		lastSession = session;
		log.debug("Ssh Connect Success {}", host);

		return session;
	}
}
