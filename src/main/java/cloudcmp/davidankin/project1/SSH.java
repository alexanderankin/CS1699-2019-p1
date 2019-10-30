package cloudcmp.davidankin.project1;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.password.PasswordFinder;
import net.schmizz.sshj.userauth.password.Resource;
import net.schmizz.sshj.xfer.FileSystemFile;

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

  public boolean upload(String from, String to) {
    boolean error = false;
    SSHClient client = new SSHClient();
    client.addHostKeyVerifier(new PromiscuousVerifier());
    try {
      client.connect(host);
      client.authPassword(this.username, this.passwordFinder);
      LOGGER.error("Connected SSH");

      client.useCompression();
      client.newSCPFileTransfer().upload(new FileSystemFile(from), "/tmp/");
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
