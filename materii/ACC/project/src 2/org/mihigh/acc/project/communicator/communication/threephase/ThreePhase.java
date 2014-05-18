  package org.mihigh.acc.project.communicator.communication.threephase;

  import java.util.ArrayList;
  import java.util.Collections;
  import java.util.Comparator;
  import java.util.HashMap;
  import java.util.Iterator;

  import org.mihigh.acc.project.commons.Action;
  import org.mihigh.acc.project.communicator.communication.Message;
  import org.mihigh.acc.project.communicator.communication.Protocol;
  import org.mihigh.acc.project.network.Network;

  import static org.mihigh.acc.project.communicator.communication.threephase.ThreePhaseMessage.Type;

  public class ThreePhase implements Protocol, Runnable {

    private ThreePhaseNode node;
    private TreePhaseNetworkManager networkManager;
    private int clock = 0;

    public ThreePhase(ThreePhaseNode node, Network network) {
      this.node = node;
      this.networkManager = (TreePhaseNetworkManager) network;
    }


    int maxPriority = 0;

    ArrayList<ThreePhaseMessage> notDeliverableMsg = new ArrayList<ThreePhaseMessage>();

    @Override
    public void receiveMessage(Message message) {
      ThreePhaseMessage msg = (ThreePhaseMessage) message;

      switch (msg.type) {
        case REVISE:
          maxPriority = maxPriority + 1 < msg.revise.clock ? msg.revise.clock : maxPriority + 1;
          networkManager
              .sendMessage(new ThreePhaseMessage(Type.PROPOSED, msg.revise.msgId, maxPriority, msg.revise.sourceId));
          msg.revise.clock = maxPriority;
          msg.canDeliver = false;
          notDeliverableMsg.add(msg);
          break;
        case PROPOSED:
          waitingProposesMap.get(msg.proposed.msgId).add(msg.proposed.clock);
          checkFullProposals(msg.proposed.msgId);
          break;
        case FINAL:
          for (ThreePhaseMessage notDelMsg : notDeliverableMsg) {
            if (notDelMsg.revise.msgId == msg.revise.msgId) {
              notDelMsg.canDeliver = true;
            }
          }

          Collections.sort(notDeliverableMsg, new Comparator<ThreePhaseMessage>() {
            @Override
            public int compare(ThreePhaseMessage o1, ThreePhaseMessage o2) {
              return o1.revise.clock - o2.revise.clock;
            }
          });

          if (notDeliverableMsg.size() != 0) {
            if (notDeliverableMsg.get(0).revise.msgId == msg.revise.msgId) {
              deliverMessageToUI(msg);
              notDeliverableMsg.remove(0);
              Iterator<ThreePhaseMessage> it = notDeliverableMsg.iterator();
              while (it.hasNext()) {
                ThreePhaseMessage topMsg = it.next();
                if (topMsg.canDeliver) {
                  it.remove();
                  deliverMessageToUI(topMsg);
                  clock = Math.max(clock, topMsg.revise.clock) + 1;
                }
              }
            }
          }

      }


    }


    private void checkFullProposals(String msgId) {
      System.out.println(waitingProposesMap);

      ArrayList<Integer> times = waitingProposesMap.get(msgId);
      if (times.size() == node.nodeNr - 1) {
        System.out.println(node.getId() + ":" + "FULL");
        Integer temp_ts = Collections.max(times);

        ThreePhaseMessage msg = myMessages.get(msgId);
        msg.type = Type.FINAL;
        msg.revise.clock = temp_ts;
        networkManager.sendMessage(msg);
        receiveMessage(msg);
        clock = Math.max(clock, temp_ts);


      }
    }


    HashMap<String, ArrayList<Integer>> waitingProposesMap = new HashMap<String, ArrayList<Integer>>();
    HashMap<String, ThreePhaseMessage> myMessages = new HashMap<String, ThreePhaseMessage>();

    @Override
    synchronized public void typedMessage(Action action) {
      ++clock;
      String msgId = node.getId() + "" + clock;
  //    maxPriority = maxPriority > clock ? maxPriority : clock;
      ThreePhaseMessage msg = new ThreePhaseMessage(Type.REVISE, action, node.getId(), msgId, clock, -1);
      msg.canDeliver = false;
      networkManager.sendMessage(msg);
      myMessages.put(msgId, msg);
      notDeliverableMsg.add(msg);
      waitingProposesMap.put(msgId, new ArrayList<Integer>());
    }

    private void deliverMessageToUI(ThreePhaseMessage message) {
      System.out.println("TO UI : " + message);
      if (message.revise.action.getEventType() == Action.EventType.INSERT) {
        node.getABCAST_UI().insertAsDisplay(message.revise.action.getText(), message.revise.action.getPoz());
      } else {
        node.getABCAST_UI().removeAsDisplay(message.revise.action.getPoz());
      }
    }


    @Override
    public void run() {
      while (true) {
        sleep(100);
  //      for(String msgId : waitingProposesMap.put())
        //TODO
      }
    }

    private void sleep(int time) {
      try {
        Thread.sleep(time);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
