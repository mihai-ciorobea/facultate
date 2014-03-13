package base;

/**
 * Interface that must be implemented by any class representing an agent environment.
 *
 * @author Andrei Olaru
 */
public interface Environment {

  /**
   * Adds an agent to the environment. The environment places the agent in it, with the specified initial position and
   * orientation.
   *
   * @param agent - the agent to add.
   */
  void addAgent(Agent agent);

  /**
   * When the method is invoked, all agents should receive a perception of the environment and decide on an action to
   * perform.
   *
   * @return <code>true</code> if all agents completed their goals.
   */
  boolean step();

  @Override
  public String toString();
}
