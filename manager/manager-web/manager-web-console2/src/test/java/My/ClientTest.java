package My;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ClientTest {

	public static void main(String[] args) throws Exception {
		SSHClient client = new SSHClient();
		String rslt = "";
		rslt = client.sendShell("whoami");
		
		rslt = client.sendShell("ls -alt");
		
		rslt = client.sendShell("cd ~/zhome/conf");
		
		rslt = client.sendShell("pwd");
		
		client.disconnect();
		
	}
}
