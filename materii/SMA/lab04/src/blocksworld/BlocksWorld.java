package blocksworld;

import java.util.ArrayList;
import java.util.List;

import base.Agent;
import base.Environment;
import blocksworld.Blocks.Block;

/**
 * Blocks world environment.
 *
 * @author Andrei Olaru
 */
public class BlocksWorld implements Environment {

  private int stepNr = 0;

  public static class AgentData {

    /**
     * The agent reference.
     */
    Agent agent;

    /**
     * Block that the agent is currently holding. Use <code>null</code> for none.
     */
    Block holding = null;
    public boolean previusActionSucceeded = true;

    /**
     * Default constructor.
     *
     * @param linkedAgent - the agent.
     */
    public AgentData(Agent linkedAgent) {
      agent = linkedAgent;
    }
  }

  /**
   * Current state of the world.
   */
  public Blocks worldState;
  /**
   * List of agents in the system.
   */
  protected List<AgentData> agents = new ArrayList<>();

  /**
   * Constructor of the environment.
   *
   * @param world - initial state.
   */
  public BlocksWorld(Blocks world) {
    worldState = world;
  }

  @Override
  public void addAgent(Agent agent) {
    agents.add(new AgentData(agent));
  }


  List<Agent> finished = new ArrayList<>();

  @Override
  public boolean step() {
    for (AgentData agentData : agents) {
      if (finished.contains(agentData.agent)) {
        continue;
      }
      Action action = agentData.agent.perform(worldState, agentData.holding, agentData.previusActionSucceeded);
      System.out.println(agentData.agent + ": executes " + action);
      agentData.previusActionSucceeded = executeAction(agentData, action);
    }

    return agents.size() == finished.size();
  }

  private boolean executeAction(AgentData agentData, Action action) {
    switch (action.actionType) {
      case PICKUP:
        agentData.holding = worldState.pickup(action.block);
        return true;
      case PUTDOWN:
        worldState.putDown(action.block);
        agentData.holding = null;
        return true;
      case STACK:
        worldState.stack(action.over, action.under);
        agentData.holding = null;
        return true;
      case UNSTACK:
        agentData.holding = worldState.unstack(action.over, action.under);
        return true;
      case NONE:
        return true;
      case NULL:
        finished.add(agentData.agent);

    }
    return false;
  }

  @Override
  public String toString() {
    String ret = "";
    for (AgentData data : agents) {
      ret += "*" + data.agent.toString() + "  ";
    }
    ret += "\n";
    for (AgentData data : agents) {
      if (data.holding != null) {
        ret += "<" + data.holding.toString() + "> ";
      } else {
        ret += "<>  ";
      }
    }
    ret += "\n\n";
    ret += worldState.toString() + "" + "STEP: " + ++stepNr + "\n" + worldState.toPredicates().toString();
    return ret;
  }
}
