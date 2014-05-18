package org.mihigh.acc.project.communicator.communication.paxos;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.mihigh.acc.project.commons.Action;

public class EventListener implements DocumentListener {

  private Node Node;
  public boolean realUpdate = true;

  public EventListener(Node Node) {
    this.Node = Node;
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
      Node.getProtocol().typedMessage(new Action(Action.Type.INSERT, text, start));
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

    Node.getProtocol().typedMessage(new Action(Action.Type.DELETE, null, e.getOffset()));
  }

  @Override
  public void changedUpdate(DocumentEvent e) {

  }
}
