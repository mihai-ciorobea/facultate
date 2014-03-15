package org.mihigh.acc.project.communicator.communication.cbcast;

import java.util.ArrayList;
import java.util.List;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.communicator.communication.Message;
import org.mihigh.acc.project.communicator.communication.Protocol;
import org.mihigh.acc.project.network.NetworkManager;

public class CBCAST implements Protocol {

  private NetworkManager networkManager;
  private CBCASTNode CBCASTNode;
  private int[] VT;

  private List<Message> messages = new ArrayList<Message>();

  public CBCAST(NetworkManager networkManager, CBCASTNode CBCASTNode, int nodeNr) {
    this.networkManager = networkManager;
    this.CBCASTNode = CBCASTNode;
    this.VT = new int[nodeNr];
  }

  @Override
  public void receiveMessage(Message message) {
    System.out.println(CBCASTNode.getId() + " ==> " + message);
    messages.add(message);

    boolean apply = true;
    while (apply) {
      apply = false;
      //TODO: itertator
      for (int index = 0; index < messages.size(); ++index) {
        System.out.println(CBCASTNode.getId() + ": " + message + " === " + messages);
        if (handelMessage(messages.get(index))) {
          apply = true;
          messages.remove(message);
          break;
        }
      }
    }
  }

  private boolean handelMessage(Message message) {
    if (checkVT(message.getVt(), message.getSourceId())) {
      if (message.getAction().getEventType() == Action.EventType.INSERT) {
        CBCASTNode.getCBCASTUi().insert(message.getAction().getText(), message.getAction().getPoz());
      } else {
        CBCASTNode.getCBCASTUi().remove(message.getAction().getPoz());
      }
      VT[message.getSourceId()]++;

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
    VT[CBCASTNode.getId()]++;
    int savedVT[] = new int[VT.length];
    System.arraycopy( VT, 0, savedVT, 0, VT.length );
    networkManager.broadcastMessage(new Message(CBCASTNode.getId(), action, savedVT));
  }


}
