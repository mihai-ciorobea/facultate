package tester;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gridworld.GridPosition;
import mas1.Action;
import mas1.Agent;
import mas1.Orientation;
import mas1.RelativeOrientation;

public class Agent2 implements Agent {

  private Action.ActionType turnWhenNorth;
  private Action.ActionType turnWhenSouth;
  private GridPosition.GridOrientation orientation;

  public Agent2(final Action.ActionType turnWhenNorth, final Action.ActionType turnWhenSouth, GridPosition.GridOrientation gridOrientation) {
    this.turnWhenNorth = turnWhenNorth;
    this.turnWhenSouth = turnWhenSouth;
    orientation = gridOrientation;

  }

  List<Action.ActionType> nextMoves = new ArrayList<>();

  //private GridPosition position = new GridPosition(1, 1);


  @Override
  public Action getAction(Map<RelativeOrientation, Boolean> obstacles, boolean isJtile) {

    System.out.println(obstacles);

    if (isJtile) {
      return new Action(Action.ActionType.PICK);
    }

    if (nextMoves.size() != 0) {
      Action.ActionType action = nextMoves.remove(0);
      orientation = orientation.getNewOrientation(action);
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
          orientation == GridPosition.GridOrientation.NORTH ? turnWhenNorth
                                                            : turnWhenSouth;
      nextMoves.add(turn);
      nextMoves.add(Action.ActionType.FORWARD);
      nextMoves.add(turn);

      Action.ActionType action = nextMoves.remove(0);
      orientation = orientation.getNewOrientation(action);

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
      orientation = orientation.getNewOrientation(action);

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
    return "2";
  }


  @Override
  public Action getAction(Map<RelativeOrientation, Boolean> obstacles, boolean isJtile,
                          Orientation absoluteOrientation) {
    throw new RuntimeException();
  }


}
