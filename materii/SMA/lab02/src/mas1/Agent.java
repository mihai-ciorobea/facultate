package mas1;

import java.util.Map;

public interface Agent
{
	/**
	 * The method contains the agent's algorithm. Based on the received arguments -- it's
	 * perceptions -- the agent must decide upon an action to perform.
	 * 
	 * @param obstacles
	 *            - the neighboring obstacles, as a {@link Map} containing <code>true</code> if the
	 *            cell with the specified (relative) orientation contains an obstacle.
	 * @param isJtile
	 *            - <code>true</code> if the current cell contains junk.
	 * @param absoluteOrientation
	 *            - the absolute orientation of the agent.
	 * @return the {@link Action} to perform upon the environment.
	 */
	Action getAction(Map<RelativeOrientation, Boolean> obstacles, boolean isJtile);
    int getMemory();
	
}
