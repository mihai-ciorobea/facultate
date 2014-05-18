package org.mihigh.acc.project.communicator.communication.jupiter;

import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import org.mihigh.acc.project.communicator.communication.Node;

public class Jupiter_UI {

  private final JTextArea textArea;
  private final int id;
  private final Node node;
  private final JupiterEventListener listener;

  public Jupiter_UI(int id, Node node, JupiterEventListener listener) {
    this.id = id;
    this.node = node;
    this.listener = listener;

    JFrame frame = new JFrame("Editor" + id);
    JPanel panel = new JPanel();
    textArea = new JTextArea(10, 20);
    frame.add(panel);
    panel.add(textArea);
    textArea.getDocument().addDocumentListener(this.listener);

    frame.setBounds(100 + 250 * id, 100, 250, 200);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void insert(String text, int poz) {
    listener.realUpdate = false;
    insertReal(text, poz);
  }

  public void insertReal(final String text, final int poz) {
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          textArea.insert(text, poz);
        }
      });
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public void remove(int poz) {
    listener.realUpdate = false;
    removeReal(poz);
  }

  public void removeReal(int poz) {
    try {
      textArea.getDocument().remove(poz, 1);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

}
