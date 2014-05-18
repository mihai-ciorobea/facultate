//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Random;
//
//import javax.swing.*;
//
//import jade.core.AID;
//import jade.core.behaviours.Behaviour;
//import jade.core.behaviours.CyclicBehaviour;
//import jade.lang.acl.ACLMessage;
//import jade.lang.acl.MessageTemplate;
//import javafx.util.Pair;
//
//public class Agent extends jade.core.Agent {
//
//  public int own[];
//  public int wants[];
//
//  //(Agent,Resursa)
//  public ArrayList<Pair<String, Integer>> location = new ArrayList<Pair<String, Integer>>();
//
//  //(AgentIntersat, ResursaIntersata)
//  public ArrayList<Pair<String, Integer>> interests = new ArrayList<Pair<String, Integer>>();
//
//  private int resNr;
//  private JLabel ownLabel = new JLabel();
//  private JLabel wantsLabel = new JLabel();
//
//  @Override
//  public void setup() {
//
//    ui();
//    init();
//    randomInit();
//
//    addBehaviour(new Behaviour() {
//      @Override
//      public void action() {
//        ACLMessage msg = receive();
//        if (msg != null) {
//          msg.getContent();
//        }
//      }
//
//      @Override
//      public boolean done() {
//        return false;
//      }
//    });
//
////
////    addBehaviour(new WakerBehaviour(this, 100) {
////      @Override
////      protected void onWake() {
////        System.out.println("GIGI" + new Date().getTime());
////      }
////    });
////
//                     Ticker
//    // ADD Random
//    addBehaviour(new CyclicBehaviour() {
//      @Override
//      public void action() {
//        try {
//          Thread.sleep(3 * 1000);
//        } catch (interruptedexception e) {
//          e.printStackTrace();
//        }
//
//        int randomOwn = getRandomNumber(own);
//        int wantsOwn = getRandomNumber(wants);
//        System.out.println("Add to owns: " + randomOwn + " Add to wants: " + wantsOwn);
//        if (randomOwn != -1) {
//          own[randomOwn] = 1;
//        }
//        if (wantsOwn != -1) {
//          wants[wantsOwn] = 1;
//        }
//
//      }
//    });
//
//    final Agent agent = this;
//
//    addBehaviour(new CyclicBehaviour() {
//      @Override
//      public void action() {
//        for (int index = 0; index < wants.length; index++) {
//          if (wants[index] == 1) {
//            if (own[index] == 1) {
//              wants[index] = 2;
//            }
//
//            final String ownerForResource = getOwnerForResource(index, Agent.this.location);
//            final int finalIndex = index;
//            if (ownerForResource != null) {
//              //Stiu owner si cer => REQUEST
//              agent.addBehaviour(new Behaviour() {
//
//                @Override
//                public void action() {
//                  System.out.println(agent.getLocalName() + " REQUESTING for resource: " + finalIndex + " to agent: "
//                                     + ownerForResource);
//                  ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//                  msg.addReceiver(new AID(ownerForResource, false));
//                  msg.setProtocol("REQUEST");
//                  msg.setContent(finalIndex + "");
//                  send(msg);
//                }
//
//                @Override
//                public boolean done() {
//                  return true;
//                }
//              });
//            } else {
//              //NU stiu owner si get random => QUERY
//              final String agentName = run.Main.Ags[new Random().nextInt(run.Main.Ags.length)];
//              agent.addBehaviour(new Behaviour() {
//
//                @Override
//                public void action() {
//                  System.out.println(agent.getLocalName() + " QUERY for resource: " + finalIndex + " to agent: "
//                                     + agentName);
//                  ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//                  msg.addReceiver(new AID(agentName, false));
//                  msg.setProtocol("QUERY");
//                  msg.setContent(finalIndex + "");
//                  send(msg);
//                }
//
//                @Override
//                public boolean done() {
//                  return true;
//                }
//              });
//            }
//          }
//        }
//      }
//    });
//
//    addBehaviour(new Behaviour() {
//      @Override
//      public void action() {
//        ACLMessage msg = receive(MessageTemplate.MatchProtocol("QUERY"));
//        if (msg != null) {
//
//          final int resource = Integer.parseInt(msg.getContent());
//          if (own[resource] == 1) {
//
//          }
//          //Intorc resursa sau false
//          final ACLMessage finalMsg = msg;
//          agent.addBehaviour(new Behaviour() {
//
//            @Override
//            public void action() {
//              System.out.println(agent.getLocalName() + " intoarce INFORM");
//              ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
//              msg1.addReceiver(new AID(finalMsg.getSender().getLocalName(), false));
//              msg1.setProtocol("INFORM");
//              msg1.setContent(own[resource] == 1 ? resource + "" : "FALSE");
//              send(msg1);
//            }
//
//            @Override
//            public boolean done() {
//              return true;
//            }
//          });
//        }
//
//        msg = receive(MessageTemplate.MatchProtocol("REQUEST"));
//        if (msg != null) {
//          final int resource = Integer.parseInt(msg.getContent());
//          if (own[resource] == 1) {
//
//          }
//          //Intorc resursa sau false
//          agent.addBehaviour(new Behaviour() {
//
//            @Override
//            public void action() {
//              System.out.println(agent.getLocalName() + " intoarce INFORM");
//              ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//              msg.addReceiver(new AID(msg.getSender().getLocalName(), false));
//              msg.setProtocol("INFORM");
//              msg.setContent(own[resource] == 1 ? resource + "" : "FALSE");
//              send(msg);
//            }
//
//            @Override
//            public boolean done() {
//              return true;
//            }
//          });
//        }
//
//
//
//        msg = receive(MessageTemplate.MatchProtocol("REQUEST"));
//        if (msg != null) {
//          if (!msg.getContent().equals("FALSE")) {
//            int resource = Integer.parseInt(msg.getContent());
//            own[resource] = 1;
//            wants[resource] = 3;
//          }
//        }
//
//        }
//
//      @Override
//      public boolean done() {
//        return false;
//      }
//    });
//  }
//
//  private String getOwnerForResource(int index, ArrayList<Pair<String, Integer>> pairList) {
//    for (Pair<String, Integer> pair : pairList) {
//      if (pair.getValue() == index) {
//        return pair.getKey();
//      }
//    }
//
//    return null;
//  }
//
//  private void init() {
//    resNr = run.Main.Res.length;
//    own = new int[resNr];
//    wants = new int[resNr];
//  }
//
//  private void randomInit() {
//    own[getRandomNumber(own)] = 1;
//    own[getRandomNumber(own)] = 1;
//    wants[getRandomNumber(wants)] = 1;
//    wants[getRandomNumber(wants)] = 1;
//  }
//
//  int getRandomNumber(int[] a1) {
//    boolean isAllTrueA1 = true;
//    for (int i = 0; i < a1.length; i++) {
//      if (a1[i] == 0) {
//        isAllTrueA1 = false;
//      }
//    }
//
//    if (isAllTrueA1 == true) {
//      return -1;
//    }
//
//    Random random = new Random();
//    int randomNr = random.nextInt(resNr);
//    while (a1[randomNr] == 1) {
//      randomNr = random.nextInt(resNr);
//    }
//
//    return randomNr;
//  }
//
//
//  private void ui() {
//    JFrame jFrame = new JFrame("Agent1");
//    jFrame.setBounds(50, 50, 500, 500);
//    JPanel jPanel = new JPanel();
//    jPanel.setSize(500, 500);
//
//    jPanel.add(ownLabel);
//    jPanel.add(wantsLabel);
//    jFrame.add(jPanel);
//    jFrame.setVisible(true);
//
//    new Thread(new Runnable() {
//      @Override
//      public void run() {
//        while (true) {
//          try {
//            Thread.sleep(100);
//          } catch (InterruptedException e) {
//            e.printStackTrace();
//          }
//          ownLabel.setText("own: " + Arrays.toString(own));
//          wantsLabel.setText("wants: " + Arrays.toString(wants));
//        }
//      }
//    }).start();
//  }
//
//  @Override
//  protected void afterMove() {
//    super.afterMove();
//  }
//}