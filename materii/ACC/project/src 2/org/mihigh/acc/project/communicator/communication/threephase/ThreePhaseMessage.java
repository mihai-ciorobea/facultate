package org.mihigh.acc.project.communicator.communication.threephase;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.communicator.communication.Message;

public class ThreePhaseMessage extends Message {

  public Revise revise;
  public Proposed proposed;
  public Type type;
  public final int destinationId;
  public boolean canDeliver;

  public static enum Type {
    REVISE,
    PROPOSED,
    FINAL;
  }

  public ThreePhaseMessage(Type type, Action action, int sourceId, String msgId, int clock, int destinationId) {
    this.type = type;
    this.destinationId = destinationId;
    this.revise = new Revise(action, sourceId, msgId, clock);
  }

  public ThreePhaseMessage(Type type, String msgId, int clock, int destinationId) {
    this.type = type;
    this.destinationId = destinationId;
    this.proposed = new Proposed(clock, msgId);
  }

  @Override
  public String toString() {
    return "ThreePhaseMessage{" +
           "revise=" + revise +
           ", proposed=" + proposed +
           ", type=" + type +
           ", destinationId=" + destinationId +
           ", canDeliver=" + canDeliver +
           '}';
  }
}

class Proposed {
  public int clock;
  public String msgId;

  Proposed(int clock, String msgId) {
    this.clock = clock;
    this.msgId = msgId;
  }

  @Override
  public String toString() {
    return "Proposed{" +
           "clock=" + clock +
           ", msgId='" + msgId + '\'' +
           '}';
  }
}

class Revise {
  public int clock;
  public int sourceId;
  public Action action;
  public String msgId;

  public Revise(Action action, int sourceId, String msgId, int clock) {
    this.action = action;
    this.sourceId = sourceId;
    this.msgId = msgId;
    this.clock = clock;
  }

  @Override
  public String toString() {
    return "Revise{" +
           "clock=" + clock +
           ", sourceId=" + sourceId +
           ", action=" + action +
           ", msgId='" + msgId + '\'' +
           '}';
  }
}
