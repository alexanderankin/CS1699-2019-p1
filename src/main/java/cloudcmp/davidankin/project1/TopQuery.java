package cloudcmp.davidankin.project1;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class TopQuery implements Query {
  private final Logger LOGGER = LoggerFactory.getLogger(SSH.class);

  private SSH server;
  private Output output;

  public TopQuery(SSH server) {
    this.server = server;
    output = null;
  }

  public String word(String input) {
    output = server.run("cat collectedResultsWC | sort -n -k2 -r | head -n " + input);
    if (output.code == 0) {
      return output.standardOut;
    } else {
      return null;
    }
  }

  public Output getWholeOutput() throws IllegalStateException {
    if (output == null)
      throw new IllegalStateException();
    return output;
  }
}
