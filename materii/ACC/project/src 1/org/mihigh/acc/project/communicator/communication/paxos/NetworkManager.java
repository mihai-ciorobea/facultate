package org.mihigh.acc.project.communicator.communication.paxos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.mihigh.acc.project.network.Network;


public class NetworkManager implements Network {

  public static NetworkManager instance = new NetworkManager();
  public final List<Node> Nodes = new ArrayList<Node>();

  private NetworkManager() {
  }

  @Override
  public void attach(org.mihigh.acc.project.communicator.communication.Node Node) {
    Nodes.add((org.mihigh.acc.project.communicator.communication.paxos.Node) Node);
  }


  List<Message> messages = Collections.synchronizedList(new ArrayList<Message>());

  public void broadcastMessage(Message message) {
    messages.add(message);
  }


  Random random = new Random();

  @Override
  public void run() {
    while (true) {

      if (messages.size() == 0) {
        continue;
      }
      try {
        Thread.currentThread().sleep(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      int msgId = random.nextInt(messages.size());
      Message message = messages.get(msgId);
      for (int index = 0; index < Nodes.size(); ++index) {
        if (index == message.getSourceId()) {
          continue;
        }

        Nodes.get(index).receiveMessage(message);
      }
      messages.remove(message);


    }
  }
}
