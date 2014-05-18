package org.mihigh.acc.project.communicator.communication.jupiter;

import java.util.ArrayList;
import java.util.List;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.commons.ActionExtention;
import org.mihigh.acc.project.communicator.communication.Node;

public class JupiterNode implements Node {

  private final int id;
  private List<ActionExtention> actions;
  private final Jupiter protocol;

  private Jupiter_UI jupiter_ui;
  private ArrayList<JupiterMessage> msgList = new ArrayList<JupiterMessage>();
  private ArrayList<Integer> idList = new ArrayList<Integer>();


  public JupiterNode(int id, List<ActionExtention> actions, int NODES_NR, JupiterNetworkManager instance) {
    this.id = id;
    this.actions = actions;
    this.protocol = new Jupiter(instance, this);
    this.jupiter_ui = new Jupiter_UI(id, this, new JupiterEventListener(this));
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
        jupiter_ui.insertReal(action.getText(), action.getPoz());
      } else {
        jupiter_ui.removeReal(action.getPoz());
      }
    }

    int index = 0;

    while(true) {
      try {
        Thread.currentThread().sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if ( msgList.size() > index && idList.size() > index) {
        protocol.receiveMessage(msgList.get(index), idList.get(index));
        index++;
      }

    }
  }

  /**
   * Receive a message from other node
   */
  public void receiveMessage(JupiterMessage message, int id) {
    msgList.add(message);
    idList.add(id);

  }

  public Jupiter getProtocol() {
    return protocol;
  }

  public Jupiter_UI getJupiter_ui() {
    return jupiter_ui;
  }

  public int getId() {
    return id;
  }

}
