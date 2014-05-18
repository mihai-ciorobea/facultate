package org.mihigh.acc.project.communicator.communication.paxos;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.text.BadLocationException;

public class Ui {


  public final JTextArea textArea;
  public final int id;
  public final Node node;

  public Ui(int id, final Node node) {
    this.id = id;
    this.node = node;

    JFrame frame = new JFrame("Editor" + id);
    JPanel panel = new JPanel();
    textArea = new JTextArea(10, 20);
    textArea.setEditable(false);
    frame.add(panel);
    panel.add(textArea);

    frame.setBounds(100 + 250 * id, 100, 250, 400);

    JLabel insCarLabel = new JLabel("Char:");
    final JTextField insCar = new JTextField(20);
    JLabel possitionLabel = new JLabel("Poz:");
    final JTextField position = new JTextField(20);

    panel.add(insCarLabel);
    panel.add(insCar);
    panel.add(possitionLabel);
    panel.add(position);

    JButton send = new JButton("Send");
    JButton del = new JButton("Del");
    panel.add(send);
    panel.add(del);

    send.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String text = insCar.getText();
        int start = Integer.valueOf(position.getText());
        node.getProtocol().typedMessage(new org.mihigh.acc.project.commons.Action(
            org.mihigh.acc.project.commons.Action.Type.INSERT, text, start));
      }
    });

    del.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int start = Integer.valueOf(position.getText());
        node.getProtocol().typedMessage(new org.mihigh.acc.project.commons.Action(
            org.mihigh.acc.project.commons.Action.Type.DELETE, "", start));
      }
    });

    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }


  public void insertAsTyped(String text, int poz) {
    node.getProtocol().typedMessage(new org.mihigh.acc.project.commons.Action(
        org.mihigh.acc.project.commons.Action.Type.INSERT, text, poz));
  }

  public void insertAsDisplay(final String text, final int poz) {
    textArea.insert(text, poz);
  }

  public void removeAsTyped(int poz) {
    node.getProtocol().typedMessage(new org.mihigh.acc.project.commons.Action(
        org.mihigh.acc.project.commons.Action.Type.DELETE, "", poz));
  }

  public void removeAsDisplay(int poz) {
    try {
      textArea.getDocument().remove(poz, 1);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }


  public void isLeader(boolean isLeader) {
    if (isLeader) {
      textArea.setBackground(new Color(0,255, 105));
    } else {
      textArea.setBackground(new Color(255,255,255));
    }
  }

}
