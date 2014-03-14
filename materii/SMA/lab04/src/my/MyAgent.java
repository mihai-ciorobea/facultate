package my;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
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
    for(int i = 0; i<10; ++i) {
      System.out.print(RANDOM.nextInt(actionList.size()));
    }
    return possibleMoves.indexOf(actionList.get(index));
  }

  private int getScoreForMove(Blocks playGround) {
    LinkedList<Predicate> predicates1 = playGround.toPredicates();
    LinkedList<Predicate> predicates2 = finalState.toPredicates();




    System.out.println("GIGI:");
    System.out.println(playGround);
    int score = 0;

    for (Integer towerId : finalState.towers.keySet()) {
      Deque<Blocks.Block> finalTower = finalState.towers.get(towerId);
      Deque<Blocks.Block> currentTower = playGround.towers.get(towerId);

      //1.  Current state has a blank and goal state has a block.
      //    So increment the heuristic value by the number of blocks needed to be moved into a place.
      if (currentTower == null || currentTower.size() == 0) {
        score += finalTower.size();
      }

      //2.  Current state has a block and goal state has a blank.
      //    So increment the heuristic value by the number of blocks needed to be moved out of a place.
      if (finalTower == null || finalTower.size() == 0) {
        score += currentTower.size();
      }

      //3.  Both states have a block, but they are not the same.
      //    The incorrect block must be moved out and the correct blocks must be moved in. Increment the heuristic value by the number of these blocks.
      if (finalTower != null && currentTower != null) {
        if (finalTower.size() != 0 && currentTower.size() != 0) {
          Object[] finalList =  finalTower.toArray();
          Object[] currentList = currentTower.toArray();

          for(int index = 0 ; index < Math.min(finalList.length, currentList.length); ++index) {

            Blocks.Block finalBlock = (Blocks.Block) finalList[index];
            Blocks.Block currentBlock = (Blocks.Block) currentList[index];

            if (finalBlock != null && currentBlock != null) {
              score += finalBlock.equals(currentBlock) ? 0 : 1;
            } else {
              ++score;
            }
          }

          score += Math.abs(finalList.length - currentList.length);
        }
      }
    }

       score -= intersection(predicates1,predicates2).size();

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



  public <T> List<T> union(List<T> list1, List<T> list2) {
    Set<T> set = new HashSet<T>();

    set.addAll(list1);
    set.addAll(list2);

    return new ArrayList<T>(set);
  }

  public <T> List<T> intersection(List<T> list1, List<T> list2) {
    List<T> list = new ArrayList<T>();

    for (T t : list1) {
      if(list2.contains(t)) {
        list.add(t);
      }
    }

    return list;
  }
}
