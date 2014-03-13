package blocksworld;

public class Action {


  public ActionType actionType;

  public Blocks.Block over;
  public Blocks.Block under;

  public Blocks.Block block;

  public Action(ActionType actionType, Blocks.Block over, Blocks.Block under) {
    this.actionType = actionType;
    this.over = over;
    this.under = under;
  }

  public Action(ActionType actionType, Blocks.Block block) {
    this.actionType = actionType;
    this.block = block;
  }

  public static enum ActionType {
    PICKUP,
    PUTDOWN,
    UNSTACK,
    STACK,
    NONE,
    NULL
  }

  @Override
  public String toString() {
    return "Action{" +
           "actionType=" + actionType +
           ", over=" + over +
           ", under=" + under +
           ", block=" + block +
           '}';
  }
}
