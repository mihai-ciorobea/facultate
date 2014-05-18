package org.mihigh.acc.project.communicator.communication.dopt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mihigh.acc.project.commons.Action;

public class Dopt {

  private DoptNetworkManager networkManager;
  private DoptNode doptNode;
  private int s = 0;

  private List<DoptMessage> messagesDelayed = new ArrayList<DoptMessage>();
  private List<DoptMessage> messagesDelivered = new ArrayList<DoptMessage>();

  public Dopt(DoptNetworkManager networkManager, DoptNode doptNode) {
    this.networkManager = networkManager;
    this.doptNode = doptNode;
  }

  public void receiveMessage(DoptMessage doptMessage) {
    messagesDelayed.add(doptMessage);
    boolean apply = true;
    while (apply) {
      apply = false;
      Iterator<DoptMessage> it = messagesDelayed.iterator();
      while (it.hasNext()) {
        if (handelMessage(it.next())) {
          apply = true;
          it.remove();
          break;
        }
      }
    }
  }

  private boolean handelMessage(DoptMessage doptMessage) {
    if (checkVT(doptMessage.getS())) {

      transformMessage(doptMessage);

      if (doptMessage.getAction().getEventType() == Action.EventType.INSERT) {
        doptNode.getDopt_ui().insert(doptMessage.getAction().getText(), doptMessage.getAction().getPoz());
      } else {
        doptNode.getDopt_ui().remove(doptMessage.getAction().getPoz());
      }
      s++;

      return true;
    }
    return false;
  }

  private void transformMessage(DoptMessage doptMessage) {

    int start = doptNode.getId() > doptMessage.getSourceId() ? doptMessage.getS() + 1 : doptMessage.getS();
    for(int i = start; i < messagesDelivered.size(); i++){
      modifyMessageIfNeed(doptMessage, messagesDelivered.get(i));
    }
  }

  private void modifyMessageIfNeed(DoptMessage receivedMsg, DoptMessage localMsg) {
    int posLocal = localMsg.getAction().getPoz();
    int posReceived = receivedMsg.getAction().getPoz();

    if ( posLocal <= posReceived ) {
      receivedMsg.getAction().poz += localMsg.getAction().getEventType() == Action.EventType.INSERT ? +1 : -1;
    }


  }

  private boolean checkVT(int receivedS) {
    if ( s  == receivedS ) {
      return true;
    }

    if ( receivedS < s ) {
      return true;
    }

    return false;
  }

  synchronized public void typedMessage(Action action) {

    DoptMessage doptMessage = new DoptMessage(doptNode.getId(), action, s);

    messagesDelivered.add(doptMessage);
    networkManager.broadcastMessage(doptMessage);
    s++;

  }


}
