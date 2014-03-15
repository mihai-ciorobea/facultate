package org.mihigh.acc.project.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.mihigh.acc.project.communicator.communication.Message;
import org.mihigh.acc.project.communicator.communication.cbcast.Node;

public class NetworkManager implements Runnable {

  public static final NetworkManager instance = new NetworkManager();

  private final List<Node> Nodes = new ArrayList<Node>();

  private NetworkManager() {
  }

  public void attach(Node Node) {
    Nodes.add(Node);
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
      System.out.println(messages.size());

      try {
        Thread.currentThread().sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      Message message = messages.get(random.nextInt(messages.size()));
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
