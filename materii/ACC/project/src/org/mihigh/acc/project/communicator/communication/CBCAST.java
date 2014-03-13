package org.mihigh.acc.project.communicator.communication;

import java.util.ArrayList;
import java.util.List;

import org.mihigh.acc.project.communicator.Node;
import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.network.Message;
import org.mihigh.acc.project.network.NetworkManager;

public class CBCAST implements Protocol {

  private NetworkManager networkManager;
  private Node node;
  private int[] VT;

  private List<Message> messages = new ArrayList<Message>();

  public CBCAST(NetworkManager networkManager, Node node, int nodeNr) {
    this.networkManager = networkManager;
    this.node = node;
    this.VT = new int[nodeNr];
  }

  @Override
  public void receiveMessage(Message message) {
    System.out.println(node.getId() + " ==> " + message);
    messages.add(message);

    boolean apply = true;
    while (apply) {
      ITERATOR
      apply = false;
      for (int index = 0; index < messages.size(); ++index) {
        System.out.println(node.getId() + ": " + message + " === " + messages);
        if (handelMessage(messages.get(index))) {
          apply = true;
          break;
        }
      }
    }
  }

  private boolean handelMessage(Message message) {
    if (checkVT(message.getVt(), message.getSourceId())) {
      if (message.getAction().getEventType() == Action.EventType.INSERT) {
        node.getUi().insert(message.getAction().getText(), message.getAction().getPoz());
      } else {
        node.getUi().remove(message.getAction().getPoz());
      }
      VT[message.getSourceId()]++;

      messages.remove(message);
      return true;
    }
    return false;
  }

  private boolean checkVT(int[] receivedVT, int senderId) {
    for (int index = 0; index < receivedVT.length; ++index) {
      if (index == senderId) {
        if (receivedVT[index] != VT[index] + 1) {
          return false;
        }
      } else {
        if (receivedVT[index] > VT[index]) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  synchronized public void typedMessage(Action action) {
    VT[node.getId()]++;
    int savedVT[] = new int[VT.length];
    System.arraycopy( VT, 0, savedVT, 0, VT.length );
    networkManager.broadcastMessage(new Message(node.getId(), action, savedVT));
  }


}
