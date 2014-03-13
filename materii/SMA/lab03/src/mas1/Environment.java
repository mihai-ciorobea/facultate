package mas1;

import java.util.Set;

public interface Environment
{
	/**
	 * Generates the environment with the specified features.
	 * 
	 * @param allPositions
	 *            - a set of all existing positions in the environment.
	 * @param environmentJtiles
	 *            - a set of all positions with junk in them.
	 * @param environmentXtiles
	 *            - a set of all positions with obstacles.
	 */
	void initialize(Set<Position> allPositions, Set<Position> environmentJtiles,
			Set<Position> environmentXtiles);
	
	/**
	 * @return <code>true</code> if no J-tiles remain in the environment; <code>false</code>
	 *         otherwise.
	 */
	boolean isClean();
	
	/**
	 * Adds an agent to the environment. The environment places the agent in it, with the specified
	 * initial position and orientation.
	 * 
	 * @param agent
	 *            - the {@link Agent} instance, containing the agent's algorithm.
	 * @param initialPosition
	 *            - the initial position of the agent in the environment. It must not be an
	 *            obstacle.
	 * @param initialOrientation
	 *            - the initial orientation of the agent.
	 */
	void addAgent(Agent agent, Position initialPosition, Orientation initialOrientation);
	
	/**
	 * When the method is invoked, all agents should receive a perception of the environment and
	 * decide on an action to perform.
	 */
	void step();
	
	/**
	 * @return a String representation of the environment and the elements in it.
	 */
	String printToString();
}
