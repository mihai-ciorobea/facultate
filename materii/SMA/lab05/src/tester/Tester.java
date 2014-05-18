package tester;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import base.Agent;
import blocksworld.Blocks;
import blocksworld.BlocksWorld;
import my.MyAgent;

/**
 * Class to test the system.
 *
 * @author Andrei Olaru
 */
public class Tester {

  /**
   * Delay between two agent steps. In milliseconds.
   */
  protected static final int STEP_DELAY = 500;

  /**
   * The place to get the tests from.
   */
  protected static final String TEST_SUITE = "tests/2/";

  /**
   * Name for the file containing the initial state of the world.
   */
  protected static final String SI = TEST_SUITE + "si.txt";
  /**
   * Name for the file containing the desired (goal) state of the world for a single agent.
   */
  protected static final String SF = TEST_SUITE + "sf1.txt";
  /**
   * Name for the file containing a desired (goal) state of the world for one of multiple agents agent.
   */
  protected static final String SF1 = TEST_SUITE + "sf1.txt";
  /**
   * Name for the file containing a desired (goal) state of the world for one of multiple agents agent.
   */
  protected static final String SF2 = TEST_SUITE + "sf2.txt";

  /**
   * Main.
   *
   * @param args - unused.
   * @throws FileNotFoundException - if world state file not found.
   * @throws IOException           - if world state file is corrupted.
   */
  public static void main(String[] args) throws FileNotFoundException, IOException {
    BlocksWorld env = null;
    try (InputStream input = new FileInputStream(SI)) {
      env = new BlocksWorld(new Blocks(input));
    }

    List<Agent> agents = new ArrayList<>();
    BlocksWorld finalState = null;
    try (InputStream input = new FileInputStream(SF)) {
      finalState = new BlocksWorld(new Blocks(input));
    }
    agents.add(new MyAgent(finalState.worldState, '1'));
    for (Agent agent : agents) {
      env.addAgent(agent);
    }

    System.out.println(finalState.worldState.equals(env.worldState));

    System.out.println(env.toString());
    System.out.println("\n\n=================================================");

    boolean complete = false;
    while (!complete) {
      complete = env.step();
      try {
        Thread.sleep(0);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(env.toString());
      for (Agent agent : agents) {
        System.out.print(agent.statusString() + "\t\t");
      }
      System.out.println("\n\n=================================================");
    }
  }

}
