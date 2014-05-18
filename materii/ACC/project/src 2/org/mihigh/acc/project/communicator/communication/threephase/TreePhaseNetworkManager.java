package org.mihigh.acc.project.communicator.communication.threephase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mihigh.acc.project.communicator.communication.Message;
import org.mihigh.acc.project.communicator.communication.Node;
import org.mihigh.acc.project.network.Network;

public class TreePhaseNetworkManager implements Network {

  public static final TreePhaseNetworkManager instance = new TreePhaseNetworkManager();
  public final List<ThreePhaseNode> nodes = new ArrayList<ThreePhaseNode>();
  List<ThreePhaseMessage> messages = Collections.synchronizedList(new ArrayList<ThreePhaseMessage>());

  private TreePhaseNetworkManager() {
  }

  @Override
  public void attach(Node node) {
    nodes.add((ThreePhaseNode) node);
  }

  @Override
  public void broadcastMessage(Message message) {
  }

  public void sendMessage(ThreePhaseMessage message) {
    messages.add(message);
  }


  @Override
  public void run() {
    while (true) {

      if (messages.size() == 0) {
        continue;
      }
      System.out.println(messages.size());

      try {
        Thread.currentThread().sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      ThreePhaseMessage message = messages.get(0);
      if (message.destinationId == -1) {
        for (int index = 0; index < nodes.size(); ++index) {
          if (index == message.revise.sourceId) {
            continue;
          }

          nodes.get(index).receiveMessage(message);
        }
      }

      if (message.destinationId != -1) {
        nodes.get(message.destinationId).receiveMessage(message);
      }

      messages.remove(message);
    }
  }
}

