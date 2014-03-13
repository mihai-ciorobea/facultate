package mas1;

public interface Position
{
	Position getNeighborPosition(Orientation orientation);
	
	Position getNeighborPosition(Orientation orientation, RelativeOrientation relativeOrientation);
	
	boolean isNeighborStraight(Position neighbor);
	
	boolean isNeighbor(Position neighbor);
}
