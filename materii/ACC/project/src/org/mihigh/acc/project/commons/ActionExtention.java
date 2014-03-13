package org.mihigh.acc.project.commons;

public class ActionExtention extends Action{
  private final int delay;

  public ActionExtention(EventType eventType, String text, int poz, int delay) {
    super(eventType, text, poz);
    this.eventType = eventType;
    this.text = text;
    this.poz = poz;
    this.delay = delay;
  }

  public int getDelay() {
    return delay;
  }
}
