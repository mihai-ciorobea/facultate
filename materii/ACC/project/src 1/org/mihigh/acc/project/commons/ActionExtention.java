package org.mihigh.acc.project.commons;

public class ActionExtention extends Action{
  private final int delay;

  public ActionExtention(Type type, String text, int poz, int delay) {
    super(type, text, poz);
    this.type = type;
    this.text = text;
    this.position = poz;
    this.delay = delay;
  }

  public int getDelay() {
    return delay;
  }
}
