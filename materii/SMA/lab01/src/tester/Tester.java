package tester;

import gridworld.AbstractGridEnvironment;
import gridworld.GridPosition;
import gridworld.GridPosition.GridOrientation;
import mas1.Action;
import mas1.Agent;
import mas1.Environment;
import mas1.Orientation;
import mas1.Position;
import mas1.RelativeOrientation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Tester {

    /**
     * Delay between two agent steps. In milliseconds.
     */
    protected static final int STEP_DELAY = 500;

    protected static Agent initializeAgent() {

        // EXAMPLE
        return new Agent() {
            List<Action.ActionType> nextMoves = new ArrayList<>();

            @Override
            public Action getAction(Map<RelativeOrientation, Boolean> obstacles, boolean isJtile,
                                    Orientation absoluteOrientation) {

                System.out.println(obstacles);

                if (isJtile) {
                    return new Action(Action.ActionType.PICK);
                }

                if (nextMoves.size() != 0) {
                    return new Action(nextMoves.remove(0));
                }

                //clear front
                if (obstacles.get(GridPosition.GridRelativeOrientation.FRONT) == false) {
                    return new Action(Action.ActionType.FORWARD);
                }

                //if wall
                if (obstacles.get(GridPosition.GridRelativeOrientation.FRONT) == true
                        && obstacles.get(GridPosition.GridRelativeOrientation.FRONT_LEFT) == true
                        && obstacles.get(GridPosition.GridRelativeOrientation.FRONT_RIGHT) == true) {

                    Action.ActionType turn = absoluteOrientation == GridOrientation.NORTH ? Action.ActionType.TURN_RIGH : Action.ActionType.TURN_LEFT;
                    nextMoves.add(turn);
                    nextMoves.add(Action.ActionType.FORWARD);
                    nextMoves.add(turn);

                    return new Action(nextMoves.remove(0));
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

                    return new Action(nextMoves.remove(0));
                }

                return null;

            }


            @Override
            public String toString() {
                return "1";
            }
        };
    }

    protected static Environment initializeEnvironment() {

        // EXAMPLE:
        Environment e = new AbstractGridEnvironment() {
            @Override
            public void step() {
                AgentData agentData = getAgentData();
                Position position = agentData.position;


                HashMap<RelativeOrientation, Boolean> obstacles = new HashMap<>();

                for (RelativeOrientation relativeOrientation : GridPosition.GridRelativeOrientation.values()) {
                    Position neighbour = position.getNeighborPosition(agentData.orientation, relativeOrientation);
                    obstacles.put(relativeOrientation, this.Xtiles.contains(neighbour));
                }


                Action action = agentData.agent.getAction(obstacles, this.Jtiles.contains(position), agentData.orientation);
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
                        break;
                    case FORWARD:
                        agentData.position = agentData.position.getNeighborPosition(agentData.orientation);
                        break;
                }
            }

            private AgentData getAgentData() {
                return this.agents.get(0);
            }
        };
        Set<Position> all = new HashSet<Position>();
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                all.add(new GridPosition(i, j));
        Set<Position> xs = new HashSet<Position>();
        for (int i = 0; i < 10; i++) {
            xs.add(new GridPosition(0, i));
            xs.add(new GridPosition(9, i));
            xs.add(new GridPosition(i, 0));
            xs.add(new GridPosition(i, 9));
        }

        Random random = new Random();
        int xsNr = random.nextInt(5) + 2;
        while (xsNr != 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
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
        int jsNr = random.nextInt(5) + 5;

        while (jsNr != 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

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
        Agent agent = initializeAgent();
        env.addAgent(agent, new GridPosition(1, 1), GridOrientation.NORTH);

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
