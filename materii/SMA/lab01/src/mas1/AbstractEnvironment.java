package mas1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Partial implementation of the {@link Environment} interface.
 * 
 * @author Andrei Olaru
 */
public abstract class AbstractEnvironment implements Environment
{
	public static class AgentData
	{
		public Agent		agent;
		public Position		position;
		public Orientation	orientation;
		
		public AgentData(Agent linkedAgent, Position currentPosition, Orientation currentOrientation)
		{
			agent = linkedAgent;
			position = currentPosition;
			orientation = currentOrientation;
		}
	}
	
	protected Set<Position>		positions;
	protected Set<Position>		Jtiles;
	protected Set<Position>		Xtiles;
	protected List<AgentData>	agents	= new ArrayList<>();
	
	@Override
	public boolean isClean()
	{
		return Jtiles.isEmpty();
	}
	
	@Override
	public void initialize(Set<Position> allPositions, Set<Position> environmentJtiles,
			Set<Position> environmentXtiles)
	{
		positions = allPositions;
		Jtiles = environmentJtiles;
		Xtiles = environmentXtiles;
	}
	
	@Override
	public void addAgent(Agent agent, Position initialPosition, Orientation initialOrientation)
	{
		if(agent == null || initialPosition == null)
			throw new IllegalArgumentException("Null arguments not allowed.");
		if(Xtiles.contains(initialPosition))
			throw new IllegalArgumentException("Initial position is notfree.");
		agents.add(new AgentData(agent, initialPosition, initialOrientation));
	}
}
