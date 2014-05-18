package org.mihigh.acc.project.communicator.communication.cbcast;

import java.util.List;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.commons.ActionExtention;
import org.mihigh.acc.project.communicator.communication.Message;
import org.mihigh.acc.project.communicator.communication.Node;
import org.mihigh.acc.project.communicator.communication.Protocol;
import org.mihigh.acc.project.network.Network;

public class CBCASTNode implements Node {

  private final int id;
  private List<ActionExtention> actions;
  private final Protocol protocol;

  private CBCAST_UI CBCASTUi;


  public CBCASTNode(int id, List<ActionExtention> actions, int NODES_NR, Network instance) {
    this.id = id;
    this.actions = actions;
    this.protocol = new CBCAST(instance, this, NODES_NR);
    this.CBCASTUi = new CBCAST_UI(id, this, new CBCASTEventListener(this));
  }

  @Override
  public void run() {
    for (ActionExtention action : actions) {
      try {
        Thread.currentThread().sleep(action.getDelay());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (action.getEventType() == Action.EventType.INSERT) {
        CBCASTUi.insertReal(action.getText(), action.getPoz());
      } else {
        CBCASTUi.removeReal(action.getPoz());
      }
    }
  }

  @Override
  /**
   * Receive a message from other node
   */
  public void receiveMessage(Message message) {
    System.out.println(getId() + ": receiveMessage " + message);
    protocol.receiveMessage(message);
  }

  public Protocol getProtocol() {
    return protocol;
  }

  public CBCAST_UI getCBCASTUi() {
    return CBCASTUi;
  }

  public int getId() {
    return id;
  }

}
