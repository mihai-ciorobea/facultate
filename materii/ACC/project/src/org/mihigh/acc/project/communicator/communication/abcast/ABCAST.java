package org.mihigh.acc.project.communicator.communication.abcast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.communicator.communication.Message;
import org.mihigh.acc.project.communicator.communication.Protocol;
import org.mihigh.acc.project.network.NetworkManager;

public class ABCAST implements Protocol {

  private NetworkManager networkManager;
  private ABCASTNode ABCASTNode;
  private final boolean isTokenHolder;

  private int messageTimeStamp = -1;

  public ABCAST(NetworkManager networkManager, ABCASTNode ABCASTNode, boolean isTokenHolder) {
    this.networkManager = networkManager;
    this.ABCASTNode = ABCASTNode;
    this.isTokenHolder = isTokenHolder;
  }

  @Override
  public void receiveMessage(Message message) {
    MessageAbcst messageAbcst = (MessageAbcst) message;

    if (isTokenHolder) {
      receivedTokenHolder(messageAbcst);
    } else {
      receivedNormal(messageAbcst);
    }
  }


  private void receivedTokenHolder(MessageAbcst messageAbcst) {
    handelMessage(messageAbcst);
    ++messageTimeStamp;
    networkManager.broadcastMessage(new MessageAbcst(messageTimeStamp, messageAbcst.getAction()));
  }


  int lastMsgDisplayed = -1;

  List<MessageAbcst> messages = new ArrayList<MessageAbcst>();

  private void receivedNormal(MessageAbcst messageAbcst) {
    if (messageAbcst.time != -1) {
      messages.add(messageAbcst);
    }

    Collections.sort(messages, new Comparator<MessageAbcst>() {
      @Override
      public int compare(MessageAbcst o1, MessageAbcst o2) {
        return o1.time - o2.time;
      }
    });

    Iterator<MessageAbcst> it = messages.iterator();
    while (it.hasNext()) {
      MessageAbcst msg = it.next();
      if (msg.time == lastMsgDisplayed + 1) {
        handelMessage(msg);
        it.remove();
        ++lastMsgDisplayed;
      }
    }


  }


  private void handelMessage(Message message) {
    if (message.getAction().getEventType() == Action.EventType.INSERT) {
      ABCASTNode.getABCAST_UI().insertAsTyped(message.getAction().getText(), message.getAction().getPoz());
    } else {
      ABCASTNode.getABCAST_UI().removeAsTyped(message.getAction().getPoz());
    }
  }


  @Override
  synchronized public void typedMessage(Action action) {
    if (isTokenHolder) {
      ++messageTimeStamp;
      networkManager.broadcastMessage(new MessageAbcst(messageTimeStamp, action));
    } else {
      networkManager.broadcastMessage(new MessageAbcst(ABCASTNode.getId(), action, null));
    }
  }


}
