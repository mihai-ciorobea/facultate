package org.mihigh.acc.project.communicator.communication.abcast;

import java.util.Arrays;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.communicator.communication.Message;

public class MessageAbcst extends Message {

  public int time = -1;

  public MessageAbcst(int time, Action action) {

    this.time = time;
    this.action = action;
  }

  public MessageAbcst(int sourceId, Action action, int VT[]) {
    super(sourceId, action, VT);
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
