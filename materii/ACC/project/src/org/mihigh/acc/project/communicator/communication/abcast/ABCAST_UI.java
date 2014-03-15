package org.mihigh.acc.project.communicator.communication.abcast;

import javax.swing.*;
import javax.swing.text.BadLocationException;

public class ABCAST_UI {

  public final JTextArea textArea;
  public final int id;
  public final ABCASTNode ABCASTNode;
  public final ABCASTEventListener listener;

  public ABCAST_UI(int id, ABCASTNode ABCASTNode, ABCASTEventListener listener) {
    this.id = id;
    this.ABCASTNode = ABCASTNode;
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


  public void insertAsTyped(String text, int poz) {
//    listener.realUpdate = false;
//      insertAsDisplay(text, poz);
  }

  public void insertAsDisplay(final String text, final int poz) {


//    try {
//      SwingUtilities.invokeAndWait(new Runnable() {
//        @Override
//        public void run() {
//          textArea.insertAsTyped(text, poz);
//        }
//      });
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    } catch (InvocationTargetException e) {
//      e.printStackTrace();
//    }
  }

  public void removeAsTyped(int poz) {
//    listener.realUpdate = false;
//    removeAsDisplay(poz);
  }

  public void removeAsDisplay(int poz) {
    try {
      textArea.getDocument().remove(poz, 1);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

}
