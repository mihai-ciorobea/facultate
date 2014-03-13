package my;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import base.Agent;
import blocksworld.Action;
import blocksworld.Blocks;
import blocksworld.Predicate;

public class MyAgent implements Agent {

  private Blocks finalState;
  char agentName;

  public MyAgent(Blocks finalState, char name) {
    this.finalState = finalState;
    agentName = name;
  }

  @Override
  //playGround este doar mediu de pe masa nu si ce tine fiecare / el in brat
  public Action perform(Blocks playGround, Blocks.Block holding, boolean previousActionSucceeded) {

    if (playGround.equals(finalState)) {
      return new Action(Action.ActionType.NULL, null);
    }

    List<Action> possibleMoves;
    if (holding == null) {
      possibleMoves = pickupOrUnStack(playGround);
    } else {
      possibleMoves = putdownOrStack(playGround, holding);
    }

    System.out.println("Possible Moves: " +  possibleMoves);
    int index = getBestMove(playGround, possibleMoves);

    return index == -1 ? new Action(Action.ActionType.NONE, null) : possibleMoves.get(index);
  }

  private int getBestMove(Blocks playGround, List<Action> possibleMoves) {
    HashMap<Integer, Action> moves = new HashMap<>();
    for (Action action : possibleMoves) {
      switch (action.actionType) {
        case STACK:
          playGround.stack(action.over, action.under);
          moves.put(getScoreForMove(playGround), action);
          playGround.unstack(action.over, action.under);
          break;
        case UNSTACK:
          playGround.unstack(action.over, action.under);
          moves.put(getScoreForMove(playGround), action);
          playGround.stack(action.over, action.under);
          break;
        case PICKUP:
          playGround.pickup(action.block);
          moves.put(getScoreForMove(playGround), action);
          playGround.putDown(action.block);

          break;
        case PUTDOWN:
          playGround.putDown(action.block);
          moves.put(getScoreForMove(playGround), action);
          playGround.pickup(action.block);
          break;
      }

    }

    System.out.println(moves.keySet());
    Integer maxScore = Collections.min(moves.keySet());
    return possibleMoves.indexOf(moves.get(maxScore));
  }

  private int getScoreForMove(Blocks playGround) {
    LinkedList<Predicate> predicates1 = playGround.toPredicates();
    LinkedList<Predicate> predicates2 = finalState.toPredicates();

    int score = 0;
//    for (Predicate predicate : predicates1) {
//      if (!predicates2.contains(predicate)) {
//        ++score;
//      }
//    }
//    for (Predicate predicate : predicates2) {
//      if (!predicates1.contains(predicate)) {
//        ++score;
//      }
//    }



     TODO: Euristic
    return score;
  }

  private List<Action> putdownOrStack(Blocks playGround, Blocks.Block holding) {
    List<Action> possibleMoves = playGround.possiblePutDowns(holding);
    possibleMoves.addAll(playGround.possibleStacks(holding));

    return possibleMoves;
  }

  private List<Action> pickupOrUnStack(Blocks playGround) {
    List<Action> possibleMoves = playGround.possiblePickups();
    possibleMoves.addAll(playGround.possibleUnstacks());

    return possibleMoves;
  }

  @Override
  public String statusString() {
    return toString() + ": OK.";
  }

  @Override
  public String toString() {
    return "" + agentName;
  }

}
