package org.mihigh.acc.project.application;

import java.util.ArrayList;
import java.util.List;

import org.mihigh.acc.project.commons.ActionExtention;
import org.mihigh.acc.project.communicator.communication.Node;
import org.mihigh.acc.project.communicator.communication.abcast.ABCASTNode;
import org.mihigh.acc.project.communicator.communication.cbcast.CBCASTNode;
import org.mihigh.acc.project.communicator.communication.centralized.CentralizeNode;
import org.mihigh.acc.project.communicator.communication.threephase.ThreePhaseNode;
import org.mihigh.acc.project.communicator.communication.threephase.TreePhaseNetworkManager;
import org.mihigh.acc.project.network.Network;
import org.mihigh.acc.project.network.NetworkManager;

public class Main {

  public static final int NODES_NR = 3;
//      public static final boolean USE_AUTO_TYPING = false;
  public static final boolean USE_AUTO_TYPING = true;

  public static final CommunicationType communicationType =
      CommunicationType.CBCAST;
//      CommunicationType.ABCAST;
//      CommunicationType.CENTRALIZED;
//      CommunicationType.THREE_PHASE;

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
      case CBCAST:
        return NetworkManager.instance;
      case ABCAST:
        return NetworkManager.instance;
      case CENTRALIZED:
        return NetworkManager.instance;
      case THREE_PHASE:
        return TreePhaseNetworkManager.instance;

    }

    return null;

  }


  private static Node getNode(ActionProvider actionProvider, int index) {
    switch (communicationType) {
      case CBCAST:
        return USE_AUTO_TYPING ?
               new CBCASTNode(index, actionProvider.getActions(index, usesProfInput), NODES_NR, getInstance()) :
               new CBCASTNode(index, new ArrayList<ActionExtention>(), NODES_NR, getInstance());
      case ABCAST:
        return USE_AUTO_TYPING ?
               new ABCASTNode(index, actionProvider.getActions(index, usesProfInput), NODES_NR, getInstance()) :
               new ABCASTNode(index, new ArrayList<ActionExtention>(), NODES_NR, getInstance());
      case CENTRALIZED:
        return USE_AUTO_TYPING ?
               new CentralizeNode(index, actionProvider.getActions(index, usesProfInput), NODES_NR, getInstance()) :
               new CentralizeNode(index, new ArrayList<ActionExtention>(), NODES_NR, getInstance());
      case THREE_PHASE:
        return USE_AUTO_TYPING ?
               new ThreePhaseNode(index, actionProvider.getActions(index, usesProfInput), NODES_NR, getInstance()) :
               new ThreePhaseNode(index, new ArrayList<ActionExtention>(), NODES_NR, getInstance());
    }

    return null;
  }


}