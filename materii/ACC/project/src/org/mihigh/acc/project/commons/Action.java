package org.mihigh.acc.project.commons;

public class Action {

  protected EventType eventType;
  protected String text;
  public int poz;

  public Action(EventType eventType, String text, int poz) {
    this.eventType = eventType;
    this.text = text;
    this.poz = poz;
  }

  public EventType getEventType() {
    return eventType;
  }

  public String getText() {
    return text;
  }

  public int getPoz() {
    return poz;
  }

  public enum EventType {
    INSERT,
    DELETE;
  }


  @Override
  public String toString() {
    return "Action{" +
           "eventType=" + eventType +
           ", text='" + text + '\'' +
           ", poz=" + poz +
           '}';
  }
}

