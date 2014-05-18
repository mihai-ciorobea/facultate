package base;

import blocksworld.Action;
import blocksworld.Blocks;

/**
 * Interface to be implemented by agents.
 *
 * @author Andrei Olaru
 */
public interface Agent {

  /**
   * Supplies the agent with perceptions and demands one action from the agent. The environment also specifies if the
   * previous action of the agent has succeeded or not.
   *
   *
   * @param holding
   * @param previousActionSucceeded - <code>true</code> if the previous action was successful (it has affected the
   *                                world); <code>false</code> otherwise (the action had no effect).
   * @return <code>true</code> if the agent has completed its goals.
   */
  //playGround este doar mediu de pe masa nu si ce tine fiecare / el in brat
  Action perform(Blocks playGround, Blocks.Block holding, boolean previousActionSucceeded);

  /**
   * @return a string that is printed at every cycle to show the status of the agent.
   */
  public String statusString();
}
