package org.mihigh.acc.project.communicator;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.mihigh.acc.project.commons.Action;

public class EventListener implements DocumentListener {

  private Node node;
  public boolean realUpdate = true;

  public EventListener(Node node) {
    this.node = node;
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
      node.getProtocol().typedMessage(new Action(Action.EventType.INSERT, text, start));
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

    node.getProtocol().typedMessage(new Action(Action.EventType.DELETE, null, e.getOffset()));
  }

  @Override
  public void changedUpdate(DocumentEvent e) {

  }
}
