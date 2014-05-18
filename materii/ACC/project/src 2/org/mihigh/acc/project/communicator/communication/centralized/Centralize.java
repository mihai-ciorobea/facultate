package org.mihigh.acc.project.communicator.communication.centralized;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.communicator.communication.Message;
import org.mihigh.acc.project.communicator.communication.Node;
import org.mihigh.acc.project.communicator.communication.Protocol;
import org.mihigh.acc.project.network.Network;
import org.mihigh.acc.project.network.NetworkManager;

public class Centralize implements Protocol {

  private boolean isServer;
  private Network networkManager;
  private CentralizeNode centralizeNode;

  public Centralize(boolean isServer, Network networkManager, CentralizeNode centralizeNode) {
    this.isServer = isServer;
    this.networkManager = networkManager;
    this.centralizeNode = centralizeNode;
  }

  @Override
  public void receiveMessage(Message message) {
    if (isServer) {
      typedMessage(message.getAction());
    } else {
      deliverMessageToUI(message);
    }
  }

  @Override
  synchronized public void typedMessage(Action action) {
    Message message = new Message(-1, action, null);

    if (isServer) {
      deliverMessageToUI(message);
      for (Node node : ((NetworkManager)(networkManager)).Nodes) {
        if (node.getId() != centralizeNode.getId()) {
          node.receiveMessage(message);
        }
      }
    } else {
      ((NetworkManager)(networkManager)).Nodes.get(0).receiveMessage(message);
    }
  }



  private void deliverMessageToUI(Message message) {
    if (message.getAction().getEventType() == Action.EventType.INSERT) {
      centralizeNode.getABCAST_UI().insertAsDisplay(message.getAction().getText(), message.getAction().getPoz());
    } else {
      centralizeNode.getABCAST_UI().removeAsDisplay(message.getAction().getPoz());
    }
  }


}
