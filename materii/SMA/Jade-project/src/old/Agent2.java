//import javax.swing.*;
//
//import jade.core.Agent;
//import jade.core.behaviours.Behaviour;
//import jade.lang.acl.ACLMessage;
//
//public class Agent2 extends Agent {
//
//    @Override
//    public void setup() {
//        final JFrame jFrame = new JFrame("Agent2");
//        JPanel jPanel = new JPanel();
//        jFrame.setBounds(500,50,500,500);
//        final JTextArea jTextArea = new JTextArea(getName(), 10, 10);
//        jTextArea.setSize(500,500);
//        jPanel.add(jTextArea);
//        jFrame.add(jPanel);
//
//        jFrame.setVisible(true);
//
//        addBehaviour(new Behaviour() {
//            @Override
//            public void action() {
//                ACLMessage msg = receive();
//                if (msg != null) {
//                    jTextArea.append("\n" + msg.getContent());
//                }
//            }
//
//            @Override
//            public boolean done() {
//                return false;
//            }
//        });
//    }
//}