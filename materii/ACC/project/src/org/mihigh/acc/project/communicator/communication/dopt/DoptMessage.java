package org.mihigh.acc.project.communicator.communication.dopt;

import org.mihigh.acc.project.commons.Action;

public class DoptMessage {

  protected int sourceId;
  protected Action action;
  protected int s;

  public DoptMessage(int sourceId, Action action, int s) {
    this.sourceId = sourceId;
    this.action = action;
    this.s = s;
  }

  public int getSourceId() {
    return sourceId;
  }

  public Action getAction() {
    return action;
  }

  public int getS() {
    return s;
  }

  @Override
  public String toString() {
    return "DoptMessage{" +
           "sourceId=" + sourceId +
           ", action=" + action +
           ", s= " + s +
           '}';
  }
}
