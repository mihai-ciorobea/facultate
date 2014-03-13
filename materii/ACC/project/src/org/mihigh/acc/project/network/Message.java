package org.mihigh.acc.project.network;

import java.util.Arrays;

import org.mihigh.acc.project.commons.Action;

public class Message {

  private final int sourceId;
  private final Action action;
  private final int[] vt;

  public Message (int sourceId, Action action, int VT[]){
    this.sourceId = sourceId;
    this.action = action;
    vt = VT;
  }

  public int getSourceId() {
    return sourceId;
  }

  public Action getAction() {
    return action;
  }

  public int[] getVt() {
    return vt;
  }

  @Override
  public String toString() {
    return "Message{" +
           "sourceId=" + sourceId +
           ", action=" + action +
           ", vt=" + Arrays.toString(vt) +
           '}';
  }
}
