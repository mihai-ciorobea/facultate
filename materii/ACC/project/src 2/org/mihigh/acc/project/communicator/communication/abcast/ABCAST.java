package org.mihigh.acc.project.communicator.communication.abcast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.communicator.communication.Message;
import org.mihigh.acc.project.communicator.communication.Protocol;
import org.mihigh.acc.project.network.Network;

public class ABCAST implements Protocol {

  private Network networkManager;
  private ABCASTNode ABCASTNode;
  private final boolean isTokenHolder;
  private int[] VT;

  private int messageTimeStamp = -1;

  public ABCAST(Network networkManager, ABCASTNode ABCASTNode, boolean isTokenHolder, int nodeNr) {
    this.networkManager = networkManager;
    this.ABCASTNode = ABCASTNode;
    this.isTokenHolder = isTokenHolder;

    this.VT = new int[nodeNr];
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

  private List<MessageAbcst> messagesTokenHolder = new ArrayList<MessageAbcst>();

  private void receivedTokenHolder(MessageAbcst messageAbcst) {

//    deliverMessageToUI(messageAbcst);

    messagesTokenHolder.add(messageAbcst);


    boolean hasDeleted = true;
    while(hasDeleted){
      hasDeleted = false;
      Iterator<MessageAbcst> it = messagesTokenHolder.iterator();
      while (it.hasNext()) {
        MessageAbcst message = it.next();

        if (message.getVt()[message.getSourceId()] == VT[message.getSourceId()] + 1) {
            ++messageTimeStamp;
            networkManager.broadcastMessage(new MessageAbcst(messageTimeStamp, message.getAction()));

          ++VT[message.getSourceId()];
          deliverMessageToUI(message);
          it.remove();
          hasDeleted = true;
        }
      }
    }

    System.out.println("TOKEN: " + messagesTokenHolder);
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
        deliverMessageToUI(msg);
        it.remove();
        ++lastMsgDisplayed;
      }
    }


  }


  private void deliverMessageToUI(Message message) {
    if (message.getAction().getEventType() == Action.EventType.INSERT) {
      ABCASTNode.getABCAST_UI().insertAsDisplay(message.getAction().getText(), message.getAction().getPoz());
    } else {
      ABCASTNode.getABCAST_UI().removeAsDisplay(message.getAction().getPoz());
    }
  }


  @Override
  synchronized public void typedMessage(Action action) {
    if (isTokenHolder) {
      ++messageTimeStamp;
      MessageAbcst message = new MessageAbcst(messageTimeStamp, action);
      deliverMessageToUI(message);
      networkManager.broadcastMessage(message);
    } else {
      ++VT[ABCASTNode.getId()];
      int savedVT[] = new int[VT.length];
      System.arraycopy(VT, 0, savedVT, 0, VT.length);
      networkManager.broadcastMessage(new MessageAbcst(ABCASTNode.getId(), action, savedVT));
    }
  }

//  TODO: ADD VT for messages and deliver CBCAST

}
