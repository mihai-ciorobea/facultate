package org.mihigh.acc.project.communicator.communication.dopt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.mihigh.acc.project.communicator.communication.Node;
import org.mihigh.acc.project.network.Network;


public class DoptNetworkManager implements Network {

  public static DoptNetworkManager instance = new DoptNetworkManager();
  public final List<DoptNode> Nodes = new ArrayList<DoptNode>();

  private DoptNetworkManager() {
  }

  @Override
  public void attach(Node Node) {
    Nodes.add((DoptNode) Node);
  }


  List<DoptMessage> doptMessages = Collections.synchronizedList(new ArrayList<DoptMessage>());

  public void broadcastMessage(DoptMessage doptMessage) {
    doptMessages.add(doptMessage);
  }


  Random random = new Random();

  @Override
  public void run() {
    while (true) {

      if (doptMessages.size() == 0) {
        continue;
      }
      try {
        Thread.currentThread().sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      int msgId = random.nextInt(doptMessages.size());
      DoptMessage doptMessage = doptMessages.get(msgId);
      for (int index = 0; index < Nodes.size(); ++index) {
        if (index == doptMessage.getSourceId()) {
          continue;
        }

        Nodes.get(index).receiveMessage(doptMessage);
      }
      doptMessages.remove(doptMessage);


    }
  }
}
