package org.mihigh.acc.project.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.mihigh.acc.project.application.CommunicationType;
import org.mihigh.acc.project.application.Main;
import org.mihigh.acc.project.communicator.communication.Message;
import org.mihigh.acc.project.communicator.communication.Node;


public class NetworkManager implements Network {

  public static NetworkManager instance = new NetworkManager();
  public final List<Node> Nodes = new ArrayList<Node>();

  private NetworkManager() {
  }

  @Override
  public void attach(Node Node) {
    Nodes.add(Node);
  }


  List<Message> messages = Collections.synchronizedList(new ArrayList<Message>());

  @Override
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

      int msgId = Main.communicationType == CommunicationType.CENTRALIZED ?
                  0 : random.nextInt(messages.size());
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
