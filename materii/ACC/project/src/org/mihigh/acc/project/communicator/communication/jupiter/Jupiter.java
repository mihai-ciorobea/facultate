package org.mihigh.acc.project.communicator.communication.jupiter;

import java.util.ArrayList;
import java.util.Iterator;

import org.mihigh.acc.project.commons.Action;

public class Jupiter {

  private final int id;
  private JupiterNetworkManager networkManager;
  private JupiterNode jupiterNode;
  private int myMsgs = 0;
  private int otherMsgs = 0;
  private ArrayList<JupiterMessage> outgoing = new ArrayList<JupiterMessage>();


  public Jupiter(JupiterNetworkManager networkManager, JupiterNode jupiterNode) {
    this.networkManager = networkManager;
    this.jupiterNode = jupiterNode;
    this.id = jupiterNode.getId();
  }

  public void receiveMessage(JupiterMessage msg, int senderId) {

    msg = new JupiterMessage(msg.action, msg.myMsgs, msg.otherMsgs);
    System.out.println(id + ": from " +  senderId + " with content:" + msg);
    System.out.println(id + ": outgoing" + outgoing);



    Iterator<JupiterMessage> it = outgoing.iterator();
    while (it.hasNext()) {
      JupiterMessage m = it.next();
      if (m.myMsgs < msg.otherMsgs) {
        it.remove();
      }
    }



    System.out.println(id + ": outgoing" + outgoing);

    for (JupiterMessage outgoingMsg : outgoing) {
           updateMsg(msg, outgoingMsg);
    }

    System.out.println(id + ": msg after:" + msg);


    if(isServer()) {
      networkManager.broadcastMessage(msg, senderId);
    }
    applyMessage(msg);
    otherMsgs = otherMsgs + 1;

  }

  private void updateMsg(JupiterMessage msg, JupiterMessage outgoingMsg) {
    System.out.println(id + ": compare: " + msg + outgoingMsg);


    int posLocal = outgoingMsg.action.getPoz();
    int posReceived = msg.action.getPoz();

    if ( (isServer() && posLocal <= posReceived ) ) {
      msg.action.poz += outgoingMsg.action.getEventType() == Action.EventType.INSERT ? +1 : -1;
    }

    if  (!isServer() && posLocal <= posReceived ) {
      msg.action.poz += outgoingMsg.action.getEventType() == Action.EventType.INSERT ? -1 : +1;
      outgoingMsg.action.poz += outgoingMsg.action.getEventType() == Action.EventType.INSERT ? 1 : -1;

    }

  }

  private void applyMessage(JupiterMessage message) {


      if (message.action.getEventType() == Action.EventType.INSERT) {
        jupiterNode.getJupiter_ui().insert(message.action.getText(), message.action.getPoz());
      } else {
        jupiterNode.getJupiter_ui().remove(message.action.getPoz());
      }
  }

  synchronized public void typedMessage(Action action) {
    JupiterMessage message = new JupiterMessage(action, myMsgs, otherMsgs);

    if (isServer()) {
      networkManager.broadcastMessage(message, jupiterNode.getId());
    } else {
      networkManager.sendMsgToServer(message, jupiterNode.getId());
    }


    outgoing.add(message);
    myMsgs++;
  }

  private boolean isServer() {
    return jupiterNode.getId() == 0;
  }


}
