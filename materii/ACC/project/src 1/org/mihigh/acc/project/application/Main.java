package org.mihigh.acc.project.application;

import java.util.ArrayList;
import java.util.List;

import org.mihigh.acc.project.commons.ActionExtention;
import org.mihigh.acc.project.communicator.communication.paxos.NetworkManager;
import org.mihigh.acc.project.communicator.communication.paxos.Node;
import org.mihigh.acc.project.network.Network;

public class Main {

  public static final int NODES_NR = 3;
//        public static final boolean USE_AUTO_TYPING = false;
  public static final boolean USE_AUTO_TYPING = true;

  public static final CommunicationType communicationType =
      CommunicationType.PAXOS;

  private static boolean usesProfInput = false;
//  private static boolean usesProfInput = true;

  public static void main(String[] args) throws InterruptedException {
    Network networkManager = getInstance();
    List<org.mihigh.acc.project.communicator.communication.Node> nodes = new ArrayList<org.mihigh.acc.project.communicator.communication.Node>(NODES_NR);

    ActionProvider actionProvider = new ActionProvider();

    for (int index = 0; index < NODES_NR; index++) {

      org.mihigh.acc.project.communicator.communication.Node node = getNode(actionProvider, index);
      networkManager.attach(node);
      nodes.add(node);
    }

    Thread.currentThread().sleep(200);

    for (org.mihigh.acc.project.communicator.communication.Node node : nodes) {
      new Thread(node).start();
    }

    new Thread(networkManager).start();
  }

  private static Network getInstance() {
    switch (communicationType) {
      case PAXOS:
        return NetworkManager.instance;

    }

    return null;

  }


  private static org.mihigh.acc.project.communicator.communication.Node getNode(ActionProvider actionProvider, int index) {
    switch (communicationType) {
      case PAXOS:
        return USE_AUTO_TYPING ?
               new Node(index, actionProvider.getActions(index, usesProfInput), NODES_NR, (NetworkManager) getInstance()) :
               new Node(index, new ArrayList<ActionExtention>(), NODES_NR, (NetworkManager) getInstance());
    }

    return null;
  }


}