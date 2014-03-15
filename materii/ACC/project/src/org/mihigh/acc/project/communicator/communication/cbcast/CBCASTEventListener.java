package org.mihigh.acc.project.communicator.communication.cbcast;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.mihigh.acc.project.commons.Action;

public class CBCASTEventListener implements DocumentListener {

  private CBCASTNode CBCASTNode;
  public boolean realUpdate = true;

  public CBCASTEventListener(CBCASTNode CBCASTNode) {
    this.CBCASTNode = CBCASTNode;
  }

  public CBCASTEventListener() {

  }



  @Override
  public void insertUpdate(DocumentEvent e) {
    if (!realUpdate) {
      realUpdate = true;
      return;
    }

    try {
      int start = e.getOffset();
      String text = e.getDocument().getText(start, 1);
      CBCASTNode.getProtocol().typedMessage(new Action(Action.EventType.INSERT, text, start));
    } catch (BadLocationException e1) {
      e1.printStackTrace();
    }

  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    if (!realUpdate) {
      realUpdate = true;
      return;
    }

    CBCASTNode.getProtocol().typedMessage(new Action(Action.EventType.DELETE, null, e.getOffset()));
  }

  @Override
  public void changedUpdate(DocumentEvent e) {

  }
}
