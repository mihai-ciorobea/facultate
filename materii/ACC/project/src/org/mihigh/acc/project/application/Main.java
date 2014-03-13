package org.mihigh.acc.project.application;

import java.util.ArrayList;
import java.util.List;

import org.mihigh.acc.project.communicator.Node;
import org.mihigh.acc.project.communicator.communication.CBCAST;
import org.mihigh.acc.project.communicator.communication.Protocol;
import org.mihigh.acc.project.network.NetworkManager;

public class Main {

  public static final int NODES_NR = 5;
  public static final CommunicationType communicationType = CommunicationType.CBCAST;

  public static void main(String[] args) throws InterruptedException {
    NetworkManager networkManager = NetworkManager.instance;
    List<Node> nodes = new ArrayList<Node>(NODES_NR);

    ActionProvider actionProvider = new ActionProvider();

    for (int index = 0; index < NODES_NR; index++) {

      Node node = new Node(index, actionProvider.getActions(index));
      networkManager.attach(node);
      nodes.add(node);
    }

    Thread.currentThread().sleep(200);

    for (Node node : nodes) {
      new Thread(node).start();
    }

    new Thread(networkManager).start();
  }

  public static Protocol getComunicationType(Node node) {
    switch (communicationType) {
      case CBCAST:
        return new CBCAST(NetworkManager.instance, node, NODES_NR);

    }

    return null;
  }

}
