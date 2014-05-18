//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Date;
//
//import javax.swing.*;
//
//import jade.core.AID;
//import jade.core.Agent;
//import jade.core.behaviours.Behaviour;
//import jade.lang.acl.ACLMessage;
//import jade.wrapper.ControllerException;
//
//public class Agent1 extends Agent {
//
//  @Override
//  public void setup() {
//    JFrame jFrame = new JFrame("Agent1");
//    jFrame.setBounds(50, 50, 500, 500);
//    JPanel jPanel = new JPanel();
//    jPanel.setSize(500, 500);
//
//    final JLabel label = new JLabel("No Text");
//    JButton call = new JButton("Call");
//    jPanel.add(label);
//    jPanel.add(call);
//
//
//    final Agent agent = this;
//    call.addActionListener(new ActionListener() {
//
//      public void actionPerformed(ActionEvent e) {
//        System.out.println(agent.getLocalName() + " clicked call button");
//
//        agent.addBehaviour(new Behaviour() {
//          @Override
//          public void action() {
//            System.out.println(agent.getLocalName() + " sending call button");
//            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//            msg.addReceiver(new AID("serverAgent", false));
//            try {
//              msg.setContent(agent.getContainerController().getContainerName());
//            } catch (ControllerException e1) {
//              e1.printStackTrace();
//            }
//            msg.setProtocol("clientCall");
//            send(msg);
//          }
//
//          @Override
//          public boolean done() {
//            return true;
//          }
//        });
//      }
//    });
//
//
//    addBehaviour(new Behaviour() {
//      @Override
//      public void action() {
//        ACLMessage msg = receive();
//        if (msg != null) {
//          label.setText(msg.getContent() + " " + new Date().getTime());
//        }
//      }
//
//      @Override
//      public boolean done() {
//        return false;
//      }
//    });
//
//    jFrame.add(jPanel);
//    jFrame.setVisible(true);
//  }
//
//  @Override
//  protected void afterMove() {
//    super.afterMove();
//  }
//}