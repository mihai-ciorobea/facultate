package org.mihigh.acc.project.application;

import java.util.ArrayList;
import java.util.List;

import org.mihigh.acc.project.commons.ActionExtention;
import org.mihigh.acc.project.communicator.communication.abcast.ABCASTNode;
import org.mihigh.acc.project.communicator.communication.cbcast.CBCASTNode;
import org.mihigh.acc.project.communicator.communication.cbcast.Node;
import org.mihigh.acc.project.network.NetworkManager;

public class Main {

  public static final int NODES_NR = 2;
  public static final boolean USE_AUTO_TYPING = false;

  public static final CommunicationType communicationType =
//      CommunicationType.CBCAST;
      CommunicationType.ABCAST;

  public static void main(String[] args) throws InterruptedException {
    NetworkManager networkManager = NetworkManager.instance;
    List<Node> nodes = new ArrayList<Node>(NODES_NR);

    ActionProvider actionProvider = new ActionProvider();

    for (int index = 0; index < NODES_NR; index++) {

      Node Node = getNode(actionProvider, index);
      networkManager.attach(Node);
      nodes.add(Node);
    }

    Thread.currentThread().sleep(200);

    for (Node node : nodes) {
      new Thread(node).start();
    }

    new Thread(networkManager).start();
  }

  private static Node getNode(ActionProvider actionProvider, int index) {
    switch (communicationType) {
      case CBCAST:
        return USE_AUTO_TYPING ?
               new CBCASTNode(index, actionProvider.getActions(index), NODES_NR) :
               new CBCASTNode(index, new ArrayList<ActionExtention>(), NODES_NR);
      case ABCAST:
        return USE_AUTO_TYPING ?
               new ABCASTNode(index, actionProvider.getActions(index)) :
               new ABCASTNode(index, new ArrayList<ActionExtention>());
    }

    return null;
  }

}
