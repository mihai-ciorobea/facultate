package org.mihigh.acc.project.communicator.communication.jupiter;

import org.mihigh.acc.project.commons.Action;

public class JupiterMessage {

  public Action action;
  public final int myMsgs;
  public final int otherMsgs;

  public JupiterMessage(Action action, int myMsgs, int otherMsgs) {
    this.action = action;
    this.myMsgs = myMsgs;
    this.otherMsgs = otherMsgs;
  }

  @Override
  public String toString() {
    return "JupiterMessage{" +
           "action=" + action +
           ", myMsgs=" + myMsgs +
           ", otherMsgs=" + otherMsgs +
           '}';
  }
}
