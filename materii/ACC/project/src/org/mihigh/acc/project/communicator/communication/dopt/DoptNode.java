package org.mihigh.acc.project.communicator.communication.dopt;

import java.util.List;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.commons.ActionExtention;
import org.mihigh.acc.project.communicator.communication.Node;

public class DoptNode implements Node {

  private final int id;
  private List<ActionExtention> actions;
  private final Dopt protocol;

  private Dopt_UI dopt_ui;


  public DoptNode(int id, List<ActionExtention> actions, int NODES_NR, DoptNetworkManager instance) {
    this.id = id;
    this.actions = actions;
    this.protocol = new Dopt(instance, this);
    this.dopt_ui = new Dopt_UI(id, this, new DoptEventListener(this));
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
        dopt_ui.insertReal(action.getText(), action.getPoz());
      } else {
        dopt_ui.removeReal(action.getPoz());
      }
    }
  }

  public void receiveMessage(DoptMessage doptMessage) {
    System.out.println(getId() + ": receiveMessage " + doptMessage);
    protocol.receiveMessage(doptMessage);
  }

  public Dopt getProtocol() {
    return protocol;
  }

  public Dopt_UI getDopt_ui() {
    return dopt_ui;
  }

  public int getId() {
    return id;
  }

}
