package tester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import gridworld.AbstractGridEnvironment;
import gridworld.GridPosition;
import gridworld.GridPosition.GridOrientation;
import mas1.Action;
import mas1.Agent;
import mas1.Environment;
import mas1.Position;
import mas1.RelativeOrientation;

public class Tester {

  /**
   * Delay between two agent steps. In milliseconds.
   */
  protected static final int STEP_DELAY = 500;

  public static int n = 10;
  public static int m = 10;

  protected static Environment initializeEnvironment() {

    // EXAMPLE:
    Environment e = new AbstractGridEnvironment() {
      @Override
      public void step() {

        for (int index = 0; index < 4; index++) {

          AgentData agentData = getAgentData(index);
          Position position = agentData.position;

          HashMap<RelativeOrientation, Boolean> obstacles = new HashMap<>();

          for (RelativeOrientation relativeOrientation : GridPosition.GridRelativeOrientation.values()) {
            Position neighbour = position.getNeighborPosition(agentData.orientation, relativeOrientation);
            obstacles.put(relativeOrientation, this.Xtiles.contains(neighbour));
          }

          Action action;
          if (index <= 1) {

            System.out.println("AGENT 1:" + getAgentsArround(index));
            action = agentData.agent.getAction(obstacles, this.Jtiles.contains(position),
                                               agentData.orientation);
          } else {
            System.out.println("AGENT 2:" + getAgentsArroundx2(index));
            action = agentData.agent.getAction(obstacles, this.Jtiles.contains(position));
          }
          System.out.println(action.getType());
          switch (action.getType()) {
            case TURN_RIGH:
              agentData.orientation = ((GridOrientation) agentData.orientation).getNewOrientation(action.getType());
              break;
            case TURN_LEFT:
              agentData.orientation = ((GridOrientation) agentData.orientation).getNewOrientation(action.getType());
              break;
            case PICK:
              this.Jtiles.remove(agentData.position);
              agentData.score += 10;
              break;
            case FORWARD:
              agentData.position = agentData.position.getNeighborPosition(agentData.orientation);
              break;
          }

          agentData.score -= 1 + (double) agentData.agent.getMemory() * 0.1;
          System.out.println("Score: " + agentData.score);
        }
      }

      private ArrayList<GridOrientation> getAgentsArround(int index) {
        GridPosition position = (GridPosition) getAgentData(index).position;
        ArrayList<GridOrientation> result = new ArrayList();

        for (int i = 0; i < 4; i++) {
          if (i == index) {
            continue;
          }

          AgentData agentData = getAgentData(i);
          GridPosition gridPosition = ((GridPosition) agentData.position);

          if (gridPosition.isNeighbor(position)) {
            for (GridPosition.GridOrientation orientation : GridPosition.GridOrientation.values()) {
              if (position.getNeighborPosition(orientation).equals(gridPosition)) {
                result.add(orientation);
              }
            }
          }
        }

        return result;

      }


      private ArrayList<GridOrientation> getAgentsArroundx2(int index) {
        GridPosition position = (GridPosition) getAgentData(index).position;
        ArrayList<GridOrientation> result = new ArrayList();

        for (int i = 0; i < 4; i++) {
          if (i == index) {
            continue;
          }

          AgentData agentData = getAgentData(i);
          GridPosition gridPosition = ((GridPosition) agentData.position);

          if (gridPosition.isNeighbor(position)) {
            for (GridPosition.GridOrientation orientation : GridPosition.GridOrientation.values()) {
              for (GridPosition.GridOrientation orientation2 : GridPosition.GridOrientation.values()) {
                if (position.getNeighborPosition(orientation).getNeighborPosition(orientation2).equals(gridPosition)) {
                  //result.add(orientation);
                  System.out.println(orientation + " " +  orientation2);
                }
              }
            }
          }
        }

        return result;

      }

      private AgentData getAgentData(int index) {
        return this.agents.get(index);
      }
    };

    Set<Position> all = new HashSet<Position>();
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        all.add(new GridPosition(i, j));
      }
    }
    Set<Position> xs = new HashSet<Position>();
    for (int i = 0; i < m; i++) {
      xs.add(new GridPosition(i, 0));
      xs.add(new GridPosition(i, n - 1));
    }

    for (int i = 0; i < n; i++) {

      xs.add(new GridPosition(0, i));
      xs.add(new GridPosition(m - 1, i));
    }
    Random random = new Random();
//    int xsNr = Math.min(n, m) - 1;
    int xsNr = 3;
    while (xsNr != 0) {
      int x = random.nextInt(m);
      int y = random.nextInt(n);
      System.out.println(x + " " + y);

      if (!(xs.contains(new GridPosition(x - 1, y - 1))
            || xs.contains(new GridPosition(x - 1, y))
            || xs.contains(new GridPosition(x - 1, y + 1))
            || xs.contains(new GridPosition(x, y - 1))
            || xs.contains(new GridPosition(x, y))
            || xs.contains(new GridPosition(x, y + 1))
            || xs.contains(new GridPosition(x + 1, y - 1))
            || xs.contains(new GridPosition(x + 1, y))
            || xs.contains(new GridPosition(x + 1, y + 1)))) {
        xs.add(new GridPosition(x, y));
        --xsNr;
      }
    }

    Set<Position> js = new HashSet<Position>();
//    int jsNr = (m * n) / 3;
    int jsNr = 10;

    while (jsNr != 0) {
      int x = random.nextInt(m);
      int y = random.nextInt(n);

      if (!(js.contains(new GridPosition(x, y)) || xs.contains(new GridPosition(x, y)))) {
        js.add(new GridPosition(x, y));
        --jsNr;
      }
    }

    e.initialize(all, js, xs);
    return e;
  }

  public static void main(String[] args) {
    Environment env = initializeEnvironment();
    Agent agent1a = new Agent1(Action.ActionType.TURN_RIGH, Action.ActionType.TURN_LEFT);
    Agent agent1b = new Agent1(Action.ActionType.TURN_LEFT, Action.ActionType.TURN_RIGH);
    Agent agent2a = new Agent2(Action.ActionType.TURN_LEFT, Action.ActionType.TURN_RIGH, GridOrientation.NORTH);
    Agent agent2b = new Agent2(Action.ActionType.TURN_RIGH, Action.ActionType.TURN_LEFT, GridOrientation.SOUTH);

    env.addAgent(agent1a, new GridPosition(1, 1), GridOrientation.NORTH);
    env.addAgent(agent1b, new GridPosition(m - 2, n - 2), GridOrientation.SOUTH);

    env.addAgent(agent2a, new GridPosition(1, n - 2), GridOrientation.SOUTH);
    env.addAgent(agent2b, new GridPosition(m - 2, 1), GridOrientation.NORTH);

    System.out.println(env.printToString());

    while (!env.isClean()) {
      env.step();
      System.out.println(env.printToString());
      try {
        Thread.sleep(STEP_DELAY);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
