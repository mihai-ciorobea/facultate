package tester;

import gridworld.AbstractGridEnvironment;
import gridworld.GridPosition;
import gridworld.GridPosition.GridOrientation;
import mas1.Action;
import mas1.Agent;
import mas1.Environment;
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

    public static int n = 10;
    public static int m = 10;

    protected static Agent initializeAgent() {

        // EXAMPLE
        return new Agent() {
            List<Action.ActionType> nextMoves = new ArrayList<>();
            private GridOrientation orientation = GridOrientation.NORTH;
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

                    Action.ActionType turn = orientation == GridOrientation.NORTH ? Action.ActionType.TURN_RIGH : Action.ActionType.TURN_LEFT;
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


                Action action = agentData.agent.getAction(obstacles, this.Jtiles.contains(position));
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

            private AgentData getAgentData() {
                return this.agents.get(0);
            }
        };


        Set<Position> all = new HashSet<Position>();
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                all.add(new GridPosition(i, j));
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
        int xsNr = Math.min(n, m) - 1;
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
        int jsNr = (m * n) / 3;

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
