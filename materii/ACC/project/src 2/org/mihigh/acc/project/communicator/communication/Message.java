package org.mihigh.acc.project.communicator.communication;

import java.util.Arrays;

import org.mihigh.acc.project.commons.Action;

public class Message {

  protected int sourceId;
  protected Action action;
  protected int[] vt;

  public Message() {
  }

  public Message(int sourceId, Action action, int VT[]) {
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
