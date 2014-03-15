package org.mihigh.acc.project.communicator.communication.abcast;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.mihigh.acc.project.commons.Action;

public class ABCASTEventListener implements DocumentListener {

  private ABCASTNode ABCASTNode;
  boolean manuallyTyped = true;
  public ABCASTEventListener(ABCASTNode ABCASTNode) {
    this.ABCASTNode = ABCASTNode;
  }

  public ABCASTEventListener() {

  }



  @Override
  public void insertUpdate(DocumentEvent e) {
    if (manuallyTyped) {
      //removeAsTyped typed
      ABCASTNode.getABCAST_UI().removeAsDisplay(e.getOffset());


    }

    try {
      int start = e.getOffset();
      String text = e.getDocument().getText(start, 1);
      ABCASTNode.getProtocol().typedMessage(new Action(Action.EventType.INSERT, text, start));
    } catch (BadLocationException e1) {
      e1.printStackTrace();
    }

  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    if (manuallyTyped) {
      //add typed

    }

    ABCASTNode.getProtocol().typedMessage(new Action(Action.EventType.DELETE, null, e.getOffset()));
  }

  @Override
  public void changedUpdate(DocumentEvent e) {

  }
}
