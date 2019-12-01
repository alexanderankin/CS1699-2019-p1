package cloudcmp.davidankin.project1;

public class Output {
  public String standardOut;
  public String standardErr;
  public int code;

  @Override
  public String toString() {
    return "Command returned with code [" + code + "]";
  }
}
