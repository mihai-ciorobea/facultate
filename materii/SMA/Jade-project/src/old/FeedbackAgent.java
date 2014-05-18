//import jade.core.AID;
//import jade.core.Agent;
//import jade.core.ContainerID;
//import jade.core.behaviours.Behaviour;
//import jade.lang.acl.ACLMessage;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.Serializable;
//import java.util.Date;
//
///**
//* Created by baduna on 5/7/14.
//*/
//public class FeedbackAgent extends Agent implements Serializable  {
//  JFrame jFrame = new JFrame("Feedback");
//  JPanel jPanel = new JPanel();
//  JButton call = new JButton("Done");
//
//  @Override
//  public void setup() {
//    jFrame.setBounds(50, 50, 500, 500);
//    jPanel.setSize(500, 500);
//    jPanel.add(call);
//
//    final Agent agent = this;
//    call.addActionListener(new ActionListener() {
//
//      public void actionPerformed(ActionEvent e) {
//        System.out.println(agent.getLocalName() + " clicked done button");
//        doMove(new ContainerID(run.Main.MAIN_CONTAINER, null));
//      }
//    });
//
//
//    addBehaviour(new Behaviour() {
//      @Override
//      public void action() {
//        ACLMessage msg = receive();
//        if (msg != null) {
//          doMove(new ContainerID(msg.getContent(), null));
//        }
//      }
//
//      @Override
//      public boolean done() {
//        return false;
//      }
//
//
//
//    });
//
//    jFrame.add(jPanel);
//    jFrame.setVisible(false);
//  }
//
//  @Override
//  public void afterMove() {
//    //in the server
//    if (jFrame.isVisible()) {
//      jFrame.setVisible(false);
//
//      ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//      msg.addReceiver(new AID(run.Main.SERVER_AGENT_NAME, AID.ISLOCALNAME));
//      msg.setProtocol(run.Main.FEEDBACK_CALL_PROTOCOL);
//      msg.setLanguage("English");
//      msg.setContent("moved back in");
//      send(msg);
//
//    }
//    //in the client
//    else {
//      jFrame.setVisible(true);
//
//    }
//  }
//}