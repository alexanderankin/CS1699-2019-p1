package cloudcmp.davidankin.project1;

public interface Query {
  /**
   * Returns standard out or null for performing this query
   */
  public String word(String input);

  /**
   * Throws if there was no query done.
   */
  public Output getWholeOutput() throws IllegalStateException;
}
