package tester;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gridworld.GridPosition;
import mas1.Action;
import mas1.Agent;
import mas1.Orientation;
import mas1.RelativeOrientation;

public class Agent1 implements Agent {

  private Action.ActionType turnWhenNorth;
  private Action.ActionType turnWhenSouth;

  public Agent1(final Action.ActionType turnRigh, final Action.ActionType turnLeft) {
    turnWhenNorth = turnRigh;
    turnWhenSouth = turnLeft;

  }

  // EXAMPLE
  List<Action.ActionType> nextMoves = new ArrayList<>();


  @Override
  public Action getAction(Map<RelativeOrientation, Boolean> obstacles, boolean isJtile) {
    throw new RuntimeException("");
  }

  @Override
  public Action getAction(Map<RelativeOrientation, Boolean> obstacles, boolean isJtile,
                          Orientation absoluteOrientation) {

    System.out.println(obstacles);

    if (isJtile) {
      return new Action(Action.ActionType.PICK);
    }

    if (nextMoves.size() != 0) {
      Action.ActionType action = nextMoves.remove(0);
      return new Action(action);
    }

    //clear front
    if (obstacles.get(GridPosition.GridRelativeOrientation.FRONT) == false) {
      return new Action(Action.ActionType.FORWARD);
    }

    //if wall
    if (obstacles.get(GridPosition.GridRelativeOrientation.FRONT) == true
        && obstacles.get(GridPosition.GridRelativeOrientation.FRONT_LEFT) == true
        && obstacles.get(GridPosition.GridRelativeOrientation.FRONT_RIGHT) == true) {

      Action.ActionType turn =
          absoluteOrientation == GridPosition.GridOrientation.NORTH ? turnWhenNorth
                                                                    : turnWhenSouth;
      nextMoves.add(turn);
      nextMoves.add(Action.ActionType.FORWARD);
      nextMoves.add(turn);

      Action.ActionType action = nextMoves.remove(0);

      return new Action(action);
    }

    //if obstacle
    if (obstacles.get(GridPosition.GridRelativeOrientation.FRONT) == true
        && obstacles.get(GridPosition.GridRelativeOrientation.FRONT_LEFT) == false
        && obstacles.get(GridPosition.GridRelativeOrientation.FRONT_RIGHT) == false) {

      nextMoves.add(Action.ActionType.TURN_RIGH);
      nextMoves.add(Action.ActionType.FORWARD);
      nextMoves.add(Action.ActionType.TURN_LEFT);
      nextMoves.add(Action.ActionType.FORWARD);
      nextMoves.add(Action.ActionType.FORWARD);
      nextMoves.add(Action.ActionType.TURN_LEFT);
      nextMoves.add(Action.ActionType.FORWARD);
      nextMoves.add(Action.ActionType.TURN_RIGH);

      Action.ActionType action = nextMoves.remove(0);

      return new Action(action);
    }

    return null;

  }

  @Override
  public int getMemory() {
    return 1 + //Orientare
           nextMoves.size(); //memoria efectiva ;
  }


  @Override
  public String toString() {
    return "1";
  }

}
