package mas1;

public class Action
{
	public static enum ActionType {
		FORWARD,
		
		TURN_LEFT,
		
		TURN_RIGH,
		
		PICK,
	}
	
	ActionType	type;
	
	public Action(ActionType actionType)
	{
		type = actionType;
	}
	
	public ActionType getType()
	{
		return type;
	}
}
