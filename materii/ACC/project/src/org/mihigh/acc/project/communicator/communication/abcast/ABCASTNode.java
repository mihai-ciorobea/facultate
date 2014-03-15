package org.mihigh.acc.project.communicator.communication.abcast;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

import org.mihigh.acc.project.commons.Action;
import org.mihigh.acc.project.commons.ActionExtention;
import org.mihigh.acc.project.communicator.communication.Message;
import org.mihigh.acc.project.communicator.communication.Protocol;
import org.mihigh.acc.project.communicator.communication.cbcast.Node;
import org.mihigh.acc.project.network.NetworkManager;

public class ABCASTNode implements Node {

  private final int id;
  private List<ActionExtention> actions;
  private final Protocol protocol;

  private ABCAST_UI ABCAST_UI;


  public ABCASTNode(final int id, final List<ActionExtention> actions) {
    this.id = id;
    this.actions = actions;
    this.protocol = new ABCAST(NetworkManager.instance, this, id == 0);

    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_TYPED) {
          System.out.println(id + "" + (int)e.getKeyChar() + "" + ABCAST_UI.textArea.isFocusOwner() );
        }

        return false;
      }
    });

    this.ABCAST_UI = new ABCAST_UI(id, this, new ABCASTEventListener(this));
  }

  @Override
  public void run() {
    for (ActionExtention action : actions) {
      try {
        Thread.currentThread().sleep(action.getDelay());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (action.getEventType() == Action.EventType.INSERT) {
        ABCAST_UI.insertAsDisplay(action.getText(), action.getPoz());
      } else {
        ABCAST_UI.removeAsDisplay(action.getPoz());
      }
    }
  }

  /**
   * Receive a message from other node
   */
  public void receiveMessage(Message message) {
    System.out.println(getId() + ": receiveMessage " + message);
    protocol.receiveMessage(message);
  }

  public Protocol getProtocol() {
    return protocol;
  }

  public ABCAST_UI getABCAST_UI() {
    return ABCAST_UI;
  }

  public int getId() {
    return id;
  }

}
