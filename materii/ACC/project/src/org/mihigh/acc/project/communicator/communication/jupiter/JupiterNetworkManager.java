package org.mihigh.acc.project.communicator.communication.jupiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.communicator.communication.Node;
import org.mihigh.acc.project.network.Network;


public class JupiterNetworkManager implements Network {

  public static JupiterNetworkManager instance = new JupiterNetworkManager();
  public final List<JupiterNode> nodes = new ArrayList<JupiterNode>();

  private JupiterNetworkManager() {
  }

  public void attach(Node node) {
    nodes.add((JupiterNode) node);
  }


  List<NetworkJupiterMessage> messages = Collections.synchronizedList(new ArrayList<NetworkJupiterMessage>());

  public void broadcastMessage(JupiterMessage message, int id) {
    messages.add(new NetworkJupiterMessage(message, id, true));
  }

  public void sendMsgToServer(JupiterMessage message, int id) {
    messages.add(new NetworkJupiterMessage(message, id, false));
  }

  @Override
  public void run() {
    while (true) {



      try {
        Thread.currentThread().sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (messages.size() == 0) {
        continue;
      }

      NetworkJupiterMessage jupiterMessage = messages.get(0);
      for (int index = 0; index < nodes.size(); ++index) {
        if (index == jupiterMessage.source) {
          continue;
        }

        if (index == 0 && jupiterMessage.isServer) {
          continue;
        }

        nodes.get(index).receiveMessage(new JupiterMessage(new Action(jupiterMessage.action.getEventType(),
                                                                      jupiterMessage.action.getText(),
                                                                      jupiterMessage.action.getPoz()),
                                                           jupiterMessage.myMsgs,
                                                           jupiterMessage.otherMsgs),
                                        jupiterMessage.source);
      }
      messages.remove(jupiterMessage);
    }
  }


  private class NetworkJupiterMessage extends JupiterMessage {

    public final int source;
    public final boolean isServer;

    public NetworkJupiterMessage(JupiterMessage jupiterMessage, int source, boolean isServer) {
      super(jupiterMessage.action, jupiterMessage.myMsgs, jupiterMessage.otherMsgs);
      this.source = source;
      this.isServer = isServer;
    }
  }
}
