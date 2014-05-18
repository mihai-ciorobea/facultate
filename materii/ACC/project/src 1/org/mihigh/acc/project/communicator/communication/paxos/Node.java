package org.mihigh.acc.project.communicator.communication.paxos;

import java.util.ArrayList;
import java.util.List;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.commons.ActionExtention;

public class Node implements org.mihigh.acc.project.communicator.communication.Node {

  private final int id;
  private List<ActionExtention> actions;
  private final Paxos protocol;

  private Ui ui;
  private ArrayList<Message> receiveMessages = new ArrayList<Message>();


  public Node(int id, List<ActionExtention> actions, int NODES_NR, NetworkManager instance) {
    this.id = id;
    this.actions = actions;
    this.protocol = new Paxos(instance, this);
    this.ui = new Ui(id, this);//, new EventListener(this));
  }

  @Override
  public void run() {
    new Thread(protocol).start();

    for (ActionExtention action : actions) {
      try {
        Thread.currentThread().sleep(action.getDelay());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (action.type == Action.Type.INSERT) {
        ui.insertAsTyped(action.text, action.position);
      } else {
        ui.removeAsTyped(action.position);
      }
    }

    while (true) {
      ui.isLeader(Distinguished.isLeader(getId()));
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  synchronized public Message receiveMessage(Message message) {
    if ( message == null) {
      if (!receiveMessages.isEmpty())
        return receiveMessages.remove(0);
      return null;
    }

    System.out.println(getId() + ": receiveMessage " + message);
    receiveMessages.add(message);
    return null;
  }

  public Paxos getProtocol() {
    return protocol;
  }

  public Ui getUi() {
    return ui;
  }

  public int getId() {
    return id;
  }

}
