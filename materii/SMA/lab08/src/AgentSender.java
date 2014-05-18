import javax.swing.*;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class AgentSender extends jade.core.Agent {
  private JFrame jFrame;

  protected void setup() {
    jFrame = new JFrame();
    jFrame.setBounds(100, 100, 100, 100);
    jFrame.setVisible(true);

    jFrame.add(new JTextArea(getName()));

    addBehaviour(new Behaviour() {
      @Override
      public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID(Main.NICKNAME_3, AID.ISLOCALNAME));
        msg.setLanguage("English");
        msg.setContent("1 ");
        send(msg);
      }

      @Override
      public boolean done() {
        return false;
      }
    });
  }
}

