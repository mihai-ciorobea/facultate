import javax.swing.*;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class AgentReceiver extends jade.core.Agent {

  private JFrame jFrame;


  protected void setup() {
    jFrame = new JFrame();
    jFrame.setBounds(200, 100, 100, 100);
    jFrame.setVisible(true);

    final JTextArea textArea = new JTextArea(getName());
    jFrame.add(textArea);

    addBehaviour(new Behaviour() {
      @Override
      public void action() {
        ACLMessage msg = receive();
        if (msg != null) {
          textArea.append(msg.getContent()+"\n");
        }
      }

      @Override
      public boolean done() {
        return false;
      }
    });
  }


}

