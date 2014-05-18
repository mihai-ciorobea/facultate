package agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import agent.dto.AgentDetail;
import agent.dto.Alternative;
import agent.dto.TaskResult;
import agent.dto.TaskState;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SenderBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import run.Main;
import util.Logger;

import static agent.dto.TaskState.State.ACCEPTED;
import static agent.dto.TaskState.State.DECLINED;
import static agent.dto.TaskState.State.LEFT_OVER;
import static agent.dto.TaskState.State.NOT_ANALYZED;
import static agent.dto.TaskState.State.SEND_REQUEST;

public class Agent extends jade.core.Agent {

  private Logger logger = new Logger();
  private int id;
  private AgentDetail agentDetail;
  private ArrayList<TaskResult> tasks = new ArrayList<TaskResult>();
  private HashMap<Integer, ArrayList<Alternative>> alternatives = new HashMap<Integer, ArrayList<Alternative>>();

  private ArrayList<TaskState> taskStates;


  private int analyzedTasks = -1;
  private int budget = 0;

  @Override
  public void setup() {
    logger.addToPrefix(getLocalName());
    id = Integer.parseInt(getLocalName().replace("AP_", ""));
    agentDetail = Main.agentDetails.get(id);

    logger.print("Budget: " + agentDetail.getBudget() + "; capabilities:" + agentDetail.getCapabilities(), true);

    //Read tasks from Facilitator PHASE_1_AF_TO_AP_GIVE_TASK
    //and asks about each task    PHASE_1_AP_TO_AF_ASK_ABOUT_TASK
    addBehaviour(new Behaviour() {
      @Override
      public void action() {
        final ACLMessage msg = receive(MessageTemplate.MatchProtocol(Messages.Type.PHASE_1_AF_TO_AP_GIVE_TASK + ""));
        if (msg != null) {
          try {
            budget = agentDetail.getBudget();
            ArrayList<Integer> tasksList = (ArrayList<Integer>) msg.getContentObject();
            for (Integer capability : tasksList) {
              tasks.add(new TaskResult(capability, false, 0));
              requestAlternatives(capability);
            }

            analyzedTasks = 0;
            taskStates = new ArrayList<TaskState>(tasks.size());
            for (int index = 0; index < tasks.size(); index++) {
              taskStates.add(new TaskState(NOT_ANALYZED));
            }
          } catch (UnreadableException e) {
            e.printStackTrace();
          }
        }
      }

      @Override
      public boolean done() {
        return false;
      }
    });

    //Read tasks from Facilitator PHASE_1_AF_TO_AP_RESPONSE_ABOUT_TASK
    readAlternatives();

    //check each task
    addBehaviour(new TickerBehaviour(this, 100) {
      @Override
      protected void onTick() {
        if (taskStates == null) {
          return;
        }

        if (analyzedTasks != -1) {
          for (int index = 0; index < tasks.size(); ++index) {
            TaskResult task = tasks.get(index);
            TaskState taskState = taskStates.get(index);
            ArrayList<Alternative> alternativesForTask = alternatives.get(task.capability);

            if (task.isFinished
                || taskState.state == LEFT_OVER
                ) {
              continue;
            }

            //Waiting all the costs for all alternatives
            if (alternativesForTask == null) {
              return;
            }

            for (Alternative alternative : alternativesForTask) {
              if (alternative.cost == Alternative.NOT_SET) {
                return;
              }
            }

            Collections.sort(alternativesForTask, new Comparator<Alternative>() {
              @Override
              public int compare(Alternative o1, Alternative o2) {
                return o1.cost - o2.cost;
              }
            });

            if (checkIfCurrentAgentWantsToMakeTheTask(task, alternativesForTask)) {
              continue;
            }

            switch (taskState.state) {
              case NOT_ANALYZED:

                if (alternativesForTask.size() == 0) {
                  taskState.state = LEFT_OVER;
                  continue;
                }

                taskState.proposalIndex = 0;
                taskState.state = SEND_REQUEST;
                sendTaskRequest(alternativesForTask.get(taskState.proposalIndex).agent, task.capability, index);

                break;
              case ACCEPTED:
                task.isFinished = true;

                Integer cost = agentDetail.getCapabilities().get(task.capability);
                if (cost == null) {
                  task.profit = 0;
                } else {
                  if (budget - cost < 0) {
                    task.profit = 0;
                  } else {
                    task.profit = cost - alternativesForTask.get(taskState.proposalIndex).cost;
                  }
                }
                ++analyzedTasks;
                break;
              case DECLINED:
                ++taskState.proposalIndex;
                if (taskState.proposalIndex >= alternativesForTask.size()) {
                  taskState.state = LEFT_OVER;
                  ++analyzedTasks;
                } else {
                  taskState.state = SEND_REQUEST;
                  sendTaskRequest(alternativesForTask.get(taskState.proposalIndex).agent, task.capability, index);
                }
                break;
            }
          }
        }

        if (analyzedTasks == -1 || analyzedTasks != tasks.size()) {
          return;
        }

        //Send to Facilitator all the results
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("AF", false));
        msg.setProtocol(Messages.Type.PHASE_2_AP_TO_AF_RESULTS + "");
        try {
          msg.setContentObject(tasks);
        } catch (IOException e) {
          e.printStackTrace();
        }
        send(msg);

        //Remove done tasks
        Iterator<TaskResult> iterator = tasks.iterator();
        while (iterator.hasNext()) {
          if (iterator.next().isFinished) {
            iterator.remove();
          }
        }

        analyzedTasks = -1;

      }
    });

    readTaskRequests();
    readTaskResponses();

  }

  private void readTaskResponses() {
    addBehaviour(new Behaviour() {
      @Override
      public void action() {
        ACLMessage msg =
            receive(MessageTemplate.MatchProtocol(Messages.Type.PHASE_1_AP_TO_AP_OUTSOURCES_TASK_RESPONSE + ""));
        if (msg != null) {
          int taskId = Integer.parseInt(msg.getLanguage());
          boolean accepted = Boolean.parseBoolean(msg.getContent());
          taskStates.get(taskId).state = accepted ? ACCEPTED : DECLINED;
        }
      }

      @Override
      public boolean done() {
        return false;
      }
    });
  }

  private void readTaskRequests() {
    addBehaviour(new Behaviour() {
      @Override
      public void action() {
        ACLMessage msg =
            receive(MessageTemplate.MatchProtocol(Messages.Type.PHASE_1_AP_TO_AP_OUTSOURCES_TASK_REQUEST + ""));
        if (msg != null) {

          int capability = Integer.parseInt(msg.getContent());
          Integer cost = agentDetail.getCapabilities().get(capability);

          if (cost == null) {
            respondToOutsourcingRequest(capability, msg.getLanguage(), false, msg.getSender().getLocalName());
            return;
          }

          if (budget - cost < 0) {
            respondToOutsourcingRequest(capability, msg.getLanguage(), false, msg.getSender().getLocalName());
          } else {
            respondToOutsourcingRequest(capability, msg.getLanguage(), true, msg.getSender().getLocalName());
            budget -= cost;

          }

        }

      }

      @Override
      public boolean done() {
        return false;
      }
    });
  }

  private void respondToOutsourcingRequest(int capability, String taskId, boolean status, String agent) {
    logger.print((status ? "ACCEPTING" : "DECLINING ") + " outsource for task(capability:" + capability + ")", true);
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    msg.addReceiver(new AID(agent, false));
    msg.setProtocol(Messages.Type.PHASE_1_AP_TO_AP_OUTSOURCES_TASK_RESPONSE + "");
    msg.setLanguage(taskId);
    msg.setContent(status + "");
    addBehaviour(new SenderBehaviour(this, msg));
  }


  private void sendTaskRequest(int agent, int capability, int taskId) {
    logger.print(
        "sending OUTSOURCE request to " + getAgentNameFromId(agent) + " for task(capability:" + capability + ")", true);
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    msg.addReceiver(new AID(getAgentNameFromId(agent), false));
    msg.setProtocol(Messages.Type.PHASE_1_AP_TO_AP_OUTSOURCES_TASK_REQUEST + "");
    msg.setLanguage(taskId + "");
    msg.setContent(capability + "");
    addBehaviour(new SenderBehaviour(this, msg));
  }

  private boolean checkIfCurrentAgentWantsToMakeTheTask(TaskResult task, ArrayList<Alternative> alternativesForTask) {
    //TODO: bonus task

    Integer cost = agentDetail.getCapabilities().get(task.capability);
    if (cost != null && budget - cost >= 0) {
      //Task made by current Agent
      budget -= cost;
      task.isFinished = true;
      task.profit = 0;

      ++analyzedTasks;
      return true;
    }

    return false;
  }


  //Read tasks from Facilitator PHASE_1_AF_TO_AP_RESPONSE_ABOUT_TASK
  private void readAlternatives() {
    addBehaviour(new Behaviour() {
      @Override
      public void action() {
        ACLMessage msg =
            receive(MessageTemplate.MatchProtocol(Messages.Type.PHASE_1_AF_TO_AP_RESPONSE_ABOUT_TASK + ""));
        if (msg != null) {
          try {
            ArrayList<Integer> alternativeAgents = (ArrayList<Integer>) msg.getContentObject();
            int taskId = Integer.parseInt(msg.getLanguage());
            alternativeAgents.remove((Integer) id);

            ArrayList<Alternative> alternativesForTask = new ArrayList<Alternative>();
            for (Integer alternativeAgent : alternativeAgents) {
              logger.print("Adding AP_" + alternativeAgent + " as alternative for capability " + taskId, true);
              alternativesForTask.add(new Alternative(alternativeAgent, Alternative.NOT_SET));

              //Request cost for task 'taskId' to agent 'alternativeAgent'
              requestCostForTask(alternativeAgent, taskId);
            }

            alternatives.put(taskId, alternativesForTask);
          } catch (UnreadableException e) {
            e.printStackTrace();
          }
        }
      }

      @Override
      public boolean done() {
        return false;
      }
    });

    respondToCostForTaskRequests();
    readCostsForTaskResponses();
  }


  private void requestAlternatives(Integer capability) {
    if (alternatives.containsKey(capability)) {
      return;
    }

    ACLMessage msgResponse = new ACLMessage(ACLMessage.INFORM);
    msgResponse.addReceiver(new AID("AF", false));
    msgResponse.setLanguage(capability + "");
    msgResponse.setProtocol(Messages.Type.PHASE_1_AP_TO_AF_ASK_ABOUT_TASK + "");
    msgResponse.setContent(capability + "");
    send(msgResponse);
  }

  private void requestCostForTask(Integer alternativeAgent, int taskId) {
    ACLMessage msgResponse = new ACLMessage(ACLMessage.INFORM);
    msgResponse.addReceiver(new AID(getAgentNameFromId(alternativeAgent), false));
    msgResponse.setLanguage(taskId + "");
    msgResponse.setProtocol(Messages.Type.PHASE_1_AP_TO_AP_ASK_COST_FOR_TASK + "");
    msgResponse.setContent(taskId + "");
    send(msgResponse);
  }


  private void respondToCostForTaskRequests() {
    addBehaviour(new CyclicBehaviour(this) {

      @Override
      public void action() {
        ACLMessage msg = receive(MessageTemplate.MatchProtocol(Messages.Type.PHASE_1_AP_TO_AP_ASK_COST_FOR_TASK + ""));
        if (msg != null) {
          int taskId = Integer.parseInt(msg.getContent());
          int cost = agentDetail.getCapabilities().get(taskId);

          msg = msg.createReply();
          msg.setProtocol(Messages.Type.PHASE_1_AP_TO_AP_RESPONSE_COST_FOR_TASK + "");
          msg.setContent(cost + "");

          send(msg);
        }
      }
    });

  }


  private void readCostsForTaskResponses() {
    addBehaviour(new CyclicBehaviour(this) {
      @Override
      public void action() {
        ACLMessage
            msg =
            receive(MessageTemplate.MatchProtocol(Messages.Type.PHASE_1_AP_TO_AP_RESPONSE_COST_FOR_TASK + ""));
        if (msg != null) {
          int cost = Integer.parseInt(msg.getContent());
          int taskId = Integer.parseInt(msg.getLanguage());
          int agentId = Integer.parseInt(msg.getSender().getLocalName().replace("AP_", ""));

          ArrayList<Alternative> alternativesForTaskId = alternatives.get(taskId);
          for (Alternative alternative : alternativesForTaskId) {
            if (alternative.agent == agentId) {
              logger.print("Adding for capability:" + taskId + " (agentId:" + agentId + ",cost:" + cost + ")", true);
              alternative.cost = cost;
            }
          }
        }
      }
    });
  }


  private String getAgentNameFromId(int agentId) {
    return "AP_" + agentId;
  }

}
