package cloudcmp.davidankin.project1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import io.vertx.core.json.Json;
// import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

import java.util.Base64;

public class Util {
  private final static Logger logger = LoggerFactory.getLogger(Util.class);
  public Util () {

  }

  public JsonArray tryParseArray(String input) {
    try {
      return new JsonArray(input);
    } catch (Exception e) {
      return null;
    }
  }

  public String extractBetweenBracket(String input) {
    if (input == null) {
      return input;
    }
    int start = input.indexOf("[");
    if (start == -1)
      return null;

    return input.substring(start, input.indexOf("]") + 1);
  }

  public String encode64(String input) {
    if (input == null)
      return input;

    return Base64.getEncoder().encodeToString(input.getBytes());
  }

  public String decode64(String input) {
    if (input == null)
      return input;

    return Base64.getDecoder().decode(input).toString();
  }
}
