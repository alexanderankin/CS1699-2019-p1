package cloudcmp.davidankin.project1;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.password.PasswordFinder;
import net.schmizz.sshj.userauth.password.Resource;
import net.schmizz.sshj.xfer.FileSystemFile;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.InputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.security.Security;

public class SSH {
  private final Logger LOGGER = LoggerFactory.getLogger(SSH.class);
  static {
    Security.insertProviderAt(new BouncyCastleProvider(), 1);
  }

  private String host;
  private String username;
  private PasswordFinder passwordFinder;

  public SSH () {
    this.passwordFinder = new PasswordFinder() {
      @Override
      public char[] reqPassword(Resource<?> resource) {
        return System.getenv("ssh_password").toCharArray();
      }

      @Override
      public boolean shouldRetry(Resource<?> resource) { return false; }
    };
  }

  public void connect(String host, String username) {
    this.host = host;
    this.username = username;
  }

  /**
   * @return Output (or null on failure)
   */
  public String run(String command) {
    SSHClient client = new SSHClient();
    client.addHostKeyVerifier(new PromiscuousVerifier());
    Session session = null;
    LOGGER.error("Connecting");
    try {
      client.connect(host);
      client.authPassword(this.username, this.passwordFinder);
      LOGGER.error("Connected Client");
      session = client.startSession();
      LOGGER.error("Connected Session");
      final Command cmd = session.exec(command);
      String output = IOUtils.readFully(cmd.getInputStream()).toString();
      LOGGER.error("Read InputStream fully");
      cmd.join();
      output += ("\n** exit status: " + cmd.getExitStatus());
      return output;
    } catch (Exception e) {
      LOGGER.error("Error with command " + command);
      LOGGER.error(e.toString());
      e.printStackTrace();
    } finally {
      try {
        if (session != null) {
          session.close();
        }
      } catch (IOException e) {
        LOGGER.error("Tried to close session on error, but was closed");
        LOGGER.error(e.toString());
      }

      try {
        client.disconnect();
      } catch (Exception e) {
        LOGGER.error("Tried to disconnect client on error, but was closed");
        LOGGER.error(e.toString());
      }
    }
    return null;
  }

  public boolean upload(String from, String to) {
    boolean error = false;
    SSHClient client = new SSHClient();
    client.addHostKeyVerifier(new PromiscuousVerifier());
    try {
      client.connect(host);
      client.authPassword(this.username, this.passwordFinder);
      LOGGER.error("Connected SSH");

      client.useCompression();
      client.newSCPFileTransfer().upload(new FileSystemFile(from), "~/" + from);
    } catch (Exception e) {
      LOGGER.error("Error uploading fr:" + from + " to:" + to + " on " + host);
      LOGGER.error(e.toString());
      e.printStackTrace();
      error = true;
    } finally {
      try {
        client.disconnect();
      } catch (Exception e) {
        LOGGER.error(e.toString());
        e.printStackTrace();
      }
    }

    return error;
  }
}
