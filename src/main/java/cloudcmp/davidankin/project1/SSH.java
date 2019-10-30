package cloudcmp.davidankin.project1;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.InputStream;
import java.io.IOException;
import java.security.Security;

public class SSH {
  private final Logger LOGGER = LoggerFactory.getLogger(SSH.class);
  static {
    Security.insertProviderAt(new BouncyCastleProvider(), 1);
  }
  protected static class HardcodedUserInfo implements UserInfo {
    public String getPassphrase() {
      return null;
    }
    public String getPassword() {
      return System.getenv("ssh_password");
    }
    public boolean promptPassphrase(String message) { return true; }
    public boolean promptPassword(String message) { return true; }
    public boolean promptYesNo(String message) { return true; }
    public void showMessage(String message) { }
  }

  private String host = null;
  private String username = null;
  private String password = null;

  private JSch jsch = null;
  private Session session = null;

  public SSH() {
    jsch = new JSch();
  }

  public void connect(String host, String username) throws JSchException {
    session = jsch.getSession(username, host);
    session.setUserInfo(new HardcodedUserInfo());
    // session.setConfig("StrictHostKeyChecking", "no");
    session.connect(10 * 1000);
  }

  /**
   * @return the stdout
   */
  public String runRemote(String command) throws JSchException, IOException {
    if (session == null) return null;
    Channel channel = session.openChannel("exec");
    ((ChannelExec) channel).setCommand(command);
    channel.connect(3 * 1000);
    channel.setInputStream(null);

    StringBuilder sb = new StringBuilder();
    InputStream in = channel.getInputStream();
    byte[] tmp = new byte[1024];
    while (true) {
      while (in.available() > 0) {
        int i = in.read(tmp, 0, 1024);
        if (i < 0)
          break;
        String t = new String(tmp, 0, i);
        System.out.println(t);
        sb.append(t);
      }

      if (channel.isClosed()) {
        if (in.available() > 0)
          continue; 
        LOGGER.error("exit-status: " + channel.getExitStatus());
        break;
      }

      // try {
      //   Thread.sleep(1000);
      // } catch (Exception ee) { }
    }

    channel.disconnect();
    return sb.toString();
  }
}
