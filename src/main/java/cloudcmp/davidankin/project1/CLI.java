package cloudcmp.davidankin.project1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

public class CLI {
  private final static Logger logger = LoggerFactory.getLogger(CLI.class);
  public static void main(String[] args) {
    Util util = new Util();

    // SSH ssh = new SSH();
    // ssh.connect("138.197.74.229", "root", 22);
    // Output output = ssh.run("./script.sh king");
    // Output output = ssh.run("cat king_counts.txt");
    // System.out.println(output.toString());
    // System.out.println("Stdout");
    // System.out.println(output.standardOut);
    // System.out.println("Stderr");
    // System.out.println(output.standardErr);

    // String example = "[{\"Li9+ZmlsZQ==\":5}]\n" +
    //   "\n" + 
    //   "\n";
    // String extracted = util.extractBetweenBracket(example);
    // System.out.println(extracted);
    // JsonObject obj = util.tryParse(extracted);
    // System.out.println(obj);
    // JsonArray obj = util.tryParseArray(extracted);
    JsonArray obj = util.tryParseArray("[{\"Li9+ZmlsZQ==\":5}]");
    // JsonArray obj = new JsonArray("[{\"Li9+ZmlsZQ==\":4}]");
    JsonObject o = (JsonObject) obj.getValue(0);
    System.out.println(o.toString());

    String first = o.fieldNames().toArray()[0].toString();
    System.out.println(o.getValue(first));
  }
}
