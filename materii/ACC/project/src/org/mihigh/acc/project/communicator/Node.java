package org.mihigh.acc.project.communicator;

import java.util.List;

import org.mihigh.acc.project.application.Main;
import org.mihigh.acc.project.commons.ActionExtention;
import org.mihigh.acc.project.communicator.communication.Protocol;
import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.network.Message;

public class Node implements Runnable {

  private final int id;
  private List<ActionExtention> actions;
  private final Protocol protocol;

  private UI ui;


  public Node(int id, List<ActionExtention> actions) {
    this.id = id;
    this.actions = actions;
    this.protocol = Main.getComunicationType(this);
    this.ui = new UI(id, this);
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
        ui.insertReal(action.getText(), action.getPoz());
      } else {
        ui.removeReal(action.getPoz());
      }
    }
  }

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

  public UI getUi() {
    return ui;
  }

  public int getId() {
    return id;
  }

}
