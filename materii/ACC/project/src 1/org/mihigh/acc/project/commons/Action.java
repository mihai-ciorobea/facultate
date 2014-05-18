package org.mihigh.acc.project.commons;

public class Action {

  public static int currentId = 0;
  public int id;
  public Type type;
  public String text;
  public int position;

  public Action(Type type, String text, int position) {
    this.id = ++currentId;
    this.type = type;
    this.text = text;
    this.position = position;
  }

  public enum Type {
    INSERT,
    DELETE;
  }
}

