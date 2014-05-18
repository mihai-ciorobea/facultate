package my;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import base.Agent;
import blocksworld.Action;
import blocksworld.Blocks;
import blocksworld.Predicate;

public class MyAgent implements Agent {

  public static final Random RANDOM = new Random();
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

    System.out.println("Possible Moves: " + possibleMoves);
    int index = getBestMove(playGround, possibleMoves);

    return index == -1 ? new Action(Action.ActionType.NONE, null) : possibleMoves.get(index);
  }

  private int getBestMove(Blocks playGround, List<Action> possibleMoves) {
    HashMap<Integer, List<Action>> moves = new HashMap<>();
    for (Action action : possibleMoves) {
      switch (action.actionType) {
        case STACK:
          playGround.stack(action.over, action.under);

          int scoreForMove = getScoreForMove(playGround);
          List list = moves.get(scoreForMove) == null ? new ArrayList() : moves.get(scoreForMove);
          list.add(action);
          moves.put(scoreForMove, list);
          playGround.unstack(action.over, action.under);
          break;
        case UNSTACK:
          playGround.unstack(action.over, action.under);
          scoreForMove = getScoreForMove(playGround);
          list = moves.get(scoreForMove) == null ? new ArrayList() : moves.get(scoreForMove);
          list.add(action);
          moves.put(scoreForMove, list);
          playGround.stack(action.over, action.under);
          break;
        case PICKUP:
          playGround.pickup(action.block);
          scoreForMove = getScoreForMove(playGround);
          list = moves.get(scoreForMove) == null ? new ArrayList() : moves.get(scoreForMove);
          list.add(action);
          moves.put(scoreForMove, list);
          playGround.putDown(action.block);

          break;
        case PUTDOWN:
          playGround.putDown(action.block);
          scoreForMove = getScoreForMove(playGround);
          list = moves.get(scoreForMove) == null ? new ArrayList() : moves.get(scoreForMove);
          list.add(action);
          moves.put(scoreForMove, list);
          playGround.pickup(action.block);
          break;
      }

    }

    System.out.println(moves.keySet());
    Integer maxScore = Collections.min(moves.keySet());
    List<Action> actionList = moves.get(maxScore);
    int index = RANDOM.nextInt(actionList.size());
    System.out.println(index);
    for (int i = 0; i < 10; ++i) {
      System.out.print(RANDOM.nextInt(actionList.size()));
    }
    return possibleMoves.indexOf(actionList.get(index));
  }

  private int getScoreForMove(Blocks playGround) {
    LinkedList<Predicate> playGroundPredicates = playGround.toPredicates();
    LinkedList<Predicate> finalPredicates = finalState.toPredicates();

    int score = 0;

    List<Predicate> commonPredicates = intersection(playGroundPredicates, finalPredicates);

    List<Predicate> dijunction = new ArrayList<>(finalPredicates);
    dijunction.removeAll(commonPredicates);

    List<Blocks.Block> invalidBlocks = new ArrayList<>();
    for (Predicate invalidPredicate : dijunction) {
      if (invalidPredicate.getFirstArgument() != null) {
        invalidBlocks.add(invalidPredicate.getFirstArgument());
      }

      if (invalidPredicate.getSecondArgument() != null) {
        invalidBlocks.add(invalidPredicate.getSecondArgument());
      }

      //scot din intersectie toate Predicatele care se bazeaza pe unul din blocurile invalide
      boolean hasRemoved = true;
      while (hasRemoved) {
        hasRemoved = false;

        Iterator<Predicate> it = commonPredicates.iterator();
        while (it.hasNext()) {
          Predicate commonPredicate = it.next();
          if (invalidBlocks.contains(commonPredicate.getFirstArgument())) {
            it.remove();
            hasRemoved = true;
            invalidBlocks.add(commonPredicate.getFirstArgument());
          }

          if (invalidBlocks.contains(commonPredicate.getSecondArgument())) {
            it.remove();
            hasRemoved = true;
            invalidBlocks.add(commonPredicate.getSecondArgument());
          }
        }
      }
    }

    System.out.println(playGround + "" + commonPredicates.size());
    return invalidBlocks.size();
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


  public <T> List<T> union(List<T> list1, List<T> list2) {
    Set<T> set = new HashSet<T>();

    set.addAll(list1);
    set.addAll(list2);

    return new ArrayList<T>(set);
  }

  public <T> List<T> intersection(List<T> list1, List<T> list2) {
    List<T> list = new ArrayList<T>();

    for (T t : list1) {
      if (list2.contains(t)) {
        list.add(t);
      }
    }

    return list;
  }
}
