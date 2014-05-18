package org.mihigh.acc.project.application;

import java.util.ArrayList;
import java.util.List;

import org.mihigh.acc.project.commons.ActionExtention;
import org.mihigh.acc.project.communicator.communication.Node;
import org.mihigh.acc.project.communicator.communication.dopt.DoptNetworkManager;
import org.mihigh.acc.project.communicator.communication.dopt.DoptNode;
import org.mihigh.acc.project.communicator.communication.jupiter.JupiterNetworkManager;
import org.mihigh.acc.project.communicator.communication.jupiter.JupiterNode;
import org.mihigh.acc.project.network.Network;

public class Main {

  public static final int NODES_NR = 2;
//        public static final boolean USE_AUTO_TYPING = false;
  public static final boolean USE_AUTO_TYPING = true;

  public static final CommunicationType communicationType =
      CommunicationType.DOPT;
//      CommunicationType.JUPITER;

  private static boolean usesProfInput = false;
//  private static boolean usesProfInput = true;

  public static void main(String[] args) throws InterruptedException {
    Network networkManager = getInstance();
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

  private static Network getInstance() {
    switch (communicationType) {
      case DOPT:
        return DoptNetworkManager.instance;
      case JUPITER:
        return JupiterNetworkManager.instance;

    }

    return null;

  }


  private static Node getNode(ActionProvider actionProvider, int index) {
    switch (communicationType) {
      case DOPT:
        return USE_AUTO_TYPING ?
               new DoptNode(index, actionProvider.getActions(index, usesProfInput), NODES_NR, (DoptNetworkManager) getInstance()) :
               new DoptNode(index, new ArrayList<ActionExtention>(), NODES_NR, (DoptNetworkManager) getInstance());
      case JUPITER:
        return USE_AUTO_TYPING ?
               new JupiterNode(index, actionProvider.getActions(index, usesProfInput), NODES_NR,
                               (JupiterNetworkManager) getInstance()) :
               new JupiterNode(index, new ArrayList<ActionExtention>(), NODES_NR, (JupiterNetworkManager) getInstance());
    }

    return null;
  }


}