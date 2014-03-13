package gridworld;

import java.util.Set;

import mas1.AbstractEnvironment;
import mas1.Position;

public abstract class AbstractGridEnvironment extends AbstractEnvironment
{
	protected int	x0, x1, y0, y1;
	protected int	cellW	= 2;
	protected int	cellH	= 2;
	
	@Override
	public void initialize(Set<Position> allPositions, Set<Position> environmentJtiles,
			Set<Position> environmentXtiles)
	{
		super.initialize(allPositions, environmentJtiles, environmentXtiles);
		
		GridPosition pos = (GridPosition)allPositions.iterator().next();
		x0 = x1 = pos.positionX;
		y0 = y1 = pos.positionY;
		for(Position p : allPositions)
		{
			GridPosition gp = (GridPosition)p;
			if(gp.positionX < x0)
				x0 = gp.positionX;
			if(gp.positionX > x1)
				x1 = gp.positionX;
			if(gp.positionY < y0)
				y0 = gp.positionY;
			if(gp.positionY > y1)
				y1 = gp.positionY;
		}
	}
	
	@Override
	public String printToString()
	{
		// border top
		String res = "+";
		for(int i = x0; i <= x1; i++)
		{
			for(int k = 0; k < cellW; k++)
				res += "-";
			res += "+";
		}
		res += "\n";
		// for each line; line 0 is at the button, so it will be last
		for(int j = y1; j >= y0; j--)
		{
			res += "|";
			// for each cell (position)
			for(int i = x0; i <= x1; i++)
			{
				GridPosition pos = new GridPosition(i, j);
				String agentString = "";
				for(AgentData agent : agents)
					if(agent.position.equals(pos))
						agentString += agent.orientation.toString() + agent.agent.toString();
				// first cell row
				for(int k = 0; k < cellW; k++)
				{
					// is obstacle
					if(Xtiles.contains(pos))
						res += "X";
					// contains agent(s)
					else if(agentString.length() > 0)
					{
						if(cellW == 1)
						{
							if(agentString.length() > 1)
								// no room for all the string
								res += ".";
							else
								res += agentString;
						}
						else
						{ // insert as much of the agent string as possible
							res += agentString.substring(0, cellW);
							k += cellW - agentString.length() + 1;
						}
						if((agentString.length() < cellW) && (cellH < 2) && Jtiles.contains(pos))
						{
							// there is some room left to also represent the junk
							res += "~";
							k++;
						}
					}
					// junk tile
					else if((cellH < 2) && Jtiles.contains(pos))
						res += "~";
					// empty space
					else
						res += " ";
				}
				res += "|";
			}
			res += "\n";
			// second cell row
			res += "|";
			for(int i = x0; i <= x1; i++)
			{
				GridPosition pos = new GridPosition(i, j);
				for(int k = 0; k < cellW; k++)
					if(Xtiles.contains(pos))
						res += "X";
					else if((k == 0) && Jtiles.contains(pos))
						res += "~";
					else
						res += " ";
				res += "|";
			}
			res += "\n";
			// other cell rows
			for(int ky = 0; ky < cellH - 2; ky++)
			{
				res += "|";
				for(int i = x0; i <= x1; i++)
				{
					for(int k = 0; k < cellW; k++)
						res += Xtiles.contains(new GridPosition(i, j)) ? "X" : " ";
					res += "|";
				}
				res += "\n";
			}
			res += "+";
			for(int i = x0; i <= x1; i++)
			{
				for(int k = 0; k < cellW; k++)
					res += "-";
				res += "+";
			}
			res += "\n";
			
		}
		return res;
	}
}
