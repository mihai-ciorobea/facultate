package util;

public class Logger {

  private String prefix = "";

  public Logger() {
  }

  public void addToPrefix(String text) {
    prefix += text;
  }

  public void debug(String text) {
    System.out.println(prefix + ": " + text);
  }

  public void print(String text, boolean addPrefix) {
    System.out.println((addPrefix ? prefix+ ": " : "" ) + text);

  }
}
