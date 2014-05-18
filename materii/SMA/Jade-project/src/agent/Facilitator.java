package agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import agent.dto.AgentDetail;
import agent.dto.TaskResult;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import run.Main;
import util.Logger;

public class Facilitator extends jade.core.Agent {


  private static Logger logger = new Logger();
  private ArrayList<ArrayList<Integer>> cyclesTasks;
  private int agentsNr;
  private int cyclesNr;
  private double penalty;
  private HashMap<Integer, ArrayList<Integer>> agentsCapabilities;


  private int cycleId = -1;
  private boolean getToNextCycle = true;
  private boolean printNextPhase = false;
  private double cycleTotalProfit = 0;
  private int finishedAgents = 0;
  private double totalProfit = 0;

  @Override
  public void setup() {
    logger.addToPrefix(getLocalName());

    agentsNr = Main.agentsNr;

    cyclesNr = Main.cyclesNr;
    cyclesTasks = Main.cyclesDetails;

    penalty = Main.penalty;

    agentsCapabilities = new HashMap<Integer, ArrayList<Integer>>();
    for (int agentId = 0; agentId < Main.agentDetails.size(); ++agentId) {
      AgentDetail agentDetail = Main.agentDetails.get(agentId);
      for (int capability : agentDetail.getCapabilities().keySet()) {
        ArrayList<Integer> agents =
            agentsCapabilities.get(capability) == null ? new ArrayList<Integer>() : agentsCapabilities.get(capability);
        agents.add(agentId);
        agentsCapabilities.put(capability, agents);
      }
    }

    //Cycle behaviour
    addBehaviour(new TickerBehaviour(this, 1000) {
      @Override
      protected void onTick() {

        if (canGetToNextCycle()) {
          ++cycleId;
          if (checkIfFinished()) {
            return;
          }

          logger.print("", false);
          logger.print("Cycle " + cycleId + ", phase 1:", true);
          giveTasksToAgentsPhase1();
          printNextPhase = true;
          return;
        }
      }

      private boolean checkIfFinished() {
        //No more cycles
        if (cycleId == cyclesNr) {
          this.stop();
          return true;
        }
        return false;
      }

      private boolean canGetToNextCycle() {
        return getToNextCycle;
      }
    });

    // Read msg from Agents:  PHASE_1_AP_TO_AF_ASK_ABOUT_TASK
    // and respond with       PHASE_1_AF_TO_AP_RESPONSE_ABOUT_TASK
    addBehaviour(new Behaviour() {
      @Override
      public void action() {

        ACLMessage msg = receive(MessageTemplate.MatchProtocol(Messages.Type.PHASE_1_AP_TO_AF_ASK_ABOUT_TASK + ""));
        if (msg != null) {
          try {
            int capabilityId = Integer.parseInt(msg.getContent());
            ACLMessage msgResponse = new ACLMessage(ACLMessage.INFORM);
            msgResponse.addReceiver(new AID(msg.getSender().getLocalName(), false));
            msgResponse.setProtocol(Messages.Type.PHASE_1_AF_TO_AP_RESPONSE_ABOUT_TASK + "");
            msgResponse.setLanguage(msg.getLanguage());
            msgResponse.setContentObject(agentsCapabilities.get(capabilityId));
            send(msgResponse);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

      }

      @Override
      public boolean done() {
        return false;
      }
    });

    //Read results for each task
    addBehaviour(new Behaviour() {
      @Override
      public void action() {
        ACLMessage msg = receive(MessageTemplate.MatchProtocol(Messages.Type.PHASE_2_AP_TO_AF_RESULTS + ""));
        if (msg != null) {
          printMessageForNextPhase();

          try {
            ArrayList<TaskResult> taskResults = (ArrayList<TaskResult>) msg.getContentObject();

            for (TaskResult taskResult : taskResults) {
              logger.print("Result: " + msg.getSender().getLocalName() + " - "
                           + "capability" + taskResult.capability + " \tfinished:" + taskResult.isFinished
                           + "\tprofit:" + taskResult.profit, true);
              cycleTotalProfit += taskResult.isFinished ? taskResult.profit : -penalty;

            }
            ++finishedAgents;
          } catch (UnreadableException e) {
            e.printStackTrace();
          }

          if (finishedAgents == agentsNr) {
            totalProfit += cycleTotalProfit;
            System.out.println("Total profit: " + totalProfit + " (" + cycleTotalProfit + " this cycle);");
            getToNextCycle = true;
            cycleTotalProfit = 0;
            finishedAgents = 0;
          }
        }


      }

      @Override
      public boolean done() {
        return false;
      }
    });

  }

  private void printMessageForNextPhase() {
    if (printNextPhase == true) {
      logger.print("", false);
      logger.print("Cycle " + cycleId + ", phase 2:", true);

      printNextPhase = false;
    }
  }

  //Sends tasks to agents:    PHASE_1_AF_TO_AP_GIVE_TASK
  private void giveTasksToAgentsPhase1() {
    getToNextCycle = false;
    Random random = new Random();

    ArrayList<ArrayList<Integer>> agentsTasks = new ArrayList<ArrayList<Integer>>(agentsNr);
    for (int index = 0; index < agentsNr; ++index) {
      agentsTasks.add(new ArrayList<Integer>());
    }

    for (final int capability : this.cyclesTasks.get(cycleId)) {
      //TODO: (optional) add heuristic for task giving
      final int agentId = random.nextInt(agentsNr);

      logger.print(getLocalName() + " -> " + getAgentNameFromId(agentId) + ": task(capability " + capability + ")",
                   false);

      agentsTasks.get(agentId).add(capability);

    }

    for (int agentId = 0; agentId < agentsNr; ++agentId) {
      final ArrayList<Integer> tasks = agentsTasks.get(agentId);

      final int finalAgentId = agentId;
      addBehaviour(new WakerBehaviour(this, 0) {
        @Override
        protected void onWake() {
          ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
          msg.addReceiver(new AID(getAgentNameFromId(finalAgentId), false));
          msg.setProtocol(Messages.Type.PHASE_1_AF_TO_AP_GIVE_TASK + "");
          try {
            msg.setContentObject(tasks);
          } catch (IOException e) {
            e.printStackTrace();
          }
          send(msg);
        }
      });
    }


  }

  private String getAgentNameFromId(int agentId) {
    return "AP_" + agentId;
  }


}
